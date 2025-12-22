<template>
    <div class="actor-detail">

        <h2 class="title-text">배우 상세보기</h2>
        <hr />

        <!-- 배우 기본 정보 -->
        <div v-if="actor" class="profile-wrapper">
            <div class="img-box">
                <img :src="`/${actor.image}`" :alt="actor.name" />
            </div>

            <div class="info-box">
                <div class="name-row">
                    <h1 class="actor-name">{{ actor.name }}</h1>
                    <Heart @click="heartClicked" />
                </div>

                <div class="meta">
                    <div v-if="actor.birth">생년월일: {{ actor.birth }}</div>
                    <div v-if="actor.agency">소속사: {{ actor.agency }}</div>

                    <a v-if="actor.sns" :href="actor.sns" target="_blank" rel="noopener" class="sns">
                        <i class="bi bi-instagram"></i>
                    </a>

                    <p class="description">
                        {{ actor.description }}
                    </p>
                </div>
            </div>
        </div>

        <hr />

        <!-- 출연 작품 -->
        <section>
            <ActorWorkList :works="works" />
        </section>
        <RelatedActorList :actors="relatedActors" />
    </div>
</template>

<script setup>

import ActorWorkList from '@/components/common/actor/ActorWorkList.vue';
import RelatedActorList from '@/components/common/actor/RelatedActorList.vue';
import CardItemActor from '@/components/common/CardItemActor.vue';
import Heart from '@/components/common/icon/Heart.vue';
import { parseActorWorks } from '@/utils/workParser';
import axios from 'axios';
import { onMounted, ref, computed, watch } from 'vue';
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
const id = computed(() => route.params.id) 

//배우 정보 받아오기
const actor = ref(null)
const name = ref('')
const description = ref('')
const relatedActors = ref([])

const fetchActor = async () => {
    const { data } = await axios.get(`/api/actors/${id.value}`) // 🔴
    actor.value = data
}

const fetchRelatedActors = async () => {
    try {
        const { data } = await axios.get(`/api/actors/${id.value}/related`) // 🔴
        relatedActors.value = data
    } catch (e) {
        console.error('관련 배우 조회 실패', e)
    }
}

onMounted(() => {
    fetchActor()
    fetchRelatedActors()
})
watch(
    () => route.params.id,
    () => {
        fetchActor()
        fetchRelatedActors()
    }
)



// 배우 work 위해서
const works = computed(() => {
    if (!actor.value) return []
    return parseActorWorks(actor.value)
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
.actor-detail {
    margin-top: 20px;
}

.profile-wrapper {
    display: flex;
    align-items: flex-start;
    margin: 20px 0;
}

.img-box {
    width: 200px;
    aspect-ratio: 3 / 4;
    margin-right: 30px;
}

.img-box img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.info-box {
    flex: 1;
}

.name-row {
    display: flex;
    align-items: center;
    gap: 10px;
}

.actor-name {
    font-size: 35px;
    margin: 0;
}

.meta {
    margin-top: 20px;
}

.description {
    margin-top: 10px;
    white-space: pre-line;
}

.sns {
    display: inline-block;
    margin-top: 8px;
    font-size: 20px;
}
</style>
