<script setup>
import { RouterLink, RouterView } from 'vue-router'
import Header from './components/layout/Header.vue';
import Footer from './components/layout/Footer.vue';
</script>

<template>
  <div class="layout">
    <Header />
    <main>
      <RouterView></RouterView>
    </main>
    <Footer />
  </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=IBM+Plex+Sans+KR&display=swap');
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
main {
  margin : 20px auto;
  width : 50%;
  flex: 1;
  font-family: "IBM Plex Sans KR", sans-serif;
  font-size: 16px;
}
</style>
