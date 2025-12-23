<template>
  <div class="search-wrapper">

    <!-- ===== 상단 검색바 ===== -->
    <div class="search-row">
      <div class="search-input">
        <i class="bi bi-search"></i>
        <input
          v-model="keyword"
          placeholder="배우명을 입력하세요."
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

        <!-- 정렬 -->
        <div class="filter-row">
          <div class="label">정렬</div>
          <label class="radio">
            <input type="radio" value="latest" v-model="sortType" />
            최신순
          </label>
          <label class="radio">
            <input type="radio" value="likes" v-model="sortType" />
            좋아요순
          </label>
          <label class="radio">
            <input type="radio" value="name" v-model="sortType" />
            가나다순
          </label>
        </div>

        <!-- 순서 -->
        <div class="filter-row">
          <div class="label">순서</div>
          <label class="radio">
            <input type="radio" value="desc" v-model="orderType" />
            기본
          </label>
          <label class="radio">
            <input type="radio" value="asc" v-model="orderType" />
            오름차순
          </label>
        </div>

      </div>
    </transition>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

/* ===== 상태 ===== */
const isOpen = ref(false)
const keyword = ref('')
const sortType = ref('latest')
const orderType = ref('desc')

/* ===== emit ===== */
const emit = defineEmits(['search'])

/* ===== 상세 토글 ===== */
const toggleDetail = () => {
  isOpen.value = !isOpen.value
}

/* ===== 검색 ===== */
const onSearch = async () => {
  try {
    const res = await axios.get('/api/actors/search', {
      params: {
        keyword: keyword.value,
        orderBy: sortType.value,
        order: orderType.value,
        page: 0,
        size: 1000,
      },
    })

    emit('search', res.data)
  } catch (e) {
    console.error('Actor search error', e)
  }
}
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
</style>
