<template>
  <div class="admin-layout">
    <!-- 左侧菜单栏 -->
    <aside class="admin-sidebar" :class="{ collapsed: !isMenuOpen }">
      <div class="sidebar-header">
        <div class="logo-icon">🐾</div>
        <h1 class="logo">宠物商城后台</h1>
      </div>
      <nav class="sidebar-menu">
        <ul>
          <!-- 首页（页面） -->
          <li class="menu-item" :class="{ active: $route.path === '/admin' }">
            <router-link to="/admin">首页</router-link>
          </li>
          
          <!-- 个人中心（页面） -->
          <li class="menu-item" :class="{ active: $route.path === '/admin/profile' }">
            <router-link to="/admin/profile">个人中心</router-link>
          </li>
          
          <!-- 管理员管理（菜单） -->
          <li class="menu-item" :class="{ active: $route.path === '/admin/admin-users', open: openMenus['admin-users'] }">
            <div class="menu-link" @click="toggleMenu('admin-users')">
              <span>管理员管理</span>
              <i class="menu-arrow">▼</i>
            </div>
            <ul class="submenu" v-if="openMenus['admin-users']">
              <li class="submenu-item" :class="{ active: $route.name === 'admin-admin-users' }">
                <router-link to="/admin/admin-users" @click="handleSubmenuClick('admin-users')">管理员列表</router-link>
              </li>
            </ul>
          </li>
          
          <!-- 用户管理（菜单） -->
          <li class="menu-item" :class="{ active: $route.path === '/admin/users', open: openMenus['users'] }">
            <div class="menu-link" @click="toggleMenu('users')">
              <span>用户管理</span>
              <i class="menu-arrow">▼</i>
            </div>
            <ul class="submenu" v-if="openMenus['users']">
              <li class="submenu-item" :class="{ active: $route.name === 'admin-users' }">
                <router-link to="/admin/users" @click="handleSubmenuClick('users')">用户列表</router-link>
              </li>
            </ul>
          </li>
          
          <!-- 商品管理（菜单） -->
          <li class="menu-item" :class="{ active: $route.path.includes('/admin/products') || $route.path.includes('/admin/chongwuyongpin-order') || $route.path.includes('/admin/chongwuyongpin-commentback'), open: openMenus['products'] }">
            <div class="menu-link" @click="toggleMenu('products')">
              <span>商品管理</span>
              <i class="menu-arrow">▼</i>
            </div>
            <ul class="submenu" v-if="openMenus['products']">
              <li class="submenu-item" :class="{ active: $route.name === 'admin-products' }">
                <router-link to="/admin/products" @click="handleSubmenuClick('products')">商品列表</router-link>
              </li>
              <li class="submenu-item" :class="{ active: $route.name === 'admin-chongwuyongpin-order' }">
                <router-link to="/admin/chongwuyongpin-order" @click="handleSubmenuClick('products')">商品订单管理</router-link>
              </li>
              <li class="submenu-item" :class="{ active: $route.name === 'admin-chongwuyongpin-commentback' }">
                <router-link to="/admin/chongwuyongpin-commentback" @click="handleSubmenuClick('products')">商品评价管理</router-link>
              </li>
            </ul>
          </li>

          <!-- 宠物管理（菜单） -->
          <li class="menu-item" :class="{ active: $route.path.includes('/admin/pet'), open: openMenus['pet'] }">
            <div class="menu-link" @click="toggleMenu('pet')">
              <span>宠物管理</span>
              <i class="menu-arrow">▼</i>
            </div>
            <ul class="submenu" v-if="openMenus['pet']">
              <li class="submenu-item" :class="{ active: $route.name === 'admin-pet-category' }">
                <router-link to="/admin/pet-category" @click="handleSubmenuClick('pet')">宠物分类管理</router-link>
              </li>
              <li class="submenu-item" :class="{ active: $route.name === 'admin-pet' }">
                <router-link to="/admin/pet" @click="handleSubmenuClick('pet')">宠物列表</router-link>
              </li>
              <li class="submenu-item" :class="{ active: $route.name === 'admin-pet-order' }">
                <router-link to="/admin/pet-order" @click="handleSubmenuClick('pet')">宠物订单管理</router-link>
              </li>
            </ul>
          </li>
          
          <!-- 宠物寄养管理（菜单） -->
          <li class="menu-item" :class="{ active: $route.path === '/admin/chongwujiyang', open: openMenus['chongwujiyang'] }">
            <div class="menu-link" @click="toggleMenu('chongwujiyang')">
              <span>宠物寄养管理</span>
              <i class="menu-arrow">▼</i>
            </div>
            <ul class="submenu" v-if="openMenus['chongwujiyang']">
              <li class="submenu-item" :class="{ active: $route.name === 'admin-chongwujiyang' }">
                <router-link to="/admin/chongwujiyang" @click="handleSubmenuClick('chongwujiyang')">寄养项目列表</router-link>
              </li>
            </ul>
          </li>
          
          <!-- 宠物寄养订单管理（菜单） -->
          <li class="menu-item" :class="{ active: $route.path === '/admin/chongwujiyang-yuyue', open: openMenus['chongwujiyang-yuyue'] }">
            <div class="menu-link" @click="toggleMenu('chongwujiyang-yuyue')">
              <span>宠物寄养订单管理</span>
              <i class="menu-arrow">▼</i>
            </div>
            <ul class="submenu" v-if="openMenus['chongwujiyang-yuyue']">
              <li class="submenu-item" :class="{ active: $route.name === 'admin-chongwujiyang-yuyue' }">
                <router-link to="/admin/chongwujiyang-yuyue" @click="handleSubmenuClick('chongwujiyang-yuyue')">寄养订单列表</router-link>
              </li>
            </ul>
          </li>
          
          <!-- 寄存日志管理（菜单） -->
          <li class="menu-item" :class="{ active: $route.path === '/admin/chongwurizhi', open: openMenus['chongwurizhi'] }">
            <div class="menu-link" @click="toggleMenu('chongwurizhi')">
              <span>寄存日志管理</span>
              <i class="menu-arrow">▼</i>
            </div>
            <ul class="submenu" v-if="openMenus['chongwurizhi']">
              <li class="submenu-item" :class="{ active: $route.name === 'admin-chongwurizhi' }">
                <router-link to="/admin/chongwurizhi" @click="handleSubmenuClick('chongwurizhi')">寄存日志列表</router-link>
              </li>
            </ul>
          </li>
          
          <!-- 客服聊天管理（菜单） -->
          <li class="menu-item" :class="{ active: $route.path === '/admin/customer-service', open: openMenus['customer-service'] }">
            <div class="menu-link" @click="toggleMenu('customer-service')">
              <span>客服聊天管理</span>
              <i class="menu-arrow">▼</i>
            </div>
            <ul class="submenu" v-if="openMenus['customer-service']">
              <li class="submenu-item" :class="{ active: $route.name === 'admin-customer-service' }">
                <router-link to="/admin/customer-service" @click="handleSubmenuClick('customer-service')">客服聊天列表</router-link>
              </li>
            </ul>
          </li>
          
          <!-- 基础数据管理（菜单） -->
          <li class="menu-item" :class="{ active: $route.path === '/admin/jichushuju', open: openMenus['jichushuju'] }">
            <div class="menu-link" @click="toggleMenu('jichushuju')">
              <span>基础数据管理</span>
              <i class="menu-arrow">▼</i>
            </div>
            <ul class="submenu" v-if="openMenus['jichushuju']">
              <li class="submenu-item" :class="{ active: $route.name === 'admin-jichushuju' }">
                <router-link to="/admin/jichushuju" @click="handleSubmenuClick('jichushuju')">字典管理</router-link>
              </li>
            </ul>
          </li>
          
          <!-- 论坛管理（菜单） -->
          <li class="menu-item" :class="{ active: $route.path === '/admin/luntan', open: openMenus['luntan'] }">
            <div class="menu-link" @click="toggleMenu('luntan')">
              <span>论坛管理</span>
              <i class="menu-arrow">▼</i>
            </div>
            <ul class="submenu" v-if="openMenus['luntan']">
              <li class="submenu-item" :class="{ active: $route.name === 'admin-luntan' }">
                <router-link to="/admin/luntan" @click="handleSubmenuClick('luntan')">论坛帖子列表</router-link>
              </li>
            </ul>
          </li>
          
          <!-- 公告信息管理（菜单） -->
          <li class="menu-item" :class="{ active: $route.path === '/admin/gonggao', open: openMenus['gonggao'] }">
            <div class="menu-link" @click="toggleMenu('gonggao')">
              <span>公告信息管理</span>
              <i class="menu-arrow">▼</i>
            </div>
            <ul class="submenu" v-if="openMenus['gonggao']">
              <li class="submenu-item" :class="{ active: $route.name === 'admin-gonggao' }">
                <router-link to="/admin/gonggao" @click="handleSubmenuClick('gonggao')">公告列表</router-link>
              </li>
            </ul>
          </li>
          
          <!-- 轮播图信息（菜单） -->
          <li class="menu-item" :class="{ active: $route.path === '/admin/lunbotu', open: openMenus['lunbotu'] }">
            <div class="menu-link" @click="toggleMenu('lunbotu')">
              <span>轮播图信息</span>
              <i class="menu-arrow">▼</i>
            </div>
            <ul class="submenu" v-if="openMenus['lunbotu']">
              <li class="submenu-item" :class="{ active: $route.name === 'admin-lunbotu' }">
                <router-link to="/admin/lunbotu" @click="handleSubmenuClick('lunbotu')">轮播图列表</router-link>
              </li>
            </ul>
          </li>
          
        </ul>
      </nav>
    </aside>
    
    <!-- 右侧主内容区 -->
    <main class="admin-main">
      <!-- 顶部导航栏 -->
      <header class="admin-header">
        <div class="header-left">
          <button class="menu-toggle" @click="toggleSidebar">
            <span></span>
            <span></span>
            <span></span>
          </button>
        </div>
        <div class="header-right">
          <div class="user-info">
            <router-link to="/admin/profile" class="username">{{ userInfo.username }}</router-link>
            <div class="user-avatar">
              <i class="icon-user"></i>
            </div>
            <button class="logout-btn" @click="handleLogout">退出登录</button>
          </div>
        </div>
      </header>
      
      <!-- 内容区域 -->
      <div class="admin-content">
        <router-view></router-view>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { usersApi } from '../../utils/api'

