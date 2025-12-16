<template>
  <div class="search-bar">
    <input
      v-model="keyword"
      @keyup.enter="onSearch"
    />
    <i class="bi bi-search"></i>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const keyword = ref('')

//검색
const onSearch = () => {
  console.log('검색어:', keyword.value)
}
</script>

<style scoped>
.search-bar {
  display: flex;
  align-items: center;
  width: 300px;
  padding: 4px;
  padding-left: 20px;
  border-radius: 20px;
  border: none;
  background-color: #fff;
}

.search-bar i {
  font-size: 18px;
  color: #800000;
  font-weight: bold;
  margin-right: 10px;
}

.search-bar input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 15px;
}

.search-bar button {
  border: none;
  background: none;
  cursor: pointer;
  font-weight: bold;
}

/* 공연/뮤지컬 느낌 */
.search-bar {
  font-family: "IBM Plex Sans KR", sans-serif;
}

/* hover 효과 */
.search-bar:hover {
  border-color: #bbb;
}

</style>