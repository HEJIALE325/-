// 全局消息提示工具

let messageInstance = null

// 初始化消息实例
const initMessage = (instance) => {
  messageInstance = instance
}

// 消息提示方法
const message = {
  success: (text, duration) => {
    if (messageInstance) {
      messageInstance.success(text, duration)
    } else {
      console.warn('Message instance not initialized, falling back to alert')
      alert(text)
    }
  },
  warning: (text, duration) => {
    if (messageInstance) {
      messageInstance.warning(text, duration)
    } else {
      console.warn('Message instance not initialized, falling back to alert')
      alert(text)
    }
  },
  error: (text, duration) => {
    if (messageInstance) {
      messageInstance.error(text, duration)
    } else {
      console.warn('Message instance not initialized, falling back to alert')
      alert(text)
    }
  },
  info: (text, duration) => {
    if (messageInstance) {
      messageInstance.info(text, duration)
    } else {
      console.warn('Message instance not initialized, falling back to alert')
      alert(text)
    }
  }
}

export { initMessage, message }
export default message