<template>
  <div class="admin-products">
    <h2>商品管理</h2>
    <div class="card">
      <div class="card-body">
        <!-- 操作栏 -->
        <div class="action-section">
          <button class="btn btn-primary" @click="handleAdd">
            新增商品
          </button>
        </div>
        
        <!-- 搜索表单 -->
        <div class="search-section">
          <form @submit.prevent="handleSearch">
            <div class="form-row">
              <div class="form-group">
                <label for="search-name">商品名称</label>
                <input 
                  type="text" 
                  id="search-name" 
                  v-model="searchParams.name"
                  placeholder="请输入商品名称进行搜索"
                >
              </div>
              <div class="form-group">
                <label for="search-category">分类</label>
                <select 
                  id="search-category" 
                  v-model="searchParams.category"
                  style="width: 100%; padding: 12px 16px; border: 1px solid var(--border); border-radius: var(--radius-base); font-size: 14px;"
                >
                  <option value="">全部</option>
                  <option v-for="type in productTypes" :key="type.id" :value="type.codeIndex">
                    {{ type.indexName }}
                  </option>
                </select>
              </div>
              <div class="form-group">
                <label for="search-shangxia">上架状态</label>
                <select 
                  id="search-shangxia" 
                  v-model="searchParams.shangxiaTypes"
                  style="width: 100%; padding: 12px 16px; border: 1px solid var(--border); border-radius: var(--radius-base); font-size: 14px;"
                >
                  <option value="">全部</option>
                  <option value="1">上架</option>
                  <option value="2">下架</option>
                </select>
              </div>
              <div class="form-group">
                <label for="search-delete">删除状态</label>
                <select 
                  id="search-delete" 
                  v-model="searchParams.chongwuyongpinDelete"
                  style="width: 100%; padding: 12px 16px; border: 1px solid var(--border); border-radius: var(--radius-base); font-size: 14px;"
                >
                  <option value="">全部</option>
                  <option value="1">已删除</option>
                  <option value="0">未删除</option>
                </select>
              </div>
              <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                  搜索
                </button>
                <button type="button" class="btn btn-secondary" @click="handleReset">
                  重置
                </button>
              </div>
            </div>
          </form>
        </div>
        
        <div class="table-container">
          <table class="admin-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>图片</th>
                <th>编号</th>
                <th>商品名称</th>
                <th>原价</th>
                <th>现价</th>
                <th>库存</th>
                <th>热度</th>
                <th>分类</th>
                <th>状态</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="product in products" :key="product.id">
                <td>{{ product.id }}</td>
                <td>
                  <img v-if="product.photo" :src="'http://localhost:8080/wangshangchongwudian/' + product.photo" alt="商品图片" style="max-width: 60px; max-height: 60px;">
                  <span v-else>无图片</span>
                </td>
                <td>{{ product.uuid }}</td>
                <td>{{ product.name }}</td>
                <td>¥{{ product.oldPrice.toFixed(2) }}</td>
                <td>¥{{ product.price.toFixed(2) }}</td>
                <td>{{ product.stock }}</td>
                <td>{{ product.clickNum }}</td>
                <td>{{ product.category }}</td>
                <td>
                  <span class="status-badge" :class="product.status ? 'status-active' : 'status-inactive'">
                    {{ product.status ? '上架' : '下架' }}
                  </span>
                </td>
                <td>
                  <button class="btn btn-primary btn-sm" @click="handleDetail(product)">
                    详情
                  </button>
                  <button class="btn btn-secondary btn-sm" @click="handleEdit(product)">
                    编辑
                  </button>
                  <button 
                    class="btn btn-sm" 
                    :class="product.status ? 'btn-warning' : 'btn-success'"
                    @click="handleStatusChange(product)"
                  >
                    {{ product.status ? '下架' : '上架' }}
                  </button>
                  <button 
                    class="btn btn-danger btn-sm" 
                    @click="handleDelete(product)"
                  >
                    删除
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        
        <!-- 分页 -->
        <div class="pagination">
          <span>共 {{ total }} 条记录</span>
        </div>
      </div>
    </div>
    
    <!-- 编辑弹窗 -->
    <div v-if="showEditModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <div class="modal-header">
          <h3>{{ editingProduct.id ? '编辑商品' : '添加商品' }}</h3>
          <button class="modal-close" @click="closeModal">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSave">
            <div class="form-group">
                <label for="edit-photo">商品图片</label>
                <div class="avatar-upload-section">
                  <div class="avatar-preview" @click="$refs.photoInput.click()">
                    <img v-if="editingProduct.photo" :src="'http://localhost:8080/wangshangchongwudian/' + editingProduct.photo" alt="商品图片" style="max-width: 100px; max-height: 100px;">
                    <span v-else class="avatar-placeholder">点击上传图片</span>
                  </div>
                  <input 
                    type="file" 
                    id="edit-photo" 
                    ref="photoInput"
                    accept="image/*"
                    @change="handlePhotoUpload"
                    style="display: none"
                  >
                  <button 
                    type="button" 
                    class="btn btn-secondary btn-sm" 
                    @click="removePhoto"
                    v-if="editingProduct.photo"
                  >
                    删除图片
                  </button>
                </div>
              </div>
            
            <div class="form-row">
              <div class="form-group">
                <label for="edit-name">商品名称</label>
                <input 
                  type="text" 
                  id="edit-name" 
                  v-model="editingProduct.name" 
                  required
                >
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label for="edit-uuid">商品编号</label>
                <input 
                  type="text" 
                  id="edit-uuid" 
                  v-model="editingProduct.uuid" 
                  required
                >
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label for="edit-category">分类</label>
                <select 
                  id="edit-category" 
                  v-model="editingProduct.category"
                  required
                >
                  <option value="">请选择分类</option>
                  <option v-for="type in productTypes" :key="type.id" :value="type.codeIndex">
                    {{ type.indexName }}
                  </option>
                </select>
              </div>
              <div class="form-group">
                <label for="edit-status">状态</label>
                <select 
                  id="edit-status" 
                  v-model="editingProduct.status"
                >
                  <option :value="true">上架</option>
                  <option :value="false">下架</option>
                </select>
              </div>
            </div>
            
            <div class="form-row">
              <div class="form-group">
                <label for="edit-old-price">原价</label>
                <input 
                  type="number" 
                  id="edit-old-price" 
                  v-model="editingProduct.oldPrice"
                  step="0.01"
                  required
                >
              </div>
              <div class="form-group">
                <label for="edit-price">现价</label>
                <input 
                  type="number" 
                  id="edit-price" 
                  v-model="editingProduct.price"
                  step="0.01"
                  required
                >
              </div>
              <div class="form-group">
                <label for="edit-stock">库存</label>
                <input 
                  type="number" 
                  id="edit-stock" 
                  v-model="editingProduct.stock"
                  required
                >
              </div>
            </div>
            
            <div class="form-row">
              <div class="form-group">
                <label for="edit-click-num">热度</label>
                <input 
                  type="number" 
                  id="edit-click-num" 
                  v-model="editingProduct.clickNum"
                  min="0"
                  required
                >
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label for="edit-description">商品描述</label>
                <textarea 
                  id="edit-description" 
                  v-model="editingProduct.description"
                  rows="4"
                  placeholder="请输入商品描述"
                ></textarea>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="closeModal">
                取消
              </button>
              <button type="submit" class="btn btn-primary" :disabled="isLoading">
                {{ isLoading ? '保存中...' : '保存' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
    <!-- 详情弹窗 -->
    <div v-if="showDetailModal" class="modal-overlay" @click.self="closeDetailModal">
      <div class="modal">
        <div class="modal-header">
          <h3>商品详情</h3>
          <button class="modal-close" @click="closeDetailModal">×</button>
        </div>
        <div class="modal-body">
          <div class="detail-content">
            <div class="detail-row">
              <div class="detail-label">ID</div>
              <div class="detail-value">{{ detailProduct.id }}</div>
            </div>
            <div class="detail-row">
              <div class="detail-label">商品名称</div>
              <div class="detail-value">{{ detailProduct.name }}</div>
            </div>
            <div class="detail-row">
              <div class="detail-label">商品编号</div>
              <div class="detail-value">{{ detailProduct.uuid }}</div>
            </div>
            <div class="detail-row">
              <div class="detail-label">分类</div>
              <div class="detail-value">{{ detailProduct.category }}</div>
            </div>
            <div class="detail-row">
              <div class="detail-label">原价</div>
              <div class="detail-value">¥{{ detailProduct.oldPrice ? detailProduct.oldPrice.toFixed(2) : '0.00' }}</div>
            </div>
            <div class="detail-row">
              <div class="detail-label">现价</div>
              <div class="detail-value">¥{{ detailProduct.price ? detailProduct.price.toFixed(2) : '0.00' }}</div>
            </div>
            <div class="detail-row">
              <div class="detail-label">库存</div>
              <div class="detail-value">{{ detailProduct.stock || 0 }}</div>
            </div>
            <div class="detail-row">
              <div class="detail-label">热度</div>
              <div class="detail-value">{{ detailProduct.clickNum || 0 }}</div>
            </div>
            <div class="detail-row">
              <div class="detail-label">状态</div>
              <div class="detail-value">
                <span class="status-badge" :class="detailProduct.status ? 'status-active' : 'status-inactive'">
                  {{ detailProduct.status ? '上架' : '下架' }}
                </span>
              </div>
            </div>
            <div class="detail-row detail-photo">
              <div class="detail-label">商品图片</div>
              <div class="detail-value">
                <img v-if="detailProduct.photo" :src="'http://localhost:8080/wangshangchongwudian/' + detailProduct.photo" alt="商品图片" style="max-width: 100px; max-height: 100px;">
                <span v-else class="no-photo">暂无图片</span>
              </div>
            </div>
            <div class="detail-row detail-description">
              <div class="detail-label">商品描述</div>
              <div class="detail-value">{{ detailProduct.description || '暂无描述' }}</div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" @click="closeDetailModal">
            关闭
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { chongwuyongpinApi, dictionaryApi, fileApi } from '../../utils/api'
import message from '../../utils/message'
import confirm from '../../utils/confirm'

// 商品列表
const products = ref([])
// 总数
const total = ref(0)
// 加载状态
const isLoading = ref(false)
// 编辑弹窗
const showEditModal = ref(false)
// 详情弹窗
const showDetailModal = ref(false)
// 商品类型字典
const productTypes = ref([])
// 编辑数据
const editingProduct = ref({
  id: '',
  name: '',
  uuid: '',
  category: '',
  oldPrice: 0,
  price: 0,
  stock: 0,
  clickNum: 0,
  description: '',
  status: true,
  photo: ''
})
// 详情数据
const detailProduct = ref({})

// 搜索参数
const searchParams = ref({
  page: 1,
  limit: 10,
  sort: 'id',
  name: '',
  category: '',
  shangxiaTypes: '',
  chongwuyongpinDelete: ''
})

// 获取商品类型字典
const getProductTypes = async () => {
  try {
    const response = await dictionaryApi.getByDicCode('chongwuyongpin_types')
    if (response.code === 0) {
      productTypes.value = response.data.list || []
    }
  } catch (error) {
    console.error('获取商品类型字典失败:', error)
  }
}

// 获取商品列表
const getProductsList = async () => {
  try {
    isLoading.value = true
    
    // 准备查询参数
    const params = {
      page: searchParams.value.page,
      limit: searchParams.value.limit,
      sort: searchParams.value.sort
    }
    
    // 添加搜索条件
    if (searchParams.value.name) params.chongwuyongpinName = searchParams.value.name
    if (searchParams.value.category) params.chongwuyongpinTypes = parseInt(searchParams.value.category)
    if (searchParams.value.shangxiaTypes !== '') params.shangxiaTypes = parseInt(searchParams.value.shangxiaTypes)
    if (searchParams.value.chongwuyongpinDelete !== '') params.chongwuyongpinDelete = parseInt(searchParams.value.chongwuyongpinDelete)
    
    // 使用封装的API方法调用
    const response = await chongwuyongpinApi.getPage(params)
    if (response.code === 0) {
      // 转换API返回的数据结构到组件需要的结构
      products.value = (response.data.list || []).map(item => ({
        id: item.id,
        uuid: item.chongwuyongpinUuidNumber,
        name: item.chongwuyongpinName,
        category: item.chongwuyongpinValue || '',
        oldPrice: parseFloat(item.chongwuyongpinOldMoney),
        price: parseFloat(item.chongwuyongpinNewMoney),
        stock: item.chongwuyongpinKucunNumber,
        clickNum: item.chongwuyongpinClicknum,
        description: item.chongwuyongpinContent,
        status: item.shangxiaTypes === 1 ? true : false,
        photo: item.chongwuyongpinPhoto
      }))
      total.value = response.data.total || 0
    }
  } catch (error) {
    console.error('获取商品列表失败:', error)
    message.error(error.msg || '获取商品列表失败')
    // 确保products和total有默认值
    products.value = []
    total.value = 0
  } finally {
    isLoading.value = false
  }
}

// 处理搜索
const handleSearch = () => {
  // 重置页码为1
  searchParams.value.page = 1
  getProductsList()
}

// 处理重置
const handleReset = () => {
  // 重置搜索参数
  searchParams.value = {
    page: 1,
    limit: 10,
    sort: 'id',
    name: '',
    category: '',
    shangxiaTypes: '',
    chongwuyongpinDelete: ''
  }
  getProductsList()
}

// 处理新增
const handleAdd = () => {
  // 重置编辑数据
  editingProduct.value = {
    id: '',
    name: '',
    uuid: '',
    category: '',
    oldPrice: 0,
    price: 0,
    stock: 0,
    clickNum: 0,
    description: '',
    status: true,
    photo: ''
  }
  // 打开编辑弹窗
  showEditModal.value = true
}

// 处理编辑
const handleEdit = async (product) => {
  try {
    const response = await chongwuyongpinApi.getInfo(product.id)
    if (response.code === 0) {
      // 转换API返回的数据结构到组件需要的结构
      const data = response.data
      editingProduct.value = {
        id: data.id,
        uuid: data.chongwuyongpinUuidNumber,
        name: data.chongwuyongpinName,
        category: data.chongwuyongpinTypes || '',
        oldPrice: parseFloat(data.chongwuyongpinOldMoney),
        price: parseFloat(data.chongwuyongpinNewMoney),
        stock: data.chongwuyongpinKucunNumber,
        clickNum: data.chongwuyongpinClicknum,
        description: data.chongwuyongpinContent,
        status: data.shangxiaTypes === 1 ? true : false,
        photo: data.chongwuyongpinPhoto
      }
      showEditModal.value = true
    }
  } catch (error) {
    console.error('获取商品详情失败:', error)
    message.error(error.msg || '获取商品详情失败')
  }
}

// 处理删除
const handleDelete = async (product) => {
  const result = await confirm({
    message: `确定要删除商品 ${product.name} 吗？`
  })
  
  if (result) {
    try {
      const response = await chongwuyongpinApi.delete([product.id])
      if (response.code === 0) {
        message.success('删除成功')
        getProductsList()
      } else {
        message.error(response.msg || '删除失败')
      }
    } catch (error) {
      console.error('删除商品失败:', error)
      message.error(error.msg || '删除商品失败')
    }
  }
}

// 保存编辑
const handleSave = async () => {
  try {
    isLoading.value = true
    // 转换组件数据结构到API需要的结构
    const data = {
      id: editingProduct.value.id,
      chongwuyongpinName: editingProduct.value.name,
      chongwuyongpinUuidNumber: editingProduct.value.uuid,
      chongwuyongpinTypes: editingProduct.value.category,
      chongwuyongpinOldMoney: editingProduct.value.oldPrice,
      chongwuyongpinNewMoney: editingProduct.value.price,
      chongwuyongpinKucunNumber: editingProduct.value.stock,
      chongwuyongpinClicknum: editingProduct.value.clickNum,
      chongwuyongpinContent: editingProduct.value.description,
      shangxiaTypes: editingProduct.value.status ? 1 : 2,
      chongwuyongpinPhoto: editingProduct.value.photo || ''
    }
    
    let response
    if (data.id) {
      // 编辑
      response = await chongwuyongpinApi.update(data)
    } else {
      // 添加
      response = await chongwuyongpinApi.save(data)
    }
    
    if (response.code === 0) {
      message.success('保存成功')
      closeModal()
      getProductsList()
    } else {
      message.error(response.msg || '保存失败')
    }
  } catch (error) {
    console.error('保存商品失败:', error)
    message.error(error.msg || '保存商品失败')
  } finally {
    isLoading.value = false
  }
}

// 关闭弹窗
const closeModal = () => {
  showEditModal.value = false
  // 重置表单
  editingProduct.value = {
    id: '',
    name: '',
    uuid: '',
    category: '',
    oldPrice: 0,
    price: 0,
    stock: 0,
    clickNum: 0,
    description: '',
    status: true,
    photo: ''
  }
}

// 图片上传处理
const handlePhotoUpload = async (e) => {
  const file = e.target.files[0]
  if (!file) return
  
  try {
    isLoading.value = true
    // 调用上传接口，使用product类型
    const response = await fileApi.upload('product', file)
    if (response.code === 0) {
      // 上传成功，设置图片路径
      // 根据实际响应结构，使用response.file
      editingProduct.value.photo = 'upload/' + response.file
      message.success('图片上传成功')
    } else {
      message.error(response.msg || '图片上传失败')
    }
  } catch (error) {
    console.error('图片上传失败:', error)
    message.error('图片上传失败')
  } finally {
    isLoading.value = false
    // 清空文件输入
    e.target.value = ''
  }
}

// 删除图片
const removePhoto = () => {
  editingProduct.value.photo = ''
  message.success('图片已删除')
}

// 处理上架下架状态切换
const handleStatusChange = async (product) => {
  try {
    isLoading.value = true
    // 准备更新数据，只修改状态字段
    const updateData = {
      id: product.id,
      shangxiaTypes: product.status ? 2 : 1 // 1=上架，2=下架
    }
    
    // 调用更新接口
    const response = await chongwuyongpinApi.update(updateData)
    if (response.code === 0) {
      message.success(product.status ? '下架成功' : '上架成功')
      // 刷新商品列表
      getProductsList()
    } else {
      message.error(response.msg || '状态更新失败')
    }
  } catch (error) {
    console.error('状态更新失败:', error)
    message.error('状态更新失败')
  } finally {
    isLoading.value = false
  }
}

// 处理详情查看
const handleDetail = async (product) => {
  try {
    isLoading.value = true
    // 调用API获取商品详情
    const response = await chongwuyongpinApi.getInfo(product.id)
    if (response.code === 0) {
      // 转换API返回的数据结构到组件需要的结构
      const data = response.data
      detailProduct.value = {
        id: data.id,
        name: data.chongwuyongpinName,
        uuid: data.chongwuyongpinUuidNumber,
        category: data.chongwuyongpinValue,
        oldPrice: parseFloat(data.chongwuyongpinOldMoney),
        price: parseFloat(data.chongwuyongpinNewMoney),
        stock: data.chongwuyongpinKucunNumber,
        clickNum: data.chongwuyongpinClicknum,
        description: data.chongwuyongpinContent,
        status: data.shangxiaTypes === 1,
        photo: data.chongwuyongpinPhoto
      }
      // 打开详情弹窗
      showDetailModal.value = true
    } else {
      message.error(response.msg || '获取商品详情失败')
    }
  } catch (error) {
    console.error('获取商品详情失败:', error)
    message.error('获取商品详情失败')
  } finally {
    isLoading.value = false
  }
}

// 关闭详情弹窗
const closeDetailModal = () => {
  showDetailModal.value = false
  // 重置详情数据
  detailProduct.value = {}
}

// 组件挂载时获取数据
onMounted(async () => {
  await getProductTypes()
  await getProductsList()
})
</script>

<style scoped>
.admin-products {
  padding: 0;
}

/* 主卡片容器 */
.card {
  background-color: var(--card);
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-base);
  overflow: hidden;
}

.card-body {
  padding: 24px;
}

/* 操作栏样式 */
.action-section {
  margin-bottom: 24px;
  display: flex;
  justify-content: flex-start;
  gap: 12px;
}

/* 搜索区域样式 */
.search-section {
  margin-bottom: 24px;
  background-color: white;
  padding: 20px;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
}

.form-row {
  display: flex;
  align-items: flex-end;
  gap: 24px;
  flex-wrap: wrap;
}

.form-group {
  flex: 1;
  min-width: 200px;
  max-width: 300px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--text-1);
  font-size: 14px;
}

