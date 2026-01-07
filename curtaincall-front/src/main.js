import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import 'bootstrap-icons/font/bootstrap-icons.css'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import './assets/styles/global.css';
import { Vue3KakaoMaps } from 'vue3-kakao-maps' // ⭐ named import
// import Vue3KakaoMaps from 'vue3-kakao-maps'
const app = createApp(App)

app.use(Vue3KakaoMaps, {
    appkey: import.meta.env.VITE_KAKAO_JS_API_KEY,
    libraries: ['services']
})
// useKakao(import.meta.env.VITE_KAKAO_API_KEY); // 카카오맵 API

app.use(createPinia())
app.use(router)

app.mount('#app')
