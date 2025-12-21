<template>
  <section v-if="actors && actors.length" class="related-section">
    <h3 class="title-text">관련 배우</h3>

    <div class="related-list">
      <RelatedActorCard
        v-for="actor in actors"
        :key="actor.actorId"
        :actor="actor"
      />
    </div>
  </section>
</template>

<script setup>
import RelatedActorCard from './RelatedActorCard.vue'

defineProps({
  actors: {
    type: Array,
    required: true
  }
})
</script>

<style scoped>
.related-section {
  margin-top: 48px;
}

.related-list {
  display: grid;
  margin-top: 10px;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 16px;
}
</style>
