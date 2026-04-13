<template>
  <div class="order-confirm-page">
    <Header />
    <main class="order-confirm-content">
      <div class="container">
        <!-- 页面标题 -->
        <div class="page-header">
          <h1>确认订单</h1>
          <p>请确认订单信息</p>
        </div>

        <!-- 加载中 -->
        <div v-if="loading" class="loading">
          <div class="loading-spinner"></div>
          <p>加载中...</p>
        </div>

        <!-- 支付状态 -->
        <div v-if="showPayStatus" class="pay-status">
          <div class="pay-status-content">
            <div class="pay-status-icon" :class="payStatusIconClass">
              {{ payStatusIcon }}
            </div>
            <h3>{{ payStatusTitle }}</h3>
            <p>{{ payStatusMessage }}</p>
            <div class="pay-status-actions">
              <button v-if="payStatus === 'success'" class="btn btn-primary" @click="goToOrders">查看订单</button>
              <button v-else-if="payStatus === 'error'" class="btn btn-primary" @click="retryPay">重新支付</button>
              <button v-else-if="payStatus === 'cancel'" class="btn btn-secondary" @click="goToCart">返回购物车</button>
            </div>
          </div>
        </div>

        <div v-else>
          <!-- 地址选择 -->
          <div class="section">
            <h2 class="section-title">收货地址</h2>
            <div v-if="addresses.length > 0" class="address-list">
              <div v-for="address in addresses" :key="address.id" class="address-item" :class="{ active: selectedAddressId === address.id }" @click="selectAddress(address.id)">
                <div class="address-info">
                  <div class="address-header">
                    <span class="address-name">{{ address.addressName }}</span>
                    <span class="address-phone">{{ address.addressPhone }}</span>
                    <span v-if="address.isdefaultValue === '是'" class="address-default">默认</span>
                  </div>
                  <div class="address-detail">{{ address.addressDizhi }}</div>
                </div>
                <div class="address-select">
                  <input type="radio" :name="'address'" :checked="selectedAddressId === address.id" @change="selectAddress(address.id)">
                </div>
              </div>
            </div>
            <div v-else class="no-address">
              <p>暂无收货地址</p>
              <router-link to="/user/address" class="btn btn-primary">添加地址</router-link>
            </div>
          </div>

          <!-- 商品信息 -->
          <div class="section">
            <h2 class="section-title">商品信息</h2>
            <div class="product-list">
              <div v-for="item in selectedItems" :key="item.id" class="product-item">
                <div class="product-image">
                  <img :src="'http://localhost:8080/wangshangchongwudian/' + item.chongwuyongpinPhoto" :alt="item.chongwuyongpinName">
                </div>
                <div class="product-info">
                  <h3 class="product-name">{{ item.chongwuyongpinName }}</h3>
                  <div class="product-price-row">
                    <span class="product-price">¥{{ item.chongwuyongpinNewMoney }}</span>
                    <span class="product-quantity">x{{ item.buyNumber }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 支付方式 -->
          <div class="section">
            <h2 class="section-title">支付方式</h2>
            <div class="payment-list">
              <div class="payment-item" :class="{ active: paymentType === 1 }" @click="selectPayment(1)">
                <div class="payment-info">
                  <span class="payment-name">微信支付</span>
                </div>
                <div class="payment-select">
                  <input type="radio" name="payment" :checked="paymentType === 1" @change="selectPayment(1)">
                </div>
              </div>
              <div class="payment-item" :class="{ active: paymentType === 2 }" @click="selectPayment(2)">
                <div class="payment-info">
                  <span class="payment-name">支付宝</span>
                </div>
                <div class="payment-select">
                  <input type="radio" name="payment" :checked="paymentType === 2" @change="selectPayment(2)">
                </div>
              </div>
            </div>
          </div>

          <!-- 订单金额 -->
          <div class="section">
            <h2 class="section-title">订单金额</h2>
            <div class="order-amount">
              <div class="amount-item">
                <span class="amount-label">商品金额</span>
                <span class="amount-value">¥{{ subtotal.toFixed(2) }}</span>
              </div>
              <div class="amount-item">
                <span class="amount-label">运费</span>
                <span class="amount-value">¥{{ shippingFee.toFixed(2) }}</span>
              </div>
              <div class="amount-item total">
                <span class="amount-label">总计</span>
                <span class="amount-value total-price">¥{{ totalAmount.toFixed(2) }}</span>
              </div>
            </div>
          </div>

          <!-- 提交订单 -->
          <div class="submit-section">
            <div class="submit-total">
              <span class="submit-label">合计：</span>
              <span class="submit-price">¥{{ totalAmount.toFixed(2) }}</span>
            </div>
            <button class="btn btn-primary submit-btn" @click="submitOrder" :disabled="!selectedAddressId">
              提交订单
            </button>
          </div>
        </div>
      </div>
    </main>
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { addressApi, chongwuyongpinOrderApi, cartApi, usersApi } from '../../utils/api'
import message from '../../utils/message'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'

const router = useRouter()
const route = useRoute()
const loading = ref(false)
const addresses = ref([])
const selectedAddressId = ref(null)
const paymentType = ref(1)
const selectedItems = ref([])

// 支付状态
const showPayStatus = ref(false)
const payStatus = ref('') // success, error, cancel
const payStatusTitle = ref('')
const payStatusMessage = ref('')
const payStatusIcon = ref('')
const payStatusIconClass = ref('')

// 计算商品金额
const subtotal = computed(() => {
  return selectedItems.value.reduce((total, item) => {
    return total + (item.chongwuyongpinNewMoney * item.buyNumber)
  }, 0)
})

// 运费
const shippingFee = ref(0)

// 总金额
const totalAmount = computed(() => {
  return subtotal.value + shippingFee.value
})

// 加载地址列表
const loadAddresses = async () => {
  try {
    // 获取用户会话信息
    const sessionResponse = await usersApi.session()
    if (sessionResponse.code === 0 && sessionResponse.data) {
      const yonghuId = sessionResponse.data.yonghuId || sessionResponse.data.id
      
      // 调用地址API，传递用户id
      const response = await addressApi.getList({ yonghuId, t: Date.now() })
      if (response.code === 0) {
        addresses.value = response.data.list || []
        // 默认选择默认地址
        const defaultAddress = addresses.value.find(addr => addr.isdefaultValue === '是')
        if (defaultAddress) {
          selectedAddressId.value = defaultAddress.id
        } else if (addresses.value.length > 0) {
          // 没有默认地址则选择第一个
          selectedAddressId.value = addresses.value[0].id
        }
      }
    } else {
      message.error('获取用户信息失败')
    }
  } catch (error) {
    console.error('获取地址列表失败:', error)
    message.error('获取地址列表失败')
  }
}

// 加载选中的商品
const loadSelectedItems = () => {
  // 从路由参数中获取选中的商品信息
  const selectedItemsStr = route.query.selectedItems
  if (selectedItemsStr) {
    try {
      selectedItems.value = JSON.parse(selectedItemsStr)
      if (selectedItems.value.length === 0) {
        message.error('请选择要购买的商品')
        router.push('/cart')
      }
    } catch (error) {
      console.error('解析商品信息失败:', error)
      message.error('商品信息有误')
      router.push('/cart')
    }
  } else {
    // 如果没有传递商品信息，从购物车获取
    message.error('请选择要购买的商品')
    router.push('/cart')
  }
}

// 选择地址
const selectAddress = (addressId) => {
  selectedAddressId.value = addressId
}

// 选择支付方式
const selectPayment = (type) => {
  paymentType.value = type
}

// 设置支付状态
const setPayStatus = (status, title, message, icon) => {
  payStatus.value = status
  payStatusTitle.value = title
  payStatusMessage.value = message
  payStatusIcon.value = icon
  
  // 设置图标类名
  switch (status) {
    case 'success':
      payStatusIconClass.value = 'success'
      break
    case 'error':
      payStatusIconClass.value = 'error'
      break
    case 'cancel':
      payStatusIconClass.value = 'cancel'
      break
    default:
      payStatusIconClass.value = ''
  }
  
  showPayStatus.value = true
}

// 跳转到订单页面
const goToOrders = () => {
  router.push('/user/orders')
}

// 重新支付
const retryPay = () => {
  showPayStatus.value = false
}

// 返回购物车
const goToCart = () => {
  router.push('/user/cart')
}

// 提交订单
const submitOrder = async () => {
  if (!selectedAddressId.value) {
    message.error('请选择收货地址')
    return
  }

  try {
    loading.value = true
    
    // 获取用户会话信息
    const sessionResponse = await usersApi.session()
    if (sessionResponse.code === 0 && sessionResponse.data) {
      const yonghuId = sessionResponse.data.yonghuId || sessionResponse.data.id
      
      // 构建订单参数
      const params = {
        addressId: selectedAddressId.value,
        buyNumber: selectedItems.value.reduce((total, item) => total + item.buyNumber, 0),
        chongwuyongpinId: selectedItems.value.length > 0 ? selectedItems.value[0].chongwuyongpinId : null,
        chongwuyongpins: JSON.stringify(selectedItems.value),
        yonghuId,
        chongwuyongpinOrderPaymentTypes: paymentType.value,
        t: Date.now()
      }

      // 调用创建订单接口
      const response = await chongwuyongpinOrderApi.order(params)
      if (response.code === 0) {
        // 清空购物车中已购买的商品
        const selectedIds = selectedItems.value.map(item => item.id)
        await cartApi.delete(selectedIds)
        
        // 生成订单号
        const orderNo = String(Date.now())
        const amount = totalAmount.value
        const body = selectedItems.value.map(item => item.chongwuyongpinName).join(', ')
        
        // 根据支付方式调用相应的支付接口
        if (paymentType.value === 1) {
          // 微信支付
          try {
            const wechatPayResponse = await chongwuyongpinOrderApi.wechatPay({
              orderNo,
              amount: Math.round(amount * 100), // 转换为分
              body,
              openid: 'test_openid' // 实际应该从用户信息中获取
            })
            
            if (wechatPayResponse.code === 0) {
              // 调用微信支付JSAPI
              if (typeof WeixinJSBridge !== 'undefined') {
                WeixinJSBridge.invoke('getBrandWCPayRequest', wechatPayResponse.data, function(res) {
                  if (res.err_msg === 'get_brand_wcpay_request:ok') {
                    setPayStatus('success', '支付成功', '您的订单已支付成功，正在跳转至订单页面...', '✅')
                    setTimeout(() => {
                      router.push('/user/orders')
                    }, 2000)
                  } else if (res.err_msg === 'get_brand_wcpay_request:cancel') {
                    setPayStatus('cancel', '支付已取消', '您已取消支付，可重新尝试支付或返回购物车', '⏹️')
                  } else {
                    setPayStatus('error', '支付失败', '支付失败，请检查网络连接后重新尝试', '❌')
                  }
                })
              } else {
                setPayStatus('error', '支付失败', '请在微信浏览器中打开进行支付', '❌')
              }
            } else {
              setPayStatus('error', '支付下单失败', '微信支付下单失败：' + (wechatPayResponse.msg || '未知错误'), '❌')
            }
          } catch (error) {
            console.error('微信支付异常:', error)
            setPayStatus('error', '支付异常', '微信支付异常，请检查网络连接后重试', '❌')
          }
        } else if (paymentType.value === 2) {
          // 支付宝支付
          try {
            const alipayResponse = await chongwuyongpinOrderApi.alipay({
              orderNo,
              amount,
              subject: '宠物商品订单',
              body
            })
            
            if (alipayResponse.code === 0) {
              // 跳转到支付宝支付页面
              document.write(alipayResponse.data)
              document.close()
            } else {
              setPayStatus('error', '支付下单失败', '支付宝支付下单失败：' + (alipayResponse.msg || '未知错误'), '❌')
            }
          } catch (error) {
            console.error('支付宝支付异常:', error)
            setPayStatus('error', '支付异常', '支付宝支付异常，请检查网络连接后重试', '❌')
          }
        }
      } else {
        message.error('订单创建失败')
      }
    } else {
      message.error('获取用户信息失败')
    }
  } catch (error) {
    console.error('订单创建失败:', error)
    message.error('订单创建失败')
  } finally {
    loading.value = false
  }
}

// 页面加载时获取数据
onMounted(() => {
  loadAddresses()
  loadSelectedItems()
})
</script>

<style scoped>
.order-confirm-page {
  min-height: 100vh;
  background-color: var(--bg);
}

.order-confirm-content {
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

.section {
  background: var(--card);
  border-radius: var(--radius-base);
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  margin-bottom: var(--spacing-xl);
  padding: var(--spacing-lg);
}

.section-title {
  color: var(--text-1);
  font-size: var(--fs-lg);
  font-weight: 600;
  margin-bottom: var(--spacing-lg);
  padding-bottom: var(--spacing-base);
  border-bottom: 1px solid var(--border);
}

.address-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-base);
}

