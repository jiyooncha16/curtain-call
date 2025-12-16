# from selenium import webdriver
# from selenium.webdriver.chrome.service import Service
# from selenium.webdriver.chrome.options import Options
# from selenium.webdriver.common.by import By
# from selenium.webdriver.support.ui import WebDriverWait
# from selenium.webdriver.support import expected_conditions as EC
# from webdriver_manager.chrome import ChromeDriverManager
# from bs4 import BeautifulSoup
# import json

# URL = "http://www.playdb.co.kr/playdb/playdblist.asp?sReqMainCategory=000001"

# # ======================
# # Selenium 설정
# # ======================
# options = Options()
# options.add_argument("--start-maximized")

# driver = webdriver.Chrome(
#     service=Service(ChromeDriverManager().install()),
#     options=options
# )

# print("▶ 프로그램 시작")

# # ======================
# # 페이지 접속
# # ======================
# driver.get(URL)

# # ======================
# # 공연 링크 로딩 대기 (핵심!)
# # ======================
# WebDriverWait(driver, 10).until(
#     EC.presence_of_element_located(
#         (By.CSS_SELECTOR, "a[onclick^='goDetail']")
#     )
# )

# print("▶ 공연 링크 로딩 완료")

# # ======================
# # HTML 파싱
# # ======================
# soup = BeautifulSoup(driver.page_source, "html.parser")

# links = soup.select("a[onclick^='goDetail']")
# print("▶ 공연 개수:", len(links))

# result = []

# for a in links:
#     title = a.get_text(strip=True)

#     play_no = a["onclick"].split("'")[1]

#     table = a.find_parent("table")
#     if not table:
#         continue

#     text = table.get_text("\n", strip=True)

#     # 기간
#     try:
#         period = text.split("일시 :")[1].split("\n")[0]
#         start_date, end_date = [d.strip() for d in period.split("~")]
#     except:
#         continue

#     # 장소
#     try:
#         theater = text.split("장소 :")[1].split("\n")[0]
#     except:
#         theater = ""

#     result.append({
#         "play_no": play_no,
#         "title": title,
#         "start_date": start_date.replace(".", "-"),
#         "end_date": end_date.replace(".", "-"),
#         "description": "",
#         "theater": theater,
#         "actors": []
#     })

# driver.quit()

# # ======================
# # JSON 저장
# # ======================
# with open("musicals.json", "w", encoding="utf-8") as f:
#     json.dump(result, f, ensure_ascii=False, indent=2)

# print("✅ 완료! musicals.json 생성됨")


# from selenium import webdriver
# from selenium.webdriver.common.by import By
# from selenium.webdriver.chrome.options import Options
# from selenium.webdriver.support.ui import WebDriverWait
# from selenium.webdriver.support import expected_conditions as EC
# import json
# import time

# # ======================
# # Selenium 설정
# # ======================
# options = Options()
# options.add_argument("--start-maximized")

# driver = webdriver.Chrome(options=options)
# wait = WebDriverWait(driver, 15)

# print("▶ 프로그램 시작")

# # ======================
# # 기존 JSON 로드
# # ======================
# with open("musicals.json", "r", encoding="utf-8") as f:
#     musicals = json.load(f)

# print(f"▶ 공연 개수: {len(musicals)}")

# result = []

# # ======================
# # 상세 페이지 크롤링
# # ======================
# for idx, m in enumerate(musicals, 1):
#     play_no = m["play_no"]
#     detail_url = f"http://www.playdb.co.kr/playdb/PlaydbDetail.asp?sReqPlayNo={play_no}"

#     print(f"\n▶ ({idx}) 상세페이지 진입: {m['title']}")
#     driver.get(detail_url)

#     # 페이지 로딩 대기
#     wait.until(EC.presence_of_element_located((By.TAG_NAME, "body")))
#     time.sleep(1)

#     # ======================
#     # 극장
#     # ======================
#     theater = ""
#     try:
#         theater = driver.find_element(
#             By.XPATH, "//td[contains(text(),'장소')]/following-sibling::td//a"
#         ).text.strip()
#     except:
#         pass
#     # ======================
#     # 배우
#     # ======================
#     actors = []
#     try:
#         actor_elements = driver.find_elements(
#             By.XPATH, "//a[contains(@href,'/artistdb/detail.asp')]"
#         )
#         actors = list({a.text.strip() for a in actor_elements if a.text.strip()})
#     except:
#         pass

#     result.append({
#         "title": m["title"],
#         "start_date": m["start_date"],
#         "end_date": m["end_date"],
#         "description": "",
#         "theater": theater,
#         "actors": actors
#     })

# # ======================
# # 저장
# # ======================
# with open("musicals_full.json", "w", encoding="utf-8") as f:
#     json.dump(result, f, ensure_ascii=False, indent=2)

# driver.quit()

# print("\n✅ 완료! musicals_full.json 생성됨")
# from selenium import webdriver
# from selenium.webdriver.common.by import By
# from selenium.webdriver.chrome.options import Options
# from selenium.webdriver.support.ui import WebDriverWait
# from selenium.webdriver.support import expected_conditions as EC
# import json
# import time

# # ======================
# # Selenium 설정
# # ======================
# options = Options()
# options.add_argument("--start-maximized")

# driver = webdriver.Chrome(options=options)
# wait = WebDriverWait(driver, 15)

# print("▶ 프로그램 시작")

# # ======================
# # 기존 JSON 로드
# # ======================
# with open("musicals.json", "r", encoding="utf-8") as f:
#     musicals = json.load(f)

# print(f"▶ 공연 개수: {len(musicals)}")

# result = []

# # ======================
# # 상세 페이지 크롤링
# # ======================
# for idx, m in enumerate(musicals, 1):
#     play_no = m["play_no"]
#     detail_url = f"http://www.playdb.co.kr/playdb/PlaydbDetail.asp?sReqPlayNo={play_no}"

