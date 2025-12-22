<template>
    <div class="actor-card" @click="goDetail">
        <img :src="`/${actor.image}`" alt="actor" />
        <div class="name">{{ actor.name }}</div>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
    actor: Object
})

const router = useRouter()

const goDetail = () => {
  router.push(`/actor/${props.actor.actorId}`)
}

</script>

<style scoped>
.actor-card {
    cursor: pointer;
    border-radius: 14px;
    overflow: hidden;
    background: #fff;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.actor-card:hover {
    transform: translateY(-6px);
    box-shadow: 0 10px 24px rgba(0, 0, 0, 0.15);
}

.actor-card img {
    width: 100%;
    aspect-ratio: 3 / 4;
    object-fit: cover;
}

.name {
    padding: 10px;
    text-align: center;
    font-weight: 600;
}


</style>
