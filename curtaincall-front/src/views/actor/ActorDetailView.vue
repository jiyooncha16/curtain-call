<template>
    <div>
        <h1>배우 (번호 : {{ id }})</h1>
        <hr>
        <div class="container flex" style="align-items:flex-start;margin:20px 0;">
            <div class="img-box">
                <img src="@/assets/홍광호.jpg">
            </div>
            <div>
                <div class="center">
                    <div class="title-text" style="margin:0;font-size: 35px;">{{name}}</div>
                    <Heart @click="heartClicked"/>
                </div>
                <div style="margin: 20px 0">
                    <div class="main-text">약력</div>
                    <div class="basic-text text"> {{description}} </div>
                </div> 
            </div>
        </div>
        <hr>
        <div>
            <div class="title-text">출연 작품</div>
            <div class="flex musical-box">
                <CardItemActor v-for="i in 10"/>
                <!-- <CardItemActor v-for="musical in musicalList" :musical="musical"/> -->
            </div>
        </div>
    </div>
</template>

<script setup>
import CardItemActor from '@/components/common/CardItemActor.vue';
import Heart from '@/components/common/icon/Heart.vue';
import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
const profile = 
    `2002 뮤지컬 <명성황후>
    2006 뮤지컬 <미스 사이공>
    2009 뮤지컬 <오페라의 유령>
    2010 뮤지컬 <지킬 앤 하이드>
    2013 뮤지컬 <노트르담 드 파리>
    2015 뮤지컬 <데스노트>
    2019 뮤지컬 <스위니토드>
    2022 뮤지컬 <물랑루즈!>
    2023 뮤지컬 <데스노트>
    `;

const route = useRoute()
const id = route.params.id

//배우 정보 받아오기
const name = ref('')
const description = ref('')

onMounted(() => {
    axios.get(`/api/actors/${id}`)
    .then((res) => {
        console.log(res.data)
        name.value = res.data.name
        description.value = res.data.description
        
        console.log(name.value)
        console.log(description.value)
    })
})

// 하트 처리
const isHearted = ref(false)
const itemId = id
const userId = 1 /////////// 나중에 처리

const heartClicked = async () => {
  try {
    if (!isHearted.value) { // 하트 안 눌려있었다면 좋아요 post
      await axios.post(`/api/actors/like`, {
        actorId: id, 
        userId: userId
      })
      isHearted.value = true
    } else {
      // 좋아요 취소
      await axios.delete(`/api/actors/like/${itemId}?userId=${userId}`)
      isHearted.value = false
    }
  } catch (e) {
    console.error('하트 처리 실패', e)
  }
}

</script>

<style scoped>
.img-box {
    width: 30%;
    aspect-ratio: 3/4; /*비율*/
    margin-right: 30px;
}
img {
    width: 100%;
    height: 100%;
    object-fit:cover;
}
.center {
    gap : 10px;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
}
.text {
    white-space: pre-line;
}
.musical-box {
    gap:20px;
    margin:5px;
    display: grid;
    grid-template-columns: repeat(6, 1fr);
}
</style>