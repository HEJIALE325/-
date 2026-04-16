<template>
  <div class="home">
    <Header />
    <main class="main-content">
      <!-- Banner轮播图 -->
      <section class="banner-slider">
        <div class="slider-container">
          <div 
            v-for="(banner, index) in banners" 
            :key="banner.id"
            class="banner-slide"
            :class="{ active: index === currentBanner }"
          >
            <div class="banner-image">
              <img :src="banner.image" :alt="banner.title">
              <div class="banner-overlay"></div>
            </div>
            <div class="banner-content">
              <h2>{{ banner.title }}</h2>
              <p>{{ banner.subtitle }}</p>
              <div class="banner-actions">
                <router-link to="/products" class="btn btn-primary">
                  宠物用品 🛒
                </router-link>
                <router-link to="/pets" class="btn btn-secondary">
                  可爱宠物 🐶
                </router-link>
              </div>
            </div>
          </div>
          <!-- 轮播指示器 -->
          <div class="slider-indicators">
            <span 
              v-for="(banner, index) in banners" 
              :key="banner.id"
              class="indicator"
              :class="{ active: index === currentBanner }"
              @click="currentBanner = index"
            ></span>
          </div>
          <!-- 轮播控制按钮 -->
          <button class="slider-btn slider-btn-prev" @click="prevBanner">
            <span>&lt;</span>
          </button>
          <button class="slider-btn slider-btn-next" @click="nextBanner">
            <span>&gt;</span>
          </button>
        </div>
      </section>
      
      <!-- 分类导航 -->
      <section class="category-nav">
        <div class="container">
          <div class="section-header">
            <h2>分类导航</h2>
          </div>
          <div class="categories">
            <router-link 
              v-for="category in categories" 
              :key="category.codeIndex"
              :to="category.codeIndex === '3' ? '/chongwujiyang' : category.codeIndex === '2' ? '/pets' : `/products?category=${category.codeIndex}`" 
              class="category-item"
            >
              <div class="category-icon">{{ category.icon || '�' }}</div>
              <div class="category-text">{{ category.indexName }}</div>
            </router-link>
          </div>
        </div>
      </section>
      
      <!-- 推荐商品 -->
      <section class="featured-products">
        <div class="container">
          <div class="section-header">
            <h2>热门推荐</h2>
            <router-link to="/products" class="view-all">查看全部</router-link>
          </div>
          
          <!-- 加载状态 -->
          <div v-if="loading" class="loading-container">
            <div class="loading-spinner"></div>
            <p>加载中...</p>
          </div>
          
          <!-- 错误提示 -->
          <div v-else-if="error" class="error-container">
            <p>{{ error }}</p>
            <button class="btn btn-primary" @click="fetchProducts">重新加载</button>
          </div>
          
          <!-- 商品列表 -->
          <div v-else class="products-grid">
            <div v-for="product in products" :key="product.id">
              <router-link :to="`/product/${product.id}`" class="product-card-link">
                <div class="product-card">
                  <!-- 商品标签 -->
                  <div class="product-tag" :class="product.chongwuyongpinTypes === '1' ? 'hot' : product.chongwuyongpinTypes === '2' ? 'new' : 'sale'">
                    {{ product.chongwuyongpinTypes === '1' ? '热销' : product.chongwuyongpinTypes === '2' ? '新品' : '促销' }}
                  </div>
                  
                  <!-- 商品图片 -->
                  <div class="product-image">
                    <img :src="getImageUrl(product.chongwuyongpinPhoto)" :alt="product.chongwuyongpinName">
                    <div class="product-image-overlay">
                      <button class="btn btn-secondary btn-sm">查看详情</button>
                    </div>
                  </div>
                  
                  <!-- 商品信息 -->
                  <div class="product-info">
                    <h3 class="product-title">{{ product.chongwuyongpinName }}</h3>
                    <p class="product-desc">{{ product.chongwuyongpinContent || '专为宠物设计，健康安全，品质保证' }}</p>
                    <div class="product-price-row">
                      <span class="product-price">¥{{ product.chongwuyongpinNewMoney || product.chongwuyongpinOldMoney }}</span>
                      <span v-if="product.chongwuyongpinOldMoney" class="product-original-price">¥{{ product.chongwuyongpinOldMoney }}</span>
                    </div>
                  </div>
                </div>
              </router-link>
            </div>
            
            <!-- 无商品时的提示 -->
            <div v-if="products.length === 0" class="empty-container">
              <p>暂无商品</p>
            </div>
          </div>
        </div>
      </section>
      
      <!-- 可爱宠物 -->
      <section class="featured-pets">
        <div class="container">
          <div class="section-header">
            <h2>可爱宠物</h2>
            <router-link to="/pets" class="view-all">查看全部</router-link>
          </div>
          
          <!-- 加载状态 -->
          <div v-if="loading" class="loading-container">
            <div class="loading-spinner"></div>
            <p>加载中...</p>
          </div>
          
          <!-- 错误提示 -->
          <div v-else-if="error" class="error-container">
            <p>{{ error }}</p>
            <button class="btn btn-primary" @click="fetchPets">重新加载</button>
          </div>
          
          <!-- 宠物列表 -->
          <div v-else class="pets-grid">
            <div v-for="pet in pets" :key="pet.id">
              <router-link :to="`/pet/${pet.id}`" class="pet-card-link">
                <div class="pet-card">
                  <!-- 宠物图片 -->
                  <div class="pet-image">
                    <img :src="getImageUrl(pet.imageUrl)" :alt="pet.name">
                    <div class="pet-image-overlay">
                      <button class="btn btn-secondary btn-sm">查看详情</button>
                    </div>
                  </div>
                  
                  <!-- 宠物信息 -->
                  <div class="pet-info">
                    <div class="pet-header">
                      <h3 class="pet-title">{{ pet.name }}</h3>
                      <span class="pet-breed">{{ pet.breed }}</span>
                    </div>
                    <p class="pet-desc">{{ pet.description || '可爱的宠物，期待您的领养' }}</p>
                    <div class="pet-price-row">
                      <span class="pet-price">¥{{ pet.price }}</span>
                      <button class="btn btn-primary btn-sm">立即领养</button>
                    </div>
                  </div>
                </div>
              </router-link>
            </div>
            
            <!-- 无宠物时的提示 -->
            <div v-if="pets.length === 0" class="empty-container">
              <p>暂无宠物</p>
            </div>
          </div>
        </div>
      </section>
      
      <!-- 最新资讯 -->
      <section class="latest-news">
        <div class="container">
          <div class="section-header">
            <h2>宠物资讯</h2>
            <router-link to="/news" class="view-all">查看全部</router-link>
          </div>
          
          <div class="news-grid">
            <div v-for="item in news" :key="item.id">
              <router-link :to="`/news/detail/${item.id}`" class="news-card-link">
                <div class="news-card">
                  <div class="news-image">
                    <img :src="getImageUrl(item.newsPhoto)" :alt="item.newsTitle">
                    <div class="news-image-overlay"></div>
                  </div>
                  <div class="news-content">
                    <h3 class="news-title">{{ item.newsTitle }}</h3>
                    <p class="news-desc">{{ item.newsContent || '这里是资讯内容摘要，为您提供专业的宠物护理建议和知识...' }}</p>
                    <div class="news-meta">
                      <span class="news-time">{{ item.insertTime || '2026-03-27' }}</span>
                      <span class="news-read-more">阅读更多 →</span>
                    </div>
                  </div>
                </div>
              </router-link>
            </div>
            
            <!-- 无新闻时的提示 -->
            <div v-if="news.length === 0" class="empty-container">
              <p>暂无资讯</p>
            </div>
          </div>
        </div>
      </section>
      
      <!-- 服务保障 -->
      <section class="service-guarantee">
        <div class="container">
          <div class="service-items">
            <div class="service-item">
              <div class="service-icon">🛒</div>
              <div class="service-content">
                <h4>正品保障</h4>
                <p>所有商品均为正品，品质保证</p>
              </div>
            </div>
            <div class="service-item">
              <div class="service-icon">🚚</div>
              <div class="service-content">
                <h4>闪电发货</h4>
                <p>24小时内发货，快速送达</p>
              </div>
            </div>
            <div class="service-item">
              <div class="service-icon">💝</div>
              <div class="service-content">
                <h4>7天无理由</h4>
                <p>不满意可退货，购物无忧</p>
              </div>
            </div>
            <div class="service-item">
              <div class="service-icon">📞</div>
              <div class="service-content">
                <h4>专业客服</h4>
                <p>24小时在线，随时解答</p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'
