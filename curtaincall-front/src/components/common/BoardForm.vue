<template>
  <div class="form-wrapper">

    <div class="form-box">
      <!-- 제목 -->
      <div class="row">
        <label>제목</label>
        <input v-model="localTitle" />
      </div>

      <!-- 작성자 -->
      <div class="row">
        <label>작성자</label>
        <input :value="writer" readonly />
      </div>

      <!-- 내용 -->
      <div class="row column">
        <label>내용</label>
        <textarea v-model="localContent" rows="12" />
      </div>

      <!-- 버튼 -->
      <div class="btn-row">
        <button class="cancel" @click="$emit('cancel')">취소</button>
        <button class="submit" @click="submit">
          {{ isEdit ? '수정하기' : '등록하기' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  title: String,
  content: String,
  writer: String,
  isEdit: Boolean,
})

const emit = defineEmits(['submit', 'cancel'])

const localTitle = ref(props.title || '')
const localContent = ref(props.content || '')

// 수정 시 props 변경 감지
watch(
  () => [props.title, props.content],
  () => {
    localTitle.value = props.title || ''
    localContent.value = props.content || ''
  }
)

const submit = () => {
  if (!localTitle.value.trim() || !localContent.value.trim()) {
    alert('제목과 내용을 입력하세요')
    return
  }

  emit('submit', {
    title: localTitle.value,
    content: localContent.value,
  })
}
</script>

<style scoped>
.form-wrapper {
  max-width: 800px;
  margin: 40px auto;
}
.page-title {
  font-size: 26px;
  margin-bottom: 24px;
}
.form-box {
  background: #f2f2f2;
  padding: 24px;
}
.row {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}
.row.column {
  flex-direction: column;
  align-items: flex-start;
}
label {
  width: 80px;
}
input,
textarea {
  flex: 1;
  background: #ddd;
  border: none;
  padding: 10px;
  font-size: 16px;
}
textarea {
  width: 100%;
  resize: none;
}
.btn-row {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
button {
  border: none;
  padding: 8px 18px;
  cursor: pointer;
}
</style>
