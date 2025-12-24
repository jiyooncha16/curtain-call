<template>
  <article class="card" @click="clicked(item.musicalId)">
    <div class="poster">
      <img :src="`/${item.image}`" :alt="item.title" />

      <!-- 하단 오버레이 -->
      <div class="overlay">
        <div class="title" :title="item.title">
          {{ item.title }}
        </div>
      </div>
    </div>
  </article>
</template>


<script setup>
import router from '@/router';

const props = defineProps({
  item: Object
})
console.log(props.item)
const clicked = function (id) {
  router.push(`/musical/${id}`)
}
</script>


<style scoped>
/* ===== 카드 ===== */
.card {
  width: 100%;
  max-width: 180px;
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;

  background: #fff;
  box-shadow: 0 10px 24px rgba(0, 0, 0, 0.08);
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.card:hover {
  transform: translateY(-6px);
  box-shadow: 0 16px 36px rgba(0, 0, 0, 0.14);
}

/* ===== 포스터 ===== */
.poster {
  position: relative;
  width: 100%;
  aspect-ratio: 3 / 4;
  overflow: hidden;
}

.poster img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.35s ease;
}

/* 포스터 hover 확대 */
.poster:hover img {
  transform: scale(1.06);
}

/* ===== 하단 오버레이 ===== */
.overlay {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;

  padding: 28px 12px 12px;

  /* 🔥 핵심: 검은색 흐림 그라데이션 */
  background: linear-gradient(to top,
      rgba(0, 0, 0, 0.75),
      rgba(0, 0, 0, 0.35),
      rgba(0, 0, 0, 0));
}

/* ===== 제목 ===== */
.title {
  color: #fff;
  font-size: 14px;
  font-weight: 700;
  line-height: 1.3;
  text-align: center;

  /* 두 줄 말줄임 */
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;

  text-shadow: 0 2px 6px rgba(0, 0, 0, 0.6);
}
</style>
