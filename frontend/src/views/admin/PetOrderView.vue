<template>
  <div class="admin-pet-order">
    <div class="card">
      <div class="card-body">
        <h2>宠物订单管理</h2>
        
        <!-- 筛选区域 -->
        <div class="filter-section">
          <form @submit.prevent="handleFilter">
            <div class="form-row">
              <div class="form-group">
                <label for="filter-orderUuid">订单号</label>
                <input type="text" id="filter-orderUuid" v-model="filterForm.orderUuid" placeholder="请输入订单号">
              </div>
              <div class="form-group">
                <label for="filter-userName">用户</label>
                <input type="text" id="filter-userName" v-model="filterForm.userName" placeholder="请输入用户名">
              </div>
              <div class="form-group">
                <label for="filter-orderStatus">状态</label>
                <select id="filter-orderStatus" v-model="filterForm.orderStatus">
                  <option value="">全部状态</option>
                  <option value="1">待付款</option>
                  <option value="2">待发货</option>
                  <option value="3">已发货</option>
                  <option value="4">已完成</option>
                  <option value="5">已取消</option>
                </select>
              </div>
              <div class="form-actions">
                <button type="submit" class="btn btn-primary">筛选</button>
                <button type="button" class="btn btn-secondary" @click="resetFilter">重置</button>
              </div>
            </div>
          </form>
        </div>
        
        <div class="table-container">
          <table class="admin-table">
            <thead>
              <tr>
                <th>订单ID</th>
                <th>订单号</th>
                <th>用户</th>
                <th>宠物</th>
                <th>价格</th>
                <th>状态</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="order in orders" :key="order.id">
                <td>{{ order.id }}</td>
                <td>{{ order.orderUuid }}</td>
                <td>{{ order.userName || '-' }}</td>
                <td>
                  <div class="pet-info-cell">
                    <img :src="'http://localhost:8080/wangshangchongwudian/' + order.petImageUrl" :alt="order.petName" class="pet-thumb" v-if="order.petImageUrl">
                    <span>{{ order.petName }}</span>
                  </div>
                </td>
                <td>¥{{ order.price }}</td>
                <td>
                  <span class="status-badge" :class="getStatusClass(order.orderStatus)">
                    {{ getStatusText(order.orderStatus) }}
                  </span>
                </td>
                <td>
                  <button class="btn btn-info btn-sm" @click="handleView(order)">
                    查看详情
                  </button>
                  <!-- 根据订单状态显示不同的操作按钮 -->
                  <button v-if="order.orderStatus === 1" class="btn btn-primary btn-sm" @click="updateStatus(order.id, 2)">
                    标记为已付款
                  </button>
                  <button v-if="order.orderStatus === 1" class="btn btn-danger btn-sm" @click="updateStatus(order.id, 5)">
                    取消订单
                  </button>
                  <button v-if="order.orderStatus === 2" class="btn btn-primary btn-sm" @click="updateStatus(order.id, 3)">
                    发货
                  </button>
                  <button v-if="order.orderStatus === 3" class="btn btn-primary btn-sm" @click="updateStatus(order.id, 4)">
                    标记为已完成
                  </button>
                  <button class="btn btn-danger btn-sm" @click="handleDelete(order.id)">
                    删除
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    
    <!-- 查看详情对话框 -->
    <div v-if="showViewModal" class="modal-overlay" @click.self="showViewModal = false">
      <div class="modal modal-large">
        <div class="modal-header">
          <h3>订单详情</h3>
          <button class="modal-close" @click="showViewModal = false">×</button>
        </div>
        <div class="modal-body">
          <div v-if="currentOrder" class="order-detail">
            <div class="detail-section">
              <h4>基本信息</h4>
              <div class="detail-grid">
                <div class="detail-item">
                  <span class="detail-label">订单ID：</span>
                  <span class="detail-value">{{ currentOrder.id }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">订单号：</span>
                  <span class="detail-value">{{ currentOrder.orderUuid }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">订单金额：</span>
                  <span class="detail-value">¥{{ currentOrder.price }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">订单状态：</span>
                  <span class="detail-value">{{ getStatusText(currentOrder.orderStatus) }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">创建时间：</span>
                  <span class="detail-value">{{ formatDate(currentOrder.createTime) }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">更新时间：</span>
                  <span class="detail-value">{{ formatDate(currentOrder.updateTime) }}</span>
                </div>
              </div>
            </div>
            
            <div class="detail-section">
              <h4>宠物信息</h4>
              <div class="pet-info-full">
                <div class="pet-image" v-if="currentOrder.petImageUrl">
                  <img :src="'http://localhost:8080/wangshangchongwudian/' + currentOrder.petImageUrl" :alt="currentOrder.petName">
                </div>
                <div class="pet-details">
                  <h5>{{ currentOrder.petName }}</h5>
                  <p>性别：{{ currentOrder.petGender || '-' }}</p>
                  <p>年龄：{{ currentOrder.petAge || '-' }}</p>
                  <p>描述：{{ currentOrder.petDescription || '-' }}</p>
                </div>
              </div>
            </div>
            
            <div class="detail-section" v-if="currentOrder.paymentType">
              <h4>支付信息</h4>
              <div class="detail-grid">
                <div class="detail-item">
                  <span class="detail-label">支付方式：</span>
                  <span class="detail-value">{{ currentOrder.paymentTypeText || '-' }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showViewModal = false">关闭</button>
        </div>
      </div>
    </div>
    
    <!-- 编辑对话框 -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal">
        <div class="modal-header">
          <h3>编辑订单</h3>
          <button class="modal-close" @click="showModal = false">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="form-row">
              <div class="form-group">
                <label for="orderStatus">订单状态</label>
                <select id="orderStatus" v-model="formData.orderStatus">
                  <option value="1">待付款</option>
                  <option value="2">待发货</option>
                  <option value="3">已发货</option>
                  <option value="4">已完成</option>
                  <option value="5">已取消</option>
                </select>
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label for="remark">备注</label>
                <textarea id="remark" v-model="formData.remark" rows="4"></textarea>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="showModal = false">取消</button>
              <button type="submit" class="btn btn-primary">保存</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { petOrderApi } from '../../utils/api'
import confirm from '../../utils/confirm'

export default {
  name: 'PetOrderView',
  data() {
    return {
      orders: [],
      showModal: false,
      showViewModal: false,
      currentOrder: null,
      filterForm: {
        orderUuid: '',
        userName: '',
        orderStatus: ''
      },
      formData: {
        id: null,
        orderStatus: 1,
        remark: ''
      }
    }
  },
  mounted() {
    this.fetchOrders()
  },
  methods: {
    async fetchOrders() {
      try {
        const params = {}
        
        if (this.filterForm.orderUuid) {
          params.orderUuid = this.filterForm.orderUuid
        }
        if (this.filterForm.userName) {
          params.userName = this.filterForm.userName
        }
        if (this.filterForm.orderStatus !== '') {
          params.orderStatus = this.filterForm.orderStatus
        }
        
        const response = await petOrderApi.getList(params)
        this.orders = response.data.list
      } catch (error) {
        console.error('获取订单列表失败:', error)
      }
    },
    handleFilter() {
      this.fetchOrders()
    },
    resetFilter() {
      this.filterForm = {
        orderUuid: '',
        userName: '',
        orderStatus: ''
      }
      this.fetchOrders()
    },
    getStatusText(status) {
      const statusMap = {
        1: '待付款',
        2: '待发货',
        3: '已发货',
        4: '已完成',
        5: '已取消'
      }
      return statusMap[status] || '未知状态'
    },
    getStatusClass(status) {
      const classMap = {
        1: 'status-warning',
        2: 'status-info',
        3: 'status-primary',
        4: 'status-active',
        5: 'status-inactive'
      }
      return classMap[status] || 'status-inactive'
    },
    handleView(order) {
      this.currentOrder = order
      this.showViewModal = true
    },
    handleEdit(order) {
      this.formData = {
        id: order.id,
        orderStatus: order.orderStatus,
        remark: order.remark || ''
      }
      this.showModal = true
    },
    async handleSubmit() {
      try {
        await petOrderApi.update(this.formData)
        this.showModal = false
        this.fetchOrders()
      } catch (error) {
        console.error('更新订单失败:', error)
      }
    },
    async handleDelete(id) {
      const result = await confirm({
        title: '确认删除',
        message: '确定要删除这个订单吗？'
      })
      if (result) {
        try {
          await petOrderApi.delete([id])
          this.fetchOrders()
        } catch (error) {
          console.error('删除订单失败:', error)
        }
      }
    },

    async updateStatus(id, status) {
      try {
        await petOrderApi.update({ id, orderStatus: status })
        this.fetchOrders()
      } catch (error) {
        console.error('更新状态失败:', error)
      }
    },
    formatDate(dateString) {
      if (!dateString) return '-'
      const date = new Date(dateString)
      return date.toLocaleString()
    }
  }
}
</script>

<style scoped>
.admin-pet-order {
  padding: 0;
}

.card {
  background-color: var(--card);
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-base);
  overflow: hidden;
}

.card-body {
  padding: 24px;
}

.filter-section {
  margin-bottom: 24px;
  background-color: white;
  padding: 20px;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
}

.filter-section .form-row {
  display: flex;
  align-items: flex-end;
  gap: 24px;
  flex-wrap: wrap;
}

.filter-section .form-group {
  flex: 1;
  min-width: 200px;
  max-width: 300px;
}

.filter-section .form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--text-1);
  font-size: 14px;
}

.filter-section .form-group input,
.filter-section .form-group select {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  transition: all 0.3s ease;
  background-color: white;
}

.filter-section .form-group input:focus,
.filter-section .form-group select:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.filter-section .form-actions {
  display: flex;
  gap: 12px;
  align-items: flex-end;
}

.filter-section .form-actions .btn {
  padding: 12px 20px;
}

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

.pet-info-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.pet-thumb {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-sm);
  object-fit: cover;
  flex-shrink: 0;
}

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
  color: var(--warning);
  border: 1px solid var(--warning);
}

.btn-warning:hover {
  background-color: rgba(255, 152, 0, 0.1);
}

.btn-info {
  background-color: white;
  color: var(--info);
  border: 1px solid var(--info);
}

.btn-info:hover {
  background-color: rgba(33, 150, 243, 0.1);
}

.btn-sm {
  padding: 6px 12px;
  font-size: 12px;
  margin-right: 8px;
}

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
  max-width: 500px;
  max-height: 90vh;
  margin: 0;
  animation: modalSlideIn 0.3s ease-out;
  display: flex;
  flex-direction: column;
}

