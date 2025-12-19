<template>
    <div class="wrapper">
        <h3 class="title">게시글 수정하기</h3>
        <BoardForm :title="board.title" :content="board.content" :writer="board.nickname" isEdit @submit="updateBoard"
            @cancel="goBack" />
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import BoardForm from '@/components/common/BoardForm.vue'

const route = useRoute()
const router = useRouter()
const boardId = Number(route.params.id)

const board = ref({})

onMounted(async () => {
    const { data } = await axios.get(`/api/boards/${boardId}`)
    board.value = data
})

const updateBoard = async (payload) => {
    await axios.put(`/api/boards/${boardId}`, payload)
    router.push(`/community/${boardId}`)
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