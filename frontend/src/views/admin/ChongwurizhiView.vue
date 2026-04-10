<template>
  <div class="admin-chongwurizhi">
    <h2>寄存日志管理</h2>
    <div class="card">
      <div class="card-body">
        <!-- 搜索表单 -->
        <div class="search-section">
          <form @submit.prevent="handleSearch">
            <div class="form-row">
              <div class="form-group">
                <label for="search-petName">宠物名称</label>
                <input 
                  type="text" 
                  id="search-petName" 
                  v-model="searchParams.chongwujiyangYuyueName"
                  placeholder="请输入宠物名称进行搜索"
                >
              </div>
              <div class="form-group">
                <label for="search-chongwuTypes">宠物类型</label>
                <select 
                  id="search-chongwuTypes" 
                  v-model="searchParams.chongwuTypes"
                >
                  <option value="">请选择宠物类型</option>
                  <option v-for="item in chongwuTypes" :key="item.codeIndex" :value="item.codeIndex">
                    {{ item.indexName }}
                  </option>
                </select>
              </div>
              <div class="form-group">
                <label for="search-title">标题</label>
                <input 
                  type="text" 
                  id="search-title" 
                  v-model="searchParams.chongwurizhiName"
                  placeholder="请输入标题进行搜索"
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
                <th>宠物名称</th>
                <th>宠物类型</th>
                <th>标题</th>
                <th>寄养时间</th>
                <th>日志内容</th>
                <th>日志时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in list" :key="item.id">
                <td>{{ item.id }}</td>
                <td>{{ item.chongwujiyangYuyueName }}</td>
                <td>{{ item.chongwuValue }}</td>
                <td>{{ item.chongwurizhiName }}</td>
                <td>{{ formatDate(item.chongwujiyangYuyueTime) }}</td>
                <td class="log-content">{{ item.chongwurizhiContent }}</td>
                <td>{{ formatDate(item.insertTime) }}</td>
                <td>
                  <button class="btn btn-secondary btn-sm" @click="handleView(item)">
                    查看
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
    
    <!-- 日志详情模态框 -->
    <div class="modal" v-if="showDetailModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>日志详情</h3>
          <button class="close-btn" @click="closeDetailModal">&times;</button>
        </div>
        <div class="modal-body">
          <div class="detail-item-container">
            <div class="detail-item-row">
              <div class="detail-item">
                <label>宠物名称：</label>
                <span>{{ currentLog.chongwujiyangYuyueName }}</span>
              </div>
              <div class="detail-item">
                <label>宠物类型：</label>
                <span>{{ currentLog.chongwuValue }}</span>
              </div>
            </div>
            <div class="detail-item-row">
              <div class="detail-item">
                <label>宠物重量：</label>
                <span>{{ currentLog.chongwuZhongliang }}</span>
              </div>
              <div class="detail-item">
                <label>寄养开始时间：</label>
                <span>{{ formatDate(currentLog.chongwujiyangYuyueTime) }}</span>
              </div>
            </div>
            <div class="detail-item-row">
              <div class="detail-item">
                <label>寄养天数：</label>
                <span>{{ currentLog.chongwujiyangYuyueNum }}</span>
              </div>
              <div class="detail-item">
                <label>是否接送：</label>
                <span>{{ currentLog.shifouValue }}</span>
              </div>
            </div>
            <div class="detail-item-row">
              <div class="detail-item">
                <label>总价格：</label>
                <span>{{ currentLog.chongwujiyangYuyuePrice }}</span>
              </div>
              <div class="detail-item">
                <label>寄养审核：</label>
                <span>{{ currentLog.chongwujiyangYuyueYesnoValue }}</span>
              </div>
            </div>
            <div class="detail-item-row">
              <div class="detail-item">
                <label>审核结果：</label>
                <span>{{ currentLog.chongwujiyangYuyueYesnoText }}</span>
              </div>
              <div class="detail-item">
                <label>标题：</label>
                <span>{{ currentLog.chongwurizhiName }}</span>
              </div>
            </div>
            <div class="detail-item-row">
              <div class="detail-item">
                <label>寄养服务详情：</label>
                <span class="log-content">{{ currentLog.chongwurizhiContent }}</span>
              </div>
              <div class="detail-item">
                <label>日志时间：</label>
                <span>{{ formatDate(currentLog.insertTime) }}</span>
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
import { chongwurizhiApi, dictionaryApi } from '../../utils/api'

// 列表数据
const list = ref([])
// 宠物类型数据字典
const chongwuTypes = ref([])
// 搜索参数
const searchParams = ref({
  chongwujiyangYuyueName: '',
  chongwurizhiName: '',
  chongwuTypes: ''
})
// 分页参数
const currentPage = ref(1)
const pageSize = ref(5)
const totalPages = ref(1)
// 详情模态框
const showDetailModal = ref(false)
const currentLog = ref({})

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString()
}

// 获取宠物类型数据字典
const getChongwuTypes = async () => {
  try {
    const response = await dictionaryApi.getByDicCode('chongwu_types')
    if (response.code === 0) {
      chongwuTypes.value = response.data.list || []
    }
  } catch (error) {
    console.error('获取宠物类型数据字典失败:', error)
  }
}

// 获取列表数据
const getList = async () => {
  try {
    const params = {
      page: currentPage.value,
      limit: pageSize.value,
      sort: 'id',
      chongwurizhiDelete: 1,
      ...searchParams.value
    }
    const response = await chongwurizhiApi.getPage(params)
    if (response.code === 0) {
      list.value = response.data.list || []
      totalPages.value = response.data.totalPage || 1
    }
  } catch (error) {
    console.error('获取寄存日志列表失败:', error)
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
    chongwujiyangYuyueName: '',
    chongwurizhiName: '',
    chongwuTypes: ''
  }
  currentPage.value = 1
  getList()
}

// 查看详情
const handleView = async (item) => {
  try {
    const response = await chongwurizhiApi.getInfo(item.id)
    if (response.code === 0) {
      currentLog.value = response.data
      showDetailModal.value = true
    }
  } catch (error) {
    console.error('获取日志详情失败:', error)
  }
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

// 组件挂载时获取列表数据和宠物类型数据字典
onMounted(async () => {
  await getChongwuTypes()
  getList()
})
</script>

<style scoped>
/* 样式与其他管理页面保持一致 */
.admin-chongwurizhi {
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

.log-content {
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 日志类型标签样式 */
.log-type-tag {
  display: inline-block;
  padding: 4px 12px;
  border-radius: var(--radius-full);
  font-size: 12px;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.log-type-feeding {
  background-color: rgba(250, 152, 3, 0.1);
  color: var(--warning);
}

.log-type-walk {
  background-color: rgba(82, 196, 26, 0.1);
  color: var(--success);
}

.log-type-cleaning {
  background-color: rgba(59, 130, 246, 0.1);
  color: var(--primary);
}

.log-type-medical {
  background-color: rgba(245, 34, 45, 0.1);
  color: var(--danger);
}

.log-type-other {
  background-color: rgba(156, 163, 175, 0.1);
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

.detail-item-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 24px;
}

.detail-item-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
}

.detail-item {
  display: flex;
  flex-direction: row;
  gap: 12px;
  align-items: flex-start;
}

.detail-item label {
  width: 100px;
  font-weight: 500;
  color: var(--text-1);
  flex-shrink: 0;
  font-size: 14px;
}

.detail-item span {
  flex: 1;
  color: var(--text-2);
  font-size: 14px;
  word-break: break-word;
}

.modal-body .log-content {
  max-width: none;
  overflow: visible;
  text-overflow: unset;
  white-space: normal;
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