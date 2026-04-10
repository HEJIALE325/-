<template>
  <div class="products-page">
    <Header />
    <main class="products-content">
      <div class="container">
        <!-- 页面标题 -->
        <div class="page-header">
          <h1>宠物商品</h1>
          <p>为您的毛孩子挑选优质商品</p>
        </div>

        <!-- 筛选 -->
        <div class="filter-section">
          <div class="filter-left">
            <div class="category-filter">
              <label>分类：</label>
              <select v-model="filter.category">
                <option value="">全部分类</option>
                <option v-for="type in productTypes" :key="type.codeIndex" :value="type.codeIndex">
                  {{ type.indexName }}
                </option>
              </select>
            </div>
            <div class="name-filter">
              <label>名称：</label>
              <input type="text" v-model="filter.name" placeholder="商品名称">
            </div>
          </div>
          <div class="filter-right">
            <button class="btn btn-primary" @click="loadProducts">查询</button>
          </div>
        </div>

        <!-- 商品列表 -->
        <div v-if="!loading" class="products-grid">
          <div v-for="product in products" :key="product.id">
            <router-link :to="`/product/${product.id}`" class="product-card-link">
              <div class="product-card">
                <!-- 商品标签 -->
                <div v-if="product.shangxiaValue === '上架'" class="product-tag new">{{ product.shangxiaValue }}</div>
                <div v-else class="product-tag sale">{{ product.shangxiaValue }}</div>
                
                <!-- 商品图片 -->
                <div class="product-image">
                  <img :src="'http://localhost:8080/wangshangchongwudian/' + product.chongwuyongpinPhoto" :alt="product.chongwuyongpinName">
                </div>
                
                <!-- 商品信息 -->
                <div class="product-info">
                  <h3 class="product-title">{{ product.chongwuyongpinName }}</h3>
                  <p class="product-type">{{ getProductTypeName(product.chongwuyongpinTypes) }}</p>
                  <p class="product-desc">{{ product.chongwuyongpinContent }}</p>
                  <div class="product-meta">
                    <span class="product-stock">库存：{{ product.chongwuyongpinKucunNumber }}</span>
                    <span class="product-click">浏览：{{ product.chongwuyongpinClicknum }}</span>
                  </div>
                  <div class="product-price-row">
                    <span class="product-price">¥{{ product.chongwuyongpinNewMoney }}</span>
                    <span v-if="product.chongwuyongpinOldMoney" class="product-original-price">¥{{ product.chongwuyongpinOldMoney }}</span>
                  </div>
                </div>
              </div>
            </router-link>
            <div class="product-actions">
              <button class="btn btn-secondary" @click="addToCart(product.id)">加入购物车</button>
            </div>
          </div>
        </div>

        <!-- 加载中 -->
        <div v-else class="loading">
          <div class="loading-spinner"></div>
          <p>加载中...</p>
        </div>

        <!-- 无数据 -->
        <div v-if="!loading && products.length === 0" class="no-data">
          <p>暂无商品</p>
        </div>

        <!-- 分页 -->
        <div v-if="!loading && products.length > 0" class="pagination">
          <button class="btn btn-secondary" @click="prevPage" :disabled="currentPage === 1">上一页</button>
          <span class="page-info">第 {{ currentPage }} 页，共 {{ totalPages }} 页</span>
          <button class="btn btn-secondary" @click="nextPage" :disabled="currentPage === totalPages">下一页</button>
        </div>
      </div>
    </main>
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { chongwuyongpinApi, cartApi, dictionaryApi, usersApi } from '../../utils/api'
import message from '../../utils/message'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'

const router = useRouter()
const products = ref([])
const loading = ref(false)
const currentPage = ref(1)
const totalPages = ref(1)
const productTypes = ref([])
const filter = ref({
  category: '',
  name: ''
})

// 加载商品类型字典
const loadProductTypes = async () => {
  try {
    const response = await dictionaryApi.getByDicCode('chongwuyongpin_types')
    if (response.code === 0) {
      productTypes.value = response.data.list || []
    }
  } catch (error) {
    console.error('获取商品类型失败:', error)
  }
}

// 获取商品类型名称
const getProductTypeName = (typeCode) => {
  const type = productTypes.value.find(t => t.codeIndex === typeCode)
  return type ? type.indexName : '未知类型'
}

