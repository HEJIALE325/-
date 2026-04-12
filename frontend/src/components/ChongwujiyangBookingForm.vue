<template>
  <div class="chongwujiyang-booking-form">
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
      <button class="btn btn-primary" @click="$emit('submit')" :disabled="!isFormValid">提交预约</button>
      <button class="btn btn-secondary" @click="$emit('cancel')">取消</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'

const props = defineProps({
  service: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['submit', 'cancel'])

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

// 计算寄养天数和费用
const calculateDays = () => {
  if (form.value.startDate && form.value.endDate) {
    const start = new Date(form.value.startDate)
    const end = new Date(form.value.endDate)
    const diffTime = Math.abs(end - start)
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1 // 包含开始和结束日期
    
    if (diffDays > 0) {
      form.value.days = diffDays
      if (props.service) {
        form.value.totalPrice = (props.service.chongwujiyangNewMoney * diffDays).toFixed(2)
      }
    } else {
      form.value.days = 0
      form.value.totalPrice = 0
    }
  }
}

// 重置表单
const resetForm = () => {
  form.value = {
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
  }
}

// 暴露方法给父组件
defineExpose({
  form,
  isFormValid,
  resetForm
})
</script>

<style scoped>
.chongwujiyang-booking-form {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xl);
}

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
  .date-picker {
    grid-template-columns: 1fr;
  }
  
  .submit-section {
    flex-direction: column;
  }
  
  .submit-section .btn {
    max-width: 100%;
  }
}
</style>