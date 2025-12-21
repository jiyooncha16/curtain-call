<template>
  <div class="wrapper">
    <canvas ref="chartRef" ></canvas>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick, watch } from 'vue'
import axios from 'axios'
import ChartDataLabels from 'chartjs-plugin-datalabels'
import { useRoute } from 'vue-router'

const props = defineProps({
  stats : Array
})

watch(
  () => props.stats,
  (newStats) => {
    if (newStats && newStats.length) {
      nextTick(() => createChart())
    }
  },
  { immediate: true }
)

// Chart.js 등록
import {
  Chart,
  BarController,
  BarElement,
  LinearScale,
  CategoryScale,
  Tooltip,
  Legend
} from 'chart.js'

Chart.register(
  BarController,
  BarElement,
  LinearScale,
  CategoryScale,
  Tooltip,
  Legend,
  ChartDataLabels
)
Chart.defaults.font.family = "'IBM Plex Sans KR', sans-serif"
Chart.defaults.font.size = 17
Chart.defaults.color = '#111827'

// const rateList = ref({})
const chartRef = ref(null)
let chartInstance = null
// const route = useRoute()

const createChart = () => {
  if (!chartRef.value) return

  const labels = ['1점', '2점', '3점', '4점', '5점']
  const counts = props.stats.map(item => item.count)
  const ctx = chartRef.value.getContext('2d')
  const maxValue = Math.max(...counts)

// 그라데이션
const gradient = ctx.createLinearGradient(0, 0, 0, 260)
gradient.addColorStop(0, 'rgba(253, 226, 225, 1)')    // 위 완전 진함
gradient.addColorStop(1, 'rgba(253, 226, 225, 0.3)') // 아래쪽 (연함)
chartInstance = new Chart(chartRef.value, {
    type: 'bar',
    data: {
      labels,
      datasets: [
        {
            label: '별점 분포',
            data: counts,
            backgroundColor: gradient,
            borderRadius: 12,           // ⭐ 핵심
            borderSkipped: 'bottom',    // ⭐ 위쪽만 둥글게
            barThickness: 100,           // 막대 두께 (선택)
            maxBarThickness: 100,
        }
        ]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        layout: {
            padding: {
            top: 50,
            bottom: 15,
            right : 30,
            left : 20,
            }
        },
        plugins: {
            legend: {
            display: false
            },
            // tooltip: {
            // backgroundColor: '#111827',
            // titleColor: '#fff',
            // bodyColor: '#fff',
            // padding: 12,
            // displayColors: false,
            // callbacks: {
            //     label: (ctx) => `${ctx.raw}개`
            // }
        datalabels: {
          formatter: (value) => `${value}`,
          anchor: 'end',
          align: 'top',
          offset: 4,
          font: {
            size: 17,
            weight: 'bold'
          },
          color: '#f3b6b2'
        },},
        scales: {
          x: {
            grid: { display: false },
            ticks: {
              font: { size: 17}
            }
          },
          y: {
            display : false,
            beginAtZero: true,
            max: maxValue,
            suggestedMax: Math.max(...counts) + 5,
            grid: { display: false },
            ticks: {
              stepSize: 5
            }
          }
        }
      }
  })
}

// 평점 통계 받아오기
// onMounted(async () => {
//   console.log('route.params:', route.params)
//   console.log('id:', route.params.id)
//   if (!id) return

//   /** 1️⃣ 데이터 먼저 */
//   const res = await axios.get(`/api/reviews/rating/stats/${id}`)
//   console.log(res.data)
//   rateList.value = res.data

//   /** 2️⃣ DOM 렌더 완료 대기 */
//   await nextTick()

//   /** 3️⃣ 차트 생성 */
//   createChart()
// })

onBeforeUnmount(() => {
  if (chartInstance) {
    chartInstance.destroy()
  }
})
</script>

<style scoped>
  .wrapper {
    width: 100%;
    height:260px;
  }

  .wrapper canvas {
  width: 100% !important;
  height: 100% !important;
  display: block;
}
</style>
