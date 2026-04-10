<template>
  <div class="profile-page">
    <Header />
    <main class="profile-content">
      <div class="container">
        <div class="profile-header">
          <h1>个人中心</h1>
          <p>管理您的个人信息和账户设置</p>
        </div>

        <div class="profile-grid">
          <!-- 个人信息卡片 -->
          <div class="profile-card">
            <div class="card-header">
              <h2>个人信息</h2>
              <button class="btn btn-sm btn-secondary" @click="isEditing = !isEditing">
                {{ isEditing ? '取消' : '编辑' }}
              </button>
            </div>

            <div class="profile-info">
              <div class="avatar-section">
                <div class="avatar-container">
                  <img v-if="userInfo.yonghuPhoto"
                    :src="'http://localhost:8080/wangshangchongwudian/' + userInfo.yonghuPhoto"
                    :alt="userInfo.yonghuName" style="max-width: 100px; max-height: 100px;">
                  <div v-else class="avatar-placeholder">
                    {{ userInfo.yonghuName?.charAt(0) || '用' }}
                  </div>
                </div>
                <div class="avatar-actions">
                  <input type="file" id="avatar-upload" class="avatar-upload" @change="handleAvatarUpload"
                    v-if="isEditing">
                  <label for="avatar-upload" class="btn btn-sm btn-primary" v-if="isEditing">
                    更换头像
                  </label>
                </div>
              </div>

              <div class="info-form">
                <div class="form-row">
                  <div class="form-group">
                    <label>用户名</label>
                    <input type="text" v-model="form.username" :disabled="!isEditing">
                  </div>
                  <div class="form-group">
                    <label>姓名</label>
                    <input type="text" v-model="form.yonghuName" :disabled="!isEditing">
                  </div>
                </div>

                <div class="form-row">
                  <div class="form-group">
                    <label>手机号</label>
                    <input type="tel" v-model="form.yonghuPhone" :disabled="!isEditing">
                  </div>
                  <div class="form-group">
                    <label>邮箱</label>
                    <input type="email" v-model="form.yonghuEmail" :disabled="!isEditing">
                  </div>
                </div>

                <div class="form-row">
                  <div class="form-group">
                    <label>身份证号</label>
                    <input type="text" v-model="form.yonghuIdNumber" :disabled="!isEditing">
                  </div>
                  <div class="form-group">
                    <label>性别</label>
                    <select v-model="form.sexTypes" :disabled="!isEditing">
                      <option value="1">男</option>
                      <option value="2">女</option>
                    </select>
                  </div>
                </div>

                <div class="form-row">
                  <div class="form-group">
                    <label>余额</label>
                    <input type="number" v-model="form.newMoney" disabled>
                  </div>
                  <div class="form-group">
                    <label>注册时间</label>
                    <input type="text" :value="form.createTime" disabled>
                  </div>
                </div>

                <div class="form-actions" v-if="isEditing">
                  <button class="btn btn-primary" @click="saveProfile">保存</button>
                </div>
              </div>
            </div>
          </div>

          <!-- 账户安全卡片 -->
          <div class="profile-card">
            <div class="card-header">
              <h2>账户安全</h2>
            </div>

            <div class="security-options">
              <div class="security-item">
                <div class="security-info">
                  <h3>修改密码</h3>
                  <p>定期更新密码以保护账户安全</p>
                </div>
                <button class="btn btn-sm btn-secondary" @click="showChangePassword = true">
                  修改
                </button>
              </div>

              <div class="security-item">
                <div class="security-info">
                  <h3>绑定手机号</h3>
                  <p>{{ userInfo.yonghuPhone }}</p>
                </div>
                <button class="btn btn-sm btn-secondary" @click="openBindPhoneModal">
                  绑定
                </button>
              </div>
              
              <div class="security-item">
                <div class="security-info">
                  <h3>绑定邮箱</h3>
                  <p>{{ userInfo.yonghuEmail || '未绑定' }}</p>
                </div>
                <button class="btn btn-sm btn-secondary" @click="openBindEmailModal">
                  绑定
                </button>
              </div>
              
              <div class="security-item">
                <div class="security-info">
                  <h3>账户余额</h3>
                  <p>¥{{ userInfo.newMoney || 0 }}</p>
                </div>
                <button class="btn btn-sm btn-primary" @click="showRechargeModal = true">
                  充值
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
    <Footer />

    <!-- 修改密码弹窗 -->
    <div class="modal" v-if="showChangePassword">
      <div class="modal-content">
        <div class="modal-header">
          <h3>修改密码</h3>
          <button class="modal-close" @click="showChangePassword = false">×</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>原密码</label>
            <input 
              type="password" 
              v-model="passwordForm.oldPassword" 
              placeholder="请输入原密码"
            >
          </div>
          <div class="form-group">
            <label>新密码</label>
            <input 
              type="password" 
              v-model="passwordForm.newPassword" 
              placeholder="请输入新密码"
            >
          </div>
          <div class="form-group">
            <label>确认新密码</label>
            <input 
              type="password" 
              v-model="passwordForm.confirmPassword" 
              placeholder="请确认新密码"
            >
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showChangePassword = false">取消</button>
          <button class="btn btn-primary" @click="changePassword">确认修改</button>
        </div>
      </div>
    </div>
    
    <!-- 绑定手机号弹窗 -->
    <div class="modal" v-if="showBindPhone">
      <div class="modal-content">
        <div class="modal-header">
          <h3>绑定手机号</h3>
          <button class="modal-close" @click="showBindPhone = false">×</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>手机号</label>
            <input 
              type="tel" 
              v-model="phoneForm.phone" 
              placeholder="请输入手机号"
            >
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showBindPhone = false">取消</button>
          <button class="btn btn-primary" @click="bindPhone">确认绑定</button>
        </div>
      </div>
    </div>
    
    <!-- 绑定邮箱弹窗 -->
    <div class="modal" v-if="showBindEmail">
      <div class="modal-content">
        <div class="modal-header">
          <h3>绑定邮箱</h3>
          <button class="modal-close" @click="showBindEmail = false">×</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>邮箱</label>
            <input 
              type="email" 
              v-model="emailForm.email" 
              placeholder="请输入邮箱"
            >
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showBindEmail = false">取消</button>
          <button class="btn btn-primary" @click="bindEmail">确认绑定</button>
        </div>
      </div>
    </div>
    
    <!-- 充值弹窗 -->
    <div class="modal recharge-modal" v-if="showRechargeModal">
      <div class="modal-content recharge-content" :class="{ 'recharging': isRecharging, 'recharge-success': rechargeSuccess }">
        <!-- 充值表单 -->
        <div v-if="!isRecharging && !rechargeSuccess" class="recharge-form">
          <div class="modal-header">
            <h3>账户充值</h3>
            <button class="modal-close" @click="closeRechargeModal">×</button>
          </div>
          <div class="modal-body">
            <!-- 余额显示 -->
            <div class="balance-info">
              <p class="balance-label">当前余额</p>
              <p class="balance-amount">¥{{ userInfo.newMoney || 0 }}</p>
            </div>
            
            <!-- 充值金额选择 -->
            <div class="amount-section">
              <h4>选择金额</h4>
              <div class="amount-options">
                <button 
                  v-for="amount in presetAmounts" 
                  :key="amount"
                  class="amount-btn" 
                  :class="{ active: rechargeForm.amount === amount }"
                  @click="rechargeForm.amount = amount"
                >
                  ¥{{ amount }}
                </button>
                <div class="custom-amount">
                  <input 
                    type="number" 
                    v-model.number="rechargeForm.amount" 
                    placeholder="自定义金额"
                    min="1"
                  >
                </div>
              </div>
            </div>
            
            <!-- 支付方式选择 -->
            <div class="payment-section">
              <h4>支付方式</h4>
              <div class="payment-options">
                <div 
                  v-for="method in paymentMethods" 
                  :key="method.id"
                  class="payment-method" 
                  :class="{ active: rechargeForm.paymentMethod === method.id }"
                  @click="rechargeForm.paymentMethod = method.id"
                >
                  <div class="payment-icon">{{ method.icon }}</div>
                  <div class="payment-name">{{ method.name }}</div>
                  <div class="payment-check" v-if="rechargeForm.paymentMethod === method.id">✓</div>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="closeRechargeModal">取消</button>
            <button class="btn btn-primary" @click="handleRecharge" :disabled="!rechargeForm.amount || isRecharging">
              {{ isRecharging ? '充值中...' : '确认充值' }}
            </button>
          </div>
        </div>
        
        <!-- 充值中动画 -->
        <div v-else-if="isRecharging" class="recharge-loading">
          <div class="loading-spinner"></div>
          <p class="loading-text">充值中，请稍候...</p>
        </div>
        
        <!-- 充值成功 -->
        <div v-else-if="rechargeSuccess" class="recharge-success-content">
          <div class="success-icon">✅</div>
          <h3>充值成功</h3>
          <p class="success-amount">已充值 ¥{{ rechargeForm.amount }}</p>
          <p class="success-balance">当前余额 ¥{{ rechargeResult.balance }}</p>
          <button class="btn btn-primary" @click="closeRechargeModal">确定</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { yonghuApi, fileApi } from '../../utils/api'
