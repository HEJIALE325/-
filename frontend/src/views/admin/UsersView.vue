<template>
  <div class="admin-users">
    <h2>用户管理</h2>
    <div class="card">
      <div class="card-body">
        <!-- 操作栏 -->
        <div class="action-section">
          <button class="btn btn-primary" @click="handleAdd">
            新增用户
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
              <div class="form-group">
                <label for="search-yonghuName">用户姓名</label>
                <input 
                  type="text" 
                  id="search-yonghuName" 
                  v-model="searchParams.yonghuName"
                  placeholder="请输入用户姓名进行搜索"
                >
              </div>
              <div class="form-group">
                <label for="search-yonghuPhone">电话</label>
                <input 
                  type="text" 
                  id="search-yonghuPhone" 
                  v-model="searchParams.yonghuPhone"
                  placeholder="请输入电话进行搜索"
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
                <th>用户姓名</th>
                <th>电话</th>
                <th>身份证号</th>
                <th>邮箱</th>
                <th>性别</th>
                <th>余额</th>
                <th>创建时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in users" :key="user.id">
                <td>{{ user.id }}</td>
                <td>{{ user.username }}</td>
                <td>{{ user.yonghuName }}</td>
                <td>{{ user.yonghuPhone }}</td>
                <td>{{ user.yonghuIdNumber }}</td>
                <td>{{ user.yonghuEmail }}</td>
                <td>{{ user.sexValue }}</td>
                <td>{{ user.newMoney }}</td>
                <td>{{ formatDate(user.createTime) }}</td>
                <td>
                  <button class="btn btn-secondary btn-sm" @click="handleEdit(user)">
                    编辑
                  </button>
                  <button 
                    class="btn btn-warning btn-sm" 
                    @click="handleResetPassword(user)"
                  >
                    重置密码
                  </button>
                  <button 
                    class="btn btn-danger btn-sm" 
                    @click="handleDelete(user)"
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
          <h3>{{ editingUser.id ? '编辑用户' : '添加用户' }}</h3>
          <button class="modal-close" @click="closeModal">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSave">
            <div class="form-row">
              <div class="form-group">
                <label for="edit-username">用户名</label>
                <input 
                  type="text" 
                  id="edit-username" 
                  v-model="editingUser.username" 
                  required
                >
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label for="edit-yonghuName">用户姓名</label>
                <input 
                  type="text" 
                  id="edit-yonghuName" 
                  v-model="editingUser.yonghuName" 
                  required
                >
              </div>
              <div class="form-group">
                <label for="edit-yonghuPhone">电话</label>
                <input 
                  type="text" 
                  id="edit-yonghuPhone" 
                  v-model="editingUser.yonghuPhone" 
                  required
                >
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label for="edit-yonghuIdNumber">身份证号</label>
                <input 
                  type="text" 
                  id="edit-yonghuIdNumber" 
                  v-model="editingUser.yonghuIdNumber" 
                >
              </div>
              <div class="form-group">
                <label for="edit-yonghuEmail">邮箱</label>
                <input 
                  type="email" 
                  id="edit-yonghuEmail" 
                  v-model="editingUser.yonghuEmail" 
                >
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label for="edit-sexTypes">性别</label>
                <select 
                  id="edit-sexTypes" 
                  v-model="editingUser.sexTypes"
                >
                  <option value="">请选择性别</option>
                  <option v-for="sex in sexTypes" :key="sex.codeIndex" :value="sex.codeIndex">
                    {{ sex.indexName }}
                  </option>
                </select>
              </div>
              <div class="form-group">
                <label for="edit-newMoney">余额</label>
                <input 
                  type="number" 
                  id="edit-newMoney" 
                  v-model="editingUser.newMoney" 
                  step="0.01"
                >
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label for="edit-yonghuPhoto">用户头像</label>
                <div class="avatar-upload-section">
                  <div class="avatar-preview" @click="$refs.avatarInput.click()">
                    <img 
                      v-if="editingUser.yonghuPhoto" 
                      :src="'http://localhost:8080/wangshangchongwudian/' + editingUser.yonghuPhoto" 
                      alt="用户头像"
                      style="max-width: 100px; max-height: 100px;"
                    >
                    <span v-else class="avatar-placeholder">点击上传头像</span>
                  </div>
                  <input 
                    type="file" 
                    id="edit-yonghuPhoto" 
                    ref="avatarInput"
                    accept="image/*"
                    @change="handleAvatarUpload"
                    style="display: none"
                  >
                </div>
              </div>
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
import { yonghuApi, fileApi } from '../../utils/api'
import message from '../../utils/message'
import confirm from '../../utils/confirm'

