// 全局确认框工具

// 全局实例引用
let confirmInstance = null
let resolveCallback = null

// 默认配置
const defaultConfig = {
  show: false,
  title: '确认操作',
  message: '确定要执行此操作吗？',
  confirmText: '确定',
  cancelText: '取消'
}

// 当前配置
let currentConfig = {...defaultConfig}

// 显示确认框
const showConfirm = (options = {}) => {
  return new Promise((resolve) => {
    // 保存resolve回调
    resolveCallback = resolve
    
    // 合并配置
    currentConfig = {
      ...defaultConfig,
      ...options,
      show: true
    }
    
    // 更新组件配置
    if (confirmInstance) {
      // 深拷贝确保响应式更新
      Object.assign(confirmInstance.config.value, currentConfig)
    }
  })
}

// 处理确认
const handleConfirm = () => {
  if (resolveCallback) {
    resolveCallback(true)
    resolveCallback = null
  }
  // 关闭确认框
  if (confirmInstance) {
    confirmInstance.config.value.show = false
  }
  currentConfig.show = false
}

// 处理取消
const handleCancel = () => {
  if (resolveCallback) {
    resolveCallback(false)
    resolveCallback = null
  }
  // 关闭确认框
  if (confirmInstance) {
    confirmInstance.config.value.show = false
  }
  currentConfig.show = false
}

// 注册确认框实例
const registerConfirm = (instance) => {
  confirmInstance = instance
  // 初始化配置
  instance.config.value = {...currentConfig}
}

// 导出
const confirm = showConfirm
export default confirm
export { handleConfirm, handleCancel, registerConfirm }