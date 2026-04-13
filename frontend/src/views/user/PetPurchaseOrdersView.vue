<template>
  <div class="pet-purchase-orders-page">
    <Header />
    <main class="pet-purchase-orders-content">
      <div class="container">
        <div class="pet-purchase-orders-header">
          <h1>宠物购买订单</h1>
          <p>查看和管理您的宠物购买订单</p>
        </div>
        
        <!-- 搜索表单 -->
        <div class="search-section">
          <form @submit.prevent="handleSearch">
            <div class="form-row">
              <div class="form-group">
                <label for="search-petName">宠物名称</label>
                <input 
                  type="text" 
                  id="search-petName" 
                  v-model="searchParams.petName"
                  placeholder="请输入宠物名称进行搜索"
                >
              </div>
              <div class="form-group">
                <label for="search-status">订单状态</label>
                <select 
                  id="search-status" 
                  v-model="searchParams.status"
                  class="form-select"
                >
                  <option value="">全部</option>
                  <option value="1">待付款</option>
                  <option value="2">待发货</option>
                  <option value="3">已发货</option>
                  <option value="4">已完成</option>
                  <option value="5">已取消</option>
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
                <th>宠物名称</th>
                <th>宠物品种</th>
                <th>订单金额</th>
                <th>下单时间</th>
                <th>订单状态</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="order in orders" :key="order.id">
                <td>{{ order.id }}</td>
                <td>{{ order.petName }}</td>
                <td>{{ order.petAge || '-' }}</td>
                <td>¥{{ order.price }}</td>
                <td>{{ formatDate(order.createTime) }}</td>
                <td>
                  <span :class="['status-badge', getStatusClass(order.orderStatus)]">
                    {{ getStatusText(order.orderStatus) }}
                  </span>
                </td>
                <td>
                  <button class="btn btn-sm btn-secondary" @click="viewOrderDetail(order)">
                    查看详情
                  </button>
                  <button v-if="order.orderStatus === 1" class="btn btn-sm btn-primary" @click="payOrder(order.id)">
                    立即支付
                  </button>
                  <button v-if="order.orderStatus === 1" class="btn btn-sm btn-danger" @click="cancelOrder(order.id)">
                    取消订单
                  </button>
                </td>
              </tr>
              <tr v-if="orders.length === 0">
                <td colspan="7" class="no-data">暂无订单</td>
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
    </main>
    <Footer />
    
    <!-- 订单详情弹窗 -->
    <div class="modal" v-if="showDetailModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>订单详情</h3>
          <button class="modal-close" @click="showDetailModal = false">×</button>
        </div>
        <div class="modal-body">
          <div v-if="currentOrder" class="order-detail">
            <!-- 订单基本信息 -->
            <div class="detail-section">
              <h4>订单基本信息</h4>
              <div class="detail-grid">
                <div class="detail-item">
                  <span class="detail-label">订单ID：</span>
                  <span class="detail-value">{{ currentOrder.id }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">下单时间：</span>
                  <span class="detail-value">{{ formatDate(currentOrder.createTime) }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">订单状态：</span>
                  <span class="detail-value">{{ getStatusText(currentOrder.orderStatus) }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">订单金额：</span>
                  <span class="detail-value">¥{{ currentOrder.price }}</span>
                </div>
              </div>
            </div>
            
            <!-- 宠物信息 -->
            <div class="detail-section">
              <h4>宠物信息</h4>
              <div class="pet-info-full">
                <div class="pet-image">
                  <img :src="'http://localhost:8080/wangshangchongwudian/' + currentOrder.petImageUrl" :alt="currentOrder.petName">
                </div>
                <div class="pet-details">
                  <h5>{{ currentOrder.petName }}</h5>
                  <p>性别：{{ currentOrder.petGender }}</p>
                  <p>年龄：{{ currentOrder.petAge }}</p>
                  <p>价格：¥{{ currentOrder.price }}</p>
                </div>
              </div>
            </div>
            

          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showDetailModal = false">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { petOrderApi, yonghuApi } from '../../utils/api'
import message from '../../utils/message'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'

const router = useRouter()
const currentPage = ref(1)
const total = ref(0)
const totalPage = ref(1)
const orders = ref([])
const loading = ref(false)
const userInfo = ref(null)
const showDetailModal = ref(false)
const currentOrder = ref(null)

// 搜索参数
const searchParams = ref({
  petName: '',
  status: ''
})

// 获取用户信息
const getUserInfo = async () => {
  try {
    const response = await yonghuApi.session()
    if (response.code === 0) {
      userInfo.value = response.data
      return response.data
    } else {
      message.error('获取用户信息失败')
      router.push('/user/login')
      return null
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    message.error('获取用户信息失败')
    router.push('/user/login')
    return null
  }
}

// 获取订单列表
const fetchOrders = async () => {
  try {
    loading.value = true
    const user = await getUserInfo()
    if (!user) return
    
    const params = {
      page: currentPage.value,
      limit: 8,
      sort: 'id',
      order: 'desc',
      ...searchParams.value
    }
    
    const response = await petOrderApi.getUserOrders(user.id, params)
    
    if (response.code === 0) {
      orders.value = response.data.list
      total.value = response.data.total
      totalPage.value = response.data.totalPage
    } else {
      message.error('获取订单列表失败')
    }
  } catch (error) {
    console.error('获取订单列表失败:', error)
    message.error('获取订单列表失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 切换页码
const handlePageChange = (newPage) => {
  if (newPage >= 1 && newPage <= totalPage.value) {
    currentPage.value = newPage
    fetchOrders()
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchOrders()
}

// 重置
const handleReset = () => {
  searchParams.value = {
    petName: '',
    status: ''
  }
  currentPage.value = 1
  fetchOrders()
}

// 查看订单详情
const viewOrderDetail = async (order) => {
  try {
    const response = await petOrderApi.getDetail(order.id)
    if (response.code === 0) {
      currentOrder.value = response.data
      showDetailModal.value = true
    } else {
      message.error('获取订单详情失败')
    }
  } catch (error) {
    console.error('获取订单详情失败:', error)
    message.error('获取订单详情失败，请稍后重试')
  }
}

// 立即支付
const payOrder = async (orderId) => {
  try {
    const response = await petOrderApi.pay(orderId)
    if (response.code === 0) {
      message.success('支付成功')
      fetchOrders() // 重新获取订单列表
    } else {
      message.error(response.msg || '支付失败')
    }
  } catch (error) {
    console.error('支付失败:', error)
    message.error('支付失败，请稍后重试')
  }
}

// 取消订单
const cancelOrder = async (orderId) => {
  if (confirm('确定要取消该订单吗？')) {
    try {
      const response = await petOrderApi.updateStatus(orderId, 5)
      if (response.code === 0) {
        message.success('订单取消成功')
        fetchOrders() // 重新获取订单列表
      } else {
        message.error('订单取消失败')
      }
    } catch (error) {
      console.error('取消订单失败:', error)
      message.error('取消订单失败，请稍后重试')
    }
  }
}

// 获取状态样式类
const getStatusClass = (status) => {
  switch (status) {
    case 1:
      return 'status-pending'
    case 2:
      return 'status-processing'
    case 3:
      return 'status-shipped'
    case 4:
      return 'status-completed'
    case 5:
      return 'status-cancelled'
    default:
      return ''
  }
}

// 获取状态文本
const getStatusText = (status) => {
  switch (status) {
    case 1:
      return '待付款'
    case 2:
      return '待发货'
    case 3:
      return '已发货'
    case 4:
      return '已完成'
    case 5:
      return '已取消'
    default:
      return '未知状态'
  }
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString()
}

// 页面加载时的初始化
onMounted(() => {
  fetchOrders()
})
</script>

<style scoped>
.pet-purchase-orders-page {
  min-height: 100vh;
  background-color: var(--bg);
}

.pet-purchase-orders-content {
  padding: var(--spacing-2xl) 0;
}

.pet-purchase-orders-header {
  text-align: center;
  margin-bottom: var(--spacing-2xl);
}

.pet-purchase-orders-header h1 {
  color: var(--text-1);
  font-size: var(--fs-2xl);
  font-weight: 600;
  margin-bottom: var(--spacing-sm);
}

.pet-purchase-orders-header p {
  color: var(--text-2);
  font-size: var(--fs-base);
  margin: 0;
}

/* 搜索区域样式 */
.search-section {
  margin-bottom: var(--spacing-xl);
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
  font-family: inherit;
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
  justify-content: flex-end;
  margin-top: 20px;
}

/* 按钮样式 */
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
  background-color: #dc3545;
  color: white;
}

.btn-danger:hover {
  background-color: #c82333;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(220, 53, 69, 0.3);
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
.status-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: var(--radius-full);
  font-size: 12px;
  font-weight: 500;
}

.status-pending {
  background-color: rgba(255, 193, 7, 0.1);
  color: #ffc107;
}

.status-processing {
  background-color: rgba(0, 123, 255, 0.1);
  color: #007bff;
}

.status-completed {
  background-color: rgba(25, 135, 84, 0.1);
  color: #198754;
}

.status-shipped {
  background-color: rgba(108, 117, 125, 0.1);
  color: #6c757d;
}

.status-cancelled {
  background-color: rgba(220, 53, 69, 0.1);
  color: #dc3545;
}

/* 分页样式 */
.pagination {
  display: flex;
  justify-content: center;
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

/* 无数据样式 */
.no-data {
  text-align: center;
  color: var(--text-3);
  padding: 40px 0;
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
  animation: fadeIn 0.3s ease;
}

.modal-content {
  background-color: white;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-lg);
  width: 90%;
  max-width: 800px;
  max-height: 90vh;
  overflow-y: auto;
  animation: slideIn 0.3s ease;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-lg) var(--spacing-xl);
  border-bottom: 1px solid var(--border);
  background-color: var(--card);
  border-radius: var(--radius-base) var(--radius-base) 0 0;
}

.modal-header h3 {
  margin: 0;
  color: var(--text-1);
  font-size: var(--fs-lg);
  font-weight: 600;
}

.modal-close {
  background: none;
  border: none;
  font-size: var(--fs-xl);
  color: var(--text-2);
  cursor: pointer;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius-sm);
  transition: all 0.3s ease;
}

.modal-close:hover {
  background-color: var(--hover-bg);
  color: var(--text-1);
}

.modal-body {
  padding: var(--spacing-xl);
  background-color: white;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: var(--spacing-base);
  padding: var(--spacing-lg) var(--spacing-xl);
  border-top: 1px solid var(--border);
  background-color: var(--card);
  border-radius: 0 0 var(--radius-base) var(--radius-base);
}

/* 订单详情样式 */
.order-detail {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xl);
}

.detail-section {
  background-color: var(--card);
  border-radius: var(--radius-base);
  padding: var(--spacing-lg);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.detail-section h4 {
  margin: 0 0 var(--spacing-lg) 0;
  color: var(--text-1);
  font-size: var(--fs-base);
  font-weight: 600;
  padding-bottom: var(--spacing-sm);
  border-bottom: 1px solid var(--border);
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--spacing-base);
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
}

.detail-label {
  font-size: var(--fs-sm);
  color: var(--text-2);
  font-weight: 500;
}

.detail-value {
  font-size: var(--fs-sm);
  color: var(--text-1);
  font-weight: 400;
}

/* 宠物信息完整样式 */
.pet-info-full {
  display: flex;
  gap: var(--spacing-lg);
  align-items: flex-start;
}

.pet-info-full .pet-image {
  flex-shrink: 0;
  width: 100px;
  height: 100px;
  border-radius: var(--radius-base);
  border: 1px solid var(--border);
  overflow: hidden;
}

.pet-info-full .pet-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.pet-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
}

.pet-details h5 {
  margin: 0 0 var(--spacing-sm) 0;
  color: var(--text-1);
  font-size: var(--fs-base);
  font-weight: 600;
}

.pet-details p {
  margin: 0;
  font-size: var(--fs-sm);
  color: var(--text-2);
}

/* 动画 */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideIn {
  from {
    transform: translateY(-20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

@media (max-width: 768px) {
  .pet-purchase-orders-content {
    padding: var(--spacing-xl) 0;
  }
  
  .form-row {
    flex-direction: column;
    align-items: stretch;
  }
  
  .form-group {
    max-width: none;
  }
  
  .form-actions {
    justify-content: center;
  }
  
  .admin-table th,
  .admin-table td {
    padding: 12px 16px;
    font-size: 12px;
  }
  
  .modal-content {
    width: 95%;
    margin: 0 var(--spacing-base);
  }
  
  .modal-header,
  .modal-body,
  .modal-footer {
    padding: var(--spacing-base);
  }
  
  .detail-grid {
    grid-template-columns: 1fr;
  }
  
  .pet-info-full {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .pet-info-full .pet-image {
    margin: 0 auto var(--spacing-lg);
  }
}
</style>