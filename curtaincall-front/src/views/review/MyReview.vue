<template>
  <div class="page">
    <!-- 헤더 -->
    <header class="page-header">
      <div>
        <h1 class="page-title">내 리뷰</h1>
      </div>
    </header>
    
    <!-- 컨트롤 -->
    <div class="controls">
    <!-- ⭐ 별점 필터 (멀티 선택) -->
    <div class="rating-filter">
        <button
        v-for="n in 5"
        :key="n"
        class="rating-btn"
        :class="{ active: selectedRatings.has(n) }"
        @click="toggleRating(n)"
        >
        {{ n }}점
        </button>
    </div>
    </div>


    <!-- 본문 -->
    <main class="content">
      <!-- 로딩 -->
      <section v-if="loading" class="grid">
        <article v-for="n in 8" :key="n" class="card skeleton">
          <div class="poster"></div>
          <div class="body">
            <div class="row">
              <div class="line w60"></div>
              <div class="chip w20"></div>
            </div>
            <div class="line w80"></div>
            <div class="line w95"></div>
            <div class="line w90"></div>
            <div class="line w70"></div>
            <div class="footer">
              <div class="chip w30"></div>
              <div class="chip w20"></div>
            </div>
          </div>
        </article>
      </section>

      <!-- 리스트 -->
      <section v-else class="grid">
        <ReviewCard
          v-for="review in visibleReviews"
          :key="review.id"
          :review="review"
          @toggle-like="toggleLike"
        />

        <!-- empty -->
        <div v-if="visibleReviews.length === 0" class="empty">
          <div class="empty-icon">🎭</div>
          <div class="empty-title">
            리뷰가 없어요. 작성해볼까요?
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import api from "@/api/axios";
import ReviewCard from "@/components/ReviewCard.vue";
import { computed, onMounted, ref } from "vue";
// axios 사용 중이면 아래 주석 해제
// import axios from "axios";

const loading = ref(true);

// UI state
const keyword = ref("");
const sortKey = ref("latest");

const reviews = ref([]);

onMounted(async () => {
  try {
    loading.value = true;

    const res = await api.get("/api/reviews/my");
    reviews.value = res.data;
    console.log("리뷰 목록 : ", reviews.value[0].image)

    // // 일단 mock
    // await new Promise((r) => setTimeout(r, 550));
    // reviews.value = mock;
  } catch (e) {
    console.error(e);
    reviews.value = [];
  } finally {
    loading.value = false;
  }
});

const filtered = computed(() => {
  const k = keyword.value.trim().toLowerCase()
  const ratingSet = selectedRatings.value 

  return reviews.value.filter((r) => {
    // 별점 필터
    const matchRating =
      ratingSet.size === 0 || ratingSet.has(r.rate)
    return matchRating
  })
})


const visibleReviews = computed(() => {
  const list = [...filtered.value];
  list.sort((a, b) => new Date(b.createDate).getTime() - new Date(a.createdDate).getTime());
  return list;
});

function toggleLike(review) {
  if (!review) return;

  // UI optimistic update
  review.liked = !review.liked;
  review.likeCount += review.liked ? 1 : -1;

  // ✅ 서버 반영 (예: POST /api/reviews/{id}/like)
  // axios.post(`/api/reviews/${review.id}/like`, { liked: review.liked }).catch(() => { ...롤백... })
}

function formatDate(iso) {
  if (!iso) return "";
  const d = new Date(iso);
  const yyyy = d.getFullYear();
  const mm = String(d.getMonth() + 1).padStart(2, "0");
  const dd = String(d.getDate()).padStart(2, "0");
  const hh = String(d.getHours()).padStart(2, "0");
  const mi = String(d.getMinutes()).padStart(2, "0");
  return `${yyyy}-${mm}-${dd} ${hh}:${mi}`;
}

function initial(name) {
  if (!name) return "?";
  return name.trim().slice(0, 1);
}

// 별점 선택 버튼
const selectedRatings = ref(new Set())
function toggleRating(rating) {
  const set = new Set(selectedRatings.value)

  if (set.has(rating)) {
    set.delete(rating)
  } else {
    set.add(rating)
  }

  selectedRatings.value = set
}

</script>

<style scoped>
/* =============================
   전체 레이아웃
============================= */
.page {
  min-height: 100vh;
  /* background-color: #f7f7fb; */
  padding: 30px 20px 60px;
  color: #222;
}

.page-header {
  max-width: 1100px;
  margin: 0 auto 24px;
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  flex-wrap: wrap;
  gap: 16px;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  margin: 0;
}

.page-sub {
  margin-top: 6px;
  font-size: 14px;
  color: #666;
}

/* =============================
   검색 / 정렬
============================= */
.controls {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.search {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 8px 12px;
}

.search input {
  border: none;
  outline: none;
  font-size: 14px;
  width: 220px;
}

.select {
  border-radius: 10px;
  border: 1px solid #ddd;
  padding: 8px 12px;
  background: #fff;
  cursor: pointer;
}

/* =============================
   그리드
============================= */
.content {
  max-width: 1100px;
  margin: 0 auto;
}

.grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 18px;
}

@media (max-width: 900px) {
  .grid {
    grid-template-columns: 1fr;
  }
}

/* =============================
   카드
============================= */
.card {
  background: #fff;
  border-radius: 14px;
  overflow: hidden;
  display: grid;
  grid-template-columns: 140px 1fr;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
  transition: transform 0.15s ease, box-shadow 0.15s ease;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.12);
}

/* =============================
   포스터
============================= */
.poster-wrap {
  position: relative;
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
}

.musical-name {
  font-weight: 700;
  font-size: 15px;
}

/* =============================
   본문
============================= */
.body {
  padding: 14px 16px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.top {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 작성자 */
.author {
  display: flex;
  align-items: center;
  gap: 10px;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #4f46e5;
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

/* =============================
   좋아요
============================= */
.like-btn {
  border: none;
  background: #f1f1f5;
  border-radius: 20px;
  padding: 6px 12px;
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

/* =============================
   리뷰 내용
============================= */
.review-content {
  font-size: 14px;
  line-height: 1.6;
  color: #333;
  background: #fafafa;
  padding: 12px;
  border-radius: 10px;
  white-space: pre-wrap;
}

/* =============================
   하단 정보
============================= */
.bottom {
  display: flex;
  gap: 10px;
  margin-top: auto;
}

.pill {
  font-size: 12px;
  background: #f3f4f6;
  padding: 6px 10px;
  border-radius: 999px;
  color: #555;
}

/* =============================
   Empty
============================= */
.empty {
  grid-column: 1 / -1;
  background: #fff;
  padding: 40px;
  border-radius: 14px;
  text-align: center;
  box-shadow: 0 6px 18px rgba(0,0,0,0.06);
}

/* rating */
.rating-filter {
  display: flex;
  gap: 6px;
}

.rating-btn {
  padding: 6px 12px;
  border-radius: 999px;
  border: 1px solid #ddd;
  background: #fff;
  cursor: pointer;
  font-size: 13px;
}

.rating-btn.active {
  background: #4f46e5;
  color: #fff;
  border-color: #4f46e5;
}

</style>