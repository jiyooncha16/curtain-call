<template>
    <div class="flex">
        <div v-for="i in rate"><i class="bi bi-star-fill"></i></div>
        <div v-for="i in (5-rate)"><i class="bi bi-star"></i></div>
    </div>
</template>

<script setup>
const props = defineProps({
    rate : Number
})
</script>

<style scoped>
div {
    color : rgba(255, 213, 0, 0.963)
}
</style>