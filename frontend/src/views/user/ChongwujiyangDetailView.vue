<template>
  <div class="chongwujiyang-detail-page">
    <Header />
    <main class="chongwujiyang-detail-content">
      <div class="container">
        <!-- 加载中 -->
        <div v-if="loading" class="loading">
          <div class="loading-spinner"></div>
          <p>加载中...</p>
        </div>

        <!-- 服务详情 -->
        <div v-else-if="service" class="service-detail">
          <div class="service-header">
            <h1>{{ service.chongwujiyangName }}</h1>
            <div class="service-meta">
              <span class="service-category">{{ getServiceTypeName(service.chongwujiyangTypes) }}</span>
              <span class="service-click">热度：{{ service.chongwujiyangClicknum }}</span>
            </div>
          </div>

          <div class="service-body">
            <!-- 服务图片 -->
            <div class="service-images">
              <div class="main-image">
                <img :src="`http://localhost:8080/wangshangchongwudian/` + service.chongwujiyangPhoto" :alt="service.chongwujiyangName">
              </div>
            </div>

            <!-- 服务信息 -->
            <div class="service-info">
              <div class="price-section">
                <span class="price-label">价格：</span>
                <span class="service-price">¥{{ service.chongwujiyangNewMoney }}/天</span>
              </div>

              <div class="actions-section">
                <button class="btn btn-primary" @click="bookService">立即预约</button>
                <button class="btn btn-secondary" @click="backToList">返回列表</button>
              </div>

              <div class="service-description">
                <h3>服务介绍</h3>
                <div v-html="service.chongwujiyangContent"></div>
              </div>

              <div class="service-specs">
                <h3>服务信息</h3>
                <ul>
                  <li>
                    <span class="spec-label">服务类型：</span>
                    <span class="spec-value">{{ getServiceTypeName(service.chongwujiyangTypes) }}</span>
                  </li>
                  <li>
                    <span class="spec-label">价格/天：</span>
                    <span class="spec-value">¥{{ service.chongwujiyangNewMoney }}</span>
                  </li>
                  <li>
                    <span class="spec-label">热度：</span>
                    <span class="spec-value">{{ service.chongwujiyangClicknum }}</span>
                  </li>
                  <li>
                    <span class="spec-label">发布时间：</span>
                    <span class="spec-value">{{ service.insertTime }}</span>
                  </li>
                </ul>
              </div>
            </div>
          </div>

          <!-- 相关服务推荐 -->
          <div class="related-services">
            <h3>相关服务推荐</h3>
            <div v-if="relatedServices.length > 0" class="services-grid">
              <div v-for="relatedService in relatedServices" :key="relatedService.id">
                <router-link :to="`/chongwujiyang/detail/${relatedService.id}`" class="service-card-link">
                  <div class="service-card">
                    <div class="service-image">
                      <img :src="`http://localhost:8080/wangshangchongwudian/` + relatedService.chongwujiyangPhoto" :alt="relatedService.chongwujiyangName">
                    </div>
                    <div class="service-info">
                      <h4 class="service-title">{{ relatedService.chongwujiyangName }}</h4>
                      <p class="service-type">{{ getServiceTypeName(relatedService.chongwujiyangTypes) }}</p>
                      <span class="service-price">¥{{ relatedService.chongwujiyangNewMoney }}/天</span>
                    </div>
                  </div>
                </router-link>
              </div>
            </div>
            <div v-else class="no-related-services">
              <p>暂无相关服务</p>
            </div>
          </div>
        </div>

        <!-- 无数据 -->
        <div v-else class="no-data">
          <p>服务不存在</p>
        </div>
      </div>
    </main>
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { chongwujiyangApi, dictionaryApi } from '../../utils/api'
import message from '../../utils/message'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'

const router = useRouter()
const route = useRoute()
const service = ref(null)
const loading = ref(false)
const relatedServices = ref([])
const serviceTypes = ref([])

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

// 获取服务详情
const getServiceDetail = async () => {
  try {
    loading.value = true
    const serviceId = route.params.id
    const response = await chongwujiyangApi.getDetail(serviceId)
    if (response.code === 0) {
      service.value = response.data
      // 加载相关服务
      loadRelatedServices(serviceId, service.value.chongwujiyangTypes)
    } else {
      message.error('获取服务详情失败')
    }
  } catch (error) {
    console.error('获取服务详情失败:', error)
    message.error('获取服务详情失败')
  } finally {
    loading.value = false
  }
}

// 加载相关服务
const loadRelatedServices = async (currentId, serviceType) => {
  try {
    const response = await chongwujiyangApi.getList({ 
      chongwujiyangTypes: serviceType,
      limit: 4,
      t: Date.now()
    })
    if (response.code === 0) {
      // 过滤掉当前服务
      relatedServices.value = (response.data.list || []).filter(item => item.id !== parseInt(currentId))
    }
  } catch (error) {
    console.error('获取相关服务失败:', error)
  }
}