import message from '../../utils/message'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'

const router = useRouter()
const userInfo = ref({})
const form = ref({})
const isEditing = ref(false)
const showChangePassword = ref(false)
const showBindPhone = ref(false)
const showBindEmail = ref(false)

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const phoneForm = ref({
  phone: ''
})

const emailForm = ref({
  email: ''
})

// 充值相关数据
const showRechargeModal = ref(false)
const isRecharging = ref(false)
const rechargeSuccess = ref(false)
const rechargeForm = ref({
  amount: 0,
  paymentMethod: 'wechat'
})
const presetAmounts = [10, 50, 100, 200, 500, 1000]
const paymentMethods = [
  { id: 'wechat', name: '微信支付', icon: '💳' },
  { id: 'alipay', name: '支付宝', icon: '💰' },
  { id: 'card', name: '银行卡', icon: '💵' }
]
const rechargeResult = ref({
  balance: 0
})

// 获取用户信息
const getUserInfo = async () => {
  try {
    const response = await yonghuApi.session()
    if (response.code === 0) {
      userInfo.value = response.data
      // 初始化表单数据
      form.value = {
        id: response.data.id, // 确保包含id字段
        username: response.data.username,
        yonghuName: response.data.yonghuName,
        yonghuPhone: response.data.yonghuPhone,
        yonghuEmail: response.data.yonghuEmail,
        yonghuIdNumber: response.data.yonghuIdNumber,
        sexTypes: response.data.sexTypes,
        newMoney: response.data.newMoney,
        createTime: response.data.createTime
      }
    } else {
      message.error('获取用户信息失败')
      router.push('/user/login')
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    message.error('获取用户信息失败')
    router.push('/user/login')
  }
}

// 保存个人信息
const saveProfile = async () => {
  try {
    // 构建正确的数据格式
    const updateData = {
      ...form.value,
      t: Date.now() // 添加时间戳
    }
    
    const response = await yonghuApi.update(updateData)
    if (response.code === 0) {
      message.success('保存成功')
      isEditing.value = false
      getUserInfo()
    } else {
      message.error('保存失败：' + (response.msg || '未知错误'))
    }
  } catch (error) {
    console.error('保存失败:', error)
    message.error('保存失败，请稍后重试')
  }
}

// 修改密码
const changePassword = async () => {
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    message.error('两次输入的密码不一致')
    return
  }

  try {
    const response = await yonghuApi.updatePassword(
      passwordForm.value.oldPassword,
      passwordForm.value.newPassword
    )
    if (response.code === 0) {
      message.success('密码修改成功')
      showChangePassword.value = false
      passwordForm.value = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
    } else {
      message.error('密码修改失败：' + (response.msg || '未知错误'))
    }
  } catch (error) {
    console.error('密码修改失败:', error)
    message.error('密码修改失败，请稍后重试')
  }
}