.address-item {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  padding: var(--spacing-base);
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  cursor: pointer;
  transition: all 0.3s ease;
}

.address-item:hover {
  border-color: var(--primary);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.address-item.active {
  border-color: var(--primary);
  background-color: rgba(0, 122, 255, 0.05);
}

.address-info {
  flex: 1;
  margin-right: var(--spacing-base);
}

.address-header {
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
  margin-bottom: var(--spacing-sm);
}

.address-name {
  font-weight: 500;
  color: var(--text-1);
  font-size: var(--fs-base);
}

.address-phone {
  color: var(--text-2);
  font-size: var(--fs-base);
}

.address-default {
  padding: var(--spacing-xs) var(--spacing-sm);
  background-color: var(--primary);
  color: white;
  border-radius: var(--radius-sm);
  font-size: var(--fs-xs);
}

.address-detail {
  color: var(--text-2);
  font-size: var(--fs-sm);
  line-height: 1.5;
}

.address-select {
  display: flex;
  align-items: center;
}

.no-address {
  text-align: center;
  padding: var(--spacing-xl) 0;
  color: var(--text-3);
  font-size: var(--fs-base);
}

.no-address .btn {
  margin-top: var(--spacing-lg);
}

.product-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-base);
}

.product-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-base);
  padding: var(--spacing-base);
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
}

