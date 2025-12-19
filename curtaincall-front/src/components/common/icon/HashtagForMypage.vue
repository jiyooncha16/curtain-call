<template>
  <div class="hash-wrapper">

    <div class="hashbox">
      <!-- 보여줄 태그들 -->
      <div v-for="(tag, idx) in visibleTags" :key="idx" class="hash main-text">
        #{{ tag }}
      </div>

      <!-- 숨겨진 태그 개수 -->
      <div v-if="hiddenCount > 0" class="hash more main-text">
        +{{ hiddenCount }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  tags: {
    type: Array,
    required: true
  },
  limit: {
    type: Number,
    default: 3   // 기본 3개까지만 표시
  }
})

/* 화면에 보여줄 태그 */
const visibleTags = computed(() => {
  return props.tags.slice(0, props.limit)
})

/* 숨겨진 태그 개수 */
const hiddenCount = computed(() => {
  return props.tags.length - visibleTags.value.length
})
</script>

<style scoped>
.hash-wrapper {
  /* background-color: var(--bg-light); */
  width:100%;
  padding: 5px;
  border-radius: 8px;
  margin-top: 8px;

}

.hashbox {
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
}

.hash {
  background-color: #e0e0e0;
  padding: 4px 10px;
  border-radius: 12px;
  /* width:80px; */
  font-size: 16px;
  white-space: nowrap;
}

.hash.more {
  background-color: #d0d0d0;
  /* font-weight: bold; */
}
</style>
