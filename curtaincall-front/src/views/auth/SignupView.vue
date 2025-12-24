<template>
  <div class="page">
    <!-- 헤더 -->
    <header class="page-header">
      <div class="header-inner">
        <p class="page-kicker">MY PAGE</p>
        <h1 class="page-title">회원가입</h1>
      </div>
    </header>

    <!-- 본문 -->
    <main class="content">
      <section class="card signup-card">
        <form @submit.prevent="signupClicked">

          <div class="form-group">
            <label>아이디</label>
            <input
              type="text"
              v-model="username"
              placeholder="아이디를 입력하세요"
              required
            />
          </div>

          <div class="form-group">
            <label>비밀번호</label>
            <input
              type="password"
              v-model="password"
              placeholder="비밀번호를 입력하세요"
              required
            />
          </div>

          <div class="form-group">
            <label>비밀번호 확인</label>
            <input
              type="password"
              v-model="passwordCheck"
              placeholder="비밀번호를 다시 입력하세요"
              required
            />
          </div>

          <div class="form-group">
            <label>이름</label>
            <input
              type="text"
              v-model="name"
              placeholder="이름을 입력하세요"
            />
          </div>

          <div class="form-group">
            <label>닉네임</label>
            <input
              type="text"
              v-model="nickname"
              placeholder="닉네임을 입력하세요"
            />
          </div>

          <div class="form-group">
            <label>이메일</label>
            <input
              type="email"
              v-model="email"
              placeholder="이메일을 입력하세요"
              required
            />
          </div>

          <button type="submit" class="btn primary">
            회원가입
          </button>

          <div class="extra">
            <span @click="goLogin">이미 계정이 있으신가요? 로그인</span>
          </div>

        </form>
      </section>
    </main>
  </div>
</template>
<script setup>
import axios from 'axios'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const username = ref('')
const password = ref('')
const passwordCheck = ref('')
const name = ref('')
const email = ref('')
const nickname = ref('')

const signupClicked = async () => {
  if (!username.value || !password.value || !passwordCheck.value || !email.value) {
    alert('필수 항목을 모두 입력해주세요.')
    return
  }

  if (password.value !== passwordCheck.value) {
    alert('비밀번호가 일치하지 않습니다.')
    return
  }

  try {
    await axios.post('/api/user', {
      username: username.value,
      password: password.value,
      name: name.value,
      email: email.value,
      nickname: nickname.value
    })

    alert('회원가입이 완료되었습니다. 로그인해주세요.')
    router.push('/login')
  } catch (err) {
    console.error(err)
    alert('회원가입 중 오류가 발생했습니다.')
  }
}

const goLogin = () => {
  router.push('/login')
}
</script>
<style scoped>
.page {
  padding: 18px 18px 60px;
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

.signup-card {
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
</style>
