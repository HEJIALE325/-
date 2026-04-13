// API服务文件，直接在api里写路径，直观易懂

import { http } from './request'

// 地址相关API服务
export const addressApi = {
  // 后端列表
  getPage: params => http.get('/address/page', params),
  // 后端详情
  getInfo: id => http.get(`/address/info/${id}`),
  // 后端保存
  save: data => http.post('/address/save', data),
  // 后端修改
  update: data => http.post('/address/update', data),
  // 删除
  delete: ids => http.post('/address/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/address/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/address/list', params),
  // 前端详情
  getDetail: id => http.get(`/address/detail/${id}`),
  // 前端保存
  add: data => http.post('/address/add', data)
}

// 购物车相关API服务
export const cartApi = {
  // 后端列表
  getPage: params => http.get('/cart/page', params),
  // 后端详情
  getInfo: id => http.get(`/cart/info/${id}`),
  // 后端保存
  save: data => http.post('/cart/save', data),
  // 后端修改
  update: data => http.post('/cart/update', data),
  // 删除
  delete: ids => http.post('/cart/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/cart/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/cart/list', params),
  // 前端详情
  getDetail: id => http.get(`/cart/detail/${id}`),
  // 前端保存
  add: data => http.post('/cart/add', data)
}

// 聊天相关API服务
export const chatApi = {
  // 后端列表
  getPage: params => http.get('/chat/page', params),
  // 后端详情
  getInfo: id => http.get(`/chat/info/${id}`),
  // 后端保存
  save: data => http.post('/chat/save', data),
  // 后端修改
  update: data => http.post('/chat/update', data),
  // 删除
  delete: ids => http.post('/chat/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/chat/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/chat/list', params),
  // 前端详情
  getDetail: id => http.get(`/chat/detail/${id}`),
  // 前端保存
  add: data => http.post('/chat/add', data)
}

// 宠物寄养相关API服务
export const chongwujiyangApi = {
  // 后端列表
  getPage: params => http.get('/chongwujiyang/page', params),
  // 后端详情
  getInfo: id => http.get(`/chongwujiyang/info/${id}`),
  // 后端保存
  save: data => http.post('/chongwujiyang/save', data),
  // 后端修改
  update: data => http.post('/chongwujiyang/update', data),
  // 删除
  delete: ids => http.post('/chongwujiyang/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/chongwujiyang/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/chongwujiyang/list', params),
  // 前端详情
  getDetail: id => http.get(`/chongwujiyang/detail/${id}`),
  // 前端保存
  add: data => http.post('/chongwujiyang/add', data)
}

// 宠物寄养预约相关API服务
export const chongwujiyangYuyueApi = {
  // 后端列表
  getPage: params => http.get('/chongwujiyangYuyue/page', params),
  // 后端详情
  getInfo: id => http.get(`/chongwujiyangYuyue/info/${id}`),
  // 后端保存
  save: data => http.post('/chongwujiyangYuyue/save', data),
  // 后端修改
  update: data => http.post('/chongwujiyangYuyue/update', data),
  // 删除
  delete: ids => http.post('/chongwujiyangYuyue/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/chongwujiyangYuyue/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/chongwujiyangYuyue/list', params),
  // 前端详情
  getDetail: id => http.get(`/chongwujiyangYuyue/detail/${id}`),
  // 前端保存
  add: data => http.post('/chongwujiyangYuyue/add', data),
  // 获取用户宠物寄存订单列表
  getUserOrders: (yonghuId, params = {}) => http.get('/chongwujiyangYuyue/list', { ...params, yonghuId, t: Date.now() })
}

// 宠物日志相关API服务
export const chongwurizhiApi = {
  // 后端列表
  getPage: params => http.get('/chongwurizhi/page', params),
  // 后端详情
  getInfo: id => http.get(`/chongwurizhi/info/${id}`),
  // 后端保存
  save: data => http.post('/chongwurizhi/save', data),
  // 后端修改
  update: data => http.post('/chongwurizhi/update', data),
  // 删除
  delete: ids => http.post('/chongwurizhi/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/chongwurizhi/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/chongwurizhi/list', params),
  // 前端详情
  getDetail: id => http.get(`/chongwurizhi/detail/${id}`),
  // 前端保存
  add: data => http.post('/chongwurizhi/add', data)
}

