<template>
  <div class="write-box">
    <!-- 제목 + 작성자 -->
    <div class="top-row">
      <div class="field left">
        <div class="basic-text">제목</div>
        <input
          class="title-input"
          v-model="title"
          placeholder="제목을 입력하세요"
        />
      </div>

      <div class="field right">
        <div class="basic-text">작성자</div>
        <input
          class="title-input"
          :value="writer"
          readonly
        />
      </div>
    </div>

    <!-- 내용 -->
    <textarea
      v-model="content"
      placeholder="내용을 작성해주세요."
      rows="10"
    ></textarea>

    <!-- 버튼 -->
    <div class="btn-row">
      <button @click="goBack">취소</button>
      <button @click="updateBoard">수정하기</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

const boardId = Number(route.params.id)

// 임시 로그인 유저
const writer = ref('지니')
const userId = 1   // ⚠️ 나중에 Spring Security로 교체

const title = ref('')
const content = ref('')

// 기존 게시글 불러오기
const fetchBoard = async () => {
  try {
    const { data } = await axios.get(`/api/boards/${boardId}`)
    title.value = data.title
    content.value = data.content
  } catch (e) {
    alert('게시글을 불러올 수 없습니다.')
    router.back()
  }
}

onMounted(fetchBoard)

// 수정 요청
const updateBoard = async () => {
  if (!title.value.trim() || !content.value.trim()) {
    alert('제목과 내용을 입력해주세요')
    return
  }

  const board = {
    title: title.value,
    content: content.value,
    userId: userId,
  }

  try {
    await axios.put(`/api/boards/${boardId}`, board)
    alert('게시글이 수정되었습니다.')
    router.push(`/community/${boardId}`)
  } catch (e) {
    alert('게시글 수정 실패')
    console.error(e)
  }
}

const goBack = () => router.back()
</script>

<style scoped>
.write-box {
  width: 100%;
  padding: 20px;
  background: #f2f2f2;
}

.top-row {
  width: 100%;
  display: flex;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 10px;
}

.field {
  display: flex;
  align-items: center;
}

.field.left {
  flex: 3;
}

.field.right {
  flex: 2;
  justify-content: flex-end;
}

.basic-text {
  min-width: 60px;
  font-size: 14px;
}

.title-input {
  flex: 1;
  height: 40px;
  background: #ddd;
  padding: 5px 10px;
  font-size: 18px;
  border: none;
  outline: none;
  margin-left: 10px;
  min-width: 0;
}

textarea {
  width: 100%;
  resize: none;
  border: none;
  padding: 10px;
  margin-bottom: 10px;
  background: #ddd;
  font-size: 18px;
}

textarea:focus,
.title-input:focus {
  outline: none;
  box-shadow: none;
}

.btn-row {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

button {
  border: none;
  padding: 6px 14px;
  background: #ccc;
  cursor: pointer;
}
</style>
