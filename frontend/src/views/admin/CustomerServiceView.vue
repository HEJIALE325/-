<template>
  <div class="admin-customer-service">
    <h2>客服聊天管理</h2>
    <div class="chat-container">
      <!-- 左侧用户列表 -->
      <div class="user-list">
        <!-- 搜索栏 -->
        <div class="search-section">
          <form @submit.prevent="handleSearch">
            <div class="form-row">
              <div class="form-group">
                <input 
                  type="text" 
                  v-model="searchParams.username"
                  placeholder="搜索用户..."
                  class="search-input"
                >
              </div>
              <div class="form-group">
                <select 
                  v-model="searchParams.status"
                  class="status-select"
                >
                  <option value="">全部</option>
                  <option value="pending">待处理</option>
                  <option value="processing">处理中</option>
                  <option value="completed">已完成</option>
                </select>
              </div>
            </div>
          </form>
        </div>
        
        <!-- 用户列表 -->
        <div class="user-items">
          <div 
            v-for="item in list" 
            :key="item.id"
            class="user-item"
            :class="{ active: selectedItem?.id === item.id }"
            @click="selectUser(item)"
          >
            <div class="user-avatar">
              {{ item.yonghuName?.charAt(0) || '?' }}
            </div>
            <div class="user-info">
              <div class="user-name">{{ item.yonghuName || '未知用户' }}</div>
              <div class="user-message">{{ item.chatIssue || '无消息' }}</div>
              <div class="user-meta">
                <span class="user-time">{{ formatDate(item.issueTime) }}</span>
                <span class="status-tag" :class="getStatusClass(item.zhuangtaiTypes)">
                  {{ getStatusText(item.zhuangtaiTypes) }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 右侧聊天窗口 -->
      <div class="chat-window" v-if="selectedItem">
        <!-- 聊天头部 -->
        <div class="chat-header">
          <div class="chat-header-info">
            <div class="chat-header-avatar">{{ selectedItem.yonghuName?.charAt(0) || '?' }}</div>
            <div>
              <div class="chat-header-name">{{ selectedItem.yonghuName || '未知用户' }}</div>
              <div class="chat-header-contact">{{ selectedItem.yonghuPhone || '无联系方式' }}</div>
            </div>
          </div>
          <div class="chat-header-actions">
            <span class="status-tag" :class="getStatusClass(selectedItem.zhuangtaiTypes)">
              {{ getStatusText(selectedItem.zhuangtaiTypes) }}
            </span>
          </div>
        </div>
        
        <!-- 聊天内容 -->
        <div class="chat-body">
          <!-- 用户消息 -->
          <div class="message-item user">
            <div class="message-avatar">{{ selectedItem.yonghuName?.charAt(0) || '?' }}</div>
            <div class="message-content">
              <div class="message-text">{{ selectedItem.chatIssue }}</div>
              <div class="message-time">{{ formatDate(selectedItem.issueTime) }}</div>
            </div>
          </div>
          
          <!-- 客服回复 -->
          <div class="message-item admin" v-if="selectedItem.chatReply">
            <div class="message-avatar">客</div>
            <div class="message-content">
              <div class="message-text">{{ selectedItem.chatReply }}</div>
              <div class="message-time">{{ formatDate(selectedItem.replyTime) }}</div>
            </div>
          </div>
        </div>
        
        <!-- 回复输入框 -->
        <div class="chat-footer">
          <form @submit.prevent="handleSubmit">
            <textarea 
              v-model="formData.response"
              placeholder="请输入回复内容..."
              rows="3"
              required
            ></textarea>
            <div class="chat-footer-actions">
              <button type="submit" class="btn btn-primary" :disabled="isLoading">
                {{ isLoading ? '发送中...' : '发送' }}
              </button>
            </div>
          </form>
        </div>
      </div>
      
      <!-- 空状态 -->
      <div class="empty-state" v-else>
        <div class="empty-icon">💬</div>
        <h3>选择一个用户开始聊天</h3>
        <p>从左侧列表中选择一个用户，开始处理他们的咨询</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { message } from '../../utils/message'
import { chatApi } from '../../utils/api'

// 列表数据
const list = ref([])
// 搜索参数
const searchParams = ref({
  username: '',
  status: ''
})
// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
// 加载状态
const isLoading = ref(false)
// 选中的用户
const selectedItem = ref(null)
// 回复表单
const formData = ref({})

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

// 获取列表数据
const getList = async () => {
  try {
    isLoading.value = true
    const response = await chatApi.getPage({
      page: currentPage.value,
      limit: pageSize.value,
      ...searchParams.value
    })
    if (response.code === 0) {
      list.value = response.data.list || []
      totalPages.value = Math.ceil(response.data.total / pageSize.value)
    }
  } catch (error) {
    console.error('获取客服聊天列表失败:', error)
    message.error('获取列表失败')
  } finally {
    isLoading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  getList()
}

// 重置搜索
const handleReset = () => {
  searchParams.value = {
    username: '',
    status: ''
  }
  currentPage.value = 1
  getList()
}

// 选择用户
const selectUser = (item) => {
  selectedItem.value = { ...item }
  formData.value = {
    response: item.chatReply || ''
  }
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

// 提交回复
const handleSubmit = async () => {
  if (!selectedItem.value) return
  
  try {
    isLoading.value = true
    const currentTime = new Date()
    const response = await chatApi.update({
      id: selectedItem.value.id,
      chatReply: formData.value.response,
      replyTime: formatDateForBackend(currentTime),
      zhuangtaiTypes: 2
    })
    if (response.code === 0) {
      selectedItem.value.chatReply = formData.value.response
      selectedItem.value.zhuangtaiTypes = 2
      selectedItem.value.replyTime = new Date()
      // 更新列表数据
      const index = list.value.findIndex(item => item.id === selectedItem.value.id)
      if (index !== -1) {
        list.value[index] = { ...selectedItem.value }
      }
      message.success('回复成功')
    } else {
      message.error('回复失败')
    }
  } catch (error) {
    console.error('处理咨询失败:', error)
    message.error('处理失败')
  } finally {
    isLoading.value = false
  }
}

// 分页处理
const handlePrevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
    getList()
  }
}

const handleNextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
    getList()
  }
}

