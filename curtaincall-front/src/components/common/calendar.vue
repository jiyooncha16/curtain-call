<template>
  <div class="calendar-container">

    <!-- ===== 월 이동 ===== -->
    <div class="calendar-header">
      <button @click="prevMonth">◀</button>
      <div class="month-title">{{ year }}년 {{ month + 1 }}월</div>
      <button @click="nextMonth">▶</button>
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
        :class="{ today: cell.isToday }"
        @click="cell.review && openReview(cell.review)"
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
    <teleport to="body">
      <div v-if="selectedReview" class="modal-bg" @click.self="closeReview">
        <div class="modal">
          <h3>{{ selectedReview.title }}</h3>
          <img :src="selectedReview.posterImg" />
          <p>{{ selectedReview.content }}</p>
          <p class="meta">
            ⭐ {{ selectedReview.rate }} · {{ selectedReview.date }}
          </p>
          <button @click="closeReview">닫기</button>
        </div>
      </div>
    </teleport>

  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

/* =====================
   리뷰 데이터 (임시)
===================== */
const reviews = ref([
  {
    id: 1,
    title: '데스노트 4연 후기',
    content: '홍광호 제발...',
    rate: 4,
    date: '2025-12-26',
    posterImg: new URL('@/assets/데스노트.jpg', import.meta.url).href
  },
  {
    id: 2,
    title: '레베카 후기',
    content: '댄버스 미쳤다',
    rate: 5,
    date: '2025-12-10',
    posterImg: new URL('@/assets/데스노트.jpg', import.meta.url).href
  },
  {
    id: 3,
    title: '이상하다~~~~',
    content: '오프라인 하자마자 공연할 예정',
    rate: 5,
    date: '2025-12-21',
    posterImg: new URL('@/assets/데스노트.jpg', import.meta.url).href
  }
])

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
  border-radius: 6px;
  background: #f0f0f0;
  overflow: hidden;
  cursor: pointer;
}

/* 오늘 */
.today {
  border: 2px solid #800000;
}

/* 날짜 숫자 */
.day-number {
  position: absolute;
  top: 6px;
  left: 6px;
  z-index: 2;
  font-size: 12px;
  font-weight: bold;
  color: white;
  background: rgba(0, 0, 0, 0.6);
  padding: 2px 6px;
  border-radius: 4px;
}

/* 리뷰 이미지 */
.day-image {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* ===== 모달 ===== */
.modal-bg {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.45);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999999;
}

.modal {
  background: white;
  padding: 20px;
  width: 320px;
  border-radius: 8px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.3);
}

.modal img {
  width: 100%;
  border-radius: 6px;
  margin: 10px 0;
}
</style>
