<template>
  <div class="chongwujiyang-booking-page">
    <Header />
    <main class="chongwujiyang-booking-content">
      <div class="container">
        <!-- 页面标题 -->
        <div class="page-header">
          <h1>宠物寄养预约</h1>
          <p>为您的爱宠预约专业的寄养服务</p>
        </div>

        <!-- 加载中 -->
        <div v-if="loading" class="loading">
          <div class="loading-spinner"></div>
          <p>加载中...</p>
        </div>

        <!-- 预约表单 -->
        <div v-else-if="service" class="booking-form-container">
          <div class="booking-form">
            <!-- 服务信息 -->
            <div class="service-info-section">
              <h3>服务信息</h3>
              <div class="service-card">
                <div class="service-image">
                  <img :src="`http://localhost:8080/wangshangchongwudian/` + service.chongwujiyangPhoto" :alt="service.chongwujiyangName">
                </div>
                <div class="service-details">
                  <h4>{{ service.chongwujiyangName }}</h4>
                  <p class="service-type">{{ getServiceTypeName(service.chongwujiyangTypes) }}</p>
                  <p class="service-price">¥{{ service.chongwujiyangNewMoney }}/天</p>
                </div>
              </div>
            </div>

            <!-- 日期选择 -->
            <div class="date-section">
              <h3>寄养日期</h3>
              <div class="date-picker">
                <div class="date-item">
                  <label for="startDate">开始日期：</label>
                  <input type="date" id="startDate" v-model="form.startDate" @change="calculateDays">
                </div>
                <div class="date-item">
                  <label for="endDate">结束日期：</label>
                  <input type="date" id="endDate" v-model="form.endDate" @change="calculateDays">
                </div>
                <div class="date-item">
                  <label>寄养天数：</label>
                  <span class="days-count">{{ form.days }} 天</span>
                </div>
                <div class="date-item">
                  <label>预估费用：</label>
                  <span class="total-price">¥{{ form.totalPrice }}</span>
                </div>
              </div>
            </div>

            <!-- 宠物信息 -->
            <div class="pet-info-section">
              <h3>宠物信息</h3>
              <div class="form-group">
                <label for="petName">宠物名称：</label>
                <input type="text" id="petName" v-model="form.petName" placeholder="请输入宠物名称">
              </div>
              <div class="form-group">
                <label for="petType">宠物类型：</label>
                <select id="petType" v-model="form.petType">
                  <option value="">请选择宠物类型</option>
                  <option value="狗">狗</option>
                  <option value="猫">猫</option>
                  <option value="其他">其他</option>
                </select>
              </div>
              <div class="form-group">
                <label for="petBreed">宠物品种：</label>
                <input type="text" id="petBreed" v-model="form.petBreed" placeholder="请输入宠物品种">
              </div>
              <div class="form-group">
                <label for="petAge">宠物年龄：</label>
                <input type="text" id="petAge" v-model="form.petAge" placeholder="请输入宠物年龄">
              </div>
              <div class="form-group">
                <label for="petNotes">特殊说明：</label>
                <textarea id="petNotes" v-model="form.petNotes" placeholder="如有特殊需求，请在此说明"></textarea>
              </div>
            </div>

            <!-- 联系信息 -->
            <div class="contact-info-section">
              <h3>联系信息</h3>
              <div class="form-group">
                <label for="contactName">联系人：</label>
                <input type="text" id="contactName" v-model="form.contactName" placeholder="请输入联系人姓名">
              </div>
              <div class="form-group">
                <label for="contactPhone">联系电话：</label>
                <input type="tel" id="contactPhone" v-model="form.contactPhone" placeholder="请输入联系电话">
              </div>
              <div class="form-group">
                <label for="contactAddress">联系地址：</label>
                <input type="text" id="contactAddress" v-model="form.contactAddress" placeholder="请输入联系地址">
              </div>
            </div>

            <!-- 提交按钮 -->
            <div class="submit-section">
              <button class="btn btn-primary" @click="submitBooking" :disabled="!isFormValid">提交预约</button>
              <button class="btn btn-secondary" @click="backToDetail">返回详情</button>
            </div>
          </div>
        </div>

        <!-- 无数据 -->
        <div v-else class="no-data">
          <p>服务不存在</p>
        </div>
      </div>
    </main>
    <Footer />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { chongwujiyangApi, chongwujiyangYuyueApi, usersApi, dictionaryApi } from '../../utils/api'