// 用户列表
const users = ref([])
// 总数
const total = ref(0)
// 加载状态
const isLoading = ref(false)
// 编辑弹窗
const showEditModal = ref(false)
// 性别类型选项
const sexTypes = ref([])

// 编辑数据
const editingUser = ref({
  id: '',
  username: '',
  yonghuName: '',
  yonghuPhone: '',
  yonghuIdNumber: '',
  yonghuPhoto: '',
  sexTypes: '',
  yonghuEmail: '',
  newMoney: 0,
  createTime: ''
})

// 搜索参数
const searchParams = ref({
  page: 1,
  limit: 5,
  sort: 'id',
  yonghuDelete: 1,
  username: '',
  yonghuName: '',
  yonghuPhone: ''
})

// 格式化日期
const formatDate = (timestamp) => {
  if (!timestamp) return ''
  // 处理字符串格式的时间
  if (typeof timestamp === 'string') {
    return timestamp
  }
  // 处理时间戳格式的时间
  const date = new Date(timestamp)
  return date.toLocaleString('zh-CN')
}

// 获取性别类型
const getSexTypes = async () => {
  try {
    // 这里需要调用字典API获取性别类型，暂时使用静态数据
    // 实际应该调用类似 dictionaryApi.getPage({ dicCode: 'sex_types', page: 1, limit: 100 })
    // 由于没有提供dictionaryApi，暂时使用静态数据
    sexTypes.value = [
      { id: 69, dicCode: 'sex_types', dicName: '性别类型', codeIndex: 1, indexName: '男', superId: null, beizhu: null, createTime: '2023-03-11 16:32:40' },
      { id: 70, dicCode: 'sex_types', dicName: '性别类型', codeIndex: 2, indexName: '女', superId: null, beizhu: null, createTime: '2023-03-11 16:32:40' }
    ]
  } catch (error) {
    console.error('获取性别类型失败:', error)
  }
}

// 获取用户列表
const getUsersList = async () => {
  try {
    isLoading.value = true
    
    // 准备查询参数
    const params = {
      page: searchParams.value.page,
      limit: searchParams.value.limit,
      sort: searchParams.value.sort,
      yonghuDelete: 1
    }
    
    // 添加搜索条件
    if (searchParams.value.username) params.username = searchParams.value.username
    if (searchParams.value.yonghuName) params.yonghuName = searchParams.value.yonghuName
    if (searchParams.value.yonghuPhone) params.yonghuPhone = searchParams.value.yonghuPhone
    
    // 使用封装的API方法调用
    const response = await yonghuApi.getPage(params)
    if (response.code === 0) {
      users.value = response.data.list || []
      total.value = response.data.total || 0
    }
  } catch (error) {
    console.error('获取用户列表失败:', error)
    message.error(error.msg || '获取用户列表失败')
    // 确保users和total有默认值
    users.value = []
    total.value = 0
  } finally {
    isLoading.value = false
  }
}

// 处理搜索
const handleSearch = () => {
  // 重置页码为1
  searchParams.value.page = 1
  getUsersList()
}

// 处理重置
const handleReset = () => {
  // 重置搜索参数
  searchParams.value = {
    page: 1,
    limit: 5,
    sort: 'id',
    yonghuDelete: 1,
    username: '',
    yonghuName: '',
    yonghuPhone: ''
  }
  getUsersList()
}

// 处理新增
const handleAdd = () => {
  // 重置编辑数据
  editingUser.value = {
    id: '',
    username: '',
    password: '',
    yonghuName: '',
    yonghuPhone: '',
    yonghuIdNumber: '',
    yonghuPhoto: '',
    sexTypes: '',
    yonghuEmail: '',
    newMoney: 0,
    createTime: ''
  }
  // 打开编辑弹窗
  showEditModal.value = true
}

// 处理编辑
const handleEdit = async (user) => {
  try {
    const response = await yonghuApi.getInfo(user.id)
    if (response.code === 0) {
      editingUser.value = { ...response.data, password: '' }
      showEditModal.value = true
    }
  } catch (error) {
    console.error('获取用户详情失败:', error)
    message.error(error.msg || '获取用户详情失败')
  }
}