// 宠物用品收藏相关API服务
export const chongwuyongpinCollectionApi = {
  // 后端列表
  getPage: params => http.get('/chongwuyongpinCollection/page', params),
  // 后端详情
  getInfo: id => http.get(`/chongwuyongpinCollection/info/${id}`),
  // 后端保存
  save: data => http.post('/chongwuyongpinCollection/save', data),
  // 后端修改
  update: data => http.post('/chongwuyongpinCollection/update', data),
  // 删除
  delete: ids => http.post('/chongwuyongpinCollection/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/chongwuyongpinCollection/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/chongwuyongpinCollection/list', params),
  // 前端详情
  getDetail: id => http.get(`/chongwuyongpinCollection/detail/${id}`),
  // 前端保存
  add: data => http.post('/chongwuyongpinCollection/add', data),
  // 获取用户收藏列表
  getUserCollections: (yonghuId, params = {}) => http.get('/chongwuyongpinCollection/list', { ...params, yonghuId, chongwuyongpinCollectionTypes: 1, t: Date.now() })
}

// 宠物用品评论相关API服务
export const chongwuyongpinCommentbackApi = {
  // 后端列表
  getPage: params => http.get('/chongwuyongpinCommentback/page', params),
  // 后端详情
  getInfo: id => http.get(`/chongwuyongpinCommentback/info/${id}`),
  // 后端保存
  save: data => http.post('/chongwuyongpinCommentback/save', data),
  // 后端修改
  update: data => http.post('/chongwuyongpinCommentback/update', data),
  // 删除
  delete: ids => http.post('/chongwuyongpinCommentback/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/chongwuyongpinCommentback/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/chongwuyongpinCommentback/list', params),
  // 前端详情
  getDetail: id => http.get(`/chongwuyongpinCommentback/detail/${id}`),
  // 前端保存
  add: data => http.post('/chongwuyongpinCommentback/add', data),
  // 获取用户评价列表
  getUserComments: (yonghuId, params = {}) => http.get('/chongwuyongpinCommentback/list', { ...params, yonghuId, t: Date.now() }),
  // 删除评价
  delete: ids => http.post('/chongwuyongpinCommentback/delete', ids)
}

// 宠物用品相关API服务
export const chongwuyongpinApi = {
  // 后端列表
  getPage: params => http.get('/chongwuyongpin/page', params),
  // 后端详情
  getInfo: id => http.get(`/chongwuyongpin/info/${id}`),
  // 后端保存
  save: data => http.post('/chongwuyongpin/save', data),
  // 后端修改
  update: data => http.post('/chongwuyongpin/update', data),
  // 删除
  delete: ids => http.post('/chongwuyongpin/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/chongwuyongpin/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/chongwuyongpin/list', params),
  // 前端详情
  getDetail: id => http.get(`/chongwuyongpin/detail/${id}`),
  // 前端保存
  add: data => http.post('/chongwuyongpin/add', data)
}

// 宠物用品订单相关API服务
export const chongwuyongpinOrderApi = {
  // 后端列表
  getPage: params => http.get('/chongwuyongpinOrder/page', params),
  // 后端详情
  getInfo: id => http.get(`/chongwuyongpinOrder/info/${id}`),
  // 后端保存
  save: data => http.post('/chongwuyongpinOrder/save', data),
  // 后端修改
  update: data => http.post('/chongwuyongpinOrder/update', data),
  // 删除
  delete: ids => http.post('/chongwuyongpinOrder/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/chongwuyongpinOrder/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/chongwuyongpinOrder/list', params),
  // 前端详情 - 根据订单ID获取详情
  getDetail: id => http.get(`/chongwuyongpinOrder/detail/${id}`),
  // 根据订单号获取订单详情（包含商品信息）
  getDetailByOrderNo: orderNo => http.get(`/chongwuyongpinOrder/detailByOrderNo/${orderNo}`),
  // 前端保存
  add: data => http.post('/chongwuyongpinOrder/add', data),
  // 提交订单（多商品）
  order: params => http.get('/chongwuyongpinOrder/order', params),
  // 发货
  deliver: id => http.post('/chongwuyongpinOrder/deliver', {}, { params: { id } }),
  // 确认收货
  receiving: id => http.get('/chongwuyongpinOrder/receiving', { id, t: Date.now() }),
  // 评价
  commentback: (id, commentbackText, chongwuyongpinCommentbackPingfenNumber) => http.get('/chongwuyongpinOrder/commentback', { id, commentbackText, chongwuyongpinCommentbackPingfenNumber, t: Date.now() })
}

