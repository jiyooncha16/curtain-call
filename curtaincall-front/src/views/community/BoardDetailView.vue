<template>
  <div class="page">
    <h2>게시글 상세</h2>

    <div v-if="loading">불러오는 중...</div>
    <div v-else-if="error" class="error">{{ error }}</div>

    <!-- 🔥 핵심 수정 -->
    <div v-else-if="board" class="card">
      <div class="header">
        <h3 class="title">{{ board.title }}</h3>
        <span class="category">{{ board.category }}</span>
      </div>

      <div class="meta">
        <span>작성자 {{ board.userId }}</span>
        <span>·</span>
        <span>{{ board.createDate }}</span>
      </div>

      <hr />

      <pre class="content">{{ board.content }}</pre>
      <hr/>
      <ReplyList v-if="board" :boardId="boardId" />

      <div class="actions">
        <button @click="goBack">목록</button>
        <button @click="goEdit">수정</button>
        <button class="danger" @click="removeBoard">삭제</button>
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

// 🔐 로그인 유저
const loginUserId = Number(localStorage.getItem('loginUserId'))

// 🔥 게시글 주인인지
const isOwner = computed(() => {
  return board.value && board.value.userId === loginUserId
})

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

const goBack = () => router.back()

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



<style scoped>
.page {
  max-width: 900px;
  margin: auto;
  padding: 24px;
}
.card {
  border: 1px solid #eee;
  border-radius: 14px;
  padding: 20px;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.title {
  font-size: 22px;
}
.category {
  font-size: 13px;
  color: #666;
}
.meta {
  font-size: 13px;
  color: #777;
  margin-top: 4px;
}
.content {
  white-space: pre-wrap;
  margin-top: 16px;
}
.actions {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}
.danger {
  color: #c40000;
}
.error {
  color: red;
}
</style>
