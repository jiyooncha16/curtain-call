<template>
    <div>
        <h2>배우</h2>
        <section class="section-divider">
            <div class="main-text">배우 인기 랭킹 TOP 5</div>
            <div class="wrapper">
                <a href=""v-for="actor in topFiveList">
                    <CardItemActor :actor="actor"/>
                </a>
            </div>
        </section>

        <!-- 검색창 -->
        <section class="section-divider">
            <SearchBoxActor />
        </section>

        <!-- 검색 결과 -->
        <section class="section-divider">
            <div class="result">
                <div class="basic-text">검색 결과(10)</div>
                <hr>
                <div style="margin : 10px 0;">
                    <ActorList />


                </div>
            </div>
        </section>
    </div>
</template>

<script setup>
import ActorList from '@/components/actor/ActorList.vue';
import CardItemActor from '@/components/common/CardItemActor.vue';
import SearchBoxActor from '@/components/common/SearchBoxActor.vue';
import axios from 'axios';
import { onMounted, ref } from 'vue';


// /search/topFive
const topFiveList = ref([])
onMounted(() => {
    axios.get(`/api/actors/search/topFive`)
    .then((res) => {
        // console.log("top 5 : " + res.data)
        topFiveList.value = res.data
    })
})

</script>

<style scoped>
section {
  margin-bottom: 50px;
}
 .container {
    width:100%;
}
.title-text {
    margin-left : 60px;
}
.wrapper {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap : 24px;
}
.result {
    border: 2px lightgray solid;
    padding: 10px;
    margin: 10px 0;
    background: #fff;
    border-radius: 12px;
    padding: 24px;
}
/* .section-divider {
  padding-top: 32px;
  border-top: 1px solid #eee;
} */


</style>