// 处理头像上传
const handleAvatarUpload = async (event) => {
  const file = event.target.files[0]
  if (file) {
    try {
      // 使用fileApi上传文件，type为yonghuPhoto
      const response = await fileApi.upload('yonghuPhoto', file)
      if (response.code === 0) {
        // 拼接头像路径：upload/ + 文件名
        const avatarPath = 'upload/' + response.file
        // 更新表单和用户信息中的头像路径
        form.value.yonghuPhoto = avatarPath
        userInfo.value.yonghuPhoto = avatarPath
        message.success('头像上传成功')
      } else {
        message.error('头像上传失败：' + (response.msg || '未知错误'))
      }
    } catch (error) {
      console.error('头像上传失败:', error)
      message.error('头像上传失败，请稍后重试')
    }
  }
}

// 绑定手机号
const bindPhone = async () => {
  try {
    // 构建更新数据
    const updateData = {
      ...form.value,
      yonghuPhone: phoneForm.value.phone,
      t: Date.now()
    }
    
    const response = await yonghuApi.update(updateData)
    if (response.code === 0) {
      message.success('手机号绑定成功')
      showBindPhone.value = false
      getUserInfo()
    } else {
      message.error('手机号绑定失败：' + (response.msg || '未知错误'))
    }
  } catch (error) {
    console.error('手机号绑定失败:', error)
    message.error('手机号绑定失败，请稍后重试')
  }
}

