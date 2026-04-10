<template>
  <div class="cart-page">
    <Header />
    <main class="cart-content">
      <div class="container">
        <!-- 页面标题 -->
        <div class="page-header">
          <h1>购物车</h1>
          <p>管理您的购物车商品</p>
        </div>

        <!-- 加载中 -->
        <div v-if="loading" class="loading">
          <div class="loading-spinner"></div>
          <p>加载中...</p>
        </div>

        <!-- 购物车商品列表 -->
        <div v-else-if="cartItems.length > 0" class="cart-items">
          <div class="cart-header">
            <div class="cart-col cart-select">
              <input type="checkbox" v-model="selectAll" @change="handleSelectAll">
            </div>
            <div class="cart-col cart-image">商品图片</div>
            <div class="cart-col cart-info">商品信息</div>
            <div class="cart-col cart-price">单价</div>
            <div class="cart-col cart-quantity">数量</div>
            <div class="cart-col cart-subtotal">小计</div>
            <div class="cart-col cart-action">操作</div>
          </div>

          <div v-for="item in cartItems" :key="item.id" class="cart-item">
            <div class="cart-col cart-select">
              <input type="checkbox" v-model="item.selected" @change="handleSelectItem">
            </div>
            <div class="cart-col cart-image">
              <img :src="'http://localhost:8080/wangshangchongwudian/' + item.chongwuyongpinPhoto" :alt="item.chongwuyongpinName">
            </div>
            <div class="cart-col cart-info">
              <h3 class="product-name">{{ item.chongwuyongpinName }}</h3>
              <p class="product-type">{{ item.chongwuyongpinValue }}</p>
              <p class="product-desc">{{ item.chongwuyongpinContent }}</p>
              <p class="product-stock">库存：{{ item.chongwuyongpinKucunNumber }} 件</p>
            </div>
            <div class="cart-col cart-price">¥{{ item.chongwuyongpinNewMoney }}</div>
            <div class="cart-col cart-quantity">
              <div class="quantity-control">
                <button class="quantity-btn" @click="decreaseQuantity(item)" :disabled="item.buyNumber <= 1">-</button>
                <input type="number" v-model.number="item.buyNumber" min="1" @change="updateQuantity(item)">
                <button class="quantity-btn" @click="increaseQuantity(item)">+</button>
              </div>
            </div>
            <div class="cart-col cart-subtotal">¥{{ (item.chongwuyongpinNewMoney * item.buyNumber).toFixed(2) }}</div>
            <div class="cart-col cart-action">
              <button class="btn btn-sm btn-danger" @click="removeItem(item.id)">删除</button>
            </div>
          </div>
        </div>

        <!-- 空购物车 -->
        <div v-else class="empty-cart">
          <div class="empty-icon">🛒</div>
          <h3>购物车是空的</h3>
          <p>快去挑选心仪的商品吧！</p>
          <router-link to="/products" class="btn btn-primary">去购物</router-link>
        </div>

        <!-- 结算栏 -->
        <div v-if="!loading && cartItems.length > 0" class="checkout-bar">
          <div class="checkout-left">
            <input type="checkbox" v-model="selectAll" @change="handleSelectAll">
            <span>全选</span>
            <button class="btn btn-sm btn-secondary" @click="removeSelectedItems">删除选中</button>
          </div>
          <div class="checkout-right">
            <div class="total-section">
              <span class="total-label">合计：</span>
              <span class="total-price">¥{{ totalPrice.toFixed(2) }}</span>
            </div>
            <button class="btn btn-primary" @click="checkout" :disabled="selectedItems.length === 0">
              结算 ({{ selectedItems.length }})
            </button>
          </div>
        </div>
      </div>
    </main>
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { cartApi, chongwuyongpinOrderApi, usersApi } from '../../utils/api'
import message from '../../utils/message'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'

const router = useRouter()
const cartItems = ref([])
const loading = ref(false)
const selectAll = ref(false)

// 加载购物车商品
const loadCartItems = async () => {
  try {
    loading.value = true
    
    // 获取用户会话信息
    const sessionResponse = await usersApi.session()
    if (sessionResponse.code === 0 && sessionResponse.data) {
      const yonghuId = sessionResponse.data.yonghuId || sessionResponse.data.id
      
      // 调用购物车API，传递用户id
      const response = await cartApi.getList({ page: 1, limit: 8, yonghuId, t: Date.now() })
      if (response.code === 0) {
        cartItems.value = (response.data.list || []).map(item => ({
          ...item,
          selected: true,
          // 确保buyNumber字段存在
          buyNumber: item.buyNumber || 1
        }))
        updateSelectAll()
      } else {
        message.error('获取购物车商品失败')
      }
    } else {
      message.error('获取用户信息失败')
    }
  } catch (error) {
    console.error('获取购物车商品失败:', error)
    message.error('获取购物车商品失败')
  } finally {
    loading.value = false
  }
}

// 更新全选状态
const updateSelectAll = () => {
  selectAll.value = cartItems.value.length > 0 && cartItems.value.every(item => item.selected)
}

// 处理全选
const handleSelectAll = () => {
  cartItems.value.forEach(item => {
    item.selected = selectAll.value
  })
}

// 处理单个商品选择
const handleSelectItem = () => {
  updateSelectAll()
}

// 减少数量
const decreaseQuantity = async (item) => {
  if (item.buyNumber > 1) {
    item.buyNumber--
    await updateQuantity(item)
  }
}

// 增加数量
const increaseQuantity = async (item) => {
  item.buyNumber++
  await updateQuantity(item)
}

