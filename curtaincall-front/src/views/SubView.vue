<template>
  <div class="page">
    <!-- 헤더 -->
    <header class="page-header">
      <div>
        <h1 class="page-title">뮤지컬 리뷰</h1>
        <p class="page-sub">
          관람 후기들을 한 곳에서 확인해요. (상세 없이 리스트에서 전체 내용 노출)
        </p>
      </div>

      <!-- 컨트롤 -->
      <div class="controls">
        <div class="search">
          <i class="bi bi-search"></i>
          <input
            v-model="keyword"
            type="text"
            placeholder="뮤지컬명 / 작성자 / 내용 검색"
          />
        </div>

        <select v-model="sortKey" class="select">
          <option value="latest">최신순</option>
          <option value="likes">좋아요순</option>
          <option value="title">뮤지컬명순</option>
        </select>
      </div>
    </header>

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
        <article
          v-for="review in visibleReviews"
          :key="review.id"
          class="card"
        >
          <!-- 포스터 -->
          <div class="poster-wrap">
            <img class="poster" :src="review.posterUrl" :alt="review.musicalTitle" />
            <div class="poster-dim"></div>
            <div class="poster-title">
              <div class="musical-name">{{ review.musicalTitle }}</div>
              <div class="sub-meta">
                <span class="badge">REVIEW</span>
                <span class="dot"></span>
                <span class="time">{{ formatDate(review.createdAt) }}</span>
              </div>
            </div>
          </div>

          <!-- 내용 -->
          <div class="body">
            <div class="top">
              <div class="author">
                <div class="avatar">
                  <span>{{ initial(review.authorName) }}</span>
                </div>
                <div class="author-info">
                  <div class="author-name">{{ review.authorName }}</div>
                  <div class="author-sub">관람 후기</div>
                </div>
              </div>

              <button
                class="like-btn"
                :class="{ active: review.liked }"
                @click="toggleLike(review)"
                type="button"
                aria-label="like"
              >
                <i :class="review.liked ? 'bi bi-heart-fill' : 'bi bi-heart'"></i>
                <span>{{ review.likeCount }}</span>
              </button>
            </div>

            <!-- 리뷰 내용: 상세 없이 전체 노출 -->
            <div class="review-content">
              {{ review.content }}
            </div>

            <div class="bottom">
              <div class="pill">
                <i class="bi bi-person"></i>
                <span>작성자</span>
                <span class="sep">|</span>
                <b>{{ review.authorName }}</b>
              </div>

              <div class="pill">
                <i class="bi bi-film"></i>
                <span>뮤지컬</span>
                <span class="sep">|</span>
                <b>{{ review.musicalTitle }}</b>
              </div>
            </div>
          </div>
        </article>

        <!-- empty -->
        <div v-if="visibleReviews.length === 0" class="empty">
          <div class="empty-icon">🎭</div>
          <div class="empty-title">검색 결과가 없어요</div>
          <div class="empty-sub">다른 키워드로 찾아보세요.</div>
          <button class="reset" @click="keyword = ''" type="button">검색 초기화</button>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
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
  const k = keyword.value.trim().toLowerCase();
  if (!k) return reviews.value;

  return reviews.value.filter((r) => {
    return (
      r.authorName.toLowerCase().includes(k) ||
      r.musicalTitle.toLowerCase().includes(k) ||
      r.content.toLowerCase().includes(k)
    );
  });
});

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
</script>

