<template>
  <div class="search-wrapper">

    <!-- ===== 상단 검색바 ===== -->
    <div class="search-row">
      <div class="search-input">
        <i class="bi bi-search"></i>
        <input
          v-model="keyword"
          placeholder="작품명을 입력하세요."
          @keyup.enter="onSearch"
        />
      </div>

      <button class="btn ghost" @click="toggleDetail">
        상세
        <i :class="isOpen ? 'bi bi-chevron-up' : 'bi bi-chevron-down'"></i>
      </button>

      <button class="btn primary" @click="onSearch">
        검색
      </button>
    </div>

    <!-- ===== 상세 필터 ===== -->
    <transition name="slide">
      <div v-if="isOpen" class="detail-panel">

        <!-- 기간 -->
        <div class="filter-row">
          <div class="label">기간</div>

          <div class="date-range">
            <div class="input-group">
              <input ref="startInput" type="text" placeholder="시작일" readonly />
              <i class="fa-solid fa-calendar"></i>
            </div>

            <span class="range-separator">~</span>

            <div class="input-group">
              <input ref="endInput" type="text" placeholder="종료일" readonly />
              <i class="fa-solid fa-calendar"></i>
            </div>
          </div>
        </div>

        <!-- 정렬 -->
        <div class="filter-row">
          <div class="label">정렬</div>
          <label class="radio">
            <input type="radio" name="sort" value="latest" v-model="sortType" />
            최신순
          </label>
          <label class="radio">
            <input type="radio" name="sort" value="like" v-model="sortType" />
            좋아요순
          </label>
          <label class="radio">
            <input type="radio" name="sort" value="title" v-model="sortType" />
            가나다순
          </label>
        </div>

        <!-- 순서 -->
        <div class="filter-row">
          <div class="label">순서</div>
          <label class="radio">
            <input type="radio" name="order" value="desc" v-model="orderType" />
            기본
          </label>
          <label class="radio">
            <input type="radio" name="order" value="asc" v-model="orderType" />
            오름차순
          </label>
        </div>
      </div>
    </transition>

  </div>
</template>



<script setup>
import { ref, watch, nextTick } from 'vue'
import flatpickr from 'flatpickr'
import 'flatpickr/dist/flatpickr.css'
import { Korean } from 'flatpickr/dist/l10n/ko'
import axios from 'axios'

const isOpen = ref(false)

const startInput = ref(null)
const endInput = ref(null)

const keyword = ref('')
const sortType = ref('latest')
const orderType = ref('desc')
const startDate = ref(null)
const endDate = ref(null)

const searchResult = ref([])


// 상세보기 토글
const toggleDetail = () => {
  isOpen.value = !isOpen.value
}


//검색하기 버튼 눌렀을 때
const onSearch = async () => {
  console.log({
    keyword: keyword.value,
    orderBy: sortType.value,
    order: orderType.value,
    startDate: startDate.value,
    endDate: endDate.value,
  })
  if (keyword.value == null || keyword.value == '' || keyword.value == " ") {
    const res = await axios.get('/api/musicals')
    emit('search', res.data)
  } else {
    axios.get('/api/musicals/search', {
          params: { 
            keyword: keyword.value,
            order: orderType.value,
            orderBy: sortType.value,
            startDate: startDate.value,
            endDate: endDate.value }
      })
      .then((result)=> {
        console.log('맞춤 작품', result.data)
        emit('search', result.data)
        // searchResult.value = result.data
      })
  }
}

//결과 부모로 올려보내기
const emit = defineEmits(['search'])
// emit('search', searchResult.value)

//기간검색 설정
let startPicker = null
let endPicker = null

watch(isOpen, async (open) => {
  if (open) {
    await nextTick()

    if (!startPicker) {
      startPicker = flatpickr(startInput.value, {
        locale: Korean,
        dateFormat: 'Y-m-d',
        allowInput: false,
        onChange: (_, dateStr) => {
          startDate.value = dateStr
        }
      })
    }

    if (!endPicker) {
      endPicker = flatpickr(endInput.value, {
        locale: Korean,
        dateFormat: 'Y-m-d',
        allowInput: false,
        onChange: (_, dateStr) => {
          endDate.value = dateStr
        }
      })
    }
  }
})
</script>

<style scoped>

/* ===== Top Row ===== */
.search-row {
  display: flex;
  gap: 10px;
  align-items: center;
}

/* 검색 입력 */
.search-input {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 0 14px;
  height: 44px;
  border-radius: 999px;
  background: #f5f6f8;
}

.search-input i {
  color: #888;
  font-size: 16px;
}

.search-input input {
  flex: 1;
  border: none;
  outline: none;
  background: transparent;
  font-size: 15px;
}

/* 버튼 공통 */
.btn {
  height: 44px;
  padding: 0 18px;
  border-radius: 999px;
  font-weight: 700;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
}

/* Primary */
.btn.primary {
  background: #7b0000;
  color: #fff;
}

.btn.primary:hover {
  background: #7b0000cc;
}

/* Ghost */
.btn.ghost {
  background: #f0f1f3;
  color: #333;
}

/* ===== Detail Panel ===== */
.detail-panel {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #eee;
}

/* 필터 행 */
.filter-row {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 14px;
}

.label {
  width: 60px;
  font-weight: 700;
  color: #444;
}

/* 날짜 */
.date-range {
  display: flex;
  align-items: center;
  gap: 8px;
}

.input-group {
  position: relative;
}

.input-group input {
  width: 120px;
  height: 36px;
  padding: 0 32px 0 10px;
  border-radius: 8px;
  border: 1px solid #ddd;
}

.input-group i {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #888;
}

/* 라디오 */
.radio {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  color: #333;
}

.radio input {
  accent-color: #7b0000;
}

/* ===== Transition ===== */
.slide-enter-active,
.slide-leave-active {
  transition: all 0.25s ease;
}

.slide-enter-from,
.slide-leave-to {
  opacity: 0;
  transform: translateY(-6px);
}

/* flatpickr */
.flatpickr-calendar {
  z-index: 9999;
}
</style>
