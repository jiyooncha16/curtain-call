<template>
    <div class="wrapper">
        <h3 class="title">게시글 수정하기</h3>
        <BoardInputForm /> <!--데이터 받기-->
    </div>
</template>

<script setup>
import BoardInputForm from '@/components/common/BoardInputForm .vue';
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