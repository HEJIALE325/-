<template>
  <div class="product-detail-page">
    <Header />
    <main class="product-detail-content">
      <div class="container">
        <!-- 加载中 -->
        <div v-if="loading" class="loading">
          <div class="loading-spinner"></div>
          <p>加载中...</p>
        </div>

        <!-- 商品详情 -->
        <div v-else-if="product" class="product-detail">
          <div class="product-header">
            <h1>{{ product.chongwuyongpinName }}</h1>
            <div class="product-meta">
              <span class="product-category">{{ getProductTypeName(product.chongwuyongpinTypes) }}</span>
              <span class="product-status" :class="{ 'on-sale': product.shangxiaValue === '上架', 'off-sale': product.shangxiaValue !== '上架' }">{{ product.shangxiaValue }}</span>
              <span class="product-click">浏览量：{{ product.chongwuyongpinClicknum }}</span>
            </div>
          </div>

          <div class="product-body">
            <!-- 商品图片 -->
            <div class="product-images">
              <div class="main-image">
                <img :src="'http://localhost:8080/wangshangchongwudian/' + product.chongwuyongpinPhoto" :alt="product.chongwuyongpinName">
              </div>
            </div>

            <!-- 商品信息 -->
            <div class="product-info">
              <div class="price-section">
                <span class="price-label">价格：</span>
                <span class="product-price">¥{{ product.chongwuyongpinNewMoney }}</span>
                <span v-if="product.chongwuyongpinOldMoney" class="product-original-price">¥{{ product.chongwuyongpinOldMoney }}</span>
              </div>

              <div class="stock-section">
                <span class="stock-label">库存：</span>
                <span class="stock-value" :class="{ 'low-stock': product.chongwuyongpinKucunNumber < 10 }">{{ product.chongwuyongpinKucunNumber }} 件</span>
              </div>

              <div class="quantity-section">
                <span class="quantity-label">数量：</span>
                <div class="quantity-control">
                  <button class="quantity-btn" @click="decreaseQuantity" :disabled="quantity <= 1">-</button>
                  <input type="number" v-model.number="quantity" min="1" :max="product.chongwuyongpinKucunNumber" @change="handleQuantityChange">
                  <button class="quantity-btn" @click="increaseQuantity" :disabled="quantity >= product.chongwuyongpinKucunNumber">+</button>
                </div>
              </div>

              <div class="actions-section">
                <button class="btn btn-secondary" @click="addToCart" :disabled="product.shangxiaValue !== '上架'">加入购物车</button>
                <button class="btn btn-primary" @click="buyNow" :disabled="product.shangxiaValue !== '上架'">立即购买</button>
                <button class="btn btn-outline" @click="addToFavorites">
                  <span v-if="isFavorite">已收藏</span>
                  <span v-else>收藏</span>
                </button>
              </div>

              <div class="product-description">
                <h3>商品描述</h3>
                <div v-html="product.chongwuyongpinContent"></div>
              </div>

              <div class="product-specs">
                <h3>商品信息</h3>
                <ul>
                  <li>
                    <span class="spec-label">商品编号：</span>
                    <span class="spec-value">{{ product.chongwuyongpinUuidNumber }}</span>
                  </li>
                  <li>
                    <span class="spec-label">商品类型：</span>
                    <span class="spec-value">{{ getProductTypeName(product.chongwuyongpinTypes) }}</span>
                  </li>
                  <li>
                    <span class="spec-label">上架状态：</span>
                    <span class="spec-value">{{ product.shangxiaValue }}</span>
                  </li>
                  <li>
                    <span class="spec-label">发布时间：</span>
                    <span class="spec-value">{{ product.insertTime }}</span>
                  </li>
                </ul>
              </div>
            </div>
          </div>

          <!-- 评论区域 -->
          <div class="product-comments">
            <h3>用户评论</h3>
            <div v-if="comments.length > 0" class="comments-list">
              <div v-for="comment in comments" :key="comment.id" class="comment-item">
                <div class="comment-header">
                  <div class="comment-avatar">
                    <img :src="comment.yonghuPhoto ? 'http://localhost:8080/wangshangchongwudian/' + comment.yonghuPhoto : 'https://via.placeholder.com/40x40'" alt="用户头像">
                  </div>
                  <div class="comment-user-info">
                    <span class="comment-user">{{ comment.yonghuName }}</span>
                    <span class="comment-time">{{ comment.createTime }}</span>
                  </div>
                </div>
                <div class="comment-content">{{ comment.chongwuyongpinCommentbackText }}</div>
                <div v-if="comment.replyText" class="comment-reply">
                  <span class="reply-label">回复：</span>
                  <span class="reply-content">{{ comment.replyText }}</span>
                </div>
              </div>
            </div>
            <div v-else class="no-comments">
              <p>暂无评论</p>
            </div>
          </div>
        </div>

        <!-- 无数据 -->
        <div v-else class="no-data">
          <p>商品不存在</p>
        </div>
      </div>
    </main>
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { chongwuyongpinApi, cartApi, chongwuyongpinCollectionApi, chongwuyongpinCommentbackApi, dictionaryApi, usersApi } from '../../utils/api'
import message from '../../utils/message'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'