.form-group input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  transition: all 0.3s ease;
  background-color: white;
}

.form-group input:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.form-actions {
  display: flex;
  gap: 12px;
}

/* 表格容器 */
.table-container {
  overflow-x: auto;
  margin-bottom: 24px;
  background-color: white;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
}

.admin-table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
}

.admin-table th,
.admin-table td {
  padding: 16px 20px;
  text-align: left;
  border-bottom: 1px solid var(--border);
  font-size: 14px;
}

.admin-table th {
  background-color: var(--bg);
  font-weight: 600;
  color: var(--text-1);
  font-size: 14px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.admin-table tr:last-child td {
  border-bottom: none;
}

.admin-table tr:hover {
  background-color: var(--hover-bg);
  transition: background-color 0.2s ease;
}

/* 按钮样式增强 */
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 10px 16px;
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

.btn-primary:hover {
  background-color: var(--primary-dark);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(66, 184, 131, 0.3);
}

.btn-secondary {
  background-color: white;
  color: var(--text-1);
  border: 1px solid var(--border);
}

.btn-secondary:hover {
  background-color: var(--hover-bg);
  border-color: var(--primary);
  color: var(--primary);
}

.btn-danger {
  background-color: white;
  color: var(--danger);
  border: 1px solid var(--danger);
}

.btn-danger:hover {
  background-color: rgba(255, 77, 79, 0.1);
}

.btn-warning {
  background-color: white;
  color: var(--warning);
  border: 1px solid var(--warning);
}

.btn-warning:hover {
  background-color: rgba(255, 152, 0, 0.1);
}

.btn-success {
  background-color: white;
  color: var(--success);
  border: 1px solid var(--success);
}

.btn-success:hover {
  background-color: rgba(76, 175, 80, 0.1);
}

.btn-sm {
  padding: 6px 12px;
  font-size: 12px;
  margin-right: 8px;
}

/* 分页样式 */
.pagination {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 16px;
  margin-top: 24px;
  font-size: 14px;
  color: var(--text-2);
}

/* 弹窗样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(2px);
}

.modal {
  background-color: var(--card);
  border-radius: var(--radius-base);
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
  width: 100%;
  max-width: 800px;
  max-height: 90vh;
  margin: 0;
  animation: modalSlideIn 0.3s ease-out;
  display: flex;
  flex-direction: column;
}

@keyframes modalSlideIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid var(--border);
  background-color: var(--bg);
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: var(--text-1);
}

.modal-close {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: var(--text-3);
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius-base);
  transition: all 0.3s ease;
}

.modal-close:hover {
  background-color: var(--hover-bg);
  color: var(--text-1);
}

.modal-body {
  padding: 24px;
  flex: 1;
  overflow-y: auto;
  max-height: calc(90vh - 120px);
}

.modal-body .form-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.modal-body .form-group {
  margin-bottom: 20px;
  max-width: none;
  flex: 1;
  min-width: 0;
}

.modal-body .form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--text-1);
  font-size: 14px;
}

.modal-body .form-group input,
.modal-body .form-group textarea,
.modal-body .form-group select {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: 14px;
  transition: all 0.3s ease;
  background-color: white;
}

.modal-body .form-group input:focus,
.modal-body .form-group textarea:focus,
.modal-body .form-group select:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
  padding: 20px 24px;
  border-top: 1px solid var(--border);
  background-color: var(--bg);
}

/* 状态标签样式 */
.status-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: var(--radius-full);
  font-size: 12px;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.status-active {
  background-color: rgba(82, 196, 26, 0.1);
  color: var(--success);
}

.status-inactive {
  background-color: rgba(144, 164, 174, 0.1);
  color: var(--text-3);
}

/* 商品图片样式 */
.product-avatar {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-sm);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.product-avatar:hover {
  transform: scale(1.05);
  box-shadow: var(--shadow-base);
}

.no-photo {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 60px;
  background-color: var(--hover-bg);
  border: 2px dashed var(--border);
  border-radius: var(--radius-base);
  color: var(--text-3);
  font-size: 12px;
  font-weight: 500;
  transition: all 0.2s ease;
}

.no-photo:hover {
  background-color: var(--border);
  color: var(--text-2);
}

/* 图片上传样式 */
.avatar-upload-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-top: 8px;
}

