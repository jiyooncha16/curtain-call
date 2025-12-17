<template>
    <div class="wrapper">
        <h3 class="title">리뷰 작성하기</h3>
        <ReviewInputForm/>
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