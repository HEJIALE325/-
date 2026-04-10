<template>
  <div class="admin-customer-service">
    <h2>客服聊天管理</h2>
    <div class="card">
      <div class="card-body">
        <!-- 搜索表单 -->
        <div class="search-section">
          <form @submit.prevent="handleSearch">
            <div class="form-row">
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
                  <option value="pending">待处理</option>
                  <option value="processing">处理中</option>
                  <option value="completed">已完成</option>
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
                <th>用户名</th>
                <th>用户名称</th>
                <th>联系方式</th>
                <th>咨询内容</th>
                <th>状态</th>
                <th>创建时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in list" :key="item.id">
                <td>{{ item.id }}</td>
                <td>{{ item.username }}</td>
                <td>{{ item.yonghuName }}</td>
                <td>{{ item.yonghuPhone }}</td>
                <td>{{ item.content }}</td>
                <td>
                  <span class="status-tag" :class="getStatusClass(item.status)">
                    {{ getStatusText(item.status) }}
                  </span>
                </td>
                <td>{{ formatDate(item.createTime) }}</td>
                <td>
                  <button class="btn btn-secondary btn-sm" @click="handleView(item)">
                    查看
                  </button>
                  <button 
                    class="btn btn-primary btn-sm" 
                    @click="handleProcess(item)"
                    v-if="item.status !== 'completed'"
                  >
                    处理
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
          <h3>聊天详情</h3>
          <button class="close-btn" @click="closeDetailModal">&times;</button>
        </div>
        <div class="modal-body">
          <div class="detail-item">
            <label>ID：</label>
            <span>{{ currentItem.id }}</span>
          </div>
          <div class="detail-item">
            <label>用户名：</label>
            <span>{{ currentItem.username }}</span>
          </div>
          <div class="detail-item">
            <label>用户名称：</label>
            <span>{{ currentItem.yonghuName }}</span>
          </div>
          <div class="detail-item">
            <label>联系方式：</label>
            <span>{{ currentItem.yonghuPhone }}</span>
          </div>
          <div class="detail-item">
            <label>咨询内容：</label>
            <span>{{ currentItem.content }}</span>
          </div>
          <div class="detail-item">
            <label>状态：</label>
            <span class="status-tag" :class="getStatusClass(currentItem.status)">
              {{ getStatusText(currentItem.status) }}
            </span>
          </div>
          <div class="detail-item" v-if="currentItem.response">
            <label>回复内容：</label>
            <span>{{ currentItem.response }}</span>
          </div>
          <div class="detail-item">
            <label>创建时间：</label>
            <span>{{ formatDate(currentItem.createTime) }}</span>
          </div>
          <div class="detail-item" v-if="currentItem.updateTime">
            <label>更新时间：</label>
            <span>{{ formatDate(currentItem.updateTime) }}</span>
          </div>
          <div class="form-actions">
            <button type="button" class="btn btn-secondary" @click="closeDetailModal">关闭</button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 处理模态框 -->
    <div class="modal" v-if="showProcessModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>处理咨询</h3>
          <button class="close-btn" @click="closeProcessModal">&times;</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="form-group">
              <label>咨询内容</label>
              <div class="consult-content">{{ currentItem.content }}</div>
            </div>
            <div class="form-group">
              <label for="form-response">回复内容</label>
              <textarea 
                id="form-response" 
                v-model="formData.response"
                placeholder="请输入回复内容"
                rows="4"
                required
              ></textarea>
            </div>
            <div class="form-actions">
              <button type="button" class="btn btn-secondary" @click="closeProcessModal">取消</button>
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
// 详情模态框
const showDetailModal = ref(false)
const currentItem = ref({})
// 处理模态框
const showProcessModal = ref(false)
const formData = ref({})

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString()
}

// 获取状态样式类
const getStatusClass = (status) => {
  switch (status) {
    case 'pending':
      return 'status-pending'
    case 'processing':
      return 'status-processing'
    case 'completed':
      return 'status-completed'
    default:
      return ''
  }
}

// 获取状态文本
const getStatusText = (status) => {
  switch (status) {
    case 'pending':
      return '待处理'
    case 'processing':
      return '处理中'
    case 'completed':
      return '已完成'
    default:
      return status
  }
}

// 获取列表数据
const getList = async () => {
  try {
    isLoading.value = true
    // 这里需要调用实际的API获取数据
    // 暂时使用模拟数据
    list.value = [
      {
        id: 1,
        username: 'user1',
        yonghuName: '张三',
        yonghuPhone: '13800138000',
        content: '咨询宠物寄养服务',
        status: 'pending',
        createTime: new Date().toISOString()
      },
      {
        id: 2,
        username: 'user2',
        yonghuName: '李四',
        yonghuPhone: '13900139000',
        content: '咨询宠物用品',
        status: 'completed',
        response: '已回复咨询',
        createTime: new Date().toISOString(),
        updateTime: new Date().toISOString()
      }
    ]
    totalPages.value = 1
  } catch (error) {
    console.error('获取客服聊天列表失败:', error)
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

// 查看详情
const handleView = (item) => {
  currentItem.value = { ...item }
  showDetailModal.value = true
}

// 处理咨询
const handleProcess = (item) => {
  currentItem.value = { ...item }
  formData.value = {
    response: item.response || ''
  }
  showProcessModal.value = true
}

// 提交回复
const handleSubmit = async () => {
  try {
    isLoading.value = true
    // 这里需要调用实际的API提交数据
    // 暂时模拟成功
    currentItem.value.response = formData.value.response
    currentItem.value.status = 'completed'
    currentItem.value.updateTime = new Date().toISOString()
    // 更新列表数据
    const index = list.value.findIndex(item => item.id === currentItem.value.id)
    if (index !== -1) {
      list.value[index] = { ...currentItem.value }
    }
    closeProcessModal()
    alert('处理成功')
  } catch (error) {
    console.error('处理咨询失败:', error)
    alert('处理失败')
  } finally {
    isLoading.value = false
  }
}

// 关闭详情模态框
const closeDetailModal = () => {
  showDetailModal.value = false
}

// 关闭处理模态框
const closeProcessModal = () => {
  showProcessModal.value = false
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
.form-group select,
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
.form-group select:focus,
.form-group textarea:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
}

.consult-content {
  padding: 12px 16px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  background-color: var(--bg);
  min-height: 60px;
  white-space: pre-wrap;
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
.modal-body .form-group select,
.modal-body .form-group textarea {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  transition: all 0.3s ease;
}

.modal-body .form-group input:focus,
.modal-body .form-group select:focus,
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

/* 详情项样式 */
.detail-item {
  margin-bottom: 20px;
  display: flex;
  gap: 12px;
  align-items: flex-start;
}

.detail-item label {
  width: 120px;
  font-weight: 500;
  color: var(--text-1);
  flex-shrink: 0;
  font-size: 14px;
}

.detail-item span {
  flex: 1;
  color: var(--text-2);
  font-size: 14px;
}
</style>