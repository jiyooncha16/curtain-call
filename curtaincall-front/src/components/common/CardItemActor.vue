<template>
  <div class="poster" @click="goDetail">
    <div class="card" >
      <div class="img-box">
        <img
          v-if="actor?.image"
          :src="`/${actor.image}`"
          :alt="actor.name"
        />
      </div>
      <div class="title basic-text">{{ actor?.name }}</div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'


const props = defineProps({
  actor: {
    type: Object,
    required: true
  }
})

const router = useRouter()

const goDetail = () => {
  router.push(`/actor/${props.actor.actorId}`)
}
</script>

<style scoped>
.img-box {
    width: 100%;
    aspect-ratio: 3/4; /*비율*/
    /* border-radius: 12px; */
}
img {
    width: 100%;
    height: 100%;
    object-fit:cover;
}
.poster {
    display: flex;
    flex-direction: column;
    align-items: center;
}

 .card {
  background: #fff;
  border-radius: 12px;
  border-color: white;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  text-align: center;
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


</style>