#     print(f"\n▶ ({idx}) 상세페이지 진입: {m['title']}")
#     driver.get(detail_url)

#     # 페이지 로딩 대기
#     wait.until(EC.presence_of_element_located((By.TAG_NAME, "body")))
#     time.sleep(1)

#     # ======================
#     # 극장 (★★★★★ 핵심)
#     # ======================
#     theater = ""
#     try:
#         theater = driver.find_element(
#             By.XPATH,
#             "//img[@alt='장소']/parent::td/following-sibling::td/a"
#         ).text.strip()
#     except:
#         pass

#     # ======================
#     # 배우
#     # ======================
#     actors = []
#     try:
#         actor_elements = driver.find_elements(
#             By.XPATH,
#             "//a[contains(@href,'/artistdb/detail.asp')]"
#         )
#         actors = list({a.text.strip() for a in actor_elements if a.text.strip()})
#     except:
#         pass

#     result.append({
#         "title": m["title"],
#         "start_date": m["start_date"],
#         "end_date": m["end_date"],
#         "description": "",
#         "theater": theater,
#         "actors": actors
#     })

# # ======================
# # JSON 저장
# # ======================
# with open("musicals_full.json", "w", encoding="utf-8") as f:
#     json.dump(result, f, ensure_ascii=False, indent=2)

# driver.quit()

# print("\n✅ 완료! musicals_full.json 생성됨")
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException, StaleElementReferenceException
import json
import time

# ======================
# 설정
# ======================
BASE_URL = "http://www.playdb.co.kr/playdb/playdblist.asp?sReqMainCategory=000001"
MAX_PAGE = 5
WAIT_TIME = 10
SLEEP_TIME = 1.5

options = Options()
options.add_argument("--start-maximized")

driver = webdriver.Chrome(options=options)
wait = WebDriverWait(driver, WAIT_TIME)

print("▶ 프로그램 시작")

# ======================
# 1단계: 리스트 페이지에서 play_no 수집
# ======================
play_list = []
seen_play_no = set()

for page in range(1, MAX_PAGE + 1):
    print(f"\n▶ 리스트 페이지 {page} 진입")
    driver.get(f"{BASE_URL}&Page={page}")

    try:
        wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, "a[onclick^='goDetail']")))
    except TimeoutException:
        print("⚠ 리스트 로딩 실패, 다음 페이지")
        continue

    time.sleep(SLEEP_TIME)

    links = driver.find_elements(By.CSS_SELECTOR, "a[onclick^='goDetail']")

    for a in links:
        try:
            title = a.text.strip()
            play_no = a.get_attribute("onclick").split("'")[1]

            if play_no in seen_play_no:
                continue

            table = a.find_element(By.XPATH, "./ancestor::table")
            text = table.text

            period = text.split("일시 :")[1].split("\n")[0]
            start_date, end_date = [d.strip().replace(".", "-") for d in period.split("~")]

            play_list.append({
                "play_no": play_no,
                "title": title,
                "start_date": start_date,
                "end_date": end_date
            })

            seen_play_no.add(play_no)

        except Exception:
            continue

print(f"\n▶ 수집된 공연 수: {len(play_list)}")

# ======================
# 상세페이지 크롤링 함수
# ======================
def crawl_detail(play_no):
    url = f"http://www.playdb.co.kr/playdb/PlaydbDetail.asp?sReqPlayNo={play_no}"
    driver.get(url)

    wait.until(EC.presence_of_element_located((By.TAG_NAME, "body")))
    time.sleep(SLEEP_TIME)

    # 극장
    theater = ""
    try:
        theater = driver.find_element(
            By.XPATH, "//td[img[contains(@alt,'장소')]]/following-sibling::td//a"
        ).text.strip()
    except:
        pass

    # 배우 (까렐 스보보다 같은 제작진 제거)
    actors = set()
    actor_elements = driver.find_elements(By.XPATH, "//a[contains(@href,'/artistdb/detail.asp')]")

    for a in actor_elements:
        name = a.text.strip()
        if not name:
            continue
        if any(x in name for x in ["스보보다", "Productions", "Creative"]):
            continue
        actors.add(name)

    return theater, list(actors)

# ======================
# 2단계: 상세페이지 크롤링
# ======================
results = []
failed = []

for idx, item in enumerate(play_list, 1):
    play_no = item["play_no"]
    print(f"\n▶ ({idx}/{len(play_list)}) 상세 진입: {item['title']}")

    try:
        theater, actors = crawl_detail(play_no)

        results.append({
            "title": item["title"],
            "start_date": item["start_date"],
            "end_date": item["end_date"],
            "description": "",
            "theater": theater,
            "actors": actors
        })

    except Exception as e:
        print(f"⚠ 실패: {play_no} → 재시도")
        failed.append(play_no)

# ======================
# 3단계: 실패 재시도 (1회)
# ======================
final_failed = []

for play_no in failed:
    print(f"\n⚠ 재시도: {play_no}")
    try:
        theater, actors = crawl_detail(play_no)

        results.append({
            "title": "",
            "start_date": "",
            "end_date": "",
            "description": "",
            "theater": theater,
            "actors": actors
        })

    except Exception:
        final_failed.append(play_no)

driver.quit()

# ======================
# 저장
# ======================
with open("musicals_full.json", "w", encoding="utf-8") as f:
    json.dump(results, f, ensure_ascii=False, indent=2)

with open("failed_play_no.json", "w", encoding="utf-8") as f:
    json.dump(final_failed, f, ensure_ascii=False, indent=2)

print("\n✅ 완료")
print(f"성공: {len(results)}")
print(f"최종 실패: {len(final_failed)}")

