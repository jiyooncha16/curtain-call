<template>
  <div>
    <div class="container title-text">마이페이지</div>
    <!-- 상단 프로필 영역 -->
    <div class="flex">
      <div
        class="container flex"
        style="justify-content: space-around; width: 100%"
      >
        <!-- 왼쪽 프로필 -->
        <div class="container profile">
          <div class="circle-img-wrapper">
            <div class="circle-img">
              <img :src="hong" alt="홍광호" />
            </div>

            <!-- 수정 버튼 -->
            <div class="icon-box">
              <i class="bi bi-pencil"></i>
            </div>
          </div>

          <div class="info-row">
            <div class="nickname-text">{{user.nickname}}</div>
          </div>
        </div>
        <!-- 오른쪽 내 정보 -->
        <div class="container my-info">
          <!--해시태그-->
          <div class="container">
            <div class="basic-text">{{user.nickname}} 님은</div>
            <div class="main-text">{{ taste }}</div>
            <hr />
            <HashtagForMypage :tags="hashtag" :limit="3" />
          </div>

          <!-- 통계 카드 영역 -->
          <div class="stats-wrapper">
            <div class="stat-card" style="background-color: rgba(211, 211, 211, 1.0);">
              <div class="stat-value">Like</div>
              <div class="stat-label">나의 취향</div>
            </div>
            <div class="stat-card">
              <div class="stat-value">{{ likeMusicalCount }}</div>
              <div class="stat-label">뮤지컬</div>
            </div>
            <div class="stat-card">
              <div class="stat-value">{{ likeActorCount }}</div>
              <div class="stat-label">배우</div>
            </div>
            <div class="stat-card">
              <div class="stat-value">{{ reviewCount }}</div>
              <div class="stat-label">리뷰</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!--자주 만나는 작품, 자주 만나는 배우 -->
    <div class="flex-center" style="margin-bottom :50px; gap:10px">
        <div class="shadow">
            <PhotoBoard :obj="musical" />
        </div>
        <div class="shadow">
            <PhotoBoard :obj="actor" />
        </div>
    </div>

    <!-- 평점 통계 -->
    <div>
      <div class="title-text">평점 통계</div>
      <div class="rate-wrapper">
        <div class="rate-avg-wrapper">
            <div class=" rate-box center" style="width: 30%">
                <div class="title-text" style="font-size: 35px;">{{ rate }}</div>
                <Rate :rate="rate" />
                <div class="basic-text">내 리뷰 27개</div>
            </div>
            <!-- 평점 통계 연결해야함 -->
            <div style="width: 70%;padding:10px 0">
                <RateStats />
            </div>
        </div>
        </div>
    </div>
    
    <!-- 캘린더 -->
    <div class="container">
      <div class="title-text">캘린더</div>
      <div class="calendar-wrapper">
        <Calendar/>
      </div>
    </div>
  </div>
</template>

<script setup>
import PhotoBoard from "@/components/common/PhotoBoard.vue";
import hong from "@/assets/홍광호.jpg";
import HashtagForMypage from "@/components/common/icon/HashtagForMypage.vue";
import Calendar from "@/components/common/calendar.vue";
import { onMounted, ref, toRaw } from "vue";
import axios from "axios";
import { useRoute } from "vue-router";
import RateStats from "@/components/common/RateStats.vue";
import Rate from "@/components/common/icon/Rate.vue";

// const monthNow = emit.month
// const emit = defineEmits({
//     month : Number
// })


const route = useRoute();
const id = route.params.id;
console.log("id : " + id);
const rate = 4.3;

// 내 정보 가져오기

const user = ref({})
onMounted (() => {
    axios.get(`/api/user/id/${id}`)
    .then((res) => {
        user.value = res.data;
        console.log(res.data)
  });
})

const musical = {
  title: "자주 만나는 작품",
  imgs: [
    { src: new URL("@/assets/데스노트.jpg", import.meta.url).href },
    { src: new URL("@/assets/데스노트.jpg", import.meta.url).href },
    { src: new URL("@/assets/데스노트.jpg", import.meta.url).href },
  ],
};

const actor = {
  title: "자주 만나는 배우",
  imgs: [
    { src: new URL("@/assets/홍광호.jpg", import.meta.url).href },
    { src: new URL("@/assets/홍광호.jpg", import.meta.url).href },
    { src: new URL("@/assets/홍광호.jpg", import.meta.url).href },
  ],
};

//해시태그 받아오기
const hashtag = ref([]);
onMounted(() => {
  axios.get(`/api/user/tag/${id}`).then((res) => {
    hashtag.value = res.data.map(item => item.hashtag);
    console.log("tag > ", toRaw(hashtag.value))
  });
});


//수식어(AI)
const taste = ref("");
onMounted(() => {
  axios.get(`/api/user/taste/${id}`).then((res) => {
    taste.value = res.data;
  });
});

// 좋아요 리뷰 카운트
const likeReviewCount = ref("");
const likeMusicalCount = ref("");
const likeActorCount = ref("");
const reviewCount = ref("");
onMounted(() => {
  axios.get(`/api/user/count/${id}`).then((res) => {
    likeMusicalCount.value = res.data.likeMusicalCount;
    likeActorCount.value = res.data.likeActorCount;
    likeReviewCount.value = res.data.likeReviewCount;
    reviewCount.value = res.data.reviewCount;
  });
});
</script>

<style scoped>
/* 프로필 */
.profile {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
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
}

/* 내 정보 카드 */
.my-info {
  position: relative;
  width: 50%;
  aspect-ratio: 4/1;
  background-color: white;
  /* opacity: 50%; */
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
    margin : 20px;
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
</style>
