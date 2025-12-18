<template>
    <div>
        <h2>뮤지컬</h2>
        <!-- 검색창 -->
        <div style="margin-bottom: 50px;">
            <SearchBox />
        </div>
        <!-- 검색 결과 -->
        <div class="result"  style="margin-bottom: 50px;">
            <div class="basic-text">검색 결과(2)</div>
            <div>
                <MusicalList />
            </div>
        </div>
        <!--기타-->
        <div>
            <div class="title-text">공연 중인 작품</div>
            <div class="wrapper">
                <CardSlide />
            </div>
        </div>
        <div>
            <div class="title-text">공연 예정 작품</div>
            <div class="wrapper">
                <CardSlide />
            </div>
        </div>
    </div>
</template>

<script setup>
import CardSlide from '@/components/common/CardSlide.vue';
import SearchBox from '@/components/common/SearchBox.vue';
import MusicalList from '@/components/musical/MusicalList.vue';


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