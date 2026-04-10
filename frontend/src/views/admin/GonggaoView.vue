<template>
  <div class="admin-gonggao">
    <h2>公告信息管理</h2>
    <div class="card">
      <div class="card-body">
        <!-- 操作栏 -->
        <div class="action-section">
          <button class="btn btn-primary" @click="handleAdd">
            新增公告
          </button>
        </div>
        
        <!-- 搜索表单 -->
        <div class="search-section">
          <form @submit.prevent="handleSearch" class="search-form">
              <div class="form-row">
                <div class="form-group">
                  <label for="search-newsName">标题</label>
                  <input 
                    type="text" 
                    id="search-newsName" 
                    v-model="searchParams.newsName"
                    placeholder="请输入公告标题进行搜索"
                  >
                </div>
                <div class="form-group">
                  <label for="search-newsTypes">公告类型</label>
                  <select 
                    id="search-newsTypes" 
                    v-model="searchParams.newsTypes"
                  >
                    <option value="">全部</option>
                    <option v-for="type in newsTypeOptions" :key="type.id" :value="type.codeIndex">
                      {{ type.indexName }}
                    </option>
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
                <th>标题</th>
                <th>类型</th>
                <th>发布时间</th>
                <th>创建时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in list" :key="item.id">
                <td>{{ item.id }}</td>
                <td>
                  <img v-if="item.newsPhoto" :src="'http://localhost:8080/wangshangchongwudian/' + item.newsPhoto" alt="公告图片" style="max-width: 60px; max-height: 60px;">
                  <span v-else>无图片</span>
                </td>
                <td>{{ item.newsName }}</td>
                <td>
                  <span class="type-tag">
                    {{ item.newsValue || '未知类型' }}
                  </span>
                </td>
                <td>{{ formatDate(item.publishTime) }}</td>
                <td>{{ formatDate(item.createTime) }}</td>
                <td>
                  <button class="btn btn-secondary btn-sm" @click="handleView(item)">
                    查看
                  </button>
                  <button class="btn btn-primary btn-sm" @click="handleEdit(item)">
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
          <h3>{{ isEdit ? '编辑公告' : '新增公告' }}</h3>
          <button class="close-btn" @click="closeModal">&times;</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="form-group">
              <label for="form-photo">公告图片</label>
              <div class="avatar-upload-section">
                <div class="avatar-preview" @click="$refs.photoInput.click()">
                  <img v-if="formData.newsPhoto" :src="'http://localhost:8080/wangshangchongwudian/' + formData.newsPhoto" alt="公告图片" style="max-width: 100px; max-height: 100px;">
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
              <label for="form-newsName">公告标题 *</label>
              <input 
                type="text" 
                id="form-newsName" 
                v-model="formData.newsName"
                placeholder="请输入公告标题"
                required
              >
            </div>
            <div class="form-group">
              <label for="form-type">公告类型 *</label>
              <select 
                id="form-type" 
                v-model="formData.newsTypes"
                required
              >
                <option value="">请选择公告类型</option>
                <option v-for="type in newsTypeOptions" :key="type.id" :value="type.codeIndex">
                  {{ type.indexName }}
                </option>
              </select>
            </div>
            <div class="form-group">
              <label for="form-content">公告内容 *</label>
              <textarea 
                id="form-content" 
                v-model="formData.content"
                placeholder="请输入公告内容"
                rows="6"
                required
              ></textarea>
            </div>

            <div class="form-group">
              <label for="form-publishTime">发布时间</label>
              <input 
                type="datetime-local" 
                id="form-publishTime" 
                v-model="formData.publishTime"
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
    
    <!-- 详情模态框 -->
    <div class="modal" v-if="showDetailModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>公告详情</h3>
          <button class="close-btn" @click="closeDetailModal">&times;</button>
        </div>
        <div class="modal-body">
          <div class="detail-header">
            <h4>{{ currentItem.newsName }}</h4>
            <div class="detail-meta">
              <span class="meta-item">类型: {{ currentItem.newsValue || '未知类型' }}</span>
              <span class="meta-item">发布时间: {{ formatDate(currentItem.publishTime) }}</span>
              <span class="meta-item">创建时间: {{ formatDate(currentItem.createTime) }}</span>
              <span v-if="currentItem.updateTime" class="meta-item">更新时间: {{ formatDate(currentItem.updateTime) }}</span>
            </div>
          </div>
          <div v-if="currentItem.newsPhoto" class="detail-photo">
            <img :src="'http://localhost:8080/wangshangchongwudian/' + currentItem.newsPhoto" alt="公告图片" style="max-width: 100%; max-height: 300px;">
          </div>
          <div class="detail-content">{{ currentItem.content }}</div>
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
import { newsApi, dictionaryApi, fileApi } from '../../utils/api'
import confirm from '../../utils/confirm'
import message from '../../utils/message'

// 列表数据
const list = ref([])
// 公告类型选项
const newsTypeOptions = ref([])
// 搜索参数
const searchParams = ref({
  newsName: '',
  newsTypes: ''
})
// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
// 加载状态
const isLoading = ref(false)
const isLoadingTypes = ref(false)
// 编辑模态框
const showModal = ref(false)
const isEdit = ref(false)
const formData = ref({})
// 详情模态框
const showDetailModal = ref(false)
const currentItem = ref({})

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString()
}



