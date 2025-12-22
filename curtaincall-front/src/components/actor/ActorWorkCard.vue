<template>
    <div class="work-card" :class="{ current: work.isCurrent }">
      <h4 class="title">{{ work.title }}</h4>
      
      <div class="period">{{ work.period }}</div>
      <div class="theater">{{ work.theater }}</div>
      <div v-if="work.role" class="role">배역: {{ work.role }}</div>
      
      <!-- 현재 공연 -->
      <div v-if="work.isCurrent" class="badge red">
      현재 공연 중
    </div>
    
    <!-- D-day -->
    <div v-if="work.isCurrent && work.dDay !== null" class="d-day">
      D-{{ work.dDay }}
    </div>
  </div>
</template>

<script setup>
defineProps({ work: Object })
</script>

<style scoped>
.work-card {
  position: relative;
  border: 1px solid #e5e5e5;
  border-radius: 12px;
  padding: 16px;
  background: #fff;
  transition: all 0.25s ease;
  margin-bottom: 10px;
}
/* hover */
.work-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0,0,0,0.08);
}

.badge.red {
  position: absolute;
  top: 12px;
  right: 12px;
  background: #b11226;
  color: #fff;
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 8px;
}

.title {
  font-weight: 700;
}
.period {
  color: #666;
}
.theater {
  font-size: 14px;
  color: #333;
   margin-top: 6px; 
}

.role {
 margin-top: 4px;
  font-size: 13px;
  color: #777;
}
.work-card.current {
  border-color: #b11226;
  background: #fff5f5;
}

.d-day {
  position: absolute;
  bottom: 12px;
  right: 12px;
  font-size: 13px;
  font-weight: 600;
  color: #b11226;
}

</style>
