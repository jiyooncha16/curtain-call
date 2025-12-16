import json
import re
import time
from urllib.parse import urljoin, urlparse, parse_qs

from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import (
    TimeoutException,
    StaleElementReferenceException,
    WebDriverException,
)

# =========================
# 설정
# =========================
BASE = "http://www.playdb.co.kr"
LIST_URL = "http://www.playdb.co.kr/playdb/playdblist.asp?sReqMainCategory=000001"
MAX_PAGES = 5               # ✅ 5페이지만
WAIT_SEC = 15
POLITE_SLEEP = 0.7          # ✅ 속도 낮춰서 실패율 감소
RETRY = 2                   # ✅ 실패시 2번 더 (총 3회 시도)

OUT_MUSICALS = "musicals_full.json"
OUT_THEATERS = "theaters.json"
LOG_FAILED_PLAY = "failed_play_no.log"
LOG_FAILED_PLACE = "failed_place_cd.log"


# =========================
# 유틸
# =========================
def write_log(path: str, msg: str):
    with open(path, "a", encoding="utf-8") as f:
        f.write(msg + "\n")


def norm_date(s: str) -> str:
    # "2025.11.27" -> "2025-11-27"
    s = s.strip()
    s = s.replace(".", "-")
    return s


def safe_find_text(driver, xpath: str) -> str:
    try:
        return driver.find_element(By.XPATH, xpath).text.strip()
    except Exception:
        return ""


def extract_place_cd_from_url(url: str):
    # 예: ...PlacedbInfo.asp?PlacecCD=2117
    try:
        q = parse_qs(urlparse(url).query)
        if "PlacecCD" in q and q["PlacecCD"]:
            return q["PlacecCD"][0]
    except:
        pass
    return None


def wait_body(driver, wait: WebDriverWait):
    # body만 기다리면 광고/리다이렉트 때문에 가끔 timeout 날 수 있어서
    # document.readyState도 같이 확인
    wait.until(lambda d: d.execute_script("return document.readyState") == "complete")
    wait.until(EC.presence_of_element_located((By.TAG_NAME, "body")))


def get_detail_play_no_from_onclick(onclick: str):
    # onclick="goDetail('221943')"
    try:
        return onclick.split("'")[1]
    except:
        return None


# =========================
# Selenium 시작
# =========================
options = Options()
options.add_argument("--start-maximized")
# options.add_argument("--headless=new")  # 필요하면 주석 해제
options.add_argument("--disable-gpu")
options.add_argument("--no-sandbox")
options.add_argument("--disable-dev-shm-usage")

driver = webdriver.Chrome(options=options)
wait = WebDriverWait(driver, WAIT_SEC)

print("▶ 시작")


# =========================
# 1) 리스트 페이지들에서 play_no 수집
#    (stale 방지: WebElement 들고 있지 말고 "onclick 문자열"만 뽑기)
# =========================
play_items = []  # [{play_no, title, start_date, end_date, theater_hint}]
seen_play_no = set()

