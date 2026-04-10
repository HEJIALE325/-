<template>
  <div class="admin-chongwuyongpin-commentback">
    <h2>商品评价管理</h2>
    <div class="card">
      <div class="card-body">
        <!-- 搜索表单 -->
        <div class="search-section">
          <form @submit.prevent="handleSearch">
            <div class="form-row">
              <div class="form-group">
                <label for="search-chongwuyongpinName">商品名称</label>
                <input 
                  type="text" 
                  id="search-chongwuyongpinName" 
                  v-model="searchParams.chongwuyongpinName"
                  placeholder="请输入商品名称进行搜索"
                >
              </div>
              <div class="form-group">
                <label for="search-yonghuName">用户姓名</label>
                <input 
              dddd    type="text" 
                  id="search-yonghuName" 
                  v-model="searchParams.yonghuName"
                  placeholder="请输入用户姓名进行搜索"
                >
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
                <th>商品名称</th>
                <th>用户姓名</th>
                <th>评价内容</th>
                <th>评价时间</th>
                <th>回复内容</th>
                <th>回复时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in list" :key="item.id">
                <td>{{ item.id }}</td>
                <td>{{ item.chongwuyongpinName }}</td>
                <td>{{ item.yonghuName }}</td>
                <td>{{ item.chongwuyongpinCommentbackText }}</td>
                <td>{{ formatDate(item.insertTime) }}</td>
                <td>{{ item.replyText || '-' }}</td>
                <td>{{ item.updateTime ? formatDate(item.updateTime) : '-' }}</td>
                <td>
                  <button class="btn btn-secondary btn-sm" @click="handleReply(item)">
                    回复
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
    
    <!-- 回复模态框 -->
    <div class="modal" v-if="showReplyModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>回复评价</h3>
          <button class="close-btn" @click="closeReplyModal">&times;</button>
        </div>
        <div class="modal-body">
          <div class="comment-info">
            <h4>{{ currentItem.chongwuyongpinName }}</h4>
            <p class="user-info">用户：{{ currentItem.yonghuName }}</p>
            <p class="comment-text">{{ currentItem.chongwuyongpinCommentbackText }}</p>
            <p class="comment-time">评价时间：{{ formatDate(currentItem.insertTime) }}</p>
          </div>
          <form @submit.prevent="handleSubmitReply">
            <div class="form-group">
              <label for="form-replyText">回复内容 *</label>
              <textarea 
                id="form-replyText" 
                v-model="formData.replyText"
                placeholder="请输入回复内容"
                rows="4"
                required
              ></textarea>
            </div>
            <div class="form-actions">
              <button type="button" class="btn btn-secondary" @click="closeReplyModal">取消</button>
              <button type="submit" class="btn btn-primary" :disabled="isLoading">
                {{ isLoading ? '保存中...' : '保存' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { chongwuyongpinCommentbackApi } from '../../utils/api'
import message from '../../utils/message'
import confirm from '../../utils/confirm'

// 列表数据
const list = ref([])
// 搜索参数
const searchParams = ref({
  chongwuyongpinName: '',
  yonghuName: ''
})
// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
// 加载状态
const isLoading = ref(false)
// 回复模态框
const showReplyModal = ref(false)
const currentItem = ref({})
const formData = ref({})

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString()
}

// 获取列表数据
const getList = async () => {
  try {
    isLoading.value = true
    const params = {
      page: currentPage.value,
      limit: pageSize.value,
      sort: 'id',
      chongwuyongpinCommentbackDelete: 1,
      ...searchParams.value
    }
    // 调用真实的API获取数据
    const response = await chongwuyongpinCommentbackApi.getPage(params)
    if (response.code === 0) {
      list.value = response.data.list || []
      totalPages.value = response.data.totalPage || 1
    } else {
      console.error('获取商品评价列表失败:', response.msg)
      list.value = []
      totalPages.value = 1
    }
  } catch (error) {
    console.error('获取商品评价列表失败:', error)
    list.value = []
    totalPages.value = 1
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
    chongwuyongpinName: '',
    yonghuName: ''
  }
  currentPage.value = 1
  getList()
}

// 回复评价
const handleReply = (item) => {
  currentItem.value = { ...item }
  formData.value = {
    replyText: item.replyText || ''
  }
  showReplyModal.value = true
}

// 删除评价
const handleDelete = async (item) => {
  // 使用全局confirm组件
  const result = await confirm({
    title: '删除确认',
    message: '确定要删除该评价吗？',
    confirmText: '确定',
    cancelText: '取消'
  })
  
  if (result) {
    try {
      isLoading.value = true
      // 调用实际的API删除数据
      const response = await chongwuyongpinCommentbackApi.delete([item.id])
      if (response.code === 0) {
        list.value = list.value.filter(i => i.id !== item.id)
        message.success('删除成功')
      } else {
        message.error(response.msg || '删除失败')
      }
    } catch (error) {
      console.error('删除评价失败:', error)
      message.error('删除失败')
    } finally {
      isLoading.value = false
    }
  }
}

// 提交回复
const handleSubmitReply = async () => {
  try {
    isLoading.value = true
    // 调用实际的API提交数据
    const updateData = {
      id: currentItem.value.id,
      replyText: formData.value.replyText
    }
    const response = await chongwuyongpinCommentbackApi.update(updateData)
    if (response.code === 0) {
      // 更新本地数据
      const index = list.value.findIndex(item => item.id === currentItem.value.id)
      if (index !== -1) {
        list.value[index].replyText = formData.value.replyText
        list.value[index].updateTime = new Date().toISOString()
      }
      closeReplyModal()
      message.success('回复成功')
    } else {
      message.error(response.msg || '回复失败')
    }
  } catch (error) {
    console.error('回复评价失败:', error)
    message.error('回复失败')
  } finally {
    isLoading.value = false
  }
}

// 关闭回复模态框
const closeReplyModal = () => {
  showReplyModal.value = false
  currentItem.value = {}
  formData.value = {}
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

// 组件挂载时获取数据
onMounted(() => {
  getList()
})
</script>

<style scoped>
/* 样式与其他管理页面保持一致 */
.admin-chongwuyongpin-commentback {
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
.form-group textarea {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  transition: all 0.3s ease;
  font-family: inherit;
  background-color: white;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
}

.form-actions {
  display: flex;
  gap: 12px;
}

/* 评论信息样式 */
.comment-info {
  margin-bottom: 24px;
  padding: 16px;
  background-color: var(--bg);
  border-radius: var(--radius-base);
}

.comment-info h4 {
  margin: 0 0 8px 0;
  color: var(--text-1);
}

.user-info {
  margin: 0 0 8px 0;
  color: var(--text-2);
  font-size: 14px;
}

.comment-text {
  margin: 0 0 8px 0;
  color: var(--text-1);
  line-height: 1.5;
}

.comment-time {
  margin: 0;
  color: var(--text-3);
  font-size: 12px;
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
  max-width: 600px;
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

.modal-body .form-group {
  margin-bottom: 20px;
  max-width: none;
}

.modal-body .form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--text-1);
  font-size: 14px;
}

.modal-body .form-group input,
.modal-body .form-group textarea {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  transition: all 0.3s ease;
}

.modal-body .form-group input:focus,
.modal-body .form-group textarea:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
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
</style>