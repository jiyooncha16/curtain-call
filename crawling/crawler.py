import os
import re
import json
import csv
import time
import requests

from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException, StaleElementReferenceException

# =========================
# 설정
# =========================
BASE = "http://www.playdb.co.kr"
LIST_URL = f"{BASE}/playdb/playdblist.asp?sReqMainCategory=000001"
MAX_PAGES = 5

WAIT_SEC = 15
POLITE_SLEEP = 1.2

OUT_JSON = "musicals_full.json"
OUT_CSV = "musicals_full.csv"
FAILED_JSON = "failed_play_no.json"
IMG_DIR = "images"

os.makedirs(IMG_DIR, exist_ok=True)

# =========================
# 유틸
# =========================
def norm_date(s):
    return s.replace("/", "-").strip()

def wait_page(driver, wait):
    wait.until(lambda d: d.execute_script("return document.readyState") == "complete")
    wait.until(EC.presence_of_element_located((By.TAG_NAME, "body")))

def download_image(url, play_no):
    if not url:
        return ""
    try:
        res = requests.get(url, timeout=10)
        if res.status_code == 200:
            path = f"{IMG_DIR}/{play_no}.jpg"
            with open(path, "wb") as f:
                f.write(res.content)
            return path
    except:
        pass
    return ""

# =========================
# Selenium 시작
# =========================
options = Options()
options.add_argument("--start-maximized")
options.add_argument("--disable-gpu")
options.add_argument("--no-sandbox")

driver = webdriver.Chrome(options=options)
wait = WebDriverWait(driver, WAIT_SEC)

print("▶ 크롤링 시작")

# =========================
# 1️⃣ 리스트 페이지 → play_no 수집
# =========================
play_nos = []

for page in range(1, MAX_PAGES + 1):
    url = f"{LIST_URL}&Page={page}"
    print(f"\n▶ 리스트 페이지 {page}")
    driver.get(url)

    try:
        wait_page(driver, wait)
    except TimeoutException:
        print("⚠ 페이지 로딩 실패")
        continue

    time.sleep(POLITE_SLEEP)

    links = driver.find_elements(By.CSS_SELECTOR, "a[onclick^='goDetail']")
    if not links:
        print(f"[WARN] page {page}: detail links 0개")

    for a in links:
        try:
            onclick = a.get_attribute("onclick")
            play_no = onclick.split("'")[1]
            if play_no not in play_nos:
                play_nos.append(play_no)
        except:
            continue

print(f"\n▶ 수집된 공연 수: {len(play_nos)}")

# =========================
# 2️⃣ 상세페이지 크롤링 (재시도 1회)
# =========================
musicals = []
failed = []

def crawl_detail(play_no):
    url = f"{BASE}/playdb/PlaydbDetail.asp?sReqPlayNo={play_no}"
    driver.get(url)
    wait_page(driver, wait)
    time.sleep(POLITE_SLEEP)

    # 제목
    title = driver.find_element(By.CSS_SELECTOR, ".pddetail_subject .title").text.strip()

    # 상세 테이블
    detail = driver.find_element(By.CSS_SELECTOR, ".detaillist")

    # 날짜
    date_text = detail.find_element(
        By.XPATH, ".//tr[td/img[@alt='일시']]/td[2]"
    ).text
    start_date, end_date = map(norm_date, date_text.split("~"))

    # 공연장
    theater = detail.find_element(
        By.XPATH, ".//tr[td/img[@alt='장소']]//a"
    ).text.strip()

    # 포스터 이미지
    img_url = driver.find_element(
        By.CSS_SELECTOR, ".pddetail h2 img"
    ).get_attribute("src")
    image_path = download_image(img_url, play_no)

    # 배우 (역별)
    actors = {}
    role_tables = driver.find_elements(
        By.XPATH, "//table[.//b[contains(text(),'역')]]"
    )

    for table in role_tables:
        role = table.find_element(By.XPATH, ".//b").text.strip()
        names = table.find_elements(By.XPATH, ".//a[contains(@href,'artistdb')]")
        actors[role] = [n.text.strip() for n in names if n.text.strip()]

    return {
        "play_no": play_no,
        "title": title,
        "start_date": start_date,
        "end_date": end_date,
        "theater": theater,
        "image": image_path,
        "actors": actors
    }

for idx, play_no in enumerate(play_nos, 1):
    print(f"\n▶ ({idx}/{len(play_nos)}) 공연 상세: {play_no}")

    success = False
    for attempt in [1, 2]:
        try:
            data = crawl_detail(play_no)
            musicals.append(data)
            print(f"   ✔ 성공 ({attempt}차)")
            success = True
            break
        except Exception as e:
            print(f"   ⚠ 실패 {attempt}차: {type(e).__name__}")
            time.sleep(2)

    if not success:
        failed.append(play_no)

driver.quit()

# =========================
# 저장
# =========================
with open(OUT_JSON, "w", encoding="utf-8") as f:
    json.dump(musicals, f, ensure_ascii=False, indent=2)

with open(FAILED_JSON, "w", encoding="utf-8") as f:
    json.dump(failed, f, ensure_ascii=False, indent=2)

with open(OUT_CSV, "w", encoding="utf-8-sig", newline="") as f:
    writer = csv.writer(f)
    writer.writerow(["play_no", "title", "start_date", "end_date", "theater", "image", "actors"])
    for m in musicals:
        writer.writerow([
            m["play_no"],
            m["title"],
            m["start_date"],
            m["end_date"],
            m["theater"],
            m["image"],
            json.dumps(m["actors"], ensure_ascii=False)
        ])

print("\n✅ DONE")
print(f"- JSON: {OUT_JSON}")
print(f"- CSV : {OUT_CSV}")
print(f"- IMG : {IMG_DIR}/")
print(f"- FAIL: {FAILED_JSON}")