.product-image {
  flex-shrink: 0;
}

.product-image img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: var(--radius-base);
}

.product-info {
  flex: 1;
}

.product-name {
  font-size: var(--fs-base);
  font-weight: 500;
  color: var(--text-1);
  margin-bottom: var(--spacing-sm);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-price-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.product-price {
  color: var(--danger);
  font-size: var(--fs-base);
  font-weight: 600;
}

.product-quantity {
  color: var(--text-2);
  font-size: var(--fs-sm);
}

.payment-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-base);
}

.payment-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--spacing-base);
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  cursor: pointer;
  transition: all 0.3s ease;
}

.payment-item:hover {
  border-color: var(--primary);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.payment-item.active {
  border-color: var(--primary);
  background-color: rgba(0, 122, 255, 0.05);
}

.payment-info {
  flex: 1;
}

.payment-name {
  font-size: var(--fs-base);
  color: var(--text-1);
}

.payment-select {
  display: flex;
  align-items: center;
}

.order-amount {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-base);
}

.amount-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: var(--fs-base);
}

.amount-label {
  color: var(--text-2);
}

.amount-value {
  color: var(--text-1);
}

.amount-item.total {
  padding-top: var(--spacing-base);
  border-top: 1px solid var(--border);
  font-weight: 500;
}

