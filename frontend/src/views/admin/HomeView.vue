<template>
  <div class="admin-home">
    <h2>后台管理系统首页</h2>
    <div class="admin-dashboard">
      <div class="dashboard-card">
        <div class="dashboard-icon users-icon">👥</div>
        <h3>用户总数</h3>
        <p class="dashboard-number">{{ isLoading ? '加载中...' : statistics.userCount }}</p>
      </div>
      <div class="dashboard-card">
        <div class="dashboard-icon products-icon">🐶</div>
        <h3>商品总数</h3>
        <p class="dashboard-number">{{ isLoading ? '加载中...' : statistics.productCount }}</p>
      </div>
      <div class="dashboard-card">
        <div class="dashboard-icon orders-icon">📦</div>
        <h3>订单总数</h3>
        <p class="dashboard-number">{{ isLoading ? '加载中...' : statistics.orderCount }}</p>
      </div>
      <div class="dashboard-card">
        <div class="dashboard-icon today-orders-icon">📊</div>
        <h3>今日订单</h3>
        <p class="dashboard-number">{{ isLoading ? '加载中...' : statistics.todayOrderCount }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
// 后台管理首页
import { ref, onMounted } from 'vue'
import { yonghuApi, chongwuyongpinApi, chongwuyongpinOrderApi } from '../../utils/api'

// 统计数据
const statistics = ref({
  userCount: 0,
  productCount: 0,
  orderCount: 0,
  todayOrderCount: 0
})

// 加载状态
const isLoading = ref(false)

// 获取统计数据
const getStatistics = async () => {
  isLoading.value = true
  try {
    // 获取用户总数
    const userResponse = await yonghuApi.getPage({ page: 1, limit: 1 })
    if (userResponse.code === 0) {
      statistics.value.userCount = userResponse.data.total || 0
    }

    // 获取商品总数
    const productResponse = await chongwuyongpinApi.getPage({ page: 1, limit: 1 })
    if (productResponse.code === 0) {
      statistics.value.productCount = productResponse.data.total || 0
    }

    // 获取订单总数
    const orderResponse = await chongwuyongpinOrderApi.getPage({ page: 1, limit: 1 })
    if (orderResponse.code === 0) {
      statistics.value.orderCount = orderResponse.data.total || 0
    }

    // 获取今日订单数 (这里假设API支持按日期过滤，具体实现可能需要调整)
    const today = new Date().toISOString().split('T')[0]
    const todayOrderResponse = await chongwuyongpinOrderApi.getPage({
      page: 1, 
      limit: 1,
      createTime: today
    })
    if (todayOrderResponse.code === 0) {
      statistics.value.todayOrderCount = todayOrderResponse.data.total || 0
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  } finally {
    isLoading.value = false
  }
}

// 组件挂载时获取数据
onMounted(() => {
  getStatistics()
})
</script>

<style scoped>
.admin-home {
  padding: 0;
}

.admin-dashboard {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
  margin-top: 24px;
}

.dashboard-card {
  background-color: var(--card);
  padding: 24px;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-base);
  text-align: center;
  transition: all 0.3s ease;
  border: 1px solid var(--border);
  position: relative;
  overflow: hidden;
}

.dashboard-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
}

.dashboard-icon {
  font-size: 32px;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 64px;
  height: 64px;
  margin: 0 auto 16px;
  border-radius: 50%;
  background-color: rgba(66, 184, 131, 0.1);
}

.users-icon {
  background-color: rgba(66, 184, 131, 0.1);
  color: var(--primary);
}

.products-icon {
  background-color: rgba(70, 169, 255, 0.1);
  color: var(--secondary);
}

.orders-icon {
  background-color: rgba(255, 140, 56, 0.1);
  color: var(--accent);
}

.today-orders-icon {
  background-color: rgba(250, 173, 20, 0.1);
  color: var(--warning);
}

.dashboard-card h3 {
  margin-bottom: 12px;
  color: var(--text-2);
  font-size: 16px;
  font-weight: 500;
}

.dashboard-number {
  font-size: 40px;
  font-weight: 700;
  color: var(--text-1);
  margin-bottom: 8px;
}

/* 各卡片添加不同的渐变背景装饰 */
.dashboard-card:nth-child(1)::after {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 120px;
  height: 120px;
  background: linear-gradient(135deg, rgba(66, 184, 131, 0.1), rgba(66, 184, 131, 0.05));
  border-radius: 0 0 0 50%;
  pointer-events: none;
}

.dashboard-card:nth-child(2)::after {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 120px;
  height: 120px;
  background: linear-gradient(135deg, rgba(70, 169, 255, 0.1), rgba(70, 169, 255, 0.05));
  border-radius: 0 0 0 50%;
  pointer-events: none;
}

.dashboard-card:nth-child(3)::after {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 120px;
  height: 120px;
  background: linear-gradient(135deg, rgba(255, 140, 56, 0.1), rgba(255, 140, 56, 0.05));
  border-radius: 0 0 0 50%;
  pointer-events: none;
}

.dashboard-card:nth-child(4)::after {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 120px;
  height: 120px;
  background: linear-gradient(135deg, rgba(250, 173, 20, 0.1), rgba(250, 173, 20, 0.05));
  border-radius: 0 0 0 50%;
  pointer-events: none;
}
</style>