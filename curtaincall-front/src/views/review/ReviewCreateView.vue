<template>
  <div class="page">
    <div class="review-wrapper">
    <div class="title-text">리뷰 작성</div>

    <div class="shadow review-card">
      <!-- 뮤지컬 이름 -->
      <div class="field">
        <label class="label">뮤지컬</label>
        <input
          type="text"
          class="input readonly"
          :value="title"
          readonly
        />
      </div>

      <!-- ⭐ 별점 -->
      <div class="field">
        <label class="label">평점</label>
        <div class="rate-box">
          <i
            v-for="n in 5"
            :key="n"
            class="bi"
            :class="n <= rate ? 'bi-star-fill active' : 'bi-star'"
            @click="rate = n"
          ></i>
          <span class="rate-text">{{ rate }} / 5</span>
        </div>
      </div>

      <!-- 리뷰 내용 -->
      <div class="field">
        <label class="label">리뷰 내용</label>
        <textarea
          class="textarea"
          v-model="content"
          placeholder="공연을 보고 느낀 점을 자유롭게 작성해 주세요."
        ></textarea>
      </div>

      <!-- 버튼 -->
      <div class="btn-row">
        <button class="btn primary" @click="submitReview">
          작성
        </button>
      </div>
    </div>
  </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import axios from '@/api/axios' // 네 프로젝트에 맞게 경로 조정
import { useRoute } from 'vue-router'
import api from '@/api/axios'
import { useAuthStore } from '@/stores/auth'
import router from '@/router'

// 👉 보통은 route.params로 받음
const route = useRoute()
const title = ref('')
const id = route.params.id
onMounted( async ()=> {
    const musical = await axios.get(`/api/musicals/${id}`)
    console.log(musical.data)
    title.value = musical.data.title
})
const content = ref('')
const rate = ref(0)
const authStore = useAuthStore()
const submitReview = async () => {
    if (rate.value === 0) {
    alert('평점을 선택해 주세요.')
    return
  }

  if (!content.value.trim()) {
    alert('리뷰 내용을 입력해 주세요.')
    return
  }

  try {

    await api.post(`/api/reviews/${id}`, {
            rate: rate.value,
            content: content.value,
        }
    )
    alert('리뷰가 등록되었습니다.')
    router.back()
  } catch (e) {
    console.error(e)
    alert('리뷰 등록에 실패했습니다.')
  }
}
</script>

<style scoped>
.review-wrapper {
  width: 100%;
  max-width: 600px;   /* 카드랑 동일 */
  margin: 0 auto;     /* 전체는 중앙 */
}

.title-text {
  text-align: left;   /* ⭐ 카드 왼쪽 끝에 맞춤 */
  margin-bottom: 10px;
}
.review-card {
  width: 100%;
  max-width: 600px;
}

.field {
  margin-bottom: 24px;
}

.label {
  display: block;
  font-weight: 700;
  margin-bottom: 8px;
}

.input,
.textarea {
  width: 100%;
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 12px 14px;
  font-size: 14px;
}

.input.readonly {
  background: #f5f5f5;
  color: #555;
}

.textarea {
  min-height: 180px;
  resize: vertical;
}

.textarea:focus,
.input:focus {
  outline: none;
  border-color: #800000;
}

/* 버튼 */
.btn-row {
  display: flex;
  justify-content: flex-end;
}

.btn {
  padding: 10px 22px;
  border-radius: 10px;
  font-weight: 700;
  cursor: pointer;
}

.btn.primary {
  background: #800000;
  color: #fff;
  border: none;
}

.btn.primary:hover {
  opacity: 0.9;
}

/* 별점 */
.rate-box {
  display: flex;
  align-items: center;
  gap: 6px;
}

.rate-box i {
  font-size: 26px;
  cursor: pointer;
  color: #ddd;
  transition: transform 0.15s ease, color 0.15s ease;
}

.rate-box i.active {
  color: #f5c518;
}

.rate-box i:hover {
  transform: scale(1.1);
}

.rate-text {
  margin-left: 10px;
  font-size: 14px;
  color: #555;
}

</style>