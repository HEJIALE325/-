<template>
  <div class="comments-page">
    <Header />
    <main class="comments-content">
      <div class="container">
        <div class="comments-header">
          <h1>我的评价</h1>
          <p>查看和管理您的所有评价</p>
        </div>
        
        <!-- 搜索表单
        <div class="search-section">
          <form @submit.prevent="handleSearch">
            <div class="form-row">
              <div class="form-group">
                <label for="search-commentText">评价内容</label>
                <input 
                  type="text" 
                  id="search-commentText" 
                  v-model="searchParams.chongwuyongpinCommentbackText"
                  placeholder="请输入评价内容进行搜索"
                >
              </div>
              <div class="form-group">
                <label for="search-replyText">回复内容</label>
                <input 
                  type="text" 
                  id="search-replyText" 
                  v-model="searchParams.replyText"
                  placeholder="请输入回复内容进行搜索"
                >
              </div>
              <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                  搜索
                </button>
                <button type="button" class="btn btn-secondary" @click="handleReset">
                  重置
                </button>
              </div>
            </div>
          </form>
        </div> -->
        
        <div class="table-container">
          <table class="admin-table">
            <thead>
              <tr>
                <th>商品名称</th>
                <th>评价内容</th>
                <th>回复内容</th>
                <th>评价时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="comment in comments" :key="comment.id">
                <td>
                  <div class="product-info">
                    <img :src="'http://localhost:8080/wangshangchongwudian/' + comment.chongwuyongpinPhoto" :alt="comment.chongwuyongpinName" class="product-image">
                    <div class="product-details">
                      <span class="product-name">{{ comment.chongwuyongpinName }}</span>
                      <span class="product-price">¥{{ comment.chongwuyongpinNewMoney }}</span>
                    </div>
                  </div>
                </td>
                <td>{{ comment.chongwuyongpinCommentbackText }}</td>
                <td>{{ comment.replyText || '暂无回复' }}</td>
                <td>{{ formatDate(comment.insertTime) }}</td>
                <td>
                  <button class="btn btn-sm btn-secondary" @click="viewProduct(comment.chongwuyongpinId)">
                    查看商品
                  </button>
                  <button class="btn btn-sm btn-secondary" @click="viewCommentDetail(comment)">
                    查看详情
                  </button>
                  <button class="btn btn-sm btn-danger" @click="handleDelete(comment.id)">
                    删除
                  </button>
                </td>
              </tr>
              <tr v-if="comments.length === 0">
                <td colspan="5" class="no-data">暂无评价</td>
              </tr>
            </tbody>
          </table>
        </div>
        
        <!-- 分页 -->
        <div class="pagination" v-if="total > 0">
          <button 
            class="btn btn-secondary" 
            @click="handlePageChange(currentPage - 1)" 
            :disabled="currentPage === 1"
          >
            上一页
          </button>
          <span class="page-info">
            第 {{ currentPage }} / {{ totalPage }} 页
          </span>
          <button 
            class="btn btn-secondary" 
            @click="handlePageChange(currentPage + 1)" 
            :disabled="currentPage === totalPage"
          >
            下一页
          </button>
        </div>
      </div>
    </main>
    <Footer />
    
    <!-- 评价详情弹窗 -->
    <div class="modal" v-if="showCommentDetailModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>评价详情</h3>
          <button class="modal-close" @click="showCommentDetailModal = false">×</button>
        </div>
        <div class="modal-body">
          <div v-if="currentComment" class="comment-detail">
            <!-- 评价基本信息 -->
            <div class="detail-section">
              <h4>评价基本信息</h4>
              <div class="detail-grid">
                <div class="detail-item">
                  <span class="detail-label">评价ID：</span>
                  <span class="detail-value">{{ currentComment.id }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">评价时间：</span>
                  <span class="detail-value">{{ formatDate(currentComment.insertTime) }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">更新时间：</span>
                  <span class="detail-value">{{ formatDate(currentComment.updateTime) }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">创建时间：</span>
                  <span class="detail-value">{{ formatDate(currentComment.createTime) }}</span>
                </div>
              </div>
            </div>
            
            <!-- 商品信息 -->
            <div class="detail-section">
              <h4>商品信息</h4>
              <div class="product-info">
                <div class="product-image">
                  <img :src="currentComment.chongwuyongpinPhoto ? 'http://localhost:8080/wangshangchongwudian/' + currentComment.chongwuyongpinPhoto : ''" :alt="currentComment.chongwuyongpinName">
                </div>
                <div class="product-details">
                  <h5>{{ currentComment.chongwuyongpinName }}</h5>
                  <p>商品编号：{{ currentComment.chongwuyongpinUuidNumber }}</p>
                  <p>商品类型：{{ currentComment.chongwuyongpinValue }}</p>
                  <p>商品库存：{{ currentComment.chongwuyongpinKucunNumber }}件</p>
                  <p>商品价格：¥{{ currentComment.chongwuyongpinNewMoney }}</p>
                  <p>商品热度：{{ currentComment.chongwuyongpinClicknum }}</p>
                  <p>商品状态：{{ currentComment.shangxiaValue }}</p>
                </div>
              </div>
            </div>
            
            <!-- 评价内容 -->
            <div class="detail-section">
              <h4>评价内容</h4>
              <div class="comment-content">
                {{ currentComment.chongwuyongpinCommentbackText }}
              </div>
            </div>
            
            <!-- 回复内容 -->
            <div class="detail-section" v-if="currentComment.replyText">
              <h4>回复内容</h4>
              <div class="reply-content">
                {{ currentComment.replyText }}
              </div>
            </div>
            
            <!-- 用户信息 -->
            <div class="detail-section">
              <h4>用户信息</h4>
              <div class="detail-grid">
                <div class="detail-item">
                  <span class="detail-label">用户姓名：</span>
                  <span class="detail-value">{{ currentComment.yonghuName }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">联系电话：</span>
                  <span class="detail-value">{{ currentComment.yonghuPhone }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">电子邮箱：</span>
                  <span class="detail-value">{{ currentComment.yonghuEmail }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">账户余额：</span>
                  <span class="detail-value">¥{{ currentComment.newMoney }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showCommentDetailModal = false">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { chongwuyongpinCommentbackApi, yonghuApi } from '../../utils/api'
import message from '../../utils/message'
import confirm from '../../utils/confirm'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'

const router = useRouter()
const currentPage = ref(1)
const total = ref(0)
const totalPage = ref(1)
const comments = ref([])
const loading = ref(false)
const userInfo = ref(null)
const showCommentDetailModal = ref(false)
const currentComment = ref(null)

// 搜索参数
const searchParams = ref({
  chongwuyongpinCommentbackText: '',
  replyText: ''
})

// 获取用户信息
const getUserInfo = async () => {
  try {
    const response = await yonghuApi.session()
    if (response.code === 0) {
      userInfo.value = response.data
      return response.data
    } else {
      message.error('获取用户信息失败')
      router.push('/user/login')
      return null
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    message.error('获取用户信息失败')
    router.push('/user/login')
    return null
  }
}

// 获取评价列表
const fetchComments = async () => {
  try {
    loading.value = true
    const user = await getUserInfo()
    if (!user) return
    
    const params = {
      page: currentPage.value,
      limit: 8,
      sort: 'id',
      order: 'desc',
      ...searchParams.value
    }
    
    const response = await chongwuyongpinCommentbackApi.getUserComments(user.id, params)
    
    if (response.code === 0) {
      comments.value = response.data.list
      total.value = response.data.total
      totalPage.value = response.data.totalPage
    } else {
      message.error('获取评价列表失败')
    }
  } catch (error) {
    console.error('获取评价列表失败:', error)
    message.error('获取评价列表失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 切换页码
const handlePageChange = (newPage) => {
  if (newPage >= 1 && newPage <= totalPage.value) {
    currentPage.value = newPage
    fetchComments()
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchComments()
}

// 重置
const handleReset = () => {
  searchParams.value = {
    chongwuyongpinCommentbackText: '',
    replyText: ''
  }
  currentPage.value = 1
  fetchComments()
}

// 查看商品
const viewProduct = (productId) => {
  router.push(`/product/${productId}`)
}

// 查看评价详情
const viewCommentDetail = (comment) => {
  currentComment.value = comment
  showCommentDetailModal.value = true
}

// 删除评价
const handleDelete = async (id) => {
  try {
    const result = await confirm({
      message: '确定要删除这条评价吗？'
    })
    if (result) {
      const response = await chongwuyongpinCommentbackApi.delete([id])
      if (response.code === 0) {
        message.success('删除评价成功')
        fetchComments() // 刷新评价列表
      } else {
        message.error('删除评价失败：' + response.msg)
      }
    }
  } catch (error) {
    console.error('删除评价失败:', error)
    message.error('删除评价失败，请稍后重试')
  }
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString()
}

// 页面加载时的初始化
onMounted(() => {
  fetchComments()
})
</script>

<style scoped>
.comments-page {
  min-height: 100vh;
  background-color: var(--bg);
}

.comments-content {
  padding: var(--spacing-2xl) 0;
}

.comments-header {
  text-align: center;
  margin-bottom: var(--spacing-2xl);
}

.comments-header h1 {
  color: var(--text-1);
  font-size: var(--fs-2xl);
  font-weight: 600;
  margin-bottom: var(--spacing-sm);
}

.comments-header p {
  color: var(--text-2);
  font-size: var(--fs-base);
  margin: 0;
}

/* 搜索区域样式 */
.search-section {
  margin-bottom: var(--spacing-xl);
  background-color: white;
  padding: 20px;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
}

.form-row {
  display: flex;
  align-items: flex-end;
  gap: 24px;
  flex-wrap: wrap;
}

.form-group {
  flex: 1;
  min-width: 200px;
  max-width: 300px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--text-1);
  font-size: 14px;
}

.form-group input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  transition: all 0.3s ease;
  font-family: inherit;
  background-color: white;
}

.form-group input:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.form-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 20px;
}

/* 按钮样式增强 */
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 10px 16px;
  font-size: 14px;
  font-weight: 500;
  border-radius: var(--radius-base);
  transition: all 0.3s ease;
  cursor: pointer;
  border: none;
  text-decoration: none;
}

.btn-primary {
  background-color: var(--primary);
  color: white;
}

.btn-primary:hover {
  background-color: var(--primary-dark);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(66, 184, 131, 0.3);
}

.btn-secondary {
  background-color: white;
  color: var(--text-1);
  border: 1px solid var(--border);
}

.btn-secondary:hover {
  background-color: var(--hover-bg);
  border-color: var(--primary);
  color: var(--primary);
}

.btn-danger {
  background-color: var(--danger);
  color: white;
  border: 1px solid var(--danger);
}

.btn-danger:hover {
  background-color: var(--danger-dark);
  border-color: var(--danger-dark);
  color: white;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(245, 34, 45, 0.3);
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-sm {
  padding: 6px 12px;
  font-size: 12px;
  margin-right: 8px;
}

/* 表格容器 */
.table-container {
  overflow-x: auto;
  margin-bottom: 24px;
  background-color: white;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
}

.admin-table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
}

.admin-table th,
.admin-table td {
  padding: 16px 20px;
  text-align: left;
  border-bottom: 1px solid var(--border);
  font-size: 14px;
}

.admin-table th {
  background-color: var(--bg);
  font-weight: 600;
  color: var(--text-1);
  font-size: 14px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.admin-table tr:last-child td {
  border-bottom: none;
}

.admin-table tr:hover {
  background-color: var(--hover-bg);
  transition: background-color 0.2s ease;
}

/* 商品信息样式 */
.product-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.product-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex: 1;
}

.product-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-1);
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-price {
  font-size: 12px;
  color: var(--danger);
  font-weight: 600;
}

.product-image {
  width: 60px;
  height: 60px;
  border-radius: var(--radius-sm);
  object-fit: cover;
  flex-shrink: 0;
}

/* 评价详情弹窗样式 */
.comment-detail {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xl);
}

.detail-section {
  background-color: var(--card);
  border-radius: var(--radius-base);
  padding: var(--spacing-lg);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.detail-section h4 {
  margin: 0 0 var(--spacing-lg) 0;
  color: var(--text-1);
  font-size: var(--fs-base);
  font-weight: 600;
  padding-bottom: var(--spacing-sm);
  border-bottom: 1px solid var(--border);
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--spacing-base);
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
}

.detail-label {
  font-size: var(--fs-sm);
  color: var(--text-2);
  font-weight: 500;
}

.detail-value {
  font-size: var(--fs-sm);
  color: var(--text-1);
  font-weight: 400;
}

.comment-content,
.reply-content {
  padding: var(--spacing-base);
  background-color: var(--bg);
  border-radius: var(--radius-base);
  font-size: var(--fs-base);
  line-height: 1.5;
  color: var(--text-1);
}

/* 评价详情弹窗中的商品信息 */
.comment-detail .product-info {
  display: flex;
  gap: var(--spacing-lg);
  align-items: flex-start;
  background: none !important;
}

.comment-detail .product-image {
  flex-shrink: 0;
  width: 100px;
  height: 100px;
  border-radius: var(--radius-base);
  border: 1px solid var(--border);
  overflow: hidden;
  background: none !important;
}

.comment-detail .product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  background: none !important;
}

.comment-detail .product-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
  background: none !important;
}

/* 确保弹窗内容背景为白色，没有背景图 */
.modal-content,
.modal-body,
.detail-section {
  background: white !important;
  background-image: none !important;
}

.comment-detail .product-details h5 {
  margin: 0 0 var(--spacing-sm) 0;
  color: var(--text-1);
  font-size: var(--fs-base);
  font-weight: 600;
}

.comment-detail .product-details p {
  margin: 0;
  font-size: var(--fs-sm);
  color: var(--text-2);
}

/* 分页样式 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  margin-top: 24px;
  font-size: 14px;
  color: var(--text-2);
}

.page-info {
  color: var(--text-2);
  font-size: 14px;
}

/* 无数据样式 */
.no-data {
  text-align: center;
  color: var(--text-3);
  padding: 40px 0;
  font-size: 14px;
}

/* 弹窗样式 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

.modal-content {
  background-color: white;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-lg);
  width: 90%;
  max-width: 800px;
  max-height: 90vh;
  overflow-y: auto;
  animation: slideIn 0.3s ease;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-lg) var(--spacing-xl);
  border-bottom: 1px solid var(--border);
  background-color: var(--card);
  border-radius: var(--radius-base) var(--radius-base) 0 0;
}

.modal-header h3 {
  margin: 0;
  color: var(--text-1);
  font-size: var(--fs-lg);
  font-weight: 600;
}

.modal-close {
  background: none;
  border: none;
  font-size: var(--fs-xl);
  color: var(--text-2);
  cursor: pointer;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius-sm);
  transition: all 0.3s ease;
}

.modal-close:hover {
  background-color: var(--hover-bg);
  color: var(--text-1);
}

.modal-body {
  padding: var(--spacing-xl);
  background-color: white;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: var(--spacing-base);
  padding: var(--spacing-lg) var(--spacing-xl);
  border-top: 1px solid var(--border);
  background-color: var(--card);
  border-radius: 0 0 var(--radius-base) var(--radius-base);
}

/* 动画 */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideIn {
  from {
    transform: translateY(-20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

@media (max-width: 768px) {
  .comments-content {
    padding: var(--spacing-xl) 0;
  }
  
  .form-row {
    flex-direction: column;
    align-items: stretch;
  }
  
  .form-group {
    max-width: none;
  }
  
  .form-actions {
    justify-content: center;
  }
  
  .admin-table th,
  .admin-table td {
    padding: 12px 16px;
    font-size: 12px;
  }
  
  .product-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .product-image {
    width: 40px;
    height: 40px;
  }
  
  .modal-content {
    width: 95%;
    margin: 0 var(--spacing-base);
  }
  
  .modal-header,
  .modal-body,
  .modal-footer {
    padding: var(--spacing-base);
  }
  
  .detail-grid {
    grid-template-columns: 1fr;
  }
  
  .comment-detail .product-info {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .comment-detail .product-image {
    margin: 0 auto var(--spacing-lg);
  }
}
</style>