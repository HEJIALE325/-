<template>
  <div class="admin-pet-order">
    <div class="card">
      <div class="card-body">
        <h2>宠物订单管理</h2>
        
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
                <td>{{ order.userName }}</td>
                <td>{{ order.petName }}</td>
                <td>{{ order.price }}</td>
                <td>
                  <span class="status-badge" :class="getStatusClass(order.orderStatus)">
                    {{ getStatusText(order.orderStatus) }}
                  </span>
                </td>
                <td>
                  <button class="btn btn-secondary btn-sm" @click="handleEdit(order)">
                    编辑
                  </button>
                  <button class="btn btn-danger btn-sm" @click="handleDelete(order.id)">
                    删除
                  </button>
                  <button class="btn btn-primary btn-sm" @click="handleStatus(order.id, order.orderStatus)">
                    更新状态
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
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
                  <option value="0">待支付</option>
                  <option value="1">已支付</option>
                  <option value="2">已发货</option>
                  <option value="3">已完成</option>
                  <option value="4">已取消</option>
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

export default {
  name: 'PetOrderView',
  data() {
    return {
      orders: [],
      showModal: false,
      formData: {
        id: null,
        orderStatus: 0,
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
        const response = await petOrderApi.getList()
        this.orders = response.data.data
      } catch (error) {
        console.error('获取订单列表失败:', error)
      }
    },
    getStatusText(status) {
      const statusMap = {
        0: '待支付',
        1: '已支付',
        2: '已发货',
        3: '已完成',
        4: '已取消'
      }
      return statusMap[status] || '未知状态'
    },
    getStatusClass(status) {
      const classMap = {
        0: 'status-warning',
        1: 'status-info',
        2: 'status-primary',
        3: 'status-active',
        4: 'status-inactive'
      }
      return classMap[status] || 'status-inactive'
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
      if (confirm('确定要删除这个订单吗？')) {
        try {
          await petOrderApi.delete([id])
          this.fetchOrders()
        } catch (error) {
          console.error('删除订单失败:', error)
        }
      }
    },
    handleStatus(id, currentStatus) {
      // 简单的状态流转
      let newStatus = currentStatus
      switch (currentStatus) {
        case 0: // 待支付
          newStatus = 1 // 已支付
          break
        case 1: // 已支付
          newStatus = 2 // 已发货
          break
        case 2: // 已发货
          newStatus = 3 // 已完成
          break
        default:
          return
      }
      this.updateStatus(id, newStatus)
    },
    async updateStatus(id, status) {
      try {
        await petOrderApi.update({ id, orderStatus: status })
        this.fetchOrders()
      } catch (error) {
        console.error('更新状态失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.admin-pet-order {
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
</style>