<template>
  <div class="admin-pet-order">
    <h1>宠物订单管理</h1>
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
          <td>{{ getStatusText(order.orderStatus) }}</td>
          <td>
            <button class="btn btn-sm btn-warning" @click="handleEdit(order)">编辑</button>
            <button class="btn btn-sm btn-danger" @click="handleDelete(order.id)">删除</button>
            <button class="btn btn-sm btn-info" @click="handleStatus(order.id, order.orderStatus)">更新状态</button>
          </td>
        </tr>
      </tbody>
    </table>
    <!-- 编辑对话框 -->
    <div class="modal" v-if="showModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>编辑订单</h3>
          <button class="close" @click="showModal = false">&times;</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
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
            <div class="form-group">
              <label for="remark">备注</label>
              <textarea id="remark" v-model="formData.remark" rows="3"></textarea>
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
  padding: 20px;
}

.admin-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.admin-table th,
.admin-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.admin-table th {
  background-color: #f2f2f2;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  width: 400px;
  max-width: 90%;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.close {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group select,
.form-group textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
  gap: 10px;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-primary {
  background-color: #007bff;
  color: white;
}

.btn-warning {
  background-color: #ffc107;
  color: black;
}

.btn-danger {
  background-color: #dc3545;
  color: white;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
}

.btn-info {
  background-color: #17a2b8;
  color: white;
}

.btn-sm {
  padding: 4px 8px;
  font-size: 12px;
}
</style>