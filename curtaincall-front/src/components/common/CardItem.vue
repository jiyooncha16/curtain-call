<template>
    <div class="poster">
        <div class="img-box">
            <img src="@/assets/데스노트.jpg">
        </div>
        <div class="basic-text">데스노트</div>
    </div>
</template>

<script setup>

</script>

<style scoped>
.img-box {
    width: 100%;
    aspect-ratio: 3/4; /*비율*/
}
img {
    width: 100%;
    height: 100%;
    object-fit:cover;
}
.poster {
    display: flex;
    flex-direction: column;
    align-items: center;
}

</style>