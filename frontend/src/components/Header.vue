<template>
  <header class="navbar">
    <div class="container">
      <div class="navbar-content">
        <div class="logo">
          <h1>宠物电商平台</h1>
        </div>
        
        <!-- 搜索框 -->
        <div class="search-container">
          <input type="text" class="search-input" placeholder="搜索商品、品牌...">
          <button class="search-btn">🔍</button>
        </div>
        
        <div class="navbar-actions">
          <!-- 导航链接 -->
          <nav class="nav">
            <router-link to="/" class="nav-link">首页</router-link>
            <router-link to="/products" class="nav-link">商品</router-link>
            <router-link to="/chongwujiyang" class="nav-link">宠物寄养</router-link>
            <router-link to="/forum" class="nav-link">论坛</router-link>
            <router-link to="/news" class="nav-link">资讯</router-link>
          </nav>
          
          <!-- 购物车图标 -->
          <div class="cart-container" @mouseenter="handleCartMouseEnter" @mouseleave="handleCartMouseLeave">
            <a href="/cart" class="cart-icon" @click.prevent="navigateToCart">
              🛒
              <span v-if="cartCount > 0" class="cart-badge">{{ cartCount }}</span>
            </a>
            
            <!-- 购物车悬浮弹窗 -->
            <div 
              v-if="showCartPopup" 
              class="cart-popup"
              @mouseenter="handleCartMouseEnter"
              @mouseleave="handleCartMouseLeave"
            >
              <div class="cart-popup-header">
                <h3>购物车</h3>
                <a href="/cart" class="view-cart-link" @click.prevent="navigateToCart">查看全部</a>
              </div>
              <div v-if="cartItems.length > 0" class="cart-popup-content">
                <div v-for="item in cartItems" :key="item.id" class="cart-item">
                  <div class="cart-item-image">
                    <img :src="'http://localhost:8080/wangshangchongwudian/' + item.chongwuyongpinPhoto" :alt="item.chongwuyongpinName">
                  </div>
                  <div class="cart-item-info">
                    <div class="cart-item-name">{{ item.chongwuyongpinName }}</div>
                    <div class="cart-item-price">¥{{ item.chongwuyongpinPrice }}</div>
                  </div>
                  <div class="cart-item-quantity">x{{ item.count }}</div>
                </div>
              </div>
              <div v-else class="cart-empty">
                购物车是空的
              </div>
              <div v-if="cartItems.length > 0" class="cart-popup-footer">
                <div class="cart-total">
                  合计: <span class="total-price">¥{{ totalPrice }}</span>
                </div>
                <a href="/cart" class="checkout-btn" @click.prevent="navigateToCart">去结算</a>
              </div>
            </div>
          </div>
          
          <!-- 用户操作按钮 -->
          <div v-if="!userInfo" class="user-actions">
            <router-link to="/user/login" class="btn btn-primary">登录</router-link>
            <router-link to="/user/register" class="btn btn-secondary">注册</router-link>
          </div>
          
          <!-- 用户信息 -->
          <div v-else class="user-info">
            <div class="user-avatar" @click="toggleUserMenu">
              <img v-if="userInfo.yonghuPhoto" :src="'http://localhost:8080/wangshangchongwudian/' + userInfo.yonghuPhoto" :alt="userInfo.yonghuName" style="max-width: 100px; max-height: 100px;">
              <div v-else class="avatar-placeholder">{{ userInfo.yonghuName?.charAt(0) || '用' }}</div>
              <span class="user-name">{{ userInfo.username || userInfo.yonghuName }}</span>
            </div>
            
            <div v-if="showUserMenu" class="user-menu">
              <router-link to="/user/profile" class="menu-item">个人中心</router-link>
              <router-link to="/user/orders" class="menu-item">我的订单</router-link>
              <router-link to="/user/pet-orders" class="menu-item">宠物寄存订单</router-link>
              <router-link to="/user/comments" class="menu-item">我的评价</router-link>
              <router-link to="/user/favorites" class="menu-item">我的收藏</router-link>
              <router-link to="/user/address" class="menu-item">地址管理</router-link>
              <div class="menu-divider"></div>
              <div class="menu-item logout" @click="handleLogout">退出登录</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { yonghuApi, cartApi } from '../utils/api'
import message from '../utils/message'

const router = useRouter()
const userInfo = ref(null)
const showUserMenu = ref(false)
const showCartPopup = ref(false)
const cartItems = ref([])

// 计算购物车商品总数
const cartCount = computed(() => {
  return cartItems.value.reduce((total, item) => total + item.count, 0)
})

// 计算购物车总价
const totalPrice = computed(() => {
  return cartItems.value.reduce((total, item) => total + (item.chongwuyongpinPrice * item.count), 0).toFixed(2)
})

