<template>
  <div class="hashbox">
    <!-- 보여줄 태그들 -->
    <div
      v-for="(tag, idx) in tags"
      :key="idx"
      class="hash"
    >
      #{{ tag.tag }}
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  tags: {
    type: Array,
    required: true
  }
})
</script>

<style scoped>
.hashbox {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.hash {
  background-color: #e0e0e0;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 13px;
  font: bold;
  white-space: nowrap;
}

.hash.more {
  background-color: #d0d0d0;
  font-weight: bold;
}
</style>