import message from '../../utils/message'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'

const router = useRouter()
const route = useRoute()
const service = ref(null)
const loading = ref(false)
const serviceTypes = ref([])

// 表单数据
const form = ref({
  startDate: '',
  endDate: '',
  days: 0,
  totalPrice: 0,
  petName: '',
  petType: '',
  petBreed: '',
  petAge: '',
  petNotes: '',
  contactName: '',
  contactPhone: '',
  contactAddress: ''
})

// 计算表单是否有效
const isFormValid = computed(() => {
  return form.value.startDate && 
         form.value.endDate && 
         form.value.days > 0 && 
         form.value.petName && 
         form.value.petType && 
         form.value.contactName && 
         form.value.contactPhone
})

// 加载服务类型字典
const loadServiceTypes = async () => {
  try {
    const response = await dictionaryApi.getByDicCode('chongwujiyang_types')
    if (response.code === 0) {
      serviceTypes.value = response.data.list || []
    }
  } catch (error) {
    console.error('获取服务类型失败:', error)
  }
}

// 获取服务类型名称
const getServiceTypeName = (typeCode) => {
  const type = serviceTypes.value.find(t => t.codeIndex === typeCode)
  return type ? type.indexName : '未知类型'
}

// 获取服务详情
const getServiceDetail = async () => {
  try {
    loading.value = true
    const serviceId = route.params.id
    const response = await chongwujiyangApi.getDetail(serviceId)
    if (response.code === 0) {
      service.value = response.data
    } else {
      message.error('获取服务详情失败')
    }
  } catch (error) {
    console.error('获取服务详情失败:', error)
    message.error('获取服务详情失败')
  } finally {
    loading.value = false
  }
}

// 计算寄养天数和费用
const calculateDays = () => {
  if (form.value.startDate && form.value.endDate) {
    const start = new Date(form.value.startDate)
    const end = new Date(form.value.endDate)
    const diffTime = Math.abs(end - start)
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1 // 包含开始和结束日期
    
    if (diffDays > 0) {
      form.value.days = diffDays
      if (service.value) {
        form.value.totalPrice = (service.value.chongwujiyangNewMoney * diffDays).toFixed(2)
      }
    } else {
      form.value.days = 0
      form.value.totalPrice = 0
    }
  }
}

// 提交预约
const submitBooking = async () => {
  if (!isFormValid.value) {
    message.error('请填写完整的预约信息')
    return
  }

  try {
    // 获取用户会话信息
    const sessionResponse = await usersApi.session()
    if (sessionResponse.code === 0 && sessionResponse.data) {
      const yonghuId = sessionResponse.data.yonghuId || sessionResponse.data.id
      
      // 构建预约数据
      const bookingData = {
        yonghuId,
        chongwujiyangId: service.value.id,
        chongwujiyangYuyueName: service.value.chongwujiyangName,
        chongwujiyangYuyueTypes: service.value.chongwujiyangTypes,
        chongwujiyangYuyueMoney: form.value.totalPrice,
        chongwujiyangYuyueStarttime: form.value.startDate,
        chongwujiyangYuyueEndtime: form.value.endDate,
        chongwujiyangYuyuePetname: form.value.petName,
        chongwujiyangYuyuePettype: form.value.petType,
        chongwujiyangYuyuePetbreed: form.value.petBreed,
        chongwujiyangYuyuePetage: form.value.petAge,
        chongwujiyangYuyueBeizhu: form.value.petNotes,
        chongwujiyangYuyueContactname: form.value.contactName,
        chongwujiyangYuyueContactphone: form.value.contactPhone,
        chongwujiyangYuyueContactaddress: form.value.contactAddress,
        t: Date.now()
      }

      // 调用预约API
      const response = await chongwujiyangYuyueApi.add(bookingData)
      if (response.code === 0) {
        message.success('预约成功')
        // 跳转到订单列表
        router.push('/user/pet-orders')
      } else {
        message.error('预约失败')
      }
    } else {
      message.error('获取用户信息失败，请先登录')
      router.push('/user/login')
    }
  } catch (error) {
    console.error('提交预约失败:', error)
    message.error('提交预约失败')
  }
}

