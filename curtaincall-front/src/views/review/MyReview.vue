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
import ReviewCard from "@/components/ReviewCard.vue";
import { computed, onMounted, ref } from "vue";
// axios 사용 중이면 아래 주석 해제
// import axios from "axios";

const loading = ref(true);

// UI state
const keyword = ref("");
const sortKey = ref("latest");

// ✅ 실제 API 응답에 맞춰 필드명만 맞추면 그대로 사용 가능
const reviews = ref([]);

/**
 * 예시 데이터 (API 붙이면 이 부분 제거)
 * 필수: 작성자(authorName), 포스터(posterUrl), 뮤지컬명(musicalTitle), 리뷰내용(content), 좋아요(likeCount)
 */
const mock = [
  {
    id: 1,
    authorName: "지니",
    musicalTitle: "데스노트",
    posterUrl:
      "https://images.unsplash.com/photo-1524985069026-dd778a71c7b4?w=800&q=80",
    content:
      "극 전개가 빠르고 넘버가 미쳤어요. 배우 에너지랑 무대 연출이 한 번에 터지는 느낌… 커튼콜 때 진짜 소름. 다시 보고 싶다.",
    likeCount: 87,
    liked: false,
    rating: 5,
    createdAt: "2025-12-20T12:20:00",
  },
  {
    id: 2,
    authorName: "웬디",
    musicalTitle: "오페라의 유령",
    posterUrl:
      "https://images.unsplash.com/photo-1527766833261-b09c3163a791?w=800&q=80",
    content:
      "클래식의 정석. 오케스트라부터 무대장치까지 촘촘하게 쌓여서 몰입감이 대단했어요. 특히 그 장면… 말이 필요 없음.",
    likeCount: 52,
    liked: true,
    rating: 3,
    createdAt: "2025-12-19T21:10:00",
  },
  {
    id: 3,
    authorName: "하나",
    musicalTitle: "레베카",
    posterUrl:
      "https://images.unsplash.com/photo-1517602302552-471fe67acf66?w=800&q=80",
    content:
      "스토리+넘버 조합이 너무 탄탄해서 끝까지 긴장감 유지. 넘버가 감정선을 그대로 끌고 가서 눈물 줄줄…",
    likeCount: 33,
    liked: false,
    rating: 4,
    createdAt: "2025-12-18T10:05:00",
  },
];

onMounted(async () => {
  try {
    loading.value = true;

    // ✅ API 붙이는 버전 예시
    // const res = await axios.get("/api/reviews");
    // reviews.value = res.data;

    // 일단 mock
    await new Promise((r) => setTimeout(r, 550));
    reviews.value = mock;
  } catch (e) {
    console.error(e);
    reviews.value = [];
  } finally {
    loading.value = false;
  }
});

const filtered = computed(() => {
  const k = keyword.value.trim().toLowerCase()
  const ratingSet = selectedRatings.value   // ⭐ 이 줄이 빠져 있었음

  return reviews.value.filter((r) => {
    // 🔍 검색 필터
    const matchKeyword =
      !k ||
      r.authorName.toLowerCase().includes(k) ||
      r.musicalTitle.toLowerCase().includes(k) ||
      r.content.toLowerCase().includes(k)

    // ⭐ 별점 필터
    const matchRating =
      ratingSet.size === 0 || ratingSet.has(r.rating)

    return matchKeyword && matchRating
  })
})


const visibleReviews = computed(() => {
  const list = [...filtered.value];

  if (sortKey.value === "likes") {
    list.sort((a, b) => (b.likeCount ?? 0) - (a.likeCount ?? 0));
  } else if (sortKey.value === "title") {
    list.sort((a, b) => (a.musicalTitle || "").localeCompare(b.musicalTitle || ""));
  } else {
    // latest
    list.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
  }

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