import { chongwuyongpinApi, newsApi, configApi, dictionaryApi, petApi } from '../../utils/api'

// 状态管理
const products = ref([])
const news = ref([])
const loading = ref(false)
const error = ref(null)
const banners = ref([])
const categories = ref([])
const pets = ref([])

// 当前轮播图索引
const currentBanner = ref(0)

// 获取图片URL
const getImageUrl = (imagePath) => {
  if (!imagePath) {
    return 'https://via.placeholder.com/300x300'
  }
  // 检查是否已经是完整的URL
  if (imagePath.startsWith('http://') || imagePath.startsWith('https://')) {
    return imagePath
  }
  return `http://localhost:8080/wangshangchongwudian/${imagePath}`
}

// 轮播图控制函数
const prevBanner = () => {
  currentBanner.value = (currentBanner.value - 1 + banners.value.length) % banners.value.length
}

const nextBanner = () => {
  currentBanner.value = (currentBanner.value + 1) % banners.value.length
}

// 获取商品列表
const fetchProducts = async () => {
  try {
    loading.value = true
    error.value = null
    const response = await chongwuyongpinApi.getList({ page: 1, limit: 6 })
    if (response.code === 0) {
      products.value = response.data.list || []
    } else {
      error.value = '获取商品列表失败'
    }
  } catch (err) {
    error.value = '获取商品列表失败'
    console.error('获取商品列表失败:', err)
  } finally {
    loading.value = false
  }
}

