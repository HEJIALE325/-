<template>
  <div class="chongwujiyang-page">
    <Header />
    <main class="chongwujiyang-content">
      <div class="container">
        <!-- 页面标题 -->
        <div class="page-header">
          <h1>宠物寄养服务</h1>
          <p>为您的爱宠提供专业的寄养服务</p>
        </div>

        <!-- 筛选 -->
        <div class="filter-section">
          <div class="filter-left">
            <div class="category-filter">
              <label>服务类型：</label>
              <select v-model="filter.category">
                <option value="">全部类型</option>
                <option v-for="type in serviceTypes" :key="type.codeIndex" :value="type.codeIndex">
                  {{ type.indexName }}
                </option>
              </select>
            </div>
            <div class="name-filter">
              <label>服务名称：</label>
              <input type="text" v-model="filter.name" placeholder="服务名称">
            </div>
          </div>
          <div class="filter-right">
            <button class="btn btn-primary" @click="loadServices">查询</button>
          </div>
        </div>

        <!-- 服务列表 -->
        <div v-if="!loading" class="services-grid">
          <div v-for="service in services" :key="service.id">
            <router-link :to="`/chongwujiyang/detail/${service.id}`" class="service-card-link">
              <div class="service-card">
                <!-- 服务图片 -->
                <div class="service-image">
                  <img :src="`http://localhost:8080/wangshangchongwudian/` + service.chongwujiyangPhoto" :alt="service.chongwujiyangName">
                </div>
                
                <!-- 服务信息 -->
                <div class="service-info">
                  <h3 class="service-title">{{ service.chongwujiyangName }}</h3>
                  <p class="service-type">{{ getServiceTypeName(service.chongwujiyangTypes) }}</p>
                  <p class="service-desc">{{ service.chongwujiyangContent }}</p>
                  <div class="service-meta">
                    <span class="service-click">热度：{{ service.chongwujiyangClicknum }}</span>
                  </div>
                  <div class="service-price-row">
                    <span class="service-price">¥{{ service.chongwujiyangNewMoney }}/天</span>
                  </div>
                </div>
              </div>
            </router-link>
            <div class="service-actions">
              <button class="btn btn-primary" @click="bookService(service.id)">立即预约</button>
            </div>
          </div>
        </div>

        <!-- 加载中 -->
        <div v-else class="loading">
          <div class="loading-spinner"></div>
          <p>加载中...</p>
        </div>

        <!-- 无数据 -->
        <div v-if="!loading && services.length === 0" class="no-data">
          <p>暂无寄养服务</p>
        </div>

        <!-- 分页 -->
        <div v-if="!loading && services.length > 0" class="pagination">
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
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { chongwujiyangApi, dictionaryApi } from '../../utils/api'
import message from '../../utils/message'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'

const router = useRouter()
const services = ref([])
const loading = ref(false)
const currentPage = ref(1)
const totalPages = ref(1)
const serviceTypes = ref([])
const filter = ref({
  category: '',
  name: ''
})

// 加载服务类型字典
const loadServiceTypes = async () => {
  try {
    const response = await dictionaryApi.getByDicCode('chongwujiyang_types')
    if (response.code === 0) {
      serviceTypes.value = response.data.list || []
    }
  } catch (error) {
    console.error('获取服务类型失败:', error)
  }
}

// 获取服务类型名称
const getServiceTypeName = (typeCode) => {
  const type = serviceTypes.value.find(t => t.codeIndex === typeCode)
  return type ? type.indexName : '未知类型'
}

// 加载服务列表
const loadServices = async () => {
  try {
    loading.value = true
    // 构建与后端接口匹配的筛选参数
    const params = {
      page: currentPage.value,
      limit: 12,
      chongwujiyangTypes: filter.value.category,
      chongwujiyangName: filter.value.name,
      t: Date.now()
    }
    const response = await chongwujiyangApi.getList(params)
    if (response.code === 0) {
      services.value = response.data.list || []
      totalPages.value = Math.ceil(response.data.total / 12)
    } else {
      message.error('获取服务列表失败')
    }
  } catch (error) {
    console.error('获取服务列表失败:', error)
    message.error('获取服务列表失败')
  } finally {
    loading.value = false
  }
}

// 预约服务
const bookService = (id) => {
  router.push(`/chongwujiyang/booking/${id}`)
}

// 上一页
const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
    loadServices()
  }
}

// 下一页
const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
    loadServices()
  }
}

// 页面加载时获取服务类型和服务列表
onMounted(() => {
  loadServiceTypes()
  loadServices()
})
</script>

<style scoped>
.chongwujiyang-page {
  min-height: 100vh;
  background-color: var(--bg);
}

.chongwujiyang-content {
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

.services-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, 280px);
  gap: var(--spacing-xl);
  margin-bottom: var(--spacing-2xl);
  justify-content: center;
}

.service-card-link {
  display: block;
  text-decoration: none;
  color: inherit;
}

.service-card {
  background: var(--card);
  border-radius: var(--radius-base);
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
  position: relative;
}

.service-card-link:hover .service-card {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.service-actions {
  margin-top: var(--spacing-sm);
  display: flex;
  justify-content: center;
}

.service-image {
  position: relative;
  padding-top: 100%; /* 1:1 比例 */
  overflow: hidden;
}

.service-image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.service-card:hover .service-image img {
  transform: scale(1.05);
}

.service-info {
  padding: var(--spacing-base);
}

.service-title {
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

.service-type {
  font-size: var(--fs-xs);
  color: var(--primary);
  margin-bottom: var(--spacing-xs);
  font-weight: 500;
}

.service-desc {
  font-size: var(--fs-xs);
  color: var(--text-3);
  margin-bottom: var(--spacing-base);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.service-meta {
  display: flex;
  justify-content: space-between;
  font-size: var(--fs-xs);
  color: var(--text-2);
  margin-bottom: var(--spacing-base);
}

.service-click {
  color: var(--text-3);
}

.service-price-row {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  margin-bottom: var(--spacing-base);
}

.service-price {
  color: var(--danger);
  font-size: var(--fs-lg);
  font-weight: 600;
}

.service-actions {
  display: flex;
  gap: var(--spacing-sm);
}

.service-actions .btn {
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
  
  .services-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: var(--spacing-base);
  }
  
  .service-actions {
    flex-direction: column;
  }
}
</style>