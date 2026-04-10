<template>
  <div class="admin-luntan">
    <h2>论坛管理</h2>
    <div class="card">
      <div class="card-body">
        <!-- 搜索表单 -->
        <div class="search-section">
          <form @submit.prevent="handleSearch">
            <div class="form-row">
              <div class="form-group">
                <label for="search-title">帖子标题</label>
                <input 
                  type="text" 
                  id="search-title" 
                  v-model="searchParams.title"
                  placeholder="请输入帖子标题进行搜索"
                >
              </div>
              <div class="form-group">
                <label for="search-username">用户名</label>
                <input 
                  type="text" 
                  id="search-username" 
                  v-model="searchParams.username"
                  placeholder="请输入用户名进行搜索"
                >
              </div>
              <div class="form-group">
                <label for="search-status">状态</label>
                <select 
                  id="search-status" 
                  v-model="searchParams.status"
                >
                  <option value="">全部</option>
                  <option value="1">正常</option>
                  <option value="0">已删除</option>
                </select>
              </div>
              <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                  搜索
                </button>
                <button type="button" class="btn btn-secondary" @click="handleReset">
                  重置
                </button>
              </div>
            </div>
          </form>
        </div>
        
        <div class="table-container">
          <table class="admin-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>用户头像</th>
                <th>标题</th>
                <th>作者</th>
                <th>状态</th>
                <th>创建时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in list" :key="item.id">
                <td>{{ item.id }}</td>
                <td>
                  <img v-if="item.yonghuPhoto" :src="'http://localhost:8080/wangshangchongwudian/' + item.yonghuPhoto" alt="用户头像" style="max-width: 40px; max-height: 40px; border-radius: 50%;">
                  <span v-else>无头像</span>
                </td>
                <td>{{ item.title }}</td>
                <td>{{ item.username }}</td>
                <td>
                  <span class="status-tag" :class="getStatusClass(item.status)">
                    {{ item.status === 1 ? '正常' : '已删除' }}
                  </span>
                </td>
                <td>{{ formatDate(item.createTime) }}</td>
                <td>
                  <button class="btn btn-secondary btn-sm" @click="handleView(item)">
                    查看
                  </button>
                  <button 
                    class="btn btn-danger btn-sm" 
                    @click="handleDelete(item)"
                  >
                    删除
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        
        <!-- 分页 -->
        <div class="pagination">
          <button 
            class="btn btn-secondary" 
            @click="handlePrevPage"
            :disabled="currentPage === 1"
          >
            上一页
          </button>
          <span class="page-info">
            第 {{ currentPage }} / {{ totalPages }} 页
          </span>
          <button 
            class="btn btn-secondary" 
            @click="handleNextPage"
            :disabled="currentPage === totalPages"
          >
            下一页
          </button>
        </div>
      </div>
    </div>
    
    <!-- 详情模态框 -->
    <div class="modal" v-if="showDetailModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>帖子详情</h3>
          <button class="close-btn" @click="closeDetailModal">&times;</button>
        </div>
        <div class="modal-body">
          <div class="detail-header">
            <h4>{{ currentItem.title }}</h4>
            <div class="detail-meta">
              <div class="author-info">
                <img v-if="currentItem.yonghuPhoto" :src="'http://localhost:8080/wangshangchongwudian/' + currentItem.yonghuPhoto" alt="用户头像" style="width: 40px; height: 40px; border-radius: 50%; margin-right: 10px;">
                <span class="meta-item">作者: {{ currentItem.username }}</span>
              </div>
              <span class="meta-item">状态: 
                <span class="status-tag" :class="getStatusClass(currentItem.status)">
                  {{ currentItem.status === 1 ? '正常' : '已删除' }}
                </span>
              </span>
            </div>
          </div>
          <div class="detail-content">{{ currentItem.content }}</div>
          <div class="detail-footer">
            <span>创建时间: {{ formatDate(currentItem.createTime) }}</span>
            <span v-if="currentItem.updateTime">更新时间: {{ formatDate(currentItem.updateTime) }}</span>
          </div>
          
          <!-- 回复表单 -->
          <div class="reply-form-section">
            <h4>管理员回复</h4>
            <form @submit.prevent="handleReply">
              <div class="form-group">
                <textarea 
                  v-model="replyContent" 
                  placeholder="请输入回复内容" 
                  rows="4"
                  required
                ></textarea>
              </div>
              <div class="form-actions">
                <button type="submit" class="btn btn-primary" :disabled="isSubmittingReply">
                  {{ isSubmittingReply ? '提交中...' : '提交回复' }}
                </button>
              </div>
            </form>
          </div>
          
          <!-- 回复列表 -->
          <div class="reply-section">
            <h4>回复列表</h4>
            <div v-if="isLoadingReplies" class="loading-state">
              加载回复中...
            </div>
            <div v-else-if="replyList.length === 0" class="empty-state">
              暂无回复
            </div>
            <div v-else class="reply-list">
              <div v-for="reply in replyList" :key="reply.id" class="reply-item">
                <div class="reply-header">
                  <img v-if="reply.yonghuPhoto" :src="'http://localhost:8080/wangshangchongwudian/' + reply.yonghuPhoto" alt="用户头像" style="width: 32px; height: 32px; border-radius: 50%; margin-right: 10px;">
                  <div class="reply-author-info">
                    <span class="reply-author">{{ reply.author }}</span>
                    <span class="reply-author-type">{{ reply.authorType }}</span>
                    <span class="reply-time">{{ formatDate(reply.createTime) }}</span>
                  </div>
                  <div class="reply-actions">
                    <button class="btn btn-danger btn-sm" @click="handleDeleteReply(reply.id, reply.author)">
                      删除
                    </button>
                  </div>
                </div>
                <div class="reply-content">{{ reply.content }}</div>
              </div>
            </div>
          </div>
          
          <div class="form-actions">
            <button type="button" class="btn btn-secondary" @click="closeDetailModal">关闭</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { forumApi } from '../../utils/api'
