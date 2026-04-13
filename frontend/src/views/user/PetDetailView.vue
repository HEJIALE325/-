<template>
  <div class="pet-detail-page">
    <Header />
    <main class="pet-detail-content">
      <div class="container" v-if="pet">
        <div class="pet-detail-header">
          <h1>{{ pet.name }}</h1>
          <p class="pet-category">{{ pet.categoryName }}</p>
        </div>
        
        <!-- 宠物详情 -->
        <div class="pet-detail-body">
          <div class="pet-image-section">
            <div class="pet-image">
              <img :src="pet.imageUrl" :alt="pet.name">
            </div>
          </div>
          
          <div class="pet-info-section">
            <div class="pet-basic-info">
              <div class="info-item">
                <span class="info-label">品种：</span>
                <span class="info-value">{{ pet.breed }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">性别：</span>
                <span class="info-value">{{ pet.gender }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">年龄：</span>
                <span class="info-value">{{ pet.age }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">价格：</span>
                <span class="info-value price">{{ pet.price }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">状态：</span>
                <span :class="['info-value status-badge', pet.status === 1 ? 'status-active' : 'status-inactive']">
                  {{ pet.status === 1 ? '上架' : '下架' }}
                </span>
              </div>
            </div>
            
            <div class="pet-actions">
              <button class="btn btn-primary buy-btn" @click="buyPet" :disabled="pet.status !== 1">
                立即购买
              </button>
              <button class="btn btn-secondary favorite-btn" @click="addToFavorite">
                <span v-if="isFavorite">已收藏</span>
                <span v-else>收藏</span>
              </button>
            </div>
          </div>
        </div>
        
        <!-- 宠物描述 -->
        <div class="pet-description-section">
          <h2>宠物描述</h2>
          <div class="description-content">
            {{ pet.description || '暂无描述' }}
          </div>
        </div>
      </div>
      
      <!-- 加载中 -->
      <div class="loading" v-else>
        <p>加载中...</p>
      </div>
    </main>
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { petApi } from '../../utils/api'
import message from '../../utils/message'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'

const route = useRoute()
const router = useRouter()
const pet = ref(null)
const isFavorite = ref(false)
const loading = ref(true)

// 获取宠物详情
const fetchPetDetail = async () => {
  try {
    loading.value = true
    const petId = route.params.id
    
    if (!petId) {
      message.error('宠物ID不存在')
      router.push('/user/pets')
      return
    }
    
    const response = await petApi.info(petId)
    
    if (response.code === 0) {
      pet.value = response.data
    } else {
      message.error('获取宠物详情失败')
      router.push('/user/pets')
    }
  } catch (error) {
    console.error('获取宠物详情失败:', error)
    message.error('获取宠物详情失败，请稍后重试')
    router.push('/user/pets')
  } finally {
    loading.value = false
  }
}

// 立即购买
const buyPet = () => {
  if (pet.value && pet.value.status === 1) {
    router.push(`/user/order-confirm?type=pet&id=${pet.value.id}`)
  }
}

// 收藏宠物
const addToFavorite = () => {
  // 这里可以实现收藏功能
  isFavorite.value = !isFavorite.value
  message.success(isFavorite.value ? '收藏成功' : '取消收藏')
}

// 页面加载时的初始化
onMounted(() => {
  fetchPetDetail()
})

// 监听路由参数变化
watch(() => route.params.id, () => {
  fetchPetDetail()
})
</script>

<style scoped>
.pet-detail-page {
  min-height: 100vh;
  background-color: var(--bg);
}

.pet-detail-content {
  padding: var(--spacing-2xl) 0;
}

.pet-detail-header {
  text-align: center;
  margin-bottom: var(--spacing-2xl);
}

.pet-detail-header h1 {
  color: var(--text-1);
  font-size: var(--fs-3xl);
  font-weight: 600;
  margin-bottom: var(--spacing-sm);
}

.pet-category {
  color: var(--text-2);
  font-size: var(--fs-base);
  margin: 0;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* 宠物详情主体 */
.pet-detail-body {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
  margin-bottom: var(--spacing-3xl);
  background-color: white;
  padding: 30px;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
}

.pet-image-section {
  display: flex;
  justify-content: center;
  align-items: center;
}

.pet-image {
  width: 100%;
  max-width: 400px;
  height: 400px;
  overflow: hidden;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-md);
}

.pet-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.pet-image:hover img {
  transform: scale(1.05);
}

.pet-info-section {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.pet-basic-info {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 30px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 16px;
}

.info-label {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-1);
  min-width: 80px;
}

.info-value {
  font-size: 16px;
  color: var(--text-2);
  flex: 1;
}

.info-value.price {
  color: var(--primary);
  font-size: var(--fs-2xl);
  font-weight: 600;
}

/* 状态标签 */
.status-badge {
  display: inline-block;
  padding: 6px 16px;
  border-radius: var(--radius-full);
  font-size: 14px;
  font-weight: 500;
}

.status-active {
  background-color: rgba(25, 135, 84, 0.1);
  color: #198754;
}

.status-inactive {
  background-color: rgba(220, 53, 69, 0.1);
  color: #dc3545;
}

.pet-actions {
  display: flex;
  gap: 16px;
}

/* 按钮样式 */
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 12px 24px;
  font-size: 16px;
  font-weight: 600;
  border-radius: var(--radius-base);
  transition: all 0.3s ease;
  cursor: pointer;
  border: none;
  text-decoration: none;
  flex: 1;
}

.btn-primary {
  background-color: var(--primary);
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background-color: var(--primary-dark);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(66, 184, 131, 0.3);
}

.btn-primary:disabled {
  background-color: var(--text-3);
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
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

/* 宠物描述 */
.pet-description-section {
  background-color: white;
  padding: 30px;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
}

.pet-description-section h2 {
  color: var(--text-1);
  font-size: var(--fs-xl);
  font-weight: 600;
  margin: 0 0 var(--spacing-lg) 0;
  padding-bottom: var(--spacing-sm);
  border-bottom: 1px solid var(--border);
}

.description-content {
  color: var(--text-2);
  font-size: 16px;
  line-height: 1.6;
  white-space: pre-line;
}

/* 加载中 */
.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px;
  background-color: white;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
}

.loading p {
  color: var(--text-2);
  font-size: 16px;
}

@media (max-width: 768px) {
  .pet-detail-content {
    padding: var(--spacing-xl) 0;
  }
  
  .pet-detail-header h1 {
    font-size: var(--fs-2xl);
  }
  
  .pet-detail-body {
    grid-template-columns: 1fr;
    gap: 24px;
    padding: 20px;
  }
  
  .pet-image {
    height: 300px;
  }
  
  .pet-actions {
    flex-direction: column;
  }
  
  .pet-description-section {
    padding: 20px;
  }
}
</style>