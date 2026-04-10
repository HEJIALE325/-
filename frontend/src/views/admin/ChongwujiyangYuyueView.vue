<template>
  <div class="admin-chongwujiyang-yuyue">
    <h2>宠物寄养订单管理</h2>
    <div class="card">
      <div class="card-body">
        <!-- 搜索表单 -->
        <div class="search-section">
          <form @submit.prevent="handleSearch">
            <div class="form-row">
              <div class="form-group">
                <label for="search-orderName">订单名称</label>
                <input 
                  type="text" 
                  id="search-orderName" 
                  v-model="searchParams.chongwujiyangYuyueName"
                  placeholder="请输入订单名称进行搜索"
                >
              </div>
              <div class="form-group">
                <label for="search-petType">宠物类型</label>
                <select 
                  id="search-petType" 
                  v-model="searchParams.chongwuTypes"
                >
                  <option value="">全部</option>
                  <option v-for="type in petTypeOptions" :key="type.codeIndex" :value="type.codeIndex">
                    {{ type.indexName }}
                  </option>
                </select>
              </div>
              <div class="form-group">
                <label for="search-ownerName">主人姓名</label>
                <input 
                  type="text" 
                  id="search-ownerName" 
                  v-model="searchParams.yonghuName"
                  placeholder="请输入主人姓名进行搜索"
                >
              </div>
              <div class="form-group">
                <label for="search-status">审核状态</label>
                <select 
                  id="search-status" 
                  v-model="searchParams.chongwujiyangYuyueYesnoTypes"
                >
                  <option value="">全部</option>
                  <option v-for="status in auditStatusOptions" :key="status.codeIndex" :value="status.codeIndex">
                    {{ status.indexName }}
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
                <th>订单名称</th>
                <th>宠物名称</th>
                <th>宠物类型</th>
                <th>主人姓名</th>
                <th>联系电话</th>
                <th>寄养类型</th>
                <th>寄养时间</th>
                <th>总费用</th>
                <th>审核状态</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in list" :key="item.id">
                <td>{{ item.id }}</td>
                <td>{{ item.chongwujiyangYuyueName }}</td>
                <td>{{ item.chongwujiyangYuyueName }}</td>
                <td>{{ item.chongwuValue }}</td>
                <td>{{ item.yonghuName }}</td>
                <td>{{ item.yonghuPhone }}</td>
                <td>{{ item.chongwujiyangValue }}</td>
                <td>{{ formatDate(item.chongwujiyangYuyueTime) }}</td>
                <td>{{ item.chongwujiyangYuyuePrice }}</td>
                <td>
                  <span class="status-tag" :class="getStatusClass(item.chongwujiyangYuyueYesnoTypes)">
                    {{ item.chongwujiyangYuyueYesnoValue }}
                  </span>
                </td>
                <td>
                  <button class="btn btn-secondary btn-sm" @click="handleView(item)">
                    查看
                  </button>
                  <button 
                    class="btn btn-primary btn-sm" 
                    @click="handleConfirm(item)"
                    v-if="item.chongwujiyangYuyueYesnoTypes !== 2"
                  >
                    确认
                  </button>
                  <button 
                    class="btn btn-danger btn-sm" 
                    @click="handleReject(item)"
                    v-if="item.chongwujiyangYuyueYesnoTypes !== 3"
                  >
                    拒绝
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
    
    <!-- 订单详情模态框 -->
    <div class="modal" v-if="showDetailModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>订单详情</h3>
          <button class="close-btn" @click="closeDetailModal">&times;</button>
        </div>
        <div class="modal-body">
          <!-- 订单基本信息 -->
          <h4>基本信息</h4>
          <div class="detail-grid">
            <div class="detail-item">
              <label>订单ID：</label>
              <span>{{ currentOrder.id }}</span>
            </div>
            <div class="detail-item">
              <label>订单名称：</label>
              <span>{{ currentOrder.chongwujiyangYuyueName }}</span>
            </div>
            <div class="detail-item">
              <label>宠物类型：</label>
              <span>{{ currentOrder.chongwuValue }}</span>
            </div>
            <div class="detail-item">
              <label>宠物重量：</label>
              <span>{{ currentOrder.chongwuZhongliang }} kg</span>
            </div>
            <div class="detail-item">
              <label>寄养数量：</label>
              <span>{{ currentOrder.chongwujiyangYuyueNum }}</span>
            </div>
            <div class="detail-item">
              <label>是否接送：</label>
              <span>{{ currentOrder.shifouValue }}</span>
            </div>
            <div class="detail-item">
              <label>寄养时间：</label>
              <span>{{ formatDate(currentOrder.chongwujiyangYuyueTime) }}</span>
            </div>
            <div class="detail-item">
              <label>总费用：</label>
              <span>{{ currentOrder.chongwujiyangYuyuePrice }}</span>
            </div>
            <div class="detail-item">
              <label>审核状态：</label>
              <span class="status-tag" :class="getStatusClass(currentOrder.chongwujiyangYuyueYesnoTypes)">
                {{ currentOrder.chongwujiyangYuyueYesnoValue }}
              </span>
            </div>
            <div class="detail-item">
              <label>审核结果：</label>
              <span>{{ currentOrder.chongwujiyangYuyueYesnoText }}</span>
            </div>
            <div class="detail-item">
              <label>创建时间：</label>
              <span>{{ formatDate(currentOrder.createTime) }}</span>
            </div>
            <div class="detail-item">
              <label>插入时间：</label>
              <span>{{ formatDate(currentOrder.insertTime) }}</span>
            </div>
          </div>
          
          <!-- 寄养服务信息 -->
          <h4>寄养服务信息</h4>
          <div class="detail-grid">
            <div class="detail-item">
              <label>服务名称：</label>
              <span>{{ currentOrder.chongwujiyangName }}</span>
            </div>
            <div class="detail-item">
              <label>服务类型：</label>
              <span>{{ currentOrder.chongwujiyangValue }}</span>
            </div>
            <div class="detail-item">
              <label>服务价格：</label>
              <span>{{ currentOrder.chongwujiyangNewMoney }}</span>
            </div>
            <div class="detail-item">
              <label>点击量：</label>
              <span>{{ currentOrder.chongwujiyangClicknum }}</span>
            </div>
            <div class="detail-item full-width">
              <label>服务内容：</label>
              <span>{{ currentOrder.chongwujiyangContent }}</span>
            </div>
            <div class="detail-item full-width">
              <label>服务图片：</label>
              <div v-if="currentOrder.chongwujiyangPhoto" class="image-container">
                <img :src="'http://localhost:8080/wangshangchongwudian/' + currentOrder.chongwujiyangPhoto" :alt="currentOrder.chongwujiyangName" style="max-width: 100px; max-height: 100px;">
              </div>
              <span v-else>无图片</span>
            </div>
          </div>
          
          <!-- 用户信息 -->
          <h4>用户信息</h4>
          <div class="detail-grid">
            <div class="detail-item">
              <label>用户姓名：</label>
              <span>{{ currentOrder.yonghuName }}</span>
            </div>
            <div class="detail-item">
              <label>联系电话：</label>
              <span>{{ currentOrder.yonghuPhone }}</span>
            </div>
            <div class="detail-item">
              <label>身份证号：</label>
              <span>{{ currentOrder.yonghuIdNumber }}</span>
            </div>
            <div class="detail-item">
              <label>邮箱：</label>
              <span>{{ currentOrder.yonghuEmail }}</span>
            </div>
            <div class="detail-item">
              <label>账户余额：</label>
              <span>{{ currentOrder.newMoney }}</span>
            </div>
            <div class="detail-item full-width">
              <label>用户头像：</label>
              <div v-if="currentOrder.yonghuPhoto" class="image-container">
                <img :src="'http://localhost:8080/wangshangchongwudian/' + currentOrder.yonghuPhoto" :alt="currentOrder.yonghuName" style="max-width: 100px; max-height: 100px;">
              </div>
              <span v-else>无头像</span>
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
import { chongwujiyangYuyueApi, dictionaryApi } from '../../utils/api'
import confirm from '../../utils/confirm'
import message from '../../utils/message'

