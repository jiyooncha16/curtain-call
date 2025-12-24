<template>
  <div class="page">
    <!-- 헤더 -->
    <header class="page-header">
      <div>
        <p class="page-kicker">STATS</p>
        <h1 class="page-title">좋아요 한 배우</h1>
        <p class="page-sub">관심 있는 배우들을 한눈에 확인해요.</p>
      </div>

      <div class="summary">
        <div class="sum-card">
          <div class="sum-label">배우 수</div>
          <div class="sum-value">{{ actors.length }}</div>
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
        </article>
      </section>

      <!-- 빈 상태 -->
      <section v-else-if="actors.length === 0" class="empty">
        <div class="empty-icon">🎭</div>
        <div class="empty-title">좋아요 한 배우가 없어요</div>
        <div class="empty-sub">마음에 드는 배우에게 ❤️를 눌러보세요!</div>
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
              🎭
            </div>
          </div>

          <div class="meta">
            <div class="name">{{ a.name }}</div>
          </div>
        </article>
      </section>
    </main>
  </div>
</template>

<script setup>
import api from '@/api/axios'
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

/**
 * 기대 데이터 형태
 * [
 *  {
 *    actorId: 1,
 *    name: '조승우',
 *    image: 'uploads/actor/1.jpg',
 *    role: '지킬 앤 하이드',
 *    likeCount: 1
 *  }
 * ]
 */

const actors = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await api.get('/api/actors/like/myActor')
    actors.value = res.data
  } catch (e) {
    console.error(e)
    actors.value = []
  } finally {
    loading.value = false
  }
})

function imgSrc(path) {
  return path.startsWith('/') ? path : '/' + path
}

function goActor(id) {
  if (!id) return
  router.push(`/actor/${id}`)
}
</script>
<style scoped>
.page {
  padding: 18px 18px 60px;
  font-family: 'IBM Plex Sans KR', sans-serif;
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
  font-weight: 800;
  margin: 0;
}

.page-sub {
  margin: 8px 0 0;
  opacity: 0.75;
  font-weight: 600;
}

.summary {
  display: flex;
  gap: 10px;
}

.sum-card {
  border-radius: 14px;
  padding: 10px 12px;
  box-shadow: 0 8px 24px rgba(0,0,0,0.10);
  background: #757575;
  color: white;
  min-width: 80px;
  text-align: center;
}

.sum-label {
  font-size: 13px;
  font-weight: 700;
  opacity: 0.7;
}

.sum-value {
  font-size: 20px;
  font-weight: 800;
  margin-top: 2px;
}

/* ===== GRID ===== */
.grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
}

@media (min-width: 980px) {
  .grid {
    grid-template-columns: repeat(5, minmax(0, 1fr));
  }
}

/* ===== CARD ===== */
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

/* ===== RANK ===== */
.rank {
  position: absolute;
  top: 10px;
  left: 12px;
  font-size: 15px;
  opacity: 0.85;
  font-weight: 800;
  border-radius: 50%;
  background: #111;
  color: white;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* ===== AVATAR ===== */
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
  font-size: 20px;
  font-weight: 900;
  background: #f2f3f6;
}

/* ===== META ===== */
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

/* ===== EMPTY ===== */
.empty {
  border-radius: 18px;
  padding: 40px 16px;
  background: #fff;
  box-shadow: 0 10px 28px rgba(0,0,0,0.12);
  text-align: center;
}

.empty-icon {
  font-size: 34px;
  opacity: 0.6;
}

.empty-title {
  font-size: 18px;
  font-weight: 900;
  margin-top: 10px;
}

.empty-sub {
  opacity: 0.75;
  margin-top: 6px;
}

/* ===== SKELETON ===== */
.skeleton {
  cursor: default;
}

.skeleton .avatar,
.skeleton .line {
  background: linear-gradient(
    90deg,
    #f2f3f6 25%,
    #e9ebf1 37%,
    #f2f3f6 63%
  );
  background-size: 400% 100%;
  animation: shimmer 1.2s ease infinite;
  border-radius: 12px;
}

.skeleton .avatar {
  width: 56px;
  height: 56px;
  border-radius: 16px;
}

.skeleton .meta {
  flex: 1;
}

.skeleton .line {
  height: 12px;
  margin: 8px 0;
}

.skeleton .w60 {
  width: 60%;
}

.skeleton .w40 {
  width: 40%;
}

@keyframes shimmer {
  0% { background-position: 100% 0; }
  100% { background-position: 0 0; }
}
</style>
