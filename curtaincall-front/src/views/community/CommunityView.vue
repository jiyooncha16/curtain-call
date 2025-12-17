<template>
    <div>
        <div class="board-wrapper">
            <h3>일반게시판</h3>
            <BoardList/>
        </div>
        
        <div class="board-wrapper">
            <h3>판매게시판</h3>
            <BoardList/>
        </div>
    </div>
</template>

<script setup>
import BoardList from '@/components/community/BoardList.vue';


</script>

<style scoped>
.wrapper {
    margin: 30px 0;
    gap:10px;
}
</style>