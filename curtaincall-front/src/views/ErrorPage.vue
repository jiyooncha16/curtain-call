<template>
  <div class="page error-page">
    <!-- 헤더 -->

    <!-- 본문 -->
    <main class="content">
      <section class="card error-card">
        <div class="error-icon">😢</div>
        <h1 class="page-title">{{ title }}</h1>
        <p class="page-desc">
          {{ message }}
        </p>
        <div class="error-actions">
          <button class="btn primary" @click="goHome">
            홈으로 가기
          </button>
          <button class="btn" @click="goBack">
            이전 페이지
          </button>
        </div>
      </section>
    </main>
  </div>
</template>
<script setup>
import { useRouter, useRoute } from 'vue-router'
import { computed } from 'vue'

const router = useRouter()
const route = useRoute()

// query 또는 meta로 상태 코드 받기
const code = computed(() => route.query.code || '404')

const title = computed(() => {
  if (code.value === '500') return '서버 오류가 발생했어요'
  if (code.value === '403') return '접근 권한이 없어요'
  return '페이지를 찾을 수 없어요'
})

const message = computed(() => {
  if (code.value === '500') {
    return '잠시 후 다시 시도해주세요.'
  }
  if (code.value === '403') {
    return '이 페이지에 접근할 수 없습니다.'
  }
  return '요청하신 페이지가 존재하지 않거나 이동되었습니다.'
})

const goHome = () => {
  router.push('/')
}

const goBack = () => {
  router.back()
}
</script>
<style scoped>
.page {
  padding: 18px 18px 60px;
  font-family: 'IBM Plex Sans KR', sans-serif;
}

.page-header {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.header-inner {
  width: 100%;
  max-width: 420px;
  text-align: center;
}

.page-kicker {
  font-size: 12px;
  letter-spacing: 0.18em;
  opacity: 0.7;
}

.page-title {
  font-size: 28px;
  font-weight: 800;
  margin: 6px 0;
}

.page-desc {
  margin-top: 6px;
  font-size: 13px;
  opacity: 0.7;
  line-height: 1.5;
}

.content {
  display: flex;
  justify-content: center;
}

.card {
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 10px 28px rgba(0,0,0,0.12);
}

.error-card {
  width: 100%;
  max-width: 420px;
  padding: 32px 24px;
  text-align: center;
}

.error-icon {
  font-size: 48px;
  margin-bottom: 20px;
}

.error-actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.btn {
  flex: 1;
  padding: 12px;
  border-radius: 12px;
  border: none;
  font-weight: 800;
  cursor: pointer;
}

.btn.primary {
  background: #111;
  color: #fff;
}

.btn.primary:hover {
  opacity: 0.9;
}

.btn:not(.primary) {
  background: #f2f3f6;
}
</style>
