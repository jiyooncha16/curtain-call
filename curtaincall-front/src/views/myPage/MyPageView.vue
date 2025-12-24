<template>
  <!-- 🔥 1. 로딩 중 -->
  <div v-if="loading" class="loading-wrapper">
    <div class="spinner"></div>
    <p>마이페이지 불러오는 중...</p>
  </div>

  <!-- 🔥 2. 로딩 끝 + 데이터 있음 -->
  <div v-else-if="user">
    <BackButton label="뮤지컬 목록으로" />
    <div class="title-text">마이페이지</div>
    <!-- 상단 프로필 영역 -->
    <div class="flex">
      <div class="container flex my-profile" style="justify-content: space-around; width: 100%">
        <!-- 왼쪽 프로필 -->
        <div class="container profile">
          <div class="circle-img-wrapper">
            <div class="circle-img">
              <img :src="userProfile.profileImage" alt="프로필" />
              <div class="nickname-text">
                {{ userProfile.rawUser.user.nickname }}
              </div>
            </div>

            <!-- 수정 버튼 -->
            <div class="icon-box" @click="editClicked">
              <i class="bi bi-pencil"></i>
            </div>
          </div>

          <div class="info-row">
            <div class="nickname-text">{{ user.user.nickname }}</div>
          </div>
        </div>
        <!-- 오른쪽 내 정보 -->
        <div class="container my-info">
          <!--해시태그-->
          <div class="container">
            <div class="basic-text">{{ user.user.nickname }} 님은</div>
            <div class="title-text">{{ user.taste }}</div>
            <HashtagForMypage :tags="user.tags" />
            <hr />
          </div>

          <!-- 통계 카드 영역 -->
          <div class="stats-wrapper">
            <div class="stat-card" style="background-color: rgba(211, 211, 211, 1.0);">
              <div class="stat-value">Like</div>
              <div class="stat-label">나의 취향</div>
            </div>
            <div class="stat-card" @click="likeMusicalClicked">
              <div class="stat-value">{{ user.counts.likeMusicalCount }}</div>
              <div class="stat-label">뮤지컬</div>
            </div>
            <div class="stat-card" @click="likeActorClicked">
              <div class="stat-value">{{ user.counts.likeActorCount }}</div>
              <div class="stat-label">배우</div>
            </div>
            <div class="stat-card" @click="goMyReview">
              <div class="stat-value">{{ user.counts.reviewCount }}</div>
              <div class="stat-label">리뷰</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!--자주 만나는 작품, 자주 만나는 배우 -->
    <!-- 로그인 상태 -->
    <div v-if="me" class="hero-user">
      <!-- 개인화 콘텐츠 -->
      <div class="hero-content">

        <!-- 최근 본 뮤지컬 -->
        <div class="photo-wrapper">
          <PhotoBoard :obj="{
            title: '최근 본 뮤지컬',
            imgs: me.recentMusicals.map(m => ({
              id: m.musicalId,
              src: '/' + m.image
            }))
          }" />

        </div>

        <!-- 자주 본 배우 -->
        <div class="photo-wrapper">
          <PhotoBoard :obj="{
            title: '자주 본 배우',
            imgs: me.favoriteActors
              .slice(0, 3)
              .map(a => ({
                id: a.actorId,
                src: '/' + a.image
              }))
          }" />

        </div>
      </div>
      <div class="flex-center" style="margin-bottom :50px; gap:10px" v-if="false">
        <div class="shadow">
          <PhotoBoard :obj="musical" />
        </div>
        <div class="shadow">
          <PhotoBoard :obj="actor" />
        </div>
      </div>
    </div>

    <!-- 평점 통계 -->
    <div @click="goMyReview">
      <!-- 제목 줄 -->
      <div class="rate-header">
        <div class="title-text">평점 통계</div>

        <!-- ⭐ 내 리뷰 보기 -->
        <button class="my-review-btn" @click.stop="goMyReview">
          내 리뷰 보기 →
        </button>
      </div>
      <div class="rate-wrapper">
        <!-- 블러 대상 -->
        <div class="rate-avg-wrapper" :class="{ blurred: rate == 0 }">
          <div class="rate-box center" style="width: 30%">
            <div class="title-text" style="font-size: 35px;">
              {{ rate }}
            </div>
            <Rate :rate="rate" />
            <div class="basic-text">
              내 리뷰 {{ user.counts.reviewCount }}개
            </div>
          </div>

          <div style="width: 70%; padding:10px 0">
            <RateStats :stats="stats" />
          </div>
        </div>

        <!-- 오버레이 -->
        <div v-if="rate === 0" class="rate-overlay" @click="overlayClicked">
          <div class="overlay-text">
            리뷰를 입력해보세요!
          </div>
        </div>
      </div>

    </div>

    <!-- 캘린더 -->
    <div style="margin-top: 50px;">
      <div class="title-text">나의 캘린더</div>
      <div class="calendar-wrapper">
        <Calendar />
      </div>
    </div>
  </div>
