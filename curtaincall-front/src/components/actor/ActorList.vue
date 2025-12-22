<template>
    <div class="wrapper">
        <CardItemActor v-for="actor in searchResult" :actor=actor :key="actor.actorId"/>
    </div>
</template>

<script setup>
import CardItemActor from '../common/CardItemActor.vue';

const props = defineProps ({
    searchResult : Array
})

</script>

<style scoped>
.wrapper {
    display:grid;
    grid-template-columns: repeat(160px, 1fr);
    gap: 10px;
}
</style>