<template>
  <div class="find-wrapper">
    <div class="find-box">
      <h2 class="title">아이디 찾기</h2>

      <p class="desc">
        가입 시 등록한 이메일과 이름을 입력하세요.
      </p>

      <div class="input-group">
        <label>이름</label>
        <input
          type="text"
          v-model="name"
          placeholder="이름을 입력하세요"
        />
      </div>

      <div class="input-group">
        <label>이메일</label>
        <input
          type="email"
          v-model="email"
          placeholder="이메일을 입력하세요"
        />
      </div>

      <button class="find-btn" @click="findId">
        아이디 찾기
      </button>

      <div class="extra">
        <span @click="goLogin">로그인</span>
        <span class="divider">|</span>
        <span @click="goLogin">비밀번호 재설정</span>
      </div>
    </div>
  </div>
</template>

<script setup>
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

  // TODO: axios API 호출
  // const res = await axios.post('/api/find-id', {
  //   name: name.value,
  //   email: email.value
  // })

  // 임시 응답 예시
  const foundId = 'jiyoon1234'

  // 아이디 마스킹 (보안)
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
</script>


<style scoped>
.find-wrapper {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: white;
  font-family: var(--font-main);
}

.find-box {
  width: 360px;
  padding: 30px;
  background: #eae9e973;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(71, 61, 61, 0.486);
}

.title {
  text-align: center;
  margin-bottom: 15px;
}

.desc {
  text-align: center;
  font-size: 13px;
  color: #666;
  margin-bottom: 20px;
  line-height: 1.5;
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

.find-btn {
  width: 100%;
  padding: 12px;
  margin-top: 10px;
  background-color: var(--bg-accent);
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

.find-btn:hover {
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
</style>
