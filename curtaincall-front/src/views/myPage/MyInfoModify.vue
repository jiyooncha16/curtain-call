<template>
  <div class="page">
    <!-- 헤더 -->
    <header class="page-header">
      <div>
        <p class="page-kicker">MY PAGE</p>
        <h1 class="page-title">내 정보 수정하기</h1>
      </div>
    </header>

    <!-- 본문 -->
    <main class="content">
      <section class="card form-card">
        <form @submit.prevent="submit">

          <!-- 아이디 -->
          <div class="form-group">
            <label>아이디</label>
            <input type="text" v-model="form.username" disabled />
          </div>

          <!-- 이름 -->
          <div class="form-group">
            <label>이름</label>
            <input type="text" v-model="form.name" required />
          </div>

          <!-- 닉네임 -->
          <div class="form-group">
            <label>닉네임</label>
            <input type="text" v-model="form.nickname" required />
          </div>

          <!-- 이메일 -->
          <div class="form-group">
            <label>이메일</label>
            <input type="email" v-model="form.email" required />
          </div>

          <!-- 비밀번호 -->
          <div class="form-group">
            <label>새 비밀번호</label>
            <input
              type="password"
              v-model="form.password"
              placeholder="변경 시에만 입력"
            />
          </div>

          <!-- 비밀번호 확인 -->
          <div class="form-group">
            <label>비밀번호 확인</label>
            <input
              type="password"
              v-model="form.passwordConfirm"
              placeholder="비밀번호 재입력"
            />
          </div>

          <!-- 버튼 -->
          <div class="btn-group">
            <button type="submit" class="btn primary">저장하기</button>
            <button type="button" class="btn" @click="cancel">취소</button>
          </div>

        </form>
      </section>
    </main>
  </div>
</template>

<script setup>
import api from '@/api/axios'
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = ref({
  username: '',
  name: '',
  nickname: '',
  email: '',
  password: '',
  passwordConfirm: ''
})

onMounted(async () => {
    console.log("실행됨")
  const res = await api.get('/api/user/me')
  const u = res.data.user

  form.value.username = u.username
  form.value.name = u.name
  form.value.nickname = u.nickname
  form.value.email = u.email
})

async function submit() {
  if (form.value.password) {
    if (form.value.password !== form.value.passwordConfirm) {
      alert('비밀번호가 일치하지 않습니다.')
      return
    }
  }

  const payload = {
    name: form.value.name,
    nickname: form.value.nickname,
    email: form.value.email,
    ...(form.value.password && { password: form.value.password })
  }

  await api.put('/api/user/me', payload)
  alert('회원 정보가 수정되었습니다.')
  router.back()
}

function cancel() {
  router.back()
}
</script>


<style scoped>
.page {
  padding: 18px 18px 60px;
  font-family: 'IBM Plex Sans KR', sans-serif;
}

.page-header {
  margin-bottom: 20px;
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

.page-sub {
  opacity: 0.75;
}

.content {
  display: flex;
  justify-content: center;
}

.form-card {
  width: 100%;
  max-width: 520px;
  padding: 24px;
}

.card {
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 10px 28px rgba(0,0,0,0.12);
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

input:disabled {
  background: #f3f3f3;
  color: #999;
}

.btn-group {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.btn {
  flex: 1;
  border-radius: 12px;
  padding: 12px;
  border: none;
  cursor: pointer;
  font-weight: 700;
}

.btn.primary {
  background: #111;
  color: #fff;
}

.btn.primary:hover {
  opacity: 0.9;
}
</style>
