<template>
  <div class="register-page">
    <div class="register-container">
      <div class="register-form-card">
        <div class="register-header">
          <h2>用户注册</h2>
          <p>创建新账户，开启宠物购物之旅</p>
        </div>
        
        <form class="register-form" @submit="handleRegister">
          <div class="form-group">
            <label for="username">用户名 <span class="required">*</span></label>
            <input 
              type="text" 
              id="username" 
              v-model="form.username" 
              placeholder="请输入用户名" 
              required
            >
          </div>
          
          <div class="form-group">
            <label for="password">密码 <span class="required">*</span></label>
            <div class="password-input">
              <input 
                :type="showPassword ? 'text' : 'password'" 
                id="password" 
                v-model="form.password" 
                placeholder="请输入密码" 
                required
              >
              <button 
                type="button" 
                class="password-toggle" 
                @click="showPassword = !showPassword"
              >
                {{ showPassword ? '👁️' : '👁️‍🗨️' }}
              </button>
            </div>
          </div>
          
          <div class="form-group">
            <label for="yonghuName">姓名 <span class="required">*</span></label>
            <input 
              type="text" 
              id="yonghuName" 
              v-model="form.yonghuName" 
              placeholder="请输入姓名" 
              required
            >
          </div>
          
          <div class="form-group">
            <label for="yonghuPhone">手机号 <span class="required">*</span></label>
            <input 
              type="tel" 
              id="yonghuPhone" 
              v-model="form.yonghuPhone" 
              placeholder="请输入手机号" 
              required
            >
          </div>
          
          <div class="form-group">
            <label for="yonghuIdNumber">身份证号 <span class="required">*</span></label>
            <input 
              type="text" 
              id="yonghuIdNumber" 
              v-model="form.yonghuIdNumber" 
              placeholder="请输入身份证号" 
              required
            >
          </div>
          
          <div class="form-group">
            <label for="yonghuEmail">邮箱</label>
            <input 
              type="email" 
              id="yonghuEmail" 
              v-model="form.yonghuEmail" 
              placeholder="请输入邮箱" 
            >
          </div>
          
          <div class="form-group">
            <label for="sexTypes">性别 <span class="required">*</span></label>
            <select 
              id="sexTypes" 
              v-model="form.sexTypes" 
              required
            >
              <option value="">请选择性别</option>
              <option value="1">男</option>
              <option value="2">女</option>
            </select>
          </div>
          
          <div class="form-actions">
            <button 
              type="submit" 
              class="btn btn-primary btn-block" 
              :disabled="isLoading"
            >
              {{ isLoading ? '注册中...' : '注册' }}
            </button>
          </div>
          
          <div class="form-footer">
            <p>已有账号？<router-link to="/user/login">立即登录</router-link></p>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { yonghuApi } from '../../utils/api'
import message from '../../utils/message'

// MD5加密函数
function md5(string) {
  let hash = 0;
  if (string.length === 0) return hash;
  for (let i = 0; i < string.length; i++) {
    let char = string.charCodeAt(i);
    hash = ((hash << 5) - hash) + char;
    hash = hash & hash;
  }
  let hex = hash.toString(16);
  while (hex.length < 32) {
    hex = '0' + hex;
  }
  return hex;
}

const router = useRouter()
const showPassword = ref(false)
const isLoading = ref(false)

const form = ref({
  username: '',
  password: '',
  yonghuName: '',
  yonghuPhone: '',
  yonghuIdNumber: '',
  yonghuEmail: '',
  sexTypes: ''
})

const handleRegister = async (e) => {
  e.preventDefault()
  try {
    isLoading.value = true
    
    // 对密码进行MD5加密
    const registerData = {
      ...form.value,
      password: md5(form.value.password)
    }
    
    const response = await yonghuApi.register(registerData)
    if (response.code === 0) {
      message.success('注册成功，请登录')
      router.push('/user/login')
    } else {
      message.error('注册失败：' + (response.msg || '未知错误'))
    }
  } catch (error) {
    console.error('注册失败:', error)
    message.error('注册失败，请稍后重试')
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
.register-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  width: 100vw;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e9f2 100%);
  margin: 0;
  padding: 0;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", sans-serif;
}

.register-container {
  width: 100%;
  max-width: 500px;
  padding: 20px;
}

.register-form-card {
  background-color: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  border: 1px solid #e0e0e0;
}

.register-header {
  text-align: center;
  margin-bottom: 32px;
}

.register-header h2 {
  color: var(--primary);
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 8px;
}

.register-header p {
  color: var(--text-2);
  font-size: 14px;
  margin: 0;
}

.register-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  display: block;
  font-weight: 500;
  color: var(--text-1);
  font-size: 14px;
}

.required {
  color: var(--danger);
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 14px 16px;
  border: 1px solid #d9d9d9;
  border-radius: 8px;
  font-size: 14px;
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

.password-input {
  position: relative;
}

.password-toggle {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  font-size: 16px;
  cursor: pointer;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn {
  padding: 14px 24px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary {
  background-color: var(--primary);
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background-color: var(--primary-dark);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 140, 56, 0.3);
}

.btn-primary:disabled {
  background-color: #ffb380;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.btn-block {
  width: 100%;
}

.form-footer {
  text-align: center;
  margin-top: 8px;
}

.form-footer p {
  margin: 0;
  color: var(--text-2);
  font-size: 14px;
}

.form-footer a {
  color: var(--primary);
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.form-footer a:hover {
  color: var(--primary-dark);
  text-decoration: underline;
}

@media (max-width: 768px) {
  .register-form-card {
    padding: 24px;
  }
  
  .register-header h2 {
    font-size: 20px;
  }
  
  .form-group input,
  .form-group select {
    padding: 12px 14px;
  }
  
  .btn {
    padding: 12px 20px;
  }
}
</style>