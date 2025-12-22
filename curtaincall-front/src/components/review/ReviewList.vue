<template>
  <div class="review-wrap">
    <ReviewListItem
      v-for="review in reviews"
      :key="review.id"
      :review="review"
    />
  </div>
</template>

<script setup>
import ReviewListItem from './ReviewListItem.vue'

defineProps({
  reviews: {
    type: Array,
    required: true
  }
})
</script>
