<template>
  <div class="page">

    <!-- ===== Hero ===== -->
    <section class="hero hero--actor">
  <div class="hero-inner">
    <div class="hero-content">
      <p class="hero-kicker gold">ACTOR</p>

      <h1 class="hero-title">
        무대를 <span class="gold">빛내는</span> 배우들
      </h1>

      <p class="hero-sub">
        인기 배우부터 전체 배우까지 한눈에 살펴보세요.
      </p>

      <div class="hero-stats">
        <div class="stat">
          <span class="num">{{ animatedTotal }}</span>
          <span class="label">전체 배우</span>
        </div>
        <div class="stat">
          <span class="num">
            {{ animatedTotal >= 61 ? animatedTotal - 61 : 0 }}
          </span>
          <span class="label">공연 중인 배우</span>
        </div>
      </div>
    </div>
  </div>
</section>


    <!-- ===== TOP 5 ===== -->
    <section class="section">
      <div class="section-head">
        <h2 class="section-title">배우 인기 랭킹 TOP 5</h2>
      </div>

      <div class="card slider-card">
        <div class="top-wrapper">
          <CardItemActor
            v-for="actor in topFiveList"
            :key="actor.actorId"
            :actor="actor"
          />
        </div>
      </div>
    </section>

    <!-- ===== 검색 ===== -->
    <section class="section">
      <div class="section-head">
        <h2 class="section-title">검색</h2>
        <div class="count">
          {{ isSearchMode ? searchCount : totalCount }}명
        </div>
      </div>
      
      
      <div class="card search-card">
          <SearchBoxActor @search="onSearchResult" />
        </div>
        
        <div class="section-head row">
        </div>
      <!-- ===== 리스트 ===== -->
      <div class="card list-card" ref="listTop">
        <div v-if="isSearchMode && displayList.length === 0" class="empty">
          검색 결과가 없습니다.
        </div>

        <ActorList
          v-else
          :searchResult="displayList"
        />

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
import CardItemActor from '@/components/common/CardItemActor.vue'
import ActorList from '@/components/actor/ActorList.vue'
import SearchBoxActor from '@/components/common/SearchBoxActor.vue'
import axios from 'axios'
import { onMounted, ref, computed, watch, nextTick } from 'vue'

/* ===== 데이터 ===== */
const topFiveList = ref([])
const fullList = ref([])
const searchResult = ref([])
const onStageList = ref([])

/* ===== 검색 상태 ===== */
const isSearchMode = ref(false)

/* ===== 페이지네이션 ===== */
const page = ref(1)
const pageSize = 15
const pageWindowSize = 5
const listTop = ref(null)

/* ===== 개수 ===== */
const totalCount = computed(() => fullList.value.length)
const searchCount = computed(() => searchResult.value.length)
const onStageCount = computed(() => onStageList.value.length)

