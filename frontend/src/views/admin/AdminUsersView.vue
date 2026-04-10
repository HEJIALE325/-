<template>
  <div class="admin-users">
    <h2>管理员管理</h2>
    <div class="card">
      <div class="card-body">
        <!-- 操作栏 -->
        <div class="action-section">
          <button class="btn btn-primary" @click="handleAdd">
            新增管理员
          </button>
        </div>
        
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
                <th>角色</th>
                <th>创建时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in adminList" :key="item.id">
                <td>{{ item.id }}</td>
                <td>{{ item.username }}</td>
                <td>{{ item.role }}</td>
                <td>{{ formatDate(item.addtime) }}</td>
                <td>
                  <button class="btn btn-secondary btn-sm" @click="handleEdit(item)">
                    编辑
                  </button>
                  <button 
                    class="btn btn-danger btn-sm" 
                    @click="handleDelete(item)"
                    :disabled="adminList.length <= 1"
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
          <span>共 {{ total }} 条记录</span>
        </div>
      </div>
    </div>
    
    <!-- 编辑弹窗 -->
    <div v-if="showEditModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <div class="modal-header">
          <h3>{{ editingAdmin.id ? '编辑管理员' : '添加管理员' }}</h3>
          <button class="modal-close" @click="closeModal">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSave">
            <div class="form-group">
              <label for="edit-username">用户名</label>
              <input 
                type="text" 
                id="edit-username" 
                v-model="editingAdmin.username" 
                required
              >
            </div>
            <div class="form-group">
              <label for="edit-password">密码</label>
              <input 
                type="password" 
                id="edit-password" 
                v-model="editingAdmin.password"
                :placeholder="editingAdmin.id ? '不修改密码请留空' : '请输入密码'"
                :required="!editingAdmin.id"
              >
            </div>
            <div class="form-group">
              <label for="edit-password-confirm">确认密码</label>
              <input 
                type="password" 
                id="edit-password-confirm" 
                v-model="editingAdmin.passwordConfirm"
                :placeholder="editingAdmin.id ? '不修改密码请留空' : '请确认密码'"
                :required="!editingAdmin.id"
              >
            </div>

            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="closeModal">
                取消
              </button>
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
import { usersApi } from '../../utils/api'
import { http } from '../../utils/request'
import message from '../../utils/message'
import confirm from '../../utils/confirm'

// 管理员列表
const adminList = ref([])
// 总数
const total = ref(0)
// 加载状态
const isLoading = ref(false)
// 编辑弹窗
const showEditModal = ref(false)
// 编辑数据
const editingAdmin = ref({
  id: '',
  username: '',
  password: '',
  passwordConfirm: ''
})

// 搜索参数
const searchParams = ref({
  page: 1,
  limit: 5,
  sort: 'id',
  username: ''
})

// 格式化日期
const formatDate = (timestamp) => {
  if (!timestamp) return ''
  const date = new Date(timestamp)
  return date.toLocaleString('zh-CN')
}

// 获取管理员列表
const getAdminList = async () => {
  try {
    isLoading.value = true
    
    // 准备查询参数
    const params = {
      page: searchParams.value.page,
      limit: searchParams.value.limit,
      sort: searchParams.value.sort
    }
    
    // 如果用户名不为空，添加到查询参数中
    if (searchParams.value.username) {
      params.username = searchParams.value.username
    }
    
    // 使用封装的API方法调用
    const response = await usersApi.getPage(params)
    if (response.code === 0) {
      adminList.value = response.data.list || []
      total.value = response.data.total || 0
    }
  } catch (error) {
    console.error('获取管理员列表失败:', error)
    message.error(error.msg || '获取管理员列表失败')
    // 确保adminList和total有默认值
    adminList.value = []
    total.value = 0
  } finally {
    isLoading.value = false
  }
}

// 处理搜索
const handleSearch = () => {
  // 重置页码为1
  searchParams.value.page = 1
  getAdminList()
}

