<template>
  <div class="login-page">
    <div class="login-content">
      <!-- 左侧品牌展示区 -->
      <div class="login-brand-section">
        <div class="brand-content">
          <div class="brand-logo">
            <svg width="80" height="80" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2C6.48 2 2 6.48 2 12C2 17.52 6.48 22 12 22C17.52 22 22 17.52 22 12C22 6.48 17.52 2 12 2ZM12 20C7.59 20 4 16.41 4 12C4 7.59 7.59 4 12 4C16.41 4 20 7.59 20 12C20 16.41 16.41 20 12 20ZM14 12C14 13.1 13.1 14 12 14C10.9 14 10 13.1 10 12C10 10.9 10.9 10 12 10C13.1 10 14 10.9 14 12ZM12 18C13.66 18 15 16.66 15 15C15 13.34 13.66 12 12 12C10.34 12 9 13.34 9 15C9 14.66 9.07 14.33 9.2 14C9.47 14.67 9.84 15.24 10.3 15.67C10.76 16.1 11.32 16.42 12 16.6C12.68 16.42 13.24 16.1 13.7 15.67C14.16 15.24 14.53 14.67 14.8 14C14.93 14.33 15 14.66 15 15C15 16.66 13.66 18 12 18Z" fill="white"/>
            </svg>
          </div>
          <h1 class="brand-title">宠物乐园</h1>
          <p class="brand-subtitle">为您的爱宠提供最好的服务</p>
          <div class="brand-features">
            <div class="feature-item">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M9 16.17L4.83 12L3.41 13.41L9 19L21 7L19.59 5.59L9 16.17Z" fill="white"/>
              </svg>
              <span>优质商品</span>
            </div>
            <div class="feature-item">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M9 16.17L4.83 12L3.41 13.41L9 19L21 7L19.59 5.59L9 16.17Z" fill="white"/>
              </svg>
              <span>专业服务</span>
            </div>
            <div class="feature-item">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M9 16.17L4.83 12L3.41 13.41L9 19L21 7L19.59 5.59L9 16.17Z" fill="white"/>
              </svg>
              <span>快速配送</span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 右侧登录表单区 -->
      <div class="login-form-section">
        <div class="login-form-card">
          <div class="form-header">
            <h2>欢迎回来</h2>
            <p>登录您的账号，继续享受我们的服务</p>
          </div>
          
          <form class="login-form" @submit="handleLogin">
            <div class="form-group">
              <label for="username" class="form-label">
                <span class="label-text">用户名</span>
              </label>
              <div class="input-wrapper">
                <span class="input-icon">
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M12 12C14.21 12 16 10.21 16 8C16 5.79 14.21 4 12 4C9.79 4 8 5.79 8 8C8 10.21 9.79 12 12 12ZM12 14C9.33 14 4 15.34 4 18V20H20V18C20 15.34 14.67 14 12 14Z" fill="var(--text-3)"/>
                  </svg>
                </span>
                <input 
                  type="text" 
                  id="username" 
                  v-model="form.username" 
                  placeholder="请输入用户名" 
                  required
                  class="form-input"
                >
              </div>
            </div>
            
            <div class="form-group">
              <label for="password" class="form-label">
                <span class="label-text">密码</span>
              </label>
              <div class="input-wrapper">
                <span class="input-icon">
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M12 15C13.66 15 15 13.66 15 12C15 10.34 13.66 9 12 9C10.34 9 9 10.34 9 12C9 13.66 10.34 15 12 15ZM19.07 4.93C19.36 4.64 19.91 4.64 20.2 4.93C20.49 5.22 20.49 5.77 20.2 6.06L18.13 8.13C18.34 8.5 18.46 8.91 18.46 9.35V14.65C18.46 15.09 18.34 15.5 18.13 15.87L20.2 17.94C20.49 18.23 20.49 18.78 20.2 19.07C19.91 19.36 19.36 19.36 19.07 19.07L16.39 16.39C16.12 16.66 15.75 16.84 15.36 16.91L12 18L8.64 16.91C8.25 16.84 7.88 16.66 7.61 16.39L4.93 19.07C4.64 19.36 4.09 19.36 3.8 19.07C3.51 18.78 3.51 18.23 3.8 17.94L5.87 15.87C5.66 15.5 5.54 15.09 5.54 14.65V9.35C5.54 8.91 5.66 8.5 5.87 8.13L3.8 6.06C3.51 5.77 3.51 5.22 3.8 4.93C4.09 4.64 4.64 4.64 4.93 4.93L7.61 7.61C7.88 7.34 8.25 7.16 8.64 7.09L12 6L15.36 7.09C15.75 7.16 16.12 7.34 16.39 7.61L19.07 4.93Z" fill="var(--text-3)"/>
                  </svg>
                </span>
                <input 
                  type="password" 
                  id="password" 
                  v-model="form.password" 
                  placeholder="请输入密码" 
                  required
                  class="form-input"
                >
                <span class="input-action" @click="togglePassword">
                  <svg v-if="!showPassword" width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M12 4.5C7 4.5 2.73 7.61 1 12C2.73 16.39 7 19.5 12 19.5C17 19.5 21.27 16.39 23 12C21.27 7.61 17 4.5 12 4.5ZM12 17C9.24 17 7 14.76 7 12C7 9.24 9.24 7 12 7C14.76 7 17 9.24 17 12C17 14.76 14.76 17 12 17ZM12 9C10.34 9 9 10.34 9 12C9 13.66 10.34 15 12 15C13.66 15 15 13.66 15 12C15 10.34 13.66 9 12 9Z" fill="var(--text-3)"/>
                  </svg>
                  <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M12 7C14.76 7 17 9.24 17 12C17 14.76 14.76 17 12 17C9.24 17 7 14.76 7 12C7 9.24 9.24 7 12 7ZM12 9C10.34 9 9 10.34 9 12C9 13.66 10.34 15 12 15C13.66 15 15 13.66 15 12C15 10.34 13.66 9 12 9ZM2.06 3.19L3.47 4.6C5.25 3.33 7.3 2.5 9.5 2.5C14 2.5 18.03 4.66 20.5 8.16L21.91 9.57C21.56 7.96 20.84 6.43 19.8 5.07C18.07 3.08 15.3 1.9 12.37 1.9C9.09 1.9 6.11 3.36 4.08 5.39L5.5 6.8C6.83 5.47 8.66 4.5 10.63 4.5C13.38 4.5 15.8 6.14 17.39 8.64L18.8 10.05C17.54 7.45 15 5.5 12.05 5.5C8.45 5.5 5.3 7.37 3.48 10.03L4.89 11.44C6.25 9.65 8.22 8.5 10.39 8.5C13.05 8.5 15.41 10.29 16.74 12.78L18.15 14.19C16.85 16.85 14.25 18.9 11.37 19.5C8.76 20.05 6.1 19.37 3.96 17.53L2.55 16.12C4.83 18.84 7.95 20.5 11.3 20.5C16 20.5 19.96 17.91 21.92 14.14L23.34 15.55L22.93 16.26L2.39 2.72L1.98 3.43L2.06 3.19Z" fill="var(--text-3)"/>
                  </svg>
                </span>
              </div>
              <div class="password-footer">
                <router-link to="#" class="forgot-password">忘记密码？</router-link>
              </div>
            </div>
            
            <div class="form-remember">
              <label class="remember-checkbox">
                <input type="checkbox" v-model="rememberMe">
                <span class="checkmark"></span>
                记住我
              </label>
            </div>
            
            <div class="form-actions">
              <button type="submit" class="btn btn-primary btn-block" :disabled="isLoading">
                <span v-if="!isLoading">登录</span>
                <span v-else class="loading-spinner">
                  <svg class="spinner" width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" stroke-linecap="round" stroke-dasharray="62.8" stroke-dashoffset="62.8">
                      <animate attributeName="stroke-dashoffset" dur="1s" repeatCount="indefinite" from="62.8" to="0"/>
                      <animateTransform attributeName="transform" type="rotate" from="0 12 12" to="360 12 12" dur="1s" repeatCount="indefinite"/>
                    </circle>
                  </svg>
                  登录中...
                </span>
              </button>
            </div>
            
            <div class="form-divider">
              <span>或使用以下方式登录</span>
            </div>
            
            <div class="social-login">
              <button type="button" class="social-btn wechat">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M12 2C6.48 2 2 6.48 2 12C2 17.52 6.48 22 12 22C17.52 22 22 17.52 22 12C22 6.48 17.52 2 12 2ZM12 20C7.59 20 4 16.41 4 12C4 7.59 7.59 4 12 4C16.41 4 20 7.59 20 12C20 16.41 16.41 20 12 20ZM9 16C10.1 16 11 15.1 11 14C11 12.9 10.1 12 9 12C7.9 12 7 12.9 7 14C7 15.1 7.9 16 9 16ZM15 16C16.1 16 17 15.1 17 14C17 12.9 16.1 12 15 12C13.9 12 13 12.9 13 14C13 15.1 13.9 16 15 16ZM12 10C13.66 10 15 8.66 15 7C15 5.34 13.66 4 12 4C10.34 4 9 5.34 9 7C9 8.66 10.34 10 12 10Z" fill="currentColor"/>
                </svg>
                微信登录
              </button>
              <button type="button" class="social-btn phone">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M20 15.5C18.8 15.5 17.5 15.3 16.4 14.9C16.3 14.9 16.2 14.9 16.1 14.9C15.8 14.9 15.6 15 15.4 15.2L13.2 17.4C10.4 15.9 8 13.6 6.6 10.8L8.8 8.6C9 8.4 9.1 8.2 9.1 7.9C9.1 7.8 9.1 7.7 9.1 7.6C9.5 6.5 9.7 5.2 9.7 4C9.7 3.5 9.5 3 9.2 2.7C8.9 2.4 8.4 2.2 7.9 2.2H4C3.5 2.2 3 2.4 2.7 2.7C2.4 3 2.2 3.5 2.2 4V7.9C2.2 8.4 2.4 8.9 2.7 9.2C3 9.5 3.5 9.7 4 9.7C5.2 9.7 6.5 9.5 7.6 9.1C7.7 9.1 7.8 9.1 7.9 9.1C8.2 9.1 8.4 9 8.6 8.8L10.8 6.6C13.6 8 15.9 10.4 17.4 13.2L15.2 15.4C15 15.6 14.9 15.8 14.9 16.1C14.9 16.2 14.9 16.3 14.9 16.4C15.3 17.5 15.5 18.8 15.5 20C15.5 20.5 15.7 21 16 21.3C16.3 21.6 16.8 21.8 17.3 21.8H21C21.5 21.8 22 21.6 22.3 21.3C22.6 21 22.8 20.5 22.8 20V16.3C22.8 15.8 22.6 15.3 22.3 15C22 14.7 21.5 14.5 21 14.5H20.5C20.3 15.3 20.1 15.5 20 15.5Z" fill="currentColor"/>
                </svg>
                手机号登录
              </button>
            </div>
          </form>
          
          <div class="form-footer">
            <p>还没有账号？<router-link to="/user/register">立即注册</router-link></p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { yonghuApi } from '../../utils/api'