// 组件挂载时获取列表数据
onMounted(() => {
  getList()
})
</script>

<style scoped>
/* 样式与其他管理页面保持一致 */
.admin-customer-service {
  padding: 0;
}

h2 {
  margin-bottom: 24px;
  color: var(--text-1);
  font-size: 20px;
}

/* 聊天容器 */
.chat-container {
  display: flex;
  height: 80vh;
  background-color: var(--card);
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-base);
  overflow: hidden;
}

/* 左侧用户列表 */
.user-list {
  width: 350px;
  border-right: 1px solid var(--border);
  display: flex;
  flex-direction: column;
}

/* 搜索区域 */
.search-section {
  padding: 16px;
  border-bottom: 1px solid var(--border);
  background-color: white;
}

.search-section .form-row {
  display: flex;
  gap: 12px;
  align-items: center;
}

.search-section .form-group {
  flex: 1;
  min-width: 0;
}

.search-input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  transition: all 0.3s ease;
}

.search-input:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.status-select {
  width: 120px;
  padding: 10px 12px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  background-color: white;
}

/* 用户列表 */
.user-items {
  flex: 1;
  overflow-y: auto;
}

.user-item {
  display: flex;
  padding: 16px;
  border-bottom: 1px solid var(--border);
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.user-item:hover {
  background-color: var(--hover-bg);
}

.user-item.active {
  background-color: rgba(66, 184, 131, 0.1);
  border-left: 3px solid var(--primary);
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: var(--primary);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 16px;
  margin-right: 12px;
  flex-shrink: 0;
}

.user-info {
  flex: 1;
  min-width: 0;
}

.user-name {
  font-weight: 600;
  color: var(--text-1);
  font-size: 14px;
  margin-bottom: 4px;
}

.user-message {
  font-size: 13px;
  color: var(--text-2);
  margin-bottom: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: var(--text-3);
}

.user-time {
  margin-right: 8px;
}

/* 右侧聊天窗口 */
.chat-window {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: var(--bg);
}

/* 聊天头部 */
.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background-color: white;
  border-bottom: 1px solid var(--border);
}

.chat-header-info {
  display: flex;
  align-items: center;
}

.chat-header-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: var(--primary);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 16px;
  margin-right: 12px;
}

.chat-header-name {
  font-weight: 600;
  color: var(--text-1);
  font-size: 14px;
  margin-bottom: 2px;
}

.chat-header-contact {
  font-size: 12px;
  color: var(--text-3);
}

.chat-header-actions {
  display: flex;
  align-items: center;
}

/* 聊天内容 */
.chat-body {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  max-width: 70%;
}

.message-item.user {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.message-item.admin {
  align-self: flex-start;
}

.message-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: var(--primary);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 12px;
  flex-shrink: 0;
}

.message-item.user .message-avatar {
  background-color: #6c757d;
}

.message-content {
  background-color: white;
  padding: 12px 16px;
  border-radius: 18px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  max-width: 100%;
}

.message-item.user .message-content {
  background-color: var(--primary);
  color: white;
  border-bottom-right-radius: 4px;
}

.message-item.admin .message-content {
  border-bottom-left-radius: 4px;
}

.message-text {
  font-size: 14px;
  line-height: 1.4;
  margin-bottom: 4px;
  white-space: pre-wrap;
}

.message-time {
  font-size: 11px;
  opacity: 0.7;
  align-self: flex-end;
}

/* 回复输入框 */
.chat-footer {
  padding: 16px 24px;
  background-color: white;
  border-top: 1px solid var(--border);
}

.chat-footer form {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.chat-footer textarea {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  resize: none;
  min-height: 80px;
  font-family: inherit;
  transition: all 0.3s ease;
}

.chat-footer textarea:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.chat-footer-actions {
  display: flex;
  justify-content: flex-end;
}

/* 空状态 */
.empty-state {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: var(--bg);
  color: var(--text-3);
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.5;
}

.empty-state h3 {
  font-size: 18px;
  margin-bottom: 8px;
  color: var(--text-2);
}

.empty-state p {
  font-size: 14px;
  text-align: center;
  max-width: 400px;
}

/* 状态标签样式 */
.status-tag {
  display: inline-block;
  padding: 4px 12px;
  border-radius: var(--radius-full);
  font-size: 12px;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

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

/* 按钮样式增强 */
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 10px 16px;
  font-size: 14px;
  font-weight: 500;
  border-radius: var(--radius-base);
  transition: all 0.3s ease;
  cursor: pointer;
  border: none;
  text-decoration: none;
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

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
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
</style>