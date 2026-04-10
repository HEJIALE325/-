<template>
  <div class="message-container" ref="container">
    <transition-group name="message-fade" tag="div">
      <div
        v-for="message in messages"
        :key="message.id"
        :class="['message-item', `message-${message.type}`]"
        :style="{ zIndex: 10000 + message.id }"
      >
        <div class="message-content">
          <span class="message-icon">{{ getIcon(message.type) }}</span>
          <span class="message-text">{{ message.text }}</span>
          <button class="message-close" @click="removeMessage(message.id)">&times;</button>
        </div>
      </div>
    </transition-group>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, getCurrentInstance } from 'vue'

const messages = ref([])
let messageId = 0

const removeMessage = (id) => {
  const index = messages.value.findIndex(msg => msg.id === id)
  if (index !== -1) {
    messages.value.splice(index, 1)
  }
}

const getIcon = (type) => {
  const icons = {
    success: '✅',
    warning: '⚠️',
    error: '❌',
    info: 'ℹ️'
  }
  return icons[type] || icons.info
}

const showMessage = (options) => {
  const id = ++messageId
  const message = {
    id,
    text: options.text || '',
    type: options.type || 'info',
    duration: options.duration || 3000
  }
  
  messages.value.push(message)
  
  // 自动关闭
  setTimeout(() => {
    removeMessage(id)
  }, message.duration)
  
  return id
}

// 暴露方法给外部
const message = {
  success: (text, duration) => showMessage({ text, type: 'success', duration }),
  warning: (text, duration) => showMessage({ text, type: 'warning', duration }),
  error: (text, duration) => showMessage({ text, type: 'error', duration }),
  info: (text, duration) => showMessage({ text, type: 'info', duration })
}

// 注册为全局属性
const app = getCurrentInstance()?.appContext.app
if (app) {
  app.config.globalProperties.$message = message
}

// 从工具文件导入initMessage
import { initMessage } from '../utils/message'

// 初始化消息实例
initMessage(message)

// 提供给模板使用
defineExpose({
  messages,
  showMessage,
  removeMessage,
  message
})
</script>

<style scoped>
.message-container {
  position: fixed;
  top: 20px;
  right: 20px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  z-index: 10000;
  pointer-events: none;
}

.message-item {
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  padding: 12px 16px;
  min-width: 300px;
  max-width: 500px;
  pointer-events: auto;
  animation: slideIn 0.3s ease-out;
  display: flex;
  align-items: center;
}

.message-success {
  border-left: 4px solid #67c23a;
}

.message-warning {
  border-left: 4px solid #e6a23c;
}

.message-error {
  border-left: 4px solid #f56c6c;
}

.message-info {
  border-left: 4px solid #909399;
}

.message-content {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
}

.message-icon {
  font-size: 18px;
  flex-shrink: 0;
}

.message-text {
  flex: 1;
  font-size: 14px;
  line-height: 1.5;
  color: #303133;
}

.message-close {
  background: none;
  border: none;
  font-size: 18px;
  color: #909399;
  cursor: pointer;
  padding: 0;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: all 0.2s;
}

.message-close:hover {
  background: #f5f7fa;
  color: #606266;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(100%);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.message-fade-enter-active,
.message-fade-leave-active {
  transition: all 0.3s ease;
}

.message-fade-enter-from,
.message-fade-leave-to {
  opacity: 0;
  transform: translateX(100%);
}

.message-fade-move {
  transition: transform 0.3s ease;
}
</style>