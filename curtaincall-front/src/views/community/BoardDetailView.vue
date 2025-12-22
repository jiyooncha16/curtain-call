<template>
  <div class="page">
    <!-- 상단 네비 -->
    <div class="topbar">
      <button class="icon-btn" @click="goBack" title="뒤로">
        <i class="bi bi-arrow-left"></i>
      </button>
      <div class="topbar-title">게시글 상세</div>
    </div>

    <!-- 본문 카드 -->
    <article class="card">
      <!-- 헤더 -->
      <header class="post-header">
        <div class="category-chip">{{ board.category }}</div>
        <h1 class="title">{{ board.title }}</h1>

        <div class="meta-row">
          <div class="author">
            <div class="avatar">{{ board.nickname.slice(0, 1) }}</div>
            <div class="author-info">
              <div class="author-name">{{ board.nickname }}</div>
              <div class="sub">
                <span>{{ board.createDate }}</span>
              </div>
            </div>
          </div>

          <!-- 작성자일 때만 --> <!-----------수정하기------------>
          <!-- <div class="owner-actions" v-if="isOwner"> -->
            <div class="owner-actions" v-if="isOwner">
            <button class="btn ghost" @click="onEdit">
              <i class="bi bi-pencil-square"></i>
            </button>
            <button class="btn danger" @click="onDelete">
              <i class="bi bi-trash3"></i>
            </button>
          </div>
        </div>
      </header>

      <!-- 본문 -->
      <section class="content">
        <!-- 줄바꿈 유지하려면 pre-line -->
        <p class="content-text">{{ board.content }}</p>
      </section>

      <!-- 액션 바 -->
      <footer class="actions">
        <button class="btn like" :class="{ active: liked }" @click="toggleLike">
          <i class="bi" :class="liked ? 'bi-heart-fill' : 'bi-heart'"></i>
          <span class="count">{{ likeCount }}</span>
        </button>

        <button class="btn ghost" @click="focusComment">
          <i class="bi bi-chat-dots"></i><span class="count">{{ replyList.length }}</span>
        </button>
      </footer>
    </article>

    <!-- 댓글 카드 -->
    <section class="card">
      <div class="comment-header">
        <div class="section-title">
          댓글 <span class="muted">{{ replyList.length }}</span>
        </div>
      </div>

      <!-- 댓글 입력 -->
      <div class="comment-write">
        <div class="avatar sm">{{ board.nickname.slice(0, 1) }}</div>
        <div class="write-box">
          <textarea
            ref="commentInput"
            v-model="newComment"
            class="textarea"
            rows="3"
            placeholder="댓글을 입력하세요."
          />
          <div class="write-actions">
            <div class="helper">{{ newComment.length }}/500</div>
            <button class="btn primary" :disabled="!newComment.trim()" @click="submitComment">
              등록
            </button>
          </div>
        </div>
      </div>

      <!-- 댓글 리스트 -->
      <div class="comment-list">
        <div v-if="sortedComments.length === 0" class="empty">
          아직 댓글이 없어요. 첫 댓글을 남겨보세요!
        </div>

        <div v-for="c in sortedComments" :key="c.id" class="comment-item">
          <div class="avatar sm">{{ c.nickname.slice(0, 1) }}</div>

          <div class="comment-body">
            <div class="comment-top">
              <div class="comment-name">{{ c.nickname }}</div>
              <div class="comment-sub">
                <span>{{ c.createDate }}</span>
              </div>
            </div>

            <div class="comment-text">{{ c.content }}</div>
          </div>
        </div>
      </div>
    </section>

  </div>
</template>

<script setup>
  import axios from 'axios'
  import { computed, onMounted, ref } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import { useAuthStore } from '@/stores/auth'
import api from '@/api/axios'

const router = useRouter()
const route = useRoute()
const id = route.params.id

/** ===== 데모 데이터(연동 시 API 데이터로 교체) ===== */
// const board = ref({
//   id: 10,
//   category: '자유',
//   title: '오늘 공연 보고 왔는데 너무 좋았어요',
//   userId: 1,
//   nickname: "지윤",
//   createDate: '2025-12-19 10:20:00',
// //   views: 128,
//   content:
//     '1막부터 몰입감 장난 아니고...\n넘버가 진짜 미쳤습니다.\n추천해요!!!'
// })
// const reply = ref([
//   { id: 1, userId: 1, nickname: '민지', createDate: '2025-12-19 12:01:00', content: '헉 나도 보러가야겠다!'},
//   { id: 2, userId: 2, nickname: '지윤', createDate: '2025-12-19 12:10:00', content: '진짜 추천이야 ㅠㅠ'},
// ])

