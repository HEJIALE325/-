<template>
  <div class="pets-page">
    <Header />
    <main class="pets-content">
      <div class="container">
        <div class="pets-header">
          <h1>宠物列表</h1>
          <p>选择您心仪的宠物</p>
        </div>
        
        <!-- 筛选 -->
        <div class="filter-section" @click="handleClickOutside">
          <div class="filter-left">
            <div class="category-tree-container">
              <label>分类：</label>
              <div class="tree-select-container">
                <div class="tree-select-input" @click.stop="toggleCategoryTree">
                  <span v-if="selectedCategory">{{ getCategoryName(selectedCategory) }}</span>
                  <span v-else class="placeholder">请选择分类</span>
                  <span class="tree-select-arrow">{{ showCategoryTree ? '▼' : '▶' }}</span>
                </div>
                <div v-if="showCategoryTree" class="tree-select-dropdown">
                  <div class="tree-select-tree">
                    <div 
                      class="tree-node"
                      @click="selectCategory('')"
                      :class="{ active: selectedCategory === '' }"
                    >
                      <div class="tree-node-content">全部分类</div>
                    </div>
                    <div v-for="category in categoryTree" :key="category.id" class="tree-node">
                      <div 
                        class="tree-node-content"
                        @click="selectCategory(category.id)"
                        :class="{ active: selectedCategory === category.id }"
                      >
                        {{ category.name }}
                      </div>
                      <div v-if="category.children && category.children.length > 0" class="tree-node-children">
                        <div 
                          v-for="child in category.children" 
                          :key="child.id" 
                          class="tree-node child-node"
                          @click="selectCategory(child.id)"
                          :class="{ active: selectedCategory === child.id }"
                        >
                          <div class="tree-node-content">{{ child.name }}</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="name-filter">
              <label>名称：</label>
              <input type="text" v-model="searchParams.name" placeholder="宠物名称">
            </div>
          </div>
          <div class="filter-right">
            <button class="btn btn-primary" @click="handleSearch">查询</button>
            <button class="btn btn-secondary" @click="handleReset">重置</button>
          </div>
        </div>
        
        <!-- 宠物列表 -->
        <div class="pets-grid" v-if="pets.length > 0">
          <div 
            v-for="pet in pets" 
            :key="pet.id"
            class="pet-card"
            @click="viewPetDetail(pet.id)"
          >
            <div class="pet-image">
              <img :src="pet.imageUrl" :alt="pet.name">
            </div>
            <div class="pet-info">
              <h3>{{ pet.name }}</h3>
              <p class="pet-category">{{ pet.categoryName }}</p>
              <p class="pet-breed">{{ pet.breed || '未提供' }}</p>
              <p class="pet-gender-age">{{ pet.gender }} · {{ pet.age }}</p>
              <div class="pet-price">¥{{ pet.price }}</div>
              <button class="btn btn-primary buy-btn" @click.stop="buyPet(pet.id)">
                立即购买
              </button>
            </div>
          </div>
        </div>
        
        <!-- 无数据 -->
        <div class="no-data" v-else>
          <p>暂无宠物</p>
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { petApi, petCategoryApi } from '../../utils/api'
import message from '../../utils/message'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'

const router = useRouter()
const currentPage = ref(1)
const total = ref(0)
const totalPage = ref(1)
const pets = ref([])
const categories = ref([])
const categoryTree = ref([])
const selectedCategory = ref('')
const showCategoryTree = ref(false)
const loading = ref(false)

// 搜索参数
const searchParams = ref({
  name: ''
})

// 获取宠物分类
const fetchCategories = async () => {
  try {
    const response = await petCategoryApi.getList()
    if (response.code === 0) {
      const allCategories = response.data.list || []
      // 构建分类树
      categoryTree.value = buildCategoryTree(allCategories)
    } else {
      message.error('获取宠物分类失败')
    }
  } catch (error) {
    console.error('获取宠物分类失败:', error)
    message.error('获取宠物分类失败，请稍后重试')
  }
}