.avatar-preview {
  width: 200px;
  height: 200px;
  border: 2px dashed var(--border);
  border-radius: var(--radius-base);
  overflow: hidden;
  cursor: pointer;
  position: relative;
  transition: all 0.3s ease;
  background-color: var(--hover-bg);
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-preview:hover {
  border-color: var(--primary);
  background-color: rgba(66, 184, 131, 0.05);
  transform: translateY(-2px);
  box-shadow: var(--shadow-sm);
}

.avatar-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.avatar-preview:hover img {
  transform: scale(1.02);
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--hover-bg);
  color: var(--text-3);
  font-size: 14px;
  font-weight: 500;
  text-align: center;
  padding: 20px;
  box-sizing: border-box;
  transition: all 0.3s ease;
}

.avatar-preview:hover .avatar-placeholder {
  color: var(--primary);
}

/* 列表中图片样式 */
.table-container .product-avatar {
  width: 50px;
  height: 50px;
  border: 1px solid var(--border);
}

/* 详情弹窗样式 */
.detail-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.detail-row {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: flex-start;
}

.detail-label {
  min-width: 120px;
  font-weight: 600;
  color: var(--text-1);
  font-size: 14px;
  flex-shrink: 0;
  padding-top: 8px;
}

.detail-value {
  flex: 1;
  color: var(--text-2);
  font-size: 14px;
  line-height: 1.5;
  padding-top: 8px;
}

.detail-photo {
  align-items: flex-start;
}

.detail-photo-img {
  max-width: 200px;
  max-height: 200px;
  object-fit: cover;
  border-radius: var(--radius-base);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
}

.detail-description .detail-value {
  white-space: pre-wrap;
  background-color: var(--hover-bg);
  padding: 12px;
  border-radius: var(--radius-base);
  border: 1px solid var(--border);
  min-height: 80px;
}

/* 图片上传容器 */
.photo-upload-container {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-top: 8px;
}

.photo-preview {
  width: 200px;
  height: 200px;
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  overflow: hidden;
  cursor: pointer;
  position: relative;
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.preview-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--hover-bg);
  color: var(--text-3);
}

.photo-input {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  cursor: pointer;
}

.photo-preview:hover .photo-input {
  opacity: 0.01;
}
</style>