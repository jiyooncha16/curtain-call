<template>
  <div class="login-wrapper">
    <form class="login-box" @submit.prevent="login">
      <h2 class="title">로그인</h2>

      <div class="input-group">
        <label>아이디</label>
        <input type="text" v-model="username" placeholder="아이디를 입력하세요" />
      </div>

      <div class="input-group">
        <label>비밀번호</label>
        <input type="password" v-model="password" placeholder="비밀번호를 입력하세요" />
      </div>

      <button class="login-btn" type="submit">
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
  </div>
</template>

<script setup>
import api from '@/api/axios'
import axios from 'axios'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const router = useRouter()

const username = ref('')
const password = ref('')

// 로그인 비동기 요청
const login = () => {

  // 둘 다 입력해야
  if (!username.value || !password.value) {
    alert('아이디와 비밀번호를 입력해주세요.')
    return
  }

  // 로그인
  console.log('로그인 시도:', username.value, password.value)
  api.post(`/api/user/auth/login`, {
    username: username.value,
    password: password.value
  })
    .then((res) => {
      console.log(res.data)
      alert("로그인에 성공했습니다.")
      const token = res.data.accessToken
      auth.login(token)
      router.push('/')
    })
    .catch((err) => {
      alert("아이디, 비밀번호가 잘못되었습니다.")
    })
    .finally(() => {
      username.value = ''
      password.value = ''
    })
}

const goSignup = () => {
  router.push('/signup')
}

const goFindId = () => {
  router.push('/login/findId')
}

const goChangePassword = () => {
  router.push('/login/changePassword')
}
</script>

<style scoped>
.login-wrapper {
  min-height: 70vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: white;
  font-family: var(--font-main);
}

.login-box {
  width: 360px;
  padding: 30px;
  background: #eae9e973;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(71, 61, 61, 0.486);
}

.title {
  text-align: center;
  margin-bottom: 25px;
}

.input-group {
  margin-bottom: 15px;
}

.input-group label {
  display: block;
  font-size: 14px;
  margin-bottom: 6px;
}

.input-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.login-btn {
  width: 100%;
  padding: 12px;
  margin-top: 10px;
  background-color: #800000;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

.login-btn:hover {
  background-color: var(--bg-dark);
}

.extra {
  margin-top: 15px;
  text-align: center;
  font-size: 13px;
  color: #555;
}

.extra span {
  cursor: pointer;
}

.divider {
  margin: 0 6px;
}

.input-group input {
  border: none;
  outline: none;
  box-shadow: none;
}

input:focus {
  border: none;
  outline: none;
  box-shadow: none;
}
</style>