// 获取新闻列表
const fetchNews = async () => {
  try {
    loading.value = true
    error.value = null
    const response = await newsApi.getList({ page: 1, limit: 3 })
    if (response.code === 0) {
      news.value = response.data.list || []
    } else {
      error.value = '获取新闻列表失败'
    }
  } catch (err) {
    error.value = '获取新闻列表失败'
    console.error('获取新闻列表失败:', err)
  } finally {
    loading.value = false
  }
}

// 获取轮播图数据
const fetchBanners = async () => {
  try {
    const response = await configApi.getList({ page: 1, limit: 5 })
    if (response.code === 0) {
      // 处理轮播图数据，添加完整的图片路径
      banners.value = response.data.list.map(item => ({
        id: item.id,
        title: item.name,
        subtitle: '',
        image: getImageUrl(item.value)
      }))
    }
  } catch (err) {
    console.error('获取轮播图失败:', err)
    // 如果获取失败，使用默认轮播图
    banners.value = [
      {
        id: 1,
        title: '欢迎来到宠物电商平台',
        subtitle: '用心呵护每一位毛孩子，为您提供优质的宠物用品和可爱宠物',
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cute%20pets%20in%20pet%20shop%20banner%20with%20colorful%20background&image_size=landscape_16_9'
      },
      {
        id: 2,
        title: '宠物用品专区',
        subtitle: '优质宠物用品，为您的爱宠提供全方位呵护',
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=new%20pet%20products%20display%20in%20modern%20store&image_size=landscape_16_9'
      },
      {
        id: 3,
        title: '可爱宠物等你来',
        subtitle: '精选健康可爱的宠物，为您的家庭增添新成员',
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cute%20puppies%20and%20kittens%20for%20adoption%20banner&image_size=landscape_16_9'
      }
    ]
  }
}

