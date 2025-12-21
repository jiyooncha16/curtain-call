<template>
  <div class="hash-wrapper">

    <div class="hashbox">
      <div v-for="(tag, idx) in tags" :key="idx" class="hash main-text">
        #{{ tag.hashtag }}
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
  }
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