// 绑定邮箱
const bindEmail = async () => {
  try {
    // 构建更新数据
    const updateData = {
      ...form.value,
      yonghuEmail: emailForm.value.email,
      t: Date.now()
    }
    
    const response = await yonghuApi.update(updateData)
    if (response.code === 0) {
      message.success('邮箱绑定成功')
      showBindEmail.value = false
      getUserInfo()
    } else {
      message.error('邮箱绑定失败：' + (response.msg || '未知错误'))
    }
  } catch (error) {
    console.error('邮箱绑定失败:', error)
    message.error('邮箱绑定失败，请稍后重试')
  }
}

// 打开绑定手机号弹窗
const openBindPhoneModal = () => {
  phoneForm.value.phone = userInfo.value.yonghuPhone || ''
  showBindPhone.value = true
}

// 打开绑定邮箱弹窗
const openBindEmailModal = () => {
  emailForm.value.email = userInfo.value.yonghuEmail || ''
  showBindEmail.value = true
}

// 处理充值
const handleRecharge = async () => {
  if (!rechargeForm.value.amount || rechargeForm.value.amount <= 0) {
    message.error('请输入有效的充值金额')
    return
  }
  
  try {
    isRecharging.value = true
    
    // 计算新余额
    const currentBalance = userInfo.value.newMoney || 0
    const newBalance = currentBalance + rechargeForm.value.amount
    
    // 构建更新数据
    const updateData = {
      ...form.value,
      newMoney: newBalance,
      t: Date.now()
    }
    
    // 模拟支付过程
    await new Promise(resolve => setTimeout(resolve, 2000))
    
    const response = await yonghuApi.update(updateData)
    if (response.code === 0) {
      rechargeSuccess.value = true
      rechargeResult.value.balance = newBalance
      const rechargeAmount = rechargeForm.value.amount // 保存充值金额
      
      // 延迟关闭弹窗
      setTimeout(() => {
        closeRechargeModal()
        getUserInfo()
        // 弹窗关闭后显示充值成功提示
        message.success(`充值成功！已为您的账户充值 ¥${rechargeAmount}`)
      }, 1000)
    } else {
      message.error('充值失败：' + (response.msg || '未知错误'))
      isRecharging.value = false
    }
  } catch (error) {
    console.error('充值失败:', error)
    message.error('充值失败，请稍后重试')
    isRecharging.value = false
  }
}