// 获取商品分类数据
const fetchCategories = async () => {
  // 直接返回固定的三个分类
  categories.value = [
    { codeIndex: '1', indexName: '宠物用品', icon: '🛒' },
    { codeIndex: '2', indexName: '宠物', icon: '🐶' },
    { codeIndex: '3', indexName: '宠物寄养', icon: '🏠' }
  ]
}

// 获取宠物列表
const fetchPets = async () => {
  try {
    loading.value = true
    error.value = null
    const response = await petApi.getPage({ page: 1, limit: 6, status: 1 }) // 只显示上架的宠物
    if (response.code === 0) {
      pets.value = response.data.list || []
    } else {
      error.value = '获取宠物列表失败'
    }
  } catch (err) {
    error.value = '获取宠物列表失败'
    console.error('获取宠物列表失败:', err)
  } finally {
    loading.value = false
  }
}

// 轮播图自动切换
const startBannerSlider = () => {
  setInterval(() => {
    currentBanner.value = (currentBanner.value + 1) % banners.value.length
  }, 5000)
}

// 组件挂载时获取数据
onMounted(() => {
  fetchBanners()
  fetchCategories()
  fetchProducts()
  fetchNews()
  fetchPets()
  startBannerSlider()
})
</script>

<style scoped>
.main-content {
  padding: 0;
}

/* 轮播图样式 */
.banner-slider {
  position: relative;
  height: 500px;
  overflow: hidden;
  border-radius: 0 0 var(--radius-lg) var(--radius-lg);
  margin-bottom: var(--spacing-2xl);
}

.slider-container {
  position: relative;
  width: 100%;
  height: 100%;
}

.banner-slide {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  transition: opacity 0.5s ease-in-out;
}

.banner-slide.active {
  opacity: 1;
}

.banner-image {
  width: 100%;
  height: 100%;
  overflow: hidden;
  position: relative;
}

.banner-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.banner-slide:hover .banner-image img {
  transform: scale(1.05);
}

.banner-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(rgba(0, 0, 0, 0.2), rgba(0, 0, 0, 0.4));
  z-index: 1;
}

.banner-content {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  color: white;
  z-index: 10;
  max-width: 800px;
  padding: 0 var(--spacing-xl);
}

.banner-content h2 {
  color: white;
  font-size: var(--fs-3xl);
  margin-bottom: var(--spacing-base);
  font-weight: 600;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  animation: fadeInUp 0.8s ease-out;
}

.banner-content p {
  font-size: var(--fs-base);
  margin-bottom: var(--spacing-xl);
  opacity: 0.9;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
  animation: fadeInUp 0.8s ease-out 0.2s both;
}

.banner-actions {
  display: flex;
  gap: var(--spacing-lg);
  justify-content: center;
  animation: fadeInUp 0.8s ease-out 0.4s both;
}

.banner-actions .btn {
  font-size: var(--fs-lg);
  padding: var(--spacing-base) var(--spacing-2xl);
  border-radius: var(--radius-full);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.banner-actions .btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

/* 轮播控制按钮 */
.slider-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.8);
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 20;
  transition: all 0.3s ease;
  font-size: 24px;
  color: var(--text-1);
}

.slider-btn:hover {
  background: white;
  transform: translateY(-50%) scale(1.1);
}

.slider-btn-prev {
  left: var(--spacing-xl);
}

.slider-btn-next {
  right: var(--spacing-xl);
}

/* 轮播指示器 */
.slider-indicators {
  position: absolute;
  bottom: var(--spacing-xl);
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: var(--spacing-sm);
  z-index: 20;
}

.indicator {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.5);
  cursor: pointer;
  transition: all 0.3s ease;
}

.indicator.active {
  background: white;
  width: 24px;
  border-radius: 6px;
}

