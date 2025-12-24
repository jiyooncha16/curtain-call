<template>
    <div class="role-group">

        <div v-for="group in groupedByRole" :key="group.roleName" class="role-section">
            <!-- 🔹 역 제목 -->
            <div class="role-title">
                {{ group.roleName }}
            </div>

            <!-- 🔹 배우 카드 가로 나열 -->
            <div class="actor-row">
                <CardItemRadius v-for="actor in group.actors" :key="actor.actorId" :actor="actor" />
            </div>
        </div>

    </div>
</template>

<script setup>
import { computed } from 'vue'
import CardItemRadius from '../common/CardItemRadius.vue'

const props = defineProps({
    actors: {
        type: Array,
        required: true
    }
})

/**
 * ✅ 역 기준 그룹핑
 * - 첫 등장 순서 유지
 */
const groupedByRole = computed(() => {
    const result = []
    const roleMap = {}

    props.actors.forEach(actor => {
        const role = actor.roleName

        // 처음 등장한 역
        if (!roleMap[role]) {
            roleMap[role] = {
                roleName: role,
                actors: []
            }
            result.push(roleMap[role]) // ✅ 순서 보장
        }

        roleMap[role].actors.push(actor)
    })

    return result
})
</script>

<style scoped>
.role-group {
    width: 100%;
}

/* 역 섹션 */
.role-section {
    width: 100%;
    margin-bottom: 24px;
}

.role-title {
    font-size: 16px;
    font-weight: 700;
    margin-bottom: 10px;
}

/* 배우 가로 나열 */
.actor-row {
    display: flex;
    gap: 12px;
    overflow-x: auto;
    padding-bottom: 4px;
}

/* 카드 크기 고정 */
.poster {
    flex: 0 0 auto;
    width: 150px;
}
</style>
