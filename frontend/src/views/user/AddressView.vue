<template>
  <div class="address-page">
    <Header />
    <main class="address-content">
      <div class="container">
        <div class="address-header">
          <h1>收货地址管理</h1>
          <p>管理您的收货地址，方便购物时使用</p>
        </div>

        <div class="address-actions">
          <button class="btn btn-primary" @click="openAddModal">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 5V19M5 12H19" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            添加新地址
          </button>
        </div>

        <div class="address-list">
          <div v-if="loading" class="loading-state">
            <div class="loading-spinner"></div>
            <p>加载中...</p>
          </div>
          
          <div v-else-if="addresses.length === 0" class="empty-state">
            <svg width="80" height="80" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M10 17L15 12L10 7V17Z" stroke="var(--text-3)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M21 10C21 14.4183 16.9706 18 12 18C7.02944 18 3 14.4183 3 10C3 5.58172 7.02944 2 12 2C16.9706 2 21 5.58172 21 10Z" stroke="var(--text-3)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            <p>暂无收货地址</p>
            <button class="btn btn-primary" @click="openAddModal">添加第一个地址</button>
          </div>
          
          <div v-else class="address-cards">
            <div v-for="address in addresses" :key="address.id" class="address-card">
              <div class="address-header">
                <div class="address-name">
                  <h3>{{ address.addressName }}</h3>
                  <span class="address-phone">{{ address.addressPhone }}</span>
                </div>
                <div class="address-actions">
                  <button class="btn btn-sm btn-secondary" @click="openEditModal(address)">
                    编辑
                  </button>
                  <button class="btn btn-sm btn-danger" @click="confirmDelete(address.id)">
                    删除
                  </button>
                </div>
              </div>
              <div class="address-body">
                <p class="address-dizhi">{{ address.addressDizhi }}</p>
                <div class="address-default" v-if="address.isdefaultTypes === 1">
                  <span class="default-badge">默认地址</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 分页 -->
        <div v-if="!loading && addresses.length > 0" class="pagination">
          <button class="btn btn-sm btn-secondary" @click="changePage(page - 1)" :disabled="page === 1">
            上一页
          </button>
          <span class="page-info">第 {{ page }} 页，共 {{ totalPage }} 页</span>
          <button class="btn btn-sm btn-secondary" @click="changePage(page + 1)" :disabled="page === totalPage">
            下一页
          </button>
        </div>
      </div>
    </main>
    <Footer />

    <!-- 添加/编辑地址弹窗 -->
    <div class="modal" v-if="showModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>{{ editingAddress ? '编辑地址' : '添加地址' }}</h3>
          <button class="modal-close" @click="closeModal">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveAddress">
            <div class="form-group">
              <label>收货人</label>
              <input type="text" v-model="addressForm.addressName" required placeholder="请输入收货人姓名">
            </div>
            <div class="form-group">
              <label>手机号</label>
              <input type="tel" v-model="addressForm.addressPhone" required placeholder="请输入手机号">
            </div>
            <div class="form-group">
              <label>详细地址</label>
              <textarea v-model="addressForm.addressDizhi" required placeholder="请输入详细地址" rows="3"></textarea>
            </div>
            <div class="form-group">
              <label class="checkbox-label">
                <input type="checkbox" v-model="addressForm.isdefaultTypes" true-value="1" false-value="0">
                <span class="checkmark"></span>
                设置为默认地址
              </label>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="closeModal">取消</button>
              <button type="submit" class="btn btn-primary" :disabled="saving">
                {{ saving ? '保存中...' : '保存' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- 删除确认弹窗 -->
    <div class="modal" v-if="showDeleteModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>确认删除</h3>
          <button class="modal-close" @click="showDeleteModal = false">×</button>
        </div>
        <div class="modal-body">
          <p>确定要删除这个地址吗？</p>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showDeleteModal = false">取消</button>
          <button class="btn btn-danger" @click="deleteAddress" :disabled="deleting">
            {{ deleting ? '删除中...' : '删除' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { addressApi, yonghuApi } from '../../utils/api'
import message from '../../utils/message'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'

const router = useRouter()
const addresses = ref([])
const loading = ref(true)
const saving = ref(false)
const deleting = ref(false)
const showModal = ref(false)
const showDeleteModal = ref(false)
const editingAddress = ref(null)
const addressForm = ref({
  id: '',
  addressName: '',
  addressPhone: '',
  addressDizhi: '',
  isdefaultTypes: 0
})
const page = ref(1)
const limit = ref(5)
const totalPage = ref(1)
const userInfo = ref(null)
const deleteId = ref(null)

// 获取用户信息
const getUserInfo = async () => {
  try {
    const response = await yonghuApi.session()
    if (response.code === 0) {
      userInfo.value = response.data
      return response.data
    } else {
      message.error('获取用户信息失败')
      router.push('/user/login')
      return null
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    message.error('获取用户信息失败')
    router.push('/user/login')
    return null
  }
}

// 获取地址列表
const getAddresses = async () => {
  try {
    loading.value = true
    const user = await getUserInfo()
    if (!user) return
    
    const response = await addressApi.getPage({
      page: page.value,
      limit: limit.value,
      yonghu: user.id,
      t: Date.now()
    })
    
    if (response.code === 0) {
      addresses.value = response.data.list
      totalPage.value = response.data.totalPage
    } else {
      message.error('获取地址列表失败')
    }
  } catch (error) {
    console.error('获取地址列表失败:', error)
    message.error('获取地址列表失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 打开添加地址弹窗
const openAddModal = () => {
  editingAddress.value = null
  addressForm.value = {
    id: '',
    addressName: '',
    addressPhone: '',
    addressDizhi: '',
    isdefaultTypes: 0
  }
  showModal.value = true
}

// 打开编辑地址弹窗
const openEditModal = (address) => {
  editingAddress.value = address
  addressForm.value = {
    id: address.id,
    addressName: address.addressName,
    addressPhone: address.addressPhone,
    addressDizhi: address.addressDizhi,
    isdefaultTypes: address.isdefaultTypes
  }
  showModal.value = true
}

// 关闭弹窗
const closeModal = () => {
  showModal.value = false
  editingAddress.value = null
}

// 保存地址
const saveAddress = async () => {
  try {
    saving.value = true
    const user = await getUserInfo()
    if (!user) return
    
    const formData = {
      ...addressForm.value,
      yonghuId: user.id,
      t: Date.now()
    }
    
    let response
    if (editingAddress.value) {
      response = await addressApi.update(formData)
    } else {
      response = await addressApi.save(formData)
    }
    
    if (response.code === 0) {
      message.success(editingAddress.value ? '地址更新成功' : '地址添加成功')
      closeModal()
      getAddresses()
    } else {
      message.error(editingAddress.value ? '地址更新失败' : '地址添加失败')
    }
  } catch (error) {
    console.error('保存地址失败:', error)
    message.error('保存地址失败，请稍后重试')
  } finally {
    saving.value = false
  }
}

// 确认删除
const confirmDelete = (id) => {
  deleteId.value = id
  showDeleteModal.value = true
}

// 删除地址
const deleteAddress = async () => {
  try {
    deleting.value = true
    const response = await addressApi.delete([deleteId.value])
    if (response.code === 0) {
      message.success('地址删除成功')
      showDeleteModal.value = false
      getAddresses()
    } else {
      message.error('地址删除失败')
    }
  } catch (error) {
    console.error('删除地址失败:', error)
    message.error('删除地址失败，请稍后重试')
  } finally {
    deleting.value = false
  }
}

// 切换页码
const changePage = (newPage) => {
  if (newPage >= 1 && newPage <= totalPage.value) {
    page.value = newPage
    getAddresses()
  }
}

// 页面加载时获取地址列表
onMounted(() => {
  getAddresses()
})
</script>

<style scoped>
.address-page {
  min-height: 100vh;
  background-color: var(--bg);
}

.address-content {
  padding: var(--spacing-2xl) 0;
}

.address-header {
  text-align: center;
  margin-bottom: var(--spacing-2xl);
}

.address-header h1 {
  color: var(--text-1);
  font-size: var(--fs-2xl);
  font-weight: 600;
  margin-bottom: var(--spacing-sm);
}

.address-header p {
  color: var(--text-2);
  font-size: var(--fs-base);
  margin: 0;
}

.address-actions {
  display: flex;
  justify-content: flex-end;
  margin-bottom: var(--spacing-xl);
}

.address-list {
  margin-bottom: var(--spacing-xl);
}

.loading-state {
  text-align: center;
  padding: var(--spacing-3xl) 0;
}

.loading-spinner {
  width: 40px;
  height: 40px;
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

.empty-state {
  text-align: center;
  padding: var(--spacing-3xl) 0;
  color: var(--text-3);
}

.empty-state svg {
  margin-bottom: var(--spacing-lg);
  opacity: 0.5;
}

.empty-state p {
  margin-bottom: var(--spacing-lg);
  font-size: var(--fs-base);
}

.address-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: var(--spacing-lg);
}

.address-card {
  background: var(--card);
  border-radius: var(--radius-base);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  padding: var(--spacing-lg);
  transition: all 0.3s ease;
}

.address-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.address-card .address-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--spacing-base);
  text-align: left;
}

.address-name h3 {
  margin: 0 0 var(--spacing-xs) 0;
  color: var(--text-1);
  font-size: var(--fs-base);
  font-weight: 600;
}

.address-phone {
  font-size: var(--fs-sm);
  color: var(--text-2);
}

.address-card .address-actions {
  display: flex;
  gap: var(--spacing-xs);
  margin-bottom: 0;
}

.address-body {
  border-top: 1px solid var(--border);
  padding-top: var(--spacing-base);
}

.address-dizhi {
  margin: 0 0 var(--spacing-base) 0;
  color: var(--text-1);
  font-size: var(--fs-sm);
  line-height: 1.5;
}

.address-default {
  display: inline-block;
}

.default-badge {
  display: inline-block;
  padding: 2px 8px;
  background-color: var(--primary-light);
  color: var(--primary);
  font-size: var(--fs-xs);
  border-radius: var(--radius-full);
  font-weight: 500;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: var(--spacing-lg);
  margin-top: var(--spacing-xl);
}

.page-info {
  color: var(--text-2);
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
  max-width: 500px;
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
}

.form-group {
  margin-bottom: var(--spacing-lg);
}

.form-group label {
  display: block;
  margin-bottom: var(--spacing-xs);
  font-weight: 500;
  color: var(--text-1);
  font-size: var(--fs-sm);
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: var(--spacing-base);
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: var(--fs-base);
  transition: all 0.3s ease;
  background-color: white;
  color: var(--text-1);
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 2px rgba(66, 184, 131, 0.1);
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  cursor: pointer;
  font-size: var(--fs-sm);
  color: var(--text-1);
}

.checkbox-label input[type="checkbox"] {
  display: none;
  width: auto;
}

.checkmark {
  width: 16px;
  height: 16px;
  border: 1px solid var(--border);
  border-radius: 4px;
  position: relative;
  transition: all 0.3s ease;
}

.checkbox-label input[type="checkbox"]:checked + .checkmark {
  background-color: var(--primary);
  border-color: var(--primary);
}

.checkbox-label input[type="checkbox"]:checked + .checkmark::after {
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

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: var(--spacing-base);
  padding: var(--spacing-lg);
  border-top: 1px solid var(--border);
}

.btn {
  padding: var(--spacing-base) var(--spacing-xl);
  border: none;
  border-radius: var(--radius-base);
  font-size: var(--fs-base);
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary {
  background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(66, 184, 131, 0.3);
}

.btn-primary:hover:not(:disabled) {
  background: linear-gradient(135deg, var(--primary-dark) 0%, var(--primary) 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(66, 184, 131, 0.4);
}

.btn-secondary {
  background-color: var(--card);
  color: var(--text-1);
  border: 1px solid var(--border);
}

.btn-secondary:hover:not(:disabled) {
  background-color: var(--hover-bg);
  border-color: var(--primary);
  transform: translateY(-2px);
}

.btn-danger {
  background-color: var(--danger);
  color: white;
  box-shadow: 0 4px 12px rgba(255, 77, 79, 0.3);
}

.btn-danger:hover:not(:disabled) {
  background-color: var(--danger-dark);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(255, 77, 79, 0.4);
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.btn-sm {
  padding: var(--spacing-xs) var(--spacing-base);
  font-size: var(--fs-sm);
}

@media (max-width: 768px) {
  .address-content {
    padding: var(--spacing-xl) 0;
  }
  
  .address-cards {
    grid-template-columns: 1fr;
  }
  
  .modal-content {
    margin: 0 var(--spacing-base);
  }
  
  .address-actions {
    justify-content: center;
  }
}
</style>