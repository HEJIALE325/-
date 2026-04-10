<template>
  <div class="forum-container">
    <Header />
    <main class="main-content">
      <div class="container">
        <!-- 页面头部 -->
        <div class="page-header">
          <h2 class="page-title">论坛</h2>
          <div class="page-actions">
            <button class="btn btn-primary">
              <i class="icon">+</i> 发布帖子
            </button>
          </div>
        </div>
        
        <!-- 筛选栏 -->
        <div class="filter-section">
          <div class="filter-card">
            <div class="filter-row">
              <div class="filter-item">
                <label>帖子状态</label>
                <select class="form-control">
                  <option value="1">全部</option>
                  <option value="2">精华</option>
                  <option value="3">热门</option>
                </select>
              </div>
              <div class="filter-item">
                <label>排序方式</label>
                <select class="form-control">
                  <option value="1">最新发布</option>
                  <option value="2">回复最多</option>
                  <option value="3">热度最高</option>
                </select>
              </div>
              <div class="filter-item">
                <label>搜索</label>
                <div class="search-box">
                  <input type="text" class="form-control" placeholder="搜索帖子标题或内容">
                  <button class="search-btn">🔍</button>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 帖子统计 -->
        <div class="stats-section">
          <div class="stats-card">
            <div class="stats-item">
              <div class="stats-value">{{ total }}</div>
              <div class="stats-label">帖子总数</div>
            </div>
            <div class="stats-item">
              <div class="stats-value">{{ activePosts }}</div>
              <div class="stats-label">今日新帖</div>
            </div>
            <div class="stats-item">
              <div class="stats-value">{{ hotPosts }}</div>
              <div class="stats-label">热门帖子</div>
            </div>
          </div>
        </div>
        
        <!-- 帖子列表 -->
        <div class="forum-list">
          <div v-for="item in forumList" :key="item.id">
            <router-link :to="`/forum/detail/${item.id}`" class="forum-item-link">
              <div class="forum-item">
                <div class="forum-item-header">
                  <div class="user-info">
                    <img :src="item.yonghuPhoto ? 'http://localhost:8080/wangshangchongwudian/' + item.yonghuPhoto : 'https://via.placeholder.com/50x50'" alt="用户头像" class="avatar">
                    <div class="user-details">
                      <h3 class="username">{{ item.yonghuName || item.uusername }}</h3>
                      <span class="time">{{ item.createTime }}</span>
                    </div>
                    <div class="post-status">
                      <span class="status-badge hot">热门</span>
                    </div>
                  </div>
                </div>
                <div class="forum-item-content">
                  <h4 class="forum-title">{{ item.forumName }}</h4>
                  <p class="forum-text">{{ item.forumContent }}</p>
                </div>
                <div class="forum-item-footer">
                  <div class="post-meta">
                    <span class="meta-item">
                      <i class="icon">💬</i> {{ item.replyCount || 0 }}
                    </span>
                    <span class="meta-item">
                      <i class="icon">👁️</i> {{ item.viewCount || 0 }}
                    </span>
                    <span class="meta-item">
                      <i class="icon">❤️</i> {{ item.likeCount || 0 }}
                    </span>
                  </div>
                </div>
              </div>
            </router-link>
          </div>
        </div>
        
        <!-- 分页 -->
        <div class="pagination" v-if="totalPage > 1">
          <button 
            class="btn btn-outline" 
            :disabled="currPage === 1" 
            @click="changePage(currPage - 1)"
          >
            上一页
          </button>
          <span class="page-info">第 {{ currPage }} 页，共 {{ totalPage }} 页</span>
          <button 
            class="btn btn-outline" 
            :disabled="currPage === totalPage" 
            @click="changePage(currPage + 1)"
          >
            下一页
          </button>
        </div>
      </div>
    </main>
    <Footer />
  </div>
</template>

<script>
import { forumApi } from '../../utils/api'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'

export default {
  name: 'ForumView',
  components: {
    Header,
    Footer
  },
  data() {
    return {
      forumList: [],
      total: 0,
      pageSize: 10,
      totalPage: 1,
      currPage: 1,
      activePosts: 5,
      hotPosts: 3
    }
  },
  mounted() {
    this.loadForumList()
  },
  methods: {
    async loadForumList() {
      try {
        const response = await forumApi.getList({
          forumStateTypes: 1,
          page: this.currPage,
          limit: this.pageSize,
          t: Date.now()
        })
        
        if (response.code === 0) {
          this.forumList = response.data.list
          this.total = response.data.total
          this.pageSize = response.data.pageSize
          this.totalPage = response.data.totalPage
          this.currPage = response.data.currPage
        }
      } catch (error) {
        console.error('加载论坛列表失败:', error)
      }
    },
    changePage(page) {
      if (page >= 1 && page <= this.totalPage) {
        this.currPage = page
        this.loadForumList()
      }
    }
  }
}
</script>

