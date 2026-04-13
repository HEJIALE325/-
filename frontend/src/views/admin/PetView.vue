<template>
  <div class="admin-pet">
    <h1>宠物管理</h1>
    <div class="admin-actions">
      <button class="btn btn-primary" @click="handleAdd">新增宠物</button>
    </div>
    <table class="admin-table">
      <thead>
        <tr>
          <th>宠物ID</th>
          <th>宠物名称</th>
          <th>分类</th>
          <th>价格</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="pet in pets" :key="pet.id">
          <td>{{ pet.id }}</td>
          <td>{{ pet.name }}</td>
          <td>{{ pet.categoryName }}</td>
          <td>{{ pet.price }}</td>
          <td>{{ pet.status === 1 ? '上架' : '下架' }}</td>
          <td>
            <button class="btn btn-sm btn-warning" @click="handleEdit(pet)">编辑</button>
            <button class="btn btn-sm btn-danger" @click="handleDelete(pet.id)">删除</button>
            <button class="btn btn-sm btn-info" @click="handleStatus(pet.id, pet.status === 1 ? 0 : 1)">
              {{ pet.status === 1 ? '下架' : '上架' }}
            </button>
          </td>
        </tr>
      </tbody>
    </table>
    <!-- 新增/编辑对话框 -->
    <div class="modal" v-if="showModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>{{ isEdit ? '编辑宠物' : '新增宠物' }}</h3>
          <button class="close" @click="showModal = false">&times;</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="form-group">
              <label for="name">宠物名称</label>
              <input type="text" id="name" v-model="formData.name" required>
            </div>
            <div class="form-group">
              <label for="categoryId">分类</label>
              <select id="categoryId" v-model="formData.categoryId" required>
                <option value="">请选择分类</option>
                <option v-for="category in categories" :key="category.id" :value="category.id">
                  {{ category.name }}
                </option>
              </select>
            </div>
            <div class="form-group">
              <label for="price">价格</label>
              <input type="number" id="price" v-model="formData.price" required step="0.01">
            </div>
            <div class="form-group">
              <label for="description">描述</label>
              <textarea id="description" v-model="formData.description" rows="3"></textarea>
            </div>
            <div class="form-group">
              <label for="status">状态</label>
              <select id="status" v-model="formData.status">
                <option value="1">上架</option>
                <option value="0">下架</option>
              </select>
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
import { petApi, petCategoryApi } from '../../utils/api'

export default {
  name: 'PetView',
  data() {
    return {
      pets: [],
      categories: [],
      showModal: false,
      isEdit: false,
      formData: {
        id: null,
        name: '',
        categoryId: '',
        price: 0,
        description: '',
        status: 1
      }
    }
  },
  mounted() {
    this.fetchPets()
    this.fetchCategories()
  },
  methods: {
    async fetchPets() {
      try {
        const response = await petApi.getList()
        this.pets = response.data.data
      } catch (error) {
        console.error('获取宠物列表失败:', error)
      }
    },
    async fetchCategories() {
      try {
        const response = await petCategoryApi.getList()
        this.categories = response.data.data
      } catch (error) {
        console.error('获取分类列表失败:', error)
      }
    },
    handleAdd() {
      this.isEdit = false
      this.formData = {
        id: null,
        name: '',
        categoryId: '',
        price: 0,
        description: '',
        status: 1
      }
      this.showModal = true
    },
    handleEdit(pet) {
      this.isEdit = true
      this.formData = { ...pet }
      this.showModal = true
    },
    async handleSubmit() {
      try {
        if (this.isEdit) {
          await petApi.update(this.formData)
        } else {
          await petApi.save(this.formData)
        }
        this.showModal = false
        this.fetchPets()
      } catch (error) {
        console.error('保存宠物失败:', error)
      }
    },
    async handleDelete(id) {
      if (confirm('确定要删除这个宠物吗？')) {
        try {
          await petApi.delete([id])
          this.fetchPets()
        } catch (error) {
          console.error('删除宠物失败:', error)
        }
      }
    },
    async handleStatus(id, status) {
      try {
        await petApi.update({ id, status })
        this.fetchPets()
      } catch (error) {
        console.error('更新状态失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.admin-pet {
  padding: 20px;
}

.admin-actions {
  margin-bottom: 20px;
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
  width: 500px;
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

.form-group input,
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