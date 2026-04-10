<template>
  <div class="orders-page">
    <Header />
    <main class="orders-content">
      <div class="container">
        <div class="orders-header">
          <h1>我的订单</h1>
          <p>查看和管理您的所有订单</p>
        </div>
        
        <!-- 订单状态筛选 -->
        <div class="order-filters">
          <button 
            class="filter-btn" 
            :class="{ active: activeFilter === 'all' }"
            @click="activeFilter = 'all'; fetchOrders()"
          >
            全部
          </button>
          <button 
            class="filter-btn" 
            :class="{ active: activeFilter === '101' }"
            @click="activeFilter = '101'; fetchOrders()"
          >
            已支付
          </button>
          <button 
            class="filter-btn" 
            :class="{ active: activeFilter === '102' }"
            @click="activeFilter = '102'; fetchOrders()"
          >
            已退款
          </button>
          <button 
            class="filter-btn" 
            :class="{ active: activeFilter === '103' }"
            @click="activeFilter = '103'; fetchOrders()"
          >
            已发货
          </button>
          <button 
            class="filter-btn" 
            :class="{ active: activeFilter === '104' }"
            @click="activeFilter = '104'; fetchOrders()"
          >
            已收货
          </button>
          <button 
            class="filter-btn" 
            :class="{ active: activeFilter === '105' }"
            @click="activeFilter = '105'; fetchOrders()"
          >
            已评价
          </button>
        </div>
        
        <!-- 订单列表 -->
        <div class="orders-list">
          <div v-if="loading" class="loading-state">
            <div class="loading-spinner"></div>
            <p>加载中...</p>
          </div>
          
          <div v-else class="order-card" v-for="order in orders" :key="order.orderNo">
            <div class="order-header">
              <div class="order-info">
                <span class="order-id">订单号: {{ order.orderNo }}</span>
                <span class="order-time">下单时间: {{ order.createTime }}</span>
              </div>
              <div class="order-status" :class="getStatusClass(order.chongwuyongpinOrderTypes)">
                {{ getOrderStatusText(order.chongwuyongpinOrderTypes) }}
              </div>
            </div>
            
            <div class="order-items">
              <div class="order-item" v-for="item in order.items" :key="item.id">
                <div class="item-image">
                  <img :src="'http://localhost:8080/wangshangchongwudian/' + item.chongwuyongpinPhoto" :alt="item.chongwuyongpinName">
                </div>
                <div class="item-info">
                  <h3 class="item-name">{{ item.chongwuyongpinName }}</h3>
                  <div class="item-price-row">
                    <span class="item-price">¥{{ item.chongwuyongpinNewMoney }}</span>
                    <span class="item-quantity">×{{ item.buyNumber }}</span>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="order-shipping">
              <h4>收货信息</h4>
              <p class="shipping-info">{{ order.addressName }} {{ order.addressPhone }}</p>
              <p class="shipping-address">{{ order.addressDizhi }}</p>
            </div>
            
            <div class="order-footer">
              <div class="order-total">
                共 {{ order.totalQuantity }} 件商品，合计：
                <span class="total-price">¥{{ order.totalPrice.toFixed(2) }}</span>
              </div>
              <div class="order-actions">
                <button class="btn btn-sm btn-secondary" v-if="order.chongwuyongpinOrderTypes === 101">
                  去付款
                </button>
                <button class="btn btn-sm btn-secondary" v-if="order.chongwuyongpinOrderTypes === 103" @click="handleReceiving(order.items[0].id)">
                  确认收货
                </button>
                <button class="btn btn-sm btn-secondary" v-if="order.chongwuyongpinOrderTypes === 104" @click="openCommentModal(order.items[0].id)">
                  评价
                </button>
                <button class="btn btn-sm btn-secondary" @click="openOrderDetail(order)">
                  查看详情
                </button>
                <button class="btn btn-sm btn-secondary">
                  联系客服
                </button>
              </div>
            </div>
          </div>
          
          <!-- 空状态 -->
          <div class="empty-state" v-if="!loading && orders.length === 0">
            <div class="empty-icon">📦</div>
            <h3>暂无订单</h3>
            <p>您还没有任何订单，快去选购商品吧！</p>
            <router-link to="/products" class="btn btn-primary">
              去购物
            </router-link>
          </div>
        </div>
        
        <!-- 分页 -->
        <div class="pagination" v-if="!loading && orders.length > 0">
          <button class="page-btn" :disabled="currentPage === 1" @click="changePage(currentPage - 1)">上一页</button>
          <span class="page-info">第 {{ currentPage }} 页，共 {{ totalPages }} 页</span>
          <button class="page-btn" :disabled="currentPage === totalPages" @click="changePage(currentPage + 1)">下一页</button>
        </div>
      </div>
    </main>
    <Footer />
    
    <!-- 订单详情弹窗 -->
    <div class="modal" v-if="showDetailModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>订单详情</h3>
          <button class="modal-close" @click="closeOrderDetail">×</button>
        </div>
        <div class="modal-body">
          <!-- 加载状态 -->
          <div v-if="orderDetailLoading" class="detail-loading-state">
            <div class="loading-spinner"></div>
            <p>正在加载订单详情...</p>
          </div>

          <!-- 错误提示 -->
          <div v-else-if="orderDetailError" class="detail-error-state">
            <div class="error-icon">⚠️</div>
            <p class="error-message">{{ orderDetailError }}</p>
            <p class="error-hint">已显示基础订单信息，您仍可以查看以下内容</p>
          </div>

          <!-- 订单详情内容 -->
          <div v-if="currentOrder && !orderDetailLoading" class="order-detail">
            <!-- 订单基本信息 -->
            <div class="detail-section">
              <h4>订单基本信息</h4>
              <div class="detail-grid">
                <div class="detail-item">
                  <span class="detail-label">订单号：</span>
                  <span class="detail-value">{{ currentOrder.chongwuyongpinOrderUuidNumber }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">下单时间：</span>
                  <span class="detail-value">{{ currentOrder.createTime }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">订单状态：</span>
                  <span class="detail-value status-badge" :class="getStatusClass(currentOrder.chongwuyongpinOrderTypes)">
                    {{ getOrderStatusText(currentOrder.chongwuyongpinOrderTypes) }}
                  </span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">支付方式：</span>
                  <span class="detail-value">{{ currentOrder.chongwuyongpinOrderPaymentValue || '余额支付' }}</span>
                </div>
                <div class="detail-item" v-if="currentOrder.insertTime">
                  <span class="detail-label">支付时间：</span>
                  <span class="detail-value">{{ currentOrder.insertTime }}</span>
                </div>
                <div class="detail-item" v-if="currentOrder.totalItems">
                  <span class="detail-label">商品种类：</span>
                  <span class="detail-value">{{ currentOrder.totalItems }} 种</span>
                </div>
              </div>
            </div>

            <!-- 多商品订单列表 -->
            <div class="detail-section" v-if="currentOrder.orderDetails && currentOrder.orderDetails.length > 1">
              <h4>商品清单 ({{ currentOrder.orderDetails.length }}件商品)</h4>
              <div class="order-items-list">
                <div v-for="(item, index) in currentOrder.orderDetails" :key="index" class="order-item-detail">
                  <div class="item-image-small">
                    <img :src="'http://localhost:8080/wangshangchongwudian/' + (item.product?.productPhoto || currentOrder.chongwuyongpinPhoto)" :alt="item.product?.productName">
                  </div>
                  <div class="item-info-small">
                    <h5>{{ item.product?.productName || '未知商品' }}</h5>
                    <p class="item-meta">编号: {{ item.product?.productUuidNumber || '-' }}</p>
                    <p class="item-meta">单价: ¥{{ item.product?.productNewMoney || '-' }}</p>
                    <p class="item-meta">数量: {{ item.buyNumber }}件</p>
                    <p class="item-price">小计: ¥{{ item.truePrice }}</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- 单商品信息 -->
            <div class="detail-section" v-else>
              <h4>商品信息</h4>
              <div class="product-info">
                <div class="product-image">
                  <img :src="'http://localhost:8080/wangshangchongwudian/' + currentOrder.chongwuyongpinPhoto" :alt="currentOrder.chongwuyongpinName">
                </div>
                <div class="product-details">
                  <h5>{{ currentOrder.chongwuyongpinName }}</h5>
                  <p v-if="currentOrder.chongwuyongpinValue">商品类型：{{ currentOrder.chongwuyongpinValue }}</p>
                  <p v-if="currentOrder.chongwuyongpinUuidNumber">商品编号：{{ currentOrder.chongwuyongpinUuidNumber }}</p>
                  <p>购买数量：{{ currentOrder.buyNumber }}件</p>
                  <p v-if="currentOrder.chongwuyongpinOldMoney">原价：¥{{ currentOrder.chongwuyongpinOldMoney }}</p>
                  <p v-if="currentOrder.chongwuyongpinNewMoney">现价：¥{{ currentOrder.chongwuyongpinNewMoney }}</p>
                  <p v-if="currentOrder.chongwuyongpinContent" class="product-content">商品描述：{{ currentOrder.chongwuyongpinContent }}</p>
                </div>
              </div>
            </div>

            <!-- 收货信息 -->
            <div class="detail-section">
              <h4>收货信息</h4>
              <div class="shipping-info-full">
                <p><strong>{{ currentOrder.addressName }}</strong> {{ currentOrder.addressPhone }}</p>
                <p>{{ currentOrder.addressDizhi }}</p>
              </div>
            </div>

            <!-- 用户信息 -->
            <div class="detail-section">
              <h4>用户信息</h4>
              <div class="detail-grid">
                <div class="detail-item">
                  <span class="detail-label">用户姓名：</span>
                  <span class="detail-value">{{ currentOrder.yonghuName }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">联系电话：</span>
                  <span class="detail-value">{{ currentOrder.yonghuPhone }}</span>
                </div>
              </div>
            </div>

            <!-- 订单金额 -->
            <div class="detail-section">
              <h4>订单金额</h4>
              <div class="amount-info">
                <div class="amount-item">
                  <span class="amount-label">商品数量：</span>
                  <span class="amount-value">{{ currentOrder.totalQuantity || currentOrder.buyNumber }} 件</span>
                </div>
                <div v-for="(item, index) in currentOrder.orderDetails" :key="index" class="amount-item">
                  <span class="amount-label">{{ item.product?.productName }}：</span>
                  <span class="amount-value">¥{{ item.truePrice }} (x{{ item.buyNumber }})</span>
                </div>
                <div class="amount-item total">
                  <span class="amount-label">实付金额：</span>
                  <span class="amount-value total">¥{{ (currentOrder.totalPrice || currentOrder.chongwuyongpinOrderTruePrice).toFixed(2) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="closeOrderDetail">关闭</button>
        </div>
      </div>
    </div>
    
    <!-- 评价弹窗 -->
    <div class="modal" v-if="showCommentModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>评价订单</h3>
          <button class="modal-close" @click="showCommentModal = false">×</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label for="comment-text">评价内容</label>
            <textarea 
              id="comment-text" 
              v-model="commentForm.commentbackText" 
              placeholder="请输入您的评价内容" 
              rows="4"
            ></textarea>
          </div>
          <div class="form-group">
            <label>评分</label>
            <div class="rating">
              <button 
                v-for="star in 5" 
                :key="star"
                class="star-btn"
                :class="{ active: commentForm.chongwuyongpinCommentbackPingfenNumber >= star }"
                @click="commentForm.chongwuyongpinCommentbackPingfenNumber = star"
              >
                ★
              </button>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showCommentModal = false">取消</button>
          <button class="btn btn-primary" @click="handleCommentback">提交评价</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { chongwuyongpinOrderApi, yonghuApi, dictionaryApi } from '../../utils/api'
import message from '../../utils/message'
import confirm from '../../utils/confirm'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'

const router = useRouter()
const activeFilter = ref('all')
const currentPage = ref(1)
const totalPages = ref(1)
const orders = ref([])
const loading = ref(false)
const userInfo = ref(null)
const showDetailModal = ref(false)
const currentOrder = ref(null)
const orderDetailLoading = ref(false)
const orderDetailError = ref('')
const showCommentModal = ref(false)
const currentCommentOrderId = ref(null)
const commentForm = ref({
  commentbackText: '',
  chongwuyongpinCommentbackPingfenNumber: 5
})
const orderTypes = ref({})
const loadingOrderTypes = ref(false)

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

// 获取订单列表
const fetchOrders = async () => {
  try {
    loading.value = true
    const user = await getUserInfo()
    if (!user) return
    
    const params = {
      page: currentPage.value,
      limit: 20, // 增加每页数量，确保能获取到完整的订单数据
      sort: 'id',
      order: 'desc',
      yonghuId: user.id,
      chongwuyongpinOrderUuidNumber: '',
      chongwuyongpinOrderPaymentTypes: '',
      t: Date.now()
    }
    
    // 如果不是全部，传递订单状态参数
    if (activeFilter.value !== 'all') {
      params.chongwuyongpinOrderTypes = activeFilter.value
    }
    
    const response = await chongwuyongpinOrderApi.getList(params)
    
    if (response.code === 0) {
      // 按订单号分组
      const orderGroups = {}
      response.data.list.forEach(order => {
        const orderNo = order.chongwuyongpinOrderUuidNumber
        if (!orderGroups[orderNo]) {
          orderGroups[orderNo] = {
            orderNo: orderNo,
            createTime: order.createTime,
            insertTime: order.insertTime,
            addressName: order.addressName,
            addressPhone: order.addressPhone,
            addressDizhi: order.addressDizhi,
            chongwuyongpinOrderTypes: order.chongwuyongpinOrderTypes,
            chongwuyongpinOrderPaymentTypes: order.chongwuyongpinOrderPaymentTypes,
            chongwuyongpinOrderValue: order.chongwuyongpinOrderValue,
            chongwuyongpinOrderPaymentValue: order.chongwuyongpinOrderPaymentValue,
            yonghuName: order.yonghuName,
            yonghuPhone: order.yonghuPhone,
            items: []
          }
        }
        // 计算商品总价
        const itemTotal = order.chongwuyongpinNewMoney * order.buyNumber
        orderGroups[orderNo].items.push({
          id: order.id,
          chongwuyongpinId: order.chongwuyongpinId,
          chongwuyongpinName: order.chongwuyongpinName,
          chongwuyongpinPhoto: order.chongwuyongpinPhoto,
          chongwuyongpinNewMoney: order.chongwuyongpinNewMoney,
          buyNumber: order.buyNumber,
          itemTotal: itemTotal
        })
      })
      
      // 转换为数组并计算订单总金额
      orders.value = Object.values(orderGroups).map(orderGroup => {
        const totalPrice = orderGroup.items.reduce((sum, item) => sum + item.itemTotal, 0)
        const totalQuantity = orderGroup.items.reduce((sum, item) => sum + item.buyNumber, 0)
        return {
          ...orderGroup,
          totalPrice: totalPrice,
          totalQuantity: totalQuantity
        }
      })
      
      totalPages.value = Math.ceil(orders.value.length / 8)
    } else {
      message.error('获取订单列表失败')
    }
  } catch (error) {
    console.error('获取订单列表失败:', error)
    message.error('获取订单列表失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 切换页码
const changePage = (newPage) => {
  if (newPage >= 1 && newPage <= totalPages.value) {
    currentPage.value = newPage
    fetchOrders()
  }
}

// 获取订单状态类名
const getStatusClass = (status) => {
  switch (status) {
    case 101:
      return 'pending'
    case 102:
      return 'refunded'
    case 103:
      return 'shipped'
    case 104:
      return 'delivered'
    case 105:
      return 'reviewed'
    default:
      return ''
  }
}

// 获取订单状态文本
const getOrderStatusText = (status) => {
  return orderTypes.value[status] || status
}

// 获取订单类型数据字典
const getOrderTypes = async () => {
  try {
    loadingOrderTypes.value = true
    const response = await dictionaryApi.getPage({
      page: 1,
      limit: 100,
      sort: '',
      order: '',
      dicCode: 'chongwuyongpin_order_types',
      t: Date.now()
    })
    
    if (response.code === 0) {
      const types = {}
      response.data.list.forEach(item => {
        types[item.codeIndex] = item.indexName
      })
      orderTypes.value = types
    } else {
      message.error('获取订单类型失败')
    }
  } catch (error) {
    console.error('获取订单类型失败:', error)
    message.error('获取订单类型失败，请稍后重试')
  } finally {
    loadingOrderTypes.value = false
  }
}

// 打开订单详情
const openOrderDetail = async (order) => {
  // 先显示弹窗并展示基础信息
  currentOrder.value = order
  showDetailModal.value = true
  orderDetailLoading.value = true
  orderDetailError.value = ''

  try {
    // 调用后端接口获取完整的订单详情
    const response = await chongwuyongpinOrderApi.getDetailByOrderNo(order.orderNo)

    if (response.code === 0 && response.data) {
      // 获取到详细数据，更新当前订单信息
      const detailData = response.data

      // 如果有订单列表数据，使用第一个订单项的详细信息
      if (detailData.orders && detailData.orders.length > 0) {
        const firstOrder = detailData.orders[0]

        // 计算订单总金额
        const totalPrice = detailData.orders.reduce((sum, item) => sum + item.truePrice, 0)
        const totalQuantity = detailData.orders.reduce((sum, item) => sum + item.buyNumber, 0)

        // 合并详细数据到当前订单
        currentOrder.value = {
          ...order,
          // 订单基本信息
          orderNo: detailData.orderNo,
          totalItems: detailData.totalItems,

          // 商品详细信息
          chongwuyongpinName: firstOrder.product?.productName || order.chongwuyongpinName,
          chongwuyongpinPhoto: firstOrder.product?.productPhoto || order.chongwuyongpinPhoto,
          chongwuyongpinUuidNumber: firstOrder.product?.productUuidNumber || order.chongwuyongpinUuidNumber,
          chongwuyongpinNewMoney: firstOrder.product?.productNewMoney || order.chongwuyongpinNewMoney,
          chongwuyongpinOldMoney: firstOrder.product?.productOldMoney,
          chongwuyongpinContent: firstOrder.product?.productContent,

          // 收货地址详细信息
          addressName: firstOrder.address?.receiverName || order.addressName,
          addressPhone: firstOrder.address?.receiverPhone || order.addressPhone,
          addressDizhi: firstOrder.address?.receiverAddress || order.addressDizhi,

          // 用户详细信息
          yonghuName: firstOrder.user?.userName || order.yonghuName,
          yonghuPhone: firstOrder.user?.userPhone || order.yonghuPhone,

          // 订单金额信息
          chongwuyongpinOrderTruePrice: totalPrice,
          buyNumber: totalQuantity,

          // 订单状态和时间
          chongwuyongpinOrderTypes: firstOrder.orderType || order.chongwuyongpinOrderTypes,
          chongwuyongpinOrderPaymentTypes: firstOrder.paymentType || order.chongwuyongpinOrderPaymentTypes,
          insertTime: firstOrder.insertTime || order.insertTime,
          createTime: firstOrder.createTime || order.createTime,

          // 保存完整的订单详情数据，用于展示
          orderDetails: detailData.orders,
          totalPrice: totalPrice,
          totalQuantity: totalQuantity
        }
      }
    } else {
      // 接口返回错误，显示错误信息但保留基础订单信息
      orderDetailError.value = response.msg || '获取订单详情失败'
      message.warning('获取订单详细数据失败，显示基础订单信息')
    }
  } catch (error) {
    console.error('获取订单详情失败:', error)
    orderDetailError.value = error.msg || '网络请求失败，请稍后重试'
    message.error('获取订单详情失败：' + (error.msg || '请稍后重试'))
  } finally {
    orderDetailLoading.value = false
  }
}

// 关闭订单详情弹窗
const closeOrderDetail = () => {
  showDetailModal.value = false
  currentOrder.value = null
  orderDetailError.value = ''
}

// 确认收货
const handleReceiving = async (id) => {
  try {
    const result = await confirm({
      message: '确定要确认收货吗？'
    })
    if (result) {
      const response = await chongwuyongpinOrderApi.receiving(id)
      if (response.code === 0) {
        message.success('确认收货成功')
        fetchOrders() // 刷新订单列表
      } else {
        message.error('确认收货失败：' + response.msg)
      }
    }
  } catch (error) {
    console.error('确认收货失败:', error)
    message.error('确认收货失败，请稍后重试')
  }
}

// 打开评价弹窗
const openCommentModal = (id) => {
  currentCommentOrderId.value = id
  commentForm.value = {
    commentbackText: '',
    chongwuyongpinCommentbackPingfenNumber: 5
  }
  showCommentModal.value = true
}

// 提交评价
const handleCommentback = async () => {
  try {
    if (!commentForm.value.commentbackText.trim()) {
      message.error('请输入评价内容')
      return
    }
    
    const response = await chongwuyongpinOrderApi.commentback(
      currentCommentOrderId.value,
      commentForm.value.commentbackText,
      commentForm.value.chongwuyongpinCommentbackPingfenNumber
    )
    
    if (response.code === 0) {
      message.success('评价成功')
      showCommentModal.value = false
      fetchOrders() // 刷新订单列表
    } else {
      message.error('评价失败：' + response.msg)
    }
  } catch (error) {
    console.error('评价失败:', error)
    message.error('评价失败，请稍后重试')
  }
}

// 页面加载时的初始化
onMounted(() => {
  getOrderTypes()
  fetchOrders()
})
</script>

<style scoped>
.orders-page {
  min-height: 100vh;
  background-color: var(--bg);
}

.orders-content {
  padding: var(--spacing-2xl) 0;
}

.orders-header {
  text-align: center;
  margin-bottom: var(--spacing-2xl);
}

.orders-header h1 {
  color: var(--text-1);
  font-size: var(--fs-2xl);
  font-weight: 600;
  margin-bottom: var(--spacing-sm);
}

.orders-header p {
  color: var(--text-2);
  font-size: var(--fs-base);
  margin: 0;
}

.order-filters {
  display: flex;
  gap: var(--spacing-base);
  margin-bottom: var(--spacing-xl);
  flex-wrap: wrap;
}

.filter-btn {
  padding: var(--spacing-sm) var(--spacing-lg);
  border: 1px solid var(--border);
  border-radius: var(--radius-full);
  background-color: white;
  color: var(--text-2);
  font-size: var(--fs-sm);
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.filter-btn:hover {
  border-color: var(--primary);
  color: var(--primary);
}

.filter-btn.active {
  background-color: var(--primary);
  border-color: var(--primary);
  color: white;
}

.orders-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
  margin-bottom: var(--spacing-xl);
}

.order-card {
  background: var(--card);
  border-radius: var(--radius-base);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-lg);
  border-bottom: 1px solid var(--border);
  background-color: #f9f9f9;
}

.order-info {
  display: flex;
  gap: var(--spacing-lg);
  font-size: var(--fs-sm);
  color: var(--text-2);
}

.order-id {
  font-weight: 500;
  color: var(--text-1);
}

.order-status {
  padding: var(--spacing-xs) var(--spacing-sm);
  border-radius: var(--radius-sm);
  font-size: var(--fs-xs);
  font-weight: 500;
}

.order-status.pending {
  background-color: #fff3cd;
  color: #856404;
}

.order-status.refunded {
  background-color: #f8d7da;
  color: #721c24;
}

.order-status.shipped {
  background-color: #cce7ff;
  color: #0066cc;
}

.order-status.delivered {
  background-color: #e8f5e8;
  color: #2e7d32;
}

.order-status.reviewed {
  background-color: #d1ecf1;
  color: #0c5460;
}

.order-items {
  padding: var(--spacing-lg);
  border-bottom: 1px solid var(--border);
}

.order-item {
  display: flex;
  gap: var(--spacing-lg);
  padding: var(--spacing-base) 0;
  border-bottom: 1px solid var(--border-light);
}

.order-item:last-child {
  border-bottom: none;
}

.order-shipping {
  padding: var(--spacing-lg);
  border-bottom: 1px solid var(--border);
  background-color: #f9f9f9;
}

.order-shipping h4 {
  margin: 0 0 var(--spacing-sm) 0;
  font-size: var(--fs-sm);
  font-weight: 600;
  color: var(--text-1);
}

.shipping-info {
  margin: 0 0 var(--spacing-xs) 0;
  font-size: var(--fs-sm);
  color: var(--text-1);
  font-weight: 500;
}

.shipping-address {
  margin: 0;
  font-size: var(--fs-sm);
  color: var(--text-2);
  line-height: 1.4;
}

.loading-state {
  text-align: center;
  padding: var(--spacing-3xl) 0;
  background: var(--card);
  border-radius: var(--radius-base);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid var(--primary);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto var(--spacing-lg);
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.item-image {
  width: 80px;
  height: 80px;
  border-radius: var(--radius-sm);
  overflow: hidden;
  flex-shrink: 0;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.item-name {
  font-size: var(--fs-base);
  font-weight: 500;
  color: var(--text-1);
  margin: 0 0 var(--spacing-xs) 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.item-spec {
  font-size: var(--fs-xs);
  color: var(--text-3);
  margin: 0 0 var(--spacing-xs) 0;
}

.item-price-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.item-price {
  font-size: var(--fs-base);
  font-weight: 600;
  color: var(--danger);
}

.item-quantity {
  font-size: var(--fs-sm);
  color: var(--text-3);
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-lg);
}

.order-total {
  font-size: var(--fs-base);
  color: var(--text-1);
}

.total-price {
  font-size: var(--fs-lg);
  font-weight: 600;
  color: var(--danger);
  margin-left: var(--spacing-sm);
}

.order-actions {
  display: flex;
  gap: var(--spacing-sm);
}

.btn-sm {
  padding: var(--spacing-xs) var(--spacing-base);
  font-size: var(--fs-sm);
}

.empty-state {
  text-align: center;
  padding: var(--spacing-3xl) 0;
  background: var(--card);
  border-radius: var(--radius-base);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.empty-icon {
  font-size: 48px;
  margin-bottom: var(--spacing-lg);
}

.empty-state h3 {
  color: var(--text-1);
  font-size: var(--fs-lg);
  font-weight: 600;
  margin: 0 0 var(--spacing-sm) 0;
}

.empty-state p {
  color: var(--text-2);
  font-size: var(--fs-base);
  margin: 0 0 var(--spacing-xl) 0;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: var(--spacing-lg);
  margin-top: var(--spacing-xl);
}

.page-btn {
  padding: var(--spacing-sm) var(--spacing-lg);
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  background-color: white;
  color: var(--text-1);
  font-size: var(--fs-sm);
  cursor: pointer;
  transition: all 0.3s ease;
}

.page-btn:hover:not(:disabled) {
  border-color: var(--primary);
  color: var(--primary);
}

.page-btn:disabled {
  background-color: var(--bg);
  color: var(--text-3);
  cursor: not-allowed;
}

.page-info {
  font-size: var(--fs-sm);
  color: var(--text-2);
}

@media (max-width: 768px) {
  .orders-content {
    padding: var(--spacing-xl) 0;
  }
  
  .order-filters {
    justify-content: center;
  }
  
  .order-header {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-sm);
  }
  
  .order-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-lg);
  }
  
  .order-actions {
    width: 100%;
    justify-content: space-between;
  }
  
  .item-name {
    font-size: var(--fs-sm);
  }
  
  .total-price {
    font-size: var(--fs-base);
  }
}

/* 订单详情弹窗样式 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: var(--radius-base);
  width: 100%;
  max-width: 800px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-lg);
  border-bottom: 1px solid var(--border);
  background-color: var(--card);
}

.modal-header h3 {
  color: var(--text-1);
  font-size: var(--fs-lg);
  font-weight: 600;
  margin: 0;
}

.modal-close {
  background: none;
  border: none;
  font-size: var(--fs-xl);
  cursor: pointer;
  color: var(--text-3);
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius-full);
  transition: all 0.3s ease;
}

.modal-close:hover {
  background-color: var(--hover-bg);
  color: var(--text-1);
}

.modal-body {
  padding: var(--spacing-lg);
}

.order-detail {
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

.product-info {
  display: flex;
  gap: var(--spacing-lg);
  align-items: flex-start;
}

.product-image {
  flex-shrink: 0;
  width: 100px;
  height: 100px;
  border-radius: var(--radius-base);
  overflow: hidden;
  border: 1px solid var(--border);
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
}

.product-details h5 {
  margin: 0 0 var(--spacing-sm) 0;
  color: var(--text-1);
  font-size: var(--fs-base);
  font-weight: 600;
}

.product-details p {
  margin: 0;
  font-size: var(--fs-sm);
  color: var(--text-2);
}

.shipping-info-full {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
}

.shipping-info-full p {
  margin: 0;
  font-size: var(--fs-sm);
  color: var(--text-1);
  line-height: 1.4;
}

.amount-info {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}

.amount-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-sm) 0;
  border-bottom: 1px solid var(--border-light);
}

.amount-item:last-child {
  border-bottom: none;
}

.amount-item.total {
  border-top: 1px solid var(--border);
  margin-top: var(--spacing-sm);
  padding-top: var(--spacing-sm);
  font-weight: 600;
}

.amount-label {
  font-size: var(--fs-sm);
  color: var(--text-2);
}

.amount-value {
  font-size: var(--fs-sm);
  color: var(--text-1);
}

.amount-value.total {
  color: var(--danger);
  font-size: var(--fs-base);
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  padding: var(--spacing-lg);
  border-top: 1px solid var(--border);
  background-color: var(--card);
}

/* 评价弹窗样式 */
.form-group {
  margin-bottom: var(--spacing-lg);
}

.form-group label {
  display: block;
  margin-bottom: var(--spacing-sm);
  font-weight: 500;
  color: var(--text-1);
}

.form-group textarea {
  width: 100%;
  padding: var(--spacing-base);
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: var(--fs-base);
  font-family: inherit;
  resize: vertical;
  transition: all 0.3s ease;
}

.form-group textarea:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.rating {
  display: flex;
  gap: var(--spacing-xs);
}

.star-btn {
  background: none;
  border: none;
  font-size: var(--fs-xl);
  color: #ccc;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: var(--spacing-xs);
}

.star-btn:hover {
  color: #ffd700;
}

.star-btn.active {
  color: #ffd700;
}

/* 订单详情加载状态 */
.detail-loading-state {
  text-align: center;
  padding: var(--spacing-3xl) 0;
}

.detail-loading-state p {
  color: var(--text-2);
  margin-top: var(--spacing-lg);
}

/* 订单详情错误状态 */
.detail-error-state {
  text-align: center;
  padding: var(--spacing-xl);
  background-color: #fff3cd;
  border-radius: var(--radius-base);
  margin-bottom: var(--spacing-lg);
}

.error-icon {
  font-size: 32px;
  margin-bottom: var(--spacing-sm);
}

.error-message {
  color: #856404;
  font-weight: 500;
  margin: 0 0 var(--spacing-xs) 0;
}

.error-hint {
  color: #856404;
  font-size: var(--fs-sm);
  margin: 0;
  opacity: 0.8;
}

/* 状态标签 */
.status-badge {
  display: inline-block;
  padding: var(--spacing-xs) var(--spacing-sm);
  border-radius: var(--radius-sm);
  font-size: var(--fs-xs);
  font-weight: 500;
}

.status-badge.pending {
  background-color: #fff3cd;
  color: #856404;
}

.status-badge.refunded {
  background-color: #f8d7da;
  color: #721c24;
}

.status-badge.shipped {
  background-color: #cce7ff;
  color: #0066cc;
}

.status-badge.delivered {
  background-color: #e8f5e8;
  color: #2e7d32;
}

.status-badge.reviewed {
  background-color: #d1ecf1;
  color: #0c5460;
}

/* 多商品列表样式 */
.order-items-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-base);
}

.order-item-detail {
  display: flex;
  gap: var(--spacing-base);
  padding: var(--spacing-base);
  background-color: #f9f9f9;
  border-radius: var(--radius-base);
  border: 1px solid var(--border-light);
}

.item-image-small {
  width: 80px;
  height: 80px;
  border-radius: var(--radius-sm);
  overflow: hidden;
  flex-shrink: 0;
}

.item-image-small img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-info-small {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
}

.item-info-small h5 {
  margin: 0;
  color: var(--text-1);
  font-size: var(--fs-sm);
  font-weight: 600;
}

.item-meta {
  margin: 0;
  font-size: var(--fs-xs);
  color: var(--text-2);
}

.item-price {
  margin: var(--spacing-xs) 0 0 0;
  font-size: var(--fs-sm);
  font-weight: 600;
  color: var(--danger);
}

.product-content {
  margin-top: var(--spacing-sm);
  padding-top: var(--spacing-sm);
  border-top: 1px dashed var(--border);
  font-size: var(--fs-xs);
  color: var(--text-2);
  line-height: 1.5;
}

@media (max-width: 768px) {
  .orders-content {
    padding: var(--spacing-xl) 0;
  }

  .order-card {
    margin-bottom: var(--spacing-lg);
  }

  .order-header {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-sm);
  }

  .order-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-sm);
  }

  .order-actions {
    width: 100%;
    justify-content: space-between;
  }

  .order-filters {
    overflow-x: auto;
    padding-bottom: var(--spacing-sm);
  }

  .filter-btn {
    white-space: nowrap;
  }

  .modal-content {
    margin: 0 var(--spacing-base);
    max-height: 95vh;
  }

  .product-info {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .product-image {
    margin: 0 auto var(--spacing-lg);
  }

  .detail-grid {
    grid-template-columns: 1fr;
  }

  .order-item-detail {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .item-image-small {
    margin-bottom: var(--spacing-sm);
  }
}
</style>