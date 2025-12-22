import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { parseJwt } from '@/utils/jwt'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('accessToken'))

  // 유저 정보
  const userInfo = computed(() =>
    token.value ? parseJwt(token.value) : null
  )

  // 로그인 상태, 아이디
  const isLogin = computed(() => !!userInfo.value)
  const userId = computed(() => userInfo.value?.userId)

  //로그인
  const login = (newToken) => {
    localStorage.setItem('accessToken', newToken)
    token.value = newToken
  }

  //로그아웃
  const logout = () => {
    localStorage.removeItem('accessToken')
    token.value = null
  }

  return { token, userInfo, isLogin, userId, login, logout }
}, {
  persist: true
})
