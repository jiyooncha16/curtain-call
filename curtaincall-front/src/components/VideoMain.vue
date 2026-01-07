<template>
  <div class="video-section">
    <div class="carousel-wrapper">

      <!-- 이전 / 다음 -->
      <button
        ref="prevEl"
        class="nav prev"
        :class="{ disabled: isBeginning }"
      >
        <i class="bi bi-chevron-left"></i>
      </button>

      <button
        ref="nextEl"
        class="nav next"
        :class="{ disabled: isEnd }"
      >
        <i class="bi bi-chevron-right"></i>
      </button>

      <Swiper
        :modules="[Navigation]"
        :navigation="{ prevEl, nextEl }"
        :slides-per-view="'auto'"
        :space-between="20"
        @swiper="onSwiper"
        @slideChange="onSlideChange"
      >
        <SwiperSlide
          v-for="video in videoList"
          :key="video.id.videoId"
          class="video-slide"
        >
          <VideoCard :video="video" />
        </SwiperSlide>
      </Swiper>

    </div>
  </div>
</template>

<script setup>
import VideoCard from './common/VideoCard.vue';
import { Swiper, SwiperSlide } from 'swiper/vue'
import { Navigation } from 'swiper/modules'
import 'swiper/css'
import 'swiper/css/navigation'
import { onMounted, ref } from 'vue';
import axios from 'axios';

// 슬라이드 설정
const prevEl = ref(null)
const nextEl = ref(null)
const isBeginning = ref(true)
const isEnd = ref(false)

const onSwiper = (swiper) => {
  isBeginning.value = swiper.isBeginning
  isEnd.value = swiper.isEnd
}
const onSlideChange = (swiper) => {
  isBeginning.value = swiper.isBeginning
  isEnd.value = swiper.isEnd
}

//youtube api
const props = defineProps ({
  keyword : String,
})
const videoList = ref([])

const youtubeSearch = async (keyword) => {
  const url = `https://www.googleapis.com/youtube/v3/search?` +
    new URLSearchParams({
      key: import.meta.env.VITE_YOUTUBE_API_KEY,
      part: 'snippet',
      q: keyword,
      type: 'video',
      maxResults: 10,
    })

  const res = await fetch(url)
  const data = await res.json()

  console.log(data)
  videoList.value = data.items
}


onMounted(() => {
  youtubeSearch(props.keyword)
})

</script>

<style scoped>
/* 슬라이드 크기 */
.swiper-slide {
  width: 320px;
}

/* 섹션 */
.video-section {
  margin-top: 20px;
}

/* 캐러셀 */
.carousel-wrapper {
  position: relative;
  padding: 0 56px;
}

/* ===== 네비 버튼 ===== */
.nav {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  z-index: 10;

  width: 42px;
  height: 42px;
  border-radius: 50%;
  border: none;

  background: rgba(255,255,255,0.95);
  color: #333;
  font-size: 20px;

  cursor: pointer;
  box-shadow: 0 8px 20px rgba(0,0,0,0.15);
  transition: all 0.2s ease;

  display: flex;
  align-items: center;
  justify-content: center;
}

.nav {
  opacity: 0.85;
}
.nav:not(.disabled):hover {
  opacity: 1;
}

/* 위치 */
.nav.prev {
  left: 10px;
}
.nav.next {
  right: 10px;
}

/* hover */
.nav:not(.disabled):hover {
  transform: translateY(-50%) scale(1.08);
}

/* 비활성 */
.disabled {
  opacity: 0.3;
  pointer-events: none;
}

/* ===== VideoCard 공통 추천 ===== */
:deep(.video-card) {
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 10px 24px rgba(0,0,0,0.1);
  transition: transform 0.25s ease, box-shadow 0.25s ease;
  cursor: pointer;
}

:deep(.video-card:hover) {
  transform: translateY(-6px);
  box-shadow: 0 16px 36px rgba(0,0,0,0.14);
}
</style>
