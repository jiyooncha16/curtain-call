<template>
  <div>
    <table class="table">
      <colgroup>
        <col style="width: 15%" />
        <col style="width: 35%" />
        <col style="width: 25%" />
        <col style="width: 15%" />
        <col style="width: 10%" />
      </colgroup>

      <thead>
        <tr style="text-align: center;">
          <th>번호</th>
          <th>제목</th>
          <th>작성자</th>
          <th>작성일시</th>
          <th>좋아요</th>
        </tr>
      </thead>

      <tbody>
        <tr
          v-for="item in pagedList"
          :key="item.boardId"
          style="text-align: center; cursor: pointer"
          @click="goDetail(item.boardId)"
        >
          <th>{{ item.boardId }}</th>
          <td style="text-align:start">{{ item.title }}</td>
          <td>{{ item.nickname }}</td>
          <td>{{ item.createDate }}</td>
          <td>{{ Math.floor(Math.random() * 20) }}</td>
        </tr>
      </tbody>
    </table>
    <div class="pagination">
  <button
    class="page-btn"
    :disabled="currentPage === 1"
    @click="goPage(currentPage - 1)"
  >
    ◀
  </button>

  <button
    v-for="page in totalPages"
    :key="page"
    class="page-btn"
    :class="{ active: currentPage === page }"
    @click="goPage(page)"
  >
    {{ page }}
  </button>

  <button
    class="page-btn"
    :disabled="currentPage === totalPages"
    @click="goPage(currentPage + 1)"
  >
    ▶
  </button>
</div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { onMounted, ref, computed, toRaw, watch } from "vue";

const props = defineProps({
  list: Array,
})

const router = useRouter()

/* ===== 페이징 설정 ===== */
const pageSize = 10
const currentPage = ref(1)

/* 전체 페이지 수 */
const totalPages = computed(() =>
  Math.ceil(props.list.length / pageSize)
)

/* 현재 페이지에 보여줄 데이터 */
const pagedList = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  return props.list.slice(start, end)
})

/* 페이지 이동 */
const goPage = (page) => {
  if (page < 1 || page > totalPages.value) return
  currentPage.value = page
}

/* 상세 이동 */
const goDetail = (boardId) => {
  router.push(`/community/${boardId}`)
}

/* 리스트 변경 시 1페이지로 초기화 */
watch(
  () => props.list,
  () => {
    currentPage.value = 1
  }
)
</script>
<style scoped>
  .pagination {
  display: flex;
  justify-content: center;
  gap: 6px;
  margin-top: 20px;
}

.page-btn {
  min-width: 32px;
  height: 32px;
  padding: 0 10px;
  border-radius: 8px;
  border: 1px solid #ddd;
  background: #fff;
  font-size: 13px;
  cursor: pointer;
}

.page-btn:hover {
  background: #f3f4f6;
}

.page-btn.active {
  background: #7b0000;
  color: #fff;
  border-color: #7b0000;
}

.page-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

</style>