<style scoped>
/* ✅ 배경 + 레이아웃 */
.page {
  min-height: 100vh;
  padding: 28px 18px 60px;
  background:
    radial-gradient(1200px 600px at 20% 10%, rgba(99, 102, 241, 0.18), transparent 60%),
    radial-gradient(900px 500px at 80% 0%, rgba(16, 185, 129, 0.15), transparent 55%),
    linear-gradient(180deg, #0b0d12 0%, #0b0d12 40%, #07080c 100%);
  color: #eef0f6;
}

.page-header {
  max-width: 1100px;
  margin: 0 auto 18px;
  display: flex;
  gap: 14px;
  align-items: flex-end;
  justify-content: space-between;
  flex-wrap: wrap;
}

.page-title {
  margin: 0;
  font-size: 28px;
  letter-spacing: -0.02em;
}

.page-sub {
  margin: 6px 0 0;
  color: rgba(238, 240, 246, 0.68);
  font-size: 14px;
}

/* ✅ 컨트롤 */
.controls {
  display: flex;
  gap: 10px;
  align-items: center;
}

.search {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid rgba(255, 255, 255, 0.10);
  backdrop-filter: blur(10px);
}

.search i {
  opacity: 0.75;
  font-size: 16px;
}

.search input {
  width: 260px;
  background: transparent;
  border: 0;
  outline: none;
  color: #eef0f6;
  font-size: 14px;
}
.search input::placeholder {
  color: rgba(238, 240, 246, 0.45);
}

.select {
  height: 42px;
  padding: 0 12px;
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.06);
  color: #eef0f6;
  border: 1px solid rgba(255, 255, 255, 0.10);
  outline: none;
  cursor: pointer;
}

/* ✅ 그리드 */
.content {
  max-width: 1100px;
  margin: 0 auto;
}

.grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
}

@media (max-width: 880px) {
  .grid {
    grid-template-columns: 1fr;
  }
  .search input {
    width: 200px;
  }
}

/* ✅ 카드 */
.card {
  overflow: hidden;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid rgba(255, 255, 255, 0.10);
  backdrop-filter: blur(12px);
  box-shadow: 0 18px 50px rgba(0, 0, 0, 0.35);
  display: grid;
  grid-template-columns: 150px 1fr;
  min-height: 220px;
  transition: transform 0.12s ease, border-color 0.12s ease;
}

.card:hover {
  transform: translateY(-2px);
  border-color: rgba(255, 255, 255, 0.16);
}

/* 포스터 영역 */
.poster-wrap {
  position: relative;
  overflow: hidden;
}

.poster {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transform: scale(1.02);
}

.poster-dim {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    180deg,
    rgba(0, 0, 0, 0.10) 0%,
    rgba(0, 0, 0, 0.60) 70%,
    rgba(0, 0, 0, 0.72) 100%
  );
}

.poster-title {
  position: absolute;
  left: 12px;
  right: 12px;
  bottom: 12px;
  z-index: 2;
}

.musical-name {
  font-size: 16px;
  font-weight: 800;
  letter-spacing: -0.02em;
  text-shadow: 0 10px 30px rgba(0,0,0,0.5);
}

.sub-meta {
  margin-top: 6px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: rgba(238, 240, 246, 0.75);
}

.badge {
  padding: 4px 8px;
  border-radius: 999px;
  background: rgba(99, 102, 241, 0.25);
  border: 1px solid rgba(99, 102, 241, 0.35);
  font-weight: 700;
  letter-spacing: 0.02em;
}

.dot {
  width: 4px;
  height: 4px;
  border-radius: 999px;
  background: rgba(238, 240, 246, 0.55);
}

.time {
  opacity: 0.95;
}

