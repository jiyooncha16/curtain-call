<template>
    <div class="carousel-wrapper">
        <button ref="prevEl" class="nav prev" :class="{disabled : isBeginning}"><i class="bi bi-arrow-left"></i></button>
        <button ref="nextEl" class="nav next" :class="{disabled : isEnd}" ><i class="bi bi-arrow-right"></i></button>
        <Swiper
            :modules="[Navigation]"
            :navigation="{prevEl, nextEl}"
            :slides-per-view="'auto'"
            :space-between="16"
            @swiper="onSwiper"
            @slideChange="onSlideChange"
        >
          <!-- <SwiperSlide v-for="movie in movies" :key="movie.id">
            <img :src="movie.poster" class="poster" /> -->
          <SwiperSlide v-for="num in 10" :key="num">
              <CardItem />
          </SwiperSlide>
        </Swiper>
    </div>
</template>

<script setup>
import CardItem from './CardItem.vue';

// 카드 스와이프 위한 스와이퍼
import { Swiper, SwiperSlide } from 'swiper/vue'
import { Navigation } from 'swiper/modules'
import 'swiper/css'
import 'swiper/css/navigation'
import { ref } from 'vue';

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
</script>

<style scoped>

.swiper-slide {
  width: 140px;
}


/* 전체 래퍼 */
.carousel-wrapper {
  position: relative;
  max-width: 1500px;
  padding: 0 60px;
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
  left: 5px;
}

.nav.next {
  right: 5px;
}

.disabled {
  opacity: 0;          /* 없어지기 */
  pointer-events: none;  /* 클릭 차단 */
  cursor: default;
}

.nav:not(.disabled):hover {
  opacity: 0.8;
}

button {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>