// 列表数据
const list = ref([])
// 字典选项
const petTypeOptions = ref([])
const auditStatusOptions = ref([])
const shifouTypeOptions = ref([])
// 搜索参数
const searchParams = ref({
  chongwujiyangYuyueName: '',
  chongwuTypes: '',
  yonghuName: '',
  chongwujiyangYuyueYesnoTypes: ''
})
// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
// 详情模态框
const showDetailModal = ref(false)
const currentOrder = ref({})

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString()
}

// 获取状态样式类
const getStatusClass = (status) => {
  switch (status) {
    case 1: // 待审核
      return 'status-pending'
    case 2: // 同意
      return 'status-confirmed'
    case 3: // 拒绝
      return 'status-canceled'
    default:
      return ''
  }
}

// 获取字典数据
const getDictionaryData = async () => {
  try {
    // 获取宠物类型
    const petTypeResponse = await dictionaryApi.getPage({
      dicCode: 'chongwu_types',
      page: 1,
      limit: 100
    })
    if (petTypeResponse.code === 0) {
      petTypeOptions.value = petTypeResponse.data.list || []
    }
    
    // 获取审核状态
    const auditStatusResponse = await dictionaryApi.getPage({
      dicCode: 'chongwujiyang_yuyue_yesno_types',
      page: 1,
      limit: 100
    })
    if (auditStatusResponse.code === 0) {
      auditStatusOptions.value = auditStatusResponse.data.list || []
    }
    
    // 获取是否接送类型
    const shifouTypeResponse = await dictionaryApi.getPage({
      dicCode: 'shifou_types',
      page: 1,
      limit: 100
    })
    if (shifouTypeResponse.code === 0) {
      shifouTypeOptions.value = shifouTypeResponse.data.list || []
    }
  } catch (error) {
    console.error('获取字典数据失败:', error)
  }
}

