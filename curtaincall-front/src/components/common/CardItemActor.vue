<template>
  <div class="actor-card" @click="goDetail">
    <div class="poster">
      <img
        v-if="actor.image"
        :src="`/${actor.image}`"
        :alt="actor.name"
      />

      <!-- 하단 오버레이 -->
      <div class="overlay">
        <div class="name">
          {{ actor.name }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
  actor: {
    type: Object,
    required: true
  }
})

const router = useRouter()

const goDetail = () => {
  router.push(`/actor/${props.actor.actorId}`)
}
</script>

<style scoped>
/* ===== Card Wrapper ===== */
.actor-card {
  cursor: pointer;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.actor-card:hover {
  transform: translateY(-6px);
}

/* ===== Poster ===== */
.poster {
  position: relative;
  width: 100%;
  aspect-ratio: 3 / 4;
  border-radius: 16px;
  overflow: hidden;
  background: #eee;
  box-shadow: 0 12px 28px rgba(0,0,0,0.1);
}

.poster img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.35s ease;
}

.actor-card:hover img {
  transform: scale(1.06);
}

/* ===== Bottom Overlay ===== */
.overlay {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;

  padding: 36px 14px 14px;

  background: linear-gradient(
    to top,
    rgba(0,0,0,0.85),
    rgba(0,0,0,0.45),
    rgba(0,0,0,0)
  );
}

/* 배우 이름 */
.name {
  color: #fff;
  font-size: 15px;
  font-weight: 700;
  line-height: 1.2;
  text-align: center;

  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
