<template>
  <div class="page">
    <!-- ===== Topbar ===== -->
    <div class="topbar">
      <div class="topbar-title">커뮤니티</div>
      <button class="btn primary" @click="goWrite">
        <i class="bi bi-pencil-square"></i>
        글쓰기
      </button>
    </div>

    <!-- ===== 일반 게시판 ===== -->
    <section class="card">
      <div class="section-header">
        <h2 class="section-title">일반 게시판</h2>
      </div>
      <BoardList :list="boardList" />
    </section>

    <!-- ===== 판매 게시판 ===== -->
    <section class="card">
      <div class="section-header">
        <h2 class="section-title">판매 게시판</h2>
      </div>
      <BoardList :list="sellList" />
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import BoardList from '@/components/community/BoardList.vue'

const router = useRouter()

const allBoardList = ref([])
const boardList = ref([])
const sellList = ref([])

onMounted(async () => {
  allBoardList.value = (await axios.get('/api/boards')).data
  boardList.value = (
    await axios.get('/api/boards/search', {
      params: { category: 'free' },
    })
  ).data
  sellList.value = (
    await axios.get('/api/boards/search', {
      params: { category: 'deal' },
    })
  ).data
})

const goWrite = () => {
  router.push('/community/new')
}
</script>

<style scoped>
/* ===== base ===== */
.page {
  max-width: 980px;
  margin: 0 auto;
  padding: 18px 14px 40px;
  /* background: #f6f7fb; */
  min-height: 100vh;
}

/* ===== topbar ===== */
.topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 4px 18px;
}

.topbar-title {
  font-size: 25px;
  font-weight: 900;
}

/* ===== card ===== */
.card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 26px rgba(0, 0, 0, 0.08);
  padding: 20px;
  margin-bottom: 16px;
}

/* ===== section header ===== */
.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}

.section-title {
    margin: 5px 10px;
  font-size: 20px;
  font-weight: 900;
  padding: 0 .15em; /* 살짝 여백 */
  background: linear-gradient(transparent 55%, #ffd6d6 55%);
}

/* ===== buttons ===== */
.btn {
  border: 1px solid #e6e8ee;
  background: #fff;
  border-radius: 12px;
  padding: 8px 10px;
  font-weight: 800;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.btn:hover {
  background: #f3f5fa;
}

.btn.primary {
  border-color: transparent;
  background: #ffd6d6;
  color: #fd4040;
}

.btn.primary:hover {
  background: #ffb3b3;
}
</style>
