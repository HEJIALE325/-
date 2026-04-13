<template>
  <div class="admin-pet-category">
    <h1>宠物分类管理</h1>
    <div class="admin-actions">
      <button class="btn btn-primary" @click="handleAdd">新增分类</button>
    </div>
    <table class="admin-table">
      <thead>
        <tr>
          <th>分类ID</th>
          <th>分类名称</th>
          <th>父分类</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="category in categories" :key="category.id">
          <td>{{ category.id }}</td>
          <td>{{ category.name }}</td>
          <td>{{ category.parentName || '顶级分类' }}</td>
          <td>
            <button class="btn btn-sm btn-warning" @click="handleEdit(category)">编辑</button>
            <button class="btn btn-sm btn-danger" @click="handleDelete(category.id)">删除</button>
          </td>
        </tr>
      </tbody>
    </table>
    <!-- 新增/编辑对话框 -->
    <div class="modal" v-if="showModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>{{ isEdit ? '编辑分类' : '新增分类' }}</h3>
          <button class="close" @click="showModal = false">&times;</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="form-group">
              <label for="name">分类名称</label>
              <input type="text" id="name" v-model="formData.name" required>
            </div>
            <div class="form-group">
              <label for="parentId">父分类</label>
              <select id="parentId" v-model="formData.parentId">
                <option value="0">顶级分类</option>
                <option v-for="category in categories" :key="category.id" :value="category.id" v-if="isEdit ? category.id !== formData.id : true">
                  {{ category.name }}
                </option>
              </select>
            </div>
            <div class="form-group">
              <label for="sort">排序</label>
              <input type="number" id="sort" v-model="formData.sort" required>
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
import { petCategoryApi } from '../../utils/api'

export default {
  name: 'PetCategoryView',
  data() {
    return {
      categories: [],
      showModal: false,
      isEdit: false,
      formData: {
        id: null,
        name: '',
        parentId: 0,
        sort: 0
      }
    }
  },
  mounted() {
    this.fetchCategories()
  },
  methods: {
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
        parentId: 0,
        sort: 0
      }
      this.showModal = true
    },
    handleEdit(category) {
      this.isEdit = true
      this.formData = { ...category }
      this.showModal = true
    },
    async handleSubmit() {
      try {
        if (this.isEdit) {
          await petCategoryApi.update(this.formData)
        } else {
          await petCategoryApi.save(this.formData)
        }
        this.showModal = false
        this.fetchCategories()
      } catch (error) {
        console.error('保存分类失败:', error)
      }
    },
    async handleDelete(id) {
      if (confirm('确定要删除这个分类吗？')) {
        try {
          await petCategoryApi.delete([id])
          this.fetchCategories()
        } catch (error) {
          console.error('删除分类失败:', error)
        }
      }
    }
  }
}
</script>

<style scoped>
.admin-pet-category {
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

.form-group input,
.form-group select {
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

.btn-sm {
  padding: 4px 8px;
  font-size: 12px;
}
</style>