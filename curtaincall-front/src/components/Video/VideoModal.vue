<template>
  <teleport to="body">
    <div class="modal-overlay" @click.self="close">
      <div class="modal-content">
        <button class="close-btn" @click="close">✕</button>

        <iframe
          v-if="videoId"
          :src="youtubeUrl"
          frameborder="0"
          allow="autoplay; encrypted-media"
          allowfullscreen
        ></iframe>
      </div>
    </div>
  </teleport>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  videoId: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['close'])

const close = () => emit('close')

const youtubeUrl = computed(() =>
  `https://www.youtube.com/embed/${props.videoId}?autoplay=1`
)
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.75);
  z-index: 9999;

  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-content {
  position: relative;
  width: 80%;
  max-width: 900px;
  aspect-ratio: 16 / 9;
  background: #000;
  border-radius: 12px;
  overflow: hidden;
}

iframe {
  width: 100%;
  height: 100%;
}

.close-btn {
  position: absolute;
  top: -40px;
  right: 0;
  background: none;
  border: none;
  color: #fff;
  font-size: 28px;
  cursor: pointer;
}
</style>
