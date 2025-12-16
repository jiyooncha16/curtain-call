import json
import re
import time
import csv

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

MAX_PAGES = 5
WAIT_SEC = 15
POLITE_SLEEP = 1.0

OUT_JSON = "musicals.json"
OUT_CSV = "musicals.csv"
FAILED_LOG = "failed_play_no.log"


# =========================
# 유틸
# =========================
def wait_body(driver, wait):
    wait.until(lambda d: d.execute_script("return document.readyState") == "complete")
    wait.until(EC.presence_of_element_located((By.TAG_NAME, "body")))


def extract_play_no(onclick):
    try:
        return onclick.split("'")[1]
    except:
        return None


def clean(s):
    return s.strip().replace("\xa0", " ") if s else ""


# =========================
# 메인
# =========================
def main():
    print("▶ 크롤링 시작")

    options = Options()
    options.add_argument("--start-maximized")
    options.add_argument("--disable-gpu")

    driver = webdriver.Chrome(options=options)
    wait = WebDriverWait(driver, WAIT_SEC)

    # -------------------------
    # 1. play_no 수집
    # -------------------------
    play_nos = []

    for page in range(1, MAX_PAGES + 1):
        print(f"▶ 리스트 페이지 {page}")
        driver.get(f"{LIST_URL}&Page={page}")

        try:
            wait_body(driver, wait)
        except TimeoutException:
            print("⚠ 리스트 페이지 타임아웃")
            continue

        time.sleep(POLITE_SLEEP)

        for a in driver.find_elements(By.CSS_SELECTOR, "a[onclick^='goDetail']"):
            try:
                play_no = extract_play_no(a.get_attribute("onclick"))
                if play_no:
                    play_nos.append(play_no)
            except StaleElementReferenceException:
                continue

    play_nos = list(dict.fromkeys(play_nos))
    print(f"▶ play_no 수집 완료: {len(play_nos)}개")

    # -------------------------
    # 2. 공연 상세
    # -------------------------
    musicals = []
    failed = []

    for idx, play_no in enumerate(play_nos, 1):
        print(f"\n▶ ({idx}/{len(play_nos)}) 공연 상세: {play_no}")

        try:
            driver.get(f"{BASE}/playdb/PlaydbDetail.asp?sReqPlayNo={play_no}")
            wait_body(driver, wait)
            time.sleep(POLITE_SLEEP)

            title = clean(driver.title.replace(" - PlayDB", ""))

            html = driver.page_source
            start_date, end_date = "", ""
            m = re.search(
                r"([0-9]{4}\.[0-9]{2}\.[0-9]{2})\s*~\s*([0-9]{4}\.[0-9]{2}\.[0-9]{2})",
                html,
            )
            if m:
                start_date = m.group(1).replace(".", "-")
                end_date = m.group(2).replace(".", "-")

            theater = ""
            try:
                a = driver.find_element(
                    By.XPATH, "//a[contains(@href,'PlacedbInfo.asp')]"
                )
                theater = clean(a.text)
            except:
                pass

            actors = sorted({
                clean(a.text)
                for a in driver.find_elements(
                    By.XPATH, "//a[contains(@href,'artistdb/detail.asp')]"
                )
                if clean(a.text)
            })

            musicals.append({
                "play_no": play_no,
                "title": title,
                "start_date": start_date,
                "end_date": end_date,
                "theater": theater,
                "actors": actors,
            })

            print(f"   ✔ {title}")

        except (TimeoutException, WebDriverException) as e:
            print(f"   ⚠ 실패: {play_no}")
            failed.append(play_no)

    driver.quit()

    # -------------------------
    # 저장
    # -------------------------
    with open(OUT_JSON, "w", encoding="utf-8") as f:
        json.dump(musicals, f, ensure_ascii=False, indent=2)

    with open(OUT_CSV, "w", encoding="utf-8-sig", newline="") as f:
        writer = csv.writer(f)
        writer.writerow([
            "play_no",
            "title",
            "start_date",
            "end_date",
            "theater",
            "actors",
        ])
        for m in musicals:
            writer.writerow([
                m["play_no"],
                m["title"],
                m["start_date"],
                m["end_date"],
                m["theater"],
                ",".join(m["actors"]),
            ])

    with open(FAILED_LOG, "w") as f:
        f.write("\n".join(map(str, failed)))

    print("\n✅ 완료")
    print(f"공연 수: {len(musicals)}")
    print(f"실패 수: {len(failed)}")


if __name__ == "__main__":
    main()
