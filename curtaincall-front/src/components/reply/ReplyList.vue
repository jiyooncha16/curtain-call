<template>
  <div class="reply-wrapper">
    <h3>댓글 {{ replies.length }}</h3>

    <!-- 댓글 작성 -->
    <div class="reply-write">
      <textarea
        v-model="newContent"
        placeholder="댓글을 입력하세요"
      />
      <button @click="createReply">등록</button>
    </div>

    <!-- 상태 -->
    <div v-if="loading">댓글 불러오는 중...</div>
    <div v-else-if="replies.length === 0" class="empty">
      아직 댓글이 없습니다.
    </div>

    <!-- 댓글 목록 -->
    <ReplyItem
      v-for="reply in replies"
      :key="reply.replyId"
      :reply="reply"
      @deleted="fetchReplies"
      @updated="fetchReplies"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import ReplyItem from '@/components/reply/ReplyItem.vue'
import { useAuthStore } from '@/stores/auth'
import api from '@/api/axios'     // 🔥 axios 통일

const auth = useAuthStore()

const props = defineProps({
  boardId: {
    type: Number,
    required: true,
  },
})

/* ===== 상태 ===== */
const replies = ref([])
const newContent = ref('')
const loading = ref(false)

/* ===== 댓글 조회 ===== */
const fetchReplies = async () => {
  loading.value = true
  try {
    const { data } = await api.get(`/api/reply/board/${props.boardId}`)
    replies.value = data || []
  } catch (e) {
    console.error('댓글 조회 실패', e)
  } finally {
    loading.value = false
  }
}

/* ===== 댓글 등록 ===== */
const createReply = async () => {
  if (!newContent.value.trim()) {
    alert('댓글 내용을 입력하세요')
    return
  }

  try {
    await api.post('/api/reply', {
      boardId: props.boardId,
      content: newContent.value,
      userId: Number(auth.userId),
    })

    newContent.value = ''
    fetchReplies()
  } catch (e) {
    console.error(e)
    alert('댓글 등록 실패')
  }
}

/* ===== 라이프사이클 ===== */
onMounted(fetchReplies)

/* 게시글 변경 시 댓글 재조회 */
watch(() => props.boardId, fetchReplies)
</script>

<style scoped>
.reply-wrapper {
  margin-top: 32px;
  padding-top: 16px;
  border-top: 1px solid #eee;
}
.reply-write textarea {
  width: 100%;
  min-height: 80px;
  margin-bottom: 8px;
}
.reply-write button {
  padding: 6px 12px;
}
.empty {
  color: #888;
  padding: 12px 0;
}
</style>
