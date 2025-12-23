<template>
    <!-- 🔥 클릭 가능 -->
    <div class="searchResultItem flex" @click="goDetail">
        <div class="img-box">
            <!-- 🔥 실제 이미지 사용 -->
            <img v-if="musical?.image" :src="`/${musical.image}`" :alt="musical.title" />
        </div>

        <div class="container">
            <div class="main-text">{{ musical.title }}</div>
            <div class="basic-text">
                {{ musical.startDate }} - {{ musical.endDate }}
            </div>
            <div class="basic-text">{{ musical.theater }}</div>
        </div>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
    musical: {
        type: Object,
        required: true
    }
})

const router = useRouter()

/* 🔥 상세 페이지 이동 */
const goDetail = () => {
    router.push(`/musical/${props.musical.musicalId}`)
}
</script>

<style scoped>
.searchResultItem {
    border: 2px lightgray solid;
    border-radius: 5px;
    margin: 10px 0;
    overflow: hidden;
    cursor: pointer;
    /* 🔥 클릭 느낌 */
    transition: transform 0.2s, box-shadow 0.2s;
}

.searchResultItem:hover {
    transform: translateY(-3px);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.img-box {
    width: 150px;
    aspect-ratio: 3/4;
}

img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}
</style>
