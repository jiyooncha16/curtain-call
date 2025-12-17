<template>
    <div class="box" @click="heartClicked">
        <div><i :class="icon"></i> 40</div>
    </div>
</template>

<script setup>
import { ref } from 'vue';


const icon = ref("bi bi-heart")
const heartClicked = function() {
    if (icon.value === "bi bi-heart") {
        icon.value = "bi bi-heart-fill"
    } else {
        icon.value = "bi bi-heart"
    }
}

</script>

<style scoped>
    .box {
        border: 1px #800000 solid;
        border-radius: 10px;
        color : #800000;
        font-size: 16px;
        font-weight: bold;
        width: 60px;
        height: 35px;
        display: flex;
        align-items: center;
        justify-content: center;
    }

</style>