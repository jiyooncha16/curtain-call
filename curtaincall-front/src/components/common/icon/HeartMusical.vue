<template>
    <div class="box" :class="{ active: isLiked }" @click="heartClicked">
        <div><i :class="icon"></i> {{likeCnt}}</div>
    </div>
</template>

<script setup>
import api from '@/api/axios';
// import axios from 'axios';
import { computed, onMounted, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
const props = defineProps ({
    like : Number,
})

const route = useRoute()
const id = route.params.id

// 좋아요 처리
const isLiked = ref(false)
const icon = computed(() =>
  isLiked.value ? 'bi bi-heart-fill' : 'bi bi-heart'
)

// 좋아요 숫자
const likeCnt = ref(props.like)
watch(() => props.like, (v) => {likeCnt.value = v}) // 변경되도록

onMounted(async () => {
  const res = await api.get(`/api/musicals/like/me/${id}`) // 내가 눌렀는지?
  isLiked.value = res.data

})
const heartClicked = async () => {
  const res = await api.post(`/api/musicals/like/toggle/${id}`) // 토글하기
  isLiked.value = res.data   // true / false

  const cntRes = await api.get(`/api/musicals/like/${id}`) // 개수 받기
  likeCnt.value = cntRes.data
}

</script>

<style scoped>
.box {
  border: 1px solid #800000;
  border-radius: 10px;

  color: #800000;
  background: transparent;

  font-size: 16px;
  font-weight: bold;
  width: 60px;
  height: 36px;

  display: flex;
  align-items: center;
  justify-content: center;

  cursor: pointer;
  transition: 
    background-color 0.2s ease,
    color 0.2s ease,
    transform 0.15s ease;
}

/* 눌렸을 때 */
.box.active {
  background-color: #800000;
  color: #ffffff;
  border-color: #800000;
}

/* 살짝 눌리는 느낌 */
.box:active {
  transform: scale(0.95);
}
.box:hover {
  background: rgba(128, 0, 0, 0.68);
}

</style>