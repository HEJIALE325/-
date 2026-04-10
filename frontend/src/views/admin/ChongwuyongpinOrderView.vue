<template>
  <div class="admin-chongwuyongpin-order">
    <h2>商品订单管理</h2>
    <div class="card">
      <div class="card-body">
        <!-- 操作栏 -->
        <div class="action-section">
          <!-- 商品订单管理暂无新增功能 -->
        </div>
        
        <!-- 搜索表单 -->
        <div class="search-section">
          <form @submit.prevent="handleSearch">
            <div class="form-row">
              <div class="form-group">
                <label for="search-orderNumber">订单编号</label>
                <input 
                  type="text" 
                  id="search-orderNumber" 
                  v-model="searchParams.chongwuyongpinOrderUuidNumber"
                  placeholder="请输入订单编号进行搜索"
                >
              </div>
              <div class="form-group">
                <label for="search-productName">商品名称</label>
                <input 
                  type="text" 
                  id="search-productName" 
                  v-model="searchParams.chongwuyongpinName"
                  placeholder="请输入商品名称进行搜索"
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
                <label for="search-orderStatus">订单状态</label>
                <select 
                  id="search-orderStatus" 
                  v-model="searchParams.chongwuyongpinOrderTypes"
                >
                  <option value="">全部</option>
                  <option v-for="type in orderStatusOptions" :key="type.codeIndex" :value="type.codeIndex">
                    {{ type.indexName }}
                  </option>
                </select>
              </div>
              <div class="form-group">
                <label for="search-productType">商品类型</label>
                <select 
                  id="search-productType" 
                  v-model="searchParams.chongwuyongpinTypes"
                >
                  <option value="">全部</option>
                  <option v-for="type in productTypeOptions" :key="type.codeIndex" :value="type.codeIndex">
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
                <th>订单编号</th>
                <th>商品名称</th>
                <th>用户姓名</th>
                <th>购买数量</th>
                <th>订单金额</th>
                <th>订单状态</th>
                <th>支付方式</th>
                <th>创建时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in orderList" :key="item.id">
                <td>{{ item.chongwuyongpinOrderUuidNumber }}</td>
                <td>{{ item.chongwuyongpinName }}</td>
                <td>{{ item.yonghuName }}</td>
                <td>{{ item.buyNumber }}</td>
                <td>¥{{ item.chongwuyongpinOrderTruePrice.toFixed(2) }}</td>
                <td>
                  <span class="status-tag" :class="getStatusClass(item.chongwuyongpinOrderTypes)">
                    {{ item.chongwuyongpinOrderValue }}
                  </span>
                </td>
                <td>
                  <span class="type-tag">
                    {{ item.chongwuyongpinOrderPaymentValue }}
                  </span>
                </td>
                <td>{{ formatDate(item.createTime) }}</td>
                <td>
                  <button class="btn btn-secondary btn-sm" @click="handleView(item)">
                    查看详情
                  </button>
                  <button class="btn btn-primary btn-sm" v-if="item.chongwuyongpinOrderTypes === 101" @click="handleDeliver(item.id)">
                    发货
                  </button>
                </td>
              </tr>
              <tr v-if="orderList.length === 0">
                <td colspan="9" class="no-data">暂无数据</td>
              </tr>
            </tbody>
          </table>
        </div>
        
        <!-- 分页 -->
        <div class="pagination" v-if="total > 0">
          <button 
            class="btn btn-secondary" 
            @click="handlePageChange(currentPage - 1)" 
            :disabled="currentPage === 1"
          >
            上一页
          </button>
          <span class="page-info">
            第 {{ currentPage }} / {{ totalPage }} 页
          </span>
          <button 
            class="btn btn-secondary" 
            @click="handlePageChange(currentPage + 1)" 
            :disabled="currentPage === totalPage"
          >
            下一页
          </button>
        </div>
      </div>
    </div>
    
    <!-- 订单详情模态框 -->
    <div class="modal" v-if="isDetailModalOpen">
      <div class="modal-content">
        <div class="modal-header">
          <h3>订单详情</h3>
          <button class="close-btn" @click="handleCloseModal">&times;</button>
        </div>
        <div class="modal-body">
          <div v-if="selectedOrder" class="detail-content">
            <div class="detail-item">
              <span class="detail-label">订单编号：</span>
              <span class="detail-value">{{ selectedOrder.chongwuyongpinOrderUuidNumber }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">商品名称：</span>
              <span class="detail-value">{{ selectedOrder.chongwuyongpinName }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">商品图片：</span>
              <span class="detail-value">
                <img v-if="selectedOrder.chongwuyongpinPhoto" :src="'http://localhost:8080/wangshangchongwudian/' + selectedOrder.chongwuyongpinPhoto" alt="商品图片" style="max-width: 100px; max-height: 100px;">
                <span v-else class="no-photo">暂无图片</span>
              </span>
            </div>
            <div class="detail-item">
              <span class="detail-label">用户姓名：</span>
              <span class="detail-value">{{ selectedOrder.yonghuName }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">购买数量：</span>
              <span class="detail-value">{{ selectedOrder.buyNumber }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">订单金额：</span>
              <span class="detail-value">¥{{ selectedOrder.chongwuyongpinOrderTruePrice.toFixed(2) }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">订单状态：</span>
              <span class="detail-value">
                <span class="status-tag" :class="getStatusClass(selectedOrder.chongwuyongpinOrderTypes)">
                  {{ selectedOrder.chongwuyongpinOrderValue }}
                </span>
              </span>
            </div>
            <div class="detail-item">
              <span class="detail-label">支付方式：</span>
              <span class="detail-value">
                <span class="type-tag">
                  {{ selectedOrder.chongwuyongpinOrderPaymentValue }}
                </span>
              </span>
            </div>
            <div class="detail-item">
              <span class="detail-label">创建时间：</span>
              <span class="detail-value">{{ formatDate(selectedOrder.createTime) }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">收货人：</span>
              <span class="detail-value">{{ selectedOrder.addressName }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">收货电话：</span>
              <span class="detail-value">{{ selectedOrder.addressPhone }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">收货地址：</span>
              <span class="detail-value">{{ selectedOrder.addressDizhi }}</span>
            </div>
          </div>
        </div>
        <div class="form-actions">
          <button type="button" class="btn btn-secondary" @click="handleCloseModal">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { chongwuyongpinOrderApi, dictionaryApi } from '../../utils/api'
import confirm from '../../utils/confirm'
import message from '../../utils/message'

// 订单列表数据
const orderList = ref([])
const total = ref(0)
const totalPage = ref(0)
const currentPage = ref(1)
const pageSize = ref(5)

// 搜索参数
const searchParams = ref({
  chongwuyongpinOrderUuidNumber: '',
  chongwuyongpinName: '',
  yonghuName: '',
  chongwuyongpinOrderTypes: '',
  chongwuyongpinTypes: ''
})

// 数据字典
const orderStatusOptions = ref([])
const paymentTypeOptions = ref([])
const productTypeOptions = ref([])

// 模态框
const isDetailModalOpen = ref(false)
const selectedOrder = ref(null)

// 获取订单列表
const getOrderList = async () => {
  try {
    const params = {
      page: currentPage.value,
      limit: pageSize.value,
      sort: 'id',
      chongwuyongpinOrderDelete: '1'
    }
    
    // 添加搜索条件
    if (searchParams.value.chongwuyongpinOrderUuidNumber) {
      params.chongwuyongpinOrderUuidNumber = searchParams.value.chongwuyongpinOrderUuidNumber
    }
    if (searchParams.value.chongwuyongpinName) {
      params.chongwuyongpinName = searchParams.value.chongwuyongpinName
    }
    if (searchParams.value.yonghuName) {
      params.yonghuName = searchParams.value.yonghuName
    }
    if (searchParams.value.chongwuyongpinOrderTypes !== '') {
      params.chongwuyongpinOrderTypes = searchParams.value.chongwuyongpinOrderTypes
    }
    if (searchParams.value.chongwuyongpinTypes !== '') {
      params.chongwuyongpinTypes = searchParams.value.chongwuyongpinTypes
    }
    
    const response = await chongwuyongpinOrderApi.getPage(params)
    if (response.code === 0) {
      orderList.value = response.data.list
      total.value = response.data.total
      totalPage.value = response.data.totalPage
    }
  } catch (error) {
    console.error('获取订单列表失败:', error)
  }
}

// 获取数据字典
const getDictionaries = async () => {
  try {
    // 获取订单类型字典
    const orderStatusResponse = await dictionaryApi.getByDicCode('chongwuyongpin_order_types')
    if (orderStatusResponse.code === 0) {
      orderStatusOptions.value = orderStatusResponse.data.list || []
    }
    
    // 获取支付方式字典
    const paymentTypeResponse = await dictionaryApi.getByDicCode('chongwuyongpin_order_payment_types')
    if (paymentTypeResponse.code === 0) {
      paymentTypeOptions.value = paymentTypeResponse.data.list || []
    }
    
    // 获取商品类型字典
    const productTypeResponse = await dictionaryApi.getByDicCode('chongwuyongpin_types')
    if (productTypeResponse.code === 0) {
      productTypeOptions.value = productTypeResponse.data.list || []
    }
  } catch (error) {
    console.error('获取数据字典失败:', error)
  }
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString()
}

// 获取状态样式类
const getStatusClass = (status) => {
  switch (status) {
    case 101: // 待付款
      return 'status-pending'
    case 102: // 待发货
      return 'status-processing'
    case 103: // 待收货
      return 'status-shipped'
    case 104: // 已收货
      return 'status-completed'
    case 105: // 已评价
      return 'status-active'
    default:
      return 'status-inactive'
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  getOrderList()
}

// 重置
const handleReset = () => {
  searchParams.value = {
    chongwuyongpinOrderUuidNumber: '',
    chongwuyongpinName: '',
    yonghuName: '',
    chongwuyongpinOrderTypes: '',
    chongwuyongpinTypes: ''
  }
  currentPage.value = 1
  getOrderList()
}

// 分页
const handlePageChange = (page) => {
  if (page >= 1 && page <= totalPage.value) {
    currentPage.value = page
    getOrderList()
  }
}

// 查看详情
const handleView = (item) => {
  selectedOrder.value = item
  isDetailModalOpen.value = true
}

// 关闭模态框
const handleCloseModal = () => {
  isDetailModalOpen.value = false
  selectedOrder.value = null
}

// 发货
const handleDeliver = async (id) => {
  try {
    const result = await confirm({
      message: '确定要发货吗？'
    })
    if (result) {
      const response = await chongwuyongpinOrderApi.deliver(id)
      if (response.code === 0) {
        message.success('发货成功')
        getOrderList() // 刷新订单列表
      } else {
        message.error('发货失败：' + response.msg)
      }
    }
  } catch (error) {
    console.error('发货失败:', error)
    message.error('发货失败，请稍后重试')
  }
}

// 组件挂载时获取数据
onMounted(() => {
  getDictionaries()
  getOrderList()
})
</script>

<style scoped>
/* 样式与其他管理页面保持一致 */
.admin-chongwuyongpin-order {
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

.form-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 20px;
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

.status-pending {
  background-color: rgba(255, 193, 7, 0.1);
  color: var(--warning);
}

.status-processing {
  background-color: rgba(33, 150, 243, 0.1);
  color: var(--info);
}

.status-shipped {
  background-color: rgba(103, 58, 183, 0.1);
  color: var(--primary);
}

.status-completed {
  background-color: rgba(82, 196, 26, 0.1);
  color: var(--success);
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

.page-info {
  color: var(--text-2);
  font-size: 14px;
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

/* 详情内容样式 */
.detail-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.detail-item {
  display: flex;
  gap: 16px;
  align-items: flex-start;
  margin-bottom: 16px;
}

.detail-label {
  font-weight: 600;
  color: var(--text-1);
  min-width: 120px;
  flex-shrink: 0;
  padding-top: 4px;
}

.detail-value {
  color: var(--text-2);
  flex: 1;
  padding-top: 4px;
}

/* 详情图片样式 */
.detail-photo-img {
  max-width: 200px;
  max-height: 200px;
  object-fit: cover;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
  margin-top: 8px;
}

.no-photo {
  display: inline-block;
  width: 200px;
  height: 200px;
  background-color: var(--hover-bg);
  border: 2px dashed var(--border);
  border-radius: var(--radius-base);
  color: var(--text-3);
  font-size: 14px;
  font-weight: 500;
  text-align: center;
  line-height: 200px;
  margin-top: 8px;
  transition: all 0.2s ease;
}

.no-photo:hover {
  background-color: var(--border);
  color: var(--text-2);
}

/* 无数据样式 */
.no-data {
  text-align: center;
  color: var(--text-3);
  padding: 40px 0;
  font-size: 14px;
}
</style>