<template>
    <button class="back-btn" @click="goBack">
        <i class="bi bi-arrow-left"></i>
        {{ label }}
    </button>
</template>

<script setup>
import { useRouter } from 'vue-router'

defineProps({
    label: {
        type: String,
        default: '뒤로 가기'
    }
})

const router = useRouter()

const goBack = () => {
    router.back()
}
</script>

<style scoped>
.back-btn {
    background: none;
    border: none;
    font-size: 14px;
    cursor: pointer;
    color: #666;
    display: inline-flex;
    align-items: center;
    gap: 6px;
}

.back-btn:hover {
    text-decoration: underline;
}
</style>