import confirm from '../../utils/confirm'
import message from '../../utils/message'

// 列表数据
const list = ref([])
// 搜索参数
const searchParams = ref({
  title: '',
  username: '',
  status: ''
})
// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
// 加载状态
const isLoading = ref(false)
// 详情模态框
const showDetailModal = ref(false)
const currentItem = ref({})
// 回复列表
const replyList = ref([])
const isLoadingReplies = ref(false)
// 回复表单
const replyContent = ref('')
const isSubmittingReply = ref(false)

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString()
}

// 获取状态样式类
const getStatusClass = (status) => {
  return status === 1 ? 'status-active' : 'status-inactive'
}

// 获取列表数据
const getList = async () => {
  try {
    isLoading.value = true
    const params = {
      page: currentPage.value,
      limit: pageSize.value,
      sort: 'id',
      forumStateTypes: 1,
      forumDelete: 1,
      ...searchParams.value
    }
    // 调用实际的API获取数据
    const response = await forumApi.getPage(params)
    if (response.code === 0) {
      // 映射数据字段
      list.value = (response.data.list || []).map(item => ({
        id: item.id,
        title: item.forumName,
        username: item.yonghuName,
        content: item.forumContent,
        yonghuPhoto: item.yonghuPhoto,
        viewCount: 0, // 后端返回中没有浏览量字段，暂时设为0
        replyCount: 0, // 后端返回中没有回复数字段，暂时设为0
        status: item.forumStateTypes,
        createTime: item.createTime,
        updateTime: item.updateTime
      }))
      totalPages.value = response.data.totalPage || 1
    }
  } catch (error) {
    console.error('获取论坛帖子列表失败:', error)
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
    title: '',
    username: '',
    status: ''
  }
  currentPage.value = 1
  getList()
}

// 查看详情
const handleView = (item) => {
  currentItem.value = { ...item }
  showDetailModal.value = true
  // 获取回复列表
  getReplyList(item.id)
}

