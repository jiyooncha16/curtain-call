<template>
    <div>
        <h2>뮤지컬</h2>

        <!-- 공연 중인 작품 -->
        <div>
            <div class="title-text">공연 중인 작품</div>
            <div class="wrapper">
                <CardSlide :musicalList="onStageMusical" />
            </div>
        </div>

        <!-- 검색창 -->
        <div style="margin-bottom: 50px;">
            <SearchBox @search="onSearchResult" />
        </div>

        <!-- 검색 결과 / 전체 목록 -->
        <div class="result" style="margin-bottom: 50px;">
            <div class="basic-text">
                {{ isSearching ? '검색 결과' : '전체 목록' }}
                ({{ totalCount }})
            </div>

            <MusicalList :musicalList="displayList" />

            <!-- 🔥 페이지네이션 (검색 중에는 숨김) -->
            <div class="pagination" v-if="!isSearching && totalPages > 1">
                <!-- 이전 -->
                <button class="nav" :disabled="page === 1" @click="prevPage">
                    « 이전
                </button>

                <!-- 페이지 번호 -->
                <button v-for="p in totalPages" :key="p" @click="page = p" :class="{ active: page === p }">
                    {{ p }}
                </button>

                <!-- 다음 -->
                <button class="nav" :disabled="page === totalPages" @click="nextPage">
                    다음 »
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
import CardSlide from '@/components/common/CardSlide.vue'
import SearchBox from '@/components/common/SearchBox.vue'
import MusicalList from '@/components/musical/MusicalList.vue'
import { onMounted, ref, computed } from 'vue'
import axios from 'axios'

/* ===== 데이터 ===== */
const onStageMusical = ref([])
const fullList = ref([])        // 🔥 전체 목록
const searchResult = ref([])    // 🔥 검색 결과

/* ===== 페이지네이션 ===== */
const page = ref(1)
const pageSize = 10

/* ===== 검색 여부 ===== */
const isSearching = computed(() => searchResult.value.length > 0)

/* ===== 전체 개수 ===== */
const totalCount = computed(() => {
    return isSearching.value
        ? searchResult.value.length
        : fullList.value.length
})

/* ===== 총 페이지 수 ===== */
const totalPages = computed(() => {
    return Math.ceil(totalCount.value / pageSize)
})

/* ===== 현재 페이지에 보여줄 리스트 ===== */
const displayList = computed(() => {
    const list = isSearching.value
        ? searchResult.value
        : fullList.value

    const start = (page.value - 1) * pageSize
    const end = start + pageSize

    return list.slice(start, end)
})

/* ===== API ===== */
onMounted(async () => {
    try {
        // 공연 중 작품
        const onStageRes = await axios.get('/api/musicals/search', {
            params: {
                date: '2025-12-18',
                order: 'desc',
                page: 0,
                size: 10,
            },
        })
        onStageMusical.value = onStageRes.data

        // 🔥 전체 목록 (검색 전 기본)
        const allRes = await axios.get('/api/musicals/search', {
            params: {
                order: 'desc',
                page: 0,
                size: 1000, // ⚠️ 프론트 페이지네이션용 (적당히)
            },
        })
        fullList.value = allRes.data
    } catch (e) {
        console.error('API 에러', e)
    }
})

/* ===== 검색 결과 받기 ===== */
const onSearchResult = (result) => {
    searchResult.value = result
    page.value = 1 // 🔥 검색 시 항상 1페이지
}

/* ===== 페이지 이동 ===== */
const prevPage = () => {
    if (page.value > 1) page.value--
}

const nextPage = () => {
    if (page.value < totalPages.value) page.value++
}
</script>

<style scoped>
.title-text {
    margin-left: 60px;
}

.result {
    border: 2px lightgray solid;
    background: #fff;
    border-radius: 12px;
    padding: 24px;
}

/* ===== 페이지네이션 ===== */
.pagination {
    margin-top: 20px;
    display: flex;
    justify-content: center;
    gap: 6px;
}

.pagination button {
    padding: 6px 12px;
    border: 1px solid #ddd;
    background: #fff;
    cursor: pointer;
    border-radius: 6px;
}

.pagination button.active {
    background: #800000;
    color: #fff;
    font-weight: bold;
}

.pagination button.nav {
    font-weight: bold;
}

.pagination button:disabled {
    opacity: 0.4;
    cursor: not-allowed;
}
</style>