// 通用相关API服务
export const commonApi = {
  // 后端列表
  getPage: params => http.get('/common/page', params),
  // 后端详情
  getInfo: id => http.get(`/common/info/${id}`),
  // 后端保存
  save: data => http.post('/common/save', data),
  // 后端修改
  update: data => http.post('/common/update', data),
  // 删除
  delete: ids => http.post('/common/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/common/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/common/list', params),
  // 前端详情
  getDetail: id => http.get(`/common/detail/${id}`),
  // 前端保存
  add: data => http.post('/common/add', data)
}

// 配置相关API服务
export const configApi = {
  // 后端列表
  getPage: params => http.get('/config/page', params),
  // 后端详情
  getInfo: id => http.get(`/config/info/${id}`),
  // 后端保存
  save: data => http.post('/config/save', data),
  // 后端修改
  update: data => http.post('/config/update', data),
  // 删除
  delete: ids => http.post('/config/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/config/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/config/list', params),
  // 前端详情
  getDetail: id => http.get(`/config/detail/${id}`),
  // 前端保存
  add: data => http.post('/config/add', data)
}

// 字典相关API服务
export const dictionaryApi = {
  // 后端列表
  getPage: params => http.get('/dictionary/page', params),
  // 后端详情
  getInfo: id => http.get(`/dictionary/info/${id}`),
  // 后端保存
  save: data => http.post('/dictionary/save', data),
  // 后端修改
  update: data => http.post('/dictionary/update', data),
  // 删除
  delete: ids => http.post('/dictionary/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/dictionary/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/dictionary/list', params),
  // 前端详情
  getDetail: id => http.get(`/dictionary/detail/${id}`),
  // 前端保存
  add: data => http.post('/dictionary/add', data),
  // 根据字典编码获取字典列表
  getByDicCode: dicCode => http.get('/dictionary/page', { dicCode, page: 1, limit: 100 })
}

// 文件相关API服务
export const fileApi = {
  // 上传文件
  upload: (type, file, onProgress) => http.upload('/file/upload', file, onProgress, { params: { type } }),
  // 下载文件
  download: fileName => http.get('/file/download', { params: { fileName } })
}

// 论坛相关API服务
export const forumApi = {
  // 后端列表
  getPage: params => http.get('/forum/page', params),
  // 后端详情
  getInfo: id => http.get(`/forum/info/${id}`),
  // 后端保存
  save: data => http.post('/forum/save', data),
  // 后端修改
  update: data => http.post('/forum/update', data),
  // 删除
  delete: ids => http.post('/forum/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/forum/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/forum/list', params),
  // 前端详情
  getDetail: id => http.get(`/forum/detail/${id}`),
  // 前端保存
  add: data => http.post('/forum/add', data)
}

// 资讯相关API服务
export const newsApi = {
  // 后端列表
  getPage: params => http.get('/news/page', params),
  // 后端详情
  getInfo: id => http.get(`/news/info/${id}`),
  // 后端保存
  save: data => http.post('/news/save', data),
  // 后端修改
  update: data => http.post('/news/update', data),
  // 删除
  delete: ids => http.post('/news/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/news/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/news/list', params),
  // 前端详情
  getDetail: id => http.get(`/news/detail/${id}`),
  // 前端保存
  add: data => http.post('/news/add', data)
}

// 用户相关API服务
export const usersApi = {
  // 后端列表
  getPage: params => http.get('/users/page', params),
  // 后端详情
  getInfo: id => http.get(`/users/info/${id}`),
  // 后端保存
  save: data => http.post('/users/save', data),
  // 后端修改
  update: data => http.post('/users/update', data),
  // 删除
  delete: ids => http.post('/users/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/users/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/users/list', params),
  // 前端详情
  getDetail: id => http.get(`/users/detail/${id}`),
  // 前端保存
  add: data => http.post('/users/add', data),
  // 获取用户会话信息
  session: () => http.get('/users/session'),
  // 管理员登录
  login: data => http.post('/users/login', {}, { params: data }),
  // 退出登录
  logout: () => http.get('/users/logout'),
}

