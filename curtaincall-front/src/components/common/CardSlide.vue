<template>
  <div class="carousel-wrapper">
    <!-- 🔥 Swiper -->
    <Swiper :modules="[Navigation]" :navigation="{ prevEl, nextEl }" :slides-per-view="'auto'" :space-between="18"
      @swiper="onSwiper" @slideChange="onSlideChange">
      <SwiperSlide v-for="musical in musicalList" :key="musical.musicalId">
        <CardItem :item="musical" />
      </SwiperSlide>
    </Swiper>

    <!-- 🔥 오른쪽 하단 네비 버튼 -->
    <div class="bottom-nav">
      <button ref="prevEl" class="nav-btn" :class="{ disabled: isBeginning }" aria-label="이전">
        <i class="bi bi-chevron-left"></i>
      </button>

      <button ref="nextEl" class="nav-btn" :class="{ disabled: isEnd }" aria-label="다음">
        <i class="bi bi-chevron-right"></i>
      </button>
    </div>
  </div>
</template>

<script setup>
import CardItem from './CardItem.vue'

import { Swiper, SwiperSlide } from 'swiper/vue'
import { Navigation } from 'swiper/modules'
import 'swiper/css'
import 'swiper/css/navigation'

import { ref, nextTick } from 'vue'

defineProps({
  musicalList: {
    type: Array,
    required: true
  }
})

const prevEl = ref(null)
const nextEl = ref(null)

const isBeginning = ref(true)
const isEnd = ref(false)

const onSwiper = async (swiper) => {
  await nextTick()
  swiper.update()

  isBeginning.value = swiper.isBeginning
  isEnd.value = swiper.isEnd
}

const onSlideChange = (swiper) => {
  isBeginning.value = swiper.isBeginning
  isEnd.value = swiper.isEnd
}
</script>

<style scoped>
/* ===== 카드 크기 ===== */
.swiper-slide {
  width: 150px;
}

/* ===== 전체 래퍼 ===== */
.carousel-wrapper {
  position: relative;
  padding-bottom: 36px;
  /* 🔥 버튼 공간 확보 */
}

/* ===== 오른쪽 하단 버튼 ===== */
.bottom-nav {
  position: absolute;
  right: 12px;
  bottom: 0;

  display: flex;
  gap: 10px;
}

/* 버튼 */
.nav-btn {
  width: 20px;
  height: 20px;

  border: none;

  background: #fff;
  color: #333;

  display: flex;
  align-items: center;
  justify-content: center;

  cursor: pointer;

  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.18);
  transition: transform 0.15s ease, box-shadow 0.15s ease;
}

/* 
.nav-btn:hover:not(.disabled) {
  transform: scale(1.08);
  box-shadow: 0 10px 26px rgba(0, 0, 0, 0.25);
} */

.nav-btn.disabled {
  opacity: 0.35;
  pointer-events: none;
}

/* 아이콘 */
.nav-btn i {
  font-size: 18px;
}
</style>
