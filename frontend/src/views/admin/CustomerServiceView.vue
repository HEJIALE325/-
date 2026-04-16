<template>
  <div class="admin-customer-service">
    <h2>客服聊天管理</h2>
    <div class="chat-layout">
      <!-- 左侧用户列表 -->
      <div class="user-list">
        <!-- 搜索栏 -->
        <div class="search-bar">
          <input 
            type="text" 
            v-model="searchParams.username"
            placeholder="搜索用户..."
            @keyup.enter="handleSearch"
          >
          <button class="search-btn" @click="handleSearch">🔍</button>
        </div>
        
        <!-- 状态筛选 -->
        <div class="status-filter">
          <button 
            v-for="status in statusOptions" 
            :key="status.value"
            :class="['status-btn', { active: searchParams.status === status.value }]"
            @click="searchParams.status = status.value; handleSearch()"
          >
            {{ status.label }}
          </button>
        </div>
        
        <!-- 用户列表 -->
        <div class="user-items">
          <div 
            v-for="user in userList" 
            :key="user.yonghuId"
            :class="['user-item', { active: selectedUser && selectedUser.yonghuId === user.yonghuId }]"
            @click="selectUser(user)"
          >
            <div class="user-avatar">
              <img v-if="user.yonghuPhoto" :src="`http://localhost:8080/wangshangchongwudian/${user.yonghuPhoto}`" :alt="user.yonghuName">
              <div v-else class="avatar-placeholder">{{ user.yonghuName?.charAt(0) || '用' }}</div>
              <span v-if="user.unreadCount > 0" class="unread-badge">{{ user.unreadCount }}</span>
            </div>
            <div class="user-info">
              <div class="user-name">{{ user.yonghuName }}</div>
              <div class="user-last-message">{{ user.lastMessage || '暂无消息' }}</div>
            </div>
            <div class="user-meta">
              <div class="message-time">{{ formatDate(user.lastMessageTime) }}</div>
              <span class="status-indicator" :class="getStatusClass(user.status)">
                {{ getStatusText(user.status) }}
              </span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 右侧聊天窗口 -->
      <div class="chat-window" v-if="selectedUser">
        <!-- 聊天头部 -->
        <div class="chat-header">
          <div class="chat-user-info">
            <div class="user-avatar">
              <img v-if="selectedUser.yonghuPhoto" :src="`http://localhost:8080/wangshangchongwudian/${selectedUser.yonghuPhoto}`" :alt="selectedUser.yonghuName">
              <div v-else class="avatar-placeholder">{{ selectedUser.yonghuName?.charAt(0) || '用' }}</div>
            </div>
            <div class="user-details">
              <div class="user-name">{{ selectedUser.yonghuName }}</div>
              <div class="user-phone">{{ selectedUser.yonghuPhone }}</div>
            </div>
          </div>
          <div class="chat-actions">
            <span class="status-tag" :class="getStatusClass(selectedUser.status)">
              {{ getStatusText(selectedUser.status) }}
            </span>
          </div>
        </div>
        
        <!-- 聊天内容 -->
        <div class="chat-body" ref="chatBody">
          <div class="message-list">
            <div 
              v-for="message in chatMessages" 
              :key="message.id"
              :class="['message-item', message.type]"
            >
              <div class="message-content">
                <p>{{ message.content }}</p>
                <span class="message-time">{{ formatDate(message.time) }}</span>
              </div>
              <div class="message-status" v-if="message.type === 'admin'">
                <span class="status-completed">已回复</span>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 聊天输入 -->
        <div class="chat-footer">
          <textarea 
            v-model="replyContent"
            placeholder="请输入回复内容..."
            rows="3"
            @keyup.enter.exact="sendReply"
          ></textarea>
          <button class="send-button" @click="sendReply" :disabled="!replyContent.trim() || isLoading">
            {{ isLoading ? '发送中...' : '发送' }}
          </button>
        </div>
      </div>
      
      <!-- 空状态 -->
      <div class="empty-state" v-else>
        <div class="empty-content">
          <div class="empty-icon">💬</div>
          <h3>请选择一个用户开始聊天</h3>
          <p>从左侧列表中选择一个用户，即可开始客服对话</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { message } from '../../utils/message'
import { chatApi, yonghuApi } from '../../utils/api'

