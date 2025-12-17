<template>
  <div class="login-wrapper">
    <div class="login-box">
      <h2 class="title">비밀번호 찾기</h2>

      <p class="desc">
        가입 시 사용한 아이디와 이메일을 입력해주세요.<br>
        이메일로 비밀번호 재설정 링크를 보내드립니다.
      </p>

      <div class="input-group">
        <label>아이디</label>
        <input type="text" v-model="username" placeholder="아이디를 입력하세요" />
      </div>

      <div class="input-group">
        <label>이메일</label>
        <input type="email" v-model="email" placeholder="이메일을 입력하세요" />
      </div>

      <button class="login-btn" @click="findPassword">
        비밀번호 재설정 요청
      </button>

      <div class="extra">
        <span @click="goLogin">로그인으로 돌아가기</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const username = ref('')
const email = ref('')

const findPassword = () => {
  if (!username.value || !email.value) {
    alert('아이디와 이메일을 모두 입력해주세요.')
    return
  }

  // TODO: 비밀번호 찾기 API 호출
  console.log('비밀번호 찾기 요청:', username.value, email.value)

  alert('비밀번호 재설정 안내 메일이 발송되었습니다.')
  router.push('/login')
}

const goLogin = () => {
  router.push('/login')
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
  background: #f1f0f073;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(71, 61, 61, 0.486);
}

.title {
  text-align: center;
  margin-bottom: 10px;
}

.desc {
  text-align: center;
  font-size: 13px;
  color: #666;
  margin-bottom: 20px;
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
  border: none;
  background: white;
  border-radius: 4px;
  outline: none;
  box-shadow: none;
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
</style>
