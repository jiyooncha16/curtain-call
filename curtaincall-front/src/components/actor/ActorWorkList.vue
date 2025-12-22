<template>
  <div>
    <div class="work-header">
      <h3 class="title-text">출연 작품</h3>
      <span v-if="currentCount > 0" class="current-count">
        현재 공연 중 {{ currentCount }}건
      </span>
    </div>

    <div class="work-list">
      <ActorWorkCard
        v-for="work in works"
        :key="work.id"
        :work="work"
      />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import ActorWorkCard from './ActorWorkCard.vue'

const props = defineProps({
  works: Array
})

const currentCount = computed(() =>
  props.works.filter(w => w.isCurrent).length
)
</script>

<style scoped>
.work-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.current-count {
  color: #b11226;
  font-weight: 600;
}
</style>
