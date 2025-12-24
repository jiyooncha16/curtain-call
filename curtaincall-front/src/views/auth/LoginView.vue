<template>
  <div class="page">
    <!-- 헤더 -->
    <header class="page-header">
      <div class="header-inner">
        <p class="page-kicker">MY PAGE</p>
        <h1 class="page-title">로그인</h1>
      </div>
    </header>

    <!-- 본문 -->
    <main class="content">
      <section class="card login-card">
        <form @submit.prevent="login">

          <div class="form-group">
            <label>아이디</label>
            <input
              type="text"
              v-model="username"
              placeholder="아이디를 입력하세요"
            />
          </div>

          <div class="form-group">
            <label>비밀번호</label>
            <input
              type="password"
              v-model="password"
              placeholder="비밀번호를 입력하세요"
            />
          </div>

          <button class="btn primary" type="submit">
            로그인
          </button>

          <div class="extra">
            <span @click="goSignup">회원가입</span>
            <span class="divider">|</span>
            <span @click="goFindId">아이디 찾기</span>
            <span class="divider">|</span>
            <span @click="goChangePassword">비밀번호 재설정</span>
          </div>

        </form>
      </section>
    </main>
  </div>
</template>
<script setup>
import api from '@/api/axios'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const router = useRouter()

const username = ref('')
const password = ref('')

const login = async () => {
  if (!username.value || !password.value) {
    alert('아이디와 비밀번호를 입력해주세요.')
    return
  }

  try {
    const res = await api.post('/api/user/auth/login', {
      username: username.value,
      password: password.value
    })

    auth.login(res.data.accessToken)
    alert('로그인에 성공했습니다.')
    router.push('/')
  } catch (e) {
    alert('아이디 또는 비밀번호가 잘못되었습니다.')
  } finally {
    username.value = ''
    password.value = ''
  }
}

const goSignup = () => router.push('/signup')
const goFindId = () => router.push('/login/findId')
const goChangePassword = () => router.push('/login/changePassword')
</script>
<style scoped>
.page {
  padding: 60px 18px 60px 18px;
  font-family: 'IBM Plex Sans KR', sans-serif;
}

.page-header {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.header-inner {
  width: 100%;
  max-width: 420px;
}

.page-kicker {
  font-size: 12px;
  letter-spacing: 0.18em;
  opacity: 0.7;
}

.page-title {
  font-size: 28px;
  font-weight: 800;
  margin: 6px 0;
}

.content {
  display: flex;
  justify-content: center;
}

.card {
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 10px 28px rgba(0,0,0,0.12);
}

.login-card {
  width: 100%;
  max-width: 420px;
  padding: 28px;
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 16px;
}

label {
  font-size: 13px;
  font-weight: 700;
  margin-bottom: 6px;
}

input {
  border-radius: 12px;
  border: 1px solid #ddd;
  padding: 12px 14px;
  font-size: 14px;
}

input:focus {
  outline: none;
  border-color: #111;
}

.btn {
  width: 100%;
  margin-top: 10px;
  padding: 12px;
  border-radius: 12px;
  border: none;
  font-weight: 800;
  cursor: pointer;
}

.btn.primary {
  background: #111;
  color: #fff;
}

.btn.primary:hover {
  opacity: 0.9;
}

.extra {
  margin-top: 18px;
  text-align: center;
  font-size: 13px;
  color: #666;
}

.extra span {
  cursor: pointer;
}

.divider {
  margin: 0 6px;
  opacity: 0.5;
}
</style>
