<template>
  <div class="admin-chongwujiyang">
    <h2>宠物寄养管理</h2>
    <div class="card">
      <div class="card-body">
        <!-- 操作栏 -->
        <div class="action-section">
          <button class="btn btn-primary" @click="handleAdd">
            新增寄养
          </button>
        </div>
        
        <!-- 搜索表单 -->
        <div class="search-section">
          <form @submit.prevent="handleSearch">
            <div class="form-row">
              <div class="form-group">
                <label for="search-name">寄养名称</label>
                <input 
                  type="text" 
                  id="search-name" 
                  v-model="searchParams.chongwujiyangName"
                  placeholder="请输入寄养名称进行搜索"
                >
              </div>
              <div class="form-group">
                <label for="search-type">寄养类型</label>
                <select 
                  id="search-type" 
                  v-model="searchParams.chongwujiyangTypes"
                >
                  <option value="">全部</option>
                  <option v-for="type in typeOptions" :key="type.codeIndex" :value="type.codeIndex">
                    {{ type.indexName }}
                  </option>
                </select>
              </div>
              <div class="form-group">
                <label for="search-status">状态</label>
                <select 
                  id="search-status" 
                  v-model="searchParams.chongwujiyangDelete"
                >
                  <option value="">全部</option>
                  <option value="1">启用</option>
                  <option value="0">禁用</option>
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
                <th>图片</th>
                <th>寄养名称</th>
                <th>寄养类型</th>
                <th>价格</th>
                <th>点击量</th>
                <th>状态</th>
                <th>创建时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in list" :key="item.id">
                <td>{{ item.id }}</td>
                <td>
                  <img v-if="item.chongwujiyangPhoto" :src="'http://localhost:8080/wangshangchongwudian/' + item.chongwujiyangPhoto" alt="寄养服务图片" style="max-width: 60px; max-height: 60px;">
                  <span v-else>无图片</span>
                </td>
                <td>{{ item.chongwujiyangName }}</td>
                <td>
                  <span class="type-tag">
                    {{ item.chongwujiyangValue }}
                  </span>
                </td>
                <td>{{ item.chongwujiyangNewMoney }}</td>
                <td>{{ item.chongwujiyangClicknum }}</td>
                <td>
                  <span class="status-tag" :class="getStatusClass(item.chongwujiyangDelete)">
                    {{ item.chongwujiyangDelete === 1 ? '启用' : '禁用' }}
                  </span>
                </td>
                <td>{{ formatDate(item.createTime) }}</td>
                <td>
                  <button class="btn btn-secondary btn-sm" @click="handleEdit(item)">
                    编辑
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
    
    <!-- 新增/编辑模态框 -->
    <div class="modal" v-if="showModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>{{ isEdit ? '编辑寄养' : '新增寄养' }}</h3>
          <button class="close-btn" @click="closeModal">&times;</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="form-group">
              <label for="form-photo">寄养图片</label>
              <div class="avatar-upload-section">
                <div class="avatar-preview" @click="$refs.photoInput.click()">
                  <img v-if="formData.chongwujiyangPhoto" :src="'http://localhost:8080/wangshangchongwudian/' + formData.chongwujiyangPhoto" alt="寄养服务图片" style="max-width: 100px; max-height: 100px;">
                  <span v-else class="avatar-placeholder">点击上传图片</span>
                </div>
                <input 
                  type="file" 
                  id="form-photo" 
                  ref="photoInput"
                  accept="image/*"
                  @change="handlePhotoUpload"
                  style="display: none"
                >
              </div>
            </div>
            <div class="form-group">
              <label for="form-name">寄养名称 *</label>
              <input 
                type="text" 
                id="form-name" 
                v-model="formData.chongwujiyangName"
                placeholder="请输入寄养名称"
                required
              >
            </div>
            <div class="form-group">
              <label for="form-type">寄养类型 *</label>
              <select 
                id="form-type" 
                v-model="formData.chongwujiyangTypes"
                required
              >
                <option value="">请选择寄养类型</option>
                <option v-for="type in typeOptions" :key="type.codeIndex" :value="type.codeIndex">
                  {{ type.indexName }}
                </option>
              </select>
            </div>
            <div class="form-group">
              <label for="form-price">价格 *</label>
              <input 
                type="number" 
                id="form-price" 
                v-model="formData.chongwujiyangNewMoney"
                placeholder="请输入价格"
                step="0.01"
                required
              >
            </div>
            <div class="form-group">
              <label for="form-content">寄养内容</label>
              <textarea 
                id="form-content" 
                v-model="formData.chongwujiyangContent"
                placeholder="请输入寄养内容"
                rows="4"
              ></textarea>
            </div>
            <div class="form-group">
              <label for="form-status">状态</label>
              <select 
                id="form-status" 
                v-model="formData.chongwujiyangDelete"
              >
                <option value="1">启用</option>
                <option value="0">禁用</option>
              </select>
            </div>
            <div class="form-actions">
              <button type="button" class="btn btn-secondary" @click="closeModal">取消</button>
              <button type="submit" class="btn btn-primary">保存</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { chongwujiyangApi, dictionaryApi, fileApi } from '../../utils/api'
import confirm from '../../utils/confirm'
import message from '../../utils/message'