// 加载商品列表
const loadProducts = async () => {
  try {
    loading.value = true
    // 构建与后端接口匹配的筛选参数
    const params = {
      page: currentPage.value,
      limit: 12,
      chongwuyongpinTypes: filter.value.category,
      chongwuyongpinName: filter.value.name,
      t: Date.now()
    }
    const response = await chongwuyongpinApi.getList(params)
    if (response.code === 0) {
      products.value = response.data.list || []
      totalPages.value = Math.ceil(response.data.total / 12)
    } else {
      message.error('获取商品列表失败')
    }
  } catch (error) {
    console.error('获取商品列表失败:', error)
    message.error('获取商品列表失败')
  } finally {
    loading.value = false
  }
}

// 查看商品详情
const viewProduct = (id) => {
  router.push(`/product/${id}`)
}

// 加入购物车
const addToCart = async (productId) => {
  try {
    // 获取用户会话信息
    const sessionResponse = await usersApi.session()
    if (sessionResponse.code === 0 && sessionResponse.data) {
      const yonghuId = sessionResponse.data.yonghuId || sessionResponse.data.id
      
      // 调用购物车API，传递必要字段
      const response = await cartApi.add({
        yonghuId,
        chongwuyongpinId: productId,
        buyNumber: 1,
        t: Date.now()
      })
      if (response.code === 0) {
        message.success('加入购物车成功')
      } else {
        message.error('加入购物车失败')
      }
    } else {
      message.error('获取用户信息失败')
    }
  } catch (error) {
    console.error('加入购物车失败:', error)
    message.error('加入购物车失败')
  }
}

// 上一页
const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
    loadProducts()
  }
}

// 下一页
const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
    loadProducts()
  }
}

// 页面加载时获取商品类型和商品列表
onMounted(() => {
  loadProductTypes()
  loadProducts()
})
</script>

<style scoped>
.products-page {
  min-height: 100vh;
  background-color: var(--bg);
}

.products-content {
  padding: var(--spacing-2xl) 0;
}

.page-header {
  text-align: center;
  margin-bottom: var(--spacing-2xl);
}

.page-header h1 {
  color: var(--text-1);
  font-size: var(--fs-2xl);
  font-weight: 600;
  margin-bottom: var(--spacing-sm);
}

.page-header p {
  color: var(--text-2);
  font-size: var(--fs-base);
  margin: 0;
}

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
}

.filter-right {
  display: flex;
  align-items: center;
}

.filter-right .btn {
  font-size: var(--fs-sm);
  padding: var(--spacing-xs) var(--spacing-base);
}

.category-filter, .name-filter {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.category-filter label, .name-filter label {
  font-weight: 500;
  color: var(--text-1);
  font-size: var(--fs-sm);
}

.category-filter select, .name-filter input {
  padding: var(--spacing-xs) var(--spacing-base);
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: var(--fs-sm);
  background-color: white;
  color: var(--text-1);
}

.name-filter input {
  width: 150px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, 280px);
  gap: var(--spacing-xl);
  margin-bottom: var(--spacing-2xl);
  justify-content: center;
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

.product-actions {
  margin-top: var(--spacing-sm);
  display: flex;
  justify-content: center;
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
}

.product-tag.hot {
  color: var(--danger);
}

.product-tag.new {
  color: var(--primary);
}

.product-tag.sale {
  color: var(--warning);
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

.product-type {
  font-size: var(--fs-xs);
  color: var(--primary);
  margin-bottom: var(--spacing-xs);
  font-weight: 500;
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

.product-meta {
  display: flex;
  justify-content: space-between;
  font-size: var(--fs-xs);
  color: var(--text-2);
  margin-bottom: var(--spacing-base);
}

.product-stock {
  color: var(--success);
}

.product-click {
  color: var(--text-3);
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

.product-actions {
  display: flex;
  gap: var(--spacing-sm);
}

.product-actions .btn {
  flex: 1;
  font-size: var(--fs-sm);
  padding: var(--spacing-xs) var(--spacing-base);
}

.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-3xl) 0;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid var(--primary);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: var(--spacing-lg);
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.no-data {
  text-align: center;
  padding: var(--spacing-3xl) 0;
  color: var(--text-3);
  font-size: var(--fs-base);
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: var(--spacing-lg);
  margin-top: var(--spacing-xl);
}

.page-info {
  color: var(--text-2);
  font-size: var(--fs-sm);
}

@media (max-width: 768px) {
  .filter-section {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-base);
  }
  
  .filter-left {
    flex-wrap: wrap;
    gap: var(--spacing-base);
  }
  
  .products-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: var(--spacing-base);
  }
  
  .product-actions {
    flex-direction: column;
  }
}
</style>