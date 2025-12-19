<template>
  <div>
    <table class="table">
      <colgroup>
        <col style="width: 10%" />
        <col style="width: 40%" />
        <col style="width: 15%" />
        <col style="width: 25%" />
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
          v-for="item in list"
          :key="item.boardId"
          style="text-align: center; cursor: pointer"
          @click="goDetail(item.boardId)"
        >
          <th>{{ item.boardId }}</th>
          <td style="text-align:start">{{ item.title }}</td>
          <td>{{ item.nickname }}</td>
          <td>{{ item.createDate }}</td>
          <td>{{ item.likeCount ?? 0 }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

defineProps({
  list: Array,
})

const router = useRouter()

const goDetail = (boardId) => {
  router.push(`/community/${boardId}`)
}
</script>
