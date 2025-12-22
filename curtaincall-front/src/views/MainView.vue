<template>
  <div>
    <div class="hero-section" style="margin-bottom:50px;">
  
  <!-- 로그인 상태 -->
  <div v-if="isLogin && me" class="hero-user shadow">

    <!-- 상단 인사 -->
    <div class="hero-header">
      <div class="hello">
        <span class="nickname">{{ me.nickname }}</span>님 안녕하세요 👋
      </div>

      <button class="mypage-btn" @click="goMyPage">
        마이페이지 →
      </button>
    </div>

    <!-- 개인화 콘텐츠 -->
    <div class="hero-content">

      <!-- 최근 본 뮤지컬 -->
      <div class="photo-wrapper">
        <PhotoBoard
          v-if="me.recentMusicals?.length"
          :obj="{
            title: '최근 본 뮤지컬',
            imgs: me.recentMusicals.map(m => ({
              src: '/' + m.image
            }))
          }"
        />
      </div>

      <!-- 자주 본 배우 -->
      <div class="photo-wrapper">
        <PhotoBoard
          v-if="me.favoriteActors?.length"
          :obj="{
            title: '자주 본 배우',
            imgs: me.favoriteActors.map(a => ({
              src: '/' + a.image
            }))
          }"
        />
      </div>
  </div>
</div>

  <!-- 비로그인 상태 -->
  <div v-else class="login-cta shadow">
    <h2>🎭 내 뮤지컬 기록을 남겨보세요</h2>
    <p>
      로그인하고<br />
      관람한 뮤지컬을 기록해보세요.
    </p>

    <button class="login-btn" @click="goLogin">
      로그인하러 가기
    </button>
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
import { storeToRefs } from 'pinia'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const { isLogin } = storeToRefs(authStore)

const router = useRouter()

const goLogin = () => {
  router.push('/login')
}


const keyword = "뮤지컬 인기 영상"

// const musical = {
//   title: "자주 만나는 작품",
//   imgs: [
//     { src: new URL('@/assets/데스노트.jpg', import.meta.url).href },
//     { src: new URL('@/assets/데스노트.jpg', import.meta.url).href },
//     { src: new URL('@/assets/데스노트.jpg', import.meta.url).href }
//   ]
// }

// const actor = {
//   title: "자주 만나는 배우",
//   imgs: [
//     { src: new URL('@/assets/홍광호.jpg', import.meta.url).href },
//     { src: new URL('@/assets/홍광호.jpg', import.meta.url).href },
//     { src: new URL('@/assets/홍광호.jpg', import.meta.url).href }
//   ]
// }


const hotMusical = ref([])
const onStageMusical = ref([])
const myMusical = ref([])
const reviewList = ref([])
const toggleLike = ref('')
const me = ref([])

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
    console.log('맞춤 작품', myRes.data[0])
    myMusical.value = myRes.data

    //리뷰
    const reviewRes = await axios.get('/api/reviews/top')
    console.log('맞춤 작품', reviewRes.data)
    reviewList.value = reviewRes.data

    // my 정보
    const res = await axios.get('/api/user/main', {
      headers: {
        Authorization: `Bearer ${authStore.token}`
      }
    })
    me.value = res.data
    console.log('me:', me.value)

  } catch (e) {
    console.error('API 에러', e)
  }
})

const goMyPage = function() {
  router.push('/myPage')
}
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
  /* background-color: white; */
  position: relative;
  /* background: transparent; 실제 배경은 가상요소가 담당 */
  overflow: hidden;
}
/* .hero-section {
  background: linear-gradient(135deg, #50000041, #460000e8);
  padding: 20px 20px;
  border-radius: 20px;
} */
.review-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr); /* 2열 */
  gap: 20px; /* 카드 사이 간격 */
}

/* 비로그인 CTA 전체 박스 */
.login-cta {
  width: 100%;
  max-width: 900px;
  margin: 0 auto;
  padding: 60px 40px;
  border-radius: 20px;
  background: linear-gradient(
  135deg,
  #1c1f2b,
  #2a2f45,
  #1f2a3a
);
  color: #f5f5f5;
}

/* 내부 정렬 */
.cta-inner {
  text-align: center;
}

/* 타이틀 */
.login-cta h2 {
  font-size: 26px;
  font-weight: 700;
  margin-bottom: 16px;
}

/* 설명 텍스트 */
.login-cta p {
  font-size: 16px;
  line-height: 1.6;
  color: #d0d0d0;
}
/* 로그인 버튼 - Gold */
.login-btn {
  margin-top: 30px;
  padding: 12px 36px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 999px;
  border: none;
  cursor: pointer;

  background: linear-gradient(
    135deg,
    #c9a24d,
    #e0b85c
  );
  color: #1e1f26;

  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 25px rgba(201, 162, 77, 0.45);
}

.login-btn:active {
  transform: translateY(0);
  box-shadow: 0 6px 15px rgba(201, 162, 77, 0.35);
}


/* 로그인 후 히어로 전체 */
.hero-user {
  margin-bottom: 50px;
  padding: 32px;
  border-radius: 22px;
  background: linear-gradient(
    135deg,
    #1b1d24,
    #232631
  );
  color: #f5f5f5;
}

/* 상단 헤더 */
.hero-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 26px;
}

/* 인사 */
.hello {
  font-size: 22px;
  font-weight: 600;
}

.nickname {
  color: #c9a24d; /* 골드 포인트 */
  font-weight: 700;
}

/* 마이페이지 버튼 */
.mypage-btn {
  padding: 8px 18px;
  border-radius: 999px;
  border: 1px solid #c9a24d;
  background: transparent;
  color: #c9a24d;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.mypage-btn:hover {
  background: #c9a24d;
  color: #1e1f26;
}

/* 콘텐츠 영역 */
.hero-content {
  display: flex;
  gap: 22px;
}

/* PhotoBoard 감싸는 영역 */
.photo-wrapper {
  flex: 1;
  min-width: 0;
}

/* PhotoBoard hover */
.photo-wrapper .photo-board {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.photo-wrapper .photo-board:hover {
  transform: translateY(-4px);
  box-shadow: 0 14px 34px rgba(0, 0, 0, 0.45);
}

/* 반응형 */
@media (max-width: 900px) {
  .hero-content {
    flex-direction: column;
  }
}


</style>