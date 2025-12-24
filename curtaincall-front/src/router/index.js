import { createRouter, createWebHistory } from 'vue-router'
import MainView from '../views/MainView.vue'
import MusicalView from '@/views/musical/MusicalView.vue'
import ActorView from '@/views/actor/ActorView.vue'
import MusicalDetailView from '@/views/musical/MusicalDetailView.vue'
import ActorDetailView from '@/views/actor/ActorDetailView.vue'
import CommunityView from '@/views/community/CommunityView.vue'
import BoardDetailView from '@/views/community/BoardDetailView.vue'
import BoardModifyView from '@/views/community/BoardModifyView.vue'
import BoardCreateView from '@/views/community/BoardCreateView.vue'
import ReviewCreateView from '@/views/review/ReviewCreateView.vue'
import ReviewModifyView from '@/views/review/ReviewModifyView.vue'
import MyReview from '@/views/review/MyReview.vue'
import MyInfoModify from '@/views/myPage/MyInfoModify.vue'
import FindIdView from '@/views/auth/FindIdView.vue'
import LoginView from '@/views/auth/LoginView.vue'
import ChangePasswordView from '@/views/auth/ChangePasswordView.vue'
import MyPageView from '@/views/myPage/MyPageView.vue'
import SignupView from '@/views/auth/SignupView.vue'
import SubView from '@/views/SubView.vue'
import { useAuthStore } from '@/stores/auth'
import FrequentActorsView from '@/views/myPage/FrequentActorsView.vue'
import MusicalLikeView from '@/views/myPage/MusicalLikeView.vue'
import ActorLikeView from '@/views/myPage/ActorLikeView.vue'
import ErrorPage from '@/views/ErrorPage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'main',
      component: MainView,
    },
    {
      path: '/musical',
      name: 'musical',
      component: MusicalView,
    },
    {
      path: '/musical/:id',
      name: 'musicalDetail',
      component: MusicalDetailView,
      props: true,
    },
    {
      path: '/actor',
      name: 'actor',
      component: ActorView,
    },
    {
      path: '/actor/:id',
      name: 'actorDetail',
      component: ActorDetailView,
      props: true,
      
    },
    
    {
      path: '/community',
      name: 'community',
      component: CommunityView,
    },
    {
      path: '/community/:id',
      name: 'boardDetail',
      component: BoardDetailView,
      props: true,
    },
    {
      path: '/community/:id/edit',
      name: 'boardModify',
      component: BoardModifyView,
      props: true,
      meta: { requiresAuth: true },
    },
    {
      path: '/community/new',
      name: 'boardCreate',
      component: BoardCreateView,
      meta: { requiresAuth: true },
    },
    {
      path: '/review/new/:id',
      name: 'reviewCreate',
      component: ReviewCreateView,
      meta: { requiresAuth: true },
    },
    {
      path: '/review/edit/:id',
      name: 'reviewModify',
      component: ReviewModifyView,
      props: true,
      meta: { requiresAuth: true },
    },
    {
      path: '/review/my',
      name: 'myReview',
      component: MyReview,
      props: true,
      meta: { requiresAuth: true },
    },
    {
      path: '/myPage',
      name: 'myPage',
      component: MyPageView,
      props: true,
      meta: { requiresAuth: true },
    },
    {
      path: '/myPage/edit',
      name: 'myInfoModify',
      component: MyInfoModify,
      props: true,
      meta: { requiresAuth: true },
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/login/findId',
      name: 'findId',
      component: FindIdView,
    },
    {
      path: '/login/changePassword',
      name: 'changePassword',
      component: ChangePasswordView,
    },
    {
      path: '/signup',
      name: 'signup',
      component: SignupView,
    },
    {
      path: '/myPage/actors',
      name: 'frequentActors',
      component: FrequentActorsView,
      meta: { requiresAuth: true },
    },
    {
      path: '/myPage/musicalLike',
      name: 'musicalLike',
      component: MusicalLikeView,
      meta: { requiresAuth: true },
    },
    {
      path: '/myPage/actorLike',
      name: 'actorLike',
      component: ActorLikeView,
      meta: { requiresAuth: true },
    },
    {
      path: '/example',
      name: 'exaple',
      component: SubView,
    },
    // 공용 에러 페이지
    {
      path: '/error',
      name: 'Error',
      component: ErrorPage
    },

    // 404
    {
      path: '/:pathMatch(.*)*',
      redirect: { path: '/error' }
    }
  ],
  
  scrollBehavior(to, from, savedPosition) {
    // 뒤로가기면 원래 위치
    if (savedPosition) {
      return savedPosition
    }
    // 그 외에는 항상 맨 위
    return { top: 0 }
  }
})

// requires Auth 인 페이지인데 로그인 안 되었다면 로그인 페이지로 이동
router.beforeEach((to, from, next) => {
  
  const auth = useAuthStore()
  if (to.meta.requiresAuth && !auth.isLogin) {
    alert("로그인 페이지로 이동합니다.")
    next({
    name: 'login',
    query: { redirect: to.fullPath } // 로그인 후 원래 가려던 페이지로 이동하기
  })
  } else {
    next()
  }
})
export default router