// 处理重置
const handleReset = () => {
  // 重置搜索参数
  searchParams.value = {
    page: 1,
    limit: 5,
    sort: 'id',
    username: ''
  }
  getAdminList()
}

// 处理新增
const handleAdd = () => {
  // 重置编辑数据
  editingAdmin.value = {
    id: '',
    username: '',
    password: '',
    passwordConfirm: ''
  }
  // 打开编辑弹窗
  showEditModal.value = true
}

// 处理编辑
const handleEdit = async (admin) => {
  try {
    const response = await usersApi.getInfo(admin.id)
    if (response.code === 0) {
      editingAdmin.value = { ...response.data, password: '', passwordConfirm: '' }
      showEditModal.value = true
    }
  } catch (error) {
    // console.error('获取管理员详情失败:', error)
    message.error(error.msg || '获取管理员详情失败')
  }
}

// 处理删除
const handleDelete = async (admin) => {
  if (adminList.value.length <= 1) {
    message.warning('管理员最少保留一个')
    return
  }
  
  const result = await confirm({
    message: `确定要删除管理员 ${admin.username} 吗？`
  })
  
  if (result) {
    try {
      const response = await usersApi.delete([admin.id])
      if (response.code === 0) {
        message.success('删除成功')
        getAdminList()
      } else {
        message.error(response.msg || '删除失败')
      }
    } catch (error) {
      // console.error('删除管理员失败:', error)
      message.error(error.msg || '删除管理员失败')
    }
  }
}

// 保存编辑
const handleSave = async () => {
  try {
    // 验证密码一致性
    if (editingAdmin.value.password && editingAdmin.value.password !== editingAdmin.value.passwordConfirm) {
      message.warning('两次输入的密码不一致')
      return
    }
    
    isLoading.value = true
    const data = { ...editingAdmin.value }
    // 移除密码确认字段和空密码字段
    delete data.passwordConfirm
    if (!data.password) {
      delete data.password
    }
    
    let response
    if (data.id) {
      // 编辑
      response = await usersApi.update(data)
    } else {
      // 添加
      response = await usersApi.save(data)
    }
    
    if (response.code === 0) {
      message.success('保存成功')
      closeModal()
      getAdminList()
    } else {
      message.error(response.msg || '保存失败')
    }
  } catch (error) {
    // console.error('保存管理员失败:', error)
    // console.error(error)
    // 显示后端返回的错误消息，保留保底的默认信息
    message.error(error.msg || '保存管理员失败')
  } finally {
    isLoading.value = false
  }
}

// 关闭弹窗
const closeModal = () => {
  showEditModal.value = false
  // 重置表单
  editingAdmin.value = {
    id: '',
    username: '',
    password: '',
    passwordConfirm: ''
  }
}

// 组件挂载时获取数据
onMounted(() => {
  getAdminList()
})
</script>

<style scoped>
.admin-users {
  padding: 0;
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

/* 操作栏样式 */
.action-section {
  margin-bottom: 24px;
  display: flex;
  justify-content: flex-start;
  gap: 12px;
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

.form-group input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  transition: all 0.3s ease;
  background-color: white;
}

.form-group input:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.form-actions {
  display: flex;
  gap: 12px;
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

.btn-sm {
  padding: 6px 12px;
  font-size: 12px;
  margin-right: 8px;
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
.modal-overlay {
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

.modal {
  background-color: var(--card);
  border-radius: var(--radius-base);
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
  width: 100%;
  max-width: 480px;
  margin: 20px;
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

.modal-close {
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

.modal-close:hover {
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

.modal-body .form-group input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  transition: all 0.3s ease;
}

.modal-body .form-group input:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
  padding: 20px 24px;
  border-top: 1px solid var(--border);
  background-color: var(--bg);
}

/* 状态标签样式 */
.status-badge {
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
</style>