// 删除帖子
const handleDelete = async (item) => {
  const result = await confirm({
    message: `确定要删除帖子「${item.title}」吗？`
  })
  
  if (result) {
    try {
      isLoading.value = true
      // 调用实际的API删除数据
      const response = await forumApi.delete([item.id])
      if (response.code === 0) {
        message.success('删除成功')
        getList() // 重新获取列表数据
      }
    } catch (error) {
      console.error('删除帖子失败:', error)
      message.error('删除失败')
    } finally {
      isLoading.value = false
    }
  }
}

// 恢复帖子功能已移除，删除后帖子会直接从列表中消失

// 获取回复列表
const getReplyList = async (postId) => {
  try {
    isLoadingReplies.value = true
    const params = {
      page: 1,
      limit: 10000,
      sort: 'id',
      forumStateTypes: 2,
      superIds: postId
    }
    const response = await forumApi.getPage(params)
    if (response.code === 0) {
      // 映射回复数据
      replyList.value = (response.data.list || []).map(item => ({
        id: item.id,
        content: item.forumContent,
        author: item.yonghuName || item.uusername,
        authorType: item.yonghuName ? '用户' : '管理员',
        yonghuPhoto: item.yonghuPhoto,
        createTime: item.createTime
      }))
    }
  } catch (error) {
    console.error('获取回复列表失败:', error)
  } finally {
    isLoadingReplies.value = false
  }
}

// 删除回复
const handleDeleteReply = async (replyId, author) => {
  const result = await confirm({
    message: `确定要删除 ${author} 的回复吗？`
  })
  
  if (result) {
    try {
      isLoadingReplies.value = true
      // 调用实际的API删除回复
      const response = await forumApi.delete([replyId])
      if (response.code === 0) {
        message.success('删除成功')
        // 重新获取回复列表
        getReplyList(currentItem.value.id)
      }
    } catch (error) {
      console.error('删除回复失败:', error)
      message.error('删除失败')
    } finally {
      isLoadingReplies.value = false
    }
  }
}

// 提交回复
const handleReply = async () => {
  if (!replyContent.value.trim()) {
    message.warning('回复内容不能为空')
    return
  }
  
  try {
    isSubmittingReply.value = true
    // 调用实际的API提交回复
    const response = await forumApi.save({
      superIds: currentItem.value.id,
      forumStateTypes: 2,
      forumContent: replyContent.value
    })
    if (response.code === 0) {
      message.success('回复成功')
      // 清空回复内容
      replyContent.value = ''
      // 重新获取回复列表
      getReplyList(currentItem.value.id)
    }
  } catch (error) {
    console.error('提交回复失败:', error)
    message.error('回复失败')
  } finally {
    isSubmittingReply.value = false
  }
}

// 关闭详情模态框
const closeDetailModal = () => {
  showDetailModal.value = false
  replyList.value = [] // 清空回复列表
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
.admin-luntan {
  padding: 0;
}

h2 {
  margin-bottom: 24px;
  color: var(--text-1);
  font-size: 20px;
}

/* 主卡片容器 */
.card {
  background-color: var(--card);
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-base);
  overflow: hidden;
}

.card-body {
  padding: 24px;
}

/* 搜索区域样式 */
.search-section {
  margin-bottom: 24px;
  background-color: white;
  padding: 20px;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
}

.form-row {
  display: flex;
  align-items: flex-end;
  gap: 24px;
  flex-wrap: wrap;
}

