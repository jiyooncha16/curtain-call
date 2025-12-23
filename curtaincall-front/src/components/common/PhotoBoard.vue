<template>
  <div class="photo-board">
    <!-- 헤더 -->
    <div class="pb-header">
      <div class="pb-title">{{ obj.title }}</div>
      <div class="pb-more" @click="clicked">
        <i class="bi bi-arrow-right"></i>
      </div>
    </div>

    <!-- 이미지 영역 -->
    <div class="pb-grid">
      <!-- ✅ 이미지 있을 때 -->
      <template v-if="obj.imgs && obj.imgs.length">
        <div class="pb-img-wrapper" v-for="(item, idx) in obj.imgs" :key="idx" @click="detail(item.id, obj.title)">
          <img :src="item.src" />
        </div>
      </template>

      <!-- ✅ 이미지 없을 때 (틀 유지) -->
      <div v-else class="pb-empty">
        <i class="bi bi-image"></i>
        <p>아직 표시할 항목이 없습니다</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import router from '@/router'

defineProps({
  obj: {
    type: Object,
    required: true,
  },
})

const clicked = () => {
  // 필요하면 조건 분기 가능
  router.push('/review/my')
}

const detail = (id, title) => {
  if (title === '최근 본 뮤지컬') {
    router.push(`/musical/${id}`)
  } else {
    router.push(`/actor/${id}`)
  }
}
</script>

<style scoped>
/* 전체 카드 */
.photo-board {
  width: 100%;
  max-width: 900px;
  margin: 0 auto;
  border-radius: 18px;
  overflow: hidden;
  background: #3a3f52;
  box-shadow:
    0 10px 10px rgba(0, 0, 0, 0.35),
    0 0 0 1px rgba(0, 0, 0, 0.25);
}

/* 헤더 */
.pb-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  box-shadow:
    0 20px 50px rgba(0, 0, 0, 0.55),
    0 0 0 1px rgba(255, 255, 255, 0.06),
    inset 0 1px 0 rgba(255, 255, 255, 0.08);
}

/* 제목 */
.pb-title {
  font-size: 20px;
  font-weight: 700;
  color: #f5f5f5;
}

/* 더보기 */
.pb-more {
  color: #c9a24d;
  font-size: 18px;
  cursor: pointer;
  transition: transform 0.2s ease, opacity 0.2s ease;
}

.pb-more:hover {
  transform: translateX(4px);
  opacity: 0.85;
}

/* 이미지 그리드 */
.pb-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 18px;
  padding: 20px;
  height: 220px;
}

/* 이미지 wrapper */
.pb-img-wrapper {
  width: 100%;
  aspect-ratio: 2 / 3;
  overflow: hidden;
  border-radius: 12px;
  cursor: pointer;
  position: relative;
}

/* 이미지 */
.pb-img-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform 0.35s ease;
}

.pb-img-wrapper:hover img {
  transform: scale(1.08);
}

/* hover overlay */
.pb-img-wrapper::after {
  content: '';
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.15);
  opacity: 0;
  transition: opacity 0.2s ease;
}

.pb-img-wrapper:hover::after {
  opacity: 1;
}

/* ✅ 비어있을 때 */
.pb-empty {
  grid-column: 1 / -1;
  height: 220px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.08);

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  color: rgba(255, 255, 255, 0.6);
  font-size: 14px;
  gap: 8px;
}

.pb-empty i {
  font-size: 36px;
  opacity: 0.7;
}

/* 반응형 */
@media (max-width: 700px) {
  .pb-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
