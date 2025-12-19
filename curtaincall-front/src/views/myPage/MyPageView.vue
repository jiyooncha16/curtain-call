<template>
    <div>
        <div class="container title-text"> 마이 페이지</div>
        <!-- 상단 프로필 영역 -->
        <div class="flex">

            <div class="container flex" style="justify-content: space-around; width: 100%;">
                <!-- 왼쪽 프로필 -->
                <div class="container profile">
                    <div class="circle-img">
                        <img :src="hong" alt="홍광호">
                    </div>
                    <div class="nickname-text">닉네임</div>
                </div>
                <!-- 오른쪽 내 정보 -->
                <div class="container my-info">
                    <div>
                        <div class="icon-box">
                            <i class="bi bi-pencil"></i>
                        </div>
                        <div class=" container">
                            <div class="info-text main-text">내 정보</div>
                            <div class="info-text">아이디</div>
                            <div class="info-text">리뷰 수</div>
                        </div>
                    </div>
                    <!-- ✅ 통계 카드 영역 -->
                    <div class="stats-wrapper">
                        <div class="stat-card">
                            <div class="stat-value">27</div>
                            <div class="stat-label">좋아요</div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-value">10</div>
                            <div class="stat-label">뮤지컬</div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-value">50</div>
                            <div class="stat-label">배우</div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-value">60</div>
                            <div class="stat-label">리뷰</div>
                        </div>
                    </div>

                </div>

            </div>
        </div>
        <!-- 해시태그 -->
        <div class="container">
            <div class="title-text">해시태그</div>
            <div class="basic-text">닉네임 님은 {{taste}}</div>
            <hr>
            <HashtagForMypage :tags="['로맨스', '대극장', '판타지', 'OST', '눈물']" :limit="3" />
        </div>
        <div class=" flex-center">
            <PhotoBoard :obj="musical" />
            <PhotoBoard :obj="actor" />
        </div>
        <!-- 평점 통계 -->
        <div class="container">
            <div class="title-text">평점 통계</div>
            <div class="rate-avg-wrapper" >
                <div class="center" style="width : 30%;">
                    <div class="title-text">{{ rate }}</div>
                    <Rate :rate="rate"/>
                    <div class="basic-text">내 리뷰 27개</div>
                </div>
                <hr>
                <!-- 평점 통계 연결해야함 -->
                 <div style="width : 70%;">
                     <RateStats />
                 </div>
            </div>
        </div>
        <!-- 캘린더 -->
        <div class="container">
            <div class="title-text"> {{month}}월 캘린더</div>
            <div class="calendar-wrapper">
            <Calendar @month="getMonth"/>
            </div>
        </div>


    </div>
</template>

<script setup>
import PhotoBoard from '@/components/common/PhotoBoard.vue';
import hong from '@/assets/홍광호.jpg';
import HashtagForMypage from '@/components/common/icon/HashtagForMypage.vue';
import Calendar from '@/components/common/calendar.vue';
import { onMounted, ref } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';
import RateStats from '@/components/common/RateStats.vue';
import Rate from '@/components/common/icon/Rate.vue';

// const monthNow = emit.month 
// const emit = defineEmits({
//     month : Number
// })

const rate = 4.3
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

const route = useRoute()
const id = route.params.id
console.log("id : " + id)
const taste = ref('')
onMounted(()=> {
    axios.get(`/api/user/taste/${id}`)
    .then((res) => {
    taste.value = res.data
    })
})
</script>

<style scoped>
/* 프로필 */
.profile {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;
}

.circle-img {
    width: 60%;
    aspect-ratio: 1/1;
    border-radius: 50%;
    overflow: hidden;
}

.circle-img img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}


.nickname-text {
    font-weight: bold;
}

/* 내 정보 카드 */
.my-info {
    position: relative;
    width: 50%;
    aspect-ratio: 4/1;
    background-color: var(--bg-light);
    /* opacity: 50%; */
}

.icon-box {
    position: absolute;
    top: 8px;
    right: 8px;

    width: 36px;
    height: 36px;

    display: flex;
    align-items: center;
    justify-content: center;

    background-color: white;
    border-radius: 50%;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);

    cursor: pointer;
}

.icon-box i {
    font-size: 16px;
}

.info-text {
    font-size: 20px;
    margin: 5px;
}

/* 통계 카드 영역 */
.stats-wrapper {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 16px;
    padding: 20px 24px;
}

.stat-card {
    background-color: var(--bg-highlight);
    border-radius: 6px;
    padding: 16px 0;
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
    height: 200px;
    display:flex;

}

/* 달력 영역 */
/* .calendar-wrapper {
    height: 500px;

} */
</style>
