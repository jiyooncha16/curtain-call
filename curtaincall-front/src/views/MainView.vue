<template>
  <div>
    <!-- ===== Hero : MAIN ===== -->
<section class="hero hero--main" :class="isLogin ? 'login' : 'guest'">
  <div class="hero-inner">
    <div class="hero-content">

      <!-- 로그인 상태 -->
      <template v-if="isLogin && me">
        <p class="hero-kicker gold">WELCOME</p>

        <h1 class="hero-title">
          <span class="gold">{{ me.nickname }}</span>님 안녕하세요!
        </h1>

        <p class="hero-sub">
          지금까지의 관극 기록을 살펴볼까요?
        </p>

        <div class="hero-actions">
          <button class="hero-btn gold-btn" @click="goMyPage">
            마이페이지로 가기 →
          </button>
        </div>
      </template>

      <!-- 비로그인 상태 -->
      <template v-else>
        <p class="hero-kicker gold">CURTAIN CALL</p>

        <h1 class="hero-title">
          내가 <span class="gold">사랑하는 무대</span>는 어떤 모습인가요?
        </h1>

        <p class="hero-sub">
          뮤지컬 정보/기록 플랫폼 <b> 커튼콜 </b> 에 가입하고 
          <br/> 관람 기록과 통계로 나의 뮤지컬 취향을 한눈에 확인해보세요.
        </p>

        <div class="hero-actions">
          <button class="hero-btn gold-btn" @click="goLogin">
            로그인하러 가기
          </button>
          <button class="hero-btn outline-btn" @click="goSignup">
            회원가입하러 가기
          </button>
        </div>
      </template>


    </div>
  </div>
</section>


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
import CardSlide from '@/components/common/CardSlide.vue';
import ReviewCard from '@/components/ReviewCard.vue';
import VideoMain from '@/components/VideoMain.vue';
import axios from 'axios';
import { onMounted, ref } from 'vue';
import { storeToRefs } from 'pinia'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
import PhotoBoardMain from '@/components/common/PhotoBoardMain.vue';

const authStore = useAuthStore()
const { isLogin } = storeToRefs(authStore)

const router = useRouter()

const goLogin = () => {
  router.push('/login')
}
const goSignup = () => {
  router.push('/signup')
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

// 날짜 함수
function todayString() {
  const d = new Date()
  const yyyy = d.getFullYear()
  const mm = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}
const today = todayString()

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
        date: today, 
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
  position: relative;
  overflow: hidden;
}
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
  z-index: 1;  
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
  z-index: 2; 
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
    /* 상단에서 떨어지는 금빛 조명 */
    radial-gradient(
      900px 420px at 30% -20%,
      rgba(255, 215, 120, 0.637),
      rgba(255, 215, 120, 0.26) 35%,
      transparent 100%
    ),

    /* 살짝 반사되는 골드 광 */
    linear-gradient(
      180deg,
      rgba(255, 230, 170, 0.08),
      rgba(255, 230, 170, 0)
    ),

    /* 무대 바탕 */
    linear-gradient(
      135deg,
      #1b1d24,
      #232631
    );

  box-shadow:
    0 12px 28px rgba(0, 0, 0, 0.5),
    inset 0 1px 0 rgba(255, 255, 255, 0.08);
}


/* 상단 헤더 */
.hero-header {
  padding-bottom: 18px;
  color : white
}

/* 인사 */
.hello {
  font-size: 22px;
  font-weight: 600;
}

.hello {
  text-shadow: 0 2px 8px rgba(0,0,0,0.6);
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
  margin: auto;
  display: flex;
  flex-direction: column;
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

/* ===== Hero : MAIN ===== */

/* ===== Hero Actions ===== */
.hero-actions {
  display: flex;
  gap: 14px;
  margin-top: 34px;
}

.hero-btn {
  height: 48px;
  padding: 0 28px;
  border-radius: 999px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
}

.gold-btn {
  background: linear-gradient(135deg, #c9a24d, #ffd77a);
  border: none;
  color: #1e1f26;
}

.outline-btn {
  background: transparent;
  border: 1px solid #c9a24d;
  color: #c9a24d;
}
.hero {
  width: 100vw;
  margin-left: calc(-50vw + 50%);
  margin-bottom: 40px;
  height: 480px;
  padding-top: 64px;
  position: relative;

  background-size: cover;
  background-position: 60% center;
  background-repeat: no-repeat;

  display: flex;
  align-items: center;
}

/* 로그인 */
.hero.login {
  background-image:
    linear-gradient(
      to right,
      rgba(0,0,0,0.85),
      rgba(0,0,0,0.35),
      rgba(0,0,0,0.85)
    ),
    linear-gradient(
      to bottom,
      rgba(0,0,0,0.35),
      rgba(0,0,0,0.75)
    ),
    url('/팬텀.png');
}

/* 비로그인 */
.hero.guest {
  background-image:
    linear-gradient(
      to right,
      rgba(0,0,0,0.85),
      rgba(0,0,0,0.35),
      rgba(0,0,0,0.85)
    ),
    linear-gradient(
      to bottom,
      rgba(0,0,0,0.35),
      rgba(0,0,0,0.75)
    ),
    url('/커튼콜4.jpg');
}


/* =========================
   HERO : MAIN (TEXT FIX)
========================= */

.hero--main {
  color: #ffffff;
  
  padding: 0;
}

/* Kicker */
.hero--main .hero-kicker {
  font-size: 12px;
  letter-spacing: 0.28em;
  font-weight: 600;
  margin-bottom: 14px;

  color: #ffd77a; /* 🔥 골드 */
}

/* Title */
.hero--main .hero-title {
  font-size: 42px;
  font-weight: 800;
  line-height: 1.25;
  margin-bottom: 18px;

  color: #ffffff;
  text-shadow: 0 6px 20px rgba(0,0,0,0.65);
}

/* Gold accent (뮤지컬과 동일) */
.hero-title {
  font-size: 42px;
  font-weight: 800;
  line-height: 1.25;
  margin-bottom: 20px;

  text-shadow: 0 6px 20px rgba(0,0,0,0.6);
}

.hero-title .gold {
  color: #ffd77a;
}
.hero-kicker.gold {
  color: #ffd77a;
}
/* Sub text */
.hero--main .hero-sub {
  font-size: 17px;
  line-height: 1.6;
  max-width: 440px;

  color: rgba(255,255,255,0.75);
}

/* =========================
   HERO STATS (뮤지컬과 동일)
========================= */

.hero--main .hero-stats {
  display: flex;
  gap: 28px;
  margin-top: 34px;
}

.hero--main .stat {
  min-width: 100px;
  padding: 14px 18px;

  border-radius: 14px;
  background: rgba(255,255,255,0.08);
  backdrop-filter: blur(6px);

  box-shadow:
    inset 0 1px 0 rgba(255,255,255,0.15),
    0 10px 24px rgba(0,0,0,0.35);
}

.hero--main .stat .num {
  display: block;
  font-size: 28px;
  font-weight: 800;
  color: #ffd77a; /* 🔥 숫자도 골드 */
}

.hero--main .stat .label {
  font-size: 13px;
  margin-top: 4px;
  color: rgba(255,255,255,0.7);
}
.hero-inner {
  width: 58%;
  max-width: 1200px;   /* 🔥 중앙 기준 */
  margin: 0 auto;

  padding: 10px 80px;

  position: relative;
  z-index: 1;
}

</style>