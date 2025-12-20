<template>
    <!-- 작품 정보 영역 -->
    <div class="container flex" style="align-items:flex-start">
        <!-- 포스터 영역 -->
        <div class="img-box">
            <img src="@\assets\데스노트.jpg">
        </div>
        <!-- 작품 설명 영역 -->
        <div>
            <div class="center" style="margin: 20px 0">
                <div class="title-text" style="margin:0;">{{title}}</div>
                <Heart />
            </div>
            <Hashtag :tags="['로맨스', '대극장', '판타지', 'OST', '눈물']" :limit="3" />
            <div style="margin: 20px 0">
                <div class="basic-text text"> {{ musicalInfo }} </div>
            </div>
        </div>

    </div>
    <!-- 출연 배우 영역 -->
    <div class="container">
        <div class="title-text" style="margin: 10px 0">출연 배우</div>
        <hr>
        <div class="actor-list">
            <CardItemRadius v-for="actor in actors" :key="actor.name" :name="actor.name" :img="actor.img" />
        </div>
    </div>
    <!-- 카카오 맵 api -->
    <div class="container">
        <div class="title-text" style="margin: 10px 0">공연장</div>
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
    </div>
    <!-- 관련 영상 -->
    <div class="container shadow">
        <div class="title-text" style="margin: 10px 0">관련 영상</div>
        <VideoMain :keyword="title"/>
    </div>
    <!-- 리뷰 영역 -->
    <div class="container shadow">
        <div class="title-text" style="margin: 10px 0">리뷰(50개)</div>
        <Rate :rate="avgRate" style="font-size: 30px;" />
        <!-- 인기리뷰 영역 -->
        <!-- <ReviewList /> -->
    </div>
</template>

<script setup>
import CardItemRadius from '@/components/common/CardItemRadius.vue';
import Hashtag from '@/components/common/icon/Hashtag.vue';
import Heart from '@/components/common/icon/Heart.vue';
import Rate from '@/components/common/icon/Rate.vue';
import ReviewList from '@/components/review/ReviewList.vue';
import VideoMain from '@/components/VideoMain.vue';
import axios from 'axios';
import { onMounted, ref } from 'vue';
import { KakaoMap, KakaoMapMarker } from 'vue3-kakao-maps';

const title = "데스노트"
const musicalInfo = `
20220401 -20220619
충무아트센터
초월적 힘을 가진 노트로 연쇄 살인을 시작한 라이토와
그를 쫓는 천재 탐정 L의 두뇌 싸움이 전개된다.
스릴러와 심리전이 강력한 긴장을 만든다.
라이토 : 홍광호, 고은성
엘 : 김준수, 김성철
류크 : 강홍석, 서경수
`;

const actors = [
    {
        name: '홍광호',
        img: new URL('@/assets/홍광호.jpg', import.meta.url).href
    },
    {
        name: '김준수',
        img: new URL('@/assets/홍광호.jpg', import.meta.url).href
    },
    {
        name: '김성철',
        img: new URL('@/assets/홍광호.jpg', import.meta.url).href
    },
    {
        name: '김성철',
        img: new URL('@/assets/홍광호.jpg', import.meta.url).href
    },
    {
        name: '김준수',
        img: new URL('@/assets/홍광호.jpg', import.meta.url).href
    },
    {
        name: '김성철',
        img: new URL('@/assets/홍광호.jpg', import.meta.url).href
    },
    {
        name: '김성철',
        img: new URL('@/assets/홍광호.jpg', import.meta.url).href
    },
]
const avgRate = 4;

// 뮤지컬 정보 불러오기
// onMounted(() => {
//     axios.get





//카카오맵 API

const theater = '세종문화회관'
const theaterAddress = ref('')
const theaterPhone = ref('')
const theaterUrl = ref('')

const coordinate = {
  lat: 37.566826,
  lng: 126.9786567
};
onMounted(() => {
  console.log('🔥 onMounted 실행됨')
  console.log('kakao:', window.kakao)

  const map = new kakao.maps.Map(
    document.getElementById('kakao-map'),
    {
      center: new kakao.maps.LatLng(37.566826, 126.9786567),
      level: 3
    }
  )

  const ps = new kakao.maps.services.Places()
  ps.keywordSearch(theater, (data, status) => {
    console.log('places result:', data)

    if (status !== kakao.maps.services.Status.OK) return

    const place = data[0]

    const marker = new kakao.maps.Marker({
      map,
      position: new kakao.maps.LatLng(place.y, place.x)
    })

    map.setCenter(new kakao.maps.LatLng(place.y, place.x))

    theaterAddress.value = place.road_address_name
    theaterPhone.value = place.phone
    theaterUrl.value = place.place_url
  })
})


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
</style>