</template>

<script setup>
import PhotoBoard from "@/components/common/PhotoBoard.vue";

import HashtagForMypage from "@/components/common/icon/HashtagForMypage.vue";
import Calendar from "@/components/common/calendar.vue";
import { onMounted, ref, toRaw } from "vue";
import { useRoute, useRouter } from "vue-router";
import RateStats from "@/components/common/RateStats.vue";
import Rate from "@/components/common/icon/Rate.vue";
import { useAuthStore } from "@/stores/auth";
import api from "@/api/axios";
import { computed } from "vue";
import { useUserProfileStore } from "@/stores/userProfile";
const loading = ref(true)

// const monthNow = emit.month
// const emit = defineEmits({
//     month : Number
// })

const auth = useAuthStore()
const user = ref(null)


onMounted(async () => {
  try {
    loading.value = true

    // 1️⃣ 유저 기본 정보
    const userRes = await api.get('/api/user/me')
    user.value = userRes.data

    // 2️⃣ 평점 평균
    const rateRes = await api.get('/api/reviews/rating/me')
    console.log(rateRes.data)
    rate.value = rateRes.data

    // 3️⃣ 평점 통계
    const statsRes = await api.get('/api/reviews/rating/stats/me')
    stats.value = statsRes.data

    // 4️⃣ 메인 마이페이지 데이터
    const meRes = await api.get('/api/user/main', {
      headers: {
        Authorization: `Bearer ${auth.token}`
      }
    })
    me.value = meRes.data

  } catch (e) {
    console.error('마이페이지 로딩 실패', e)
  } finally {
    loading.value = false   // 🔥 여기서 한 번만 끈다
  }
})


// const DEFAULT_IMAGES = [
//   '/profile/default.jpg',
//   '/profile/profile1.jpg',
//   '/profile/profile2.jpg',
//   '/profile/profile3.jpg',
//   '/profile/profile4.jpg',
// ]

// // ✅ userId 기반 고정 랜덤
// function getRandomDefault(userId) {
//   if (!userId) return DEFAULT_IMAGES[0]
//   const index = userId % DEFAULT_IMAGES.length
//   return DEFAULT_IMAGES[index]
// }

// const profileImage = computed(() => {
//   const img = user.value?.user?.profileImage
//   if (img) return img

//   const userId = user.value?.user?.userId
//   return getRandomDefault(userId)
// })
const userProfile = useUserProfileStore()

onMounted(async () => {
  if (!userProfile.rawUser) {
    await userProfile.fetchMe()
  }
})


// const route = useRoute();
// const id = route.params.id;
// console.log("id : " + id);

//리뷰 가져오기
const rate = ref('');
const stats = ref('');
const me = ref([])

// // 페이지 이동하기 - 내 리뷰로
const router = useRouter()
function goMyReview() {
  router.push(`/review/my`)
}

function editClicked() {
  router.push(`/myPage/edit`)
}

function likeMusicalClicked() {
  router.push(`/myPage/musicalLike`)
}

function likeActorClicked() {
  router.push(`/myPage/actorLike`)
}

</script>

