<template>
    <div class="wrapper">
        <CardItemActor v-for="i in 10"/>
    </div>
</template>

<script setup>
import CardItem from '../common/CardItem.vue';
import CardItemActor from '../common/CardItemActor.vue';


</script>

<style scoped>
.wrapper {
    display:grid;
    grid-template-columns: repeat(160px, 1fr);
    gap: 10px;
}
</style>