// 搜索参数
const searchParams = ref({
  username: '',
  status: ''
})
// 加载状态
const isLoading = ref(false)
// 用户列表
const userList = ref([])
// 选中的用户
const selectedUser = ref(null)
// 聊天消息
const chatMessages = ref([])
// 回复内容
const replyContent = ref('')
// 聊天主体元素
const chatBody = ref(null)
// 状态选项
const statusOptions = [
  { value: '', label: '全部' },
  { value: 'pending', label: '待处理' },
  { value: 'processing', label: '处理中' },
  { value: 'completed', label: '已完成' }
]

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 获取状态样式类
const getStatusClass = (status) => {
  switch (status) {
    case 0:
      return 'status-pending'
    case 1:
      return 'status-processing'
    case 2:
      return 'status-completed'
    default:
      return ''
  }
}

// 获取状态文本
const getStatusText = (status) => {
  switch (status) {
    case 0:
      return '待处理'
    case 1:
      return '处理中'
    case 2:
      return '已完成'
    default:
      return status
  }
}

// 获取用户列表
const getUserList = async () => {
  try {
    isLoading.value = true
    // 转换状态值为数字
    const params = { ...searchParams.value }
    if (params.status) {
      switch (params.status) {
        case 'pending':
          params.status = 0
          break
        case 'processing':
          params.status = 1
          break
        case 'completed':
          params.status = 2
          break
      }
    }
    const response = await chatApi.getPage({
      page: 1,
      limit: 100,
      ...params
    })
    if (response.code === 0) {
      const chatList = response.data.list || []
      // 按用户分组
      const userMap = new Map()
      chatList.forEach(item => {
        if (!userMap.has(item.yonghuId)) {
          userMap.set(item.yonghuId, {
            yonghuId: item.yonghuId,
            yonghuName: item.yonghuName,
            yonghuPhone: item.yonghuPhone,
            yonghuPhoto: item.yonghuPhoto,
            lastMessage: item.chatIssue || item.chatReply,
            lastMessageTime: item.issueTime || item.replyTime,
            status: item.zhuangtaiTypes,
            unreadCount: 0
          })
        } else {
          const user = userMap.get(item.yonghuId)
          const messageTime = new Date(item.issueTime || item.replyTime)
          const lastTime = new Date(user.lastMessageTime)
          if (messageTime > lastTime) {
            user.lastMessage = item.chatIssue || item.chatReply
            user.lastMessageTime = item.issueTime || item.replyTime
          }
          if (item.zhuangtaiTypes === 0) {
            user.unreadCount++
          }
        }
      })
      // 转换为数组并按最后消息时间排序
      userList.value = Array.from(userMap.values()).sort((a, b) => {
        return new Date(b.lastMessageTime) - new Date(a.lastMessageTime)
      })
    }
  } catch (error) {
    console.error('获取用户列表失败:', error)
    message.error('获取用户列表失败')
  } finally {
    isLoading.value = false
  }
}

// 搜索
const handleSearch = () => {
  getUserList()
}

// 选择用户
const selectUser = async (user) => {
  selectedUser.value = user
  await getChatMessages(user.yonghuId)
  
  // 将该用户的所有待处理消息更新为处理中状态
  await markAsProcessing(user.yonghuId)
}

// 标记消息为处理中
const markAsProcessing = async (yonghuId) => {
  try {
    // 获取该用户的待处理消息
    const response = await chatApi.getList({
      yonghuId: yonghuId,
      zhuangtaiTypes: 0, // 待处理状态
      page: 1,
      limit: 100
    })
    
    if (response.code === 0 && response.data.list && response.data.list.length > 0) {
      const pendingMessages = response.data.list
      
      // 批量更新状态
      for (const msg of pendingMessages) {
        await chatApi.update({
          id: msg.id,
          zhuangtaiTypes: 1 // 处理中状态
        })
      }
      
      // 重新获取用户列表，更新未读状态
      await getUserList()
    }
  } catch (error) {
    console.error('标记消息为处理中失败:', error)
  }
}

