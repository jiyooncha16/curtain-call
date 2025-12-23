<template>
  <div class="page">

    <!-- ===== Hero ===== -->
    <section class="hero">
      <div class="hero-inner">
        <div class="hero-text">
          <p class="hero-kicker">MUSICAL</p>
          <h1 class="hero-title">지금, 이 순간의 뮤지컬</h1>
          <p class="hero-sub">
            공연 중인 작품과 다양한 뮤지컬을 한눈에 확인해보세요.
          </p>

          <div class="hero-stats">
            <div class="stat">
              <span class="num">{{ onStageMusical.length }}</span>
              <span class="label">공연 중</span>
            </div>
            <div class="stat">
              <span class="num">{{ totalCount }}</span>
              <span class="label">전체 작품</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ===== 공연 중 작품 ===== -->
    <section class="section">
      <div class="section-head">
        <h2 class="section-title">공연 중인 작품</h2>
      </div>

      <div class="card slider-card">
        <CardSlide :musicalList="onStageMusical" />
      </div>
    </section>

    <!-- ===== 검색 ===== -->
    <section class="section">
      <div class="section-head">
        <h2 class="section-title">검색</h2>
        <div class="count">
            {{ isSearchMode ? searchCount : totalCount }}건
        </div>
      </div>

      <div class="card search-card">
          <SearchBox @search="onSearchResult" />
        </div>
        <div class="section-head row">
        </div>
      <div class="card list-card" ref="listTop">
        
        <div v-if="isSearchMode && displayList.length === 0" class="empty">
            검색 결과가 없습니다.
        </div>
        <MusicalList v-else :musicalList="displayList" />

        <!-- 페이지네이션 -->
        <div class="pagination" v-if="totalPages > 1">
          <button :disabled="page === 1" @click="prevPage">이전</button>

          <button
            v-for="p in totalPages"
            :key="p"
            :class="{ active: page === p }"
            @click="page = p"
          >
            {{ p }}
          </button>

          <button :disabled="page === totalPages" @click="nextPage">다음</button>
        </div>
      </div>
    </section>

  </div>
</template>


<script setup>
import CardSlide from '@/components/common/CardSlide.vue'
import SearchBox from '@/components/common/SearchBox.vue'
import MusicalList from '@/components/musical/MusicalList.vue'
import { onMounted, ref, computed, watch, nextTick  } from 'vue'
import axios from 'axios'

/* ===== 데이터 ===== */
const onStageMusical = ref([])
const fullList = ref([])
const searchResult = ref([])

/* ===== 페이지네이션 ===== */
const page = ref(1)
const pageSize = 9
const listTop = ref(null) // 페이지 이동 후 이동할 리스트의 탑 위치

/* ===== 검색 여부 ===== */
const isSearchMode = ref(false)

/* ===== 전체 개수 ===== */
const totalCount = computed(() => fullList.value.length)
const searchCount = computed(() => searchResult.value.length)

/* ===== 총 페이지 수 ===== */
const totalPages = computed(() => {
  if (!isSearchMode.value) return 1
  return Math.ceil(searchCount.value / pageSize)
})

/* ===== 현재 페이지에 보여줄 리스트 ===== */
const displayList = computed(() => {
  // 검색 안 했으면 전체 다
  if (!isSearchMode.value) {
    return fullList.value
  }

  // 검색했을 때만 페이징
  const start = (page.value - 1) * pageSize
  const end = start + pageSize
  return searchResult.value.slice(start, end)
})

/* ===== API ===== */
onMounted(async () => {
  try {
    const onStageRes = await axios.get('/api/musicals/search', {
      params: { date: '2025-12-18', order: 'desc', page: 0, size: 10 },
    })
    onStageMusical.value = onStageRes.data

    const allRes = await axios.get('/api/musicals/search', {
      params: { order: 'desc', page: 0, size: 1000 },
    })
    fullList.value = allRes.data
  } catch (e) {
    console.error('API 에러', e)
  }
})

/* ===== 검색 결과 받기 ===== */
const onSearchResult = (result) => {
    isSearchMode.value = true // 검색중
  searchResult.value = result
  page.value = 1
}

/* ===== 페이지 이동 ===== */
const prevPage = () => {
  if (page.value > 1) page.value--
}
const nextPage = () => {
  if (page.value < totalPages.value) page.value++
}
watch(page, async () => { // 페이지 탑으로 이동
  await nextTick()

  listTop.value?.scrollIntoView({
    behavior: 'smooth',
    block: 'start'
  })
})
</script>

<style scoped>
/* ===== Base ===== */
.page {
  /* background: #f9fafb; */
  color: #222;
}

/* ===== Hero ===== */
.hero {
  background: linear-gradient(
    180deg,
    #9c1e1e44 0%,
    #ffd6d673 60%,
    #ffffff 100%
  );
  padding: 80px 20px 60px;
}


.hero-inner {
  max-width: 1200px;
  margin: 0 auto;
}

.hero-kicker {
  font-size: 12px;
  letter-spacing: 0.2em;
  color: #ffffff;
  margin-bottom: 12px;
}

.hero-title {
  font-size: 36px;
  font-weight: 800;
  margin-bottom: 16px;
}

.hero-sub {
  font-size: 16px;
  color: #666;
  max-width: 420px;
}

.hero-stats {
  display: flex;
  gap: 24px;
  margin-top: 28px;
}

.stat {
  display: flex;
  flex-direction: column;
}

.stat .num {
  font-size: 28px;
  font-weight: 800;
  color: #7b0000;
}

.stat .label {
  font-size: 13px;
  color: #666;
}

/* ===== Section ===== */
.section {
  max-width: 1200px;
  padding: 40px 20px;
  padding-bottom: 10px;
}

.section-head {
  margin-bottom: 20px;
  display: flex;
  gap : 10px;
  align-items: center;
}

.section-head.row {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.section-title {
  font-size: 22px;
  font-weight: 700;
  margin: 0;
}

.section-desc {
  font-size: 14px;
  color: #777;
  margin-top: 6px;
}

.count {
  font-size: 20px;
  font-weight: 700;
  color: #7b0000;
}

/* ===== Card ===== */
.card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.08);
}

.slider-card {
  padding: 20px;
}

.search-card {
  padding: 24px;
}

.list-card {
  padding: 24px;
}

/* ===== Pagination ===== */
.pagination {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-top: 30px;
}

.pagination button {
  min-width: 36px;
  height: 36px;
  border-radius: 8px;
  border: none;
  background: #eee;
  cursor: pointer;
  font-weight: 600;
}

.pagination button.active {
  background: #7b0000;
  color: #fff;
}

.pagination button:disabled {
  opacity: 0.4;
}
</style>
