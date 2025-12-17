<template>
    <div class="container">
        <div style="margin: 0 20px;">

            <div class="title-text">내 리뷰 보관함</div>
            <hr>

            <!-- ===== 필터 영역 ===== -->
            <div class="filter-part">
                <!-- 별 클릭 필터 -->
                <div style="display: flex; gap: 4px; cursor: pointer;">
                    <i v-for="i in 5" :key="i" class="bi" :class="i <= starFilter ? 'bi-star-fill' : 'bi-star'"
                        style="color: gold; font-size: 18px" @click="setStarFilter(i)"></i>
                </div>
                <!-- 평점 필터 -->
                <select v-model="filterRate">
                    <option value="all">전체</option>
                    <option value="1">1점</option>
                    <option value="2">2점</option>
                    <option value="3">3점</option>
                    <option value="4">4점</option>
                    <option value="5">5점</option>
                </select>

                <!-- 정렬 -->
                <select v-model="sortType">
                    <option value="latest">최신순</option>
                    <option value="oldest">오래된순</option>
                </select>

            </div>
        </div>

        <!-- ===== 리뷰 리스트 ===== -->
        <ReviewList :reviews="pagedReviews" />

        <!-- ===== 페이지네이션 ===== -->
        <Pagination :current="page" :total="totalPages" @change="changePage" />
    </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import ReviewList from '@/components/review/ReviewList.vue'
import Pagination from '@/components/common/Pagination.vue'

/*  전체 리뷰 데이터 (임시)*/
const reviews = ref([
    {
        id: 1,
        title: '데스노트 4연 후기입니닷 !!!!!',
        content: '데스노트 오연이 기대되네요...',
        rate: 4,
        writer: '지니',
        date: '2025.12.26',
        posterImg: new URL('@/assets/데스노트.jpg', import.meta.url).href
    },
    {
        id: 2,
        title: '웃는 남자 후기',
        content: '넘버 미쳤음',
        rate: 5,
        writer: '지원',
        date: '2025.12.20',
        posterImg: new URL('@/assets/데스노트.jpg', import.meta.url).href
    },
    {
        id: 3,
        title: '레베카 후기',
        content: '댄버스 미쳤다',
        rate: 5,
        writer: '지영',
        date: '2025.12.10',
        posterImg: new URL('@/assets/데스노트.jpg', import.meta.url).href
    },
    {
        id: 4,
        title: '이상하다~~~~',
        content: '오프라인 하자마자 공연할 예정',
        rate: 5,
        writer: '깨꾹이',
        date: '2025.12.21',
        posterImg: new URL('@/assets/데스노트.jpg', import.meta.url).href
    },
    {
        id: 5,
        title: '뮤지컬 아이돌 하갰읍니다',
        content: '오늘부터 오디션 준비합니다',
        rate: 5,
        writer: '14반 엔시티',
        date: '2025.12.17',
        posterImg: new URL('@/assets/데스노트.jpg', import.meta.url).href
    },
    {
        id: 6,
        title: '팝팝',
        content: '파퓰러',
        rate: 5,
        writer: '깨꾹이',
        date: '2025.12.21',
        posterImg: new URL('@/assets/데스노트.jpg', import.meta.url).href
    },
    {
        id: 7,
        title: '뮤지컬 아이돌 하갰읍니다',
        content: '오늘부터 오디션 준비합니다',
        rate: 5,
        writer: '14반 엔시티',
        date: '2025.12.17',
        posterImg: new URL('@/assets/데스노트.jpg', import.meta.url).href
    },
])

/* 페이지네이션 상태 */

const page = ref(1)
const size = 5

/* 필터 상태 */
const filterRate = ref('all')     // select 평점 필터
const sortType = ref('latest')    // 최신순 / 오래된순
const starFilter = ref(0)         // 별 클릭 필터

/*  별 클릭 처리*/
const setStarFilter = (star) => {
    starFilter.value = star
    filterRate.value = 'all'
}

/* 필터 + 정렬 적용 */
const filteredReviews = computed(() => {
    let list = [...reviews.value]

    // 셀렉트 평점 필터
    if (filterRate.value !== 'all') {
        const rateNum = Number(filterRate.value)
        list = list.filter(r => r.rate === rateNum)
    }


    // 별 클릭 필터
    if (starFilter.value > 0) {
        list = list.filter(r => r.rate === starFilter.value)
    }

    // 날짜 정렬
    list.sort((a, b) => {
        return sortType.value === 'latest'
            ? new Date(b.date) - new Date(a.date)
            : new Date(a.date) - new Date(b.date)
    })

    return list
})

/* 페이지 계산*/
const totalPages = computed(() =>
    Math.ceil(filteredReviews.value.length / size)
)

const pagedReviews = computed(() => {
    const start = (page.value - 1) * size
    return filteredReviews.value.slice(start, start + size)
})

/* 필터 변경 시 페이지 초기화 */
watch([filterRate, sortType, starFilter], () => {
    page.value = 1
})

const changePage = (p) => {
    page.value = p
}
</script>

<style scoped>
select {
    padding: 6px 8px;
}

.filter-part {
    margin: 10px 0;
    display: flex;
    align-items: center;
    justify-content: flex-end;

    gap: 12px;
}

</style>
