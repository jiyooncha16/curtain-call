<template>
  <div class="review-box">
    <!-- 작품명 + 별점 -->
    <div class="top-row">
      <div class="field left">
        <div class="basic-text">제목</div>
        <input class="title-input" placeholder="제목을 입력하세요" />
      </div>

      <div class="field right">
        <div class="basic-text">작성자</div>
        <input class="title-input" :value="writer" readonly />
      </div>
    </div>

    <!-- 내용 -->
    <textarea v-model="content" placeholder="내용을 작성해주세요." rows="10"></textarea>

    <!-- 등록 버튼 -->
    <div class="btn-row">
      <button @click="submitReview">등록하기</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import RateFlexible from './icon/RateFlexible.vue'

const title = ref('뮤지컬 <데스노트>')
const rating = ref(5)
const content = ref('')
const writer = ref('지니')

const submitReview = () => {
  if (!rating.value || !content.value.trim()) {
    alert('별점과 리뷰를 입력해주세요')
    return
  }

  const review = {
    title: title.value,
    rating: rating.value,
    content: content.value,
  }

  console.log('리뷰 등록:', review)
  // 👉 여기서 API 호출하면 됨
}
</script>

<style scoped>
.review-box {
  width: 100%;
  padding: 20px;
  background: #f2f2f2;
}

.top-row {
  width:100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.work-title {
  width:90%;
  height:40px;
  background: #ddd;
  padding: 5px 10px;
  font-size: 18px;
  /* font-weight: bold; */
}
.title-input {
  width:90%;
  height:40px;
  background: #ddd;
  padding: 5px 10px;
  font-size: 18px;
  /* font-weight: bold; */
  border: none;
  outline: none;
  margin-left:10px;
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
textarea:focus {
    outline: none;
}
.title-input:focus {
  outline: none;
  box-shadow: none;
  border-color: transparent;
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

.top-row {
  display: flex;
  justify-content: space-between;
  gap: 20px;                /* 좌우 간격 */
}

.field {
  display: flex;
  align-items: center;
}

.field.left {
  flex: 3;                  /* 60% 느낌 */
}

.field.right {
  flex: 2;                  /* 40% 느낌 */
  justify-content: flex-end;
}

.title-input {
  flex: 1;                  /* ⭐ 핵심 */
  margin-left: 10px;
  min-width: 0;             /* ⭐ 글씨 삐져나옴 방지 */
}

</style>
