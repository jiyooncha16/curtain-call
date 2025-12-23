<template>
  <div class="reply-item">
    <div class="top">
      <div class="writer">{{ reply.nickname }}</div>
      <div class="date">{{ reply.createDate }}</div>
    </div>

    <!-- 일반 보기 -->
    <div v-if="!editing" class="content">
      {{ reply.content }}
    </div>

    <!-- 수정 모드 -->
    <div v-else class="edit-box">
      <textarea v-model="editContent" />
      <button @click="updateReply">저장</button>
      <button @click="cancelEdit">취소</button>
    </div>

    <!-- 🔥 본인 댓글만 -->
    <div class="actions" v-if="isOwner">
      <button @click="startEdit">수정</button>
      <button class="danger" @click="deleteReply">삭제</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useAuthStore } from '@/stores/auth'
import api from '@/api/axios'   // 🔥 axios 통일

const props = defineProps({
  reply: {
    type: Object,
    required: true,
  },
})

const emit = defineEmits(['deleted', 'updated'])

/* ===== 상태 ===== */
const editing = ref(false)
const editContent = ref('')

/* 🔥 props 바뀌면 내용 동기화 */
watch(
  () => props.reply,
  (newReply) => {
    editContent.value = newReply.content
  },
  { immediate: true }
)

/* ===== 로그인 유저 ===== */
const auth = useAuthStore()

// 🔥 내 댓글인지 판단
const isOwner = computed(() => {
  return String(props.reply.userId) === String(auth.userId)
})

/* ===== 이벤트 ===== */
const startEdit = () => {
  editing.value = true
  editContent.value = props.reply.content
}

const cancelEdit = () => {
  editing.value = false
  editContent.value = props.reply.content
}

const updateReply = async () => {
  if (!editContent.value.trim()) {
    alert('내용을 입력하세요')
    return
  }

  try {
    await api.put(`/api/reply/${props.reply.replyId}`, {
      content: editContent.value,
    })
    editing.value = false
    emit('updated')
  } catch (e) {
    console.error(e)
    alert('댓글 수정 실패')
  }
}

const deleteReply = async () => {
  if (!confirm('댓글을 삭제하시겠습니까?')) return

  try {
    await api.delete(`/api/reply/${props.reply.replyId}`)
    emit('deleted')
  } catch (e) {
    console.error(e)
    alert('댓글 삭제 실패')
  }
}
</script>

<style scoped>
.reply-item {
  padding: 12px 0;
  border-bottom: 1px solid #eee;
}
.top {
  font-size: 13px;
  color: #666;
}
.content {
  margin: 6px 0;
}
.edit-box textarea {
  width: 100%;
  min-height: 60px;
}
.actions {
  display: flex;
  gap: 8px;
}
.danger {
  color: #c40000;
}
</style>
