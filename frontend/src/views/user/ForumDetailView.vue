<template>
  <div class="forum-detail-container">
    <Header />
    <main class="main-content">
      <div class="container">
        <!-- 页面导航 -->
        <div class="breadcrumb">
          <router-link to="/">首页</router-link>
          <span>/</span>
          <router-link to="/forum">论坛</router-link>
          <span>/</span>
          <span class="current">{{ forumDetail?.forumName || '帖子详情' }}</span>
        </div>
        
        <!-- 帖子详情 -->
        <div class="forum-detail" v-if="forumDetail">
          <div class="forum-card">
            <div class="forum-header">
              <div class="user-info">
                <img :src="forumDetail.yonghuPhoto ? 'http://localhost:8080/wangshangchongwudian/' + forumDetail.yonghuPhoto : 'https://via.placeholder.com/60x60'" alt="用户头像" class="avatar">
                <div class="user-details">
                  <h3 class="username">{{ forumDetail.yonghuName || forumDetail.uusername }}</h3>
                  <div class="user-meta">
                    <span class="time">{{ forumDetail.createTime }}</span>
                    <span class="separator">|</span>
                    <span class="status">{{ forumDetail.forumStateValue }}</span>
                  </div>
                </div>
                <div class="post-actions">
                  <button class="btn btn-outline">
                    <i class="icon">❤️</i> 点赞
                  </button>
                  <button class="btn btn-outline">
                    <i class="icon">🔗</i> 分享
                  </button>
                </div>
              </div>
              <h2 class="forum-title">{{ forumDetail.forumName }}</h2>
              <div class="forum-meta">
                <span class="meta-item">
                  <i class="icon">👁️</i> {{ viewCount }}
                </span>
                <span class="meta-item">
                  <i class="icon">💬</i> {{ replyList.length }}
                </span>
                <span class="meta-item">
                  <i class="icon">❤️</i> {{ likeCount }}
                </span>
              </div>
            </div>
            <div class="forum-content">
              <p>{{ forumDetail.forumContent }}</p>
            </div>
          </div>
        </div>
        
        <!-- 回复统计 -->
        <div class="reply-stats">
          <h3 class="section-title">回复 ({{ replyList.length }})</h3>
          <div class="stats-info">
            <span>共有 {{ replyList.length }} 条回复</span>
          </div>
        </div>
        
        <!-- 回复列表 -->
        <div class="replies-section">
          <div class="reply-list">
            <div v-for="reply in replyList" :key="reply.id" class="reply-item">
              <div class="reply-card">
                <div class="reply-header">
                  <div class="user-info">
                    <img :src="reply.yonghuPhoto ? 'http://localhost:8080/wangshangchongwudian/' + reply.yonghuPhoto : 'https://via.placeholder.com/50x50'" alt="用户头像" class="avatar">
                    <div class="user-details">
                      <h4 class="username">{{ reply.yonghuName || reply.uusername }}</h4>
                      <span class="time">{{ reply.createTime }}</span>
                    </div>
                    <div class="reply-actions">
                      <button class="btn btn-sm btn-outline">
                        <i class="icon">❤️</i>
                      </button>
                      <button class="btn btn-sm btn-outline">
                        <i class="icon">💬</i> 回复
                      </button>
                    </div>
                  </div>
                </div>
                <div class="reply-content">
                  <p>{{ reply.forumContent }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 回复表单 -->
        <div class="reply-form-section">
          <div class="reply-form-card">
            <h3 class="section-title">发表回复</h3>
            <form @submit.prevent="submitReply">
              <textarea 
                v-model="replyContent" 
                placeholder="请输入您的回复..."
                rows="4"
                class="form-control"
                required
              ></textarea>
              <div class="form-actions">
                <div class="form-tips">
                  <span>支持 Markdown 格式</span>
                </div>
                <button type="submit" class="btn btn-primary">提交回复</button>
              </div>
            </form>
          </div>
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
import message from '../../utils/message'

export default {
  name: 'ForumDetailView',
  components: {
    Header,
    Footer
  },
  data() {
    return {
      forumDetail: null,
      replyList: [],
      replyContent: '',
      viewCount: 123,
      likeCount: 45
    }
  },
  mounted() {
    this.loadForumDetail()
    this.loadReplies()
  },
  methods: {
    async loadForumDetail() {
      try {
        const { id } = this.$route.params
        const response = await forumApi.getInfo(id)
        
        if (response.code === 0) {
          this.forumDetail = response.data
        }
      } catch (error) {
        console.error('加载帖子详情失败:', error)
      }
    },
    async loadReplies() {
      try {
        const { id } = this.$route.params
        const response = await forumApi.getList({
          superIds: id,
          forumStateTypes: 2,
          t: Date.now()
        })
        
        if (response.code === 0) {
          this.replyList = response.data.list
        }
      } catch (error) {
        console.error('加载回复列表失败:', error)
      }
    },
    async submitReply() {
      try {
        const { id } = this.$route.params
        const response = await forumApi.save({
          forumStateTypes: 2,
          forumContent: this.replyContent,
          forumPhoto: '',
          superIds: id,
          t: Date.now()
        })
        
        if (response.code === 0) {
          // 清空回复内容
          this.replyContent = ''
          // 重新加载回复列表
          this.loadReplies()
          message.success('回复成功！')
        }
      } catch (error) {
        console.error('提交回复失败:', error)
        message.error('回复失败，请重试')
      }
    }
  }
}
</script>

<style scoped>
.forum-detail-container {
  padding: 20px 0;
  min-height: 80vh;
  background-color: #f7f9fc;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 面包屑导航 */
.breadcrumb {
  margin-bottom: 20px;
  font-size: 14px;
  color: #666;
}

.breadcrumb a {
  color: #4CAF50;
  text-decoration: none;
  transition: color 0.3s ease;
}

.breadcrumb a:hover {
  color: #45a049;
}

.breadcrumb span {
  margin: 0 8px;
}

.breadcrumb .current {
  color: #333;
  font-weight: 500;
}

/* 帖子详情卡片 */
.forum-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 30px;
  margin-bottom: 30px;
}

.forum-header {
  margin-bottom: 24px;
}

.user-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.user-info .user-details {
  flex: 1;
  margin-left: 16px;
}

.avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #f0f0f0;
}

