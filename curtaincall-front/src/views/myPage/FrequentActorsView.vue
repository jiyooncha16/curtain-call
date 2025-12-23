<template>
  <div class="page">
    <!-- 헤더 -->
    <header class="page-header">
      <div>
        <p class="page-kicker">STATS</p>
        <h1 class="page-title">자주 본 배우</h1>
        <p class="page-sub">내가 자주 본 배우를 횟수 기준으로 확인해요.</p>
      </div>

      <div class="summary">
        <div class="sum-card">
          <div class="sum-label">총 배우 수</div>
          <div class="sum-value">{{ actors.length }}</div>
        </div>
        <div class="sum-card">
          <div class="sum-label">총 횟수</div>
          <div class="sum-value">{{ totalCount }}</div>
        </div>
      </div>
    </header>

    <!-- 본문 -->
    <main class="content">
      <!-- 로딩 -->
      <section v-if="loading" class="grid">
        <article v-for="n in 8" :key="n" class="card skeleton">
          <div class="avatar"></div>
          <div class="meta">
            <div class="line w60"></div>
            <div class="line w40"></div>
          </div>
          <div class="pill"></div>
        </article>
      </section>

      <!-- 빈 상태 -->
      <section v-else-if="actors.length === 0" class="empty">
        <div class="empty-icon"><i class="bi bi-emoji-frown"></i></div>
        <div class="empty-title">표시할 배우가 없어요</div>
        <div class="empty-sub">리뷰를 남기고 내가 만났던 배우를 확인해보세요!</div>
      </section>

      <!-- 리스트 -->
      <section v-else class="grid">
        <article
          v-for="(a, idx) in actors"
          :key="a.actorId ?? idx"
          class="card"
          @click="goActor(a.actorId)"
        >
          <div class="rank">{{ idx + 1 }}</div>

          <div class="avatar-wrap">
            <img
              v-if="a.image"
              class="avatar"
              :src="imgSrc(a.image)"
              :alt="a.name"
              loading="lazy"
            />
            <div v-else class="avatar avatar-fallback">
              {{ initial(a.name) }}
            </div>
          </div>

          <div class="meta">
            <div class="name">{{ a.name }}</div>
          </div>

          <div class="count">
            <div class="count-num">{{ a.count }}</div>
            <div class="count-label">회</div>
          </div>
        </article>
      </section>
    </main>
  </div>
</template>

<script setup>
import api from '@/api/axios'
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

/**
 * 기대 데이터 형태 (예시)
 * [
 *   { actorId: 3, name: '홍길동', image: 'uploads/actor/3.jpg', count: 12, lastSeenDate: '2025-12-20T13:20:00' }
 * ]
 */
const actors = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await api.get('/api/user/main')
    console.log("결과 : ", res.data.favoriteActors)
    actors.value = res.data.favoriteActors ?? []
  } catch (e) {
    console.error(e)
    actors.value = []
  } finally {
    loading.value = false
  }
})

const totalCount = computed(() =>
  actors.value.reduce((sum, a) => sum + (a.count ?? 0), 0)
)

function imgSrc(path) {
  // path가 이미 /로 시작하면 그대로, 아니면 앞에 / 붙이기
  return path.startsWith('/') ? path : '/' + path
}

function initial(name = '') {
  return name?.trim()?.[0] ?? '?'
}

function formatDate(iso) {
  try {
    const d = new Date(iso)
    const y = d.getFullYear()
    const m = String(d.getMonth() + 1).padStart(2, '0')
    const day = String(d.getDate()).padStart(2, '0')
    return `${y}.${m}.${day}`
  } catch {
    return '-'
  }
}

function goActor(actorId) {
  if (!actorId) return
  router.push(`/actor/${actorId}`)
}
</script>

<style scoped>
.page {
  padding: 18px 18px 60px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: flex-end;
  margin-bottom: 18px;
}

.page-kicker {
  letter-spacing: 0.18em;
  font-size: 12px;
  opacity: 0.7;
  margin: 0 0 6px;
}

.page-title {
  font-size: 28px;
  margin: 0;
}

.page-sub {
  margin: 8px 0 0;
  opacity: 0.75;
}

.summary {
  display: flex;
  gap: 10px;
}