// 更新商品数量
const updateQuantity = async (item) => {
  try {
    const response = await cartApi.update({
      id: item.id,
      buyNumber: item.buyNumber,
      t: Date.now()
    })
    if (response.code !== 0) {
      message.error('更新数量失败')
      // 恢复原数量
      loadCartItems()
    }
  } catch (error) {
    console.error('更新数量失败:', error)
    message.error('更新数量失败')
    // 恢复原数量
    loadCartItems()
  }
}

// 删除商品
const removeItem = async (itemId) => {
  try {
    const response = await cartApi.delete([itemId])
    if (response.code === 0) {
      message.success('删除成功')
      loadCartItems()
    } else {
      message.error('删除失败')
    }
  } catch (error) {
    console.error('删除失败:', error)
    message.error('删除失败')
  }
}

// 删除选中商品
const removeSelectedItems = async () => {
  const selectedIds = selectedItems.value.map(item => item.id)
  if (selectedIds.length === 0) {
    message.error('请选择要删除的商品')
    return
  }

  try {
    const response = await cartApi.delete(selectedIds)
    if (response.code === 0) {
      message.success('删除成功')
      loadCartItems()
    } else {
      message.error('删除失败')
    }
  } catch (error) {
    console.error('删除失败:', error)
    message.error('删除失败')
  }
}

// 计算总价格
const totalPrice = computed(() => {
  return selectedItems.value.reduce((total, item) => {
    return total + (item.chongwuyongpinNewMoney * item.buyNumber)
  }, 0)
})

// 选中的商品
const selectedItems = computed(() => {
  return cartItems.value.filter(item => item.selected)
})

// 结算
const checkout = async () => {
  if (selectedItems.value.length === 0) {
    message.error('请选择要结算的商品')
    return
  }

  // 跳转到订单确认页面，并传递选中的商品信息
  router.push({
    path: '/order-confirm',
    query: {
      selectedItems: JSON.stringify(selectedItems.value)
    }
  })
}

// 页面加载时获取购物车商品
onMounted(() => {
  loadCartItems()
})
</script>

<style scoped>
.cart-page {
  min-height: 100vh;
  background-color: var(--bg);
}

.cart-content {
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

.empty-cart {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-3xl) 0;
  text-align: center;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: var(--spacing-lg);
}

.empty-cart h3 {
  color: var(--text-1);
  font-size: var(--fs-xl);
  font-weight: 600;
  margin-bottom: var(--spacing-sm);
}

.empty-cart p {
  color: var(--text-2);
  font-size: var(--fs-base);
  margin-bottom: var(--spacing-xl);
}

.cart-items {
  background: var(--card);
  border-radius: var(--radius-base);
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  margin-bottom: var(--spacing-xl);
}

.cart-header {
  display: grid;
  grid-template-columns: 60px 120px 1fr 100px 120px 120px 80px;
  gap: var(--spacing-base);
  padding: var(--spacing-lg);
  background-color: #f8f9fa;
  border-bottom: 1px solid var(--border);
  font-weight: 500;
  color: var(--text-1);
  font-size: var(--fs-sm);
}

.cart-item {
  display: grid;
  grid-template-columns: 60px 120px 1fr 100px 120px 120px 80px;
  gap: var(--spacing-base);
  padding: var(--spacing-lg);
  border-bottom: 1px solid var(--border);
  align-items: center;
}

.cart-item:last-child {
  border-bottom: none;
}

.cart-select {
  display: flex;
  align-items: center;
  justify-content: center;
}

.cart-image img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: var(--radius-base);
}

.cart-info {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
}

.product-name {
  font-size: var(--fs-base);
  font-weight: 500;
  color: var(--text-1);
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-type {
  font-size: var(--fs-xs);
  color: var(--primary);
  margin: 0;
  font-weight: 500;
}

.product-desc {
  font-size: var(--fs-xs);
  color: var(--text-3);
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-stock {
  font-size: var(--fs-xs);
  color: var(--success);
  margin: 0;
}

.cart-price {
  font-size: var(--fs-base);
  font-weight: 500;
  color: var(--text-1);
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
}

.quantity-btn {
  width: 28px;
  height: 28px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  background-color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--fs-base);
  transition: all 0.3s ease;
}

.quantity-btn:hover:not(:disabled) {
  border-color: var(--primary);
  color: var(--primary);
}

.quantity-btn:disabled {
  cursor: not-allowed;
  color: var(--text-3);
  border-color: var(--border);
}

.quantity-control input {
  width: 50px;
  height: 28px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  text-align: center;
  font-size: var(--fs-base);
}

.cart-subtotal {
  font-size: var(--fs-base);
  font-weight: 600;
  color: var(--danger);
}

.cart-action {
  display: flex;
  align-items: center;
  justify-content: center;
}

.checkout-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-lg);
  background: var(--card);
  border-radius: var(--radius-base);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.checkout-left {
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
}

.checkout-left input {
  margin-right: var(--spacing-xs);
}

.checkout-right {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
}

.total-section {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.total-label {
  font-size: var(--fs-base);
  color: var(--text-1);
}

.total-price {
  font-size: var(--fs-xl);
  font-weight: 600;
  color: var(--danger);
}

.checkout-bar .btn {
  font-size: var(--fs-base);
  padding: var(--spacing-base) var(--spacing-xl);
}

@media (max-width: 768px) {
  .cart-header {
    display: none;
  }
  
  .cart-item {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-base);
    padding: var(--spacing-lg);
  }
  
  .cart-select {
    align-self: flex-start;
  }
  
  .cart-image {
    align-self: flex-start;
  }
  
  .cart-info {
    width: 100%;
  }
  
  .cart-price, .cart-quantity, .cart-subtotal, .cart-action {
    align-self: flex-start;
  }
  
  .checkout-bar {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-base);
  }
  
  .checkout-right {
    width: 100%;
    justify-content: space-between;
  }
}
</style>