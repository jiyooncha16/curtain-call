<template>
    <div class="review-container flex">
        <div class="img-box">
            <img :src="review.posterImg" />
        </div>

        <div class="review-content">
            <div class="flex space-between" style="align-items:center">
                <!-- <div class="review-title">
                    {{ review.title }}
                </div> -->
                <i class="bi bi-arrow-right-square-fill go-icon"></i>
            </div>

            <Rate :rate="review.rate" />

            <div class="review-meta">
                {{ review.writer }} | {{ review.Createdate }}
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