/* 본문 */
.body {
  padding: 14px 14px 12px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.top {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  align-items: center;
}

.author {
  display: flex;
  gap: 10px;
  align-items: center;
}

.avatar {
  width: 38px;
  height: 38px;
  border-radius: 14px;
  display: grid;
  place-items: center;
  background: linear-gradient(135deg, rgba(99,102,241,0.45), rgba(16,185,129,0.35));
  border: 1px solid rgba(255,255,255,0.14);
  box-shadow: 0 12px 30px rgba(0,0,0,0.35);
}

.avatar span {
  font-weight: 900;
}

.author-name {
  font-weight: 800;
  letter-spacing: -0.02em;
}

.author-sub {
  margin-top: 2px;
  font-size: 12px;
  color: rgba(238, 240, 246, 0.65);
}

/* 좋아요 버튼 */
.like-btn {
  height: 38px;
  padding: 0 12px;
  border-radius: 14px;
  border: 1px solid rgba(255,255,255,0.12);
  background: rgba(255,255,255,0.06);
  color: rgba(238, 240, 246, 0.92);
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  transition: transform 0.08s ease, background 0.12s ease, border-color 0.12s ease;
  user-select: none;
}

.like-btn:hover {
  transform: translateY(-1px);
  border-color: rgba(255,255,255,0.18);
}

.like-btn.active {
  background: rgba(244, 63, 94, 0.16);
  border-color: rgba(244, 63, 94, 0.28);
}

.like-btn i {
  font-size: 16px;
}

/* 리뷰 내용: "전체 내용 노출" */
.review-content {
  white-space: pre-wrap;     /* 줄바꿈 유지 */
  word-break: break-word;    /* 긴 단어 줄바꿈 */
  line-height: 1.55;
  font-size: 14px;
  color: rgba(238, 240, 246, 0.88);
  padding: 10px 12px;
  border-radius: 14px;
  background: rgba(0,0,0,0.18);
  border: 1px solid rgba(255,255,255,0.08);
}

/* 하단 정보 */
.bottom {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-top: auto;
}

.pill {
  display: inline-flex;
  gap: 8px;
  align-items: center;
  padding: 8px 10px;
  border-radius: 999px;
  background: rgba(255,255,255,0.06);
  border: 1px solid rgba(255,255,255,0.10);
  color: rgba(238, 240, 246, 0.80);
  font-size: 12px;
}

.pill i {
  opacity: 0.8;
}

.sep {
  opacity: 0.35;
}

/* 빈 상태 */
.empty {
  grid-column: 1 / -1;
  padding: 44px 20px;
  border-radius: 20px;
  border: 1px dashed rgba(255,255,255,0.18);
  background: rgba(255,255,255,0.04);
  text-align: center;
}

.empty-icon {
  font-size: 34px;
}

.empty-title {
  margin-top: 8px;
  font-size: 16px;
  font-weight: 800;
}

.empty-sub {
  margin-top: 6px;
  font-size: 13px;
  color: rgba(238, 240, 246, 0.65);
}

.reset {
  margin-top: 14px;
  height: 42px;
  padding: 0 14px;
  border-radius: 14px;
  border: 1px solid rgba(255,255,255,0.12);
  background: rgba(255,255,255,0.06);
  color: #eef0f6;
  cursor: pointer;
}

/* ✅ 스켈레톤 */
.skeleton {
  position: relative;
}
.skeleton .poster,
.skeleton .line,
.skeleton .chip {
  background: rgba(255,255,255,0.08);
  border: 1px solid rgba(255,255,255,0.08);
  border-radius: 14px;
}
.skeleton .poster {
  width: 100%;
  height: 100%;
  border-radius: 0;
  border: 0;
}

.skeleton .body {
  padding: 14px;
  gap: 10px;
}

.skeleton .row {
  display: flex;
  justify-content: space-between;
  gap: 10px;
}
.skeleton .line {
  height: 12px;
}
.skeleton .chip {
  height: 12px;
}
.w20 { width: 20%; }
.w30 { width: 30%; }
.w60 { width: 60%; }
.w70 { width: 70%; }
.w80 { width: 80%; }
.w90 { width: 90%; }
.w95 { width: 95%; }

.skeleton .footer {
  margin-top: 6px;
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

/* shimmer */
.skeleton::after {
  content: "";
  position: absolute;
  inset: 0;
  background: linear-gradient(
    90deg,
    transparent 0%,
    rgba(255,255,255,0.08) 40%,
    transparent 80%
  );
  transform: translateX(-100%);
  animation: shimmer 1.2s infinite;
}

@keyframes shimmer {
  100% {
    transform: translateX(100%);
  }
}
</style>