// 构建分类树
const buildCategoryTree = (categories) => {
  const categoryMap = {}
  const tree = []
  
  // 首先创建所有分类的映射
  categories.forEach(category => {
    categoryMap[category.id] = {
      ...category,
      children: []
    }
  })
  
  // 构建树结构
  categories.forEach(category => {
    if (category.parentId === 0) {
      // 顶级分类
      tree.push(categoryMap[category.id])
    } else {
      // 子分类
      if (categoryMap[category.parentId]) {
        categoryMap[category.parentId].children.push(categoryMap[category.id])
      }
    }
  })
  
  return tree
}

// 选择分类
const selectCategory = (categoryId) => {
  selectedCategory.value = selectedCategory.value === categoryId ? '' : categoryId
  showCategoryTree.value = false
  currentPage.value = 1
  fetchPets()
}

// 切换分类树显示
const toggleCategoryTree = () => {
  showCategoryTree.value = !showCategoryTree.value
}

// 点击外部关闭分类树
const handleClickOutside = (event) => {
  if (!event.target.closest('.category-tree-container')) {
    showCategoryTree.value = false
  }
}

// 获取宠物列表
const fetchPets = async () => {
  try {
    loading.value = true
    
    const params = {
      page: currentPage.value,
      limit: 8,
      sort: 'id',
      order: 'desc',
      status: 1, // 只显示上架的宠物
      ...searchParams.value
    }
    
    if (selectedCategory.value) {
      params.categoryId = selectedCategory.value
    }
    
    const response = await petApi.getPage(params)
    
    if (response.code === 0) {
      pets.value = (response.data.list || []).map(pet => ({
        ...pet,
        imageUrl: pet.imageUrl ? `http://localhost:8080/wangshangchongwudian/${pet.imageUrl}` : 'https://via.placeholder.com/300x300'
      }))
      total.value = response.data.total
      totalPage.value = Math.ceil(total.value / 8)
    } else {
      message.error('获取宠物列表失败')
    }
  } catch (error) {
    console.error('获取宠物列表失败:', error)
    message.error('获取宠物列表失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 切换页码
const handlePageChange = (newPage) => {
  if (newPage >= 1 && newPage <= totalPage.value) {
    currentPage.value = newPage
    fetchPets()
  }
}



// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchPets()
}

// 重置
const handleReset = () => {
  searchParams.value = {
    name: ''
  }
  selectedCategory.value = ''
  currentPage.value = 1
  fetchPets()
}

// 获取分类名称
const getCategoryName = (categoryId) => {
  if (!categoryId) return '全部分类'
  
  const findCategory = (categories, id) => {
    for (const category of categories) {
      if (category.id === id) return category.name
      if (category.children) {
        const found = findCategory(category.children, id)
        if (found) return found
      }
    }
    return '未知分类'
  }
  
  return findCategory(categoryTree.value, categoryId)
}

// 查看宠物详情
const viewPetDetail = (petId) => {
  router.push(`/pet/${petId}`)
}

// 立即购买
const buyPet = (petId) => {
  router.push(`/order-confirm?type=pet&id=${petId}`)
}

// 页面加载时的初始化
onMounted(() => {
  fetchCategories()
  fetchPets()
})
</script>

<style scoped>
.pets-page {
  min-height: 100vh;
  background-color: var(--bg);
}

.pets-content {
  padding: var(--spacing-2xl) 0;
}

.pets-header {
  text-align: center;
  margin-bottom: var(--spacing-2xl);
}

.pets-header h1 {
  color: var(--text-1);
  font-size: var(--fs-2xl);
  font-weight: 600;
  margin-bottom: var(--spacing-sm);
}

.pets-header p {
  color: var(--text-2);
  font-size: var(--fs-base);
  margin: 0;
}

/* 筛选区域样式 */
.filter-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-xl);
  padding: var(--spacing-base);
  background: var(--card);
  border-radius: var(--radius-base);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.filter-left {
  display: flex;
  gap: var(--spacing-xl);
  align-items: center;
  flex-wrap: wrap;
}

.filter-right {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.filter-right .btn {
  font-size: var(--fs-sm);
  padding: var(--spacing-xs) var(--spacing-base);
}

.category-tree-container, .name-filter {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.category-tree-container label, .name-filter label {
  font-weight: 500;
  color: var(--text-1);
  font-size: var(--fs-sm);
}

.name-filter input {
  width: 150px;
  padding: var(--spacing-xs) var(--spacing-base);
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: var(--fs-sm);
  background-color: white;
  color: var(--text-1);
  font-family: inherit;
  transition: all 0.3s ease;
}

.name-filter input:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

/* 树选择器样式 */
.tree-select-container {
  position: relative;
  min-width: 150px;
}

.tree-select-input {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-xs) var(--spacing-base);
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: var(--fs-sm);
  background-color: white;
  color: var(--text-1);
  cursor: pointer;
  transition: all 0.3s ease;
}

.tree-select-input:hover {
  border-color: var(--primary);
}

.tree-select-input:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.tree-select-input .placeholder {
  color: var(--text-3);
}

.tree-select-arrow {
  font-size: var(--fs-xs);
  color: var(--text-2);
  transition: transform 0.3s ease;
}

.tree-select-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  margin-top: 4px;
  background-color: white;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-md);
  z-index: 1000;
  max-height: 300px;
  overflow-y: auto;
}

