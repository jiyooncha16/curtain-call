import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/api/axios'

const DEFAULT_IMAGES = [
  '/profile/default.jpg',
  '/profile/profile1.jpg',
  '/profile/profile2.jpg',
  '/profile/profile3.jpg',
  '/profile/profile4.jpg'
]

function getRandomDefault(userId) {
  if (!userId) return DEFAULT_IMAGES[0]
  return DEFAULT_IMAGES[userId % DEFAULT_IMAGES.length]
}

export const useUserProfileStore = defineStore('userProfile', () => {
  const rawUser = ref(null)

  // 🔥 유저 정보 로딩 (딱 한 번)
  const fetchMe = async () => {
    const res = await api.get('/api/user/me')
    rawUser.value = res.data
  }

  // 🔥 공통 프로필 이미지
  const profileImage = computed(() => {
    const img = rawUser.value?.user?.profileImage
    if (img) return img

    const userId = rawUser.value?.user?.userId
    return getRandomDefault(userId)
  })

  return {
    rawUser,
    profileImage,
    fetchMe
  }
})