// 获取聊天消息
const getChatMessages = async (yonghuId) => {
  try {
    const response = await chatApi.getList({
      yonghuId: yonghuId,
      page: 1,
      limit: 100
    })
    if (response.code === 0) {
      const chatList = response.data.list || []
      // 转换为消息格式
      chatMessages.value = chatList.map(item => ({
        id: item.id,
        content: item.chatIssue || item.chatReply,
        time: item.issueTime || item.replyTime,
        type: item.chatReply ? 'admin' : 'user'
      })).sort((a, b) => new Date(a.time) - new Date(b.time))
      // 滚动到底部
      scrollToBottom()
    }
  } catch (error) {
    console.error('获取聊天消息失败:', error)
    message.error('获取聊天消息失败')
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

// 格式化日期为后端期望的格式
const formatDateForBackend = (date) => {
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

// 发送回复
const sendReply = async () => {
  if (!replyContent.value.trim()) return
  if (!selectedUser.value) return

  const replyText = replyContent.value.trim()
  const currentTime = new Date()

  // 先添加到消息列表
  const newMessage = {
    id: Date.now(),
    content: replyText,
    time: currentTime,
    type: 'admin'
  }
  chatMessages.value.push(newMessage)
  replyContent.value = ''
  scrollToBottom()

  try {
    isLoading.value = true
    // 查找对应的聊天记录
    const response = await chatApi.getList({
      yonghuId: selectedUser.value.yonghuId,
      page: 1,
      limit: 10
    })
    if (response.code === 0 && response.data.list && response.data.list.length > 0) {
      const chatItem = response.data.list[0]
      // 更新回复
      const updateResponse = await chatApi.update({
        id: chatItem.id,
        chatReply: replyText,
        replyTime: formatDateForBackend(currentTime),
        zhuangtaiTypes: 2
      })
      if (updateResponse.code === 0) {
        // 更新用户状态
        selectedUser.value.status = 2
        // 更新用户列表
        const userIndex = userList.value.findIndex(u => u.yonghuId === selectedUser.value.yonghuId)
        if (userIndex !== -1) {
          userList.value[userIndex].status = 2
          userList.value[userIndex].lastMessage = replyText
          userList.value[userIndex].lastMessageTime = currentTime
        }
        message.success('回复成功')
      } else {
        message.error('回复失败')
        // 移除失败的消息
        chatMessages.value.pop()
      }
    } else {
      message.error('未找到对应的聊天记录')
      // 移除失败的消息
      chatMessages.value.pop()
    }
  } catch (error) {
    console.error('发送回复失败:', error)
    message.error('回复失败')
    // 移除失败的消息
    chatMessages.value.pop()
  } finally {
    isLoading.value = false
  }
}

// 组件挂载时获取用户列表
onMounted(() => {
  getUserList()
})
</script>

<style scoped>
/* 样式与其他管理页面保持一致 */
.admin-customer-service {
  padding: 0;
  height: calc(100vh - 80px);
  display: flex;
  flex-direction: column;
}

h2 {
  margin-bottom: 24px;
  color: var(--text-1);
  font-size: 20px;
}

/* 聊天布局 */
.chat-layout {
  display: flex;
  flex: 1;
  background-color: var(--card);
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-base);
  overflow: hidden;
  min-height: 600px;
}

/* 左侧用户列表 */
.user-list {
  width: 320px;
  border-right: 1px solid var(--border);
  display: flex;
  flex-direction: column;
  background-color: white;
}

/* 搜索栏 */
.search-bar {
  padding: 12px 16px;
  border-bottom: 1px solid var(--border);
  display: flex;
  gap: 8px;
  align-items: center;
  background-color: var(--bg);
}

.search-bar input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  background-color: white;
}

.search-bar input:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 2px rgba(66, 184, 131, 0.1);
}

.search-btn {
  background: none;
  border: none;
  font-size: 16px;
  cursor: pointer;
  color: var(--text-2);
  padding: 8px;
  border-radius: var(--radius-base);
  transition: all 0.3s ease;
}

.search-btn:hover {
  background-color: var(--hover-bg);
  color: var(--text-1);
}

/* 状态筛选 */
.status-filter {
  padding: 12px 16px;
  border-bottom: 1px solid var(--border);
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.status-btn {
  padding: 4px 12px;
  border: 1px solid var(--border);
  border-radius: var(--radius-full);
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  background-color: white;
  color: var(--text-2);
}

.status-btn.active {
  background-color: var(--primary);
  color: white;
  border-color: var(--primary);
}

.status-btn:hover:not(.active) {
  border-color: var(--primary);
  color: var(--primary);
}

/* 用户列表 */
.user-items {
  flex: 1;
  overflow-y: auto;
}

.user-item {
  padding: 16px;
  display: flex;
  gap: 12px;
  align-items: center;
  border-bottom: 1px solid var(--border);
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
}

.user-item:hover {
  background-color: var(--hover-bg);
}

.user-item.active {
  background-color: rgba(66, 184, 131, 0.05);
  border-left: 3px solid var(--primary);
}

/* 用户头像 */
.user-avatar {
  position: relative;
  flex-shrink: 0;
}

.user-avatar img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid var(--border);
}

.avatar-placeholder {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary) 0%, var(--primary-light) 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 18px;
  border: 2px solid var(--border);
}

.unread-badge {
  position: absolute;
  top: -4px;
  right: -4px;
  background-color: var(--danger);
  color: white;
  border-radius: 50%;
  min-width: 18px;
  height: 18px;
  font-size: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 4px;
  font-weight: 600;
}

