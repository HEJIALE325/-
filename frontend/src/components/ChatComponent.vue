<template>
  <div class="chat-component">
    <!-- 聊天入口按钮 -->
    <div class="chat-button" @click="toggleChatWindow">
      <span class="chat-icon">💬</span>
      <span class="chat-badge" v-if="unreadCount > 0">{{ unreadCount }}</span>
    </div>

    <!-- 聊天窗口 -->
    <div class="chat-window" v-if="showChatWindow">
      <div class="chat-header">
        <h3>在线客服</h3>
        <button class="close-button" @click="closeChatWindow">&times;</button>
      </div>
      
      <div class="chat-body" ref="chatBody">
        <div class="message-list">
          <div 
            v-for="(message, index) in messages" 
            :key="index"
            :class="['message-item', message.type]"
          >
            <div class="message-content">
              <p>{{ message.content }}</p>
              <span class="message-time">{{ formatDate(message.time) }}</span>
            </div>
            <div class="message-status" v-if="message.type === 'user'">
              <span v-if="message.status === 0" class="status-pending">待处理</span>
              <span v-else-if="message.status === 1" class="status-processing">处理中</span>
              <span v-else-if="message.status === 2" class="status-completed">已完成</span>
            </div>
          </div>
        </div>
      </div>
      
      <div class="chat-footer">
        <textarea 
          v-model="inputMessage"
          placeholder="请输入您的问题..."
          @keyup.enter.exact="sendMessage"
          rows="3"
        ></textarea>
        <button class="send-button" @click="sendMessage" :disabled="!inputMessage.trim()">
          发送
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import { message } from '../utils/message'
import { chatApi } from '../utils/api'

// 聊天窗口状态
const showChatWindow = ref(false)
// 消息列表
const messages = ref([])
// 输入消息
const inputMessage = ref('')
// 未读消息数
const unreadCount = ref(0)
// 聊天主体元素
const chatBody = ref(null)
// 轮询定时器
let pollTimer = null

// 切换聊天窗口
const toggleChatWindow = () => {
  showChatWindow.value = !showChatWindow.value
  if (showChatWindow.value) {
    // 打开窗口时获取聊天历史
    getChatHistory()
    // 标记消息为已读
    unreadCount.value = 0
  }
}

// 关闭聊天窗口
const closeChatWindow = () => {
  showChatWindow.value = false
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 获取聊天历史
const getChatHistory = async () => {
  try {
    const response = await chatApi.getList({ page: 1, limit: 50 })
    if (response.code === 0) {
      const chatList = response.data.list || []
      // 转换为消息格式
      messages.value = chatList.map(item => ({
        id: item.id,
        content: item.chatIssue || item.chatReply,
        time: item.issueTime || item.replyTime,
        type: item.chatReply ? 'admin' : 'user',
        status: item.zhuangtaiTypes
      })).sort((a, b) => new Date(a.time) - new Date(b.time))
      // 滚动到底部
      scrollToBottom()
    }
  } catch (error) {
    console.error('获取聊天历史失败:', error)
    message.error('获取聊天历史失败')
  }
}

// 发送消息
const sendMessage = async () => {
  if (!inputMessage.value.trim()) return
  
  const messageContent = inputMessage.value.trim()
  
  // 先添加到消息列表
  const newMessage = {
    content: messageContent,
    time: new Date(),
    type: 'user',
    status: 0
  }
  messages.value.push(newMessage)
  inputMessage.value = ''
  scrollToBottom()
  
  try {
    // 发送到后端
    const response = await chatApi.add({
      chatIssue: messageContent,
      issueTime: new Date(),
      zhuangtaiTypes: 0,
      chatTypes: 0
    })
    if (response.code === 0) {
      // 更新消息状态
      const lastMessage = messages.value[messages.value.length - 1]
      lastMessage.id = response.data.id
    } else {
      message.error('发送失败')
      // 移除失败的消息
      messages.value.pop()
    }
  } catch (error) {
    console.error('发送消息失败:', error)
    message.error('发送失败')
    // 移除失败的消息
    messages.value.pop()
  }
}

// 检查新消息
const checkNewMessages = async () => {
  try {
    const response = await chatApi.getList({ page: 1, limit: 50 })
    if (response.code === 0) {
      const chatList = response.data.list || []
      const newMessages = []
      
      // 检查是否有新消息
      chatList.forEach(item => {
        const existingMessage = messages.value.find(msg => msg.id === item.id)
        if (!existingMessage) {
          newMessages.push({
            id: item.id,
            content: item.chatIssue || item.chatReply,
            time: item.issueTime || item.replyTime,
            type: item.chatReply ? 'admin' : 'user',
            status: item.zhuangtaiTypes
          })
        } else if (existingMessage.status !== item.zhuangtaiTypes) {
          // 更新状态
          existingMessage.status = item.zhuangtaiTypes
          existingMessage.time = item.replyTime || existingMessage.time
        }
      })
      
      if (newMessages.length > 0) {
        messages.value = [...messages.value, ...newMessages].sort((a, b) => new Date(a.time) - new Date(b.time))
        if (!showChatWindow.value) {
          unreadCount.value += newMessages.filter(msg => msg.type === 'admin').length
        }
        scrollToBottom()
      }
    }
  } catch (error) {
    console.error('检查新消息失败:', error)
  }
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (chatBody.value) {
      chatBody.value.scrollTop = chatBody.value.scrollHeight
    }
  })
}

