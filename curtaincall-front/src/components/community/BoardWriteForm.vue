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
      <button @click="submitBoard">등록하기</button>
    </div>
  </div>
</template>

<script setup>
    
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

// 임시 로그인 유저
const writer = ref('지니')
const userId = 1 // ⚠️ 나중에 Spring Security로 교체

const title = ref('')
const content = ref('')

const router = useRouter()

const submitBoard = async () => {
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
    await axios.post('/api/boards', board)
    alert('게시글이 등록되었습니다.')
    router.push('/community')
  } catch (e) {
    alert('게시글 등록 실패')
    console.error(e)
  }
}
</script>

<style scoped>
.write-box {
  width: 100%;
  padding: 20px;
  background: #f2f2f2;
}

/* 상단 영역 */
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
}

button {
  border: none;
  padding: 6px 14px;
  background: #ccc;
  cursor: pointer;
}
</style>
