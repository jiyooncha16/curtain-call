<template>
    <div class="flex video-container">
        <div class="img-box">
            <img src="@/assets/thumbnail_데스노트.jpg"></img>
        </div>
        <div class="basic-text">뮤지컬 데스노트 MV_Death Note(홍광호)</div>
    </div>
</template>

<script setup>

</script>

<style scoped>

.img-box {
    width: 100%;
    max-width: 400px;
    aspect-ratio: 5 / 3;   
    overflow: hidden;      /* 삐져나온 부분 자르기 */
    flex-shrink: 0;       /* flex에서 줄어들지 않게 */
}
.img-box img {
    width: 100%;
    height: 100%;
    object-fit: cover;     /* 중앙 기준 크롭 */
    display: block;
}
.video-container {
    flex-direction: column;
    align-items:center;
}
</style>