// 获取用户信息
const getUserInfo = async () => {
  try {
    const token = localStorage.getItem('token')
    if (!token) return
    
    const response = await yonghuApi.session()
    if (response.code === 0) {
      userInfo.value = response.data
      // 获取用户信息后，获取购物车数据
      getCartList()
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    // 清除token
    localStorage.removeItem('token')
  }
}

// 获取购物车列表
const getCartList = async () => {
  try {
    const token = localStorage.getItem('token')
    if (!token || !userInfo.value) return
    
    // 传递yonghuId参数
    const response = await cartApi.getList({ yonghuId: userInfo.value.id })
    if (response.code === 0) {
      // 适配接口返回的数据结构
      if (response.data && response.data.list) {
        cartItems.value = response.data.list.map(item => ({
          ...item,
          count: item.buyNumber, // 适配数量字段
          chongwuyongpinPrice: item.chongwuyongpinNewMoney // 适配价格字段
        }))
      } else {
        cartItems.value = []
      }
    }
  } catch (error) {
    console.error('获取购物车列表失败:', error)
  }
}

// 切换用户菜单
const toggleUserMenu = () => {
  showUserMenu.value = !showUserMenu.value
}

// 退出登录
const handleLogout = () => {
  localStorage.removeItem('token')
  userInfo.value = null
  showUserMenu.value = false
  cartItems.value = []
  message.success('退出登录成功')
  router.push('/')
}

// 导航到购物车页面
const navigateToCart = () => {
  showCartPopup.value = false
  router.push('/cart')
}

// 处理购物车鼠标进入事件
const handleCartMouseEnter = () => {
  showCartPopup.value = true
}

// 处理购物车鼠标离开事件
const handleCartMouseLeave = () => {
  // 延迟关闭弹窗，确保鼠标有足够时间从图标移动到弹窗
  setTimeout(() => {
    showCartPopup.value = false
  }, 300)
}

// 点击外部关闭菜单
const handleClickOutside = (event) => {
  if (!event.target.closest('.user-info')) {
    showUserMenu.value = false
  }
  if (!event.target.closest('.cart-container')) {
    showCartPopup.value = false
  }
}

// 页面加载时获取用户信息
onMounted(() => {
  getUserInfo()
  document.addEventListener('click', handleClickOutside)
})

// 组件销毁时移除事件监听
onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.navbar {
  background: var(--card);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: var(--spacing-base) 0;
  position: sticky;
  top: 0;
  z-index: 100;
}

.navbar-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: var(--spacing-xl);
}

.logo h1 {
  margin: 0;
  font-size: var(--fs-xl);
  color: var(--primary);
  font-weight: 600;
}

.search-container {
  flex: 1;
  max-width: 500px;
  position: relative;
  display: flex;
  align-items: center;
}

.search-input {
  width: 100%;
  background: var(--bg);
  border: none;
  border-radius: var(--radius-full);
  padding: var(--spacing-base) var(--spacing-xl);
  font-size: var(--fs-base);
  outline: none;
}

.search-btn {
  position: absolute;
  right: 4px;
  background: var(--primary);
  color: white;
  border: none;
  border-radius: var(--radius-full);
  padding: var(--spacing-sm) var(--spacing-base);
  cursor: pointer;
  font-size: var(--fs-base);
}

.navbar-actions {
  display: flex;
  align-items: center;
  gap: var(--spacing-xl);
}

.nav {
  display: flex;
  gap: var(--spacing-xl);
}

.nav-link {
  color: var(--text-1);
  text-decoration: none;
  font-weight: 500;
  font-size: var(--fs-base);
  transition: all 0.3s ease;
  padding: var(--spacing-sm) 0;
  position: relative;
}

.nav-link:hover {
  color: var(--primary);
}

.nav-link.active {
  color: var(--primary);
}

.nav-link.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background: var(--primary);
  border-radius: var(--radius-full);
}

.cart-icon {
  position: relative;
  font-size: 24px;
  color: var(--text-1);
  text-decoration: none;
  cursor: pointer;
  transition: color 0.3s ease;
}

.cart-icon:hover {
  color: var(--primary);
}

.cart-badge {
  position: absolute;
  top: -8px;
  right: -12px;
  background: var(--danger);
  color: white;
  border-radius: var(--radius-full);
  min-width: 20px;
  height: 20px;
  font-size: var(--fs-xs);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 6px;
  font-weight: 600;
}

.user-actions {
  display: flex;
  gap: var(--spacing-base);
}

/* 用户信息样式 */
.user-info {
  position: relative;
}

.user-avatar {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  cursor: pointer;
  padding: var(--spacing-sm);
  border-radius: var(--radius-base);
  transition: background-color 0.3s ease;
}

.user-avatar:hover {
  background-color: var(--hover-bg);
}

.user-avatar img {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
}

.avatar-placeholder {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary) 0%, var(--primary-light) 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: var(--fs-base);
}

.user-name {
  font-size: var(--fs-sm);
  font-weight: 500;
  color: var(--text-1);
}

