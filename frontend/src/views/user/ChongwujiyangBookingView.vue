<template>
  <div class="chongwujiyang-booking-page">
    <Header />
    <main class="chongwujiyang-booking-content">
      <div class="container">
        <div class="page-header">
          <h1>宠物寄养预约</h1>
          <p>为您的爱宠预约专业的寄养服务</p>
        </div>

        <div v-if="loading" class="loading">
          <div class="loading-spinner"></div>
          <p>加载中...</p>
        </div>

        <div v-else-if="service" class="booking-form-container">
          <div class="booking-form">
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

            <div class="date-section">
              <h3>寄养日期</h3>
              <div class="date-picker">
                <div class="date-item">
                  <label for="startDate">寄养开始时间：</label>
                  <input type="datetime-local" id="startDate" v-model="form.chongwujiyangYuyueTime">
                </div>
                <div class="date-item">
                  <label>寄养天数：</label>
                  <input type="number" id="days" v-model.number="form.chongwujiyangYuyueNum" min="1" @change="calculateTotalPrice">
                </div>
                <div class="date-item full-width">
                  <label>预估费用：</label>
                  <span class="total-price">¥{{ form.chongwujiyangYuyuePrice }}</span>
                </div>
              </div>
            </div>

            <div class="pet-info-section">
              <h3>宠物信息</h3>
              <div class="form-group">
                <label for="petName">宠物名称：</label>
                <input type="text" id="petName" v-model="form.chongwujiyangYuyueName" placeholder="请输入宠物名称">
              </div>
              <div class="form-group">
                <label for="petType">宠物类型：</label>
                <select id="petType" v-model="form.chongwuTypes">
                  <option value="">请选择宠物类型</option>
                  <option v-for="type in petTypeOptions" :key="type.codeIndex" :value="type.codeIndex">
                    {{ type.indexName }}
                  </option>
                </select>
              </div>
              <div class="form-group">
                <label for="petWeight">宠物重量(kg)：</label>
                <input type="number" id="petWeight" v-model.number="form.chongwuZhongliang" placeholder="请输入宠物重量">
              </div>
            </div>

            <div class="service-option-section">
              <h3>接送服务</h3>
              <div class="form-group">
                <label for="shifouTypes">是否接送：</label>
                <select id="shifouTypes" v-model="form.shifouTypes">
                  <option value="">请选择</option>
                  <option v-for="option in shifouTypeOptions" :key="option.codeIndex" :value="option.codeIndex">
                    {{ option.indexName }}
                  </option>
                </select>
              </div>
            </div>

            <div class="submit-section">
              <button class="btn btn-primary" @click="submitBooking" :disabled="!isFormValid">提交预约</button>
              <button class="btn btn-secondary" @click="backToDetail">返回详情</button>
            </div>
          </div>
        </div>

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
import { chongwujiyangApi, chongwujiyangYuyueApi, yonghuApi, dictionaryApi } from '../../utils/api'
import message from '../../utils/message'
import Header from '../../components/Header.vue'
import Footer from '../../components/Footer.vue'

const router = useRouter()
const route = useRoute()
const service = ref(null)
const loading = ref(false)
const serviceTypes = ref([])
const petTypeOptions = ref([])
const shifouTypeOptions = ref([])

const form = ref({
  chongwujiyangId: null,
  yonghuId: null,
  chongwujiyangYuyueName: '',
  chongwuTypes: null,
  chongwuZhongliang: null,
  chongwujiyangYuyueTime: '',
  chongwujiyangYuyueNum: 1,
  shifouTypes: null,
  chongwujiyangYuyuePrice: 0
})

const isFormValid = computed(() => {
  return form.value.chongwujiyangYuyueName && 
         form.value.chongwuTypes !== null && 
         form.value.chongwuZhongliang !== null && 
         form.value.chongwujiyangYuyueTime && 
         form.value.chongwujiyangYuyueNum > 0 && 
         form.value.shifouTypes !== null
})

const loadDictionaryData = async () => {
  try {
    const serviceTypeResponse = await dictionaryApi.getPage({
      dicCode: 'chongwujiyang_types',
      page: 1,
      limit: 100
    })
    if (serviceTypeResponse.code === 0) {
      serviceTypes.value = serviceTypeResponse.data.list || []
    }
    
    const petTypeResponse = await dictionaryApi.getPage({
      dicCode: 'chongwu_types',
      page: 1,
      limit: 100
    })
    if (petTypeResponse.code === 0) {
      petTypeOptions.value = petTypeResponse.data.list || []
    }
    
    const shifouTypeResponse = await dictionaryApi.getPage({
      dicCode: 'shifou_types',
      page: 1,
      limit: 100
    })
    if (shifouTypeResponse.code === 0) {
      shifouTypeOptions.value = shifouTypeResponse.data.list || []
    }
  } catch (error) {
    console.error('获取字典数据失败:', error)
  }
}

const getServiceTypeName = (typeCode) => {
  const type = serviceTypes.value.find(t => t.codeIndex === typeCode)
  return type ? type.indexName : '未知类型'
}

const getServiceDetail = async () => {
  try {
    loading.value = true
    const serviceId = route.params.id
    const response = await chongwujiyangApi.getDetail(serviceId)
    if (response.code === 0) {
      service.value = response.data
      form.value.chongwujiyangId = service.value.id
      calculateTotalPrice()
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

const calculateTotalPrice = () => {
  if (service.value && form.value.chongwujiyangYuyueNum > 0) {
    form.value.chongwujiyangYuyuePrice = (service.value.chongwujiyangNewMoney * form.value.chongwujiyangYuyueNum).toFixed(2)
  }
}

const submitBooking = async () => {
  if (!isFormValid.value) {
    message.error('请填写完整的预约信息')
    return
  }

  try {
    const sessionResponse = await yonghuApi.session()
    if (sessionResponse.code === 0 && sessionResponse.data) {
      form.value.yonghuId = sessionResponse.data.id || sessionResponse.data.yonghuId
      
      // 转换日期格式：ISO 8601 -> yyyy-MM-dd HH:mm:ss
      const formatDate = (dateString) => {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
      };

      const bookingData = {
        chongwujiyangId: Number(form.value.chongwujiyangId),
        yonghuId: Number(form.value.yonghuId),
        chongwujiyangYuyueName: form.value.chongwujiyangYuyueName,
        chongwuTypes: Number(form.value.chongwuTypes),
        chongwuZhongliang: Number(form.value.chongwuZhongliang),
        chongwujiyangYuyueTime: formatDate(form.value.chongwujiyangYuyueTime),
        chongwujiyangYuyueNum: Number(form.value.chongwujiyangYuyueNum),
        shifouTypes: Number(form.value.shifouTypes),
        chongwujiyangYuyuePrice: Number(form.value.chongwujiyangYuyuePrice)
      }

      const response = await chongwujiyangYuyueApi.add(bookingData)
      if (response.code === 0) {
        message.success('预约成功')
        router.push('/user/pet-orders')
      } else {
        message.error(response.msg || '预约失败')
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

const backToDetail = () => {
  router.push(`/chongwujiyang/detail/${route.params.id}`)
}

onMounted(() => {
  loadDictionaryData()
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
.service-option-section h3 {
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

.date-item.full-width {
  grid-column: span 2;
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

.total-price {
  color: var(--danger);
  font-size: var(--fs-lg);
  font-weight: 600;
}

.pet-info-section,
.service-option-section {
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
  
  .date-item.full-width {
    grid-column: span 1;
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
