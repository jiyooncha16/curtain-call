<template>
    <div v-if="user" class="user-summary">
        <!-- 상단 -->
        <div class="top">
            <div class="profile">
                <img :src="profileImg" />
                <div class="nickname">{{ user.nickname }}</div>
            </div>

            <div class="taste">
                <HashtagForMypage :tags="user.tags" />
            </div>
        </div>

        <hr />

        <!-- 통계 -->
        <div class="stats">
            <div class="stat">
                <div class="value">Like</div>
                <div class="label">나의 취향</div>
            </div>
            <div class="stat">
                <div class="value">{{ user.counts.likeMusicalCount }}</div>
                <div class="label">뮤지컬</div>
            </div>
            <div class="stat">
                <div class="value">{{ user.counts.likeActorCount }}</div>
                <div class="label">배우</div>
            </div>
            <div class="stat clickable" @click="$emit('goReview')">
                <div class="value">{{ user.counts.reviewCount }}</div>
                <div class="label">리뷰</div>
            </div>
        </div>
    </div>
</template>


<script setup>
import HashtagForMypage from '@/components/common/icon/HashtagForMypage.vue'

defineProps({
    user: Object,
    profileImg: String
})
</script>

<style scoped>
.user-summary {
    width: 320px;
    /* 🔥 드롭다운 크기 */
    padding: 16px;
}

.top {
    display: flex;
    gap: 14px;
}

.profile img {
    width: 72px;
    height: 72px;
    border-radius: 50%;
    object-fit: cover;
}

.nickname {
    text-align: center;
    font-weight: bold;
    margin-top: 6px;
}

.stats {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 8px;
    margin-top: 12px;
}

.stat {
    text-align: center;
    font-size: 13px;
}
</style>
