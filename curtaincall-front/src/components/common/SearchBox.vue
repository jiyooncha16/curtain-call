<template>
  <div class="search-wrapper">
    <div class="search-row flex">
      <div class="main-text title">검색</div>

      <div class="search-bar">
        <input v-model="keyword" @keyup.enter="onSearch" />
      </div>

      <div class="search-btn secondary" @click="toggleDetail">
        상세 <i :class="isOpen ? 'bi bi-chevron-up' : 'bi bi-chevron-down'"></i>
      </div>

      <div class="search-btn" @click="onSearch">검색</div>
    </div>
    <!--상세검색-->
    <div v-if="isOpen" class="detail-panel" style="border:3px lightgray solid; padding: 10px;">

      <!--기간-->
      <div class="flex basic-text box">
        <div class="title">기간</div>

        <div class="date-range">
          <div class="input-group">
            <input
              ref="startInput"
              type="text"
              class="date"
              placeholder="시작날짜"
              readonly
            />
            <span class="input-group-text">
              <i class="fa-solid fa-calendar"></i>
            </span>
          </div>

          <span class="range-separator">~</span>

          <div class="input-group">
            <input
              ref="endInput"
              type="text"
              class="date"
              placeholder="종료날짜"
              readonly
            />
            <span class="input-group-text">
              <i class="fa-solid fa-calendar"></i>
            </span>
          </div>
        </div>
      </div>
      <div class="flex basic-text box">
        <div class="title">정렬</div>

        <label class="radio">
          <input type="radio" name="sort" value="like" v-model="sortType" />
          좋아요 순
        </label>

        <label class="radio">
          <input type="radio" name="sort" value="title" v-model="sortType" />
          가나다 순
        </label>

        <label class="radio">
          <input type="radio" name="sort" value="latest" v-model="sortType" />
          최신 순(기본)
        </label>
      </div>

      <div class="flex basic-text box">
        <div class="title">순서</div>
        <label class="radio">
          <input type="radio" name="order" value="asc" v-model="orderType" />
          순서대로
        </label>

        <label class="radio">
          <input type="radio" name="order" value="desc" v-model="orderType" />
          반대로(기본)
        </label>
      </div>
    </div>
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
  axios.get('/musicals/search', {
        params: { 
          keyword: keyword.value,
          order: orderType.value,
          orderBy: sortType.value,
          startDate: startDate.value,
          endDate: endDate.value,
          page: 0, 
          size: 10 }
    })
    .then((result)=> {
      console.log('맞춤 작품', result.data)
      emit('search', result.data)
      // searchResult.value = result.data
    })
}

//결과 부모로 올려보내기
const emit = defineEmits(['search'])
emit('search', searchResult.value)

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
.search-bar {
  display: flex;
  align-items: center;
  width: 70%;
  height: 30px;
  margin-left: 10px;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 8px;
}

.search-bar input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 15px;
}

/* 공연/뮤지컬 느낌 */
.search-bar {
  font-family: "IBM Plex Sans KR", sans-serif;
}

.search-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: auto;
  height: 30px;
  margin-left: 10px;
  padding: 0 16px;
  border-radius: 8px;
  background-color: #800000;
  color: white;
}

.search-btn:hover {
  background-color: #800000a8;
}

.search-btn.secondary {
  background: #eee;
  color: #333;
}

.detail-panel {
  background: #fff;
  border-radius: 12px;
  border: none;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  margin-top: 16px;
}

.date {
  width: 130px;
  height: 32px;
  padding: 0 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.flatpickr-calendar {
  z-index: 9999;
}


.title {
  font-weight: bold;
  margin-right: 10px;
}
.radio {
  display: flex;
  align-items: center;
  gap: 4px;
  margin-right: 12px;
  cursor: pointer;
}

.radio input {
  cursor: pointer;
}

.box {
  margin: 10px;
  display:flex;
  align-items: center;
}

.search-wrapper {
    border: 2px lightgray solid;
    padding: 10px;
    margin: 10px 0;
    background: #fff;
    border-radius: 12px;
    padding: 24px;
}
.search-row {
  align-items: center;
}

.date-range {
  display: flex;
  align-items: center;
  gap: 8px;
}

.range-separator {
  margin: 0 4px;
  color: #888;
  font-weight: 500;
}
</style>