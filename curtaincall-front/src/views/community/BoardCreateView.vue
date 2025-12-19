<template>
  <div class="page">
    <!-- 상단 -->
    <div class="topbar">
      <button class="icon-btn" @click="goBack">
        <i class="bi bi-arrow-left"></i>
      </button>
      <div class="topbar-title">게시글 작성</div>
      <div></div>
    </div>

    <!-- 작성 카드 -->
    <section class="card">
      <!-- 카테고리 -->
      <div class="field">
        <label class="label">카테고리</label>
        <select v-model="category" class="select">
          <option value="">선택하세요</option>
          <option value="free">자유</option>
          <option value="deal">판매</option>
        </select>
      </div>

      <!-- 제목 -->
      <div class="field">
        <label class="label">제목</label>
        <input
          v-model="title"
          class="input"
          placeholder="제목을 입력하세요"
        />
      </div>

      <!-- 내용 -->
      <div class="field">
        <label class="label">내용</label>
        <textarea
          v-model="content"
          class="textarea"
          rows="10"
          placeholder="내용을 입력하세요"
        />
      </div>

      <!-- 버튼 -->
      <div class="actions">
        <button class="btn ghost" @click="goBack">취소</button>
        <button class="btn primary" @click="createBoard">등록</button>
      </div>
    </section>
  </div>
</template>

<script setup>
import axios from 'axios'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const title = ref('')
const category = ref('')
const content = ref('')

const previewImages = ref([])

const onImageChange = (e) => {
  previewImages.value = []
  Array.from(e.target.files).forEach(file => {
    previewImages.value.push(URL.createObjectURL(file))
  })
}

const goBack = () => router.back()
////////////////////////////////////////////////////////연결하기!!!!!!!!!1
const loginUser = {
    userId: 1,
    nickname: '지니', // 임시
}

const createBoard = async (payload) => {
    await axios.post('/api/boards', {
       title: title.value,
       content: content.value,
       category: category.value,
       userId: loginUser.userId,
    })
    console.log("등록했어요")
    router.push('/community')
}

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
  /* color: #ff5454;; */
}

.textarea {
  resize: none;
  line-height: 1.6;
}

/* ===== gallery ===== */
.gallery-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  margin-top: 12px;
}

.img-tile {
  aspect-ratio: 4 / 3;
  border-radius: 14px;
  overflow: hidden;
  background: #f0f1f5;
}

.img-tile img {
  width: 100%;
  height: 100%;
  object-fit: cover;
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
  background: #ffd6d6;
  color: #ff5454;
}

.btn.ghost {
  background: #fff;
}
</style>
