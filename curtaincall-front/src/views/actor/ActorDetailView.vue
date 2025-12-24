<template>
  <!-- actor 로드 전에는 아무것도 안 그림 -->
  <div v-if="actor" class="background">
    <div class="box">

    <!-- 뒤로가기 -->
    <!-- 상단 네비 -->
    <!-- <div class="topbar">
      <button class="icon-btn" @click="goBack">
        <i class="bi bi-arrow-left"></i>
      </button>
      <div class="topbar-title">배우 목록</div>
    </div> -->

    <!-- ===== Actor Hero ===== -->
    <div class="actor-hero">
      <!-- 프로필 이미지 -->
      <div class="poster-box">
        <img :src="`/${actor.image}`" :alt="actor.name" />
      </div>

      <!-- 정보 영역 -->
      <div class="info-box">
        <div class="title-row">
          <h1 class="actor-name">{{ actor.name }}</h1>
          <HeartActor />
        </div>

        <div class="meta">
          <div v-if="actor.birth" class="meta-item">
            생년월일 | {{ actor.birth }}
          </div>

          <div v-if="actor.agency" class="meta-item">
            소속사 | {{ actor.agency }}
          </div>

          <a
            v-if="actor.sns"
            :href="actor.sns"
            target="_blank"
            rel="noopener"
            class="sns-link"
          >
            <i class="bi bi-instagram"></i>
            Instagram
          </a>
        </div>

        <p v-if="actor.description" class="description">
          {{ actor.description }}
        </p>
      </div>
    </div>

    
    
    <!-- ===== 출연 작품 ===== -->
    <section>
      <ActorWorkList :works="works" />
    </section>
    
    <div class="title-text">관련 영상</div>
    <!-- ===== 인기 영상 ===== -->
    <div class="shadow" style="margin-bottom: 30px;">
      <!-- ✅ 유튜브 API는 VideoActor에서만 -->
      <VideoActor
        v-if="actor.name"
        :keyword="`${actor.name} 뮤지컬 배우`"
      />
    </div>
    <!-- ===== 관련 배우 ===== -->
    <RelatedActorList :actors="relatedActors" />
  </div>
  </div>
</template>

<script setup>
import axios from 'axios'
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

import VideoActor from '@/components/Video/VideoActor.vue'
import ActorWorkList from '@/components/actor/ActorWorkList.vue'
import RelatedActorList from '@/components/actor/RelatedActorList.vue'
import HeartActor from '@/components/common/icon/HeartActor.vue'
import { parseActorWorks } from '@/utils/workParser'

/* =====================
   router
===================== */
const route = useRoute()
const router = useRouter()

/* =====================
   state
===================== */
const actor = ref(null)
const relatedActors = ref([])

/* =====================
   api
===================== */
const fetchActor = async (actorId) => {
  const { data } = await axios.get(`/api/actors/${actorId}`)
  actor.value = data
}

const fetchRelatedActors = async (actorId) => {
  try {
    const { data } = await axios.get(`/api/actors/${actorId}/related`)
    relatedActors.value = data
  } catch (e) {
    console.error('관련 배우 조회 실패', e)
  }
}

/* =====================
   lifecycle
===================== */
onMounted(() => {
  fetchActor(route.params.id)
  fetchRelatedActors(route.params.id)
})

watch(
  () => route.params.id,
  (newId) => {
    fetchActor(newId)
    fetchRelatedActors(newId)
  }
)

/* =====================
   computed
===================== */
const works = computed(() => {
  if (!actor.value) return []
  return parseActorWorks(actor.value)
})

/* =====================
   methods
===================== */
const goBack = () => {
  router.push('/actor')
}
</script>

<style scoped>

/* .box {
  margin : 20px;
}
.background {
  background-color: rgba(211, 211, 211, 0.37);
} */

.back-btn {
  background: none;
  border: none;
  font-size: 14px;
  cursor: pointer;
  color: #666;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 20px;
}

.back-btn:hover {
  text-decoration: underline;
}

/* ===== Actor Hero ===== */
.actor-hero {
  display: flex;
  gap: 36px;
  align-items: flex-start;
  padding: 32px;
  border-radius: 20px;
  margin-top: 20px;
  margin-bottom: 50px;
  background: linear-gradient(180deg, #fafafa 0%, #ffffff 100%);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

/* 이미지 */
.poster-box {
  width: 240px;
  aspect-ratio: 3 / 4;
  border-radius: 16px;
  overflow: hidden;
  flex-shrink: 0;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.25);
}

.poster-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.poster-box:hover img {
  transform: scale(1.05);
}

/* 정보 */
.info-box {
  flex: 1;
}

.title-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.actor-name {
  font-size: 28px;
  font-weight: 800;
  margin: 0;
}

.actor-name::after {
  content: '';
  display: block;
  width: 25px;
  height: 4px;
  margin-top: 10px;
  background: #f69293;
  border-radius: 2px;
}

/* 메타 */
.meta {
  margin: 20px 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.meta-item {
  font-size: 15px;
  color: #555;
}

/* SNS */
.sns-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #e1306c;
  font-weight: 600;
  text-decoration: none;
}

.sns-link:hover {
  text-decoration: underline;
}

/* 설명 */
.description {
  margin-top: 20px;
  font-size: 14px;
  line-height: 1.7;
  color: #333;
  background: #f9fafb;
  padding: 14px 16px;
  border-radius: 12px;
  border-left: 4px solid #e5e7eb;
}


/* ===== topbar ===== */
.topbar {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 4px 14px;
}
.topbar-title {
  flex: 1;
  font-size: 18px;
  /* font-weight: 800; */
}
.icon-btn {
  width: 38px;
  height: 38px;
  border-radius: 12px;
  border: 1px solid #e6e8ee;
  background: #fff;
  cursor: pointer;
  display: grid;
  place-items: center;
}
.icon-btn:hover { background: #f3f5fa; }
</style>
