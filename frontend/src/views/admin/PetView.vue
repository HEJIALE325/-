<template>
  <div class="admin-pet">
    <div class="card">
      <div class="card-body">
        <h2>宠物管理</h2>
        <!-- 操作栏 -->
        <div class="action-section">
          <button class="btn btn-primary" @click="handleAdd">
            新增宠物
          </button>
        </div>
        
        <!-- 筛选区域 -->
        <div class="filter-section">
          <form @submit.prevent="handleFilter">
            <div class="form-row">
              <div class="form-group">
                <label for="filter-name">宠物名称</label>
                <input type="text" id="filter-name" v-model="filterForm.name" placeholder="请输入宠物名称">
              </div>
              <div class="form-group">
                <label for="filter-category">分类</label>
                <select id="filter-category" v-model="filterForm.categoryId">
                  <option value="">全部分类</option>
                  <option v-for="category in categories" :key="category.id" :value="category.id">
                    {{ category.name }}
                  </option>
                </select>
              </div>
              <div class="form-group">
                <label for="filter-status">状态</label>
                <select id="filter-status" v-model="filterForm.status">
                  <option value="">全部状态</option>
                  <option value="1">上架</option>
                  <option value="0">下架</option>
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
                <th>宠物ID</th>
                <th>图片</th>
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
                <td>
                  <div class="pet-image-small">
                    <img v-if="pet.imageUrl" :src="getImageUrl(pet.imageUrl)" alt="宠物图片">
                    <div v-else class="no-image">无图片</div>
                  </div>
                </td>
                <td>{{ pet.name }}</td>
                <td>{{ pet.categoryName }}</td>
                <td>{{ pet.price }}</td>
                <td>
                    <span class="status-badge" :class="pet.status === 1 ? 'status-active' : 'status-inactive'">
                      {{ pet.status === 1 ? '上架' : '下架' }}
                    </span>
                  </td>
                <td>
                  <button class="btn btn-secondary btn-sm" @click="handleEdit(pet)">
                    编辑
                  </button>
                  <button class="btn btn-danger btn-sm" @click="handleDelete(pet.id)">
                    删除
                  </button>
                  <button 
                    class="btn btn-sm" 
                    :class="pet.status === 1 ? 'btn-warning' : 'btn-success'"
                    @click="handleStatus(pet.id, pet.status === 1 ? 0 : 1)"
                  >
                    {{ pet.status === 1 ? '下架' : '上架' }}
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    
    <!-- 新增/编辑对话框 -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal">
        <div class="modal-header">
          <h3>{{ isEdit ? '编辑宠物' : '新增宠物' }}</h3>
          <button class="modal-close" @click="showModal = false">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="form-row">
              <div class="form-group">
                <label for="name">宠物名称</label>
                <input type="text" id="name" v-model="formData.name" required>
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label for="categoryId">分类</label>
                <div class="tree-select-container">
                  <div class="tree-select-input" @click="showCategoryTree = !showCategoryTree">
                    <span v-if="formData.categoryId">{{ getCategoryName(formData.categoryId) }}</span>
                    <span v-else class="placeholder">请选择分类</span>
                    <span class="tree-select-arrow">{{ showCategoryTree ? '▼' : '▶' }}</span>
                  </div>
                  <div v-if="showCategoryTree" class="tree-select-dropdown">
                    <div class="tree-select-tree">
                      <div v-for="category in categoryTree" :key="category.id" class="tree-node">
                        <div class="tree-node-content" @click="selectCategory(category.id, category.name)">
                          {{ category.name }}
                        </div>
                        <div v-if="category.children && category.children.length > 0" class="tree-node-children">
                          <div v-for="child in category.children" :key="child.id" class="tree-node child-node">
                            <div class="tree-node-content" @click="selectCategory(child.id, child.name)">
                              {{ child.name }}
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label for="status">状态</label>
                <select id="status" v-model="formData.status">
                  <option value="1">上架</option>
                  <option value="0">下架</option>
                </select>
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label for="price">价格</label>
                <input type="number" id="price" v-model="formData.price" required step="0.01">
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label for="gender">性别</label>
                <select id="gender" v-model="formData.gender">
                  <option value="">请选择性别</option>
                  <option value="公">公</option>
                  <option value="母">母</option>
                </select>
              </div>
              <div class="form-group">
                <label for="age">年龄</label>
                <input type="text" id="age" v-model="formData.age" placeholder="如：1个月">
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label for="description">描述</label>
                <textarea id="description" v-model="formData.description" rows="4"></textarea>
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label for="imageUrl">宠物图片</label>
                <div class="upload-container">
                  <input type="file" ref="fileInput" @change="handleImageUpload" style="display: none;" accept="image/*">
                  <div v-if="formData.imageUrl" class="image-preview">
                    <img :src="getImageUrl(formData.imageUrl)" alt="宠物图片">
                    <button class="btn btn-danger btn-sm" @click.stop.prevent="formData.imageUrl = ''">删除</button>
                  </div>
                  <button v-else class="btn btn-secondary" @click.stop.prevent="$refs.fileInput.click()">
                    选择图片
                  </button>
                </div>
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
import { petApi, petCategoryApi, fileApi } from '../../utils/api'

