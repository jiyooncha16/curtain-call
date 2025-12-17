<template>
    <div>
        <h2>배우</h2>
        <div>
            <div class="main-text">배우 인기 랭킹 TOP 7(좋아요 순)</div>
            <div class="wrapper">
                <a href=""v-for="i in 7">
                    <CardItem />
                </a>
            </div>
        </div>
        
        <div style="margin: 30px 0;">
            <hr>
        </div>
        <!-- 검색창 -->
        <div>
            <SearchBoxActor />
        </div>
        <!-- 검색 결과 -->
        <div class="result">
            <div class="basic-text">검색 결과(10)</div>
            <hr>
            <div style="margin : 10px 0;">
                <ActorList />
            </div>
        </div>
    </div>
</template>

<script setup>
import ActorList from '@/components/actor/ActorList.vue';
import CardItem from '@/components/common/CardItem.vue';
import SearchBoxActor from '@/components/common/SearchBoxActor.vue';


</script>

<style scoped>
 .container {
    width:100%;
}
.title-text {
    margin-left : 60px;
}
.wrapper {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    gap : 10px;
}
.result {
    border: 2px lightgray solid;
    padding: 10px;
    margin: 10px 0;
}


</style>