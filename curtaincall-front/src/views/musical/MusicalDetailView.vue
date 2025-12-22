<template>
    <!-- 작품 정보 영역 -->
    <div class="musical-hero">
      <!-- 포스터 -->
      <div class="poster-box">
        <img :src="`/${musical.image}`" />
      </div>

      <!-- 정보 -->
      <div class="info-box">
        <div class="title-row">
          <h1 class="musical-title">{{ musical.title }}</h1>
          <Heart v-if="isLoaded" :like="like" />
        </div>

        <div class="meta">
          <div class="meta-item">{{ musical.theater }}</div>
          <div class="meta-item">
            {{ musical.startDate }} - {{ musical.endDate }}
          </div>
        </div>

        <Hashtag :tags="tags" :limit="3" />
      </div>
    </div>

    <!-- 출연 배우 영역 -->
    <div class="title-text">출연 배우</div>
    <div class="shadow">
        <div v-if="actors" class="actor-list">
            <CardItemRadius v-for="(actor, idx) in actors" :actor="actor" :key="idx"/>
        </div>
    </div>
    <!-- 카카오 맵 api -->
        <div class="title-text">공연장</div>
        <!-- template -->
        <div class="map-container shadow">
            <div class="map-info">
                <div class="main-text">{{ theater }}</div>
                <div class="basic-text">{{ theaterAddress }}</div>
                <div class="basic-text">{{ theaterPhone }}</div>
                <a :href="theaterUrl" target="_blank" rel="noopener noreferrer" class="map-link-btn">
                    카카오맵 바로가기
                </a>

            </div>
            <div class="map-wrapper">
                <div id="kakao-map" style="width:100%; height:300px;"></div>
            </div>
        </div>
    <!-- 관련 영상 -->
    <div class="title-text">관련 영상</div>
    <div class="container shadow">
        <VideoMain v-if="musical.title" :keyword="musical.title"/>
    </div>

    <!-- 리뷰 영역 -->
    <!-- 🔹 섹션 타이틀 (밖) -->
    <div class="title-text review-title">리뷰</div>

    <!-- 🔹 리뷰 카드 영역 (안) -->
    <div class="container shadow">
      <div class="review-summary">
        <div class="left">
          <div class="sub-text">{{ reviews.length }}개의 관람 후기</div>
        </div>

        <div class="right">
          <div class="avg-score">{{ avgRate }}</div>
          <Rate :rate="avgRate" />
          <!-- ✍ 리뷰 작성 버튼 -->
          <button class="write-review-btn" @click="goWriteReview">
            리뷰 작성
          </button>
        </div>
      </div>


        <div>
          <section v-if="reviews.length != 0" class="grid">
            <ReviewCard
              v-for="review in reviews"
              :key="review.reviewId"
              :review="review"
              @toggle-like="toggleLike"
            />
          </section>
            <!-- empty -->
            <div v-else class="empty">
              <div class="empty-icon">🎭</div>
              <div class="empty-title">
                리뷰가 없어요. 작성해볼까요?
              </div>
            </div>
      </div>
         
    </div>
</template>

