<template>
  <div class="page">

    <!-- ===== Hero ===== -->
    <section class="hero">
      <div class="hero-inner">
        <div class="hero-text">
          <p class="hero-kicker">MUSICAL</p>

          <h1 class="hero-title">
            지금, <span class="gold">이 순간</span>의 뮤지컬
          </h1>

          <p class="hero-sub">
            공연 중인 작품과 다양한 뮤지컬을<br />
            한눈에 확인해보세요.
          </p>

          <div class="hero-stats">
            <div class="stat">
              <span class="num">{{ animatedOnStage }}</span>
              <span class="label">공연 중</span>
            </div>
            <div class="stat">
              <span class="num">{{ animatedWillBeOnStage }}</span>
              <span class="label">공연 예정</span>
            </div>
            <div class="stat">
              <span class="num">{{ animatedTotal }}</span>
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
        <!-- ===== 페이지네이션 ===== -->
        <div class="pagination" v-if="totalPages > 1">
          <button class="nav" :disabled="page === 1" @click="goFirst">««</button>
          <button class="nav" :disabled="page === 1" @click="prevPage">«</button>

          <button
            v-for="p in endPage - startPage + 1"
            :key="p"
            @click="goPage(startPage + p - 1)"
            :class="{ active: page === startPage + p - 1 }"
          >
            {{ startPage + p - 1 }}
          </button>

          <button class="nav" :disabled="page === totalPages" @click="nextPage">»</button>
          <button class="nav" :disabled="page === totalPages" @click="goLast">»»</button>
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
const willBeOnStageMusical = ref([])
const fullList = ref([])
const searchResult = ref([])

/* ===== 페이지네이션 ===== */
const page = ref(1)
const pageSize = 9
const pageWindowSize = 5
const listTop = ref(null) // 페이지 이동 후 이동할 리스트의 탑 위치

/* ===== 검색 여부 ===== */
const isSearchMode = ref(false)

/* ===== 전체 개수 ===== */
const totalCount = computed(() => fullList.value.length)
const searchCount = computed(() => searchResult.value.length)

/* ===== 총 페이지 수 ===== */
const totalPages = computed(() => {
  if (!isSearchMode.value) return Math.ceil(totalCount.value / pageSize)
  return Math.ceil(searchCount.value / pageSize)
})

/* ===== 현재 페이지에 보여줄 리스트 ===== */
const displayList = computed(() => {
  // 검색 안 했으면 전체 다
  if (!isSearchMode.value) {
    const start = (page.value - 1) * pageSize
    const end = start + pageSize
    return fullList.value.slice(start, end)
  }

  // 검색했을 때만 페이징
  const start = (page.value - 1) * pageSize
  const end = start + pageSize
  return searchResult.value.slice(start, end)
})



/* ===== API ===== */

// 날짜 함수
function todayString() {
  const d = new Date()
  const yyyy = d.getFullYear()
  const mm = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}

function tomorrowString() {
  const d = new Date()
  d.setDate(d.getDate() + 1)
  const yyyy = d.getFullYear()
  const mm = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}

const dateStr = todayString()
const tomStr = tomorrowString()

/*
const today = new Date()
const dateStr =
  today.getFullYear() + '-' +
  (today.getMonth() + 1) + '-' +
  today.getDate()
const tomorrow = new Date(today)
tomorrow.setDate(today.getDate() + 1)
const tomStr = 
  tomorrow.getFullYear() + '-' +
  (tomorrow.getMonth() + 1) + '-' +
  tomorrow.getDate()
*/

