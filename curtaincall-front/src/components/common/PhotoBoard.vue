<template>
    <div class="container photo-board">
        <div class="wrapper flex space-between" style="padding:5px 10px;">
            <div class="main-text" style="color:white; margin:0 0;">{{obj.title}}</div>
            <div class="main-text" style="color:white; margin:0 0;"><i class="bi bi-arrow-right-square-fill"></i></div>
        </div>
        <div class="box-container">
            <div class="img-wrapper" v-for="img in obj.imgs">
                <img :src="img.src"></img>
            </div>
        </div>
    </div>
</template>

<script setup>
const props = defineProps({
    obj : Object
})
</script>

<style scoped>
.box {
    width : 100%;
    background-color: gray;
}
.box-container {
    width : 100%;
    display : grid;
    grid-template-columns: repeat(3, 1fr);
    margin: 10px 5px;
    gap : 10px;
}
.img-wrapper {
    width: 100%;
    aspect-ratio: 2 / 3;   
    overflow: hidden;      /* 삐져나온 부분 자르기 */
}
.img-wrapper img {
    width: 100%;
    height: 100%;
    object-fit: cover;     /* 중앙 기준 크롭 */
    display: block;
}
.photo-board {
    width : 50%;
}
.wrapper {
    background-color: #5000009e;
    padding : 10px;
    margin : 5px;
    width : 100%;
}
</style>