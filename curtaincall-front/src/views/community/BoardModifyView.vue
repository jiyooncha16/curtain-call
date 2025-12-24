<template>
  <div class="page">
    <div class="topbar">
      <button class="icon-btn" @click="goBack">
        <i class="bi bi-arrow-left"></i>
      </button>
      <div class="topbar-title">게시글 수정</div>
      <div></div>
    </div>

    <section class="card">
      <!-- 카테고리 -->
      <div class="field">
        <label class="label">카테고리</label>
        <select v-model="board.category" class="select">
          <option value="free">자유</option>
          <option value="deal">판매</option>
        </select>
      </div>

      <!-- 제목 -->
      <div class="field">
        <label class="label">제목</label>
        <input v-model="board.title" class="input" />
      </div>

      <!-- 내용 -->
      <div class="field">
        <label class="label">내용</label>
        <textarea v-model="board.content" class="textarea" rows="10" />
      </div>

      <div class="actions">
        <button class="btn ghost" @click="goBack">취소</button>
        <button class="btn primary" @click="update">수정 완료</button>
      </div>
    </section>
  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'
import axios from 'axios'
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const router = useRouter()
const route = useRoute()

const auth = useAuthStore()
const id = route.params.id
const userId = auth.userId



const board = ref({
    boardId : '',
    title: '',
    content : '',
    category: '',
    createDate:'',
    userId: '',
    nickname:''
})
onMounted(async () => {
    const boardRes = await axios.get(`/api/boards/${id}`)
    board.value = boardRes.data
    console.log(board.value)
})

const update = async() => {
    console.log(board.value.category)
    await axios.put(`/api/boards/${id}`, {
       title: board.value.title,
       content: board.value.content,
       category: board.value.category,
       userId: userId
    })
    router.back()
}

const goBack = () => router.back()
</script>
<style scoped>
/* ===== base ===== */
.page {
  max-width: 980px;
  margin: 0 auto;
  padding: 18px 14px 40px;
  /* background: #f6f7fb; */
  min-height: 100vh;
}

.card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 26px rgba(0, 0, 0, 0.08);
  padding: 20px;
}

/* ===== topbar ===== */
.topbar {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 4px 14px;
}

.topbar-title {
  flex: 1;
  font-size: 18px;
  font-weight: 800;
}

.icon-btn {
  width: 38px;
  height: 38px;
  border-radius: 12px;
  border: 1px solid #e6e8ee;
  background: #fff;
  cursor: pointer;
  display: grid;
  place-items: center;
}

.icon-btn:hover {
  background: #f3f5fa;
}

/* ===== form ===== */
.field {
  margin-bottom: 18px;
}

.label {
  display: block;
  font-weight: 900;
  margin-bottom: 8px;
}

.input,
.select,
.textarea {
  width: 100%;
  border: 1px solid #e6e8ee;
  border-radius: 14px;
  padding: 12px 14px;
  font-size: 14px;
  outline: none;
}

.input:focus,
.select:focus,
.textarea:focus {
  border-color: #ffd6d6;
}

.textarea {
  resize: none;
  line-height: 1.6;
}

/* ===== buttons ===== */
.actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 24px;
}

.btn {
  border: 1px solid #e6e8ee;
  background: #fff;
  border-radius: 12px;
  padding: 12px 18px;
  font-weight: 800;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.btn:hover {
  background: #f3f5fa;
}

.btn.primary {
  border-color: transparent;
  background: #fae6b9;
  color: #ff8a1d;
}

.btn.primary:hover {
  background: hsl(0, 100%, 88%);
}

.btn.ghost {
  background: #fff;
}
</style>
