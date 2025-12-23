<template>
  <div class="page">
    <!-- 상단 네비 -->
    <div class="topbar">
      <button class="icon-btn" @click="goBack">
        <i class="bi bi-arrow-left"></i>
      </button>
      <div class="topbar-title">게시글 상세</div>
    </div>

    <!-- 게시글 카드 -->
    <article class="card">
      <header class="post-header">
        <div class="category-chip">{{ board.category }}</div>
        <h1 class="title">{{ board.title }}</h1>

        <div class="meta-row">
          <div class="author">
            <div class="avatar">{{ board.nickname?.slice(0, 1) }}</div>
            <div class="author-info">
              <div class="author-name">{{ board.nickname }}</div>
              <div class="sub">{{ board.createDate }}</div>
            </div>
          </div>

          <!-- 🔥 게시글 작성자만 -->
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

      <section class="content">
        <p class="content-text">{{ board.content }}</p>
      </section>
    </article>

    <!-- 댓글 카드 -->
    <section class="card">
      <div class="section-title">
        댓글 <span class="muted">{{ replyList.length }}</span>
      </div>

      <!-- 댓글 작성 -->
      <div class="comment-write">
        <div class="avatar sm">{{ auth.nickname?.slice(0, 1) || '?' }}</div>
        <div class="write-box">
          <textarea
            v-model="newComment"
            class="textarea"
            rows="3"
            placeholder="댓글을 입력하세요."
          />
          <div class="write-actions">
            <button
              class="btn primary"
              :disabled="!newComment.trim()"
              @click="submitComment"
            >
              등록
            </button>
          </div>
        </div>
      </div>

      <!-- 댓글 리스트 -->
      <div class="comment-list">
        <div v-if="sortedComments.length === 0" class="empty">
          아직 댓글이 없어요.
        </div>

        <div
          v-for="c in sortedComments"
          :key="c.replyId"
          class="comment-item"
        >
          <div class="avatar sm">{{ c.nickname?.slice(0, 1) }}</div>

          <div class="comment-body">
            <div class="comment-top">
              <div class="comment-name">{{ c.nickname }}</div>
              <div class="comment-sub">{{ c.createDate }}</div>

              <!-- 🔥 본인 댓글만 -->
              <div class="comment-menu" v-if="String(c.userId) === String(auth.userId)">
                <button class="mini" @click="startEdit(c)">수정</button>
                <button class="mini danger" @click="deleteComment(c.replyId)">삭제</button>
              </div>
            </div>

            <!-- 일반 보기 -->
            <div v-if="editingId !== c.replyId" class="comment-text">
              {{ c.content }}
            </div>

            <!-- 수정 모드 -->
            <div v-else>
              <textarea
                v-model="editContent"
                class="textarea"
                rows="2"
              />
              <div class="comment-actions">
                <button class="mini primary" @click="updateComment(c.replyId)">
                  저장
                </button>
                <button class="mini" @click="cancelEdit">
                  취소
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import axios from 'axios'
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import api from '@/api/axios'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()

/* 🔥 라우터 param */
const boardId = Number(route.params.id)

/* ===== 상태 ===== */
const board = ref({
  boardId: '',
  title: '',
  content: '',
  category: '',
  createDate: '',
  userId: '',
  nickname: '',
})

const replyList = ref([])
const newComment = ref('')

/* 🔥 댓글 수정 상태 */
const editingId = ref(null)
const editContent = ref('')

/* ===== 데이터 로딩 ===== */
onMounted(async () => {
  const boardRes = await axios.get(`/api/boards/${boardId}`)
  const replyRes = await axios.get(`/api/reply/board/${boardId}`)

  board.value = boardRes.data
  replyList.value = replyRes.data
})

/* ===== 계산 속성 ===== */
const isOwner = computed(() => {
  return String(board.value.userId) === String(auth.userId)
})

const sortedComments = computed(() => {
  return [...replyList.value].sort(
    (a, b) => new Date(b.createDate) - new Date(a.createDate)
  )
})

/* ===== 이벤트 ===== */
const goBack = () => router.back()

const submitComment = async () => {
  if (!newComment.value.trim()) return

  await api.post('/api/reply', {
    boardId,
    content: newComment.value,
    userId: auth.userId,
  })

  newComment.value = ''
  const res = await axios.get(`/api/reply/board/${boardId}`)
  replyList.value = res.data
}

/* 🔥 댓글 수정 */
const startEdit = (c) => {
  editingId.value = c.replyId
  editContent.value = c.content
}

const cancelEdit = () => {
  editingId.value = null
  editContent.value = ''
}

const updateComment = async (replyId) => {
  if (!editContent.value.trim()) return

  await api.put(`/api/reply/${replyId}`, {
    content: editContent.value,
  })

  cancelEdit()
  const res = await axios.get(`/api/reply/board/${boardId}`)
  replyList.value = res.data
}

/* 🔥 댓글 삭제 */
const deleteComment = async (replyId) => {
  if (!confirm('댓글을 삭제하시겠습니까?')) return

  await api.delete(`/api/reply/${replyId}`)
  const res = await axios.get(`/api/reply/board/${boardId}`)
  replyList.value = res.data
}

/* ===== 게시글 수정/삭제 ===== */
const onEdit = () => {
  router.push(`/community/${boardId}/edit`)
}

const onDelete = async () => {
  if (!confirm('게시글을 삭제하시겠습니까?')) return

  await api.delete(`/api/boards/${boardId}`)
  alert('삭제되었습니다.')
  router.push('/community')
}
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
