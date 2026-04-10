<template>
  <div v-if="config.show" class="confirm-overlay" @click.self="onCancel">
    <div class="confirm-container">
      <div class="confirm-content">
        <div class="confirm-header">
          <h3>{{ config.title }}</h3>
        </div>
        <div class="confirm-body">
          <p>{{ config.message }}</p>
        </div>
        <div class="confirm-footer">
          <button 
            class="btn btn-secondary" 
            @click="onCancel"
          >
            {{ config.cancelText }}
          </button>
          <button 
            class="btn btn-primary" 
            @click="onConfirm"
          >
            {{ config.confirmText }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { handleConfirm, handleCancel, registerConfirm } from '../utils/confirm'

// 配置数据
const config = ref({
  show: false,
  title: '确认操作',
  message: '确定要执行此操作吗？',
  confirmText: '确定',
  cancelText: '取消'
})

// 处理确认
const onConfirm = () => {
  handleConfirm()
}

// 处理取消
const onCancel = () => {
  handleCancel()
}

// 组件挂载时注册到全局
onMounted(() => {
  registerConfirm({
    config
  })
})
</script>

<style scoped>
.confirm-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  backdrop-filter: blur(2px);
}

.confirm-container {
  width: 100%;
  max-width: 480px;
  padding: 0 20px;
}

.confirm-content {
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
  animation: confirmSlideIn 0.3s ease-out;
  overflow: hidden;
}

@keyframes confirmSlideIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.confirm-header {
  padding: 20px 24px;
  border-bottom: 1px solid var(--border);
  background-color: var(--bg);
}

.confirm-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: var(--text-1);
}

.confirm-body {
  padding: 24px;
}

.confirm-body p {
  margin: 0;
  color: var(--text-2);
  font-size: 14px;
  line-height: 1.5;
  text-align: center;
}

.confirm-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 24px;
  border-top: 1px solid var(--border);
  background-color: var(--bg);
}

/* 按钮样式 */
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 10px 20px;
  font-size: 14px;
  font-weight: 500;
  border-radius: 8px;
  transition: all 0.3s ease;
  cursor: pointer;
  border: none;
  text-decoration: none;
  min-width: 80px;
}

.btn-primary {
  background-color: var(--primary);
  color: white;
}

.btn-primary:hover {
  background-color: var(--primary-dark);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(66, 184, 131, 0.3);
}

.btn-secondary {
  background-color: white;
  color: var(--text-1);
  border: 1px solid var(--border);
}

.btn-secondary:hover {
  background-color: var(--hover-bg);
  border-color: var(--primary);
  color: var(--primary);
}
</style>