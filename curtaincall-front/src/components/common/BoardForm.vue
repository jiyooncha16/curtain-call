<template>
  <div class="board-form">
    <!-- 제목 -->
    <div class="row">
      <label>제목</label>
      <input
        v-model="localTitle"
        type="text"
        placeholder="제목을 입력하세요"
      />
    </div>

    <!-- 작성자 -->
    <div class="row">
      <label>작성자</label>
      <input
        :value="writer"
        readonly
      />
    </div>

    <!-- 내용 -->
    <div class="row">
      <label>내용</label>
      <textarea
        v-model="localContent"
        rows="10"
        placeholder="내용을 입력하세요"
      />
    </div>

    <!-- 버튼 -->
    <div class="actions">
      <button @click="submit">
        {{ isEdit ? '수정하기' : '등록하기' }}
      </button>
      <button class="cancel" @click="$emit('cancel')">
        취소
      </button>
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

/* 🔥 props를 직접 수정하지 말고 local state */
const localTitle = ref('')
const localContent = ref('')

/* 🔥 수정 페이지에서 기존 값 세팅 */
watch(
  () => [props.title, props.content],
  () => {
    localTitle.value = props.title || ''
    localContent.value = props.content || ''
  },
  { immediate: true }
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
.board-form {
  width: 100%;
  background: #f2f2f2;
  padding: 20px;
  border-radius: 8px;
}

.row {
  margin-bottom: 16px;
  display: flex;
  flex-direction: column;
}

label {
  font-weight: bold;
  margin-bottom: 6px;
}

input,
textarea {
  border: none;
  padding: 10px;
  background: #ddd;
  font-size: 16px;
}

input:focus,
textarea:focus {
  outline: none;
}

.actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

button {
  padding: 6px 14px;
  border: none;
  cursor: pointer;
}

.cancel {
  background: #bbb;
}
</style>
