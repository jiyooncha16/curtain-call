<template>
  <div class="stars">
    <i
      v-for="n in 5"
      :key="n"
      class="bi star"
      :class="n <= rate ? 'bi-star-fill active' : 'bi-star'"
      @click="onClick(n)"
    ></i>
  </div>
</template>

<script setup>
const props = defineProps({
  rate: {
    type: Number,
    default: 0,
  },
})

const emit = defineEmits(['update:rate'])

const onClick = (n) => {
  emit('update:rate', n)
}
</script>

<style scoped>
.stars {
  display: flex;
  gap: 6px;
}

.star {
  font-size: 30px;          /* ⭐ 크기 */
  cursor: pointer;
  color: rgba(255, 213, 0, 0.963);
}
</style>
