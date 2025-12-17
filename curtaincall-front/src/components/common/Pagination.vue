
<template>
  <div class="pagination">
    <button
      v-for="p in total"
      :key="p"
      :class="{ active: p === current }"
      @click="$emit('change', p)"
    >
      {{ p }}
    </button>
  </div>
</template>

<script setup>
defineProps({
  current: Number,
  total: Number
})
</script>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-top: 20px;
}
button {
  padding: 6px 12px;
}
.active {
  background: #800000;
  color: white;
}
</style>
