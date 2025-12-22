<template>
  <div class="photo-board" >
    <!-- 헤더 -->
    <div class="pb-header">
      <div class="pb-title">{{ obj.title }}</div>
      <div class="pb-more" @click="clicked">
        <i class="bi bi-arrow-right"></i>
      </div>
    </div>

    <!-- 이미지 그리드 -->
    <div class="pb-grid">
      <div
        class="pb-img-wrapper"
        v-for="(img, idx) in obj.imgs"
        :key="idx"
      >
        <img :src="img.src" />
      </div>
    </div>
  </div>
</template>

<script setup>
import router from '@/router';

defineProps({
  obj: Object
})

const clicked = function() {
    router.push('/review/my')
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
    0 0 0 1px rgba(255, 255, 255, 0.06), /* ⭐ */
    inset 0 1px 0 rgba(255, 255, 255, 0.08);
}

/* 제목 */
.pb-title {
  font-size: 20px;
  font-weight: 700;
  color: #f5f5f5;
}

/* 더보기 아이콘 */
.pb-more {
  color: #c9a24d; /* 골드 */
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
  gap: 10px;
  padding: 12px;
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

/* hover 효과 */
.pb-img-wrapper:hover img {
  transform: scale(1.08);
}

/* 이미지 위 살짝 어두운 오버레이 */
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

/* 반응형 */
@media (max-width: 700px) {
  .pb-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

</style>