for page in range(1, MAX_PAGES + 1):
    url = f"{LIST_URL}&Page={page}"
    print(f"\n▶ 리스트 페이지 {page} 접속: {url}")
    driver.get(url)

    try:
        wait_body(driver, wait)
    except TimeoutException:
        print("⚠ 리스트 페이지 로딩 타임아웃 -> 다음 페이지로 넘어감")
        continue

    time.sleep(POLITE_SLEEP)

    # 리스트에서 goDetail 링크들
    # 클릭/이동 없이 HTML에서 필요한 값만 추출(=stale 최소화)
    anchors = driver.find_elements(By.CSS_SELECTOR, "a[onclick^='goDetail']")
    print(f"  - 감지된 공연 링크 수: {len(anchors)}")

    # stale 방지: element.text를 바로 쓰지 않고, 필요한 속성/텍스트만 안전하게 뽑기
    for a in anchors:
        try:
            onclick = a.get_attribute("onclick") or ""
            play_no = get_detail_play_no_from_onclick(onclick)
            if not play_no or play_no in seen_play_no:
                continue

            title = (a.text or "").strip()

            # 리스트 카드에서 기간/장소가 같이 있는 경우가 많아서 주변 테이블 텍스트에서 파싱
            theater_hint = ""
            start_date = ""
            end_date = ""

            try:
                table = a.find_element(By.XPATH, "./ancestor::table[1]")
                text = table.text.replace("\r", "\n")
                # "일시 :" 다음 줄에 "YYYY.MM.DD ~ YYYY.MM.DD"
                m = re.search(r"일시\s*:\s*([0-9]{4}\.[0-9]{2}\.[0-9]{2})\s*~\s*([0-9]{4}\.[0-9]{2}\.[0-9]{2})", text)
                if m:
                    start_date = norm_date(m.group(1))
                    end_date = norm_date(m.group(2))

                m2 = re.search(r"장소\s*:\s*(.+)", text)
                if m2:
                    theater_hint = m2.group(1).split("\n")[0].strip()
            except StaleElementReferenceException:
                # 그냥 힌트 없는 상태로 진행
                pass

            play_items.append({
                "play_no": play_no,
                "title": title,
                "start_date": start_date,
                "end_date": end_date,
                "theater_hint": theater_hint,
            })
            seen_play_no.add(play_no)

        except StaleElementReferenceException:
            # 이 경우는 그냥 스킵 (다음 요소로)
            continue

    time.sleep(POLITE_SLEEP)

print(f"\n▶ 수집된 play_no 총개수(중복제거): {len(play_items)}")


# =========================
# 2) 공연 상세페이지 크롤링 + 공연장 링크 수집
# =========================
musical_map = {}   # play_no -> musical dict
theater_map = {}   # place_cd -> theater dict


def crawl_play_detail(play_no: str):
    """
    공연 상세 페이지에서:
    - title (보정)
    - period (start/end)
    - theater name + place_cd + place_url
    - actors(출연진 링크 텍스트)
    """
    detail_url = f"{BASE}/playdb/PlaydbDetail.asp?sReqPlayNo={play_no}"

    for attempt in range(RETRY + 1):
        try:
            driver.get(detail_url)
            wait_body(driver, wait)
            time.sleep(POLITE_SLEEP)

            # 제목: 페이지 상단 타이틀이 더 정확한 경우가 많음
            # 여러 케이스 대비: h3/h2/strong 등
            title = ""
            for xp in [
                "//div[@id='wrap']//td[@class='title']/h3",
                "//div[@id='wrap']//h3",
                "//div[@id='wrap']//h2",
                "//title",
            ]:
                title = safe_find_text(driver, xp)
                if title:
                    break
            title = title.replace(" - PlayDB", "").strip()

            # 기간/장소: "일시" / "장소" 라벨 옆 td
            # 사이트 구조가 일정치 않아서 fallback 여러개
            period_text = ""
            for xp in [
                "//td[contains(text(),'일시')]/following-sibling::td",
                "//th[contains(text(),'일시')]/following-sibling::td",
            ]:
                period_text = safe_find_text(driver, xp)
                if period_text:
                    break

            start_date = ""
            end_date = ""
            m = re.search(r"([0-9]{4}\.[0-9]{2}\.[0-9]{2})\s*~\s*([0-9]{4}\.[0-9]{2}\.[0-9]{2})", period_text)
            if m:
                start_date = norm_date(m.group(1))
                end_date = norm_date(m.group(2))

            # 공연장 링크(PlaceDB): PlacedbInfo.asp?PlacecCD=xxxx
            theater_name = ""
            place_url = ""
            place_cd = None

            try:
                place_a = driver.find_element(By.XPATH, "//a[contains(@href,'/placedb/PlacedbInfo.asp?PlacecCD=')]")
                theater_name = (place_a.text or "").strip()
                place_url = place_a.get_attribute("href") or ""
                place_cd = extract_place_cd_from_url(place_url)
            except Exception:
                # 링크가 없으면 그냥 빈값
                pass

            # 출연진: artistdb/detail.asp 링크들
            actors = []
            try:
                actor_els = driver.find_elements(By.XPATH, "//a[contains(@href,'/artistdb/detail.asp')]")
                # 중복 제거 + 공백 제거
                tmp = []
                for el in actor_els:
                    try:
                        t = (el.text or "").strip()
                        if t:
                            tmp.append(t)
                    except StaleElementReferenceException:
                        continue
                actors = sorted(list(set(tmp)))
            except Exception:
                actors = []

            return {
                "play_no": play_no,
                "title": title,
                "start_date": start_date,
                "end_date": end_date,
                "description": "",
                "theater": theater_name,
                "place_cd": place_cd,
                "place_url": place_url,
                "actors": actors
            }

        except TimeoutException:
            if attempt < RETRY:
                print(f"⚠ 공연 상세 타임아웃 재시도({attempt+1}/{RETRY}) play_no={play_no}")
                time.sleep(1.2)
                continue
            raise
        except WebDriverException:
            if attempt < RETRY:
                print(f"⚠ WebDriver 에러 재시도({attempt+1}/{RETRY}) play_no={play_no}")
                time.sleep(1.2)
                continue
            raise