import message from '../../utils/message'

const router = useRouter()
const form = ref({
  username: '',
  password: ''
})
const showPassword = ref(false)
const isLoading = ref(false)
const rememberMe = ref(false)

// 切换密码可见性
const togglePassword = () => {
  showPassword.value = !showPassword.value
  const passwordInput = document.getElementById('password')
  if (passwordInput) {
    passwordInput.type = showPassword.value ? 'text' : 'password'
  }
}

// 登录逻辑
const handleLogin = async (e) => {
  e.preventDefault()
  try {
    isLoading.value = true
    // 构建URLSearchParams格式数据
    const formData = new URLSearchParams()
    formData.append('username', form.value.username)
    formData.append('password', form.value.password)
    
    // 调用登录接口，设置正确的Content-Type
    const response = await yonghuApi.login(formData, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })
    if (response.code === 0) {
      // 登录成功，保存token到本地存储
      if (response.token) {
        localStorage.setItem('token', response.token)
      }
      // 登录成功后获取用户信息
      const userResponse = await yonghuApi.session()
      if (userResponse.code === 0) {
        // 跳转到首页
        router.push('/')
        message.success('登录成功')
      } else {
        // 跳转到首页
        router.push('/')
        message.success('登录成功')
      }
    } else {
      message.error('登录失败：' + (response.msg || '未知错误'))
    }
  } catch (error) {
    console.error('登录失败:', error)
    message.error('登录失败，请稍后重试')
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
/* 登录页面容器 */
.login-page {
  min-height: 100vh;
  background-color: var(--bg);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", sans-serif;
}

/* 登录内容区域 */
.login-content {
  display: flex;
  width: 100%;
  max-width: 900px;
  min-height: 500px;
  border-radius: var(--radius-lg);
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  background-color: var(--card);
}

/* 左侧品牌展示区 */
.login-brand-section {
  flex: 1;
  background: linear-gradient(135deg, var(--accent) 0%, var(--accent-light) 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  position: relative;
  overflow: hidden;
}

.login-brand-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle at 25% 25%, rgba(255, 255, 255, 0.1) 0%, transparent 50%),
              radial-gradient(circle at 75% 75%, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
  animation: float 20s ease-in-out infinite;
}

@keyframes float {
  0% { transform: translate(0, 0) rotate(0deg); }
  50% { transform: translate(10px, 10px) rotate(5deg); }
  100% { transform: translate(0, 0) rotate(0deg); }
}

.brand-content {
  position: relative;
  z-index: 1;
  text-align: center;
}

.brand-logo {
  width: 100px;
  height: 100px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.brand-title {
  font-size: var(--fs-3xl);
  font-weight: 700;
  margin: 0 0 12px 0;
  color: white;
}

.brand-subtitle {
  font-size: var(--fs-base);
  margin: 0 0 32px 0;
  opacity: 0.9;
}

.brand-features {
  display: flex;
  flex-direction: column;
  gap: 16px;
  align-items: center;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: var(--fs-sm);
  opacity: 0.9;
}

/* 右侧登录表单区 */
.login-form-section {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.login-form-card {
  width: 100%;
  max-width: 350px;
}

.form-header {
  text-align: center;
  margin-bottom: 32px;
}

.form-header h2 {
  margin: 0 0 8px 0;
  color: var(--text-1);
  font-size: var(--fs-2xl);
  font-weight: 600;
}

.form-header p {
  margin: 0;
  color: var(--text-2);
  font-size: var(--fs-sm);
}

/* 表单样式 */
.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 表单组样式 */
.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}



/* 标签样式 */
.form-label {
  display: flex;
  align-items: center;
  gap: 4px;
  font-weight: 500;
  color: var(--text-1);
  font-size: var(--fs-sm);
}

/* 输入框容器 */
.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

/* 输入框图标 */
.input-icon {
  position: absolute;
  left: 16px;
  z-index: 1;
  color: var(--text-3);
  transition: all 0.3s ease;
}

/* 输入框操作按钮 */
.input-action {
  position: absolute;
  right: 16px;
  z-index: 1;
  color: var(--text-3);
  cursor: pointer;
  transition: all 0.3s ease;
}

.input-action:hover {
  color: var(--primary);
}

/* 输入框样式 */
.form-input {
  width: 100%;
  padding: 14px 16px 14px 48px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: var(--fs-base);
  transition: all 0.3s ease;
  background-color: white;
  color: var(--text-1);
}

.form-input:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.form-input:focus + .input-icon {
  color: var(--primary);
}

/* 密码框底部 */
.password-footer {
  margin-top: 8px;
  text-align: right;
}

.forgot-password {
  font-size: var(--fs-sm);
  color: var(--primary);
  text-decoration: none;
  transition: color 0.3s ease;
}

.forgot-password:hover {
  color: var(--primary-dark);
  text-decoration: underline;
}

/* 记住我 */
.form-remember {
  display: flex;
  align-items: center;
}

.remember-checkbox {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: var(--fs-sm);
  color: var(--text-2);
}

.remember-checkbox input[type="checkbox"] {
  display: none;
}

.checkmark {
  width: 16px;
  height: 16px;
  border: 1px solid var(--border);
  border-radius: 4px;
  position: relative;
  transition: all 0.3s ease;
}

.remember-checkbox input[type="checkbox"]:checked + .checkmark {
  background-color: var(--primary);
  border-color: var(--primary);
}

.remember-checkbox input[type="checkbox"]:checked + .checkmark::after {
  content: '';
  position: absolute;
  top: 2px;
  left: 5px;
  width: 4px;
  height: 8px;
  border: solid white;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

/* 按钮样式 */
.btn {
  padding: 14px 24px;
  border: none;
  border-radius: var(--radius-base);
  font-size: var(--fs-base);
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

/* 主要按钮样式 */
.btn-primary {
  background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(66, 184, 131, 0.3);
}

/* 主要按钮悬停样式 */
.btn-primary:hover:not(:disabled) {
  background: linear-gradient(135deg, var(--primary-dark) 0%, var(--primary) 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(66, 184, 131, 0.4);
}

/* 按钮禁用样式 */
.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

/* 按钮块样式 */
.btn-block {
  width: 100%;
}

/* 加载动画 */
.loading-spinner {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.spinner {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 分割线 */
.form-divider {
  display: flex;
  align-items: center;
  gap: 16px;
  margin: 16px 0;
}

.form-divider::before,
.form-divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background-color: var(--border);
}

.form-divider span {
  font-size: var(--fs-sm);
  color: var(--text-3);
  white-space: nowrap;
}

/* 社交登录 */
.social-login {
  display: flex;
  gap: 12px;
}

.social-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  background-color: white;
  font-size: var(--fs-sm);
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.social-btn:hover {
  border-color: var(--primary);
  background-color: var(--hover-bg);
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.social-btn.wechat {
  color: #07C160;
}

.social-btn.phone {
  color: var(--primary);
}

/* 登录页脚 */
.form-footer {
  margin-top: 24px;
  text-align: center;
}

.form-footer p {
  margin: 0;
  color: var(--text-2);
  font-size: var(--fs-sm);
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

/* 响应式设计 */
@media (max-width: 768px) {
  .login-content {
    flex-direction: column;
    height: auto;
    max-width: 400px;
  }
  
  .login-brand-section {
    padding: 32px;
    min-height: 200px;
  }
  
  .login-form-section {
    padding: 32px;
  }
  
  .brand-logo {
    width: 80px;
    height: 80px;
  }
  
  .brand-title {
    font-size: var(--fs-2xl);
  }
  
  .social-login {
    flex-direction: column;
  }
}
</style>