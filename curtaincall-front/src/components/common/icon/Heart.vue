<template>
    <div class="box" :class="{ active: isLiked }" @click="heartClicked">
        <div><i :class="icon"></i> {{likeCnt}}</div>
    </div>
</template>

<script setup>
import { computed, ref } from 'vue';
const props = defineProps ({
    like : Number,
})

// const likeCnt = ref(like)

// 좋아요 처리
const isLiked = ref(false)
const icon = computed(() =>
  isLiked.value ? 'bi bi-heart-fill' : 'bi bi-heart'
)

// 좋아요 숫자
const likeCnt = ref(props.like)
const heartClicked = () => {
  isLiked.value = !isLiked.value
  likeCnt.value += isLiked.value ? 1 : -1
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