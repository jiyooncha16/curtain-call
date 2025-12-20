import json
import os
import time
import requests
from urllib.parse import urlparse, parse_qs

from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By

# =========================
# 설정
# =========================
PLAY_NOS = [
    "223304", "222249", "215706", "221943", "223095",
    "218253", "221340", "221845", "223431", "222168",
    "222401", "222417", "222879", "223647", "222143",
    "221657", "223248", "224441", "221711", "224123",
    "215082", "223187", "223375", "223149", "224042",
    "215834", "223023", "223074", "222141", "225213",
    "220151", "223461", "221495", "223317", "197610",
    "213300", "223249", "223099", "223166", "212740",
    "221045", "216921", "224290", "205713", "224888",
    "222971", "104328", "188624", "223908", "217377",
    "197745", "224618", "180502", "196009", "215866",
    "223208", "224491", "225023", "208552", "219832",
    "220887", "220909", "222482", "222844", "223487",
    "175240",
]

OUT_JSON = "actors.json"
IMG_DIR = "actor_images"
BASE = "http://www.playdb.co.kr"
WAIT = 0.7
RETRY = 1

os.makedirs(IMG_DIR, exist_ok=True)

# =========================
# Selenium 설정
# =========================
options = Options()
options.add_argument("--disable-gpu")
options.add_argument("--no-sandbox")
driver = webdriver.Chrome(options=options)

# =========================
# retry
# =========================
def retry(func):
    def wrapper(*args, **kwargs):
        for i in range(RETRY + 1):
            try:
                return func(*args, **kwargs)
            except Exception as e:
                print(f"⚠️ {func.__name__} 실패 {i+1}: {e}")
                time.sleep(1)
        return None
    return wrapper

# =========================
# 출연진 ManNo
# =========================
@retry
def extract_actor_mannos(play_no):
    driver.get(f"{BASE}/playdb/playdbDetail.asp?sReqPlayno={play_no}")
    time.sleep(WAIT)

    mannos = set()
    links = driver.find_elements(By.XPATH, "//a[contains(@href,'ManNo=')]")

    for a in links:
        href = a.get_attribute("href")
        qs = parse_qs(urlparse(href).query)
        if "ManNo" in qs:
            mannos.add(qs["ManNo"][0])

    return list(mannos)

# =========================
# 배우 기본 정보
# =========================
@retry
def parse_actor_basic(manno):
    driver.get(f"{BASE}/artistdb/detail.asp?ManNo={manno}")
    time.sleep(WAIT)

    def safe(xpath):
        try:
            return driver.find_element(By.XPATH, xpath).text.strip()
        except:
            return ""

    name = safe("//span[@class='title']")
    birth = safe("//dt[strong[text()='생년월일']]/following-sibling::dd[1]")
    agency = safe("//dt[strong[text()='소속사']]/following-sibling::dd[1]")

    sns = []
    try:
        sns = [a.get_attribute("href") for a in driver.find_elements(By.XPATH, "//dd[@class='pssite']//a")]
    except:
        pass

    img_path = ""
    try:
        img_url = driver.find_element(By.ID, "manImage").get_attribute("src")
        ext = img_url.split(".")[-1].split("?")[0]
        img_path = os.path.join(IMG_DIR, f"{manno}.{ext}")
        if not os.path.exists(img_path):
            r = requests.get(img_url, timeout=10)
            with open(img_path, "wb") as f:
                f.write(r.content)
    except:
        pass

    return {
        "actor_id": manno,
        "name": name,
        "birth": birth,
        "agency": agency,
        "sns": sns,
        "profile_image": img_path
    }

# =========================
# 출연작 TOP 5
# =========================
@retry
def parse_actor_works_top5():
    works = []
    tables = driver.find_elements(
        By.XPATH,
        "//div[@class='detail_contentsbox'][.//img[contains(@alt,'출연작품')]]//table[@width='320']"
    )[:5]

    for t in tables:
        def safe(x):
            try:
                return t.find_element(By.XPATH, x).text.strip()
            except:
                return ""

        works.append({
            "title": safe(".//td[@class='ptitle']"),
            "period": safe(".//td[@class='time']"),
            "theater": safe(".//td[@class='small']"),
            "role": safe(".//tr[last()]")
        })

    return works

# =========================
# 실행
# =========================
actors = {}

for i, play_no in enumerate(PLAY_NOS, 1):
    print(f"\n🎬 [{i}/{len(PLAY_NOS)}] 공연 {play_no}")
    mannos = extract_actor_mannos(play_no) or []

    for manno in mannos:
        if manno in actors:
            continue

        print(f"  👤 배우 {manno}")
        basic = parse_actor_basic(manno)
        works = parse_actor_works_top5()

        actors[manno] = {
            **basic,
            "works_top5": works
        }

with open(OUT_JSON, "w", encoding="utf-8") as f:
    json.dump(list(actors.values()), f, ensure_ascii=False, indent=2)

driver.quit()

print("\n✅ 완료")
print(f"총 배우 수: {len(actors)}")
print(f"JSON: {OUT_JSON}")
print(f"이미지 폴더: {IMG_DIR}")