// 获取列表数据
const getList = async () => {
  try {
    const params = {
      page: currentPage.value,
      limit: pageSize.value,
      sort: 'id',
      chongwujiyangYuyueDelete: 1,
      ...searchParams.value
    }
    const response = await chongwujiyangYuyueApi.getPage(params)
    if (response.code === 0) {
      list.value = response.data.list || []
      totalPages.value = response.data.totalPage || 1
    }
  } catch (error) {
    console.error('获取宠物寄养订单列表失败:', error)
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
    chongwuTypes: '',
    yonghuName: '',
    chongwujiyangYuyueYesnoTypes: ''
  }
  currentPage.value = 1
  getList()
}

// 查看详情
const handleView = async (item) => {
  try {
    const response = await chongwujiyangYuyueApi.getInfo(item.id)
    if (response.code === 0) {
      currentOrder.value = response.data
      showDetailModal.value = true
    }
  } catch (error) {
    console.error('获取订单详情失败:', error)
  }
}

// 确认订单
const handleConfirm = async (item) => {
  const result = await confirm({
    message: `确定要确认订单「${item.chongwujiyangYuyueName}」吗？`
  })
  
  if (result) {
    try {
      const response = await chongwujiyangYuyueApi.update({
        id: item.id,
        chongwujiyangYuyueYesnoTypes: 2
      })
      if (response.code === 0) {
        message.success('确认成功')
        getList()
      }
    } catch (error) {
      console.error('确认订单失败:', error)
      message.error('确认失败')
    }
  }
}

// 拒绝订单
const handleReject = async (item) => {
  const result = await confirm({
    message: `确定要拒绝订单「${item.chongwujiyangYuyueName}」吗？`
  })
  
  if (result) {
    try {
      const response = await chongwujiyangYuyueApi.update({
        id: item.id,
        chongwujiyangYuyueYesnoTypes: 3
      })
      if (response.code === 0) {
        message.success('拒绝成功')
        getList()
      }
    } catch (error) {
      console.error('拒绝订单失败:', error)
      message.error('拒绝失败')
    }
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

// 组件挂载时获取列表数据和字典数据
onMounted(async () => {
  await getDictionaryData()
  getList()
})
</script>

<style scoped>
/* 样式与其他管理页面保持一致 */
.admin-chongwujiyang-yuyue {
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

.status-pending {
  background-color: rgba(250, 152, 3, 0.1);
  color: var(--warning);
}

.status-confirmed {
  background-color: rgba(82, 196, 26, 0.1);
  color: var(--success);
}

.status-completed {
  background-color: rgba(82, 196, 26, 0.1);
  color: var(--success);
}

.status-canceled {
  background-color: rgba(245, 34, 45, 0.1);
  color: var(--danger);
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

.modal-body h4 {
  margin-top: 24px;
  margin-bottom: 16px;
  color: var(--text-1);
  font-size: 16px;
  font-weight: 600;
  border-bottom: 1px solid var(--border);
  padding-bottom: 8px;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}

.detail-item {
  display: flex;
  gap: 12px;
  align-items: flex-start;
}

.detail-item.full-width {
  grid-column: span 2;
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
  word-break: break-word;
}

.image-container {
  margin-top: 4px;
}

.detail-image {
  max-width: 200px;
  max-height: 150px;
  border-radius: var(--radius-base);
  border: 1px solid var(--border);
  object-fit: cover;
}

.detail-image:hover {
  transform: scale(1.05);
  transition: transform 0.3s ease;
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