.tree-select-tree {
  padding: var(--spacing-xs);
}

.tree-node {
  margin-bottom: 4px;
}

.tree-node-content {
  padding: 8px 12px;
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: var(--fs-sm);
}

.tree-node-content:hover {
  background-color: var(--hover-bg);
}

.tree-node.active .tree-node-content {
  background-color: var(--primary);
  color: white;
}

.tree-node-children {
  margin-left: 20px;
  margin-top: 4px;
}

.tree-node.child-node .tree-node-content {
  font-size: var(--fs-xs);
}

.category-tree-container {
  position: relative;
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

/* 宠物网格 */
.pets-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
  margin-bottom: var(--spacing-xl);
}

.pet-card {
  background-color: white;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
  overflow: hidden;
  transition: all 0.3s ease;
  cursor: pointer;
}

.pet-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
}

.pet-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.pet-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.pet-card:hover .pet-image img {
  transform: scale(1.05);
}

.pet-info {
  padding: 20px;
}

.pet-info h3 {
  margin: 0 0 var(--spacing-sm) 0;
  color: var(--text-1);
  font-size: var(--fs-lg);
  font-weight: 600;
  line-height: 1.3;
}

.pet-category {
  margin: 0 0 var(--spacing-xs) 0;
  color: var(--text-2);
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.pet-breed {
  margin: 0 0 var(--spacing-xs) 0;
  color: var(--text-1);
  font-size: 14px;
  font-weight: 500;
}

.pet-gender-age {
  margin: 0 0 var(--spacing-lg) 0;
  color: var(--text-2);
  font-size: 14px;
}

.pet-price {
  margin: 0 0 var(--spacing-lg) 0;
  color: var(--primary);
  font-size: var(--fs-xl);
  font-weight: 600;
}

.buy-btn {
  width: 100%;
  padding: 12px;
  font-size: 14px;
  font-weight: 600;
}

/* 无数据样式 */
.no-data {
  text-align: center;
  color: var(--text-3);
  padding: 80px 0;
  font-size: 16px;
  background-color: white;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
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

@media (max-width: 768px) {
  .pets-content {
    padding: var(--spacing-xl) 0;
  }
  
  .filter-section {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-base);
  }
  
  .filter-left {
    flex-wrap: wrap;
    gap: var(--spacing-base);
  }
  
  .filter-right {
    width: 100%;
    justify-content: flex-start;
  }
  
  .pets-grid {
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 16px;
  }
  
  .pet-image {
    height: 180px;
  }
  
  .pet-info {
    padding: 16px;
  }
}
</style>