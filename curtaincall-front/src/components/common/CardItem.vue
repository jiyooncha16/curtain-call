<template>
    <div class="poster" @click="clicked(item.musicalId)">
        <div class="card">
            <div class="img-box">
                <img :src="`/${item.image}`">
                 <!-- <img :src="musical.이미지........"> -->
            </div>
            <div class="title basic-text">{{item.title}}</div>
        </div>
    </div>
</template>

<script setup>
import router from '@/router';

const props = defineProps({
    item: Object
})
console.log(props.item)
const clicked = function(id) {
    router.push(`/musical/${id}`)
}
</script>

<style scoped>
.img-box {
    width: 100%;
    aspect-ratio: 3/4; /*비율*/
    overflow: hidden;
    /* border-radius: 12px; */
}
img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    display: block;
}
.poster {
    display: flex;
    flex-direction: column;
    align-items: center;
    overflow: visible;
}

 .card {
  background: #fff;
  border-radius: 12px;
  border-color: white;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  text-align: center;
  width: 100%;       /* 🔥 이게 핵심 */
  max-width: 180px;  /* 카드 크기 통일 (원하는 값) */
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0,0,0,0.12);
}
/* 
.title {
  padding: 5px;
  font-size: 14px;
  font-weight: 600;
  text-align: center;
} */
.title {
  text-align: center;
  white-space: nowrap;        /* 줄바꿈 금지 */
  overflow: hidden;           /* 넘치는 텍스트 숨김 */
  text-overflow: ellipsis;    /* ... 처리 */
}

</style>