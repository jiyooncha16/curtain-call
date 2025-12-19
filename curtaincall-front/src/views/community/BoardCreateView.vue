<template>
    <div class="wrapper">
        <h3 class="title">게시글 작성하기</h3>
        <BoardForm :writer="loginUser.nickname" @submit="createBoard" @cancel="goBack" />
    </div>
</template>


<script setup>
import { useRouter } from 'vue-router'
import axios from 'axios'
import BoardForm from '@/components/common/BoardForm.vue'

const router = useRouter()

const loginUser = {
    userId: 1,
    nickname: '지니', // 임시
}

const createBoard = async (payload) => {
    await axios.post('/api/boards', {
        ...payload,
        userId: loginUser.userId,
    })
    router.push('/community')
}

const goBack = () => router.back()
</script>


<style scoped>
.title {
    font-weight: bold;
    margin-bottom: 20px;
}

.wrapper {
    width: 70%;
    margin: 0 auto;
    margin-top: 20px;
}
</style>
