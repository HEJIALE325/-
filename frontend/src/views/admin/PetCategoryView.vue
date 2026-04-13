<template>
  <div class="admin-pet-category">
    <h2>宠物分类管理</h2>
    <div class="card">
      <div class="card-body">
        <div class="category-types">
          <h3>宠物分类</h3>
          <div class="type-buttons">
            <button
              class="btn"
              :class="{ 'btn-primary': selectedParentId === 0, 'btn-secondary': selectedParentId !== 0 }"
              @click="selectParent(0)"
            >
              全部
            </button>
            <button
              v-for="parent in parentCategories"
              :key="parent.id"
              class="btn"
              :class="{ 'btn-primary': selectedParentId === parent.id, 'btn-secondary': selectedParentId !== parent.id }"
              @click="selectParent(parent.id)"
            >
              {{ parent.name }}
            </button>
          </div>
        </div>

        <div class="category-items">
          <div class="action-section">
            <button class="btn btn-primary" @click="handleAdd">
              {{ selectedParentId === 0 ? '新增顶级分类' : '新增子分类' }}
            </button>
          </div>

          <div class="table-container">
            <table class="admin-table">
              <thead>
                <tr>
                  <th>分类ID</th>
                  <th>分类名称</th>
                  <th>排序</th>
                  <th>创建时间</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="category in displayCategories" :key="category.id">
                  <td>{{ category.id }}</td>
                  <td>{{ category.name }}</td>
                  <td>{{ category.sort }}</td>
                  <td>{{ formatDate(category.createTime) }}</td>
                  <td>
                    <button class="btn btn-secondary btn-sm" @click="handleEdit(category)">
                      编辑
                    </button>
                    <button class="btn btn-danger btn-sm" @click="handleDelete(category.id)">
                      删除
                    </button>
                  </td>
                </tr>
                <tr v-if="displayCategories.length === 0">
                  <td colspan="5" class="empty-text">暂无数据</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal">
        <div class="modal-header">
          <h3>{{ isEdit ? '编辑分类' : '新增分类' }}</h3>
          <button class="modal-close" @click="showModal = false">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="form-row">
              <div class="form-group">
                <label for="name">分类名称</label>
                <input type="text" id="name" v-model="formData.name" required>
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label for="parentId">父分类</label>
                <select id="parentId" v-model="formData.parentId">
                  <option value="0">顶级分类</option>
                  <option v-for="parent in parentCategories" :key="parent.id" :value="parent.id" v-if="isEdit ? parent.id !== formData.id : true">
                    {{ parent.name }}
                  </option>
                </select>
              </div>
              <div class="form-group">
                <label for="sort">排序</label>
                <input type="number" id="sort" v-model="formData.sort" required>
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
import { petCategoryApi } from '../../utils/api'
import confirm from '../../utils/confirm'

export default {
  name: 'PetCategoryView',
  data() {
    return {
      categories: [],
      selectedParentId: 0,
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
  computed: {
    parentCategories() {
      return this.categories.filter(c => c.parentId === 0)
    },
    displayCategories() {
      if (this.selectedParentId === 0) {
        return this.categories.filter(c => c.parentId === 0)
      }
      return this.categories.filter(c => c.parentId === this.selectedParentId)
    }
  },
  mounted() {
    this.fetchCategories()
  },
  methods: {
    async fetchCategories() {
      try {
        const response = await petCategoryApi.getList()
        this.categories = response.data.list
      } catch (error) {
        console.error('获取分类列表失败:', error)
      }
    },
    selectParent(parentId) {
      this.selectedParentId = parentId
    },
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleString()
    },
    handleAdd() {
      this.isEdit = false
      this.formData = {
        id: null,
        name: '',
        parentId: this.selectedParentId === 0 ? 0 : this.selectedParentId,
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
      const result = await confirm({
        title: '确认删除',
        message: '确定要删除这个分类吗？'
      })
      if (result) {
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
  padding: 0;
}

h2 {
  margin-bottom: 24px;
  color: var(--text-1);
  font-size: 20px;
}

.category-types {
  margin-bottom: 32px;
  padding-bottom: 20px;
  border-bottom: 1px solid var(--border);
}

.category-types h3 {
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

.category-items {
  margin-top: 24px;
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

.action-section {
  margin-bottom: 24px;
  display: flex;
  justify-content: flex-start;
  gap: 12px;
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

.empty-text {
  text-align: center;
  color: var(--text-3);
  padding: 40px 20px !important;
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
</style>
