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
      <!-- ⭐ 별점 필터 (단일 선택) -->
      <div class="rating-filter">
        <!-- ✅ 전체보기 (항상 표시 + 1점 앞) -->
        <button class="rating-btn" @click="resetRating">
          전체보기
        </button>

        <button v-for="n in 5" :key="n" class="rating-btn" :class="{ active: selectedRating === n }"
          @click="selectRating(n)">
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
            <div class="line w60"></div>
            <div class="line w80"></div>
            <div class="line w95"></div>
          </div>
        </article>
      </section>

      <!-- 리스트 -->
      <section v-else class="grid">
        <ReviewCard v-for="review in visibleReviews" :key="review.id" :review="review" @toggle-like="toggleLike" />

        <!-- empty -->
        <div v-if="visibleReviews.length === 0" class="empty">
          <div class="empty-icon">🎭</div>
          <div class="empty-title">리뷰가 없어요. 작성해볼까요?</div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import api from "@/api/axios";
import ReviewCard from "@/components/ReviewCard.vue";
import { computed, onMounted, ref } from "vue";

const loading = ref(true);
const reviews = ref([]);

// ⭐ 단일 선택 별점
const selectedRating = ref(null);

onMounted(async () => {
  try {
    loading.value = true;
    const res = await api.get("/api/reviews/my");
    reviews.value = res.data;
  } catch (e) {
    console.error(e);
    reviews.value = [];
  } finally {
    loading.value = false;
  }
});

// ⭐ 별점 선택
function selectRating(rating) {
  selectedRating.value =
    selectedRating.value === rating ? null : rating;
}

// ⭐ 전체보기
function resetRating() {
  selectedRating.value = null;
}

// 필터링
const filtered = computed(() => {
  if (selectedRating.value === null) return reviews.value;
  return reviews.value.filter(r => r.rate === selectedRating.value);
});

// 정렬
const visibleReviews = computed(() => {
  const list = [...filtered.value];
  list.sort(
    (a, b) =>
      new Date(b.createdDate).getTime() -
      new Date(a.createdDate).getTime()
  );
  return list;
});

// 좋아요 토글
function toggleLike(review) {
  review.liked = !review.liked;
  review.likeCount += review.liked ? 1 : -1;
}
</script>

<style scoped>
/* =============================
   전체 레이아웃
============================= */
.page {
  min-height: 100vh;
  padding: 30px 20px 60px;
}

.page-header {
  max-width: 1100px;
  margin: 0 auto 24px;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
}

/* =============================
   컨트롤
============================= */
.controls {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
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
   Empty
============================= */
.empty {
  grid-column: 1 / -1;
  background: #fff;
  padding: 40px;
  border-radius: 14px;
  text-align: center;
}
</style>