// axios!!!!!!!!!!!!!
const board = ref({
    boardId : '',
    title: '',
    content : '',
    category: '',
    createDate:'',
    userId: '',
    nickname:''
})
const replyList = ref([])
onMounted(async () => {
    const boardRes = await axios.get(`/api/boards/${id}`)
    const replyRes = await axios.get(`/api/reply/board/${id}`)

    board.value = boardRes.data
    replyList.value = replyRes.data

    console.log(board.value)
    console.log(replyList.value)
})

/** ===== 상태 ===== */
const liked = ref(false)
const likeCount = ref(23)
const newComment = ref('')
const commentInput = ref(null)
const auth = useAuthStore()


const isOwner = computed(() => {
  return board.value.userId === auth.userId
})



const sortedComments = computed(() => {
  const list = [...replyList.value]
  return list.sort((a, b) => new Date(b.createDate) - new Date(a.createDate))
})


/** ===== 이벤트 ===== */
const goBack = () => router.back()

// 좋아요 토글
const toggleLike = () => {
  liked.value = !liked.value
  likeCount.value += liked.value ? 1 : -1
}

//댓글 등록하기
const replyUserId = 1; /////////////////////// 수정하기
const submitComment = async () => {
  const text = newComment.value.trim()
  if (!text) return
  await axios.post(`/api/reply`, {
        content : text,
        boardId : board.value.boardId,
        userId : replyUserId
  })
  newComment.value = ''
  //갱신
  const replyRes = await axios.get(`/api/reply/board/${id}`)
  replyList.value = replyRes.data
}

const onEdit = () => {
    router.push(`/community/${board.value.boardId}/edit`)
}

const onDelete = async () => {
  // 1️⃣ 작성자만 삭제 가능 (프론트 1차 방어)
  if (board.value.userId !== auth.userId) {
    alert('삭제 권한이 없습니다.')
    return
  }

  // 2️⃣ 사용자 확인
  const ok = confirm('정말 이 게시글을 삭제할까요?')
  if (!ok) return

  try {
    // 3️⃣ DELETE 요청
    await api.delete(`/api/boards/${board.value.boardId}`, {
      headers: {
        Authorization: `Bearer ${auth.token}`
      }
    })

    alert('게시글이 삭제되었습니다.')

    // 4️⃣ 목록 페이지로 이동
    router.push('/community')

  } catch (e) {
    console.error(e)
    alert('게시글 삭제 실패')
  }
}

const onReport = () => alert('신고 모달/API 연결')
const onCommentEdit = (c) => alert(`댓글 수정: ${c.id}`)
const onCommentDelete = (c) => alert(`댓글 삭제: ${c.id}`)

/* ===== 유틸 ===== */
// const formatDate = (iso) => {
//   const d = new Date(iso)
//   const yyyy = d.getFullYear()
//   const mm = String(d.getMonth() + 1).padStart(2, '0')
//   const dd = String(d.getDate()).padStart(2, '0')
//   const hh = String(d.getHours()).padStart(2, '0')
//   const mi = String(d.getMinutes()).padStart(2, '0')
//   return `${yyyy}.${mm}.${dd} ${hh}:${mi}`
// }

</script>

<style scoped>
/* ===== base ===== */
.page {
  max-width: 980px;
  margin: 0 auto;
  padding: 18px 14px 40px;
  /* background: #f6f7fb; */
  min-height: 100vh;
}

.card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 26px rgba(0, 0, 0, 0.08);
  padding: 18px;
  margin-bottom: 14px;
}

.muted { color: #8a8f99; }

/* ===== topbar ===== */
.topbar {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 4px 14px;
}
.topbar-title {
  flex: 1;
  font-size: 18px;
  font-weight: 800;
}
.icon-btn {
  width: 38px;
  height: 38px;
  border-radius: 12px;
  border: 1px solid #e6e8ee;
  background: #fff;
  cursor: pointer;
  display: grid;
  place-items: center;
}
.icon-btn:hover { background: #f3f5fa; }

/* ===== post header ===== */
.post-header { margin-bottom: 14px; }
.category-chip {
  display: inline-flex;
  padding: 6px 10px;
  border-radius: 999px;
  background: #ffd6d6;
  color: #ff5454;
  font-weight: 700;
  font-size: 12px;
  margin-bottom: 8px;
}
.title {
  margin: 0 0 12px;
  font-size: 22px;
  line-height: 1.25;
  font-weight: 900;
  letter-spacing: -0.2px;
}

.meta-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  flex-wrap: wrap;
}

.author {
  display: flex;
  align-items: center;
  gap: 10px;
}

.avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: #ffd6d6;
  color: #ff5454;
  display: grid;
  place-items: center;
  font-weight: 900;
}
.avatar.sm {
  width: 34px;
  height: 34px;
  font-size: 13px;
}

