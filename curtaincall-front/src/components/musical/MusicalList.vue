<template>
    <div>
        <MusicalListItem v-for="(musical, idx) in musicalList" :key="idx" :musical="musical"/>
    </div>
</template>

<script setup>
import MusicalListItem from './MusicalListItem.vue';

const props = defineProps ({
    musicalList: Array
})


</script>

<style scoped>

</style>