<script setup>
import CardItemRadius from '@/components/common/CardItemRadius.vue';
import Hashtag from '@/components/common/icon/Hashtag.vue';
import Heart from '@/components/common/icon/Heart.vue';
import Rate from '@/components/common/icon/Rate.vue';
import ReviewList from '@/components/review/ReviewList.vue';
import ReviewCard from '@/components/ReviewCard.vue';
import VideoMain from '@/components/VideoMain.vue';
import axios from 'axios';
import { onMounted, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { KakaoMap, KakaoMapMarker } from 'vue3-kakao-maps';

const route = useRoute()
const id = route.params.id

const musical = ref({})
const theater = ref('')
const reviews = ref([])
const actors = ref([])
const tags = ref([])
const like = ref('')
const isLoaded = ref(false)

// 카카오맵
const theaterAddress = ref('')
const theaterPhone = ref('')
const theaterUrl = ref('')
let map = null
let ps = null

// 뮤지컬 정보 로드
onMounted(async () => {
  const res = await axios.get(`/api/musicals/${id}`)
  musical.value = res.data
  theater.value = res.data.theater
  console.log("musical : " , musical.value)

  const tagRes = await axios.get(`/api/musicals/tag/${id}`)
  tags.value = tagRes.data
  console.log("tags : " , tags.value)

  const likeRes = await axios.get(`/api/musicals/like/${id}`)
  like.value = likeRes.data
  isLoaded.value = true
  console.log("like : " , like.value)

  const reviewRes = await axios.get(`/api/reviews/${id}`)
  reviews.value = reviewRes.data
  console.log("review : " , reviews.value)

  const actorRes = await axios.get(`/api/actors/casting/${id}`)
  actors.value = actorRes.data
  console.log("actor : " , actors.value)
  
})

// ----------------- theater 값이 생기면 카카오맵 실행
watch(theater, (newTheater) => {
  if (!newTheater) return

  console.log('🎯 theater 변경 감지:', newTheater)

  // 지도 최초 생성 (1번만)
  if (!map) {
    map = new kakao.maps.Map(
      document.getElementById('kakao-map'),
      {
        center: new kakao.maps.LatLng(37.566826, 126.9786567),
        level: 3
      }
    )
    ps = new kakao.maps.services.Places()
  }

  ps.keywordSearch(newTheater, (data, status) => {
    if (status !== kakao.maps.services.Status.OK) return

    const place = data[0]

    new kakao.maps.Marker({
      map,
      position: new kakao.maps.LatLng(place.y, place.x)
    })

    map.setCenter(new kakao.maps.LatLng(place.y, place.x))

    theaterAddress.value =
      place.road_address_name || place.address_name
    theaterPhone.value = place.phone
    theaterUrl.value = place.place_url
  })
})

// 평점 계산
import { computed } from 'vue'
import router from '@/router';

const avgRate = computed(() => {
  if (!reviews.value || reviews.value.length === 0) return 0

  const sum = reviews.value.reduce((acc, review) => {
    return acc + (review.rate ?? 0)
  }, 0)

  return (sum / reviews.value.length).toFixed(1)
})


// 좋아요 처리
function toggleLike(review) {
  if (!review) return;

  // UI optimistic update
  review.liked = !review.liked;
  review.likeCount += review.liked ? 1 : -1;

  // ✅ 서버 반영 (예: POST /api/reviews/{id}/like)
  // axios.post(`/api/reviews/${review.id}/like`, { liked: review.liked }).catch(() => { ...롤백... })
}

// 이동
function goWriteReview() {
  router.push(`/review/new/${id}`)
}
</script>

<style scoped>
.img-box {
    width: 30%;
    aspect-ratio: 3/4;
    /*비율*/
    margin-right: 30px;
}

img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.center {
    gap: 10px;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
}

.text {
    white-space: pre-line;
}

.actor-list {
    margin-top: 10px;
    padding : 0;
    display: grid;
    grid-template-columns: repeat(6, 1fr);
    gap: 5px;
    align-items: center;
    justify-content: start;
}

.map-container {
  display: flex;
  gap: 20px;              /* 설명-지도 사이 간격 */
  align-items: stretch;
}

.map-info {
  flex: 3;                /* 3할 */
}

.map-wrapper {
  flex: 7;                /* 7할 */
}

#kakao-map {
  width: 100%;
  height: 300px;
}

.map-link-btn {
  display: inline-block;
  padding: 8px 14px;
  margin-top: 8px;
  border-radius: 8px;
  background-color: #fee500; /* 카카오 옐로우 */
  color: #000;
  font-weight: 600;
  text-decoration: none;
  cursor: pointer;
}

.map-link-btn:hover {
  background-color: #fada00;
}


/* 리뷰 */

/* =============================
   그리드
============================= */
.content {
  max-width: 1100px;
  margin: 0 auto;
}

.grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 18px;
}

@media (max-width: 900px) {
  .grid {
    grid-template-columns: 1fr;
  }
}

/* =============================
   카드
============================= */
.card {
  background: #fff;
  border-radius: 14px;
  overflow: hidden;
  display: grid;
  grid-template-columns: 140px 1fr;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
  transition: transform 0.15s ease, box-shadow 0.15s ease;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.12);
}

/* =============================
   포스터
============================= */
.poster-wrap {
  position: relative;
}

