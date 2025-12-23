<template>
  <div class="musical-grid">
    <MusicalListItem
      v-for="(musical, idx) in musicalList"
      :key="musical.musicalId ?? idx"
      :musical="musical"
      class="grid-item"
    />
  </div>
</template>

<script setup>
import MusicalListItem from './MusicalListItem.vue'

defineProps({
  musicalList: Array
})
</script>

<style scoped>
/* ===== 그리드 전체 ===== */
.musical-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

/* 반응형 */
@media (max-width: 1024px) {
  .musical-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .musical-grid {
    grid-template-columns: 1fr;
  }
}
</style>