def crawl_theater_detail(place_cd: str, place_url: str):
    """
    공연장 상세 페이지에서 theater DB용 필드:
    - theater(이름)
    - address(도로명 우선, 없으면 주소)
    - phone
    - seats(최대 좌석수 1개로 저장: 여러 홀 중 가장 큰 숫자)
    - parking(자가용/주차 텍스트 있으면 True)
    - subway(지하철 역까지만: '6호선 한강진역' 같은 패턴)
    """
    url = place_url or f"{BASE}/placedb/PlacedbInfo.asp?PlacecCD={place_cd}"

    for attempt in range(RETRY + 1):
        try:
            driver.get(url)
            wait_body(driver, wait)
            time.sleep(POLITE_SLEEP)

            # 공연장 이름: id=Keyword td title 속성에도 있고, 본문 font에도 있음
            theater = safe_find_text(driver, "//td[@id='Keyword']/table//font")  # 종종 여기
            if not theater:
                try:
                    theater = driver.find_element(By.ID, "Keyword").get_attribute("title") or ""
                    theater = theater.strip()
                except:
                    theater = ""
            theater = theater.strip()

            # 주소: 도로명주소 우선, 없으면 주소
            road = safe_find_text(driver, "//td[contains(text(),'도로명주소')]")
            addr = safe_find_text(driver, "//td[contains(text(),'주소:')]")
            address = (road or addr).strip()
            address = address.replace("도로명주소:", "").replace("주소:", "").strip()

            # 연락처
            phone = safe_find_text(driver, "//td[contains(text(),'연락처')]")
            phone = phone.replace("연락처:", "").strip()

            # 좌석: Tab0 좌석정보 영역에 "xxxx석" 여러개 있을 수 있음 -> 최댓값
            seats = 0
            try:
                seat_area = safe_find_text(driver, "//table[@id='Tab0']")
                nums = [int(x) for x in re.findall(r"(\d+)\s*석", seat_area)]
                if nums:
                    seats = max(nums)
            except:
                seats = 0

            # 교통정보 텍스트(지하철/버스/자가용)
            traffic = safe_find_text(driver, "//td[.//strong[contains(text(),'지하철')]]")  # strong 포함 td
            if not traffic:
                # fallback: 페이지 전체에서 '지하철 이용시' 근처를 잡기
                traffic = safe_find_text(driver, "//td[contains(.,'지하철 이용시')]")

            # subway: "6호선 한강진역" 형태만 뽑기 (역까지만!)
            subway = ""
            m = re.search(r"(\d+호선)\s*([가-힣]+역)", traffic)
            if m:
                subway = f"{m.group(1)} {m.group(2)}"

            # parking: 자가용/주차 단어 있으면 True
            parking = False
            if ("자가용" in traffic) or ("주차" in traffic):
                parking = True

            return {
                "place_cd": place_cd,
                "theater": theater,
                "address": address,
                "phone": phone,
                "parking": parking,
                "seats": seats,
                "subway": subway
            }

        except TimeoutException:
            if attempt < RETRY:
                print(f"⚠ 공연장 타임아웃 재시도({attempt+1}/{RETRY}) place_cd={place_cd}")
                time.sleep(1.2)
                continue
            raise
        except WebDriverException:
            if attempt < RETRY:
                print(f"⚠ 공연장 WebDriver 에러 재시도({attempt+1}/{RETRY}) place_cd={place_cd}")
                time.sleep(1.2)
                continue
            raise