const router = useRouter()
const route = useRoute()
const product = ref(null)
const loading = ref(false)
const quantity = ref(1)
const isFavorite = ref(false)
const comments = ref([])
const productTypes = ref([])

// 加载商品类型字典
const loadProductTypes = async () => {
  try {
    const response = await dictionaryApi.getByDicCode('chongwuyongpin_types')
    if (response.code === 0) {
      productTypes.value = response.data.list || []
    }
  } catch (error) {
    console.error('获取商品类型失败:', error)
  }
}

// 获取商品类型名称
const getProductTypeName = (typeCode) => {
  const type = productTypes.value.find(t => t.codeIndex === typeCode)
  return type ? type.indexName : '未知类型'
}

// 获取商品详情
const getProductDetail = async () => {
  try {
    loading.value = true
    const productId = route.params.id
    const response = await chongwuyongpinApi.getDetail(productId)
    if (response.code === 0) {
      product.value = response.data
      // 加载评论
      loadComments(productId)
      // 检查是否已收藏
      checkFavoriteStatus(productId)
    } else {
      message.error('获取商品详情失败')
    }
  } catch (error) {
    console.error('获取商品详情失败:', error)
    message.error('获取商品详情失败')
  } finally {
    loading.value = false
  }
}

// 加载评论
const loadComments = async (productId) => {
  try {
    const response = await chongwuyongpinCommentbackApi.getList({ 
      chongwuyongpinId: productId, 
      limit: 10,
      t: Date.now()
    })
    if (response.code === 0) {
      comments.value = response.data.list || []
    }
  } catch (error) {
    console.error('获取评论失败:', error)
  }
}

// 检查收藏状态
const checkFavoriteStatus = async (productId) => {
  try {
    // 获取用户会话信息
    const sessionResponse = await usersApi.session()
    if (sessionResponse.code === 0 && sessionResponse.data) {
      const yonghuId = sessionResponse.data.yonghuId || sessionResponse.data.id
      
      const response = await chongwuyongpinCollectionApi.getList({ 
        chongwuyongpinId: productId,
        yonghuId,
        limit: 1,
        t: Date.now()
      })
      if (response.code === 0 && response.data.total > 0) {
        isFavorite.value = true
      }
    }
  } catch (error) {
    console.error('检查收藏状态失败:', error)
  }
}

// 减少数量
const decreaseQuantity = () => {
  if (quantity.value > 1) {
    quantity.value--
  }
}

// 增加数量
const increaseQuantity = () => {
  if (product.value && quantity.value < product.value.chongwuyongpinKucunNumber) {
    quantity.value++
  }
}

// 处理数量变化
const handleQuantityChange = () => {
  if (product.value) {
    if (quantity.value < 1) {
      quantity.value = 1
    } else if (quantity.value > product.value.chongwuyongpinKucunNumber) {
      quantity.value = product.value.chongwuyongpinKucunNumber
    }
  }
}

