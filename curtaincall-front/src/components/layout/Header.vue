<template>
  <header>
    <!-- 🔹 왼쪽: 로고 + 메뉴 -->
    <div class="board-row left">
      <router-link to="/">
        <img src="../../assets/curtaincall.png" width="100px" />
      </router-link>

      <router-link :to="{ name: 'musical' }" class="headerText">
        뮤지컬
      </router-link>
      <router-link :to="{ name: 'actor' }" class="headerText">
        배우
      </router-link>
      <router-link :to="{ name: 'community' }" class="headerText">
        커뮤니티
      </router-link>
    </div>

    <!-- 🔹 오른쪽: 인증 / 마이페이지 -->
    <div class="board-row right">
      <!-- ❌ 비로그인 -->
      <template v-if="!isLogin">
        <router-link :to="{ name: 'login' }" class="headerText auth-btn">
          로그인
        </router-link>
        <router-link :to="{ name: 'signup' }" class="headerText auth-btn">
          회원가입
        </router-link>
      </template>

      <!-- ✅ 로그인 -->

      <template v-else>
        <!-- 로그아웃은 상단에 그대로 -->
        <button class="headerText auth-btn logout-btn" @click="logout">
          로그아웃
        </button>

        <!-- 마이페이지 드롭다운 -->
        <div class="dropdown hover-dropdown">
          <button class="btn p-0 border-0" type="button">
            <i class="bi bi-person-fill"></i>
          </button>


          <ul class="dropdown-menu dropdown-menu-end">
            <li>
              <router-link class="dropdown-item" :to="{ name: 'myPage', params: { id: userId } }">
                내 정보
              </router-link>

            </li>
          </ul>
        </div>
      </template>
    </div>
  </header>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { storeToRefs } from 'pinia'

const auth = useAuthStore()
const router = useRouter()

const { isLogin, userId } = storeToRefs(auth)

const logout = () => {
  auth.logout()
  router.push({ name: 'login' })
}
</script>

<style scoped>
/* ===== Header ===== */
header {
  width: 100%;
  height: 60px;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #800000;
  box-sizing: border-box;
}

/* ===== Rows ===== */
.board-row {
  display: flex;
  align-items: center;
}

.board-row.left {
  gap: 12px;
}

.board-row.right {
  gap: 14px;
}

/* ===== Text ===== */
.headerText {
  font-family: "IBM Plex Sans KR", sans-serif;
  font-size: 18px;
  font-weight: bold;
  color: white;
  text-decoration: none;
  cursor: pointer;
}

/* ===== Auth Buttons ===== */
.auth-btn {
  font-size: 16px;
  font-weight: 600;
}

.logout-btn {
  background: none;
  border: none;
  padding: 0;
}

/* ===== Icon ===== */
.bi-person-fill {
  color: white;
  font-size: 26px;
  cursor: pointer;
}

/* ===== Dropdown ===== */
.dropdown-toggle::after {
  display: none;
}

:deep(.hover-dropdown .dropdown-menu) {
  right: 0;
  min-width: 120px;
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
}

:deep(.hover-dropdown .dropdown-item:hover) {
  background-color: #c0c0c0;
  color: white;
}
</style>
