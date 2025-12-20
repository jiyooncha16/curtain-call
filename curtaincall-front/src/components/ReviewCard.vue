<template>
  <article class="card">
    <!-- 포스터 -->
    <div class="poster-wrap">
      <img
        class="poster"
        :src="review.posterUrl"
        :alt="review.musicalTitle"
      />
      <div class="poster-title">
        <div class="musical-name">{{ review.musicalTitle }}</div>
        <div class="sub-meta">
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
            <!-- <div class="author-sub">{{ formatDate(review.createdAt) }}</div> -->

            <!-- ⭐ 별점 -->
            <div class="rating">
                <i
                v-for="n in 5"
                :key="n"
                class="bi"
                :class="n <= review.rating ? 'bi-star-fill' : 'bi-star'"
                ></i>
                <span class="rating-num">{{ review.rating }}</span>
            </div>
          </div>
        </div>

        <button
          class="like-btn"
          :class="{ active: review.liked }"
          @click="$emit('toggle-like', review)"
          type="button"
        >
          <i :class="review.liked ? 'bi bi-heart-fill' : 'bi bi-heart'"></i>
          <span>{{ review.likeCount }}</span>
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
          <b>{{ review.authorName }}</b>
        </div>

        <div class="pill">
          <i class="bi bi-film"></i>
          <span class="sep"> | </span>
          <b>{{ review.musicalTitle }}</b>
        </div>
      </div>
    </div>
  </article>
</template>

<script setup>
defineProps({
  review: {
    type: Object,
    required: true,
  },
})

defineEmits(['toggle-like'])

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
.review-content {
  font-size: 14px;
  line-height: 1.6;
  color: #333;
  background: #fafafa;
  padding: 12px;
  border-radius: 10px;
  white-space: pre-wrap;
}
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

</style>