.modal-large {
  max-width: 800px;
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
.modal-body .form-group textarea,
.modal-body .form-group select {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  transition: all 0.3s ease;
  background-color: white;
}

.modal-body .form-group input:focus,
.modal-body .form-group textarea:focus,
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
}

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

.status-warning {
  background-color: rgba(255, 152, 0, 0.1);
  color: var(--warning);
}

.status-info {
  background-color: rgba(33, 150, 243, 0.1);
  color: var(--info);
}

.status-primary {
  background-color: rgba(66, 184, 131, 0.1);
  color: var(--primary);
}

.order-detail {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.detail-section {
  background-color: var(--card);
  border-radius: var(--radius-base);
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.detail-section h4 {
  margin: 0 0 16px 0;
  color: var(--text-1);
  font-size: 16px;
  font-weight: 600;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--border);
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.detail-label {
  font-size: 12px;
  color: var(--text-2);
  font-weight: 500;
}

.detail-value {
  font-size: 14px;
  color: var(--text-1);
  font-weight: 400;
}

.pet-info-full {
  display: flex;
  gap: 20px;
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
  gap: 8px;
}

.pet-details h5 {
  margin: 0 0 8px 0;
  color: var(--text-1);
  font-size: 16px;
  font-weight: 600;
}

.pet-details p {
  margin: 0;
  font-size: 14px;
  color: var(--text-2);
}
</style>

