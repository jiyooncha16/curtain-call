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

      <!-- AI -->
      <div class="field ai-field">
        <label class="label">AI</label>

        <div class="ai-row">
          <input
            type="text"
            class="input ai-input"
            :value="keyword"
            placeholder="키워드를 10자 이상 입력하세요."
          />

          <button
            class="ai-btn"
            :disabled="loading"
            @click="generateReview"
          >
            {{ loading ? '생성 중...' : '리뷰 생성' }}
          </button>
        </div>

        <p class="ai-help">
          예시 : 감동적인 스토리, 또 보고 싶음, 넘버가 좋음
        </p>
      </div>



      <!-- 리뷰 내용 -->
      <div class="field">
        <label class="label">리뷰 내용</label>
        <textarea
          class="textarea"
          v-model="content"
          placeholder="공연을 보고 느낀 점을 자유롭게 작성해 주세요."
          maxlength="100"
        ></textarea>
         <p class="char-count">
          {{ content.length }} / 100
        </p>
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
const keyword = ref('')
const rate = ref(0)
const authStore = useAuthStore()
const loading = ref(false)

const generateReview = async () => {
  if (content.length < 10) {
    alert('키워드를 10자 이상 적어주세요.')
    return
  }

  if (title.length < 1) {
    alert('제목이 없습니다.')
    return
  }

  
  loading.value = true
  const res = await axios.get('/api/reviews/generateReview', {
    params : {
      title : title.value,
      keyword : keyword.value
    }
  })
  await new Promise(resolve => setTimeout(resolve, 1500))

  loading.value = false
  content.value = res.data
  alert('리뷰 초안이 생성되었습니다! 다듬어서 작성해보세요.')

}

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
.char-count {
  text-align: right;
  font-size: 12px;
  color: #888;
  margin-top: 4px;
}


.ai-field {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.ai-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.ai-input {
  flex: 1;
  height: 44px;
  font-size: 14px;
}

.ai-btn {
  height: 44px;
  padding: 0 18px;
  border-radius: 10px;
  border: none;
  background: linear-gradient(135deg, #111 0%, #333 100%);
  color: #fff;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;
  white-space: nowrap;
  transition: all 0.2s ease;
}

/* hover */
.ai-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.25);
}

/* disabled 상태용 (키워드 3개 미만일 때 추천) */
.ai-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
  box-shadow: none;
  transform: none;
}

.ai-help {
  font-size: 13px;
  color: #777;
}


</style>