// 用户信息
const userInfo = ref({
  username: ''
})

// 菜单展开状态
const isMenuOpen = ref(true)

// 子菜单展开状态管理
const openMenus = ref({
  'admin-users': false,
  'users': false,
  'products': false,
  'chongwujiyang': false,
  'chongwujiyang-yuyue': false,
  'chongwurizhi': false,
  'customer-service': false,
  'jichushuju': false,
  'luntan': false,
  'gonggao': false,
  'lunbotu': false,
  'pet': false
})

// 切换子菜单展开状态
const toggleMenu = (menuKey) => {
  // 收起所有其他菜单
  Object.keys(openMenus.value).forEach(key => {
    if (key !== menuKey) {
      openMenus.value[key] = false
    }
  })
  // 切换当前菜单的状态
  openMenus.value[menuKey] = !openMenus.value[menuKey]
}

// 处理子菜单点击
const handleSubmenuClick = (menuKey) => {
  // 收起所有其他菜单
  Object.keys(openMenus.value).forEach(key => {
    if (key !== menuKey) {
      openMenus.value[key] = false
    }
  })
  // 确保当前菜单保持展开状态
  openMenus.value[menuKey] = true
}

// 初始根据当前路由自动展开对应菜单
const initMenuOpenState = () => {
  const path = window.location.pathname
  if (path.includes('/admin/admin-users')) {
    openMenus.value['admin-users'] = true
  } else if (path.includes('/admin/users')) {
    openMenus.value['users'] = true
  } else if (path.includes('/admin/products') || path.includes('/admin/chongwuyongpin-order') || path.includes('/admin/chongwuyongpin-commentback')) {
    openMenus.value['products'] = true
  } else if (path.includes('/admin/pet')) {
    openMenus.value['pet'] = true
  } else if (path.includes('/admin/chongwujiyang')) {
    openMenus.value['chongwujiyang'] = true
  } else if (path.includes('/admin/chongwujiyang-yuyue')) {
    openMenus.value['chongwujiyang-yuyue'] = true
  } else if (path.includes('/admin/chongwurizhi')) {
    openMenus.value['chongwurizhi'] = true
  } else if (path.includes('/admin/customer-service')) {
    openMenus.value['customer-service'] = true
  } else if (path.includes('/admin/jichushuju')) {
    openMenus.value['jichushuju'] = true
  } else if (path.includes('/admin/luntan')) {
    openMenus.value['luntan'] = true
  } else if (path.includes('/admin/gonggao')) {
    openMenus.value['gonggao'] = true
  } else if (path.includes('/admin/lunbotu')) {
    openMenus.value['lunbotu'] = true
  }
}

