<template>
  <a
    class="video-card"
    :href="`https://www.youtube.com/watch?v=${video.id.videoId}`"
    target="_blank"
    rel="noopener"
  >
    <div class="thumb">
      <img :src="video.snippet.thumbnails.high.url" />

      <!-- 재생 아이콘 -->
      <div class="play">
        <i class="bi bi-play-fill"></i>
      </div>

      <!-- 하단 오버레이 -->
      <div class="overlay">
        <div class="title" :title="videoTitle">
          {{ videoTitle }}
        </div>
      </div>
    </div>
  </a>
</template>


<script setup>
import _ from 'lodash'
import { computed } from 'vue'

const props = defineProps({
  video: Object
})

// 제목 escape 문자 처리
const videoTitle = computed(() => {
  return _.unescape(props.video.snippet.title)
})
</script>

<style scoped>
/* 카드 링크 */
.video-card {
  display: block;
  border-radius: 16px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 10px 24px rgba(0,0,0,0.1);
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.video-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 16px 36px rgba(0,0,0,0.14);
}

/* 썸네일 */
.thumb {
  position: relative;
  aspect-ratio: 16 / 9;
  overflow: hidden;
}

.thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.35s ease;
}

/* hover 시 썸네일 확대 */
.video-card:hover img {
  transform: scale(1.06);
}

/* ▶ 재생 아이콘 */
.play {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;

  color: white;
  font-size: 48px;
  opacity: 0.9;

  transition: opacity 0.25s ease, transform 0.25s ease;
  text-shadow: 0 4px 12px rgba(0,0,0,0.6);
}

/* hover 시 아이콘 반응 */
.video-card:hover .play {
  opacity: 1;
  transform: scale(1.05);
}

/* 하단 그라데이션 오버레이 */
.overlay {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;

  padding: 28px 14px 14px;

  background: linear-gradient(
    to top,
    rgba(0,0,0,0.8),
    rgba(0,0,0,0.45),
    rgba(0,0,0,0)
  );
}

/* 제목 */
.title {
  color: #fff;
  font-size: 14px;
  font-weight: 700;
  line-height: 1.35;

  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;

  text-shadow: 0 2px 6px rgba(0,0,0,0.6);
}
</style>
