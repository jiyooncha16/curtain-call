<template>
    <div class="searchResultItem flex">
        <div class="img-box">
            <img src="@/assets/데스노트.jpg">
        </div>
        <div class="container">
            <div class="main-text">{{musical.title}}</div>
            <div class="basic-text">{{musical.startDate}} - {{musical.endDate}}</div>
            <div class="basic-text">{{musical.theater}}</div>
            <!-- <div class="basic-text">{{actors}}</div> -->
            <!-- <div class="basic-text"><i class="bi bi-heart-fill"></i> {{like}}</div> -->
        </div>
    </div>
</template>

<script setup>
  const props = defineProps ({
        musical: Object
    })
</script>

<style scoped>
.searchResultItem {
    border: 2px lightgray solid;
    border-radius: 5px;
    margin: 10px 0;
    overflow: hidden;
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