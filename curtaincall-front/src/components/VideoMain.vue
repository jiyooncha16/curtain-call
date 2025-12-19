<template>
    <div>
        <div class="carousel-wrapper">
            <button ref="nextEl" class="nav prev" :class="{disabled : isBeginning}"><i class="bi bi-arrow-left"></i></button>
            <button ref="prevEl" class="nav next" :class="{disabled : isEnd}" ><i class="bi bi-arrow-right"></i></button>
            <Swiper
                :modules="[Navigation]"
                :navigation="{
                    prevEl: '.prev',
                    nextEl: '.next'
                    }"
                :slides-per-view="'auto'"
                :space-between="16"
                @swiper="onSwiper"
                @slideChange="onSlideChange"
            >
            <!-- <SwiperSlide v-for="movie in movies" :key="movie.id">
                <img :src="movie.poster" class="poster" /> -->
            <SwiperSlide v-for="video in videoList" :key="video.channelId">
                    <VideoCard :video="video"/>
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
const youtubeSearch = function(keyword) {

    axios.get("https://www.googleapis.com/youtube/v3/search", {
      params:{
        key: import.meta.env.VITE_YOUTUBE_API_KEY, // 키는 필수
        part : `snippet`, // api 검색하려면 필수
        q : keyword, // 키워드 변수
        type : `video`, // 명시하지 않으면 채널, 플레이리스트도 검색됨
        maxResults : 10 // 기본 5, 최대 50
      }
    })
    .then((response)=> {
      console.log(response.data)
      videoList.value = response.data.items // 응답을 videoList에 담음
    })
}

onMounted(() => {
  youtubeSearch(props.keyword)
})

</script>

<style scoped>
.swiper-slide {
  width:350px;
}

/* 전체 래퍼 */
.carousel-wrapper {
  position: relative;
}

/* 버튼들 */
.nav {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  z-index: 10;

  width: 44px;
  height: 44px;
  border-radius: 50%;
  border: none;

  background: rgba(0,0,0,0.7);
  color: white;
  font-size: 22px;
  cursor: pointer;
  opacity: 0.4;
  transition: opacity 0.2s ease;
}

/* 버튼 위치 */
.nav.prev {
  left: -50px;
}

.nav.next {
  right: -50px;
}

.disabled {
  opacity: 0;          /* 없어지기 */
  pointer-events: none;  /* 클릭 차단 */
  cursor: default;
}

.nav:not(.disabled):hover {
  opacity: 0.8;
}
</style>