<template>
    <div class="flex video-container">
        <div class="img-box">
            <img :src="video.snippet.thumbnails.high.url"></img>
        </div>
        <div class="basic-text youtube-title">{{videoTitle}}</div>
    </div>
</template>

<script setup>
import _ from 'lodash';
import { computed } from 'vue';

const props = defineProps({
    video : Object
})

// 제목 escape문자 처리
// npm install lodash
const videoTitle = computed(()=> {
    return _.unescape(props.video.snippet.title)
})
</script>

<style scoped>

.img-box {
    width: 100%;
    max-width: 400px;
    aspect-ratio: 5 / 3;   
    overflow: hidden;      /* 삐져나온 부분 자르기 */
    flex-shrink: 0;       /* flex에서 줄어들지 않게 */
}
.img-box img {
    width: 100%;
    height: 100%;
    object-fit: cover;     /* 중앙 기준 크롭 */
    display: block;
}
.video-container {
    flex-direction: column;
    align-items:center;
}
.youtube-title {
  width: 100%;
  white-space: nowrap;      /* 줄바꿈 금지 */
  overflow: hidden;         /* 넘친 부분 숨김 */
  text-overflow: ellipsis;  /* ... 처리 */
}
</style>