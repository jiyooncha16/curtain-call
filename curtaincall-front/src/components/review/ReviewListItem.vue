<!-- <template>
    <div class="review-container flex">
        <div class="img-box">
            <img src="@/assets/데스노트.jpg">
        </div>
        <div class="container">
            <div class="flex space-between" style="align-items:center">
                <div style="font-weight: bold;font-size:18px">데스노트 4연 후기입니닷 !!!!!!!!!</div>
                <div style="color:#800000; font-size: 20px;"><i class="bi bi-arrow-right-square-fill"></i> </div>
            </div>
            <Rate :rate="rate"/>
            <div>지니 | 2025.12.26</div>
            <div style="margin-top:10px;">데스노트 오연이 기대되네요 ......홍광호 내놔 ......... 고은성이라도 내놔.........</div>
        </div>
    </div>
</template>

<script setup>
import CardItem from '../common/CardItem.vue';
import Rate from '../common/icon/Rate.vue';

const rate = 4;
</script>

<style scoped>
.review-container {
    border-radius: 3px;
    border : 2px #800000 solid;
}
.img-box {
    width: 100%;
    max-width: 140px;
    aspect-ratio: 2 / 3;   
    overflow: hidden;      /* 삐져나온 부분 자르기 */
    flex-shrink: 0;       /* flex에서 줄어들지 않게 */
}
.img-box img {
    width: 100%;
    height: 100%;
    object-fit: cover;     /* 중앙 기준 크롭 */
    display: block;
}
</style> -->
<template>
    <div class="review-container flex">
        <div class="img-box">
            <img :src="review.posterImg" />
        </div>

        <div class="review-content">
            <div class="flex space-between" style="align-items:center">
                <div class="review-title">
                    {{ review.title }}
                </div>
                <i class="bi bi-arrow-right-square-fill go-icon"></i>
            </div>

            <Rate :rate="review.rate" />

            <div class="review-meta">
                {{ review.writer }} | {{ review.date }}
            </div>

            <div class="review-text">
                {{ review.content }}
            </div>
        </div>
    </div>
</template>

<script setup>
import Rate from '../common/icon/Rate.vue'

defineProps({
    review: {
        type: Object,
        required: true
    }
})
</script>
<style scoped>
.review-container {
    border-radius: 3px;
    border: 2px #800000 solid;
    margin:  20px;
}

.img-box {
    width: 100%;
    max-width: 140px;
    aspect-ratio: 2 / 3;
    overflow: hidden;
    /* 삐져나온 부분 자르기 */
    flex-shrink: 0;
    /* flex에서 줄어들지 않게 */
}

.img-box img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    /* 중앙 기준 크롭 */
    display: block;
}
</style>
