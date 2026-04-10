<template>
  <div class="admin-lunbotu">
    <h2>轮播图信息</h2>
    <div class="card">
      <div class="card-body">
        <!-- 操作栏 -->
        <div class="action-section">
          <button class="btn btn-primary" @click="handleAdd">
            新增轮播图
          </button>
        </div>
        

        
        <div class="table-container">
          <table class="admin-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>图片</th>
                <th>标题</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in list" :key="item.id">
                <td>{{ item.id }}</td>
                <td class="image-cell">
                  <img :src="'http://localhost:8080/wangshangchongwudian/' + item.imageUrl" :alt="item.title" style="max-width: 100px; max-height: 100px;">
                </td>
                <td>{{ item.title }}</td>
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
          <h3>{{ isEdit ? '编辑轮播图' : '新增轮播图' }}</h3>
          <button class="close-btn" @click="closeModal">&times;</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="form-group">
              <label for="form-image">图片 *</label>
              <div class="image-upload-section">
                <div class="image-preview" @click="$refs.imageInput.click()">
                  <img 
                    v-if="formData.imageUrl" 
                    :src="'http://localhost:8080/wangshangchongwudian/' + formData.imageUrl" 
                    alt="轮播图预览"
                    style="max-width: 100px; max-height: 100px;"
                  >
                  <span v-else class="image-placeholder">点击上传轮播图</span>
                </div>
                <input 
                  type="file" 
                  id="form-image" 
                  ref="imageInput"
                  accept="image/*"
                  @change="handleImageUpload"
                  style="display: none"
                >
              </div>
            </div>
            <div class="form-group">
              <label for="form-title">标题 *</label>
              <input 
                type="text" 
                id="form-title" 
                v-model="formData.title"
                placeholder="请输入轮播图标题"
                required
              >
            </div>
            <div class="form-actions">
              <button type="button" class="btn btn-secondary" @click="closeModal">取消</button>
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
import confirm from '../../utils/confirm'
import message from '../../utils/message'
import { fileApi, configApi } from '../../utils/api'

// 列表数据
const list = ref([])

// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
// 加载状态
const isLoading = ref(false)
// 编辑模态框
const showModal = ref(false)
const isEdit = ref(false)
const formData = ref({})





// 获取列表数据
const getList = async () => {
  try {
    isLoading.value = true
    const params = {
      page: currentPage.value,
      limit: pageSize.value,
      sort: 'id'
    }
    // 调用实际的API获取数据
    const response = await configApi.getPage(params)
    if (response.code === 0) {
      // 映射数据字段
      list.value = (response.data.list || []).map(item => ({
        id: item.id,
        imageUrl: item.value,
        title: item.name
      }))
      totalPages.value = response.data.totalPage || 1
    }
  } catch (error) {
    console.error('获取轮播图列表失败:', error)
  } finally {
    isLoading.value = false
  }
}



// 新增轮播图
const handleAdd = () => {
  isEdit.value = false
  formData.value = {
    imageUrl: '',
    title: ''
  }
  showModal.value = true
}

// 编辑轮播图
const handleEdit = (item) => {
  isEdit.value = true
  formData.value = {
    id: item.id,
    imageUrl: item.imageUrl,
    title: item.title
  }
  showModal.value = true
}

// 删除轮播图
const handleDelete = async (item) => {
  const result = await confirm({
    message: `确定要删除轮播图「${item.title}」吗？`
  })
  
  if (result) {
    try {
      isLoading.value = true
      // 这里需要调用实际的API删除数据
      // 暂时模拟成功
      list.value = list.value.filter(i => i.id !== item.id)
      message.success('删除成功')
    } catch (error) {
      console.error('删除轮播图失败:', error)
      message.error('删除失败')
    } finally {
      isLoading.value = false
    }
  }
}



// 处理图片上传
const handleImageUpload = async (event) => {
  const file = event.target.files[0]
  if (!file) return
  
  try {
    isLoading.value = true
    // 调用实际的文件上传API
    const response = await fileApi.upload('lunbotu', file)
    if (response.code === 0) {
      formData.value.imageUrl = 'upload/' + response.file
      message.success('图片上传成功')
    } else {
      message.error(response.msg || '上传失败')
    }
  } catch (error) {
    console.error('上传图片失败:', error)
    message.error('上传失败')
  } finally {
    isLoading.value = false
    // 清空文件输入，以便可以重新选择相同的文件
    event.target.value = ''
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    isLoading.value = true
    // 这里需要调用实际的API提交数据
    // 暂时模拟成功
    if (isEdit.value) {
      // 编辑现有数据
      const index = list.value.findIndex(item => item.id === formData.value.id)
      if (index !== -1) {
        list.value[index] = { 
          ...formData.value,
          updateTime: new Date().toISOString()
        }
      }
    } else {
      // 添加新数据
      const newItem = {
        id: list.value.length + 1,
        ...formData.value,
        createTime: new Date().toISOString(),
        updateTime: new Date().toISOString()
      }
      list.value.push(newItem)
    }
    closeModal()
    message.success(isEdit.value ? '编辑成功' : '新增成功')
  } catch (error) {
    console.error(isEdit.value ? '编辑轮播图失败:' : '新增轮播图失败:', error)
    message.error(isEdit.value ? '编辑失败' : '新增失败')
  } finally {
    isLoading.value = false
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

// 组件挂载时获取列表数据
onMounted(() => {
  getList()
})
</script>

<style scoped>
/* 样式与其他管理页面保持一致 */
.admin-lunbotu {
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

/* 图片单元格样式 */
.image-cell {
  width: 120px;
}

.image-thumbnail {
  width: 100px;
  height: 60px;
  object-fit: cover;
  border-radius: var(--radius-base);
  border: 1px solid var(--border);
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

/* 图片上传区域样式 */
.image-upload-section {
  margin-top: 8px;
}

.image-preview {
  width: 100%;
  height: 200px;
  border: 2px dashed var(--border);
  border-radius: var(--radius-base);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  overflow: hidden;
  background-color: var(--bg);
  transition: all 0.3s ease;
}

.image-preview:hover {
  border-color: var(--primary);
  background-color: rgba(66, 184, 131, 0.05);
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-placeholder {
  color: var(--text-3);
  font-size: 14px;
  text-align: center;
  padding: 0 16px;
}
</style>