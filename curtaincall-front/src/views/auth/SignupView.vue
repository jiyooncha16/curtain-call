<template>
  <div class="signup-wrapper">
    <div class="signup-box">
      <h2 class="title">회원가입</h2>

      <div class="input-group">
        <label>아이디</label>
        <input type="text"v-model="username" placeholder="아이디를 입력하세요"/>
      </div>

      <div class="input-group">
        <label>비밀번호</label>
        <input type="password" v-model="password" placeholder="비밀번호를 입력하세요"
        />
      </div>

      <div class="input-group">
        <label>비밀번호 확인</label>
        <input type="password" v-model="passwordCheck" placeholder="비밀번호를 다시 입력하세요" />
      </div>

      <div class="input-group">
        <label>이메일</label>
        <input type="email" v-model="email" placeholder="이메일을 입력하세요" />
      </div>

      <button class="signup-btn" @click="signup">
        회원가입
      </button>

      <div class="extra">
        <span @click="goLogin">이미 계정이 있으신가요? 로그인</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const username = ref('')
const password = ref('')
const passwordCheck = ref('')
const email = ref('')

const signup = () => {
  if (!username.value || !password.value || !passwordCheck.value || !email.value) {
    alert('모든 항목을 입력해주세요.')
    return
  }

  if (password.value !== passwordCheck.value) {
    alert('비밀번호가 일치하지 않습니다.')
    return
  }

  // TODO: axios 회원가입 API 호출
  console.log('회원가입:', {
    username: username.value,
    password: password.value,
    email: email.value
  })

  alert('회원가입이 완료되었습니다. 로그인해주세요.')
  router.push('/login')
}

const goLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.signup-wrapper {
  min-height: 80vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: white;
  font-family: var(--font-main);
}

.signup-box {
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
  margin-bottom: 14px;
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

.signup-btn {
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

.signup-btn:hover {
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
