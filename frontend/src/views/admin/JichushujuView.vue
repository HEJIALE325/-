<template>
  <div class="admin-jichushuju">
    <h2>基础数据管理</h2>
    <div class="card">
      <div class="card-body">
        <!-- 字典类型选择区域 -->
        <div class="dictionary-types">
          <h3>字典类型</h3>
          <div class="type-buttons">
            <button 
              v-for="type in dictionaryTypes" 
              :key="type.code" 
              class="btn" 
              :class="{ 'btn-primary': selectedType === type.code, 'btn-secondary': selectedType !== type.code }"
              @click="selectType(type)"
            >
              {{ type.name }}
            </button>
          </div>
        </div>
        
        <!-- 字典项管理区域，仅在选择类型后显示 -->
        <div v-if="selectedType" class="dictionary-items">
          <!-- 操作栏 -->
          <div class="action-section">
            <button class="btn btn-primary" @click="handleAdd">
              新增{{ selectedTypeName }}项
            </button>
          </div>
          
          <!-- 搜索表单 -->
          <div class="search-section">
            <form @submit.prevent="handleSearch">
              <div class="form-row">
                <div class="form-group">
                  <label for="search-indexName">索引名称</label>
                  <input 
                    type="text" 
                    id="search-indexName" 
                    v-model="searchParams.indexName"
                    placeholder="请输入索引名称进行搜索"
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
                  <th>字典名称</th>
                  <th>字典编码</th>
                  <th>编码索引</th>
                  <th>索引名称</th>
                  <th>上级字典</th>
                  <th>备注</th>
                  <th>创建时间</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in list" :key="item.id">
                  <td>{{ item.id }}</td>
                  <td>{{ item.dicName }}</td>
                  <td>{{ item.dicCode }}</td>
                  <td>{{ item.codeIndex }}</td>
                  <td>{{ item.indexName }}</td>
                  <td>{{ item.superId || '-' }}</td>
                  <td>{{ item.beizhu || '-' }}</td>
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
    </div>
    
    <!-- 编辑模态框 -->
    <div class="modal" v-if="showEditModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>{{ isEdit ? '编辑' : '新增' }}{{ selectedTypeName }}项</h3>
          <button class="close-btn" @click="closeModal">&times;</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="form-group">
              <label for="form-codeIndex">编码索引 *</label>
              <input 
                type="number" 
                id="form-codeIndex" 
                v-model="formData.codeIndex"
                placeholder="请输入编码索引"
                required
              >
            </div>
            <div class="form-group">
              <label for="form-indexName">索引名称 *</label>
              <input 
                type="text" 
                id="form-indexName" 
                v-model="formData.indexName"
                placeholder="请输入索引名称"
                required
              >
            </div>
            <div class="form-group">
              <label for="form-beizhu">备注</label>
              <textarea 
                id="form-beizhu" 
                v-model="formData.beizhu"
                placeholder="请输入备注信息"
                rows="3"
              ></textarea>
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
import { dictionaryApi } from '../../utils/api'
import confirm from '../../utils/confirm'
import message from '../../utils/message'

// 字典类型列表
const dictionaryTypes = ref([
  { code: 'chongwu_types', name: '宠物类型' },
  { code: 'chongwujiyang_types', name: '寄养服务类型' },
  { code: 'product_types', name: '商品类型' },
  { code: 'news_types', name: '公告类型' }
])

// 选中的字典类型
const selectedType = ref('')
const selectedTypeName = ref('')

// 列表数据
const list = ref([])
// 父级字典选项
const parentOptions = ref([])
// 搜索参数
const searchParams = ref({
  indexName: ''
})
// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
// 加载状态
const isLoading = ref(false)
// 编辑模态框
const showEditModal = ref(false)
const isEdit = ref(false)
const formData = ref({})

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString()
}

// 选择字典类型
const selectType = (type) => {
  selectedType.value = type.code
  selectedTypeName.value = type.name
  currentPage.value = 1
  getList()
}

