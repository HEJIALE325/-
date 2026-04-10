<template>
  <div class="favorites-page">
    <Header />
    <main class="favorites-content">
      <div class="container">
        <div class="favorites-header">
          <h1>我的收藏</h1>
          <p>查看和管理您收藏的商品</p>
        </div>
        
        <!-- 收藏商品列表 -->
        <div class="favorites-grid">
          <div class="favorite-card" v-for="item in favorites" :key="item.id">
            <div class="favorite-image">
              <img :src="'http://localhost:8080/wangshangchongwudian/' + item.chongwuyongpinPhoto" :alt="item.chongwuyongpinName">
              <button class="favorite-btn" @click="removeFavorite(item.id)">
                ❌
              </button>
            </div>
            <div class="favorite-info">
              <h3 class="favorite-name">{{ item.chongwuyongpinName }}</h3>
              <p class="favorite-desc">{{ item.chongwuyongpinContent }}</p>
              <div class="favorite-price">¥{{ item.chongwuyongpinNewMoney }}</div>
              <div class="favorite-actions">
                <button class="btn btn-sm btn-primary" @click="addToCart(item.chongwuyongpinId)">
                  加入购物车
                </button>
                <router-link :to="`/product/${item.chongwuyongpinId}`" class="btn btn-sm btn-secondary">
                  查看详情
                </router-link>
              </div>
            </div>
          </div>
          
          <!-- 空状态 -->
          <div class="empty-state" v-if="favorites.length === 0">
            <div class="empty-icon">❤️</div>
            <h3>暂无收藏</h3>
            <p>您还没有收藏任何商品，快去发现喜欢的商品吧！</p>
            <router-link to="/products" class="btn btn-primary">
              去购物
            </router-link>
          </div>
        </div>
        
        <!-- 分页 -->
        <div class="pagination" v-if="total > 0">
          <button class="page-btn" :disabled="currentPage === 1" @click="handlePageChange(currentPage - 1)">上一页</button>
          <span class="page-info">第 {{ currentPage }} 页，共 {{ totalPages }} 页</span>
          <button class="page-btn" :disabled="currentPage === totalPages" @click="handlePageChange(currentPage + 1)">下一页</button>
        </div>
      </div>
    </main>
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'
import message from '../../utils/message'
import { chongwuyongpinCollectionApi, yonghuApi } from '../../utils/api'

const router = useRouter()
const currentPage = ref(1)
const totalPages = ref(1)
const total = ref(0)
const loading = ref(false)
const userInfo = ref(null)
const favorites = ref([])

