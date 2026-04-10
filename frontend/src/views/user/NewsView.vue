<template>
  <div class="news-page">
    <Header />
    <main class="news-content">
      <div class="container">
        <!-- 页面标题 -->
        <div class="page-header">
          <h1>宠物资讯</h1>
          <p>了解最新的宠物相关资讯和公告</p>
        </div>

        <!-- 加载中 -->
        <div v-if="loading" class="loading">
          <div class="loading-spinner"></div>
          <p>加载中...</p>
        </div>

        <!-- 新闻列表 -->
        <div v-else-if="news.length > 0" class="news-grid">
          <div v-for="item in news" :key="item.id">
            <router-link :to="`/news/detail/${item.id}`" class="news-card-link">
              <div class="news-card">
                <div class="news-image">
                  <img :src="item.newsPhoto ? `http://localhost:8080/wangshangchongwudian/${item.newsPhoto}` : 'https://via.placeholder.com/400x200'" :alt="item.newsName">
                </div>
                <div class="news-content">
                  <div class="news-meta">
                    <span class="news-type">{{ item.newsValue }}</span>
                    <span class="news-time">{{ item.insertTime }}</span>
                  </div>
                  <h3 class="news-title">{{ item.newsName }}</h3>
                  <p class="news-desc">{{ item.newsContent || '这里是资讯内容摘要，为您提供专业的宠物护理建议和知识...' }}</p>
                </div>
              </div>
            </router-link>
          </div>
        </div>

        <!-- 无数据 -->
        <div v-else class="no-data">
          <p>暂无资讯</p>
        </div>

        <!-- 分页 -->
        <div v-if="!loading && news.length > 0" class="pagination">
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
import { newsApi } from '../../utils/api'
import message from '../../utils/message'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'

const news = ref([])
const loading = ref(false)
const currentPage = ref(1)
const totalPages = ref(1)

// 加载新闻列表
const loadNews = async () => {
  try {
    loading.value = true
    const response = await newsApi.getList({ 
      page: currentPage.value, 
      limit: 9,
      t: Date.now()
    })
    if (response.code === 0) {
      news.value = response.data.list || []
      totalPages.value = Math.ceil(response.data.total / 9)
    } else {
      message.error('获取新闻列表失败')
    }
  } catch (error) {
    console.error('获取新闻列表失败:', error)
    message.error('获取新闻列表失败')
  } finally {
    loading.value = false
  }
}

// 上一页
const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
    loadNews()
  }
}

// 下一页
const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
    loadNews()
  }
}

// 页面加载时获取新闻列表
onMounted(() => {
  loadNews()
})
</script>

<style scoped>
.news-page {
  min-height: 100vh;
  background-color: var(--bg);
}

.news-content {
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

.no-data {
  text-align: center;
  padding: var(--spacing-3xl) 0;
  color: var(--text-3);
  font-size: var(--fs-base);
}

.news-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: var(--spacing-xl);
  margin-bottom: var(--spacing-2xl);
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
}

.news-card-link:hover .news-card {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.news-image {
  height: 200px;
  overflow: hidden;
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

.news-content {
  padding: var(--spacing-base);
}

.news-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-sm);
}

.news-type {
  padding: var(--spacing-xs) var(--spacing-sm);
  border-radius: var(--radius-sm);
  background-color: rgba(66, 184, 131, 0.1);
  color: var(--primary);
  font-size: var(--fs-xs);
  font-weight: 500;
}

.news-time {
  font-size: var(--fs-xs);
  color: var(--text-3);
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

.read-more {
  color: var(--primary);
  text-decoration: none;
  font-size: var(--fs-xs);
  font-weight: 500;
  transition: color 0.3s ease;
}

.read-more:hover {
  color: var(--primary-dark);
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
  .news-grid {
    grid-template-columns: 1fr;
    gap: var(--spacing-xl);
  }
  
  .news-image {
    height: 160px;
  }
  
  .page-header h1 {
    font-size: var(--fs-xl);
  }
}
</style>