// 加入购物车
const addToCart = async () => {
  if (!product.value) return
  
  try {
    // 获取用户会话信息
    const sessionResponse = await usersApi.session()
    if (sessionResponse.code === 0 && sessionResponse.data) {
      const yonghuId = sessionResponse.data.yonghuId || sessionResponse.data.id
      
      // 调用购物车API，传递必要字段
      const response = await cartApi.add({
        yonghuId,
        chongwuyongpinId: product.value.id,
        buyNumber: quantity.value,
        t: Date.now()
      })
      if (response.code === 0) {
        message.success('加入购物车成功')
      } else {
        message.error('加入购物车失败')
      }
    } else {
      message.error('获取用户信息失败')
    }
  } catch (error) {
    console.error('加入购物车失败:', error)
    message.error('加入购物车失败')
  }
}

// 立即购买
const buyNow = async () => {
  if (!product.value) return
  
  try {
    // 获取用户会话信息
    const sessionResponse = await usersApi.session()
    if (sessionResponse.code === 0 && sessionResponse.data) {
      const yonghuId = sessionResponse.data.yonghuId || sessionResponse.data.id
      
      // 先加入购物车
      const response = await cartApi.add({
        yonghuId,
        chongwuyongpinId: product.value.id,
        buyNumber: quantity.value,
        t: Date.now()
      })
      if (response.code === 0) {
        // 跳转到购物车页面
        router.push('/cart')
      } else {
        message.error('加入购物车失败')
      }
    } else {
      message.error('获取用户信息失败')
    }
  } catch (error) {
    console.error('加入购物车失败:', error)
    message.error('加入购物车失败')
  }
}

// 加入收藏
const addToFavorites = async () => {
  if (!product.value) return
  
  try {
    // 获取用户会话信息
    const sessionResponse = await usersApi.session()
    if (sessionResponse.code === 0 && sessionResponse.data) {
      const yonghuId = sessionResponse.data.yonghuId || sessionResponse.data.id
      
      if (isFavorite.value) {
        // 取消收藏
        // 先获取收藏记录的id
        const collectionResponse = await chongwuyongpinCollectionApi.getList({
          chongwuyongpinId: product.value.id,
          chongwuyongpinCollectionTypes: 1,
          yonghuId,
          limit: 1
        })
        if (collectionResponse.code === 0 && collectionResponse.data.list && collectionResponse.data.list.length > 0) {
          const collectionId = collectionResponse.data.list[0].id
          // 调用删除接口
          const deleteResponse = await chongwuyongpinCollectionApi.delete([collectionId])
          if (deleteResponse.code === 0) {
            message.success('已取消收藏')
            isFavorite.value = false
          } else {
            message.error('取消收藏失败')
          }
        } else {
          message.error('获取收藏记录失败')
        }
      } else {
        // 添加收藏
        const response = await chongwuyongpinCollectionApi.add({
          yonghuId,
          chongwuyongpinId: product.value.id,
          chongwuyongpinCollectionTypes: 1,
          t: Date.now()
        })
        if (response.code === 0) {
          message.success('收藏成功')
          isFavorite.value = true
        } else {
          message.error('收藏失败')
        }
      }
    } else {
      message.error('获取用户信息失败')
    }
  } catch (error) {
    console.error('收藏操作失败:', error)
    message.error('收藏操作失败')
  }
}

// 页面加载时获取商品类型和商品详情
onMounted(() => {
  loadProductTypes()
  getProductDetail()
})
</script>

<style scoped>
.product-detail-page {
  min-height: 100vh;
  background-color: var(--bg);
}

.product-detail-content {
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

.product-detail {
  background: var(--card);
  border-radius: var(--radius-base);
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  margin-bottom: var(--spacing-2xl);
}

.product-header {
  padding: var(--spacing-xl);
  border-bottom: 1px solid var(--border);
}

.product-header h1 {
  color: var(--text-1);
  font-size: var(--fs-xl);
  font-weight: 600;
  margin-bottom: var(--spacing-base);
}

.product-meta {
  display: flex;
  gap: var(--spacing-lg);
  font-size: var(--fs-sm);
  color: var(--text-2);
  align-items: center;
}

.product-status {
  padding: var(--spacing-xs) var(--spacing-sm);
  border-radius: var(--radius-sm);
  font-weight: 500;
}

.product-status.on-sale {
  background-color: rgba(82, 196, 26, 0.1);
  color: var(--success);
}

.product-status.off-sale {
  background-color: rgba(255, 77, 79, 0.1);
  color: var(--danger);
}

.product-body {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--spacing-xl);
  padding: var(--spacing-xl);
}