onMounted(async () => {
  try {
    const onStageRes = await axios.get('/api/musicals/search', {
      params: { date: dateStr , order: 'desc', page: 0, size: 10000 },
    })
    onStageMusical.value = onStageRes.data

    const allRes = await axios.get('/api/musicals')
    fullList.value = allRes.data

    const willRes = await axios.get('/api/musicals/search', {
      params: { fromDate: tomStr, order: 'desc', page: 0, size: 10000 },
    })
    willBeOnStageMusical.value = willRes.data
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
const goPage = (p) => (page.value = p)
const prevPage = () => page.value > 1 && page.value--
const nextPage = () => page.value < totalPages.value && page.value++
const goFirst = () => (page.value = 1)
const goLast = () => (page.value = totalPages.value)

watch(page, async () => { // 페이지 탑으로 이동
  await nextTick()

  listTop.value?.scrollIntoView({
    behavior: 'smooth',
    block: 'start'
  })
})


/* ===== 페이지 윈도우 ===== */
const startPage = computed(() => {
  const half = Math.floor(pageWindowSize / 2)
  let start = page.value - half

  if (start < 1) start = 1

  const maxStart = totalPages.value - pageWindowSize + 1
  if (start > maxStart) start = Math.max(1, maxStart)

  return start
})

const endPage = computed(() => {
  return Math.min(
    startPage.value + pageWindowSize - 1,
    totalPages.value
  )
})



/* ====== 카운트 업 애니메이션 효과 줍시다 ===== */


const animatedTotal = ref(0)
const animatedOnStage = ref(0)
const animatedWillBeOnStage = ref(0)

const animateCount = (target, animatedRef) => {
  animatedRef.value = 0

  const duration = 600
  const frameRate = 60
  const totalFrames = duration / frameRate
  let frame = 0

  const timer = setInterval(() => {
    frame++

    const progress = frame / totalFrames
    const eased = target * (1 - Math.pow(1 - progress, 3))

    if (progress >= 1) {
      animatedRef.value = target
      clearInterval(timer)
    } else {
      animatedRef.value = Math.floor(eased)
    }
  }, frameRate)
}

watch(totalCount, (newVal) => {
  if (newVal > 0) {
    animateCount(newVal, animatedTotal)
  }
}, { immediate: true })

watch(
  () => onStageMusical.value.length,
  (newVal) => {
    if (newVal > 0) {
      animateCount(newVal, animatedOnStage)
    }
  },
  { immediate: true }
)
//////////
watch(
  () => willBeOnStageMusical.value.length,
  (newVal) => {
    if (newVal > 0) {
      animateCount(newVal, animatedWillBeOnStage)
    }
  },
  { immediate: true }
)

</script>

<style scoped>
/* ===== Base ===== */
.page {
  /* background: #f9fafb; */
  color: #222;
}
/* =========================
   HERO : MUSICAL
========================= */

.hero {
  width: 100vw;
  margin-left: calc(-50vw + 50%);
  height: 480px;

  background-image:
  linear-gradient(
    to right,
    rgba(0,0,0,.75),
    rgba(0,0,0,.3),
    rgba(0,0,0,.75)
  ),
  linear-gradient(
    to bottom,
    rgba(0,0,0,.25),
    rgba(0,0,0,.6)
  ),
  url('/물랑루즈.jpg');

  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;

  display: flex;
  align-items: center;
}


.hero-inner {
  color: white;
  padding: 0 80px;
}

.hero-inner {
  color: white;
  padding: 0 80px;
}


/* 무대 안개 느낌 */
.hero::after {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(
    180deg,
    rgba(255,255,255,0.06),
    rgba(255,255,255,0)
  );
  pointer-events: none;
}


/* =========================
   HERO INNER
========================= */
.hero-inner {
  /* max-width: 1200px; */
  width: 55%;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

/* =========================
   HERO TEXT
========================= */
.hero-text {
  max-width: 520px;
  color: #ffffff;
}

/* =========================
   KICKER
========================= */
.hero-kicker {
  font-size: 12px;
  letter-spacing: 0.28em;
  font-weight: 600;

  color: rgba(255, 215, 140, 0.9);
  margin-bottom: 16px;
}

/* =========================
   TITLE
========================= */
.hero-title {
  font-size: 42px;
  font-weight: 800;
  line-height: 1.25;
  margin-bottom: 20px;

  text-shadow: 0 6px 20px rgba(0,0,0,0.6);
}

.hero-title .gold {
  color: #ffd77a;
}

/* =========================
   SUB TITLE
========================= */
.hero-sub {
  font-size: 17px;
  line-height: 1.6;

  color: rgba(255,255,255,0.75);
  margin-bottom: 36px;
}

/* =========================
   STATS
========================= */
.hero-stats {
  display: flex;
  gap: 28px;
}

.stat {
  min-width: 90px;
  padding: 14px 18px;

  border-radius: 14px;
  background: rgba(255,255,255,0.06);
  backdrop-filter: blur(6px);

  box-shadow:
    inset 0 1px 0 rgba(255,255,255,0.12),
    0 10px 24px rgba(0,0,0,0.35);
}

.stat .num {
  display: block;
  font-size: 28px;
  font-weight: 800;
  color: #ffd77a;
}

.stat .label {
  font-size: 13px;
  margin-top: 4px;
  color: rgba(255,255,255,0.7);
}

/* =========================
   RESPONSIVE
========================= */
@media (max-width: 900px) {
  .hero {
    padding: 90px 20px 70px;
  }

  .hero-title {
    font-size: 34px;
  }

  .hero-stats {
    flex-wrap: wrap;
    gap: 16px;
  }
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

.nav {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
