<template>
  <div>
    <div class="main-text">검색하기</div>
    <div class="flex" style="align-items: center;">
      <div class="basic-text title">검색어</div>
      <div class="search-bar">
        <input v-model="keyword" @keyup.enter="onSearch" />
      </div>
      <div class="search-btn" @click="toggleDetail">
        상세 <i :class="isOpen ? 'bi bi-chevron-up' : 'bi bi-chevron-down'"></i>
      </div>
      <div class="search-btn" @click="onSearch">검색</div>
    </div>
    <!--상세검색-->
    <div v-if="isOpen" class="detail-panel" style="border:3px lightgray solid; padding: 10px; margin:10px 0;">

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

const isOpen = ref(false)

const startInput = ref(null)
const endInput = ref(null)
const keyword = ref('')

const toggleDetail = () => {
  isOpen.value = !isOpen.value
}

const onSearch = () => {
  console.log({
    keyword: keyword.value,
    sort: sortType.value,
    order: orderType.value
  })
}

let startPicker = null
let endPicker = null

watch(isOpen, async (open) => {
  if (open) {
    await nextTick()

    if (!startPicker) {
      startPicker = flatpickr(startInput.value, {
        locale: Korean,
        dateFormat: 'Y-m-d',
        allowInput: false
      })
    }

    if (!endPicker) {
      endPicker = flatpickr(endInput.value, {
        locale: Korean,
        dateFormat: 'Y-m-d',
        allowInput: false
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
  border: 3px #800000 solid;
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
  width: 10%;
  height: 30px;
  margin-left: 10px;
  padding: 5px;
  background-color: #800000;
  color: white;
}

.search-btn:hover {
  background-color: #800000a8;
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
</style>