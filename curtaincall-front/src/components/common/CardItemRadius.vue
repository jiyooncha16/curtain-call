<template>
    <div class="poster" @click="clicked(actor.actorId)">
        <div class="img-box">
            <img :src="`/${actor.image}`" />
        </div>
        <div class="basic-text">{{ actor.name }}</div>
    </div>
</template>

<script setup>
import router from '@/router'

defineProps({
    actor: Object
})

const clicked = (id) => {
    router.push(`/actor/${id}`)
}
</script>

<style scoped>
.img-box {
    width: 70%;
    aspect-ratio: 1 / 1;
    border-radius: 50%;
    overflow: hidden;
}

img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.poster {
    display: flex;
    flex-direction: column;
    align-items: center;
    /* margin: 10px; */
    cursor: pointer;
}

.basic-text {
    margin-top: 6px;
    font-size: 14px;
}
</style>