// 获取用户信息
const getUserInfo = async () => {
  try {
    const response = await yonghuApi.session()
    if (response.code === 0) {
      userInfo.value = response.data
      return response.data
    } else {
      message.error('获取用户信息失败')
      router.push('/user/login')
      return null
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    message.error('获取用户信息失败')
    router.push('/user/login')
    return null
  }
}

// 页面加载时的初始化
onMounted(() => {
  fetchFavorites()
})

// 获取收藏列表
const fetchFavorites = async () => {
  try {
    loading.value = true
    const user = await getUserInfo()
    if (!user) return
    
    const params = {
      page: currentPage.value,
      limit: 8,
      sort: 'id',
      order: 'desc'
    }
    
    const response = await chongwuyongpinCollectionApi.getUserCollections(user.id, params)
    
    if (response.code === 0) {
      favorites.value = response.data.list
      total.value = response.data.total
      totalPages.value = response.data.totalPage
    } else {
      message.error('获取收藏列表失败')
    }
  } catch (error) {
    console.error('获取收藏列表失败:', error)
    message.error('获取收藏列表失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 移除收藏
const removeFavorite = async (id) => {
  try {
    const response = await chongwuyongpinCollectionApi.delete([id])
    if (response.code === 0) {
      favorites.value = favorites.value.filter(item => item.id !== id)
      message.success('移除收藏成功')
    } else {
      message.error('移除收藏失败：' + response.msg)
    }
  } catch (error) {
    console.error('移除收藏失败:', error)
    message.error('移除收藏失败，请稍后重试')
  }
}

// 添加到购物车
const addToCart = async (id) => {
  try {
    // 这里可以添加实际的API调用
    // const response = await cartApi.add({ productId: id, quantity: 1 })
    // if (response.code === 0) {
    //   message.success('添加到购物车成功')
    // }
    
    // 模拟添加
    message.success('添加到购物车成功')
  } catch (error) {
    console.error('添加到购物车失败:', error)
    message.error('添加到购物车失败，请稍后重试')
  }
}

// 切换页码
const handlePageChange = (newPage) => {
  if (newPage >= 1 && newPage <= totalPages.value) {
    currentPage.value = newPage
    fetchFavorites()
  }
}
</script>

<style scoped>
.favorites-page {
  min-height: 100vh;
  background-color: var(--bg);
}

.favorites-content {
  padding: var(--spacing-2xl) 0;
}

.favorites-header {
  text-align: center;
  margin-bottom: var(--spacing-2xl);
}

.favorites-header h1 {
  color: var(--text-1);
  font-size: var(--fs-2xl);
  font-weight: 600;
  margin-bottom: var(--spacing-sm);
}

.favorites-header p {
  color: var(--text-2);
  font-size: var(--fs-base);
  margin: 0;
}

.favorites-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: var(--spacing-xl);
  margin-bottom: var(--spacing-xl);
}

.favorite-card {
  background: var(--card);
  border-radius: var(--radius-base);
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
}

.favorite-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.favorite-image {
  position: relative;
  padding-top: 100%; /* 1:1 比例 */
  overflow: hidden;
}

.favorite-image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.favorite-card:hover .favorite-image img {
  transform: scale(1.05);
}

.favorite-btn {
  position: absolute;
  top: var(--spacing-base);
  right: var(--spacing-base);
  background: rgba(255, 255, 255, 0.9);
  border: none;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 16px;
  transition: all 0.3s ease;
  z-index: 10;
}

.favorite-btn:hover {
  background: white;
  transform: scale(1.1);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.favorite-info {
  padding: var(--spacing-base);
}

.favorite-name {
  font-size: var(--fs-base);
  font-weight: 500;
  color: var(--text-1);
  margin: 0 0 var(--spacing-xs) 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.favorite-desc {
  font-size: var(--fs-xs);
  color: var(--text-3);
  margin: 0 0 var(--spacing-base) 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.favorite-price {
  font-size: var(--fs-lg);
  font-weight: 600;
  color: var(--danger);
  margin-bottom: var(--spacing-base);
}

.favorite-actions {
  display: flex;
  gap: var(--spacing-sm);
}

.btn-sm {
  flex: 1;
  padding: var(--spacing-xs) var(--spacing-base);
  font-size: var(--fs-sm);
}

.empty-state {
  grid-column: 1 / -1;
  text-align: center;
  padding: var(--spacing-3xl) 0;
  background: var(--card);
  border-radius: var(--radius-base);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.empty-icon {
  font-size: 48px;
  margin-bottom: var(--spacing-lg);
}

.empty-state h3 {
  color: var(--text-1);
  font-size: var(--fs-lg);
  font-weight: 600;
  margin: 0 0 var(--spacing-sm) 0;
}

.empty-state p {
  color: var(--text-2);
  font-size: var(--fs-base);
  margin: 0 0 var(--spacing-xl) 0;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: var(--spacing-lg);
  margin-top: var(--spacing-xl);
}

.page-btn {
  padding: var(--spacing-sm) var(--spacing-lg);
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  background-color: white;
  color: var(--text-1);
  font-size: var(--fs-sm);
  cursor: pointer;
  transition: all 0.3s ease;
}

.page-btn:hover:not(:disabled) {
  border-color: var(--primary);
  color: var(--primary);
}

.page-btn:disabled {
  background-color: var(--bg);
  color: var(--text-3);
  cursor: not-allowed;
}

.page-info {
  font-size: var(--fs-sm);
  color: var(--text-2);
}

@media (max-width: 768px) {
  .favorites-content {
    padding: var(--spacing-xl) 0;
  }
  
  .favorites-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: var(--spacing-base);
  }
  
  .favorite-name {
    font-size: var(--fs-sm);
  }
  
  .favorite-price {
    font-size: var(--fs-base);
  }
  
  .btn-sm {
    padding: var(--spacing-xs);
    font-size: var(--fs-xs);
  }
}
</style>