// 获取公告类型选项
const getNewsTypeOptions = async () => {
  try {
    isLoadingTypes.value = true
    const response = await dictionaryApi.getPage({
      dicCode: 'news_types',
      page: 1,
      limit: 100
    })
    if (response.code === 0) {
      // 按codeIndex升序排序
      const sortedList = (response.data.list || []).sort((a, b) => a.codeIndex - b.codeIndex)
      newsTypeOptions.value = sortedList
    }
  } catch (error) {
    console.error('获取公告类型选项失败:', error)
    message.error('获取公告类型失败，请稍后重试')
  } finally {
    isLoadingTypes.value = false
  }
}

// 获取列表数据
const getList = async () => {
  try {
    isLoading.value = true
    const params = {
      page: currentPage.value,
      limit: pageSize.value,
      sort: 'id',
      newsDelete: 1,
      ...searchParams.value
    }
    // 调用实际的API获取数据
    const response = await newsApi.getPage(params)
    if (response.code === 0) {
      // 映射数据字段
      list.value = (response.data.list || []).map(item => ({
        id: item.id,
        newsName: item.newsName,
        content: item.newsContent,
        newsTypes: item.newsTypes,
        newsPhoto: item.newsPhoto,
        publishTime: item.insertTime,
        createTime: item.createTime,
        newsValue: item.newsValue
      }))
      totalPages.value = response.data.totalPage || 1
    }
  } catch (error) {
    console.error('获取公告列表失败:', error)
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
    newsName: '',
    newsTypes: ''
  }
  currentPage.value = 1
  getList()
}

// 新增公告
const handleAdd = () => {
  isEdit.value = false
  formData.value = {
    newsName: '',
    content: '',
    newsTypes: '',
    newsPhoto: '',
    publishTime: ''
  }
  showModal.value = true
}

// 编辑公告
const handleEdit = (item) => {
  isEdit.value = true
  formData.value = {
    ...item,
    newsTypes: item.newsTypes,
    newsPhoto: item.newsPhoto,
    publishTime: item.publishTime ? new Date(item.publishTime).toISOString().slice(0, 16) : ''
  }
  showModal.value = true
}

// 查看详情
const handleView = (item) => {
  currentItem.value = { ...item }
  showDetailModal.value = true
}

// 删除公告
const handleDelete = async (item) => {
  const result = await confirm({
    message: `确定要删除公告「${item.newsName}」吗？`
  })
  
  if (result) {
    try {
      isLoading.value = true
      // 这里需要调用实际的API删除数据
      // 暂时模拟成功
      list.value = list.value.filter(i => i.id !== item.id)
      message.success('删除成功')
    } catch (error) {
      console.error('删除公告失败:', error)
      message.error('删除失败')
    } finally {
      isLoading.value = false
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    isLoading.value = true
    const submitData = { ...formData.value }
    
    // 格式化发布时间
    if (submitData.publishTime) {
      submitData.publishTime = new Date(submitData.publishTime).toISOString()
    }
    
    // 这里需要调用实际的API提交数据
    // 暂时模拟成功
    if (isEdit.value) {
      // 编辑现有数据
      const index = list.value.findIndex(item => item.id === submitData.id)
      if (index !== -1) {
        list.value[index] = { 
          ...submitData,
          updateTime: new Date().toISOString()
        }
      }
    } else {
      // 添加新数据
      const newItem = {
        id: list.value.length + 1,
        ...submitData,
        createTime: new Date().toISOString(),
        updateTime: new Date().toISOString()
      }
      list.value.push(newItem)
    }
    closeModal()
    message.success(isEdit.value ? '编辑成功' : '新增成功')
  } catch (error) {
    console.error(isEdit.value ? '编辑公告失败:' : '新增公告失败:', error)
    message.error(isEdit.value ? '编辑失败' : '新增失败')
  } finally {
    isLoading.value = false
  }
}

// 处理图片上传
const handlePhotoUpload = async (event) => {
  const file = event.target.files[0]
  if (!file) return
  
  try {
    isLoading.value = true
    const response = await fileApi.upload('news', file)
    if (response.code === 0) {
      formData.value.newsPhoto = 'upload/' + response.file
      message.success('图片上传成功')
    } else {
      message.error(response.msg || '上传失败')
    }
  } catch (error) {
    console.error('上传图片失败:', error)
    message.error(error.msg || '上传图片失败')
  } finally {
    isLoading.value = false
    // 清空文件输入，以便可以重新选择相同的文件
    event.target.value = ''
  }
}

// 关闭模态框
const closeModal = () => {
  showModal.value = false
}

// 关闭详情模态框
const closeDetailModal = () => {
  showDetailModal.value = false
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

// 组件挂载时获取列表数据和公告类型选项
onMounted(async () => {
  await getNewsTypeOptions()
  getList()
})
</script>

<style scoped>
/* 样式与其他管理页面保持一致 */
.admin-gonggao {
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
  align-items: flex-end;
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



/* 类型标签样式 */
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

/* 详情页图片样式 */
.detail-photo {
  margin: 20px 0;
  text-align: center;
}

.detail-photo img {
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-sm);
  transition: transform 0.3s ease;
}

.detail-photo img:hover {
  transform: scale(1.02);
  box-shadow: var(--shadow-base);
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
</style>