// 处理头像上传
const handleAvatarUpload = async (event) => {
  const file = event.target.files[0]
  if (!file) return
  
  try {
    isLoading.value = true
    const response = await fileApi.upload('yonghuPhoto', file)
    if (response.code === 0) {
      editingUser.value.yonghuPhoto = 'upload/' + response.file
      message.success('头像上传成功')
    } else {
      message.error(response.msg || '上传失败')
    }
  } catch (error) {
    console.error('上传头像失败:', error)
    message.error(error.msg || '上传头像失败')
  } finally {
    isLoading.value = false
    // 清空文件输入，以便可以重新选择相同的文件
    event.target.value = ''
  }
}

// 处理删除
const handleDelete = async (user) => {
  const result = await confirm({
    message: `确定要删除用户 ${user.username} 吗？`
  })
  
  if (result) {
    try {
      const response = await yonghuApi.delete([user.id])
      if (response.code === 0) {
        message.success('删除成功')
        getUsersList()
      } else {
        message.error(response.msg || '删除失败')
      }
    } catch (error) {
      console.error('删除用户失败:', error)
      message.error(error.msg || '删除用户失败')
    }
  }
}

// 处理密码重置
const handleResetPassword = async (user) => {
  const result = await confirm({
    message: `确定要重置用户 ${user.username} 的密码吗？`
  })
  
  if (result) {
    try {
      const response = await yonghuApi.resetPassword(user.id)
      if (response.code === 0) {
        message.success('密码重置成功')
        getUsersList()
      } else {
        message.error(response.msg || '密码重置失败')
      }
    } catch (error) {
      console.error('密码重置失败:', error)
      message.error(error.msg || '密码重置失败')
    }
  }
}

// 保存编辑
const handleSave = async () => {
  try {
    isLoading.value = true
    const data = { ...editingUser.value }
    
    let response
    if (data.id) {
      // 编辑
      response = await yonghuApi.update(data)
    } else {
      // 添加
      response = await yonghuApi.save(data)
    }
    
    if (response.code === 0) {
      message.success('保存成功')
      closeModal()
      getUsersList()
    } else {
      message.error(response.msg || '保存失败')
    }
  } catch (error) {
    console.error('保存用户失败:', error)
    message.error(error.msg || '保存用户失败')
  } finally {
    isLoading.value = false
  }
}

// 关闭弹窗
const closeModal = () => {
  showEditModal.value = false
  // 重置表单
  editingUser.value = {
    id: '',
    username: '',
    yonghuName: '',
    yonghuPhone: '',
    yonghuIdNumber: '',
    yonghuPhoto: '',
    sexTypes: '',
    yonghuEmail: '',
    newMoney: 0,
    createTime: ''
  }
}

// 组件挂载时获取数据
onMounted(async () => {
  // 获取性别类型
  await getSexTypes()
  // 获取用户列表
  getUsersList()
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

.btn-warning {
  background-color: white;
  color: #ff9800;
  border: 1px solid #ff9800;
}

.btn-warning:hover {
  background-color: rgba(255, 152, 0, 0.1);
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
  padding: 20px;
}

.modal {
  background-color: var(--card);
  border-radius: var(--radius-base);
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
  width: 100%;
  max-width: 800px;
  max-height: 90vh;
  margin: 0;
  animation: modalSlideIn 0.3s ease-out;
  display: flex;
  flex-direction: column;
  overflow: hidden;
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
  flex-shrink: 0;
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
  flex: 1;
  overflow-y: auto;
  max-height: calc(90vh - 120px);
}

.modal-body .form-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.modal-body .form-group {
  margin-bottom: 20px;
  max-width: none;
  flex: 1;
  min-width: 0;
}

.modal-body .form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--text-1);
  font-size: 14px;
}

.modal-body .form-group input,
.modal-body .form-group select {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  transition: all 0.3s ease;
  box-sizing: border-box;
}

.modal-body .form-group input:focus,
.modal-body .form-group select:focus {
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
  flex-shrink: 0;
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

/* 头像上传样式 */
.avatar-upload-section {
  margin-top: 8px;
}

.avatar-preview {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 2px dashed var(--border);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  overflow: hidden;
  background-color: var(--bg);
  transition: all 0.3s ease;
}

.avatar-preview:hover {
  border-color: var(--primary);
  background-color: rgba(66, 184, 131, 0.05);
}

.avatar-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  color: var(--text-3);
  font-size: 14px;
  text-align: center;
  padding: 0 16px;
}
</style>