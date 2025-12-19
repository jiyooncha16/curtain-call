<template>
  <div>
    <div class="board-wrapper">
      <div class="title-text">전체게시판</div>
      <BoardList :list="allBoardList" />
    </div>

    <div class="board-wrapper">
      <div class="title-text">일반게시판</div>
      <BoardList :list="boardList" />
    </div>

    <div class="board-wrapper">
      <div class="title-text">판매게시판</div>
      <BoardList :list="sellList" />
    </div>

    <button @click="goWrite">글쓰기</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import BoardList from '@/components/community/BoardList.vue'

const router = useRouter()

const allBoardList = ref([])
const boardList = ref([])
const sellList = ref([])

onMounted(async () => {
  allBoardList.value = (await axios.get('/api/boards')).data
  boardList.value = (await axios.get('/api/boards/search', { params: { category: 'free' } })).data
  sellList.value = (await axios.get('/api/boards/search', { params: { category: 'deal' } })).data
})

const goWrite = () => {
  router.push('/community/new')
}
</script>
