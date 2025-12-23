// 요청에 토큰 넣어주기

import { useAuthStore } from '@/stores/auth'
import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true,
})

// 이 파일 사용 시 :
//import api from '@/api/axios'
// api.get('/api/user/me') 

api.interceptors.request.use((config) => {
  const auth = useAuthStore()
console.log('token in interceptor:', auth.token)
  if (auth.token) {
    // ⭐ 핵심: .value 여부
    const token =
      typeof auth.token === 'string'
        ? auth.token
        : auth.token.value

    config.headers.Authorization = `Bearer ${token}`
  }

  return config
})

export default api
