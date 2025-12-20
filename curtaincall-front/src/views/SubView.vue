<template>
  <div>
    <ReviewList
      :reviews="reviewList"
      @clickReview="goDetail"
      @openDetail="openModal"
    />
  </div>
</template>

<script setup>
import ReviewList from '@/components/ReviewList.vue';

const reviewList = {
  reviewId: 1,
  posterUrl: "@/assets/데스노트.jpg",
  musicalTitle: "데스노트",
  reviewTitle: "홍광호 회차 미쳤다",
  reviewContent: "1막부터 몰입감이…",
  writer: "지니",
  createdAt: "2025-12-20T10:30:00"
}
</script>

<style scoped>

</style>