.author-info .author-name { font-weight: 800; }
.author-info .sub {
  font-size: 12px;
  color: #8a8f99;
  margin-top: 2px;
}
.dot { margin: 0 6px; }

.owner-actions { display: flex; gap: 8px; }

/* ===== gallery ===== */
.gallery { margin: 14px 0; }
.gallery-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
}
.img-tile {
  border: none;
  padding: 0;
  border-radius: 14px;
  overflow: hidden;
  cursor: pointer;
  aspect-ratio: 4 / 3;
  background: #f0f1f5;
}
.img-tile img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}
.img-tile:hover { transform: translateY(-1px); }

/* ===== content ===== */
.content-text {
  white-space: pre-line;
  font-size: 15px;
  line-height: 1.75;
  color: #1f2328;
  margin: 0;
}
.section-title {
  font-weight: 900;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  gap: 8px;
}
/* ===== buttons ===== */
.btn {
  border: 1px solid #e6e8ee;
  background: #fff;
  border-radius: 12px;
  padding: 5px 10px;
  /* font-weight: 800; */
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}
.btn:hover { background: #f3f5fa; }
.btn.primary {
  border-color: transparent;
  background: #ffd6d6;;
  color: #ff5454;
  font-size: 13px;
  font-weight: bold;
}
.btn.primary:disabled { opacity: 0.5; cursor: not-allowed; }
.btn.ghost { background: #fff; }
.btn.danger {
  border-color: #ffd6d6;
  color: #d12b2b;
}
.btn.like.active {
  border-color: #ffd6e0;
  background: #fff2f6;
  color: #d91b5c;
}
.count {
  /* font-weight: 900; */
  /* padding: 2px 8px; */
  border-radius: 999px;
  /* background: #f1f3f8; */
}

/* ===== actions footer ===== */
.actions {
  display: flex;
  gap: 10px;
  margin-top: 18px;
  flex-wrap: wrap;
}

/* ===== comments ===== */
.comment-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 14px;
}
.select {
  border: 1px solid #e6e8ee;
  border-radius: 12px;
  padding: 10px 12px;
  font-weight: 800;
  background: #fff;
}

.comment-write {
  display: flex;
  gap: 10px;
  align-items: flex-start;
  margin-bottom: 14px;
}
.write-box { flex: 1; }
.textarea {
  width: 100%;
  resize: none;
  border: 1px solid #e6e8ee;
  border-radius: 14px;
  padding: 12px;
  outline: none;
  font-size: 14px;
  line-height: 1.6;
}
.textarea:focus { border-color: #2f5cff; }

.write-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 8px;
}
.helper { font-size: 12px; color: #8a8f99; }

.comment-list { display: grid; gap: 12px; }
.comment-item {
  display: flex;
  gap: 10px;
  align-items: flex-start;
}
.comment-body {
  flex: 1;
  border: 1px solid #eef0f5;
  border-radius: 14px;
  padding: 12px;
}
.comment-top {
  display: flex;
  align-items: center;
  gap: 10px;
}
.comment-name { font-weight: 900; }
.comment-sub { color: #8a8f99; font-size: 12px; margin-left: auto; }
.comment-menu { display: flex; gap: 6px; margin-left: 6px; }

.comment-text {
  margin-top: 8px;
  white-space: pre-line;
  line-height: 1.6;
}

.comment-actions {
  display: flex;
  gap: 8px;
  margin-top: 10px;
}
.mini {
  border: 1px solid #e6e8ee;
  background: #fff;
  border-radius: 10px;
  padding: 6px 10px;
  /* font-weight: 800; */
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
}
.mini:hover { background: #f3f5fa; }
.mini.danger { border-color: #ffd6d6; color: #d12b2b; }

.empty {
  text-align: center;
  padding: 24px 0;
  color: #8a8f99;
  font-weight: 700;
}

</style>
