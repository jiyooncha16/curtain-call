<template>
  <div>
    <!-- id 없으면 목록 -->
    <template v-if="!route.params.id">
      <div class="board-wrapper">
        <div class="title-text">일반게시판</div>
        <BoardList :list="boardList" />
      </div>

      <div class="board-wrapper">
        <div class="title-text">판매게시판</div>
        <BoardList :list="sellList" />
      </div>
    </template>

    <!-- id 있으면 상세 -->
    <template v-else>
      <BoardDetailView />
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

import BoardList from '@/components/community/BoardList.vue'
import BoardDetailView from '@/views/community/BoardDetailView.vue'

const route = useRoute()

const boardList = ref([])
const sellList = ref([])

onMounted(() => {
  // 🔥 목록일 때만 목록 API 호출
  if (!route.params.id) {
    axios
      .get('/api/boards/search', { params: { category: 'free' } })
      .then(res => (boardList.value = res.data))

    axios
      .get('/api/boards/search', { params: { category: 'deal' } })
      .then(res => (sellList.value = res.data))
  }
})
</script>
