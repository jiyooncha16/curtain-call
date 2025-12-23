<template>
  <div class="carousel-wrapper">

    <!-- 이전 / 다음 -->
    <button
      ref="prevEl"
      class="nav prev"
      :class="{ disabled: isBeginning }"
      aria-label="이전"
    >
      <i class="bi bi-chevron-left"></i>
    </button>

    <button
      ref="nextEl"
      class="nav next"
      :class="{ disabled: isEnd }"
      aria-label="다음"
    >
      <i class="bi bi-chevron-right"></i>
    </button>

    <Swiper
      :modules="[Navigation]"
      :navigation="{ prevEl, nextEl }"
      :slides-per-view="'auto'"
      :space-between="18"
      @swiper="onSwiper"
      @slideChange="onSlideChange"
    >
      <SwiperSlide
        v-for="musical in musicalList"
        :key="musical.musicalId"
      >
        <CardItem :item="musical" />
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
import { nextTick, ref } from 'vue';

const prevEl = ref(null)
const nextEl = ref(null)

const isBeginning = ref(true)
const isEnd = ref(false)

const onSwiper = async (swiper) => {
  await nextTick()        // DOM 렌더 완료 대기
  swiper.update()         // 슬라이드 재계산

  isBeginning.value = swiper.isBeginning
  isEnd.value = swiper.isEnd
}

const onSlideChange = (swiper) => {
  isBeginning.value = swiper.isBeginning
  isEnd.value = swiper.isEnd
}

const props = defineProps({
  musicalList : Array
})
</script>
<style scoped>
/* ===== 슬라이드 크기 ===== */
.swiper-slide {
  width: 150px;
  height: auto;
}

/* ===== 캐러셀 전체 =====
.carousel-wrapper {
  position: relative;
  padding: 24px 56px;

  background: #ffffff;
  border-radius: 20px;

  box-shadow:
    0 12px 32px rgba(0,0,0,0.08);

  overflow: hidden;
} */

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
  font-size: 18px;

  cursor: pointer;

  box-shadow:
    0 8px 20px rgba(0,0,0,0.15);

  transition: all 0.2s ease;
}

/* 위치 */
.nav.prev { left: 10px; }
.nav.next { right: 10px; }

/* hover */
.nav:not(.disabled):hover {
  transform: translateY(-50%) scale(1.08);
  box-shadow:
    0 12px 28px rgba(0,0,0,0.22);
}

/* 비활성화 */
.nav.disabled {
  opacity: 0.35;
  pointer-events: none;
}

/* 아이콘 정렬 */
button {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