.sum-card {
  border-radius: 14px;
  padding: 10px 12px;
  box-shadow: 0 8px 24px rgba(0,0,0,0.10);
  background: #fff;
  min-width: 80px;
  text-align: right;
}
.sum-label {
  font-size: 12px;
  opacity: 0.7;
}
.sum-value {
  font-size: 20px;
  font-weight: 800;
  margin-top: 2px;
}

/* controls */
.controls {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  margin: 10px 0 16px;
  flex-wrap: wrap;
}

.search {
  flex: 1;
  min-width: 240px;
  display: flex;
  align-items: center;
  gap: 10px;
  border-radius: 999px;
  padding: 10px 14px;
  background: #fff;
  box-shadow: 0 8px 24px rgba(0,0,0,0.10);
}
.search input {
  border: none;
  outline: none;
  width: 100%;
  font-size: 14px;
}
.sort {
  display: flex;
  align-items: center;
  gap: 8px;
}
.sort-btn, .sort-toggle {
  border: none;
  cursor: pointer;
  border-radius: 999px;
  padding: 10px 12px;
  background: #fff;
  box-shadow: 0 8px 24px rgba(0,0,0,0.10);
  font-size: 13px;
}
.sort-btn.active {
  font-weight: 800;
}

/* grid */
.grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
}

@media (min-width: 980px) {
  .grid { grid-template-columns: repeat(5, minmax(0, 1fr)); }
}

.card {
  position: relative;
  display: flex;
  align-items: center;
  gap: 12px;
  border-radius: 18px;
  padding: 14px;
  background: #fff;
  box-shadow: 0 10px 28px rgba(0,0,0,0.12);
  cursor: pointer;
  transition: transform .15s ease, box-shadow .15s ease;
}
.card:hover {
  transform: translateY(-2px);
  box-shadow: 0 14px 34px rgba(0,0,0,0.14);
}

.rank {
  position: absolute;
  top: 10px;
  left: 12px;
  font-size: 15px;
  opacity: 0.7;
  font-weight: 700;
  border-radius: 50px;
  background-color: black;
  color : white;
  width : 30px;
  height: 30px;
  display: flex;
  text-align: center;
  align-items: center;
  justify-content: center;
}

.avatar-wrap {
  width: 100px;
  height: 100px;
  border-radius: 16px;
  overflow: hidden;
  flex: 0 0 auto;
  box-shadow: 0 10px 22px rgba(0,0,0,0.12);
}
.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}
.avatar-fallback {
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 900;
  font-size: 18px;
  background: #f2f3f6;
}

.meta {
  flex: 1;
  min-width: 0;
}
.name {
  font-size: 16px;
  font-weight: 900;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.sub {
  margin-top: 4px;
  font-size: 12px;
  opacity: 0.7;
}

.count {
  border-radius: 15px;
  background-color: pink;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 60px;
  min-height : 30px;
  gap: 5px;
}
.count-num {
  font-size: 20px;
  font-weight: 900;
  line-height: 1;
}
.count-label {
  font-size: 12px;
  opacity: 0.7;
  margin-top: 2px;
}

/* empty */
.empty {
  border-radius: 18px;
  padding: 40px 16px;
  background: #fff;
  box-shadow: 0 10px 28px rgba(0,0,0,0.12);
  text-align: center;
}
.empty-icon { font-size: 34px; opacity: 0.6; }
.empty-title { font-size: 18px; font-weight: 900; margin-top: 10px; }
.empty-sub { opacity: 0.75; margin-top: 6px; }

/* skeleton */
.skeleton {
  cursor: default;
}
.skeleton .avatar,
.skeleton .line,
.skeleton .pill {
  background: linear-gradient(90deg, #f2f3f6 25%, #e9ebf1 37%, #f2f3f6 63%);
  background-size: 400% 100%;
  animation: shimmer 1.2s ease infinite;
  border-radius: 12px;
}
.skeleton .avatar { width: 56px; height: 56px; border-radius: 16px; }
.skeleton .meta { flex: 1; }
.skeleton .line { height: 12px; margin: 8px 0; }
.skeleton .w60 { width: 60%; }
.skeleton .w40 { width: 40%; }
.skeleton .pill { width: 56px; height: 22px; border-radius: 999px; }

@keyframes shimmer {
  0% { background-position: 100% 0; }
  100% { background-position: 0 0; }
}
</style>
