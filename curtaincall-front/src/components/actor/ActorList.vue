<template>
    <div class="wrapper">
        <CardItem v-for="i in 10"/>
    </div>
</template>

<script setup>
import CardItem from '../common/CardItem.vue';


</script>

<style scoped>
.wrapper {
    display:grid;
    grid-template-columns: repeat(160px, 1fr);
    gap: 10px;
}
</style>