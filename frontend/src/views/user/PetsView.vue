<template>
  <div class="pets-page">
    <Header />
    <main class="pets-content">
      <div class="container">
        <div class="pets-header">
          <h1>宠物列表</h1>
          <p>选择您心仪的宠物</p>
        </div>
        
        <!-- 分类筛选 -->
        <div class="category-filter">
          <h3>宠物分类</h3>
          <div class="category-buttons">
            <button 
              v-for="category in categories" 
              :key="category.id"
              :class="['category-btn', { active: selectedCategory === category.id }]"
              @click="selectCategory(category.id)"
            >
              {{ category.name }}
            </button>
          </div>
        </div>
        
        <!-- 搜索表单 -->
        <div class="search-section">
          <form @submit.prevent="handleSearch">
            <div class="form-row">
              <div class="form-group">
                <input 
                  type="text" 
                  v-model="searchParams.name"
                  placeholder="请输入宠物名称进行搜索"
                  class="search-input"
                >
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
              <p class="pet-breed">{{ pet.breed }}</p>
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
const selectedCategory = ref('')
const loading = ref(false)

// 搜索参数
const searchParams = ref({
  name: ''
})

// 获取宠物分类
const fetchCategories = async () => {
  try {
    const response = await petCategoryApi.list()
    if (response.code === 0) {
      categories.value = response.data
    } else {
      message.error('获取宠物分类失败')
    }
  } catch (error) {
    console.error('获取宠物分类失败:', error)
    message.error('获取宠物分类失败，请稍后重试')
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
      ...searchParams.value
    }
    
    if (selectedCategory.value) {
      params.categoryId = selectedCategory.value
    }
    
    const response = await petApi.page(params)
    
    if (response.code === 0) {
      pets.value = response.data.list
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

// 选择分类
const selectCategory = (categoryId) => {
  selectedCategory.value = selectedCategory.value === categoryId ? '' : categoryId
  currentPage.value = 1
  fetchPets()
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

// 查看宠物详情
const viewPetDetail = (petId) => {
  router.push(`/user/pet/${petId}`)
}

// 立即购买
const buyPet = (petId) => {
  router.push(`/user/order-confirm?type=pet&id=${petId}`)
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

/* 分类筛选 */
.category-filter {
  margin-bottom: var(--spacing-xl);
  background-color: white;
  padding: 20px;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
}

.category-filter h3 {
  margin: 0 0 var(--spacing-lg) 0;
  color: var(--text-1);
  font-size: var(--fs-lg);
  font-weight: 600;
}

.category-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.category-btn {
  padding: 8px 16px;
  border: 1px solid var(--border);
  border-radius: var(--radius-full);
  background-color: white;
  color: var(--text-1);
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.category-btn:hover {
  border-color: var(--primary);
  color: var(--primary);
}

.category-btn.active {
  background-color: var(--primary);
  color: white;
  border-color: var(--primary);
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
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.form-group {
  flex: 1;
  min-width: 200px;
}

.search-input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  transition: all 0.3s ease;
  font-family: inherit;
  background-color: white;
}

.search-input:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.form-actions {
  display: flex;
  gap: 12px;
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