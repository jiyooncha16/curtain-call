<template>
  <div>
    <div class="hero-section" style="margin-bottom:50px;">
  
  <!-- 로그인 상태 -->
  <div v-if="isLogin && me" class="hero-user">

    <!-- 상단 인사 -->
    <div class="hero-header flex" style="justify-content: space-between;">
      <div class="hello">
        <div>
          <span class="nickname">{{ me.nickname }}</span>님 안녕하세요😄
        </div>
          <div>
          <span class="nickname">지금까지의 관극 기록을 살펴볼까요?</span>
        </div>
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
          :obj="{
            title: '최근 본 뮤지컬',
            imgs: me.recentMusicals.map(m => ({
              src: '/' + m.image,
              id: m.musicalId
            }))
          }"
        />
      </div>

      <!-- 자주 본 배우 -->
      <div class="photo-wrapper">
        <PhotoBoard
          :obj="{
            title: '자주 본 배우',
            imgs: me.favoriteActors
              .slice(0, 3)
              .map(a => ({
                src: '/' + a.image,
                id: a.actorId
              }))
          }"
        />
      </div>
  </div>
</div>

  <!-- 비로그인 상태 -->
  <div v-else class="login-cta shadow">
    <div class="content">
      <h2>🎭 내가 사랑하는 무대는 어떤 모습인가요?</h2>
      <p>
        뮤지컬 정보/기록 플랫폼 <b> 커튼콜 </b> 에 가입하고 <br/>
        관람 기록과 통계로 나의 뮤지컬 취향을 한눈에 확인해보세요.
      </p>

      <button class="login-btn" @click="goLogin">
        로그인하기
      </button>
      <button class="login-btn" @click="goLogin">
        회원가입하기
      </button>
      </div>
  </div>

</div>

    <div class="shadow" style="margin-bottom:30px;">
      <div class="title-text">HOT 작품</div>
      <CardSlide :musicalList="hotMusical"/>
    </div>
    <div class="shadow" style="margin-bottom:30px;">
      <div class="title-text">공연 중 작품</div>
      <CardSlide :musicalList="onStageMusical"/>
    </div>
    <div class="shadow" style="margin-bottom:30px;">
      <div class="title-text">당신을 위한 추천</div>
      <CardSlide :musicalList="myMusical"/>
    </div>
    <div class="shadow" style="margin-bottom:30px;">
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

const hotMusical = ref([])
const onStageMusical = ref([])
const myMusical = ref([])
const reviewList = ref([])
const toggleLike = ref('')
const me = ref({
  nickname: '',
  recentMusicals: [],
  favoriteActors: []
})

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
    console.log('리뷰', reviewRes.data)
    reviewList.value = reviewRes.data

    // my 정보
    const res = await axios.get('/api/user/main', {
      headers: {
        Authorization: `Bearer ${authStore.token}`
      }
    })
    me.value = res.data
    console.log('내 정보:', me.value)

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
  height : 300px;
  position: relative;
  overflow: hidden;
  padding : 10px 30px;
}

.login-cta::after {
  content: '';
  position: absolute;
  inset: 0;
  z-index: 1;              /* ⭐ 텍스트 아래 */
  background:
    radial-gradient(
      circle at top left,
      rgba(196, 157, 74, 0.45),
      transparent 65%
    ),
    linear-gradient(
      90deg,
      rgba(245, 232, 200, 0.45),
      rgba(235, 216, 170, 0.158)
    );
  pointer-events: none;
}

.login-cta .content {
  margin-top: 25px;
  position: relative;
  z-index: 2; /* ⭐ 텍스트는 무조건 위 */
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
  color: #000000;
}
/* 로그인 버튼 - Gold */
.login-btn {
  margin-top: 50px;
  margin-right: 10px;
  padding: 12px 30px;
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
  margin-bottom: 60px;
  padding: 36px 40px;
  border-radius: 26px;

  background:
    linear-gradient(180deg, rgba(255,255,255,0.04), rgba(255,255,255,0)),
    linear-gradient(135deg, #1b1d24, #232631);

  box-shadow: 0 6px 8px rgba(0, 0, 0, 0.55);
}

/* 상단 헤더 */
.hero-header {
  padding-bottom: 18px;
  border-bottom: 1px solid rgba(255,255,255,0.08);
  color : white
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
  height: 50px;
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
.title-text {
  font-size: 20px;
  font-weight: 700;
  letter-spacing: -0.2px;

  margin: 6px 0 16px 10px;
  color: #1e1f26;
  position: relative;
}

.title-text::before {
  content: '';
  position: absolute;
  left: -10px;
  top: 4px;
  width: 4px;
  height: 20px;
  border-radius: 2px;
  background: linear-gradient(#c9a24d, #e0b85c);
}
.carousel-wrapper {
  position: relative;
  max-width: 1500px;
  padding: 20px 60px;

  border-radius: 18px;
  background: linear-gradient(
    180deg,
    rgba(255,255,255,0.03),
    rgba(255,255,255,0)
  );
}
.nav {
  background: rgba(20,20,25,0.9);
  backdrop-filter: blur(6px);

  box-shadow:
    0 6px 16px rgba(0,0,0,0.5),
    inset 0 1px 0 rgba(255,255,255,0.15);

  opacity: 0.6;
}

.nav:not(.disabled):hover {
  opacity: 1;
  transform: translateY(-50%) scale(1.05);
}

.review-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 26px;
  padding: 10px 14px 20px;
}
.shadow {
  background: #fff;
  border-radius: 16px;
  padding: 24px;

  border: 1px solid #eee;
  box-shadow: 0 6px 16px rgba(0,0,0,0.08);
}

</style>