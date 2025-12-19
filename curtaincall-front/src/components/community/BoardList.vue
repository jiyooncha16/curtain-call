<template>
    <div>
        <table class="table">
            <colgroup>
                <col style="width: 10%" />   <!-- 번호 -->
                <col style="width: 40%" />  <!-- 제목 -->
                <col style="width: 15%" />  <!-- 작성자 -->
                <col style="width: 25%" />  <!-- 작성일 -->
                <col style="width: 10%" />  <!-- 좋아요 -->
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
                <tr style="text-align: center;" v-for="item in list">
                    <th scope="row">{{item.boardId}}</th>
                    <td style="text-align:start">{{item.title}}</td>
                    <td>{{item.nickname}}</td> <!--작성자 닉네임 받아와야합니다-->
                    <td>{{item.createDate}}</td> <!--time이 null로 들어옴 왜인지 보기-->
                    <td>30</td> <!--추가하기-->
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script setup>
const props = defineProps({
    list : Array
})
console.log(props.list.value)
</script>

<style scoped>

</style>