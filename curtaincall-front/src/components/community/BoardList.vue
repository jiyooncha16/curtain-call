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
                    <th scope="col">번호</th>
                    <th scope="col">제목</th>
                    <th scope="col">작성자</th>
                    <th scope="col">작성일시</th>
                    <th scope="col">좋아요</th>
                </tr>
            </thead>

            <tbody>
                <tr
                    v-for="item in list"
                    :key="item.boardId"
                    style="text-align: center; cursor: pointer;"
                    @click="goDetail(item.boardId)"
                >
                    <th scope="row">{{ item.boardId }}</th>
                    <td style="text-align:start">{{ item.title }}</td>
                    <td>{{ item.nickname }}</td>
                    <td>{{ item.createDate }}</td>
                    <td>30</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
    list: {
        type: Array,
        required: true
    }
})

const router = useRouter()

const goDetail = (boardId) => {
    router.push({
        name: 'boardDetail',
        params: { id: boardId }   // ✅ 중요: id로 보내기
    })
}
</script>

<style scoped>
tbody tr:hover {
    background-color: #f8f9fa;
}
</style>
