function loadHTML(id, url) {
    fetch(url)
        .then(response => response.text())
        .then(data => {
            document.getElementById(id).innerHTML = data;
        })
        .catch(err => console.error(`Failed to load ${url}:`, err));
}

// main.html 기준으로 layout 폴더 안의 파일 불러오기
loadHTML("header", "layout/header.html");
loadHTML("footer", "layout/footer.html");