<style scoped>
.forum-container {
  padding: 0;
  min-height: 80vh;
  background-color: #f7f9fc;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 页面头部 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.page-actions {
  display: flex;
  gap: 10px;
}

/* 筛选栏 */
.filter-section {
  margin-bottom: 30px;
}

.filter-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 20px;
}

.filter-row {
  display: flex;
  gap: 20px;
  align-items: end;
}

.filter-item {
  flex: 1;
  min-width: 200px;
}

.filter-item label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #666;
  margin-bottom: 8px;
}

.search-box {
  position: relative;
}

.search-btn {
  position: absolute;
  right: 4px;
  top: 50%;
  transform: translateY(-50%);
  background: #4CAF50;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 8px 12px;
  cursor: pointer;
}

/* 统计卡片 */
.stats-section {
  margin-bottom: 30px;
}

.stats-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 20px;
  display: flex;
  gap: 40px;
}

.stats-item {
  flex: 1;
  text-align: center;
}

.stats-value {
  font-size: 24px;
  font-weight: 600;
  color: #4CAF50;
  margin-bottom: 8px;
}

.stats-label {
  font-size: 14px;
  color: #666;
}

/* 帖子列表 */
.forum-list {
  margin-bottom: 30px;
}

.forum-item-link {
  display: block;
  text-decoration: none;
  color: inherit;
  margin-bottom: 20px;
}

.forum-item {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 24px;
  transition: all 0.3s ease;
}

.forum-item-link:hover .forum-item {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.forum-item-header {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.user-info {
  display: flex;
  align-items: center;
  flex: 1;
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 16px;
  object-fit: cover;
  border: 2px solid #f0f0f0;
}

.user-details {
  display: flex;
  flex-direction: column;
}

.username {
  font-size: 16px;
  font-weight: 600;
  margin: 0;
  color: #333;
}

.time {
  font-size: 14px;
  color: #999;
  margin-top: 4px;
}

.post-status {
  display: flex;
  gap: 8px;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.hot {
  background-color: #fff3cd;
  color: #856404;
}

.status-badge.essence {
  background-color: #d1ecf1;
  color: #0c5460;
}

/* 帖子内容 */
.forum-item-content {
  margin-bottom: 20px;
}

.forum-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 12px 0;
  color: #333;
  transition: color 0.3s ease;
}

.forum-item:hover .forum-title {
  color: #4CAF50;
}

.forum-text {
  font-size: 16px;
  line-height: 1.6;
  color: #666;
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

/* 帖子底部 */
.forum-item-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.post-meta {
  display: flex;
  gap: 20px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #999;
}

.meta-item .icon {
  font-size: 16px;
}

/* 按钮样式 */
.btn {
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
}

.btn-primary {
  background-color: #4CAF50;
  color: white;
  box-shadow: 0 2px 4px rgba(76, 175, 80, 0.2);
}

.btn-primary:hover {
  background-color: #45a049;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(76, 175, 80, 0.3);
}

.btn-outline {
  background-color: transparent;
  color: #4CAF50;
  border: 1px solid #4CAF50;
}

.btn-outline:hover {
  background-color: #f0f9ff;
  transform: translateY(-2px);
}

/* 表单控件 */
.form-control {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  transition: border-color 0.3s ease;
}

.form-control:focus {
  outline: none;
  border-color: #4CAF50;
  box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.1);
}

/* 分页 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 40px;
  padding: 20px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.page-info {
  margin: 0 20px;
  font-size: 14px;
  color: #666;
  font-weight: 500;
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .filter-row {
    flex-direction: column;
    align-items: stretch;
  }
  
  .filter-item {
    min-width: auto;
  }
  
  .stats-card {
    flex-direction: column;
    gap: 20px;
  }
  
  .stats-item {
    text-align: left;
    padding: 10px 0;
    border-bottom: 1px solid #f0f0f0;
  }
  
  .stats-item:last-child {
    border-bottom: none;
  }
  
  .forum-item-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .post-meta {
    width: 100%;
    justify-content: space-between;
  }
}
</style>