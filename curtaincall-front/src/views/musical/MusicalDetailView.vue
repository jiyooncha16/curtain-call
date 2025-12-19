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
                <div class="main-text">세부정보</div>
                <div class="basic-text text"> {{ musicalInfo }} </div>
            </div>
        </div>

    </div>
    <!-- 출연 배우 영역 -->
    <div class="container">
        <div class="main-text" style="margin: 10px 0">출연 배우</div>
        <hr>
        <div class="actor-list">
            <CardItemRadius v-for="actor in actors" :key="actor.name" :name="actor.name" :img="actor.img" />
        </div>
    </div>
    <!-- 카카오 맵 api -->
    <div class="container">
        <div class="main-text" style="margin: 10px 0">공연장</div>
        <div class="map-wrapper"></div>
    </div>
    <!-- 관련 영상 -->
    <div class="container">
        <div class="main-text" style="margin: 10px 0">관련 영상</div>
        <VideoMain :keyword="title"/>
    </div>
    <!-- 리뷰 영역 -->
    <div class="container">
        <div class="main-text" style="margin: 10px 0">리뷰(50개)</div>
        <Rate :rate="avgRate" style="font-size: 30px;" />
        <!-- 인기리뷰 영역 -->
        <ReviewList />
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
</script>

<style scoped>
.img-box {
    width: 25%;
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

.map-wrapper {
    height: 300px;
}
</style>