# =========================
# 실제 크롤링 루프
# =========================
for idx, item in enumerate(play_items, 1):
    play_no = item["play_no"]
    print(f"\n▶ ({idx}/{len(play_items)}) 공연 상세: play_no={play_no}")

    # 공연 상세 크롤링
    try:
        play_data = crawl_play_detail(play_no)

        # 리스트에서 얻은 값이 있고 상세에서 비었으면 보완
        if not play_data["title"]:
            play_data["title"] = item["title"]
        if not play_data["start_date"]:
            play_data["start_date"] = item["start_date"]
        if not play_data["end_date"]:
            play_data["end_date"] = item["end_date"]
        if not play_data["theater"]:
            play_data["theater"] = item["theater_hint"]

        # ✅ 중복 공연 합치기: play_no 기준
        if play_no in musical_map:
            # 배우 합치기
            old = musical_map[play_no]
            old_actors = set(old.get("actors", []))
            new_actors = set(play_data.get("actors", []))
            old["actors"] = sorted(list(old_actors | new_actors))

            # theater/날짜가 비어있던 걸 채워주기
            for k in ["title", "start_date", "end_date", "theater", "place_cd", "place_url"]:
                if not old.get(k) and play_data.get(k):
                    old[k] = play_data[k]
        else:
            musical_map[play_no] = play_data

        # 공연장 크롤링 (place_cd 있을 때만)
        place_cd = play_data.get("place_cd")
        place_url = play_data.get("place_url")

        if place_cd and (place_cd not in theater_map):
            print(f"   ↳ 공연장 상세 수집: place_cd={place_cd}")
            try:
                tdata = crawl_theater_detail(place_cd, place_url)
                theater_map[place_cd] = tdata
            except Exception as e:
                print(f"   ⚠ 공연장 실패: place_cd={place_cd} ({type(e).__name__})")
                write_log(LOG_FAILED_PLACE, place_cd)

    except Exception as e:
        print(f"⚠ 공연 실패: play_no={play_no} ({type(e).__name__})")
        write_log(LOG_FAILED_PLAY, play_no)
        continue

    time.sleep(POLITE_SLEEP)

driver.quit()


# =========================
# JSON 저장
# =========================
musicals = []
for play_no, m in musical_map.items():
    musicals.append({
        "play_no": play_no,
        "title": m.get("title", ""),
        "start_date": m.get("start_date", ""),
        "end_date": m.get("end_date", ""),
        "description": m.get("description", ""),
        "theater": m.get("theater", ""),
        "actors": m.get("actors", []),
        "place_cd": m.get("place_cd", None),
    })

theaters = list(theater_map.values())

with open(OUT_MUSICALS, "w", encoding="utf-8") as f:
    json.dump(musicals, f, ensure_ascii=False, indent=2)

with open(OUT_THEATERS, "w", encoding="utf-8") as f:
    json.dump(theaters, f, ensure_ascii=False, indent=2)

print("\n✅ 완료")
print(f" - 공연: {OUT_MUSICALS} ({len(musicals)}개)")
print(f" - 공연장: {OUT_THEATERS} ({len(theaters)}개)")
print(f" - 실패 공연 로그: {LOG_FAILED_PLAY}")
print(f" - 실패 공연장 로그: {LOG_FAILED_PLACE}")