/* ===== 총 페이지 수 ===== */
const totalPages = computed(() => {
  const count = isSearchMode.value ? searchCount.value : totalCount.value
  return Math.ceil(count / pageSize)
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

const endPage = computed(() =>
  Math.min(totalPages.value, startPage.value + pageWindowSize - 1)
)

/* ===== 현재 페이지 리스트 ===== */
const displayList = computed(() => {
  const list = isSearchMode.value ? searchResult.value : fullList.value
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

    const onStageRes = await axios.get('/api/actors/search/onStage')
    onStageList.value = onStageRes.data

  } catch (e) {
    console.error('Actor API 에러', e)
  }
})

/* ===== 검색 ===== */
const onSearchResult = async (result) => {
  isSearchMode.value = true
  searchResult.value = result
  page.value = 1

  await nextTick()
  listTop.value?.scrollIntoView({ behavior: 'smooth' })
}

/* ===== 페이지 이동 ===== */
const goPage = (p) => (page.value = p)
const prevPage = () => page.value > 1 && page.value--
const nextPage = () => page.value < totalPages.value && page.value++
const goFirst = () => (page.value = 1)
const goLast = () => (page.value = totalPages.value)

watch(page, async () => {
  await nextTick()
  listTop.value?.scrollIntoView({ behavior: 'smooth' })
})

/* ====== 애니메이션 효과 줍시다 ===== */

const animatedTotal = ref(0)

/* ===== 카운트업 함수 ===== */
const animateCount = (target) => {
  animatedTotal.value = 0

  const duration = 800            // 전체 애니메이션 시간(ms)
  const frameRate = 60          // 약 60fps
  const totalFrames = duration / frameRate
  const increment = target / totalFrames

  let current = 0

  const timer = setInterval(() => {
    current += increment

    if (current >= target) {
      animatedTotal.value = target
      clearInterval(timer)
    } else {
      animatedTotal.value = Math.floor(current)
    }
  }, frameRate)
}
watch(totalCount, (newVal) => {
  if (newVal > 0) {
    animateCount(newVal)
  }
}, { immediate: true })

watch(onStageCount, (newVal) => {
  if (newVal > 0) {
    animateCount(newVal)
  }
}, { immediate: true })

</script>


<style scoped>
/* ===== Base ===== */
.page {
  color: #222;
}
/* ===== Hero : ACTOR ===== */
.hero {
  width: 100vw;
  margin-left: calc(-50vw + 50%);
  height: 480px;

  position: relative;

  background-image:
    /* 좌우 암전 */
    linear-gradient(
      to right,
      rgba(0,0,0,0.85),
      rgba(0,0,0,0.35),
      rgba(0,0,0,0.85)
    ),
    /* 상하 무대 눌림 */
    linear-gradient(
      to bottom,
      rgba(0,0,0,0.35),
      rgba(0,0,0,0.75)
    ),
    /* ACTOR 전용 배경 (임시 동일 이미지) */
    url('/배우3.jpg');

  background-size: cover;
  /* background-position: start; */

  background-repeat: no-repeat;

  display: flex;
  align-items: center;
}

/* ===== Gold Accent (Hero Common) ===== */
.hero .gold {
  background: linear-gradient(
    90deg,
    #c9a24d,
    #ffd77a,
    #c9a24d
  );

  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;

  text-shadow:
    0 2px 6px rgba(0,0,0,0.6),
    0 0 12px rgba(255,215,122,0.25);
}

/* ===== Title Accent (Actor) ===== */
.hero--actor .hero-title .accent {
  color: #ffffff;
  background: linear-gradient(
    90deg,
    #ffffff,
    #dcdcdc
  );

  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;

  text-shadow: 0 6px 18px rgba(0,0,0,0.6);
}

/* 안개 레이어 (뮤지컬보다 약하게) */
.hero::after {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(
    180deg,
    rgba(255,255,255,0.04),
    rgba(255,255,255,0)
  );
  pointer-events: none;
}

/* ===== Hero Inner ===== */
.hero-inner {
  width: 55%;
  margin: 0 auto;
  padding: 0 80px;

  position: relative;
  z-index: 1;
  color: #fff;
}

/* ===== Text ===== */
.hero-kicker {
  font-size: 12px;
  letter-spacing: 0.28em;
  font-weight: 600;
  color: rgba(255,255,255,0.8);
  margin-bottom: 14px;
}

.hero-title {
  font-size: 40px;
  font-weight: 800;
  line-height: 1.25;
  margin-bottom: 18px;

  color: #ffffff;
  text-shadow: 0 6px 18px rgba(0,0,0,0.65);
}

.hero-sub {
  font-size: 16px;
  line-height: 1.6;
  max-width: 420px;

  color: rgba(255,255,255,0.75);
}

/* ===== Stats ===== */
.hero-stats {
  display: flex;
  gap: 24px;
  margin-top: 30px;
}

.stat {
  min-width: 100px;
  padding: 14px 18px;

  border-radius: 14px;
  background: rgba(255,255,255,0.08);
  backdrop-filter: blur(6px);

  box-shadow:
    inset 0 1px 0 rgba(255,255,255,0.15),
    0 10px 24px rgba(0,0,0,0.35);
}

.stat .num {
  font-size: 26px;
  font-weight: 800;
  color: #ffffff;
}

.stat .label {
  font-size: 13px;
  margin-top: 4px;
  color: rgba(255,255,255,0.7);
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
  gap: 10px;
  align-items: center;
}

.section-title {
  font-size: 22px;
  font-weight: 700;
  margin: 0;
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

/* ===== TOP 5 ===== */
.top-wrapper {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 24px;
}

/* ===== Empty ===== */
.empty {
  padding: 60px 0;
  text-align: center;
  color: #888;
  font-size: 15px;
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
</style>
