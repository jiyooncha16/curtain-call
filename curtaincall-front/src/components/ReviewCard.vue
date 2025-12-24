<template>
  <article class="card" @click="posterClicked">
    <!-- 포스터 -->
    <div class="poster-wrap">
      <img
        class="poster"
        :src="`/${review.image}`"
        :alt="review.musicalTitle"
      />
      <div class="poster-title">
        <div class="musical-name">{{ review.title }}</div>
        <div class="sub-meta">
          <span class="time">{{ formatDate(review.createDate) }}</span>
        </div>
      </div>
    </div>

    <!-- 내용 -->
    <div class="body">
      <div class="top">
        <div class="author">
          <div class="avatar">
            <span>{{ initial(review.nickname) }}</span>
          </div>
          <div class="author-info">
            <div class="author-name">{{ review.nickname }}</div>
            <!-- <div class="author-sub">{{ formatDate(review.createdAt) }}</div> -->

            <!-- ⭐ 별점 -->
            <div class="rating">
                <i
                v-for="n in 5"
                :key="n"
                class="bi"
                :class="n <= review.rate ? 'bi-star-fill' : 'bi-star'"
                ></i>
                <span class="rating-num">{{ review.rate }}</span>
            </div>
          </div>
        </div>

        <button
          class="like-btn"
          :class="{ active: isLiked }"
          @click.stop="likeBtnClicked(review.reviewId)"
          type="button"
        >
          <i :class="icon"></i>
          <span>{{ likeCount }}</span>
        </button>
      </div>

      <!-- 리뷰 내용 -->
      <div class="review-content">
        {{ review.content }}
      </div>

      <div class="bottom">
        <div class="pill">
          <i class="bi bi-person"></i>
          <span class="sep"> | </span>
          <b class="pill-text">{{ review.nickname }}</b>
        </div>

        <div class="pill">
          <i class="bi bi-film"></i>
          <span class="sep"> | </span>
          <b class="pill-text">{{ review.title }}</b>
        </div>

        <!-- 수정 버튼 @click="$emit('edit', review)"-->
        <button
          v-if="isMine"
          class="edit-btn"
          @click.stop="clicked(review.reviewId)"
          type="button"
        >

          <i class="bi bi-pencil-square"></i>
        </button>
      </div>
    </div>

  </article>
</template>

<script setup>
import api from '@/api/axios'
import router from '@/router'
import { useAuthStore } from '@/stores/auth'
import { computed, onMounted, ref } from 'vue'

const props = defineProps({
  review: Object,
})
const id = props.review.reviewId
const auth = useAuthStore()

// 내 리뷰인지 확인하기
const authStore = useAuthStore()
const isMine = computed(() => {
  return authStore.userId === props.review.userId
})
console.log('auth userId =', authStore.userId)
console.log('review userId =', props.review.userId)


