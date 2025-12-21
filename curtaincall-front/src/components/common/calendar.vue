<template>
  <div class="calendar-container">

    <!-- ===== 월 이동 ===== -->
    <div class="calendar-header">
      <button class="nav-btn" @click="prevMonth">
        <i class="bi bi-chevron-left"></i>
      </button>
      <div class="month-title">{{ year }}년 {{ month + 1 }}월</div>
      <button class="nav-btn" @click="nextMonth">
        <i class="bi bi-chevron-right"></i>
      </button>
    </div>

    <!-- 요일 -->
    <div class="calendar-grid header">
      <div v-for="d in days" :key="d" class="day-name">
        {{ d }}
      </div>
    </div>

    <!-- 날짜 셀 -->
    <div class="calendar-grid">
      <div
        v-for="cell in calendarCells"
        :key="cell.key"
        class="day-cell"
        :class="{ today: cell.isToday , empty: !cell.day }"
        @click="openCell(cell)"
      >
        <!-- 날짜 숫자 -->
        <div class="day-number">{{ cell.day }}</div>

        <!-- 하루 1개 리뷰 이미지 -->
        <img
          v-if="cell.review"
          :src="cell.review.posterImg"
          class="day-image"
        />
      </div>
    </div>

    <!-- ===== 리뷰 상세 모달 (Teleport) ===== -->
    <!-- ===== 리뷰 상세 모달 (인라인) ===== -->
<div v-if="selectedReview" class="inline-modal-bg" @click.self="closeReview">
  <div class="inline-modal">
    <button class="close-btn" @click="closeReview">×</button>

    <div class="poster-wrap">
      <img :src="selectedReview.posterImg" />
    </div>

    <div class="modal-body">
      <h3 class="title">{{ selectedReview.title }}</h3>

      <div class="meta">
        <span class="rate">⭐ {{ selectedReview.rate }}</span>
        <span class="date">{{ selectedReview.date }}</span>
      </div>

      <p class="content">{{ selectedReview.content }}</p>
    </div>
  </div>
</div>


  </div>
</template>

<script setup>
import api from '@/api/axios'
import { ref, computed, onMounted } from 'vue'

// /* =====================
//    리뷰 데이터 (임시)
// ===================== */
// const reviews = ref([
//   {
//     id: 1,
//     title: '데스노트 4연 후기',
//     content: '홍광호 제발...',
//     rate: 4,
//     date: '2025-12-26',
//     posterImg: new URL('@/assets/데스노트.jpg', import.meta.url).href
//   },
//   {
//     id: 2,
//     title: '레베카 후기',
//     content: '댄버스 미쳤다',
//     rate: 5,
//     date: '2025-12-10',
//     posterImg: new URL('@/assets/데스노트.jpg', import.meta.url).href
//   },
//   {
//     id: 3,
//     title: '이상하다~~~~',
//     content: '오프라인 하자마자 공연할 예정',
//     rate: 5,
//     date: '2025-12-21',
//     posterImg: new URL('@/assets/데스노트.jpg', import.meta.url).href
//   }
// ])

const reviews = ref([])

onMounted(async () => {
  try {
    const res = await api.get('/api/reviews/my')

    reviews.value = res.data.map(r => ({
      id: r.reviewId,
      title: r.title,
      content: r.content,
      rate: r.rate,
      // 🔥 날짜만 잘라서 캘린더 키로 사용
      date: r.createDate.slice(0, 10),
      posterImg: r.image.startsWith('/')
        ? r.image
        : '/' + r.image
    }))
  } catch (e) {
    console.error('내 리뷰 불러오기 실패', e)
    reviews.value = []
  }
})

/* =====================
   날짜 상태
===================== */
const today = new Date()
const year = ref(today.getFullYear())
const month = ref(today.getMonth())
const days = ['일', '월', '화', '수', '목', '금', '토']

/* =====================
   날짜 → 리뷰 매핑 (하루 1개)
===================== */
const reviewMap = computed(() => {
  const map = {}
  reviews.value.forEach(r => {
    map[r.date] = r
  })
  return map
})

/* =====================
   캘린더 셀 생성
===================== */
const calendarCells = computed(() => {
  const cells = []
  const firstDay = new Date(year.value, month.value, 1).getDay()
  const lastDate = new Date(year.value, month.value + 1, 0).getDate()

  for (let i = 0; i < firstDay; i++) {
    cells.push({ key: `empty-${i}` })
  }

  for (let d = 1; d <= lastDate; d++) {
    const dateKey =
      `${year.value}-${String(month.value + 1).padStart(2, '0')}-${String(d).padStart(2, '0')}`

    cells.push({
      key: dateKey,
      day: d,
      review: reviewMap.value[dateKey] || null,
      isToday:
        year.value === today.getFullYear() &&
        month.value === today.getMonth() &&
        d === today.getDate()
    })
  }
  return cells
})

/* =====================
   월 이동
===================== */
const prevMonth = () => {
  if (month.value === 0) {
    month.value = 11
    year.value--
  } else month.value--
}

const nextMonth = () => {
  if (month.value === 11) {
    month.value = 0
    year.value++
  } else month.value++
}

/* =====================
   리뷰 모달
===================== */
const selectedReview = ref(null)

