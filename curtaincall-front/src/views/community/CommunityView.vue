<template>
    <div>
        <div class="board-wrapper">
            <div class="title-text">일반게시판</div>
            <BoardList :list="boardList"/>
        </div>
        
        <div class="board-wrapper">
            <div class="title-text">판매게시판</div>
            <BoardList :list="sellList"/>
        </div>
    </div>
</template>

<script setup>
import BoardList from '@/components/community/BoardList.vue';
import axios from 'axios';
import { onMounted, ref } from 'vue';

const boardList = ref([])
const sellList = ref([])
onMounted(() => {
    axios.get(`/api/boards/search`, {
        params : {
            category: "free"
        }
    })
    .then((res) => {
        console.log("일반게시판 : " + res.data)
        boardList.value = res.data
    })
})
onMounted(() => {
    axios.get(`/api/boards/search`, {
        params : {
            category: "deal"
        }
    })
    .then((res) => {
        console.log("판매게시판 : " + res.data)
        sellList.value = res.data
    })
})
</script>

<style scoped>
.wrapper {
    margin: 30px 0;
    gap:10px;
}
.board-wrapper {
    margin-top: 30px;
    margin-bottom:70px ;
}
</style>