.username {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: #333;
}

.user-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
  color: #999;
}

.separator {
  color: #ddd;
}

.status {
  color: #4CAF50;
  font-weight: 500;
}

.post-actions {
  display: flex;
  gap: 10px;
}

.forum-title {
  font-size: 24px;
  font-weight: 600;
  margin: 0 0 16px 0;
  color: #333;
  line-height: 1.3;
}

.forum-meta {
  display: flex;
  gap: 20px;
  padding: 12px 0;
  border-top: 1px solid #f0f0f0;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 20px;
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

.forum-content {
  font-size: 16px;
  line-height: 1.8;
  color: #666;
  padding: 20px 0;
}

.forum-content p {
  margin: 0 0 16px 0;
}

/* 回复统计 */
.reply-stats {
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stats-info {
  font-size: 14px;
  color: #999;
}

/* 回复列表 */
.replies-section {
  margin-bottom: 30px;
}

.reply-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 24px;
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.reply-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.reply-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.reply-header .user-info {
  flex: 1;
  margin-bottom: 0;
}

.reply-actions {
  display: flex;
  gap: 8px;
}

.reply-content {
  font-size: 15px;
  line-height: 1.6;
  color: #666;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

/* 回复表单 */
.reply-form-section {
  margin-top: 40px;
}

.reply-form-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 30px;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
}

.form-tips {
  font-size: 14px;
  color: #999;
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

.btn-sm {
  padding: 6px 12px;
  font-size: 12px;
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
  padding: 12px 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 15px;
  resize: vertical;
  font-family: inherit;
  transition: border-color 0.3s ease;
}

.form-control:focus {
  outline: none;
  border-color: #4CAF50;
  box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.1);
}

/* 标题样式 */
.section-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 20px 0;
  color: #333;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .user-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .user-info .user-details {
    margin-left: 0;
  }
  
  .post-actions {
    width: 100%;
    justify-content: flex-end;
  }
  
  .forum-meta {
    flex-wrap: wrap;
    gap: 12px;
  }
  
  .reply-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .reply-actions {
    width: 100%;
    justify-content: flex-end;
  }
  
  .form-actions {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }
  
  .form-tips {
    text-align: center;
  }
  
  .btn-primary {
    width: 100%;
  }
}
</style>