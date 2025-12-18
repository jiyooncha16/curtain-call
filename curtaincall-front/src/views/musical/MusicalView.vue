<template>
    <div>
        <h2>뮤지컬</h2>
        <!-- 검색창 -->
        <div style="margin-bottom: 50px;">
            <SearchBox @search="onSearchResult"/>
        </div>
        <!-- 검색 결과 -->
        <div class="result"  style="margin-bottom: 50px;">
            <div class="basic-text">검색 결과({{ searchResult.length }})</div>
            <div>
                <MusicalList :musicalList="searchResult"/>
            </div>
        </div>
        <!--기타-->
        <div>
            <div class="title-text">공연 중인 작품</div>
            <div class="wrapper">
                <CardSlide :musicalList="onStageMusical"/>
            </div>
        </div>
        <div>
            <div class="title-text">공연 예정 작품</div>
            <div class="wrapper">
                <CardSlide :musicalList="willBeStageMusical"/>
            </div>
        </div>
    </div>
</template>

<script setup>
import CardSlide from '@/components/common/CardSlide.vue';
import SearchBox from '@/components/common/SearchBox.vue';
import MusicalList from '@/components/musical/MusicalList.vue';
import { onMounted, ref } from 'vue';
import axios from 'axios';

const onStageMusical = ref([])
const willBeStageMusical = ref([])
const musicalList = ref([])

onMounted(async ()=> {
  try {
    const onStageRes = await axios.get('/musicals/search', {
      params: {
        date: "2025-12-18", 
        order:"desc", 
        page: 0, 
        size: 10 }
    })
    console.log('공연 중 작품', onStageRes.data)
    onStageMusical.value = onStageRes.data

    const willBeOnStageRes = await axios.get('/musicals/search', {
        params: { 
            fromDate: "2025-12-18",
            order:"asc", 
            page: 0, 
            size: 10 }
    })
    console.log('맞춤 작품', willBeOnStageRes.data)
    willBeStageMusical.value = willBeOnStageRes.data

  } catch (e) {
    console.error('API 에러', e)
  }
})

//자식에게서 받아오기
const searchResult = ref([])
const onSearchResult = (result) => {
  searchResult.value = result
}
</script>

<style scoped>
 .container {
    width:100%;
}
.title-text {
    margin-left : 60px;
}
.result {
    border: 2px lightgray solid;
    padding: 10px;
    margin: 10px 0;
    background: #fff;
    border-radius: 12px;
    padding: 24px;
}


</style>