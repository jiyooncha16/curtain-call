<template>
    <header>
    <div class="board-row">
        <router-link to="/"><img src="../../assets/curtaincall.png" width="100px"></router-link>
        <router-link :to="{name : 'musical'}" class="headerText">뮤지컬</router-link>
        <router-link :to="{name : 'actor'}" class="headerText">배우</router-link>
        <router-link :to="{name : 'community'}" class="headerText">커뮤니티</router-link>
    </div>
    <div class="board-row">
        <search-bar/>
        <router-link :to="{name : 'myPage', params : {id : 1}}" class="headerText"> <!--id 받아오면 변경해넣기-->
            <i class="bi bi-person-fill"></i>
        </router-link>
    </div>
</header>

</template>

<script setup>
import SearchBar from '../common/icon/SearchBar.vue';


</script>

<style scoped>
    
/* 헤더, 푸터 */
header {
    width:100%;
    height: 60px;
    padding : 0;
    margin : 0;
    display:flex;
    align-items: center;
    background-color: #800000;
    justify-content: space-between;
}
.headerText {
    font-family: "IBM Plex Sans KR", sans-serif;
    font-size: 18px;
    font-weight: bold;
    color: white;
    margin: 15px;
    align-items: center;
    justify-content: center;
}
#searchBar {
    width:250px;
    height:30px;
    border: white 2px solid;
    display: flex;
    align-items: center;
    justify-content: end;
}
.board-row {
    display:flex;
    flex-direction: row;
    align-items: center;
}

a {
    text-decoration: none;
}
.bi-search {
    color:white; 
    margin-right: 10px;
}
.bi-person-fill {
    color:white; 
    margin: 0 20px; 
    font-size: 25px;
}
</style>