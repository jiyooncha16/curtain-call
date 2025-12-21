<template>
    <header>
    <div class="board-row">
        <router-link to="/"><img src="../../assets/curtaincall.png" width="100px"></router-link>
        <router-link :to="{name : 'musical'}" class="headerText">뮤지컬</router-link>
        <router-link :to="{name : 'actor'}" class="headerText">배우</router-link>
        <router-link :to="{name : 'community'}" class="headerText">커뮤니티</router-link>
    </div>
    <div class="board-row">
        <search-bar/>
        <!-- 🔽 프로필 드롭다운 -->
      <div class="dropdown hover-dropdown">
        <button
          class="btn p-0 border-0 dropdown-toggle"
          type="button"
          aria-expanded="false"
        >
          <i class="bi bi-person-fill headerText"></i>
        </button>

        <ul class="dropdown-menu dropdown-menu-end">
          <!-- 로그인되어있다면 -->
          <template v-if="isLogin">
            <li>
              <router-link class="dropdown-item"
                :to="{ name: 'myPage', params: { id: userId } }"
              >
                마이페이지
              </router-link>
            </li>
            <li>
              <button class="dropdown-item" @click="logout">
                로그아웃
              </button>
            </li>
          </template>

          <!-- ❌ 비로그인 상태 -->
          <template v-else>
            <li>
              <router-link class="dropdown-item" :to="{ name: 'login' }">
                로그인
              </router-link>
            </li>
            <li>
              <router-link class="dropdown-item" :to="{ name: 'signup' }">
                회원가입
              </router-link>
            </li>
          </template>
        </ul>
      </div>
    </div>
</header>

</template>

<script setup>
import SearchBar from '../common/icon/SearchBar.vue';

import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { parseJwt } from '@/utils/jwt'
import { useAuthStore } from '@/stores/auth' // 로그인을 위한 피니아
import { storeToRefs } from 'pinia';
const auth = useAuthStore()
const router = useRouter()

//로그인 정보 가져오기
const { isLogin, userId } = storeToRefs(auth)

//로그아웃
const logout = () => {
  auth.logout()
  router.push({ name: 'login' })
}
</script>

<style scoped>
    
/* 헤더, 푸터 */
header {
    width:100%;
    height: 60px;
    padding : 0;
    margin : 0;
    display:flex;
    align-items: center;
    background-color: #800000;
    justify-content: space-between;
}
.headerText {
    font-family: "IBM Plex Sans KR", sans-serif;
    font-size: 18px;
    font-weight: bold;
    color: white;
    margin: 15px;
    align-items: center;
    justify-content: center;
}
#searchBar {
    width:250px;
    height:30px;
    border: white 2px solid;
    display: flex;
    align-items: center;
    justify-content: end;
}
.board-row {
    display:flex;
    flex-direction: row;
    align-items: center;
}

a {
    text-decoration: none;
}
.bi-search {
    color:white; 
    margin-right: 10px;
}
.bi-person-fill {
    color:white; 
    margin: 0 20px; 
    font-size: 25px;
}
.headerText {
  font-size: 20px;
  cursor: pointer;
}
.dropdown-toggle::after {
  display: none;
}
:deep(.hover-dropdown .dropdown-menu) {
    right: 10px;
  min-width: 100px;
  padding: 6px 0;
  border-radius: 12px;
  border: none;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.15);
  opacity: 0;
  transform: translateY(5px);
  transition: opacity 0.15s ease, transform 0.15s ease;
  display: block;
  pointer-events: none;
}

:deep(.hover-dropdown:hover .dropdown-menu) {
  opacity: 1;
  transform: translateY(0);
  pointer-events: auto;
}

:deep(.hover-dropdown .dropdown-item) {
  font-size: 14px;
  padding: 10px 16px;
  transition: background-color 0.15s ease;
}

:deep(.hover-dropdown .dropdown-item:hover) {
  background-color: #c0c0c0;
  color: white;
}
</style>