// 切换侧边栏展开状态
const toggleSidebar = () => {
  isMenuOpen.value = !isMenuOpen.value
}

// 获取用户信息
const getUserInfo = async () => {
  try {
    const response = await usersApi.session()
    if (response.code === 0) {
      userInfo.value = response.data
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

// 退出登录
const handleLogout = async () => {
  try {
    await usersApi.logout()
    // 清除本地存储的token
    localStorage.removeItem('token')
    // 跳转到登录页面
    window.location.href = '/admin/login'
  } catch (error) {
    console.error('退出登录失败:', error)
    // 强制清除token并跳转
    localStorage.removeItem('token')
    window.location.href = '/admin/login'
  }
}

// 组件挂载时获取用户信息和初始化菜单状态
onMounted(() => {
  getUserInfo()
  initMenuOpenState()
})
</script>

<style scoped>
.admin-layout {
  display: flex;
  height: 100vh;
  background-color: var(--bg);
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", sans-serif;
}

/* 左侧菜单栏 */
.admin-sidebar {
  width: 260px;
  background-color: white;
  color: var(--text-1);
  transition: width 0.3s ease;
  overflow: hidden;
  position: fixed;
  height: 100vh;
  z-index: 100;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.08);
  border-right: 1px solid var(--border);
}

.admin-sidebar.collapsed {
  width: 70px;
}

.sidebar-header {
  padding: 24px 20px;
  border-bottom: 1px solid var(--border);
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  color: var(--primary);
  text-align: left;
  flex: 1;
}

.logo-icon {
  width: 32px;
  height: 32px;
  background-color: var(--primary);
  border-radius: var(--radius-base);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 16px;
}

.sidebar-menu {
  padding: 16px 0;
}

.sidebar-menu ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.menu-item {
  margin: 0;
}

.menu-item a,
.menu-link {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 20px;
  color: var(--text-2);
  text-decoration: none;
  transition: all 0.3s ease;
  cursor: pointer;
  font-weight: 500;
  border-radius: 0 var(--radius-base) var(--radius-base) 0;
  margin: 0 12px;
}

.menu-item a:hover,
.menu-link:hover {
  background-color: var(--hover-bg);
  color: var(--primary);
}

.menu-item.active > a,
.menu-item.active > .menu-link {
  background-color: rgba(66, 184, 131, 0.1);
  color: var(--primary);
  border-left: 4px solid var(--primary);
}

.menu-arrow {
  font-size: 12px;
  color: var(--text-3);
  transition: transform 0.3s ease;
}

.menu-item:hover .menu-arrow {
  color: var(--primary);
}

/* 子菜单样式 */
.submenu {
  background-color: white;
  list-style: none;
  padding: 0;
  margin: 0;
  overflow: hidden;
  transition: all 0.3s ease;
}

/* 移除hover显示子菜单的样式 */
/* .menu-item:hover .submenu {
  display: block;
} */

/* 打开状态样式 */
.menu-item.open .menu-arrow {
  transform: rotate(180deg);
}

.submenu-item {
  margin: 0;
}

.submenu-item a {
  display: flex;
  align-items: center;
  padding: 12px 20px 12px 48px;
  color: var(--text-2);
  text-decoration: none;
  transition: all 0.3s ease;
  font-size: 14px;
  font-weight: 400;
  margin: 0 16px;
  border-radius: var(--radius-base);
  border-left: none;
}

.submenu-item a:hover {
  background-color: var(--hover-bg);
  color: var(--primary);
}

.submenu-item.active a {
  background-color: rgba(66, 184, 131, 0.1);
  color: var(--primary);
  border-left: 4px solid var(--primary);
}

/* 右侧主内容区 */
.admin-main {
  flex: 1;
  margin-left: 260px;
  transition: margin-left 0.3s ease;
  display: flex;
  flex-direction: column;
}

.admin-sidebar.collapsed + .admin-main {
  margin-left: 70px;
}

/* 顶部导航栏 */
.admin-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  height: 64px;
  background-color: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 90;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.menu-toggle {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  color: var(--text-2);
  display: none;
  flex-direction: column;
  justify-content: space-around;
  width: 24px;
  height: 20px;
  padding: 0;
  transition: all 0.3s ease;
}

.menu-toggle:hover {
  color: var(--primary);
}

.menu-toggle span {
  display: block;
  width: 100%;
  height: 2px;
  background-color: var(--text-2);
  transition: all 0.3s ease;
}

.menu-toggle:hover span {
  background-color: var(--primary);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 24px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.username {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-1);
  cursor: pointer;
  text-decoration: none;
  transition: color 0.3s ease;
}

.username:hover {
  color: var(--primary);
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: var(--primary);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-avatar:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(66, 184, 131, 0.3);
}

.logout-btn {
  padding: 8px 16px;
  background-color: white;
  color: var(--text-1);
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 6px;
}

.logout-btn:hover {
  background-color: var(--hover-bg);
  color: var(--danger);
  border-color: var(--danger);
}

/* 内容区域 */
.admin-content {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .admin-sidebar {
    transform: translateX(-100%);
  }
  
  .admin-sidebar.open {
    transform: translateX(0);
  }
  
  .admin-main {
    margin-left: 0;
  }
  
  .menu-toggle {
    display: flex;
  }
}
</style>