.poster {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.poster-title {
  position: absolute;
  bottom: 8px;
  left: 8px;
  right: 8px;
  color: #fff;
  text-shadow: 0 2px 8px rgba(0,0,0,0.6);
}

.musical-name {
  font-weight: 700;
  font-size: 15px;
}

/* =============================
   본문
============================= */
.body {
  padding: 14px 16px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.top {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 작성자 */
.author {
  display: flex;
  align-items: center;
  gap: 10px;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #4f46e5;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
}

.author-name {
  font-size: 14px;
  font-weight: 600;
}

.author-sub {
  font-size: 12px;
  color: #888;
}

/* =============================
   좋아요
============================= */
.like-btn {
  border: none;
  background: #f1f1f5;
  border-radius: 20px;
  padding: 6px 12px;
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  font-size: 13px;
}

.like-btn.active {
  background: #ffe4e6;
  color: #e11d48;
}

/* =============================
   리뷰 내용
============================= */
.review-content {
  font-size: 14px;
  line-height: 1.6;
  color: #333;
  background: #fafafa;
  padding: 12px;
  border-radius: 10px;
  white-space: pre-wrap;
}

/* =============================
   하단 정보
============================= */
.bottom {
  display: flex;
  gap: 10px;
  margin-top: auto;
}

.pill {
  font-size: 12px;
  background: #f3f4f6;
  padding: 6px 10px;
  border-radius: 999px;
  color: #555;
}

/* =============================
   Empty
============================= */
.empty {
  grid-column: 1 / -1;
  background: #fff;
  padding: 40px;
  border-radius: 14px;
  text-align: center;
  box-shadow: 0 6px 18px rgba(0,0,0,0.06);
}

/* rating */
.rating-filter {
  display: flex;
  gap: 6px;
}

.rating-btn {
  padding: 6px 12px;
  border-radius: 999px;
  border: 1px solid #ddd;
  background: #fff;
  cursor: pointer;
  font-size: 13px;
}

.rating-btn.active {
  background: #4f46e5;
  color: #fff;
  border-color: #4f46e5;
}

.review-summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 18px 22px;
  border-radius: 16px;
  background: linear-gradient(135deg, #fafafa, #f3f4f6);
  margin-bottom: 20px;
}
.review-summary .left {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 20px;
}
.review-summary .left .sub-text {
  font-size: 18px;
  color: #777;
}

.review-summary .right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.avg-score {
  font-size: 32px;
  font-weight: 800;
  color: #111;
}
.review-content {
  background: #f9fafb;   /* 기존보다 더 연하게 */
  border-left: 4px solid #e5e7eb;
}

.card:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 22px rgba(0,0,0,0.1);
}

.title-text {
  margin-top: 50px;
  margin-left: 5px;
  margin-bottom: 5px;

  display: inline-block;
  font-weight: 700;

  background: linear-gradient(
    to top,
    #f692943b 40%,
    transparent 40%
  );
}


/* 리뷰 작성 버튼 */
.write-review-btn {
  margin-left: 12px;
  padding: 8px 14px;
  border-radius: 999px;
  border: 1px solid #ddd;
  background: #fff;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.15s ease;
}

.write-review-btn:hover {
  background: #f3f4f6;
}

/* 강조용 (empty 상태) */
.write-review-btn.primary {
  margin-top: 14px;.write-review-btn {
  margin-left: 12px;
  padding: 8px 14px;
  border-radius: 999px;
  border: 1px solid #ddd;
  background: #fff;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.15s ease;
}

.write-review-btn:hover {
  background: #f3f4f6;
}

/* 강조용 (empty 상태) */
.write-review-btn.primary {
  margin-top: 14px;
  background: #4f46e5;
  color: #fff;
  border: none;
}

.write-review-btn.primary:hover {
  background: #4338ca;
}

  background: #4f46e5;
  color: #fff;
  border: none;
}

.write-review-btn.primary:hover {
  background: #4338ca;
}

.musical-hero {
  display: flex;
  gap: 36px;
  align-items: flex-start;
  padding: 32px;
  border-radius: 20px;

  background: linear-gradient(
    180deg,
    #fafafa 0%,
    #ffffff 100%
  );

  box-shadow:
    0 8px 24px rgba(0,0,0,0.12);
}

/* 포스터 */
.poster-box {
  width: 260px;
  aspect-ratio: 3 / 4;
  border-radius: 16px;
  overflow: hidden;
  flex-shrink: 0;

  box-shadow: 0 10px 30px rgba(0,0,0,0.25);
}

.poster-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 정보 영역 */
.info-box {
  flex: 1;
  padding-top: 8px;
}

/* 제목 + 좋아요 */
.title-row {
  display: flex;
  align-items: center;
  gap: 12px;
  justify-content: space-between;
}

.musical-title {
  font-size: 28px;
  font-weight: 800;
  line-height: 1.3;
  letter-spacing: -0.5px;
  margin: 0;
}

/* 메타 정보 */
.meta {
  margin: 20px 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.meta-item {
  font-size: 15px;
  color: #555;
}
.musical-title::after {
  content: '';
  display: block;
  width: 25px;
  height: 4px;
  margin-top: 10px;
  background: #f69293;
  border-radius: 2px;
}

.poster-box img {
  transition: transform 0.3s ease;
}
.poster-box:hover img {
  transform: scale(1.05);
}

</style>