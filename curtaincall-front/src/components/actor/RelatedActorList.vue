<template>
  <section v-if="actors && actors.length" class="related-section">
    <h3 class="title-text">관련 배우</h3>

    <div class="related-list box">
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

@media (max-width: 1200px) {
  .related-list {
    grid-template-columns: repeat(4, 1fr);
  }
}

@media (max-width: 900px) {
  .related-list {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 600px) {
  .related-list {
    grid-template-columns: repeat(2, 1fr);
  }
}

.box {
  /* display: flex; */
  gap: 30px;
  align-items: flex-start;
  padding: 32px;
  border-radius: 20px;
margin-top: 30px;
margin-bottom: 50px;
  background: linear-gradient(
    180deg,
    #fafafa 0%,
    #ffffff 100%
  );

  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
}

</style>
