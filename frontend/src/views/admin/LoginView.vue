<template>
  <div class="admin-login-page">
    <div class="admin-login-form-card">
      <div class="login-header">
        <div class="logo-container">
          <div class="logo-icon">
            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2C6.48 2 2 6.48 2 12C2 17.52 6.48 22 12 22C17.52 22 22 17.52 22 12C22 6.48 17.52 2 12 2ZM12 20C7.59 20 4 16.41 4 12C4 7.59 7.59 4 12 4C16.41 4 20 7.59 20 12C20 16.41 16.41 20 12 20ZM12 18C13.1 18 14 17.1 14 16C14 14.9 13.1 14 12 14C10.9 14 10 14.9 10 16C10 17.1 10.9 18 12 18ZM12 8C13.1 8 14 7.1 14 6C14 4.9 13.1 4 12 4C10.9 4 10 4.9 10 6C10 7.1 10.9 8 12 8Z" fill="var(--primary)"/>
            </svg>
          </div>
          <h2>后台管理系统</h2>
        </div>
        <p class="login-subtitle">欢迎回来，请登录您的账号</p>
      </div>
      
      <form class="admin-login-form" @submit="handleLogin">
        <div class="form-group">
          <label for="username" class="form-label">
            <span class="label-text">管理员账号</span>
            <span class="required-mark">*</span>
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
              placeholder="请输入管理员账号" 
              required
              class="form-input"
            >
          </div>
        </div>
        
        <div class="form-group">
          <label for="password" class="form-label">
            <span class="label-text">密码</span>
            <span class="required-mark">*</span>
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
      </form>
      
      <div class="login-footer">
        <p class="copyright">© 2026 宠物管理系统 版权所有</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { usersApi } from '../../utils/api'
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
const form = ref({
  username: '',
  password: ''
})
const showPassword = ref(false)
const isLoading = ref(false)

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
    // 对密码进行MD5加密
    const loginData = {
      ...form.value,
      password: md5(form.value.password)
    }
    // 调用登录接口
    const response = await usersApi.login(loginData)
    if (response.code === 0) {
      // 登录成功，保存token到本地存储
      if (response.token) {
        localStorage.setItem('token', response.token)
      }
      // 跳转到管理员首页
      router.push('/admin')
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
.admin-login-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  width: 100vw;
  background: linear-gradient(135deg, var(--bg) 0%, #e6f0ea 100%);
  margin: 0;
  padding: 0;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", sans-serif;
  position: relative;
  overflow: hidden;
}

/* 背景装饰 */
.admin-login-page::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle at 25% 25%, rgba(66, 184, 131, 0.1) 0%, transparent 50%),
              radial-gradient(circle at 75% 75%, rgba(70, 169, 255, 0.1) 0%, transparent 50%);
  animation: float 20s ease-in-out infinite;
  z-index: 0;
}

@keyframes float {
  0% { transform: translate(0, 0) rotate(0deg); }
  50% { transform: translate(10px, 10px) rotate(5deg); }
  100% { transform: translate(0, 0) rotate(0deg); }
}

/* 登录表单卡片 */
.admin-login-form-card {
  background-color: var(--card);
  padding: 48px 40px;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
  width: 100%;
  max-width: 420px;
  margin: 20px;
  position: relative;
  z-index: 1;
  border: 1px solid var(--border);
  transition: all 0.3s ease;
}

.admin-login-form-card:hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

/* 登录头部 */
.login-header {
  text-align: center;
  margin-bottom: 36px;
}

.logo-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 16px;
}

.logo-icon {
  width: 64px;
  height: 64px;
  background: linear-gradient(135deg, var(--primary) 0%, var(--primary-light) 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16px;
  box-shadow: 0 4px 12px rgba(66, 184, 131, 0.3);
  transition: all 0.3s ease;
}

.logo-icon:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 16px rgba(66, 184, 131, 0.4);
}

.login-header h2 {
  margin: 0 0 8px 0;
  color: var(--text-1);
  font-size: var(--fs-2xl);
  font-weight: 600;
}

.login-subtitle {
  margin: 0;
  color: var(--text-2);
  font-size: var(--fs-sm);
}

/* 表单样式 */
.admin-login-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
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

.required-mark {
  color: var(--danger);
  font-size: var(--fs-base);
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

/* 登录页脚 */
.login-footer {
  margin-top: 32px;
  text-align: center;
}

.copyright {
  margin: 0;
  color: var(--text-3);
  font-size: var(--fs-xs);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .admin-login-form-card {
    padding: 32px 24px;
    margin: 16px;
  }
  
  .logo-icon {
    width: 56px;
    height: 56px;
  }
  
  .login-header h2 {
    font-size: var(--fs-xl);
  }
  
  .admin-login-form {
    gap: 20px;
  }
}
</style>