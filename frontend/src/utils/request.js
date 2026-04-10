import axios from 'axios'
import router from '../router'

// 创建axios实例
const request = axios.create({
  // 基础URL
  baseURL: import.meta.env.VITE_API_BASE_URL || '//localhost:8080/wangshangchongwudian',
  // 请求超时时间
  timeout: 10000,
  // 请求头设置
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 从本地存储获取token
    const token = localStorage.getItem('token')
    // 如果有token，添加到请求头
    if (token) {
      config.headers.token = token
    }
    return config
  },
  error => {
    // 请求错误处理
    // console.error('请求拦截器错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    // 获取响应数据
    const res = response.data
    
    // 根据实际后端返回格式处理
    // 后端返回格式为：{ code: 0, data: {}, msg: '' }
    if (res.code === 0) {
      // 成功响应，返回完整响应对象，包含code、data和msg
      return res
    } else if (res.code === 401) {
      // 未授权，跳转到登录页面
      const errorMsg = res.msg || '未授权，请重新登录'
      router.push('/user/login')
      const error = new Error(errorMsg)
      error.code = res.code
      error.msg = errorMsg
      error.data = res
      return Promise.reject(error)
    } else {
      // 其他失败响应，抛出错误
      const errorMsg = res.msg || res.message || '请求失败'
      const error = new Error(errorMsg)
      error.code = res.code
      error.msg = errorMsg
      error.data = res
      // console.error('响应错误:', error)
      return Promise.reject(error)
    }
  },
  error => {
    // 网络错误或其他错误处理
    // console.error('响应拦截器错误:', error)
    
    // 处理不同的错误状态码
    let message = '网络请求失败，请稍后重试'
    let errorMsg = message
    
    if (error.response) {
      // 服务器返回错误响应
      const status = error.response.status
      const res = error.response.data
      
      // 优先使用后端返回的msg或message字段
      if (res.msg || res.message) {
        errorMsg = res.msg || res.message
        // 处理401错误，跳转到登录页
        if (status === 401) {
          router.push('/user/login')
        }
      } else {
        // 如果没有返回具体的错误信息，根据状态码生成
        switch (status) {
          case 400:
            errorMsg = '请求参数错误'
            break
          case 401:
            errorMsg = '未授权，请重新登录'
            // 处理登录过期，跳转到登录页
            router.push('/user/login')
            break
          case 403:
            errorMsg = '拒绝访问'
            break
          case 404:
            errorMsg = '请求资源不存在'
            break
          case 500:
            errorMsg = '服务器内部错误'
            break
          default:
            errorMsg = `请求错误 (${status})`
        }
      }
    } else if (error.request) {
      // 请求已发送，但没有收到响应
      errorMsg = '服务器无响应，请检查网络连接'
    }
    
    // 包装错误信息
    const errorInfo = {
      message: errorMsg,
      msg: errorMsg,
      code: error.response?.status || -1,
      data: error.response?.data || {}
    }
    
    return Promise.reject(errorInfo)
  }
)

// 封装请求方法
export const http = {
  // GET请求
  get(url, params = {}) {
    return request.get(url, { params })
  },
  
  // POST请求
  post(url, data = {}, config = {}) {
    return request.post(url, data, config)
  },
  
  // PUT请求
  put(url, data = {}) {
    return request.put(url, data)
  },
  
  // DELETE请求
  delete(url, params = {}) {
    return request.delete(url, {
      params
    })
  },
  
  // PATCH请求
  patch(url, data = {}) {
    return request.patch(url, data)
  },
  
  // 上传文件
  upload(url, file, onProgress, config = {}) {
    const formData = new FormData()
    formData.append('file', file)
    
    return request.post(url, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      onUploadProgress: progressEvent => {
        if (onProgress) {
          const percent = Math.round((progressEvent.loaded / progressEvent.total) * 100)
          onProgress(percent)
        }
      },
      ...config
    })
  }
}

export default request