// 关闭充值弹窗
const closeRechargeModal = () => {
  showRechargeModal.value = false
  isRecharging.value = false
  rechargeSuccess.value = false
  rechargeForm.value = {
    amount: 0,
    paymentMethod: 'wechat'
  }
  rechargeResult.value = {
    balance: 0
  }
}

// 页面加载时获取用户信息
onMounted(() => {
  getUserInfo()
})
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background-color: var(--bg);
}

.profile-content {
  padding: var(--spacing-2xl) 0;
}

.profile-header {
  text-align: center;
  margin-bottom: var(--spacing-2xl);
}

.profile-header h1 {
  color: var(--text-1);
  font-size: var(--fs-2xl);
  font-weight: 600;
  margin-bottom: var(--spacing-sm);
}

.profile-header p {
  color: var(--text-2);
  font-size: var(--fs-base);
  margin: 0;
}

.profile-grid {
  display: grid;  
  grid-template-columns: 1fr;
  gap: var(--spacing-xl);
}

.profile-card {
  background: var(--card);
  border-radius: var(--radius-base);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-xl);
  border-bottom: 1px solid var(--border);
}

.card-header h2 {
  color: var(--text-1);
  font-size: var(--fs-lg);
  font-weight: 600;
  margin: 0;
}

.profile-info {
  padding: var(--spacing-xl);
}

.avatar-section {
  display: flex;
  align-items: center;
  gap: var(--spacing-xl);
  margin-bottom: var(--spacing-xl);
}

.avatar-container {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  border: 3px solid var(--primary-light);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.avatar-container img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, var(--primary) 0%, var(--primary-light) 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--fs-2xl);
  font-weight: 600;
}

.avatar-actions {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}

.avatar-upload {
  display: none;
}

.info-form {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

.form-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: var(--spacing-lg);
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}

.form-group label {
  font-weight: 500;
  color: var(--text-1);
  font-size: var(--fs-sm);
}

.form-group input,
.form-group select {
  padding: var(--spacing-base);
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: var(--fs-base);
  transition: all 0.3s ease;
  background-color: white;
  color: var(--text-1);
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 2px rgba(255, 127, 80, 0.1);
}

.form-group input:disabled {
  background-color: var(--bg);
  color: var(--text-3);
  cursor: not-allowed;
}

.form-actions {
  margin-top: var(--spacing-lg);
  display: flex;
  gap: var(--spacing-base);
}

.security-options {
  padding: var(--spacing-xl);
}

.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-lg) 0;
  border-bottom: 1px solid var(--border);
}

.security-item:last-child {
  border-bottom: none;
}

.security-info h3 {
  color: var(--text-1);
  font-size: var(--fs-base);
  font-weight: 500;
  margin: 0 0 var(--spacing-xs) 0;
}

.security-info p {
  color: var(--text-2);
  font-size: var(--fs-sm);
  margin: 0;
}

.btn-sm {
  padding: var(--spacing-xs) var(--spacing-base);
  font-size: var(--fs-sm);
}

/* 弹窗样式 */
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
  max-width: 400px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  overflow: hidden;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-lg);
  border-bottom: 1px solid var(--border);
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
}

.modal-body {
  padding: var(--spacing-lg);
  display: flex;
  flex-direction: column;
  gap: var(--spacing-base);
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: var(--spacing-base);
  padding: var(--spacing-lg);
  border-top: 1px solid var(--border);
}

/* 充值弹窗样式 */
.recharge-modal {
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.recharge-content {
  width: 100%;
  max-width: 500px;
  border-radius: var(--radius-lg);
  overflow: hidden;
  transition: all 0.3s ease;
}

.recharge-form {
  background: white;
}

.balance-info {
  text-align: center;
  margin-bottom: var(--spacing-xl);
  padding: var(--spacing-xl);
  background: linear-gradient(135deg, var(--primary) 0%, var(--primary-light) 100%);
  border-radius: var(--radius-base);
  color: white;
}

.balance-label {
  font-size: var(--fs-sm);
  margin: 0 0 var(--spacing-xs) 0;
  opacity: 0.9;
}

.balance-amount {
  font-size: var(--fs-2xl);
  font-weight: 600;
  margin: 0;
}

.amount-section {
  margin-bottom: var(--spacing-xl);
}

.amount-section h4 {
  color: var(--text-1);
  font-size: var(--fs-base);
  font-weight: 600;
  margin: 0 0 var(--spacing-lg) 0;
}

.amount-options {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--spacing-base);
}

