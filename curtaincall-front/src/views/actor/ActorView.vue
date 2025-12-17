<template>
    <div>
        <h2>배우</h2>
        <div class="container flex" style="align-items:flex-start">
            <div class="img-box">
                <img src="@/assets/홍광호.jpg">
            </div>
            <div>
                <div class="center">
                    <div class="title-text" style="margin:0;">홍광호</div>
                    <Heart/>
                </div>
                <div style="margin: 20px 0">
                    <div class="main-text">약력</div>
                    <div class="basic-text text"> {{  profile }} </div>
                </div>
            </div>
        </div>
        <hr>
        <div>
            <div class="main-text">출연 작품</div>
            <CardItem />
        </div>
    </div>
</template>

<script setup>
import CardItem from '@/components/common/CardItem.vue';
import Heart from '@/components/common/icon/Heart.vue';
const profile = 
    `2002 뮤지컬 <명성황후>
    2006 뮤지컬 <미스 사이공>
    2009 뮤지컬 <오페라의 유령>
    2010 뮤지컬 <지킬 앤 하이드>
    2013 뮤지컬 <노트르담 드 파리>
    2015 뮤지컬 <데스노트>
    2019 뮤지컬 <스위니토드>
    2022 뮤지컬 <물랑루즈!>
    2023 뮤지컬 <데스노트>
    `;

</script>

<style scoped>
.img-box {
    width: 30%;
    aspect-ratio: 3/4; /*비율*/
    margin-right: 30px;
}
img {
    width: 100%;
    height: 100%;
    object-fit:cover;
}
.center {
    gap : 10px;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
}
.text {
    white-space: pre-line;
}
</style>