<template>
  <div class="page">

    <!-- ===== Hero ===== -->
    <section class="hero">
      <div class="hero-inner">
        <div class="hero-text">
          <p class="hero-kicker">ACTOR</p>
          <h1 class="hero-title">무대를 빛내는 배우들</h1>
          <p class="hero-sub">
            인기 배우부터 전체 배우까지 한눈에 살펴보세요.
          </p>

          <div class="hero-stats">
            <div class="stat">
              <span class="num">{{ animatedTotal  }}</span>
              <span class="label">전체 배우</span>
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

</script>


<style scoped>
/* ===== Base ===== */
.page {
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
