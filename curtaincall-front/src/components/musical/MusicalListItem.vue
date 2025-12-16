<template>
    <div class="searchResultItem flex">
        <div class="img-box">
            <img src="@/assets/데스노트.jpg">
        </div>
        <div class="container">
            <div class="main-text">위키드</div>
            <div class="basic-text">2025.10.12 - 2026.01.01</div>
            <div class="basic-text">세종문화회관</div>
            <div class="basic-text">은혜민, 조병국, ...</div>
            <div class="basic-text"><i class="bi bi-heart-fill"></i> 40</div>
        </div>
    </div>
</template>

<script setup>

</script>

<style scoped>
.searchResultItem {
    border: 1px #800000 solid;
    border-radius: 5px;
    margin: 10px 0;
}
.img-box {
    width: 150px;
    aspect-ratio: 3/4; /*비율*/
}
img {
    width: 100%;
    height: 100%;
    object-fit:cover;
}
</style>