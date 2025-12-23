<template>
  <div v-if="actor">
    <BackBtn label="배우 목록으로" />
    <div class="actor-hero">

      <!-- 프로필 이미지 -->
      <div class="poster-box">
        <img :src="`/${actor.image}`" :alt="actor.name" />
      </div>

      <!-- 정보 영역 -->
      <div class="info-box">
        <div class="title-row">
          <h1 class="actor-name">{{ actor.name }}</h1>
          <HeartActor @click="heartClicked" />
        </div>

        <div class="meta">
          <div v-if="actor.birth" class="meta-item">
            생년월일 | {{ actor.birth }}
          </div>
          <div v-if="actor.agency" class="meta-item">
            소속사 | {{ actor.agency }}
          </div>

          <a v-if="actor.sns" :href="actor.sns" target="_blank" rel="noopener" class="sns-link">
            <i class="bi bi-instagram"></i> Instagram
          </a>
        </div>

        <p v-if="actor.description" class="description">
          {{ actor.description }}
        </p>
      </div>
    </div>

    <!-- 출연 작품 -->
    <section>
      <ActorWorkList :works="works" />
    </section>
    <RelatedActorList :actors="relatedActors" />
  </div>
</template>

<script setup>

import api from '@/api/axios';
import ActorWorkList from '@/components/actor/ActorWorkList.vue';
import RelatedActorList from '@/components/actor/RelatedActorList.vue';
import BackBtn from '@/components/common/icon/BackBtn.vue';
import HeartActor from '@/components/common/icon/HeartActor.vue';
import { useAuthStore } from '@/stores/auth';
// import Heart from '@/components/common/icon/Heart.vue';
import { parseActorWorks } from '@/utils/workParser';
import axios from 'axios';
import { onMounted, ref, computed, watch } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute()
const id = computed(() => route.params.id)

//배우 정보 받아오기
const actor = ref(null)
const name = ref('')
const description = ref('')
const relatedActors = ref([])

const fetchActor = async () => {
  const { data } = await axios.get(`/api/actors/${id.value}`)
  actor.value = data
}

const fetchRelatedActors = async () => {
  try {
    const { data } = await axios.get(`/api/actors/${id.value}/related`)
    relatedActors.value = data
  } catch (e) {
    console.error('관련 배우 조회 실패', e)
  }
}

onMounted(() => {
  fetchActor()
  fetchRelatedActors()
})
watch(
  () => route.params.id,
  () => {
    fetchActor()
    fetchRelatedActors()
  }
)



// 배우 work 위해서
const works = computed(() => {
  if (!actor.value) return []
  return parseActorWorks(actor.value)
})

// 하트 처리
// const auth = useAuthStore()
// const isHearted = ref(false)
// const userId = auth.userId

// const heartClicked = async () => {
//     try {
//         if (!isHearted.value) { // 하트 안 눌려있었다면 좋아요 post
//             await api.post(`/api/actors/toggle/like/${id.value}`)
//             isHearted.value = true
//         } else {
//             // 좋아요 취소
//             await api.delete(`/api/actors/like/${id.value}?userId=${userId}`)
//             isHearted.value = false
//         }
//     } catch (e) {
//         console.error('하트 처리 실패', e)
//     }
// }

</script>

<style scoped>
.actor-detail {
  margin-top: 20px;
}

.profile-wrapper {
  display: flex;
  align-items: flex-start;
  margin: 20px 0;
}

.img-box {
  width: 200px;
  aspect-ratio: 3 / 4;
  margin-right: 30px;
}

.img-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.info-box {
  flex: 1;
}

.name-row {
  display: flex;
  align-items: center;
  gap: 10px;
}

.actor-name {
  font-size: 35px;
  margin: 0;
}

.meta {
  margin-top: 20px;
}

.description {
  margin-top: 10px;
  white-space: pre-line;
}

.sns {
  display: inline-block;
  margin-top: 8px;
  font-size: 20px;
}

/* =========================
   Actor Hero
========================= */
.actor-hero {
  display: flex;
  gap: 36px;
  align-items: flex-start;
  padding: 32px;
  border-radius: 20px;
  margin-top: 30px;
  margin-bottom: 50px;
  background: linear-gradient(180deg,
      #fafafa 0%,
      #ffffff 100%);

  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

/* 이미지 영역 */
.actor-hero .poster-box {
  width: 240px;
  aspect-ratio: 3 / 4;
  border-radius: 16px;
  overflow: hidden;
  flex-shrink: 0;

  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.25);
}

.actor-hero .poster-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.actor-hero .poster-box:hover img {
  transform: scale(1.05);
}

/* 정보 영역 */
.actor-hero .info-box {
  flex: 1;
  padding-top: 6px;
}

/* 이름 + 좋아요 */
.actor-name {
  font-size: 28px;
  font-weight: 800;
  margin: 0;
  line-height: 1.3;
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

/* 메타 정보 */
.actor-hero .meta {
  margin: 20px 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.actor-hero .meta-item {
  font-size: 15px;
  color: #555;
}

/* SNS */
.sns-link {
  margin-top: 6px;
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

.title-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
