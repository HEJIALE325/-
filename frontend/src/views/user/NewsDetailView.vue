<template>
  <div class="news-detail-page">
    <Header />
    <main class="news-detail-content">
      <div class="container">
        <!-- 加载中 -->
        <div v-if="loading" class="loading">
          <div class="loading-spinner"></div>
          <p>加载中...</p>
        </div>

        <!-- 公告详情 -->
        <div v-else-if="news" class="news-detail">
          <div class="news-header">
            <h1>{{ news.newsName }}</h1>
            <div class="news-meta">
              <span class="news-type">{{ news.newsValue }}</span>
              <span class="news-time">{{ news.insertTime }}</span>
            </div>
          </div>

          <div class="news-body">
            <!-- 公告图片 -->
            <div v-if="news.newsPhoto" class="news-image">
              <img :src="`http://localhost:8080/wangshangchongwudian/${news.newsPhoto}`" :alt="news.newsName">
            </div>

            <!-- 公告内容 -->
            <div class="news-content" v-html="news.newsContent"></div>
          </div>
        </div>

        <!-- 无数据 -->
        <div v-else class="no-data">
          <p>公告不存在</p>
        </div>

        <!-- 返回按钮 -->
        <div class="back-section">
          <button class="btn btn-secondary" @click="goBack">返回列表</button>
        </div>
      </div>
    </main>
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { newsApi } from '../../utils/api'
import message from '../../utils/message'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'

const router = useRouter()
const route = useRoute()
const news = ref(null)
const loading = ref(false)

// 获取公告详情
const getNewsDetail = async () => {
  try {
    loading.value = true
    const newsId = route.params.id
    const response = await newsApi.getDetail(newsId)
    if (response.code === 0) {
      news.value = response.data
    } else {
      message.error('获取公告详情失败')
    }
  } catch (error) {
    console.error('获取公告详情失败:', error)
    message.error('获取公告详情失败')
  } finally {
    loading.value = false
  }
}

// 返回列表
const goBack = () => {
  router.push('/news')
}

// 页面加载时获取公告详情
onMounted(() => {
  getNewsDetail()
})
</script>

<style scoped>
.news-detail-page {
  min-height: 100vh;
  background-color: var(--bg);
}

.news-detail-content {
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

.news-detail {
  background: var(--card);
  border-radius: var(--radius-base);
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  margin-bottom: var(--spacing-2xl);
}

.news-header {
  padding: var(--spacing-xl);
  border-bottom: 1px solid var(--border);
}

.news-header h1 {
  color: var(--text-1);
  font-size: var(--fs-xl);
  font-weight: 600;
  margin-bottom: var(--spacing-base);
}

.news-meta {
  display: flex;
  gap: var(--spacing-lg);
  font-size: var(--fs-sm);
  color: var(--text-2);
  align-items: center;
}

.news-type {
  padding: var(--spacing-xs) var(--spacing-sm);
  border-radius: var(--radius-sm);
  background-color: rgba(66, 184, 131, 0.1);
  color: var(--primary);
  font-weight: 500;
}

.news-body {
  padding: var(--spacing-xl);
}

.news-image {
  margin-bottom: var(--spacing-xl);
  border-radius: var(--radius-base);
  overflow: hidden;
}

.news-image img {
  /* 最大宽度，不会撑满屏幕 */
  max-width: 600px;
  /* 高度自动等比缩小 */
  height: auto;
  /* 图片居中显示 */
  display: block;
  margin: 0 auto;
  /* 可选：圆角更美观 */
  border-radius: 8px;
}

.news-content {
  color: var(--text-2);
  font-size: var(--fs-base);
  line-height: 1.6;
}

.news-content p {
  margin-bottom: var(--spacing-base);
}

.back-section {
  text-align: center;
  margin-top: var(--spacing-2xl);
}

.back-section .btn {
  font-size: var(--fs-base);
  padding: var(--spacing-base) var(--spacing-xl);
}

@media (max-width: 768px) {
  .news-header {
    padding: var(--spacing-lg);
  }
  
  .news-body {
    padding: var(--spacing-lg);
  }
  
  .news-header h1 {
    font-size: var(--fs-lg);
  }
}
</style>