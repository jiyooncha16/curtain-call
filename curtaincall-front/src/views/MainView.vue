<template>
  <div>
    <div class="hero-section" style="margin-bottom :50px;">
      <div class="flex-center" style="gap:10px">
          <div class="shadow photo-board">
              <PhotoBoard :obj="musical" />
          </div>
          <div class="shadow photo-board">
              <PhotoBoard :obj="actor" />
          </div>
      </div>
    </div>
    <div class="shadow">
      <div class="title-text">HOT 작품</div>
      <CardSlide :musicalList="hotMusical"/>
    </div>
    <div class="shadow">
      <div class="title-text">공연 중 작품</div>
      <CardSlide :musicalList="onStageMusical"/>
    </div>
    <div class="shadow">
      <div class="title-text">당신을 위한 추천</div>
      <CardSlide :musicalList="myMusical"/>
    </div>
    <div class="shadow">
      <div class="title-text">인기 영상</div>
      <VideoMain :keyword="keyword"/>
    </div>
    <div class="shadow">
      <div class="title-text">인기 리뷰</div>
      <div class="review-grid">
        <ReviewCard
          v-for="review in reviewList"
          :key="review.id"
          :review="review"
          @toggle-like="toggleLike"
        />
      </div>
    </div>
    
  </div>
</template>

<script setup>
import CardItem from '@/components/common/CardItem.vue';
import CardSlide from '@/components/common/CardSlide.vue';
import PhotoBoard from '@/components/common/PhotoBoard.vue';
import ReviewMainList from '@/components/review/ReviewMainList.vue';
import ReviewCard from '@/components/ReviewCard.vue';
import VideoMain from '@/components/VideoMain.vue';
import axios from 'axios';
import { onMounted, ref } from 'vue';

const keyword = "뮤지컬 인기 영상"

const musical = {
  title: "자주 만나는 작품",
  imgs: [
    { src: new URL('@/assets/데스노트.jpg', import.meta.url).href },
    { src: new URL('@/assets/데스노트.jpg', import.meta.url).href },
    { src: new URL('@/assets/데스노트.jpg', import.meta.url).href }
  ]
}

const actor = {
  title: "자주 만나는 배우",
  imgs: [
    { src: new URL('@/assets/홍광호.jpg', import.meta.url).href },
    { src: new URL('@/assets/홍광호.jpg', import.meta.url).href },
    { src: new URL('@/assets/홍광호.jpg', import.meta.url).href }
  ]
}


const hotMusical = ref([])
const onStageMusical = ref([])
const myMusical = ref([])
const reviewList = ref([])
const toggleLike = ref('')

onMounted(async ()=> {
  try {
    const hotRes = await axios.get('/api/musicals/search', {
      params: { orderBy: 'likes', 
        order:"desc", 
        page: 0, 
        size: 10 }
    })
    console.log('HOT 작품', hotRes.data)
    hotMusical.value = hotRes.data

    const onStageRes = await axios.get('/api/musicals/search', {
      params: {
        date: "2025-12-18", 
        order:"desc", 
        page: 0, 
        size: 10 }
    })
    console.log('공연 중 작품', onStageRes.data)
    onStageMusical.value = onStageRes.data

    const myRes = await axios.get('/api/musicals/search', {
        params: { orderBy: 'random', 
        order:"asc", 
        page: 0, 
        size: 10 }
    })
    console.log('맞춤 작품', myRes.data)
    myMusical.value = myRes.data

    //리뷰
    const reviewRes = await axios.get('/api/reviews/top')
    console.log('맞춤 작품', reviewRes.data)
    reviewList.value = reviewRes.data

  } catch (e) {
    console.error('API 에러', e)
  }
})
</script>

<style scoped>

.wrapper {
    background-color: #5000009e;
    padding : 10px;
    margin : 5px;
    width : 100%;
}
.circle {
  border-radius: 50%;
  width:80px;
  height:80px;
  background-color: white;
  color:#5000009e;
  font-weight: bold; 
  font-size: 30px; 
  align-items: center;
  justify-content: center;
}
.title-text {
  margin-bottom: 10px;
  margin-left: 10px;
}
.photo-board {
  background-color: white;
  position: relative;
  background: transparent; /* 실제 배경은 가상요소가 담당 */
  overflow: hidden;
}
.hero-section {
  background: linear-gradient(135deg, #50000041, #460000e8);
  padding: 20px 20px;
  border-radius: 20px;
}
.review-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr); /* 2열 */
  gap: 20px; /* 카드 사이 간격 */
}
</style>