/* 加载状态 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-2xl);
  text-align: center;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid rgba(66, 184, 131, 0.3);
  border-radius: 50%;
  border-top-color: var(--primary);
  animation: spin 1s ease-in-out infinite;
  margin-bottom: var(--spacing-base);
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 错误提示 */
.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-2xl);
  text-align: center;
  background: #fff2f0;
  border-radius: var(--radius-base);
  margin-bottom: var(--spacing-2xl);
}

.error-container p {
  margin-bottom: var(--spacing-base);
  color: var(--danger);
}

/* 空状态 */
.empty-container {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-2xl);
  text-align: center;
  background: #f7f9fc;
  border-radius: var(--radius-base);
  grid-column: 1 / -1;
}

.empty-container p {
  color: var(--text-3);
  font-size: var(--fs-base);
}

/* 分类导航 */
.category-nav {
  margin-bottom: var(--spacing-2xl);
  padding: var(--spacing-2xl) 0;
  background: var(--card);
  border-radius: var(--radius-lg);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.categories {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
  gap: var(--spacing-lg);
  margin-top: var(--spacing-xl);
}

.category-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-xl);
  background: var(--bg);
  border-radius: var(--radius-base);
  text-decoration: none;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  position: relative;
  overflow: hidden;
}

.category-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, var(--primary), var(--secondary));
  transform: scaleX(0);
  transition: transform 0.3s ease;
}

.category-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.category-item:hover::before {
  transform: scaleX(1);
}

.category-icon {
  font-size: 40px;
  margin-bottom: var(--spacing-sm);
  transition: transform 0.3s ease;
}

.category-item:hover .category-icon {
  transform: scale(1.1);
}

.category-text {
  color: var(--text-1);
  font-weight: 500;
  font-size: var(--fs-sm);
  text-align: center;
}

/* 模块标题 */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-xl);
  padding-bottom: var(--spacing-base);
  border-bottom: 2px solid var(--border);
}

.section-header h2 {
  font-size: var(--fs-xl);
  color: var(--text-1);
  font-weight: 600;
  margin: 0;
  position: relative;
}

.section-header h2::after {
  content: '';
  position: absolute;
  bottom: -10px;
  left: 0;
  width: 60px;
  height: 3px;
  background: var(--primary);
  border-radius: 2px;
}

.view-all {
  color: var(--text-3);
  text-decoration: none;
  font-size: var(--fs-sm);
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
}

.view-all:hover {
  color: var(--primary);
  transform: translateX(4px);
}

.view-all::after {
  content: '→';
  font-size: 12px;
  transition: transform 0.3s ease;
}

.view-all:hover::after {
  transform: translateX(2px);
}

/* 商品卡片 */
.featured-products {
  margin-bottom: var(--spacing-2xl);
  padding: var(--spacing-2xl) 0;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: var(--spacing-xl);
}

.product-card-link {
  display: block;
  text-decoration: none;
  color: inherit;
}

.product-card {
  background: var(--card);
  border-radius: var(--radius-base);
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
  position: relative;
}

.product-card-link:hover .product-card {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.product-tag {
  position: absolute;
  top: var(--spacing-base);
  right: var(--spacing-base);
  background: rgba(255, 255, 255, 0.9);
  border-radius: var(--radius-sm);
  padding: var(--spacing-xs) var(--spacing-sm);
  font-size: var(--fs-xs);
  font-weight: 600;
  z-index: 10;
  transition: all 0.3s ease;
}

.product-tag.hot {
  color: var(--danger);
  background: rgba(255, 77, 79, 0.1);
  border: 1px solid var(--danger);
}

.product-tag.new {
  color: var(--primary);
  background: rgba(66, 184, 131, 0.1);
  border: 1px solid var(--primary);
}

.product-tag.sale {
  color: var(--warning);
  background: rgba(250, 173, 20, 0.1);
  border: 1px solid var(--warning);
}

.product-image {
  position: relative;
  padding-top: 100%; /* 1:1 比例 */
  overflow: hidden;
}

.product-image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.product-card:hover .product-image img {
  transform: scale(1.05);
}

.product-image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
  z-index: 5;
}

