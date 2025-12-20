<template>
  <div class="page">
    <!-- Header -->
    <div class="page-header shadow">
      <div>
        <div class="title">뮤지컬 리뷰</div>
        <div class="subtitle">관람 후기를 모아봤어요</div>
      </div>

      <div class="controls">
        <div class="search">
          <i class="bi bi-search"></i>
          <input
            v-model="q"
            type="text"
            placeholder="작품명/리뷰 제목/작성자 검색"
            @keyup.enter="onSearch"
          />
        </div>

        <select v-model="sort" class="select" @change="onSearch">
          <option value="latest">최신순</option>
          <option value="oldest">오래된순</option>
        </select>
      </div>
    </div>

    <!-- List -->
    <div class="list">
      <article
        v-for="review in filteredReviews"
        :key="review.reviewId"
        class="review-card shadow"
        @click="$emit('clickReview', review)"
      >
        <!-- Poster -->
        <div class="poster">
          <img :src="review.posterUrl" :alt="review.musicalTitle" />
        </div>

        <!-- Content -->
        <div class="content">
          <div class="top">
            <div class="musical-title">{{ review.musicalTitle }}</div>
            <div class="time">
              <i class="bi bi-clock"></i>
              {{ formatDateTime(review.createdAt) }}
            </div>
          </div>

          <div class="review-title">{{ review.reviewTitle }}</div>

          <p class="review-body">
            {{ review.reviewContent }}
          </p>

          <div class="meta">
            <div class="author">
              <div class="avatar">{{ (review.writer || '?').slice(0, 1) }}</div>
              <span class="name">{{ review.writer }}</span>
            </div>

            <button class="mini-btn" type="button" @click.stop="$emit('openDetail', review)">
              자세히 <i class="bi bi-arrow-right"></i>
            </button>
          </div>
        </div>
      </article>

      <!-- Empty -->
      <div v-if="filteredReviews.length === 0" class="empty shadow">
        <div class="empty-title">검색 결과가 없어요</div>
        <div class="empty-desc">다른 키워드로 다시 찾아보세요.</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from "vue";

const props = defineProps({
  // reviewId, posterUrl, musicalTitle, reviewTitle, reviewContent, writer, createdAt
  reviews: {
    type: Array,
    default: () => [],
  },
});

defineEmits(["clickReview", "openDetail"]);

const q = ref("");
const sort = ref("latest");

const normalize = (s) => (s ?? "").toString().trim().toLowerCase();

const filteredReviews = computed(() => {
  const keyword = normalize(q.value);

  let arr = [...props.reviews];

  // search
  if (keyword) {
    arr = arr.filter((r) => {
      const hay = [
        r.musicalTitle,
        r.reviewTitle,
        r.reviewContent,
        r.writer,
      ]
        .map(normalize)
        .join(" ");
      return hay.includes(keyword);
    });
  }

  // sort
  arr.sort((a, b) => {
    const ta = new Date(a.createdAt).getTime();
    const tb = new Date(b.createdAt).getTime();
    return sort.value === "latest" ? tb - ta : ta - tb;
  });

  return arr;
});

const onSearch = () => {
  // 서버 검색이면 여기에서 axios 호출하도록 바꾸면 됨
};

const pad = (n) => String(n).padStart(2, "0");
const formatDateTime = (value) => {
  if (!value) return "-";
  const d = new Date(value);
  if (Number.isNaN(d.getTime())) return String(value);

  // 2025-12-20 12:34
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(
    d.getHours()
  )}:${pad(d.getMinutes())}`;
};
</script>

<style scoped>
.page {
  max-width: 1100px;
  margin: 0 auto;
  padding: 20px 14px 60px;
}

.shadow {
  border-radius: 16px;
  box-shadow: 0 10px 28px rgba(0, 0, 0, 0.12);
  background: #fff;
}

.page-header {
  padding: 18px 18px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 14px;
  position: sticky;
  top: 10px;
  z-index: 10;
}

.title {
  font-size: 22px;
  font-weight: 800;
  letter-spacing: -0.3px;
}
.subtitle {
  margin-top: 4px;
  font-size: 13px;
  color: #667085;
}

.controls {
  display: flex;
  align-items: center;
  gap: 10px;
}

.search {
  display: flex;
  align-items: center;
  gap: 8px;
  border: 1px solid #e6e8ee;
  padding: 10px 12px;
  border-radius: 12px;
  min-width: 320px;
}
.search i {
  color: #98a2b3;
}
.search input {
  border: none;
  outline: none;
  width: 100%;
  font-size: 14px;
}
.select {
  border: 1px solid #e6e8ee;
  padding: 10px 12px;
  border-radius: 12px;
  outline: none;
  font-size: 14px;
  background: #fff;
}

.list {
  margin-top: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.review-card {
  display: grid;
  grid-template-columns: 130px 1fr;
  gap: 16px;
  padding: 14px;
  cursor: pointer;
  transition: transform 0.08s ease, box-shadow 0.08s ease;
}
.review-card:hover {
  transform: translateY(-1px);
  box-shadow: 0 14px 34px rgba(0, 0, 0, 0.14);
}

.poster {
  width: 130px;
  height: 175px;
  border-radius: 14px;
  overflow: hidden;
  background: #f2f4f7;
  border: 1px solid #eef0f4;
}
.poster img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 포스터는 꽉 차게 */
  display: block;
}

.content {
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
}
.musical-title {
  font-weight: 800;
  font-size: 18px;
  letter-spacing: -0.2px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.time {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #667085;
  white-space: nowrap;
}
.review-title {
  font-size: 15px;
  font-weight: 700;
  color: #111827;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.review-body {
  margin: 0;
  font-size: 14px;
  color: #344054;
  line-height: 1.55;

  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3; /* 내용 3줄까지만 */
  overflow: hidden;
}

.meta {
  margin-top: auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  padding-top: 6px;
}

.author {
  display: flex;
  align-items: center;
  gap: 8px;
}
.avatar {
  width: 28px;
  height: 28px;
  border-radius: 10px;
  background: #111827;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 800;
}
.name {
  font-size: 13px;
  color: #111827;
  font-weight: 600;
}

.mini-btn {
  border: 1px solid #e6e8ee;
  background: #fff;
  padding: 8px 10px;
  border-radius: 12px;
  font-size: 13px;
  cursor: pointer;
}
.mini-btn:hover {
  background: #f8fafc;
}

/* empty */
.empty {
  padding: 36px 14px;
  text-align: center;
}
.empty-title {
  font-weight: 800;
  font-size: 16px;
}
.empty-desc {
  margin-top: 6px;
  color: #667085;
  font-size: 13px;
}

/* responsive */
@media (max-width: 720px) {
  .page-header {
    flex-direction: column;
    align-items: stretch;
  }
  .controls {
    flex-direction: column;
    align-items: stretch;
  }
  .search {
    min-width: unset;
  }
  .review-card {
    grid-template-columns: 110px 1fr;
  }
  .poster {
    width: 110px;
    height: 150px;
  }
}
</style>
