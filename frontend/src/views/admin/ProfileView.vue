<template>
  <div class="admin-profile">
    <h2>个人中心</h2>
    <div class="profile-form-card">
      <div class="card-header">
        <h3>修改个人信息</h3>
      </div>
      <div class="card-body">
        <form @submit.prevent="handleSubmit" class="profile-form">
          <div class="form-row">
            <div class="form-group">
              <label for="username" class="form-label">
                <span class="required">*</span> 用户名
              </label>
              <input 
                type="text" 
                id="username" 
                v-model="form.username" 
                placeholder="请输入用户名" 
                required 
                class="form-input"
                :disabled="isLoading"
              >
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label for="password" class="form-label">
                密码
              </label>
              <div class="password-input-wrapper">
                <input 
                  :type="showPassword ? 'text' : 'password'" 
                  id="password" 
                  v-model="form.password" 
                  placeholder="请输入密码" 
                  class="form-input"
                  :disabled="isLoading"
                >
                <button 
                  type="button" 
                  class="password-toggle" 
                  @click="showPassword = !showPassword"
                  :disabled="isLoading"
                >
                  {{ showPassword ? '隐藏' : '显示' }}
                </button>
              </div>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label for="confirmPassword" class="form-label">
                确认密码
              </label>
              <input 
                :type="showPassword ? 'text' : 'password'" 
                id="confirmPassword" 
                v-model="form.confirmPassword" 
                placeholder="请确认密码" 
                class="form-input"
                :disabled="isLoading"
              >
              <div v-if="form.confirmPassword && form.password !== form.confirmPassword" class="form-error">
                两次输入的密码不一致
              </div>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label for="role" class="form-label">
                角色
              </label>
              <input 
                type="text" 
                id="role" 
                v-model="form.role" 
                readonly 
                class="form-input form-input-readonly"
              >
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label for="addtime" class="form-label">
                注册时间
              </label>
              <input 
                type="text" 
                id="addtime" 
                v-model="formattedAddtime" 
                readonly 
                class="form-input form-input-readonly"
              >
            </div>
          </div>
          
          <div class="form-actions">
            <button 
              type="submit" 
              class="btn btn-primary" 
              :disabled="isLoading || (form.password && form.password !== form.confirmPassword)"
            >
              <span v-if="isLoading" class="loading-spinner"></span>
              {{ isLoading ? '保存中...' : '保存修改' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
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

const form = ref({
  id: '',
  username: '',
  password: '',
  confirmPassword: '',
  role: '',
  addtime: ''
})

// 显示密码
const showPassword = ref(false)

// 加载状态
const isLoading = ref(false)

// 格式化注册时间
const formattedAddtime = computed(() => {
  if (!form.value.addtime) return ''
  const date = new Date(form.value.addtime)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
})

// 获取用户信息
const getUserInfo = async () => {
  try {
    isLoading.value = true
    const response = await usersApi.session()
    if (response.code === 0) {
      // 根据后端返回的数据结构调整
      const userData = response.data || response
      form.value = {
        id: userData.id,
        username: userData.username,
        password: '',
        confirmPassword: '',
        role: userData.role,
        addtime: userData.addtime || ''
      }
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    message.error('获取用户信息失败，请刷新页面重试')
  } finally {
    isLoading.value = false
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    // 密码验证
    if (form.value.password && form.value.password !== form.value.confirmPassword) {
      message.error('两次输入的密码不一致')
      return
    }
    
    isLoading.value = true
    
    // 只提交需要修改的字段
    const updateData = {
      id: form.value.id,
      username: form.value.username,
      role: form.value.role
    }
    
    // 如果密码不为空，则添加到更新数据中
    if (form.value.password) {
      // 对密码进行MD5加密
      const encryptedPassword = md5(form.value.password)
      updateData.password = encryptedPassword
      // 添加mima字段，根据示例可能需要
      updateData.mima = encryptedPassword
    }
    
    const response = await usersApi.update(updateData)
    if (response.code === 0) {
      message.success('修改成功')
      // 重置密码字段
      form.value.password = ''
      form.value.confirmPassword = ''
    } else {
      message.error('修改失败：' + (response.msg || '未知错误'))
    }
  } catch (error) {
    console.error('修改失败:', error)
    message.error('修改失败，请稍后重试')
  } finally {
    isLoading.value = false
  }
}

// 组件挂载时获取用户信息
onMounted(() => {
  getUserInfo()
})
</script>

<style scoped>
.admin-profile {
  padding: 0;
}

.profile-form-card {
  background-color: var(--card);
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-base);
  max-width: 700px;
  margin-top: 20px;
  overflow: hidden;
  border: 1px solid var(--border);
}

.card-header {
  background-color: var(--bg);
  padding: 20px 24px;
  border-bottom: 1px solid var(--border);
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: var(--text-1);
}

.card-body {
  padding: 24px;
}

.profile-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-row {
  display: flex;
  gap: 24px;
}

.form-group {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
  min-width: 200px;
}

.form-label {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-1);
  display: flex;
  align-items: center;
  gap: 4px;
}

.required {
  color: var(--danger);
  font-size: 16px;
}

.form-input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  transition: all 0.3s ease;
  background-color: white;
  color: var(--text-1);
}

.form-input:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.form-input:disabled {
  background-color: var(--bg);
  cursor: not-allowed;
  border-color: var(--border);
}

.form-input-readonly {
  background-color: var(--bg);
  color: var(--text-3);
  cursor: not-allowed;
}

.password-input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
}

.password-input-wrapper .form-input {
  padding-right: 80px;
  width: 100%;
}

.password-toggle {
  position: absolute;
  right: 4px;
  top: 50%;
  transform: translateY(-50%);
  padding: 8px 12px;
  background-color: white;
  border: 1px solid var(--border);
  border-radius: var(--radius-sm);
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  color: var(--text-1);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

.password-toggle:hover:not(:disabled) {
  background-color: var(--hover-bg);
  border-color: var(--primary);
  color: var(--primary);
}

.password-toggle:disabled {
  background-color: var(--bg);
  cursor: not-allowed;
  color: var(--text-3);
  border-color: var(--border);
}

.form-hint {
  font-size: 12px;
  color: var(--text-3);
  margin-top: -4px;
}

.form-error {
  font-size: 12px;
  color: var(--danger);
  margin-top: -4px;
}

.form-actions {
  display: flex;
  justify-content: flex-start;
  margin-top: 24px;
  gap: 12px;
}

/* 按钮样式增强 */
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 10px 24px;
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

.btn-primary:hover:not(:disabled) {
  background-color: var(--primary-dark);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(66, 184, 131, 0.3);
}

.btn-primary:active:not(:disabled) {
  transform: translateY(0);
  box-shadow: 0 2px 8px rgba(66, 184, 131, 0.2);
}

.btn-primary:disabled {
  background-color: var(--primary-light);
  cursor: not-allowed;
  opacity: 0.7;
}

.loading-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .form-row {
    flex-direction: column;
  }
  
  .profile-form-card {
    margin: 16px;
  }
  
  .card-body {
    padding: 16px;
  }
}
</style>