/* 用户菜单 */
.user-menu {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: var(--spacing-sm);
  background: var(--card);
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-lg);
  min-width: 180px;
  z-index: 1000;
  border: 1px solid var(--border);
}

.menu-item {
  display: block;
  padding: var(--spacing-base);
  color: var(--text-1);
  text-decoration: none;
  font-size: var(--fs-sm);
  transition: background-color 0.3s ease;
  cursor: pointer;
}

.menu-item:hover {
  background-color: var(--hover-bg);
}

.menu-divider {
  height: 1px;
  background-color: var(--border);
  margin: var(--spacing-xs) 0;
}

.menu-item.logout {
  color: var(--danger);
}

.menu-item.logout:hover {
  background-color: rgba(255, 77, 79, 0.05);
}

/* 购物车容器 */
.cart-container {
  position: relative;
  display: inline-block;
  padding: var(--spacing-sm);
  border-radius: var(--radius-base);
  transition: background-color 0.3s ease;
}

.cart-container:hover {
  background-color: var(--hover-bg);
}

/* 购物车悬浮弹窗 */
.cart-popup {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: 0;
  background: var(--card);
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-lg);
  min-width: 300px;
  max-width: 350px;
  z-index: 1000;
  border: 1px solid var(--border);
  max-height: 400px;
  overflow-y: auto;
}

/* 购物车弹窗头部 */
.cart-popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-base);
  border-bottom: 1px solid var(--border);
}

.cart-popup-header h3 {
  margin: 0;
  font-size: var(--fs-base);
  font-weight: 600;
  color: var(--text-1);
}

.view-cart-link {
  font-size: var(--fs-sm);
  color: var(--primary);
  text-decoration: none;
  transition: color 0.3s ease;
}

.view-cart-link:hover {
  color: var(--primary-dark);
  text-decoration: underline;
}

/* 购物车弹窗内容 */
.cart-popup-content {
  padding: var(--spacing-base);
}

/* 购物车项 */
.cart-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
  padding: var(--spacing-base) 0;
  border-bottom: 1px solid var(--border);
}

.cart-item:last-child {
  border-bottom: none;
}

/* 商品图片 */
.cart-item-image {
  width: 60px;
  height: 60px;
  border-radius: var(--radius-sm);
  overflow: hidden;
  flex-shrink: 0;
}

.cart-item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 商品信息 */
.cart-item-info {
  flex: 1;
  min-width: 0;
}

.cart-item-name {
  font-size: var(--fs-sm);
  font-weight: 500;
  color: var(--text-1);
  margin-bottom: var(--spacing-xs);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.cart-item-price {
  font-size: var(--fs-sm);
  color: var(--primary);
  font-weight: 600;
}

/* 商品数量 */
.cart-item-quantity {
  font-size: var(--fs-sm);
  color: var(--text-2);
  font-weight: 500;
}

/* 空购物车 */
.cart-empty {
  padding: var(--spacing-xl);
  text-align: center;
  color: var(--text-2);
  font-size: var(--fs-sm);
}

/* 购物车弹窗底部 */
.cart-popup-footer {
  padding: var(--spacing-base);
  border-top: 1px solid var(--border);
  background: var(--bg);
}

/* 购物车合计 */
.cart-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-base);
  font-size: var(--fs-sm);
  font-weight: 500;
  color: var(--text-1);
}

.total-price {
  font-size: var(--fs-base);
  font-weight: 600;
  color: var(--primary);
}

/* 结算按钮 */
.checkout-btn {
  display: block;
  width: 100%;
  background: var(--primary);
  color: white;
  text-align: center;
  padding: var(--spacing-base);
  border-radius: var(--radius-base);
  text-decoration: none;
  font-size: var(--fs-base);
  font-weight: 600;
  transition: background-color 0.3s ease;
}

.checkout-btn:hover {
  background: var(--primary-dark);
  color: white;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .navbar-content {
    flex-wrap: wrap;
    gap: var(--spacing-base);
  }
  
  .search-container {
    order: 3;
    width: 100%;
    max-width: 100%;
  }
  
  .nav {
    gap: var(--spacing-base);
  }
  
  .logo h1 {
    font-size: var(--fs-lg);
  }
  
  /* 移动端购物车弹窗 */
  .cart-popup {
    min-width: 280px;
    max-width: 280px;
  }
  
  .cart-item {
    padding: var(--spacing-sm) 0;
  }
  
  .cart-item-image {
    width: 50px;
    height: 50px;
  }
}

/* 滚动条样式 */
.cart-popup::-webkit-scrollbar {
  width: 6px;
}

.cart-popup::-webkit-scrollbar-track {
  background: var(--bg);
  border-radius: var(--radius-full);
}

.cart-popup::-webkit-scrollbar-thumb {
  background: var(--border);
  border-radius: var(--radius-full);
}

.cart-popup::-webkit-scrollbar-thumb:hover {
  background: var(--text-3);
}
</style>