// 获取列表数据
const getList = async () => {
  if (!selectedType.value) return
  
  try {
    isLoading.value = true
    const params = {
      page: currentPage.value,
      limit: pageSize.value,
      sort: 'id',
      dicCode: selectedType.value,
      indexName: searchParams.value.indexName
    }
    // 调用真实的API获取数据
    const response = await dictionaryApi.getPage(params)
    if (response.code === 0) {
      list.value = response.data.list || []
      totalPages.value = response.data.totalPage || 1
      // 获取父级字典选项
      parentOptions.value = list.value
    } else {
      console.error('获取字典项列表失败:', response.msg)
      list.value = []
      totalPages.value = 1
    }
  } catch (error) {
    console.error('获取字典项列表失败:', error)
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
    indexName: ''
  }
  currentPage.value = 1
  getList()
}

// 新增字典项
const handleAdd = () => {
  isEdit.value = false
  formData.value = {
    dicCode: selectedType.value,
    dicName: selectedTypeName.value,
    codeIndex: '',
    indexName: '',
    superId: '',
    beizhu: ''
  }
  showEditModal.value = true
}

// 编辑字典项
const handleEdit = (item) => {
  isEdit.value = true
  formData.value = {
    id: item.id,
    dicCode: item.dicCode,
    dicName: item.dicName,
    codeIndex: item.codeIndex,
    indexName: item.indexName,
    superId: item.superId || '',
    beizhu: item.beizhu || ''
  }
  showEditModal.value = true
}

// 删除字典项
const handleDelete = async (item) => {
  const result = await confirm({
    message: `确定要删除「${item.indexName}」吗？`
  })
  
  if (result) {
    try {
      isLoading.value = true
      // 调用实际的API删除数据
      // 暂时模拟成功
      list.value = list.value.filter(i => i.id !== item.id)
      message.success('删除成功')
    } catch (error) {
      console.error('删除字典项失败:', error)
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
    // 这里需要调用实际的API提交数据
    // 暂时模拟成功
    if (isEdit.value) {
      // 编辑现有数据
      const index = list.value.findIndex(item => item.id === formData.value.id)
      if (index !== -1) {
        list.value[index] = { ...formData.value }
      }
    } else {
      // 添加新数据
      const newItem = {
        id: list.value.length + 1,
        ...formData.value,
        createTime: new Date().toISOString()
      }
      list.value.push(newItem)
    }
    closeModal()
    message.success(isEdit.value ? '编辑成功' : '新增成功')
  } catch (error) {
    console.error(isEdit.value ? '编辑字典失败:' : '新增字典失败:', error)
    message.error(isEdit.value ? '编辑失败' : '新增失败')
  } finally {
    isLoading.value = false
  }
}

// 关闭模态框
const closeModal = () => {
  showEditModal.value = false
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

// 组件挂载时初始化
onMounted(() => {
  // 默认选择第一个字典类型并加载数据
  if (dictionaryTypes.value.length > 0) {
    const firstType = dictionaryTypes.value[0]
    selectedType.value = firstType.code
    selectedTypeName.value = firstType.name
    getList()
  }
})
</script>

<style scoped>
/* 样式与其他管理页面保持一致 */
.admin-jichushuju {
  padding: 0;
}

h2 {
  margin-bottom: 24px;
  color: var(--text-1);
  font-size: 20px;
}

/* 字典类型选择区域 */
.dictionary-types {
  margin-bottom: 32px;
  padding-bottom: 20px;
  border-bottom: 1px solid var(--border);
}

.dictionary-types h3 {
  margin: 0 0 16px 0;
  color: var(--text-1);
  font-size: 16px;
  font-weight: 600;
}

.type-buttons {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.type-buttons .btn {
  padding: 10px 20px;
  font-size: 14px;
  font-weight: 500;
  border-radius: var(--radius-base);
  transition: all 0.3s ease;
  cursor: pointer;
  border: 1px solid var(--border);
  background-color: white;
  color: var(--text-1);
}

.type-buttons .btn:hover {
  border-color: var(--primary);
  color: var(--primary);
  background-color: rgba(66, 184, 131, 0.05);
}

.type-buttons .btn-primary {
  background-color: var(--primary);
  color: white;
  border-color: var(--primary);
}

.type-buttons .btn-primary:hover {
  background-color: var(--primary-dark);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(66, 184, 131, 0.3);
}

/* 字典项管理区域 */
.dictionary-items {
  margin-top: 24px;
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
</style>