<template>
  <span :class="['order-status', getStatusClass(status)]">
    {{ getStatusText(status) }}
  </span>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  status: {
    type: Number,
    required: true
  },
  statusText: {
    type: String,
    default: ''
  }
})

// 获取状态样式类
const getStatusClass = (status) => {
  switch (status) {
    case 1:
      return 'status-pending'
    case 2:
      return 'status-processing'
    case 3:
      return 'status-rejected'
    case 4:
      return 'status-completed'
    default:
      return ''
  }
}

// 获取状态文本
const getStatusText = (status) => {
  if (props.statusText) {
    return props.statusText
  }
  
  switch (status) {
    case 1:
      return '待审核'
    case 2:
      return '审核通过'
    case 3:
      return '审核拒绝'
    case 4:
      return '已完成'
    default:
      return '未知状态'
  }
}
</script>

<style scoped>
.order-status {
  display: inline-block;
  padding: 4px 12px;
  border-radius: var(--radius-full);
  font-size: 12px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.status-pending {
  background-color: rgba(255, 193, 7, 0.1);
  color: #ffc107;
}

.status-processing {
  background-color: rgba(0, 123, 255, 0.1);
  color: #007bff;
}

.status-rejected {
  background-color: rgba(220, 53, 69, 0.1);
  color: #dc3545;
}

.status-completed {
  background-color: rgba(25, 135, 84, 0.1);
  color: #198754;
}

@media (max-width: 768px) {
  .order-status {
    padding: 3px 8px;
    font-size: 11px;
  }
}
</style>