// 顾客相关API服务
export const yonghuApi = {
  // 后端列表
  getPage: params => http.get('/yonghu/page', params),
  // 后端详情
  getInfo: id => http.get(`/yonghu/info/${id}`),
  // 后端保存
  save: data => http.post('/yonghu/save', data),
  // 后端修改
  update: data => http.post('/yonghu/update', data),
  // 删除
  delete: ids => http.post('/yonghu/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/yonghu/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/yonghu/list', params),
  // 前端详情
  getDetail: id => http.get(`/yonghu/detail/${id}`),
  // 前端保存
  add: data => http.post('/yonghu/add', data),
  // 密码重置
  resetPassword: id => http.get(`/yonghu/resetPassword?id=${id}`),
  // 用户登录
  login: (data, config) => http.post('/yonghu/login', data, config),
  // 用户注册
  register: data => http.post('/yonghu/register', data),
  // 修改密码
  updatePassword: (oldPassword, newPassword) => http.get(`/yonghu/updatePassword?oldPassword=${oldPassword}&newPassword=${newPassword}`),
  // 忘记密码
  resetPass: username => http.get(`/yonghu/resetPass?username=${username}`),
  // 获取用户session信息
  session: () => http.get('/yonghu/session'),
  // 退出登录
  logout: () => http.get('/yonghu/logout'),
}

// 宠物分类相关API服务
export const petCategoryApi = {
  // 后端列表
  getPage: params => http.get('/petCategory/page', params),
  // 后端详情
  getInfo: id => http.get(`/petCategory/info/${id}`),
  // 后端保存
  save: data => http.post('/petCategory/save', data),
  // 后端修改
  update: data => http.post('/petCategory/update', data),
  // 删除
  delete: ids => http.post('/petCategory/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/petCategory/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/petCategory/list', params),
  // 前端详情
  getDetail: id => http.get(`/petCategory/detail/${id}`),
  // 前端保存
  add: data => http.post('/petCategory/add', data),
  // 获取顶级分类
  getTopLevel: () => http.get('/petCategory/topLevel')
}

// 宠物相关API服务
export const petApi = {
  // 后端列表
  getPage: params => http.get('/pet/page', params),
  // 后端详情
  getInfo: id => http.get(`/pet/info/${id}`),
  // 后端保存
  save: data => http.post('/pet/save', data),
  // 后端修改
  update: data => http.post('/pet/update', data),
  // 删除
  delete: ids => http.post('/pet/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/pet/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/pet/list', params),
  // 前端详情
  getDetail: id => http.get(`/pet/detail/${id}`),
  // 前端保存
  add: data => http.post('/pet/add', data)
}

// 宠物订单相关API服务
export const petOrderApi = {
  // 后端列表
  getPage: params => http.get('/petOrder/page', params),
  // 后端详情
  getInfo: id => http.get(`/petOrder/info/${id}`),
  // 后端保存
  save: data => http.post('/petOrder/save', data),
  // 后端修改
  update: data => http.post('/petOrder/update', data),
  // 删除
  delete: ids => http.post('/petOrder/delete', ids),
  // 批量上传
  batchInsert: fileName => http.get('/petOrder/batchInsert', { fileName }),
  // 前端列表
  getList: params => http.get('/petOrder/list', params),
  // 前端详情
  getDetail: id => http.get(`/petOrder/detail/${id}`),
  // 前端保存
  add: data => http.post('/petOrder/add', data),
  // 获取用户宠物购买订单列表
  getUserOrders: (yonghuId, params = {}) => http.get('/petOrder/list', { ...params, userId: yonghuId, t: Date.now() }),
  // 更新订单状态
  updateStatus: (orderId, status) => http.post('/petOrder/update', { id: orderId, orderStatus: status }),
  // 提交订单
  order: params => http.get('/petOrder/order', params),
  // 支付订单
  pay: orderId => http.get('/petOrder/pay', { id: orderId, t: Date.now() })
}

// 导出所有API服务
export default {
  address: addressApi,
  cart: cartApi,
  chat: chatApi,
  chongwujiyang: chongwujiyangApi,
  chongwujiyangYuyue: chongwujiyangYuyueApi,
  chongwurizhi: chongwurizhiApi,
  chongwuyongpinCollection: chongwuyongpinCollectionApi,
  chongwuyongpinCommentback: chongwuyongpinCommentbackApi,
  chongwuyongpin: chongwuyongpinApi,
  chongwuyongpinOrder: chongwuyongpinOrderApi,
  common: commonApi,
  config: configApi,
  dictionary: dictionaryApi,
  file: fileApi,
  forum: forumApi,
  news: newsApi,
  petCategory: petCategoryApi,
  pet: petApi,
  petOrder: petOrderApi,
  users: usersApi,
  yonghu: yonghuApi
}

