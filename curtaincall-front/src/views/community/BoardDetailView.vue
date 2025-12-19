<template>
    <div class="page">

        <div v-if="loading">불러오는 중...</div>
        <div v-else-if="error" class="error">{{ error }}</div>

        <div v-else-if="board" class="card">
            <div class="header">
                <h3 class="title">{{ board.title }}</h3>
                <div class="category">{{ board.category }}</div>
            </div>

            <div class="meta">
                <div> {{ board.nickname }}</div>
                <div>{{ board.createDate }}</div>
            </div>

            <hr />

            <pre class="content">{{ board.content }}</pre>

            <hr />

            <!-- 댓글 -->
            <ReplyList :boardId="boardId" />

            <div class="actions">
                <button @click="goBack">목록</button>

                <!-- 작성자만 수정/삭제 -->
                <button @click="goEdit">수정</button>
                <!-- <button v-if="isOwner" @click="goEdit">수정</button> -->
                <button class="danger" @click="removeBoard">
                    삭제
                </button>
                <!-- <button v-if="isOwner" class="danger" @click="removeBoard">
                    삭제
                </button> -->
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import ReplyList from '@/components/reply/ReplyList.vue'

const route = useRoute()
const router = useRouter()

const boardId = Number(route.params.id)
const board = ref(null)
const loading = ref(false)
const error = ref('')

// 임시 로그인 유저
const loginUserId = Number(localStorage.getItem('loginUserId')) || 1

const isOwner = computed(() =>
    board.value && board.value.userId === loginUserId
)

const fetchBoard = async () => {
    loading.value = true
    try {
        const { data } = await axios.get(`/api/boards/${boardId}`)
        board.value = data
    } catch (e) {
        error.value = '게시글을 불러올 수 없습니다.'
    } finally {
        loading.value = false
    }
}

onMounted(fetchBoard)

const goBack = () => router.push('/community')

const goEdit = () => {
    router.push({ name: 'boardModify', params: { id: boardId } })
}

const removeBoard = async () => {
    if (!confirm('게시글을 삭제하시겠습니까?')) return
    await axios.delete(`/api/boards/${boardId}`)
    alert('삭제 완료')
    goBack()
}
</script>