// 날짜 포맷
function formatDate(iso) {
  if (!iso) return ''
  const d = new Date(iso)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(
    d.getDate()
  ).padStart(2, '0')}`
}

function initial(name) {
  return name?.charAt(0) ?? '?'
}

const clicked = function(id) {
  router.push(`/review/edit/${id}`)
}

const posterClicked = function() {
  router.push(`/musical/${props.review.musicalId}`)
}

const likeCount = ref('')
const isLiked = ref(false)
const icon = computed(() =>
  isLiked.value ? 'bi bi-heart-fill' : 'bi bi-heart'
)

onMounted(async () => {
  const resCnt = await api.get(`/api/reviews/like/${id}`) // 현재 개수
  likeCount.value = resCnt.data

  const res = await api.get(`/api/reviews/like/me/${id}`) // 내가 눌렀는지?
  isLiked.value = res.data

})

const likeBtnClicked = async function(id) {

  if (!auth.isLogin) {
    const ok = confirm('로그인하시겠습니까?')
    if (ok) {
      router.push('/login')
    }
    return
  }

  const res = await api.post(`/api/reviews/like/toggle/${id}`) // 토글하기
  isLiked.value = res.data   // true / false

  const cntRes = await api.get(`/api/reviews/like/${id}`) // 개수 받기
  likeCount.value = cntRes.data

}

const showModal = ref(false)

const openModal = () => {
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
}
</script>
<style scoped>
    .card {
  background: #fff;
  border-radius: 14px;
  overflow: hidden;
  display: grid;
  grid-template-columns: 140px 1fr;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
  transition: transform 0.15s ease, box-shadow 0.15s ease;
  align-items: stretch;
  /* height: 250px; */
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.12);
}
.poster-wrap {
padding: 10px;
  position: relative;
  align-self: stretch;   /* ⭐ 이 줄이 진짜 핵심 */
  overflow: hidden;
}

.poster {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.poster-title {
  position: absolute;
  bottom: 8px;
  left: 8px;
  right: 8px;
  color: #fff;
  text-shadow: 0 2px 8px rgba(0,0,0,0.6);
  text-align: center;
  padding-bottom: 10px;
  width: 100%;
}

.musical-name {
  font-weight: 700;
  font-size: 15px;
}
.body {
  padding: 14px 16px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  min-width:0;
}

.top {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.author {
  display: flex;
  align-items: center;
  gap: 10px;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #ffd77a;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
}

.author-name {
  font-size: 14px;
  font-weight: 600;
}

.author-sub {
  font-size: 12px;
  color: #888;
}
.like-btn {
  border: none;
  background: #f1f1f5;
  border-radius: 10px;
  padding: 0px 0px;
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  font-size: 13px;
}

.like-btn.active {
  background: #ffe4e6;
  color: #e11d48;
}
.review-content {
  height: 105px;
  font-size: 14px;
  line-height: 1.6;
  color: #333;
  background: #fafafa;
  padding: 12px;
  border-radius: 10px;
  /* white-space: pre-wrap; */

  
  /* ⭐ 핵심 */
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 4;   /* ← 보여줄 줄 수 (원하면 3, 5로 변경) */
  overflow: hidden;
}

.bottom {
  display: flex;
  gap: 10px;
  margin-top: auto;
  overflow: hidden;
  width: 100%;          /* ⭐⭐⭐ 이 줄이 없으면 100% 실패 */
  max-width: 100%;      /* ⭐ 안전장치 */
}

.pill {
  display: flex;
  font-size: 12px;
  background: #f3f4f6;
  padding: 6px 10px;
  border-radius: 999px;
  color: #555;
  max-width: 100%;       /* 핵심 */
  min-width: 0;
    overflow: hidden;
    text-overflow: ellipsis
}
.pill b {
  display: block;       /* ⭐ inline → block */
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.text-ellipsis {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.pill-text {
  flex: 1;               /* ⭐ 줄어들 수 있음 */
  min-width: 0;          /* ⭐⭐⭐ 이거 없으면 절대 안 됨 */
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* =============================
   별점
============================= */
.rating {
  display: flex;
  align-items: center;
  gap: 2px;
  margin-top: 4px;
}

.rating i {
  font-size: 13px;
  color: #f59e0b; /* amber-500 */
}

.rating-num {
  font-size: 12px;
  color: #666;
  margin-left: 4px;
}
.edit-btn {
  margin-left: auto; 
  background: transparent;
  border: none;
  cursor: pointer;
  color: #666;
  font-size: 15px;
}

.edit-btn:hover {
  color: #800000;
}

/* =============================
   하트
============================= */
button {
  border: 1px solid #800000;
  border-radius: 5px;

  color: #800000;
  background: transparent;

  font-size: 16px;
  font-weight: bold;
  width: 50px;
  height: 36px;

  display: flex;
  align-items: center;
  justify-content: center;

  cursor: pointer;
  transition: 
    background-color 0.2s ease,
    color 0.2s ease,
    transform 0.15s ease;
}

/* 눌렸을 때 */
button.active {
  background-color: #800000;
  color: #ffffff;
  border-color: #800000;
}

/* 살짝 눌리는 느낌 */
button:active {
  transform: scale(0.95);
}
button:hover {
  background: rgba(128, 0, 0, 0.68);
}

</style>