/* 用户信息 */
.user-info {
  flex: 1;
  min-width: 0;
}

.user-name {
  font-weight: 500;
  color: var(--text-1);
  font-size: 14px;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-last-message {
  font-size: 12px;
  color: var(--text-2);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 用户元信息 */
.user-meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 4px;
}

.message-time {
  font-size: 11px;
  color: var(--text-3);
}

.status-indicator {
  font-size: 10px;
  padding: 2px 8px;
  border-radius: var(--radius-full);
  font-weight: 500;
}

/* 右侧聊天窗口 */
.chat-window {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

/* 聊天头部 */
.chat-header {
  padding: 16px 24px;
  background-color: white;
  border-bottom: 1px solid var(--border);
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.chat-user-info {
  display: flex;
  gap: 12px;
  align-items: center;
}

.chat-user-info .user-avatar img {
  width: 40px;
  height: 40px;
}

.chat-user-info .avatar-placeholder {
  width: 40px;
  height: 40px;
  font-size: 16px;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.user-details .user-name {
  font-size: 16px;
  font-weight: 600;
  margin: 0;
}

.user-phone {
  font-size: 12px;
  color: var(--text-2);
}

.chat-actions {
  display: flex;
  gap: 8px;
  align-items: center;
}

.status-tag {
  padding: 4px 12px;
  border-radius: var(--radius-full);
  font-size: 12px;
  font-weight: 500;
}

/* 聊天内容 */
.chat-body {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
  background-color: #f5f5f5;
  background-image: url('https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=light%20gray%20background%20with%20subtle%20pattern%20for%20chat%20interface&image_size=square');
  background-size: 200px;
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  max-width: 800px;
  margin: 0 auto;
}

.message-item {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 18px;
  position: relative;
  word-wrap: break-word;
}

.message-item.user {
  align-self: flex-start;
  background-color: white;
  color: var(--text-1);
  border-bottom-left-radius: 4px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.message-item.admin {
  align-self: flex-end;
  background-color: var(--primary);
  color: white;
  border-bottom-right-radius: 4px;
}

.message-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.message-content p {
  margin: 0;
  line-height: 1.4;
  font-size: 14px;
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
  align-self: flex-end;
}

/* 聊天输入 */
.chat-footer {
  padding: 16px 24px;
  background-color: white;
  border-top: 1px solid var(--border);
  display: flex;
  gap: 12px;
  align-items: flex-end;
  box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.05);
}

.chat-footer textarea {
  flex: 1;
  padding: 12px 16px;
  border: 1px solid var(--border);
  border-radius: 20px;
  resize: none;
  font-size: 14px;
  font-family: inherit;
  min-height: 60px;
  max-height: 120px;
  outline: none;
  transition: all 0.3s ease;
  background-color: var(--bg);
}

.chat-footer textarea:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 2px rgba(66, 184, 131, 0.2);
  background-color: white;
}

.send-button {
  padding: 12px 24px;
  background-color: var(--primary);
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
  white-space: nowrap;
  align-self: flex-end;
}

.send-button:hover {
  background-color: var(--primary-dark);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(66, 184, 131, 0.3);
}

.send-button:disabled {
  background-color: #d9d9d9;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

/* 空状态 */
.empty-state {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
}

.empty-content {
  text-align: center;
  padding: 48px;
  background-color: white;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-sm);
  max-width: 400px;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.5;
}

.empty-content h3 {
  margin: 0 0 8px 0;
  color: var(--text-1);
  font-size: 18px;
}

.empty-content p {
  margin: 0;
  color: var(--text-2);
  font-size: 14px;
}

/* 状态样式 */
.status-pending {
  background-color: rgba(250, 152, 3, 0.1);
  color: var(--warning);
}

.status-processing {
  background-color: rgba(59, 130, 246, 0.1);
  color: var(--primary);
}

.status-completed {
  background-color: rgba(82, 196, 26, 0.1);
  color: var(--success);
}

/* 滚动条样式 */
.user-items::-webkit-scrollbar,
.chat-body::-webkit-scrollbar {
  width: 6px;
}

.user-items::-webkit-scrollbar-track,
.chat-body::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.user-items::-webkit-scrollbar-thumb,
.chat-body::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.user-items::-webkit-scrollbar-thumb:hover,
.chat-body::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .chat-layout {
    flex-direction: column;
  }
  
  .user-list {
    width: 100%;
    height: 300px;
    border-right: none;
    border-bottom: 1px solid var(--border);
  }
  
  .chat-body {
    padding: 16px;
  }
  
  .message-item {
    max-width: 85%;
  }
  
  .chat-footer {
    padding: 12px 16px;
  }
}
</style>