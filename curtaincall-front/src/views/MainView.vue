<template>
  <div>
    <div class="container flex-center"> 
      <!-- <div class="wrapper main-text" style="color:white; align-content: center; flex-direction: column;">
        <div>내가 본 작품</div>
        <div class="circle flex">40</div>
      </div> -->
        <PhotoBoard :obj="musical"/>
        <PhotoBoard :obj="actor"/>
    </div>
    <!-- 나중에 props 내려보내야됨 지금은 다같이 움직임 -->
    <div class="container">
      <div class="title-text">HOT 작품</div>
      <CardSlide :musicalList="hotMusical"/>
    </div>
    <div class="container">
      <div class="title-text">공연 중 작품</div>
      <CardSlide :musicalList="onStageMusical"/>
    </div>
    <div class="container">
      <div class="title-text">당신을 위한 작품</div>
      <CardSlide :musicalList="myMusical"/>
    </div>
    <div class="container">
      <div class="title-text">인기 영상</div>
      <VideoMain :keyword="keyword"/>
    </div>
    <div class="container">
      <div class="title-text">인기 리뷰</div>
      <ReviewMainList />
    </div>
    
  </div>
</template>

<script setup>
import CardItem from '@/components/common/CardItem.vue';
import CardSlide from '@/components/common/CardSlide.vue';
import PhotoBoard from '@/components/common/PhotoBoard.vue';
import ReviewMainList from '@/components/review/ReviewMainList.vue';
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
</style>