// 组件挂载时
onMounted(() => {
  // 初始化获取聊天历史
  getChatHistory()
  // 设置轮询
  pollTimer = setInterval(checkNewMessages, 3000) // 每3秒检查一次
})

// 组件卸载时
onUnmounted(() => {
  // 清除轮询
  if (pollTimer) {
    clearInterval(pollTimer)
  }
})

// 监听消息变化，滚动到底部
watch(messages, () => {
  scrollToBottom()
}, { deep: true })
</script>

<style scoped>
.chat-component {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 1000;
}

/* 聊天入口按钮 */
.chat-button {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background-color: var(--primary);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  position: relative;
}

.chat-button:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
}

.chat-icon {
  font-size: 24px;
}

.chat-badge {
  position: absolute;
  top: -5px;
  right: -5px;
  background-color: #ff4d4f;
  color: white;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 聊天窗口 */
.chat-window {
  width: 350px;
  height: 450px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
}

/* 聊天头部 */
.chat-header {
  padding: 16px;
  background-color: var(--primary);
  color: white;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chat-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}

.close-button {
  background: none;
  border: none;
  color: white;
  font-size: 20px;
  cursor: pointer;
  padding: 0;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: background-color 0.3s ease;
}

.close-button:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

/* 聊天主体 */
.chat-body {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
  background-color: #f5f5f5;
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.message-item {
  max-width: 80%;
  padding: 10px 14px;
  border-radius: 18px;
  position: relative;
}

.message-item.user {
  align-self: flex-end;
  background-color: var(--primary);
  color: white;
  border-bottom-right-radius: 4px;
}

.message-item.admin {
  align-self: flex-start;
  background-color: white;
  color: #333;
  border-bottom-left-radius: 4px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.message-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.message-content p {
  margin: 0;
  line-height: 1.4;
}

.message-time {
  font-size: 11px;
  opacity: 0.7;
  align-self: flex-end;
}

.message-status {
  font-size: 11px;
  margin-top: 4px;
  opacity: 0.8;
}

.status-pending {
  color: #faad14;
}

.status-processing {
  color: #1890ff;
}

.status-completed {
  color: #52c41a;
}

/* 聊天底部 */
.chat-footer {
  padding: 12px 16px;
  border-top: 1px solid #e8e8e8;
  display: flex;
  gap: 10px;
  align-items: flex-end;
}

.chat-footer textarea {
  flex: 1;
  padding: 10px 14px;
  border: 1px solid #d9d9d9;
  border-radius: 20px;
  resize: none;
  font-size: 14px;
  font-family: inherit;
  min-height: 60px;
  max-height: 120px;
  outline: none;
  transition: all 0.3s ease;
}

.chat-footer textarea:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 2px rgba(66, 184, 131, 0.2);
}

.send-button {
  padding: 10px 16px;
  background-color: var(--primary);
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.send-button:hover {
  background-color: var(--primary-dark);
  transform: translateY(-1px);
}

.send-button:disabled {
  background-color: #d9d9d9;
  cursor: not-allowed;
  transform: none;
}

/* 滚动条样式 */
.chat-body::-webkit-scrollbar {
  width: 6px;
}

.chat-body::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.chat-body::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.chat-body::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>