// 预约服务
const bookService = () => {
  if (service.value) {
    router.push(`/chongwujiyang/booking/${service.value.id}`)
  }
}

// 返回列表
const backToList = () => {
  router.push('/chongwujiyang')
}

// 页面加载时获取服务类型和服务详情
onMounted(() => {
  loadServiceTypes()
  getServiceDetail()
})
</script>

<style scoped>
.chongwujiyang-detail-page {
  min-height: 100vh;
  background-color: var(--bg);
}

.chongwujiyang-detail-content {
  padding: var(--spacing-2xl) 0;
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

.service-detail {
  background: var(--card);
  border-radius: var(--radius-base);
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  margin-bottom: var(--spacing-2xl);
}

.service-header {
  padding: var(--spacing-xl);
  border-bottom: 1px solid var(--border);
}

.service-header h1 {
  color: var(--text-1);
  font-size: var(--fs-xl);
  font-weight: 600;
  margin-bottom: var(--spacing-base);
}

.service-meta {
  display: flex;
  gap: var(--spacing-lg);
  font-size: var(--fs-sm);
  color: var(--text-2);
  align-items: center;
}

.service-body {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--spacing-xl);
  padding: var(--spacing-xl);
}

.service-images {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-base);
}

.main-image {
  position: relative;
  padding-top: 100%; /* 1:1 比例 */
  overflow: hidden;
  border-radius: var(--radius-base);
}

.main-image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.service-info {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

.price-section {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.price-label {
  font-weight: 500;
  color: var(--text-1);
  font-size: var(--fs-base);
}

.service-price {
  color: var(--danger);
  font-size: var(--fs-2xl);
  font-weight: 600;
}

.actions-section {
  display: flex;
  gap: var(--spacing-base);
  margin-top: var(--spacing-lg);
}

.actions-section .btn {
  flex: 1;
  font-size: var(--fs-base);
  padding: var(--spacing-base) var(--spacing-lg);
}

.service-description {
  margin-top: var(--spacing-lg);
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--border);
}

.service-description h3 {
  color: var(--text-1);
  font-size: var(--fs-lg);
  font-weight: 600;
  margin-bottom: var(--spacing-base);
}

.service-description div {
  color: var(--text-2);
  font-size: var(--fs-base);
  line-height: 1.6;
}

.service-specs {
  margin-top: var(--spacing-lg);
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--border);
}

.service-specs h3 {
  color: var(--text-1);
  font-size: var(--fs-lg);
  font-weight: 600;
  margin-bottom: var(--spacing-base);
}

.service-specs ul {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}

.service-specs li {
  display: flex;
  gap: var(--spacing-sm);
}

.spec-label {
  font-weight: 500;
  color: var(--text-1);
  font-size: var(--fs-sm);
  min-width: 80px;
}

.spec-value {
  color: var(--text-2);
  font-size: var(--fs-sm);
}

.related-services {
  background: var(--card);
  border-radius: var(--radius-base);
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  padding: var(--spacing-xl);
}

.related-services h3 {
  color: var(--text-1);
  font-size: var(--fs-lg);
  font-weight: 600;
  margin-bottom: var(--spacing-lg);
}

.services-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, 200px);
  gap: var(--spacing-lg);
  justify-content: center;
}

.service-card-link {
  display: block;
  text-decoration: none;
  color: inherit;
}

.service-card {
  background: white;
  border-radius: var(--radius-base);
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
}

.service-card-link:hover .service-card {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.service-card .service-image {
  position: relative;
  padding-top: 100%; /* 1:1 比例 */
  overflow: hidden;
}

.service-card .service-image img {
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

.service-card .service-info {
  padding: var(--spacing-base);
}

.service-card .service-title {
  font-size: var(--fs-sm);
  font-weight: 500;
  color: var(--text-1);
  margin-bottom: var(--spacing-xs);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.service-card .service-type {
  font-size: var(--fs-xs);
  color: var(--primary);
  margin-bottom: var(--spacing-xs);
  font-weight: 500;
}

.service-card .service-price {
  color: var(--danger);
  font-size: var(--fs-base);
  font-weight: 600;
}

.no-related-services {
  text-align: center;
  padding: var(--spacing-xl) 0;
  color: var(--text-3);
  font-size: var(--fs-base);
}

@media (max-width: 768px) {
  .service-body {
    grid-template-columns: 1fr;
  }
  
  .actions-section {
    flex-direction: column;
  }
  
  .service-header {
    padding: var(--spacing-lg);
  }
  
  .service-body {
    padding: var(--spacing-lg);
  }
  
  .related-services {
    padding: var(--spacing-lg);
  }
  
  .services-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: var(--spacing-base);
  }
}
</style>