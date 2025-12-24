<template>
  <div class="page">
    <!-- 헤더 -->
    <header class="page-header">
      <div class="header-inner">
        <p class="page-kicker">MY PAGE</p>
        <h1 class="page-title">아이디 찾기</h1>
        <p class="page-desc">
          가입 시 등록한 이름과 이메일을 입력하세요.
        </p>
      </div>
    </header>

    <!-- 본문 -->
    <main class="content">
      <section class="card find-card">
        <form @submit.prevent="findId">

          <div class="form-group">
            <label>이름</label>
            <input
              type="text"
              v-model="name"
              placeholder="이름을 입력하세요"
              required
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
            아이디 찾기
          </button>

          <div class="extra">
            <span @click="goLogin">로그인</span>
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

const router = useRouter()

const name = ref('')
const email = ref('')

const findId = async () => {
  if (!name.value || !email.value) {
    alert('이름과 이메일을 모두 입력해주세요.')
    return
  }

  const res = await api.post('/api/user/auth/findId', {
    name: name.value,
    email: email.value
  })

  // 임시 응답 예시
  const foundId = res.data
  const maskedId = maskId(foundId)

  alert(`회원님의 아이디는 ${maskedId} 입니다.`)
  router.push('/login')
}

const maskId = (id) => {
  if (id.length <= 3) return id
  return id.slice(0, 2) + '*'.repeat(id.length - 4) + id.slice(-2)
}

const goLogin = () => {
  router.push('/login')
}

const goChangePassword = () => {
  router.push('/login/changePassword')
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

.page-desc {
  margin-top: 6px;
  font-size: 13px;
  opacity: 0.7;
  line-height: 1.5;
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

.find-card {
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