const openReview = (review) => {
  console.log('🔥 클릭됨', review)
  selectedReview.value = review
}

const closeReview = () => {
  selectedReview.value = null
}

const openCell = (cell) => {
  console.log('셀 클릭', cell)
  if (!cell.review) return
  selectedReview.value = cell.review
}
</script>

<style scoped>
.calendar-container {
  width: 100%;
  min-height: 600px;
}

/* 헤더 */
.calendar-header {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 10px;
}

/* 그리드 */
.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 6px;
}

/* 날짜 셀 */
.day-cell {
  position: relative;
  min-height: 120px;
  border-radius: 12px;
  background: #f1f1f1;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.15s ease, box-shadow 0.15s ease;
}
.day-cell:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.285);
}
.day-cell:not(.empty):has(image) {
  outline: 2px solid rgba(128, 0, 0, 0.25);
}
/* 오늘 */
.today {
  border: 2px solid #800000;
}

/* 날짜 숫자 */
.day-number {
  position: absolute;
  top: 8px;
  left: 8px;
  z-index: 2;
  font-size: 12px;
  font-weight: 600;
  color: #333;
  background: rgba(255,255,255,0.85);
  padding: 2px 6px;
  border-radius: 6px;
}

/* 리뷰 이미지 */
.day-image {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* ===== 인라인 모달 배경 ===== */
.inline-modal-bg {
  position: absolute;
  inset: 0;
  /* background: rgba(0, 0, 0, 0.45); */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 20;
}

/* 캘린더 기준 positioning */
.calendar-container {
  position: relative;
}

/* ===== 인라인 모달 ===== */
.inline-modal {
  position: relative;
  width: 340px;
  width: 400px;          /* 🔥 기본 크기 */
  max-width: 92vw;       /* 모바일 대응 */
  max-height: 85vh;      /* 화면 넘치지 않게 */
  background: white;
  border-radius: 14px;
  overflow-y: auto;
  box-shadow: 0 20px 40px rgba(0,0,0,0.35);
  animation: modalFadeUp 0.25s ease-out;
  padding: 20px; /* 닫기 버튼 여유 */
}

.calendar-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 24px;
  margin-bottom: 20px;
}

.month-title {
  font-size: 20px;
  font-weight: 700;
  color: #222;
}

.nav-btn {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: none;
  background: #f3f3f3;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
}

.nav-btn i {
  font-size: 18px;
  color: #555;
}

.nav-btn:hover {
  background: #800000;
}

.nav-btn:hover i {
  color: white;
}
.calendar-grid.header {
  margin-bottom: 8px;
}

.day-name {
  text-align: center;      /* 🔥 가운데 */
  font-size: 13px;
  font-weight: 600;
  color: #666;
  padding: 6px 0;
}
.day-name:first-child {
  color: #c0392b; /* 일요일 */
}

.day-name:last-child {
  color: #2980b9; /* 토요일 */
}
.calendar-container {
  width: 100%;
  padding: 20px 24px 28px;
  border-radius: 16px;
  background: white;
  box-shadow: 0 10px 30px rgba(0,0,0,0.08);
}
</style>

<style>
/* ===== 모달 ===== */
.modal-bg {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.45);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
  /* backdrop-filter: blur(2px); */
  isolation: isolate;
}

.modal {
  /* all: initial; */
  background: white;
  min-height: 200px;
  z-index: 10000;
  /* position: fixed; */
  /* position: static !important; */
  position: relative;
  width: 340px;
  max-width: 90%;
  background: white;
  border-radius: 14px;
  overflow: hidden;

  box-shadow: 0 20px 40px rgba(0,0,0,0.35);
  animation: modalFadeUp 0.25s ease-out;
  box-shadow: 0 10px 30px rgba(0,0,0,0.3);
}

.modal img {
  width: 100%;
  border-radius: 6px;
  margin: 10px 0;
}

.empty {
  pointer-events: none;
  background: transparent;
}


/* ===== 등장 애니메이션 ===== */
@keyframes modalFadeUp {
  from {
    opacity: 0;
    transform: translateY(16px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* ===== 닫기 버튼 ===== */
.close-btn {
  position: absolute;
  top: 10px;
  right: 12px;
  border: none;
  background: rgba(0,0,0,0.55);
  color: white;
  font-size: 18px;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  cursor: pointer;
  z-index: 10;
}

/* ===== 포스터 영역 (🔥 핵심) ===== */
.poster-wrap {
  padding: 24px 24px 12px;
  display: flex;
  justify-content: center;
}

.poster-wrap img {
  max-width: 100%;
  max-height: 300px;   /* 포스터 최대 높이 제한 */
  object-fit: contain; /* 🔥 절대 안 잘림 */
  border-radius: 8px;
  background: #f5f5f5;
}

/* ===== 내용 ===== */
.modal-body {
  padding: 20px 28px 28px;
}

.modal-body .title {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 10px;
}

.modal-body .meta {
  display: flex;
  gap: 12px;
  font-size: 14px;
  color: #777;
  margin-bottom: 16px;
}

.modal-body .content {
  font-size: 15px;
  line-height: 1.7;
  color: #333;
  white-space: pre-line;
}
.close-btn {
  position: absolute;
  top: 14px;
  right: 14px;
  width: 32px;
  height: 32px;
  font-size: 20px;
}
</style>