.product-images {
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

.product-info {
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

.product-price {
  color: var(--danger);
  font-size: var(--fs-2xl);
  font-weight: 600;
}

.product-original-price {
  color: var(--text-3);
  font-size: var(--fs-base);
  text-decoration: line-through;
}

.stock-section {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.stock-label {
  font-weight: 500;
  color: var(--text-1);
  font-size: var(--fs-base);
}

.stock-value {
  color: var(--text-2);
  font-size: var(--fs-base);
}

.stock-value.low-stock {
  color: var(--danger);
  font-weight: 500;
}

.quantity-section {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.quantity-label {
  font-weight: 500;
  color: var(--text-1);
  font-size: var(--fs-base);
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
}

.quantity-btn {
  width: 32px;
  height: 32px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  background-color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--fs-base);
  transition: all 0.3s ease;
}

.quantity-btn:hover:not(:disabled) {
  border-color: var(--primary);
  color: var(--primary);
}

.quantity-btn:disabled {
  cursor: not-allowed;
  color: var(--text-3);
  border-color: var(--border);
}

.quantity-control input {
  width: 60px;
  height: 32px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  text-align: center;
  font-size: var(--fs-base);
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

.actions-section .btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.product-description {
  margin-top: var(--spacing-lg);
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--border);
}

.product-description h3 {
  color: var(--text-1);
  font-size: var(--fs-lg);
  font-weight: 600;
  margin-bottom: var(--spacing-base);
}

.product-description div {
  color: var(--text-2);
  font-size: var(--fs-base);
  line-height: 1.6;
}

.product-specs {
  margin-top: var(--spacing-lg);
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--border);
}

.product-specs h3 {
  color: var(--text-1);
  font-size: var(--fs-lg);
  font-weight: 600;
  margin-bottom: var(--spacing-base);
}

.product-specs ul {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}

.product-specs li {
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

.product-comments {
  background: var(--card);
  border-radius: var(--radius-base);
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  padding: var(--spacing-xl);
}

.product-comments h3 {
  color: var(--text-1);
  font-size: var(--fs-lg);
  font-weight: 600;
  margin-bottom: var(--spacing-lg);
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

.comment-item {
  padding: var(--spacing-base);
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  background-color: white;
}

.comment-header {
  display: flex;
  align-items: flex-start;
  gap: var(--spacing-base);
  margin-bottom: var(--spacing-sm);
}

.comment-avatar {
  flex-shrink: 0;
}

.comment-avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.comment-user-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.comment-user {
  font-weight: 500;
  color: var(--text-1);
  font-size: var(--fs-sm);
}

.comment-time {
  color: var(--text-3);
  font-size: var(--fs-xs);
}

.comment-rating {
  display: flex;
  gap: 2px;
  margin-left: auto;
}

.star {
  color: #ddd;
  font-size: var(--fs-sm);
}

.star.active {
  color: var(--warning);
}

.comment-content {
  color: var(--text-2);
  font-size: var(--fs-sm);
  line-height: 1.5;
  margin-bottom: var(--spacing-sm);
}

.comment-reply {
  margin-top: var(--spacing-sm);
  padding-top: var(--spacing-sm);
  border-top: 1px solid var(--border);
  font-size: var(--fs-xs);
  color: var(--text-3);
}

.reply-label {
  font-weight: 500;
  color: var(--text-2);
}

.reply-content {
  color: var(--text-3);
}

.no-comments {
  text-align: center;
  padding: var(--spacing-xl) 0;
  color: var(--text-3);
  font-size: var(--fs-base);
}

@media (max-width: 768px) {
  .product-body {
    grid-template-columns: 1fr;
  }
  
  .actions-section {
    flex-direction: column;
  }
  
  .product-header {
    padding: var(--spacing-lg);
  }
  
  .product-body {
    padding: var(--spacing-lg);
  }
  
  .product-comments {
    padding: var(--spacing-lg);
  }
}
</style>