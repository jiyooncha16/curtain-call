<template>
    <div>
        <h2>뮤지컬</h2>
        <div class="flex">
            <div class="container">
                <div class="title-text">공연 중인 작품</div>
                <div class="wrapper">
                    <CardSlide />
                </div>
            </div>
            <div class="container">
                <div class="title-text">공연 예정 작품</div>
                <div class="wrapper">
                    <CardSlide />
                </div>
            </div>
        </div>
        <!-- 검색창 -->
        <div>
            <SearchBox />
        </div>
        <!-- 검색 결과 -->
        <div>
            <div class="basic-text">검색 결과(5)</div>
            <div>
                <MusicalList />
            </div>
        </div>
    </div>
</template>

<script setup>
import CardSlide from '@/components/common/CardSlide.vue';
import SearchBox from '@/components/common/SearchBox.vue';
import MusicalCard from '@/components/musical/MusicalCard.vue';
import MusicalList from '@/components/musical/MusicalList.vue';


</script>

<style scoped>
 .container {
    width:50%;
}
.title-text {
    margin-left : 60px;
}


</style>