.form-group {
  flex: 1;
  min-width: 200px;
  max-width: 300px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--text-1);
  font-size: 14px;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  transition: all 0.3s ease;
  background-color: white;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.form-actions {
  display: flex;
  gap: 12px;
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

.btn-danger {
  background-color: white;
  color: var(--danger);
  border: 1px solid var(--danger);
}

.btn-danger:hover {
  background-color: rgba(255, 77, 79, 0.1);
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-sm {
  padding: 6px 12px;
  font-size: 12px;
  margin-right: 8px;
}

/* 表格容器 */
.table-container {
  overflow-x: auto;
  margin-bottom: 24px;
  background-color: white;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
}

.admin-table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
}

.admin-table th,
.admin-table td {
  padding: 16px 20px;
  text-align: left;
  border-bottom: 1px solid var(--border);
  font-size: 14px;
}

.admin-table th {
  background-color: var(--bg);
  font-weight: 600;
  color: var(--text-1);
  font-size: 14px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.admin-table tr:last-child td {
  border-bottom: none;
}

.admin-table tr:hover {
  background-color: var(--hover-bg);
  transition: background-color 0.2s ease;
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

.status-active {
  background-color: rgba(82, 196, 26, 0.1);
  color: var(--success);
}

.status-inactive {
  background-color: rgba(144, 164, 174, 0.1);
  color: var(--text-3);
}

/* 分页样式 */
.pagination {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 16px;
  margin-top: 24px;
  font-size: 14px;
  color: var(--text-2);
}

/* 弹窗样式 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(2px);
}

.modal-content {
  background-color: var(--card);
  border-radius: var(--radius-base);
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
  width: 100%;
  max-width: 700px;
  max-height: 90vh;
  overflow-y: auto;
  animation: modalSlideIn 0.3s ease-out;
}

@keyframes modalSlideIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid var(--border);
  background-color: var(--bg);
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: var(--text-1);
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: var(--text-3);
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius-base);
  transition: all 0.3s ease;
}

.close-btn:hover {
  background-color: var(--hover-bg);
  color: var(--text-1);
}

.modal-body {
  padding: 24px;
}

.modal-body .form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
  padding: 20px 24px;
  border-top: 1px solid var(--border);
  background-color: var(--bg);
  margin: 24px -24px -24px;
  padding: 20px 24px;
}

/* 详情页样式 */
.detail-header {
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--border);
}

.detail-header h4 {
  margin: 0 0 12px 0;
  font-size: 18px;
  font-weight: 600;
  color: var(--text-1);
}

.detail-meta {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
  font-size: 14px;
  color: var(--text-2);
}

.author-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.detail-content {
  margin-bottom: 20px;
  padding: 16px;
  background-color: var(--bg);
  border-radius: var(--radius-base);
  line-height: 1.6;
  color: var(--text-1);
  min-height: 200px;
}

.detail-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid var(--border);
  font-size: 14px;
  color: var(--text-2);
}

/* 回复表单样式 */
.reply-form-section {
  margin-top: 24px;
  padding: 20px;
  background-color: var(--bg);
  border-radius: var(--radius-base);
  border: 1px solid var(--border);
}

.reply-form-section h4 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-1);
}

.reply-form-section .form-group {
  margin-bottom: 16px;
}

.reply-form-section textarea {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  font-family: inherit;
  resize: vertical;
  min-height: 100px;
  transition: all 0.3s ease;
}

.reply-form-section textarea:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.reply-form-section .form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 0;
  padding: 0;
  border-top: none;
  background-color: transparent;
  margin: 0;
  padding: 0;
}

/* 回复列表样式 */
.reply-section {
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid var(--border);
}

.reply-section h4 {
  margin: 0 0 20px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-1);
}

.loading-state,
.empty-state {
  padding: 40px 0;
  text-align: center;
  color: var(--text-3);
  font-size: 14px;
}

.reply-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.reply-item {
  padding: 16px;
  background-color: var(--bg);
  border-radius: var(--radius-base);
  border: 1px solid var(--border);
  transition: all 0.3s ease;
}

.reply-item:hover {
  box-shadow: var(--shadow-sm);
  border-color: var(--primary);
}

.reply-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}

.reply-actions {
  display: flex;
  gap: 8px;
}

.reply-author-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.reply-author {
  font-weight: 500;
  color: var(--text-1);
  font-size: 14px;
}

.reply-author-type {
  display: inline-block;
  padding: 2px 8px;
  border-radius: var(--radius-full);
  font-size: 12px;
  font-weight: 500;
  background-color: rgba(66, 184, 131, 0.1);
  color: var(--primary);
  margin-right: 8px;
}

.reply-time {
  font-size: 12px;
  color: var(--text-3);
}

.reply-content {
  line-height: 1.6;
  color: var(--text-1);
  font-size: 14px;
  word-break: break-word;
}
</style>