.amount-btn {
  padding: var(--spacing-base);
  border: 2px solid var(--border);
  border-radius: var(--radius-base);
  background-color: white;
  color: var(--text-1);
  font-size: var(--fs-base);
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.amount-btn:hover {
  border-color: var(--primary);
  color: var(--primary);
  transform: translateY(-2px);
}

.amount-btn.active {
  background-color: var(--primary);
  border-color: var(--primary);
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 140, 56, 0.3);
}

.custom-amount input {
  width: 100%;
  padding: var(--spacing-base);
  border: 2px solid var(--border);
  border-radius: var(--radius-base);
  font-size: var(--fs-base);
  text-align: center;
  transition: all 0.3s ease;
}

.custom-amount input:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 2px rgba(255, 127, 80, 0.1);
}

.payment-section {
  margin-bottom: var(--spacing-xl);
}

.payment-section h4 {
  color: var(--text-1);
  font-size: var(--fs-base);
  font-weight: 600;
  margin: 0 0 var(--spacing-lg) 0;
}

.payment-options {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-base);
}

.payment-method {
  display: flex;
  align-items: center;
  padding: var(--spacing-base);
  border: 2px solid var(--border);
  border-radius: var(--radius-base);
  background-color: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.payment-method:hover {
  border-color: var(--primary);
  transform: translateY(-2px);
}

.payment-method.active {
  border-color: var(--primary);
  background-color: rgba(255, 140, 56, 0.05);
  transform: translateY(-2px);
}

.payment-icon {
  font-size: 24px;
  margin-right: var(--spacing-base);
}

.payment-name {
  flex: 1;
  font-size: var(--fs-base);
  color: var(--text-1);
}

.payment-check {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: var(--primary);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
}

/* 充值中动画 */
.recharge-loading {
  background: white;
  padding: var(--spacing-3xl);
  text-align: center;
}

.loading-spinner {
  width: 60px;
  height: 60px;
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

.loading-text {
  color: var(--text-1);
  font-size: var(--fs-base);
  margin: 0;
}

/* 充值成功 */
.recharge-success-content {
  background: white;
  padding: var(--spacing-3xl);
  text-align: center;
  animation: fadeIn 0.5s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-20px); }
  to { opacity: 1; transform: translateY(0); }
}

.success-icon {
  font-size: 48px;
  margin-bottom: var(--spacing-lg);
  animation: bounce 0.5s ease;
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% { transform: translateY(0); }
  40% { transform: translateY(-20px); }
  60% { transform: translateY(-10px); }
}

.recharge-success-content h3 {
  color: var(--text-1);
  font-size: var(--fs-xl);
  font-weight: 600;
  margin: 0 0 var(--spacing-lg) 0;
}

.success-amount {
  color: var(--primary);
  font-size: var(--fs-lg);
  font-weight: 600;
  margin: 0 0 var(--spacing-sm) 0;
}

.success-balance {
  color: var(--text-2);
  font-size: var(--fs-base);
  margin: 0 0 var(--spacing-xl) 0;
}

@media (max-width: 768px) {
  .profile-content {
    padding: var(--spacing-xl) 0;
  }
  
  .avatar-section {
    flex-direction: column;
    text-align: center;
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .card-header {
    padding: var(--spacing-lg);
  }
  
  .profile-info {
    padding: var(--spacing-lg);
  }
  
  .recharge-content {
    margin: 0 var(--spacing-base);
  }
  
  .amount-options {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .balance-info {
    padding: var(--spacing-lg);
  }
  
  .balance-amount {
    font-size: var(--fs-xl);
  }
  
  .recharge-loading,
  .recharge-success-content {
    padding: var(--spacing-xl);
  }
}
</style>