<style scoped>
.loading-wrapper {
  height: 60vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.spinner {
  width: 48px;
  height: 48px;
  border: 5px solid #eee;
  border-top: 5px solid #800000;
  border-radius: 50%;
  animation: spin 0.9s linear infinite;
  margin-bottom: 14px;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* 프로필 */
/* .my-profile {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
} */

.profile {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.circle-img-wrapper {
  position: relative;
  width: 300px;
  aspect-ratio: 1/1;
}

.circle-img {
  width: 100%;
  height: 100%;
  aspect-ratio: 1/1;
  border-radius: 50%;
  overflow: hidden;
}

.circle-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* ✏ 수정 버튼 */
.icon-box {
  position: absolute;
  right: 4px;
  bottom: 4px;

  width: 20%;
  height: 20%;
  border-radius: 50%;
  background: #fff;

  display: flex;
  align-items: center;
  justify-content: center;

  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
  cursor: pointer;
}

.icon-box i {
  font-size: 20px;
}

.nickname-text {
  font-weight: bold;
  font-size: 25px;
}

/* 내 정보 카드 */
.my-info {
  position: relative;
  width: 50%;
  aspect-ratio: 4/1;
  background-color: rgb(245, 245, 245);
  /* opacity: 50%; */
  border-radius: 18px;
  box-shadow:
    0 4px 10px rgba(0, 0, 0, 0.08),
    0 12px 30px rgba(0, 0, 0, 0.06);

  display: flex;
  justify-content: center;
  flex-direction: column;
}

/* .info-row {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
}
.icon-box {
    width: 36px;
    height: 36px;

    display: flex;
    align-items: center;
    justify-content: center;

    background-color: white;
    border-radius: 50%;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);

    cursor: pointer;
} */
/* 
.icon-box i {
    font-size: 16px;
}  */

.info-text {
  font-size: 20px;
  margin: 5px;
}

/* 통계 카드 영역 */
.stats-wrapper {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  padding: 10px 24px;
}

.stat-card {
  background-color: rgba(211, 211, 211, 0.484);
  border-radius: 6px;
  padding: 10px 0;
  text-align: center;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
  cursor: pointer;

  transition:
    transform 0.25s ease,
    box-shadow 0.25s ease,
    background-color 0.25s ease;
}

.stat-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 14px 30px rgba(0, 0, 0, 0.15);
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
}

.stat-label {
  font-size: 14px;
  margin-top: 4px;
}

/* 평점통계 영역 */
.rate-avg-wrapper {
  display: flex;

}

.rate-wrapper {
  border-radius: 15px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.rate-box {
  border-radius: 15px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  margin: 20px;
}

/*
.rate-wrapper canvas {
  width: 100% !important;
  height: 100% !important;
  display: block;
} */
/* 
.rate-avg-wrapper > div:last-child {
  flex: 1;
  min-width: 0; 
} */

/* 달력 영역 */
/* .calendar-wrapper {
    height: 500px;

} */
.title-text {
  margin: 10px 0;
}

.rate-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.my-review-btn {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  padding: 4px 6px;
  font-weight: 300;
  color: gray
}

.my-review-btn:hover {
  text-decoration: underline;
}

/* 로그인 후 히어로 전체 */
/* .hero-user {
  margin-bottom: 50px;
  padding: 32px;
  border-radius: 22px;
  background: linear-gradient(
    135deg,
    #1e1f26,
    #2a2c36
  );
  color: #f5f5f5;
} */
.hero-user {
  margin-bottom: 50px;
  padding: 32px;
  border-radius: 22px;
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
  color: #c9a24d;
  /* 골드 포인트 */
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

.rate-wrapper {
  position: relative;
}

/* 블러 처리 */
.blurred {
  filter: blur(4px);
  opacity: 0.6;
  pointer-events: none;
  /* 클릭 막기 */
}

/* 오버레이 */
.rate-overlay {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
}

.overlay-text {
  background: rgba(0, 0, 0, 0.65);
  color: #fff;
  padding: 12px 20px;
  border-radius: 20px;
  font-size: 16px;
  font-weight: 600;
}
</style>
