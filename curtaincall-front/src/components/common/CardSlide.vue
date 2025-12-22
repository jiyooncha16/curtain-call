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
          <SwiperSlide v-for="musical in musicalList" :key="musical.musicalId">
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
import { ref } from 'vue';

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
.swiper-slide {
  width: 140px;
  height: 210px;
}

/* ===== 캐러셀 전체 ===== */
.carousel-wrapper {
  position: relative;
  max-width: 1500px;
  padding: 20px 56px;

  border-radius: 18px;
  background: linear-gradient(
    180deg,
    rgba(0,0,0,0.03),
    rgba(0,0,0,0)
  );
}

/* ===== 네비 버튼 ===== */
.nav {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  z-index: 20;

  width: 44px;
  height: 44px;
  border-radius: 50%;
  border: none;

  background: rgba(20, 20, 25, 0.85);
  backdrop-filter: blur(6px);

  color: #fff;
  font-size: 20px;
  cursor: pointer;

  box-shadow:
    0 6px 18px rgba(0,0,0,0.45),
    inset 0 1px 0 rgba(255,255,255,0.15);

  opacity: 0.6;
  transition: all 0.2s ease;
}

/* 위치 */
.nav.prev { left: 8px; }
.nav.next { right: 8px; }

/* hover */
.nav:not(.disabled):hover {
  opacity: 1;
  transform: translateY(-50%) scale(1.05);
}

/* 비활성화 */
.disabled {
  opacity: 0.25;
  pointer-events: none;
}

/* 아이콘 중앙 정렬 */
button {
  display: flex;
  align-items: center;
  justify-content: center;
}

</style>