.product-card:hover .product-image-overlay {
  opacity: 1;
}

.btn-sm {
  padding: var(--spacing-sm) var(--spacing-base);
  font-size: var(--fs-sm);
}

.product-info {
  padding: var(--spacing-base);
}

.product-title {
  font-size: var(--fs-base);
  font-weight: 500;
  color: var(--text-1);
  margin-bottom: var(--spacing-xs);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-desc {
  font-size: var(--fs-xs);
  color: var(--text-3);
  margin-bottom: var(--spacing-base);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-price-row {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  margin-bottom: var(--spacing-base);
}

.product-price {
  color: var(--danger);
  font-size: var(--fs-lg);
  font-weight: 600;
}

.product-original-price {
  color: var(--text-3);
  font-size: var(--fs-xs);
  text-decoration: line-through;
}

/* 宠物卡片 */
.featured-pets {
  margin-bottom: var(--spacing-2xl);
  padding: var(--spacing-2xl) 0;
  background: var(--bg);
  border-radius: var(--radius-lg);
}

.pets-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: var(--spacing-xl);
}

.pet-card-link {
  display: block;
  text-decoration: none;
  color: inherit;
}

.pet-card {
  background: var(--card);
  border-radius: var(--radius-base);
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
  position: relative;
}

.pet-card-link:hover .pet-card {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.pet-image {
  position: relative;
  padding-top: 100%; /* 1:1 比例 */
  overflow: hidden;
}

.pet-image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.pet-card:hover .pet-image img {
  transform: scale(1.05);
}

.pet-image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
  z-index: 5;
}

.pet-card:hover .pet-image-overlay {
  opacity: 1;
}

.pet-info {
  padding: var(--spacing-base);
}

.pet-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-xs);
}

.pet-title {
  font-size: var(--fs-base);
  font-weight: 500;
  color: var(--text-1);
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
}

.pet-breed {
  font-size: var(--fs-xs);
  color: var(--primary);
  font-weight: 500;
  background: rgba(66, 184, 131, 0.1);
  padding: 2px 8px;
  border-radius: var(--radius-sm);
  margin-left: var(--spacing-sm);
}

.pet-desc {
  font-size: var(--fs-xs);
  color: var(--text-3);
  margin-bottom: var(--spacing-base);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.pet-price-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-base);
}

.pet-price {
  color: var(--danger);
  font-size: var(--fs-lg);
  font-weight: 600;
}

/* 资讯卡片 */
.latest-news {
  margin-bottom: var(--spacing-2xl);
  padding: var(--spacing-2xl) 0;
}

.news-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: var(--spacing-xl);
}

.news-card-link {
  display: block;
  text-decoration: none;
  color: inherit;
}

.news-card {
  background: var(--card);
  border-radius: var(--radius-base);
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
  position: relative;
}

.news-card-link:hover .news-card {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.news-image {
  height: 180px;
  overflow: hidden;
  position: relative;
}

.news-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.news-card:hover .news-image img {
  transform: scale(1.05);
}

.news-image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(to bottom, transparent, rgba(0, 0, 0, 0.3));
  z-index: 1;
}

.news-content {
  padding: var(--spacing-base);
}

.news-title {
  font-size: var(--fs-base);
  font-weight: 500;
  color: var(--text-1);
  margin-bottom: var(--spacing-xs);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: color 0.3s ease;
}

.news-card-link:hover .news-title {
  color: var(--primary);
}

.news-desc {
  font-size: var(--fs-xs);
  color: var(--text-2);
  margin-bottom: var(--spacing-base);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.news-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: var(--spacing-sm);
  border-top: 1px solid var(--border);
}

.news-time {
  font-size: var(--fs-xs);
  color: var(--text-3);
}

.news-read-more {
  font-size: var(--fs-xs);
  color: var(--primary);
  font-weight: 500;
  transition: all 0.3s ease;
}

.news-card-link:hover .news-read-more {
  transform: translateX(4px);
}

