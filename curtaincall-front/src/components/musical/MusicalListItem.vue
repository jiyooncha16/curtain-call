<template>
  <article class="card">
    <!-- 포스터 -->
    <div class="poster">
      <img :src="`/${musical.image}`" :alt="musical.title" />
    </div>

    <!-- 정보 -->
    <div class="card-body">
      <h3 class="title">{{ musical.title }}</h3>
      <p class="period">
        {{ musical.startDate }} ~ {{ musical.endDate }}
      </p>
      <p class="theater">{{ musical.theater }}</p>
    </div>
  </article>
</template>

<script setup>
defineProps({
  musical: Object
})
</script>


<style scoped>
.searchResultItem {
    border: 2px lightgray solid;
    border-radius: 5px;
    margin: 10px 0;
    overflow: hidden;
    cursor: pointer;
    /* 🔥 클릭 느낌 */
    transition: transform 0.2s, box-shadow 0.2s;
}

.searchResultItem:hover {
    transform: translateY(-3px);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.img-box {
    width: 150px;
    aspect-ratio: 3/4;
}

img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}
.card {
  background: #fff;
  border-radius: 18px;
  overflow: hidden;
  box-shadow: 0 12px 30px rgba(0,0,0,0.08);
  transition: all 0.25s ease;
  cursor: pointer;
}

.card:hover {
  transform: translateY(-6px);
  box-shadow: 0 20px 40px rgba(0,0,0,0.12);
}

/* 포스터 */
.poster {
  width: 100%;
  aspect-ratio: 3 / 4;
  overflow: hidden;
}

.poster img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}
.poster:hover img {
  transform: scale(1.05);
}

/* 내용 */
.card-body {
  padding: 16px;
}

.title {
  font-size: 16px;
  font-weight: 700;
  margin-bottom: 6px;
  line-height: 1.3;
}

.period {
  font-size: 13px;
  color: #666;
  margin-bottom: 4px;
}

.theater {
  font-size: 13px;
  color: #888;
  margin: 0;
}
</style>
