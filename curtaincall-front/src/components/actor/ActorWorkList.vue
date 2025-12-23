<template>
  <div>
    <div class="work-header">
      <h3 class="title-text">출연 작품</h3>
      <span v-if="currentCount > 0" class="current-count">
        현재 공연 중 {{ currentCount }}건
      </span>
    </div>

    <div class="work-list box">
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

.title-text {
  margin: 0 10px;
}
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

.box {
  /* display: flex; */
  gap: 36px;
  align-items: flex-start;
  padding: 32px;
  border-radius: 20px;
/* margin-top: 30px; */
margin-bottom: 50px;
  background: linear-gradient(
    180deg,
    #fafafa 0%,
    #ffffff 100%
  );

  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
}
</style>
