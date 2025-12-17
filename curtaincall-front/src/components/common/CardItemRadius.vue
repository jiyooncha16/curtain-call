<template>
    <div class="poster container">
        <div class="img-box">
            <img src="@/assets/홍광호.jpg">
        </div>
        <div class="basic-text">홍광호</div>
    </div>
</template>

<script setup>

</script>

<style scoped>
.img-box {
    width: 45%;
    aspect-ratio: 1/1;
    border-radius: 50%;
    overflow: hidden;
}
img {
    width: 100%;
    height: 100%;
    object-fit:cover;
    
}
.poster {
  display: flex;
  flex-direction: column;  /* 이미지 위 / 이름 아래 */
  align-items: center;
}

</style>