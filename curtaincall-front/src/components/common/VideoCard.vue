<template>
    <div class="flex video-container">
        <a :href="url" class="video-link">
            <div class="img-box">
                <img :src="video.snippet.thumbnails.high.url"></img>
            </div>
            <div class="basic-text youtube-title">{{videoTitle}}</div>
        </a>
    </div>
</template>

<script setup>
import _ from 'lodash';
import { computed } from 'vue';

const props = defineProps({
    video : Object
})
// console.log(props.video.value)
const url = `https://www.youtube.com/watch?v=${props.video.id.videoId}`

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
.video-link {
  display: block;       /* div처럼 동작하도록 */
  text-decoration: none; /* 밑줄 제거 */
  color: inherit;        /* 글자색 유지 */
  width: 100%;
  max-width: 400px;   /* img-box랑 동일 */
}

</style>