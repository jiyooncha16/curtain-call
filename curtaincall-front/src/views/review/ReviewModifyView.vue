<template>
    <div class="wrapper">
        <h3 class="title">리뷰 수정하기</h3>
        <ReviewInputForm/> <!--내용물 내려보내기-->
    </div>
</template>

<script setup>
import ReviewInputForm from '@/components/common/ReviewInputForm.vue';
</script>

<style scoped>
    
.title {
  font-weight: bold;
  margin-bottom: 20px;
}

.wrapper {
    width:70%;
    margin: 0 auto;
    margin-top: 20px;
}
</style>