/* 服务保障 */
.service-guarantee {
  margin-bottom: var(--spacing-2xl);
  padding: var(--spacing-2xl);
  background: var(--card);
  border-radius: var(--radius-lg);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.service-items {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--spacing-xl);
  text-align: center;
}

.service-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: var(--spacing-xl);
  background: var(--bg);
  border-radius: var(--radius-base);
  transition: all 0.3s ease;
}

.service-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.service-icon {
  font-size: 48px;
  margin-bottom: var(--spacing-base);
  transition: transform 0.3s ease;
}

.service-item:hover .service-icon {
  transform: scale(1.1);
}

.service-content h4 {
  font-size: var(--fs-base);
  font-weight: 600;
  color: var(--text-1);
  margin-bottom: var(--spacing-xs);
}

.service-content p {
  font-size: var(--fs-sm);
  color: var(--text-2);
  margin: 0;
}

/* 移动端适配 */
@media (max-width: 768px) {
  /* 轮播图适配 */
  .banner-slider {
    height: 300px;
  }
  
  .banner-content h2 {
    font-size: var(--fs-2xl);
  }
  
  .banner-content p {
    font-size: var(--fs-sm);
  }
  
  .banner-actions {
    flex-direction: column;
    align-items: center;
    gap: var(--spacing-base);
  }
  
  .banner-actions .btn {
    font-size: var(--fs-base);
    padding: var(--spacing-sm) var(--spacing-xl);
  }
  
  .slider-btn {
    width: 36px;
    height: 36px;
    font-size: 18px;
  }
  
  .slider-btn-prev {
    left: var(--spacing-base);
  }
  
  .slider-btn-next {
    right: var(--spacing-base);
  }
  
  /* 分类导航适配 */
  .categories {
    grid-template-columns: repeat(2, 1fr);
    gap: var(--spacing-base);
  }
  
  .category-item {
    padding: var(--spacing-lg);
  }
  
  .category-icon {
    font-size: 32px;
  }
  
  /* 商品网格适配 */
  .products-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: var(--spacing-base);
  }
  
  /* 宠物网格适配 */
  .pets-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: var(--spacing-base);
  }
  
  .product-card {
    font-size: var(--fs-sm);
  }
  
  .pet-card {
    font-size: var(--fs-sm);
  }
  
  .product-info {
    padding: var(--spacing-sm);
  }
  
  .pet-info {
    padding: var(--spacing-sm);
  }
  
  /* 新闻网格适配 */
  .news-grid {
    grid-template-columns: 1fr;
    gap: var(--spacing-xl);
  }
  
  .news-image {
    height: 140px;
  }
  
  /* 服务保障适配 */
  .service-items {
    grid-template-columns: repeat(2, 1fr);
    gap: var(--spacing-base);
  }
  
  .service-item {
    padding: var(--spacing-lg);
  }
  
  .service-icon {
    font-size: 32px;
  }
  
  /* 标题适配 */
  .section-header h2 {
    font-size: var(--fs-lg);
  }
  
  /* 容器适配 */
  .container {
    padding: 0 var(--spacing-base);
  }
}

/* 平板适配 */
@media (min-width: 769px) and (max-width: 1024px) {
  .banner-slider {
    height: 400px;
  }
  
  .products-grid {
    grid-template-columns: repeat(3, 1fr);
  }
  
  .pets-grid {
    grid-template-columns: repeat(3, 1fr);
  }
  
  .news-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .service-items {
    grid-template-columns: repeat(2, 1fr);
  }
}

/* 大屏幕适配 */
@media (min-width: 1025px) {
  .banner-slider {
    height: 500px;
  }
  
  .products-grid {
    grid-template-columns: repeat(4, 1fr);
  }
  
  .pets-grid {
    grid-template-columns: repeat(4, 1fr);
  }
  
  .news-grid {
    grid-template-columns: repeat(3, 1fr);
  }
  
  .service-items {
    grid-template-columns: repeat(4, 1fr);
  }
}
</style>