// 返回详情
const backToDetail = () => {
  router.push(`/chongwujiyang/detail/${route.params.id}`)
}

// 页面加载时获取服务类型和服务详情
onMounted(() => {
  loadServiceTypes()
  getServiceDetail()
})
</script>

<style scoped>
.chongwujiyang-booking-page {
  min-height: 100vh;
  background-color: var(--bg);
}

.chongwujiyang-booking-content {
  padding: var(--spacing-2xl) 0;
}

.page-header {
  text-align: center;
  margin-bottom: var(--spacing-2xl);
}

.page-header h1 {
  color: var(--text-1);
  font-size: var(--fs-2xl);
  font-weight: 600;
  margin-bottom: var(--spacing-sm);
}

.page-header p {
  color: var(--text-2);
  font-size: var(--fs-base);
  margin: 0;
}

.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-3xl) 0;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid var(--primary);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: var(--spacing-lg);
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.no-data {
  text-align: center;
  padding: var(--spacing-3xl) 0;
  color: var(--text-3);
  font-size: var(--fs-base);
}

.booking-form-container {
  background: var(--card);
  border-radius: var(--radius-base);
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  padding: var(--spacing-xl);
}

.booking-form {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xl);
}

.service-info-section h3,
.date-section h3,
.pet-info-section h3,
.contact-info-section h3 {
  color: var(--text-1);
  font-size: var(--fs-lg);
  font-weight: 600;
  margin-bottom: var(--spacing-lg);
  border-bottom: 1px solid var(--border);
  padding-bottom: var(--spacing-sm);
}

.service-card {
  display: flex;
  gap: var(--spacing-lg);
  padding: var(--spacing-base);
  background: white;
  border-radius: var(--radius-base);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.service-image {
  flex-shrink: 0;
  width: 100px;
  height: 100px;
  border-radius: var(--radius-base);
  overflow: hidden;
}

.service-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.service-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: var(--spacing-xs);
}

.service-details h4 {
  color: var(--text-1);
  font-size: var(--fs-base);
  font-weight: 600;
  margin: 0;
}

.service-type {
  color: var(--primary);
  font-size: var(--fs-sm);
  font-weight: 500;
  margin: 0;
}

.service-price {
  color: var(--danger);
  font-size: var(--fs-lg);
  font-weight: 600;
  margin: 0;
}

.date-picker {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: var(--spacing-base);
  background: white;
  padding: var(--spacing-base);
  border-radius: var(--radius-base);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.date-item {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
}

.date-item label {
  font-weight: 500;
  color: var(--text-1);
  font-size: var(--fs-sm);
}

.date-item input {
  padding: var(--spacing-xs) var(--spacing-base);
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: var(--fs-sm);
  background-color: white;
  color: var(--text-1);
}

.days-count {
  font-weight: 500;
  color: var(--text-1);
  font-size: var(--fs-base);
}

.total-price {
  color: var(--danger);
  font-size: var(--fs-lg);
  font-weight: 600;
}

.pet-info-section,
.contact-info-section {
  background: white;
  padding: var(--spacing-base);
  border-radius: var(--radius-base);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.form-group {
  margin-bottom: var(--spacing-base);
}

.form-group label {
  display: block;
  margin-bottom: var(--spacing-xs);
  font-weight: 500;
  color: var(--text-1);
  font-size: var(--fs-sm);
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: var(--spacing-xs) var(--spacing-base);
  border: 1px solid var(--border);
  border-radius: var(--radius-base);
  font-size: var(--fs-sm);
  background-color: white;
  color: var(--text-1);
}

.form-group textarea {
  resize: vertical;
  min-height: 100px;
}

.submit-section {
  display: flex;
  gap: var(--spacing-base);
  justify-content: center;
  margin-top: var(--spacing-lg);
}

.submit-section .btn {
  flex: 1;
  max-width: 200px;
  font-size: var(--fs-base);
  padding: var(--spacing-base) var(--spacing-lg);
}

.submit-section .btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .booking-form-container {
    padding: var(--spacing-lg);
  }
  
  .date-picker {
    grid-template-columns: 1fr;
  }
  
  .service-card {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .submit-section {
    flex-direction: column;
  }
  
  .submit-section .btn {
    max-width: 100%;
  }
}
</style>