// 列表数据
const list = ref([])
// 寄养类型选项
const typeOptions = ref([])
// 加载状态
const loading = ref(false)
const loadingTypes = ref(false)
// 搜索参数
const searchParams = ref({
  chongwujiyangName: '',
  chongwujiyangTypes: '',
  chongwujiyangDelete: ''
})
// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
// 模态框
const showModal = ref(false)
const isEdit = ref(false)
const formData = ref({})

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

// 获取寄养类型选项
const getTypeOptions = async () => {
  loadingTypes.value = true
  try {
    const response = await dictionaryApi.getPage({
      dicCode: 'chongwujiyang_types',
      page: 1,
      limit: 100
    })
    if (response.code === 0) {
      // 按codeIndex升序排序
      const sortedList = (response.data.list || []).sort((a, b) => a.codeIndex - b.codeIndex)
      typeOptions.value = sortedList
    }
  } catch (error) {
    console.error('获取寄养类型选项失败:', error)
    message.error('获取寄养类型失败，请稍后重试')
  } finally {
    loadingTypes.value = false
  }
}

// 获取列表数据
const getList = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      ...searchParams.value
    }
    const response = await chongwujiyangApi.getPage(params)
    if (response.code === 0) {
      list.value = response.data.list || []
      totalPages.value = response.data.totalPage || 1
    }
  } catch (error) {
    console.error('获取宠物寄养列表失败:', error)
    message.error('获取寄养列表失败，请稍后重试')
  } finally {
    loading.value = false
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
    chongwujiyangName: '',
    chongwujiyangTypes: '',
    chongwujiyangDelete: ''
  }
  currentPage.value = 1
  getList()
}

// 新增
const handleAdd = () => {
  isEdit.value = false
  formData.value = {
    chongwujiyangName: '',
    chongwujiyangPhoto: '',
    chongwujiyangTypes: '',
    chongwujiyangNewMoney: 0,
    chongwujiyangContent: '',
    chongwujiyangDelete: 1
  }
  showModal.value = true
}

// 编辑
const handleEdit = (item) => {
  isEdit.value = true
  formData.value = { ...item }
  showModal.value = true
}

// 删除
const handleDelete = async (item) => {
  const result = await confirm({
    message: `确定要删除寄养「${item.chongwujiyangName}」吗？`
  })
  
  if (result) {
    try {
      const response = await chongwujiyangApi.delete([item.id])
      if (response.code === 0) {
        message.success('删除成功')
        getList()
      }
    } catch (error) {
      console.error('删除宠物寄养失败:', error)
      message.error('删除失败')
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    let response
    if (isEdit.value) {
      response = await chongwujiyangApi.update(formData.value)
    } else {
      response = await chongwujiyangApi.save(formData.value)
    }
    if (response.code === 0) {
      message.success(isEdit.value ? '编辑成功' : '新增成功')
      closeModal()
      getList()
    }
  } catch (error) {
    console.error(isEdit.value ? '编辑宠物寄养失败:' : '新增宠物寄养失败:', error)
    message.error(isEdit.value ? '编辑失败' : '新增失败')
  }
}

// 处理图片上传
const handlePhotoUpload = async (event) => {
  const file = event.target.files[0]
  if (!file) return
  
  try {
    loading.value = true
    const response = await fileApi.upload('chongwujiyang', file)
    if (response.code === 0) {
      formData.value.chongwujiyangPhoto = 'upload/' + response.file
      message.success('图片上传成功')
    } else {
      message.error(response.msg || '上传失败')
    }
  } catch (error) {
    console.error('上传图片失败:', error)
    message.error(error.msg || '上传图片失败')
  } finally {
    loading.value = false
    // 清空文件输入，以便可以重新选择相同的文件
    event.target.value = ''
  }
}

// 关闭模态框
const closeModal = () => {
  showModal.value = false
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

// 组件挂载时获取列表数据和类型选项
onMounted(async () => {
  await getTypeOptions()
  getList()
})
</script>

<style scoped>
/* 样式与其他管理页面保持一致 */
.admin-chongwujiyang {
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
  max-width: 500px;
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

/* 操作栏样式 */
.type-tag {
  display: inline-block;
  padding: 4px 12px;
  border-radius: var(--radius-full);
  font-size: 12px;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  background-color: rgba(66, 184, 131, 0.1);
  color: var(--primary);
}

/* 图片上传样式 */
.avatar-upload-section {
  margin-top: 8px;
}

.avatar-preview {
  width: 120px;
  height: 120px;
  border: 2px dashed var(--border);
  border-radius: var(--radius-base);
  overflow: hidden;
  cursor: pointer;
  position: relative;
  transition: all 0.3s ease;
  background-color: var(--bg);
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-preview:hover {
  border-color: var(--primary);
  background-color: rgba(66, 184, 131, 0.05);
  transform: translateY(-2px);
  box-shadow: var(--shadow-sm);
}

.avatar-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.avatar-preview:hover img {
  transform: scale(1.02);
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--bg);
  color: var(--text-3);
  font-size: 14px;
  font-weight: 500;
  text-align: center;
  padding: 20px;
  box-sizing: border-box;
  transition: all 0.3s ease;
}

.avatar-preview:hover .avatar-placeholder {
  color: var(--primary);
}
</style>