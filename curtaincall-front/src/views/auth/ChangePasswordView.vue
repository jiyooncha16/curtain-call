<template>
  <div class="page">
    <!-- 헤더 -->
    <header class="page-header">
      <div class="header-inner">
        <p class="page-kicker">MY PAGE</p>
        <h1 class="page-title">비밀번호 찾기</h1>
        <p class="page-desc">
          가입 시 사용한 아이디와 이메일을 입력하세요.<br />
          비밀번호 재설정 링크를 이메일로 보내드립니다.
        </p>
      </div>
    </header>

    <!-- 본문 -->
    <main class="content">
      <section class="card find-card">
        <form @submit.prevent="findPassword">

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
            <label>이메일</label>
            <input
              type="email"
              v-model="email"
              placeholder="이메일을 입력하세요"
              required
            />
          </div>

          <button type="submit" class="btn primary">
            비밀번호 재설정 요청
          </button>

          <div class="extra">
            <span @click="goLogin">로그인으로 돌아가기</span>
          </div>

        </form>
      </section>
    </main>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const username = ref('')
const email = ref('')

const findPassword = async () => {
  if (!username.value || !email.value) {
    alert('아이디와 이메일을 모두 입력해주세요.')
    return
  }

  // TODO: 실제 비밀번호 재설정 API 호출
  // await api.post('/api/user/find-password', {
  //   username: username.value,
  //   email: email.value
  // })

  alert('비밀번호 재설정 안내 메일이 발송되었습니다.')
  router.push('/login')
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
</style>
