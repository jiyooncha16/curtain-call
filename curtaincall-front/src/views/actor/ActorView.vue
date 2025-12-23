<template>
    <div>
        <h2>배우</h2>

        <!-- TOP 5 -->
        <section class="section-divider">
            <div class="main-text">배우 인기 랭킹 TOP 5</div>
            <div class="wrapper">
                <CardItemActor v-for="actor in topFiveList" :key="actor.actorId" :actor="actor" />
            </div>
        </section>

        <!-- 검색 -->
        <section class="section-divider">
            <SearchBoxActor @search="onSearchResult" />
        </section>

        <!-- 결과 -->
        <section class="section-divider">
            <div class="result">
                <div class="basic-text">
                    {{ isSearching ? '검색 결과' : '전체 배우' }}
                    ({{ totalCount }})
                </div>
                <hr />

                <ActorList :searchResult="displayList" />

                <!-- 🔥 페이지네이션 -->
                <div class="pagination" v-if="!isSearching && totalPages > 1">
                    <!-- 처음 -->
                    <button class="nav" :disabled="page === 1" @click="goFirst">
                        «« 처음
                    </button>

                    <!-- 이전 -->
                    <button class="nav" :disabled="page === 1" @click="prevPage">
                        « 이전
                    </button>

                    <!-- 페이지 번호 (5개 윈도우) -->
                    <button v-for="p in endPage - startPage + 1" :key="p" @click="goPage(startPage + p - 1)"
                        :class="{ active: page === startPage + p - 1 }">
                        {{ startPage + p - 1 }}
                    </button>

                    <!-- 다음 -->
                    <button class="nav" :disabled="page === totalPages" @click="nextPage">
                        다음 »
                    </button>

                    <!-- 끝 -->
                    <button class="nav" :disabled="page === totalPages" @click="goLast">
                        끝 »»
                    </button>
                </div>
            </div>
        </section>
    </div>
</template>

<script setup>
import ActorList from '@/components/actor/ActorList.vue'
import CardItemActor from '@/components/common/CardItemActor.vue'
import SearchBoxActor from '@/components/common/SearchBoxActor.vue'
import axios from 'axios'
import { onMounted, ref, computed } from 'vue'

/* ===== 데이터 ===== */
const topFiveList = ref([])
const fullList = ref([])
const searchResult = ref([])

/* ===== 페이지네이션 ===== */
const page = ref(1)
const pageSize = 10
const pageWindowSize = 5

/* ===== 검색 여부 ===== */
const isSearching = computed(() => searchResult.value.length > 0)

/* ===== 전체 개수 ===== */
const totalCount = computed(() =>
    isSearching.value ? searchResult.value.length : fullList.value.length
)

/* ===== 총 페이지 수 ===== */
const totalPages = computed(() =>
    Math.ceil(totalCount.value / pageSize)
)

/* ===== 페이지 윈도우 계산 ===== */
const startPage = computed(() => {
    const half = Math.floor(pageWindowSize / 2)
    let start = page.value - half
    if (start < 1) start = 1

    const maxStart = totalPages.value - pageWindowSize + 1
    if (start > maxStart) start = Math.max(1, maxStart)

    return start
})

const endPage = computed(() =>
    Math.min(totalPages.value, startPage.value + pageWindowSize - 1)
)

/* ===== 현재 페이지 리스트 ===== */
const displayList = computed(() => {
    const list = isSearching.value ? searchResult.value : fullList.value
    const start = (page.value - 1) * pageSize
    return list.slice(start, start + pageSize)
})

/* ===== API ===== */
onMounted(async () => {
    try {
        const topRes = await axios.get('/api/actors/search/topFive')
        topFiveList.value = topRes.data

        const allRes = await axios.get('/api/actors/search', {
            params: { page: 0, size: 1000 },
        })
        fullList.value = allRes.data
    } catch (e) {
        console.error(e)
    }
})

/* ===== 검색 ===== */
const onSearchResult = (result) => {
    searchResult.value = result
    page.value = 1
    scrollToTop()
}

/* ===== 페이지 이동 + 스크롤 ===== */
const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' })
}

const goPage = (p) => {
    page.value = p
    scrollToTop()
}

const prevPage = () => {
    if (page.value > 1) {
        page.value--
        scrollToTop()
    }
}

const nextPage = () => {
    if (page.value < totalPages.value) {
        page.value++
        scrollToTop()
    }
}

const goFirst = () => {
    page.value = 1
    scrollToTop()
}

const goLast = () => {
    page.value = totalPages.value
    scrollToTop()
}
</script>

<style scoped>
.wrapper {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: 24px;
}

.result {
    border: 2px lightgray solid;
    background: #fff;
    border-radius: 12px;
    padding: 24px;
}

/* ===== 페이지네이션 ===== */
.pagination {
    margin-top: 24px;
    display: flex;
    justify-content: center;
    gap: 6px;
    flex-wrap: wrap;
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
