<template>
  <div class="actor-grid">
    <CardItemActor
      v-for="actor in searchResult"
      :key="actor.actorId"
      :actor="actor"
    />
  </div>
</template>


<script setup>
import CardItemActor from '../common/CardItemActor.vue'

defineProps({
  searchResult: {
    type: Array,
    required: true
  }
})
</script>
<style scoped>
.actor-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr); /* 기본: 5개 */
  gap: 24px;
}

/* ===== 반응형 ===== */
@media (max-width: 1200px) {
  .actor-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}

@media (max-width: 900px) {
  .actor-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 600px) {
  .actor-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
