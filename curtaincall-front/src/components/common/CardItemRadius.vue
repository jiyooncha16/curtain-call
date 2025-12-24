<template>
    <div class="poster" @click="clicked(actor.actorId)">
        <div class="img-box">
            <img :src="`/${actor.image}`">
        </div>
        <div class="basic-text">{{actor.name}}</div>
        <div class="role-text">({{ actor.roleName }})</div>
    </div>
</template>

<script setup>
import router from '@/router';

const props = defineProps({
    actor : Object,
})

console.log(props.actor)
const clicked = function(id) {
    router.push(`/actor/${id}`)

}
</script>

<style scoped>
.img-box {
    width: 60%;
    aspect-ratio: 1/1;
    border-radius: 50%;
    overflow: hidden;
}
img {
    width: 100%;
    height: 100%;
    object-fit:cover;
    
}
.poster {
  display: flex;
  flex-direction: column;  /* 이미지 위 / 이름 아래 */
  align-items: center;
  margin: 10px;
}
.basic-text {
    margin: 0
}
.role-text{
    .empty {
  font-size: 15px;
  color: #b6b6b6;
}
}

</style>