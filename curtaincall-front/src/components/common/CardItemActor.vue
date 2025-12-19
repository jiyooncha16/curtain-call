<template>
    <div class="poster">
        <div class="card">
            <div class="img-box">
                <img src="@/assets/데스노트.jpg">
                 <!-- <img :src="musical.이미지........"> -->
            </div>
            <!-- <div class="title basic-text">{{item.title}}</div> -->
             <div class="title basic-text">아아아</div>
        </div>
    </div>
</template>

<script setup>
// const props = defineProps({
//     item: Object
// })
</script>

<style scoped>
.img-box {
    width: 100%;
    aspect-ratio: 3/4; /*비율*/
    /* border-radius: 12px; */
}
img {
    width: 100%;
    height: 100%;
    object-fit:cover;
}
.poster {
    display: flex;
    flex-direction: column;
    align-items: center;
}

 .card {
  background: #fff;
  border-radius: 12px;
  border-color: white;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  text-align: center;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0,0,0,0.12);
}
/* 
.title {
  padding: 5px;
  font-size: 14px;
  font-weight: 600;
  text-align: center;
} */


</style>