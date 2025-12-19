<template>
  <div style="width: 100%; height:260px">
    <canvas ref="chartRef"></canvas>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import {
  Chart,
  LineController,
  LineElement,
  PointElement,
  LinearScale,
  CategoryScale,
  Filler,
  Tooltip,
  Legend
} from 'chart.js'
import axios from 'axios'
import ChartDataLabels from 'chartjs-plugin-datalabels'

// Chart.js 등록
Chart.register(
  LineController,
  LineElement,
  PointElement,
  LinearScale,
  CategoryScale,
  Filler,
  Tooltip,
  Legend,
  ChartDataLabels
)

const chartRef = ref(null)
let chartInstance = null

onMounted(async () => {
  // 예: 사용자 ID
  const userId = 1

  // ⭐ 별점 분포 API
//   const res = await axios.get(`/user/${userId}/rating-stats`)

//   const ratingData = res.data

const ctx = chartRef.value.getContext('2d')

// 그라데이션
// 그라데이션
const gradient = ctx.createLinearGradient(0, 0, 0, 260)
gradient.addColorStop(0, 'rgba(253, 226, 225, 0.6)')  // 위쪽 (진함)
gradient.addColorStop(1, 'rgba(253, 226, 225, 0.05)') // 아래쪽 (연함)

const ratingData = 
  {
    "1": 10,
    "2": 3,
    "3": 15,
    "4": 20,
    "5": 5
  }

  const labels = ['1점', '2점', '3점', '4점', '5점']
  const counts = [
    ratingData[1] ?? 0,
    ratingData[2] ?? 0,
    ratingData[3] ?? 0,
    ratingData[4] ?? 0,
    ratingData[5] ?? 0
  ]

  chartInstance = new Chart(chartRef.value, {
    type: 'line',
    data: {
      labels,
      datasets: [
        {
            label: '별점 분포',
            data: counts,
            fill: true,
            clamp: true,
            tension: 0.5,          // 곡선 더 부드럽게
            backgroundColor: gradient,
            borderColor: '#f3b6b2',          // 살짝 진한 핑크
            pointBackgroundColor: '#f3b6b2',
            borderWidth: 3,
            pointRadius: 6,
            pointHoverRadius: 8,
            pointBorderWidth: 0
        }
        ]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        layout: {
            padding: {
            top: 24,
            bottom: 28
            }
        },
        plugins: {
            legend: {
            display: false
            },
            tooltip: {
            backgroundColor: '#111827',
            titleColor: '#fff',
            bodyColor: '#fff',
            padding: 12,
            displayColors: false,
            callbacks: {
                label: (ctx) => `${ctx.raw}개`
            }
        },
        datalabels: {
            formatter: (value, ctx) => {
            return `${ctx.dataIndex + 1}점`
            },
            align: 'top',      // 'top' | 'bottom'
            anchor: 'end',
            offset: 6,
            font: {
            size: 18,
            weight: 'bold'
            },
            color: '#f3b6b2'   // 다크 그레이
            }
        },
        scales: {
            x: {
                display: false,
                offset: true,
                grid: {
                    display: false
            },
            ticks: {
                font: {
                size: 20,
                weight: 'bold'
                },
                color: '#111827' // 1점 2점 폰트 컬러
            }
            },
            y: {
            position: 'right',   // ⭐ 핵심
            beginAtZero: true,
            suggestedMax: Math.max(...counts) + 5, // ⭐ 핵심
            grid: {
                display: false
            },
            ticks: {
                stepSize: 5,
                font: {
                size: 12
                }
            }
            }
        }
        }
  })
})

onBeforeUnmount(() => {
  if (chartInstance) {
    chartInstance.destroy()
  }
})
</script>