export default {
  name: 'PetView',
  data() {
    return {
      pets: [],
      categories: [],
      categoryTree: [],
      showModal: false,
      isEdit: false,
      showCategoryTree: false,
      filterForm: {
        name: '',
        categoryId: '',
        status: ''
      },
      formData: {
        id: null,
        name: '',
        categoryId: '',
        price: 0,
        description: '',
        imageUrl: '',
        status: 1,
        gender: '',
        breed: '',
        age: ''
      }
    }
  },
  mounted() {
    this.fetchCategories().then(() => {
      this.fetchPets()
    })
  },
  methods: {
    async fetchPets() {
      try {
        const params = {
          page: 1,
          limit: 100
        }
        
        // Add filter parameters if they have values
        if (this.filterForm.name) {
          params.name = this.filterForm.name
        }
        if (this.filterForm.categoryId) {
          params.categoryId = this.filterForm.categoryId
        }
        if (this.filterForm.status !== '') {
          params.status = this.filterForm.status
        }
        
        const response = await petApi.getPage(params)
        const pets = response.data.list
        // Map category IDs to category names
        this.pets = pets.map(pet => ({
          ...pet,
          categoryName: this.getCategoryName(pet.categoryId)
        }))
      } catch (error) {
        console.error('获取宠物列表失败:', error)
      }
    },
    handleFilter() {
      this.fetchPets()
    },
    resetFilter() {
      this.filterForm = {
        name: '',
        categoryId: '',
        status: ''
      }
      this.fetchPets()
    },
    async fetchCategories() {
      try {
        const response = await petCategoryApi.getList()
        this.categories = response.data.list
        this.categoryTree = this.buildCategoryTree(this.categories)
      } catch (error) {
        console.error('获取分类列表失败:', error)
      }
    },
    buildCategoryTree(categories) {
      const categoryMap = {}
      const tree = []
      
      categories.forEach(category => {
        categoryMap[category.id] = { ...category, children: [] }
      })
      
      categories.forEach(category => {
        if (category.parentId === 0) {
          tree.push(categoryMap[category.id])
        } else {
          if (categoryMap[category.parentId]) {
            categoryMap[category.parentId].children.push(categoryMap[category.id])
          }
        }
      })
      
      return tree
    },
    handleAdd() {
      this.isEdit = false
      this.formData = {
        id: null,
        name: '',
        categoryId: '',
        price: 0,
        description: '',
        imageUrl: '',
        status: 1
      }
      this.showModal = true
    },
    handleEdit(pet) {
      this.isEdit = true
      this.formData = { 
        ...pet,
        imageUrl: pet.imageUrl || ''
      }
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
        await this.fetchCategories()
        this.fetchPets()
      } catch (error) {
        console.error('保存宠物失败:', error)
      }
    },
    async handleDelete(id) {
      if (confirm('确定要删除这个宠物吗？')) {
        try {
          await petApi.delete([id])
          await this.fetchCategories()
          this.fetchPets()
        } catch (error) {
          console.error('删除宠物失败:', error)
        }
      }
    },
    async handleStatus(id, status) {
      try {
        await petApi.update({ id, status })
        await this.fetchCategories()
        this.fetchPets()
      } catch (error) {
        console.error('更新状态失败:', error)
      }
    },
    getCategoryName(categoryId) {
      const findCategory = (categories, id) => {
        for (const category of categories) {
          if (category.id === id) return category.name
          if (category.children) {
            const found = findCategory(category.children, id)
            if (found) return found
          }
        }
        return ''
      }
      return findCategory(this.categoryTree, categoryId)
    },
    selectCategory(categoryId, categoryName) {
      this.formData.categoryId = categoryId
      this.showCategoryTree = false
    },
    closeCategoryTree() {
      this.showCategoryTree = false
    },
    async handleImageUpload(e) {
      const file = e.target.files[0]
      if (!file) return
      
      try {
        const response = await fileApi.upload('1', file)
        if (response.code === 0) {
          this.formData.imageUrl = 'upload/' + response.file
        } else {
          alert('上传失败：' + response.msg)
        }
      } catch (error) {
        console.error('图片上传失败:', error)
        alert('上传失败，请重试')
      }
    },
    getImageUrl(imageUrl) {
      return imageUrl ? `http://localhost:8080/wangshangchongwudian/${imageUrl}` : ''
    }
  }
}
</script>

<style scoped>
.admin-pet {
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

/* 操作栏样式 */
.action-section {
  margin-bottom: 24px;
  display: flex;
  justify-content: flex-start;
  gap: 12px;
}

/* 筛选区域样式 */
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

.btn-success {
  background-color: white;
  color: var(--success);
  border: 1px solid var(--success);
}

.btn-success:hover {
  background-color: rgba(76, 175, 80, 0.1);
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
  max-width: 600px;
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

/* 树选择器样式 */
.tree-select-container {
  position: relative;
  width: 100%;
}

.tree-select-input {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  background-color: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tree-select-input:hover {
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.tree-select-input .placeholder {
  color: var(--text-3);
}

.tree-select-arrow {
  font-size: 12px;
  color: var(--text-3);
  transition: transform 0.3s ease;
}

.tree-select-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  margin-top: 4px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  background-color: white;
  box-shadow: var(--shadow-base);
  z-index: 1000;
  max-height: 300px;
  overflow-y: auto;
}

.tree-select-tree {
  padding: 8px 0;
}

.tree-node {
  padding: 0;
}

.tree-node-content {
  padding: 8px 16px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.tree-node-content:hover {
  background-color: var(--hover-bg);
}

.child-node {
  padding-left: 24px;
}

.tree-node-children {
  margin-left: 16px;
}

/* 图片上传样式 */
.upload-container {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.image-preview {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  background-color: white;
}

.image-preview img {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: var(--radius-base);
}

.upload-container button {
  align-self: flex-start;
}

/* 宠物列表图片样式 */
.pet-image-small {
  width: 60px;
  height: 60px;
  border-radius: var(--radius-base);
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid var(--border);
}

.pet-image-small img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.no-image {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--bg);
  color: var(--text-3);
  font-size: 12px;
  text-align: center;
  padding: 4px;
}
</style>