.total-price {
  color: var(--danger);
  font-size: var(--fs-lg);
  font-weight: 600;
}

.submit-section {
  background: var(--card);
  border-radius: var(--radius-base);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  padding: var(--spacing-lg);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.submit-total {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.submit-label {
  font-size: var(--fs-base);
  color: var(--text-1);
}

.submit-price {
  font-size: var(--fs-xl);
  font-weight: 600;
  color: var(--danger);
}

.submit-btn {
  font-size: var(--fs-base);
  padding: var(--spacing-base) var(--spacing-xl);
}

@media (max-width: 768px) {
  .section {
    padding: var(--spacing-base);
  }
  
  .submit-section {
    flex-direction: column;
    align-items: stretch;
    gap: var(--spacing-base);
  }
  
  .submit-total {
    justify-content: space-between;
  }
  
  .submit-btn {
    width: 100%;
  }
}

/* 支付状态样式 */
.pay-status {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: var(--spacing-3xl) 0;
}

.pay-status-content {
  background: var(--card);
  border-radius: var(--radius-base);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  padding: var(--spacing-2xl);
  text-align: center;
  max-width: 500px;
  width: 100%;
}

.pay-status-icon {
  font-size: 48px;
  margin-bottom: var(--spacing-lg);
  padding: var(--spacing-lg);
  border-radius: 50%;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 100px;
  height: 100px;
}

.pay-status-icon.success {
  background-color: #e8f5e8;
  color: #2e7d32;
}

.pay-status-icon.error {
  background-color: #f8d7da;
  color: #721c24;
}

.pay-status-icon.cancel {
  background-color: #fff3cd;
  color: #856404;
}

.pay-status-content h3 {
  color: var(--text-1);
  font-size: var(--fs-xl);
  font-weight: 600;
  margin: 0 0 var(--spacing-base) 0;
}

.pay-status-content p {
  color: var(--text-2);
  font-size: var(--fs-base);
  margin: 0 0 var(--spacing-xl) 0;
  line-height: 1.5;
}

.pay-status-actions {
  display: flex;
  gap: var(--spacing-base);
  justify-content: center;
}

@media (max-width: 768px) {
  .pay-status-content {
    padding: var(--spacing-xl);
    margin: 0 var(--spacing-base);
  }
  
  .pay-status-icon {
    font-size: 32px;
    width: 80px;
    height: 80px;
  }
  
  .pay-status-actions {
    flex-direction: column;
  }
  
  .pay-status-actions .btn {
    width: 100%;
  }
}
</style>