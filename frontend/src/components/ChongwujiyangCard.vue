<template>
  <div class="chongwujiyang-card">
    <router-link :to="`/chongwujiyang/detail/${service.id}`" class="card-link">
      <div class="card-image">
        <img :src="`http://localhost:8080/wangshangchongwudian/` + service.chongwujiyangPhoto" :alt="service.chongwujiyangName">
      </div>
      <div class="card-content">
        <h3 class="card-title">{{ service.chongwujiyangName }}</h3>
        <p class="card-type">{{ getServiceTypeName(service.chongwujiyangTypes) }}</p>
        <p class="card-desc">{{ service.chongwujiyangContent }}</p>
        <div class="card-footer">
          <span class="card-price">¥{{ service.chongwujiyangNewMoney }}/天</span>
          <span class="card-click">热度：{{ service.chongwujiyangClicknum }}</span>
        </div>
      </div>
    </router-link>
    <div class="card-actions">
      <button class="btn btn-primary" @click="bookService(service.id)">立即预约</button>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const props = defineProps({
  service: {
    type: Object,
    required: true
  },
  serviceTypes: {
    type: Array,
    default: () => []
  }
})

const router = useRouter()

// 获取服务类型名称
const getServiceTypeName = (typeCode) => {
  const type = props.serviceTypes.find(t => t.codeIndex === typeCode)
  return type ? type.indexName : '未知类型'
}

// 预约服务
const bookService = (id) => {
  router.push(`/chongwujiyang/booking/${id}`)
}
</script>

<style scoped>
.chongwujiyang-card {
  background: var(--card);
  border-radius: var(--radius-base);
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.chongwujiyang-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.card-link {
  display: block;
  text-decoration: none;
  color: inherit;
  flex: 1;
}

.card-image {
  position: relative;
  padding-top: 100%; /* 1:1 比例 */
  overflow: hidden;
}

.card-image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.chongwujiyang-card:hover .card-image img {
  transform: scale(1.05);
}

.card-content {
  padding: var(--spacing-base);
  flex: 1;
  display: flex;
  flex-direction: column;
}

.card-title {
  font-size: var(--fs-base);
  font-weight: 500;
  color: var(--text-1);
  margin-bottom: var(--spacing-xs);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-type {
  font-size: var(--fs-xs);
  color: var(--primary);
  margin-bottom: var(--spacing-xs);
  font-weight: 500;
}

.card-desc {
  font-size: var(--fs-xs);
  color: var(--text-3);
  margin-bottom: var(--spacing-base);
  flex: 1;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: var(--spacing-sm);
}

.card-price {
  color: var(--danger);
  font-size: var(--fs-lg);
  font-weight: 600;
}

.card-click {
  color: var(--text-3);
  font-size: var(--fs-xs);
}

.card-actions {
  padding: var(--spacing-sm) var(--spacing-base);
  border-top: 1px solid var(--border);
  background: white;
}

.card-actions .btn {
  width: 100%;
  font-size: var(--fs-sm);
  padding: var(--spacing-xs) var(--spacing-base);
}

@media (max-width: 768px) {
  .card-content {
    padding: var(--spacing-sm);
  }
  
  .card-actions {
    padding: var(--spacing-xs) var(--spacing-sm);
  }
  
  .card-actions .btn {
    font-size: var(--fs-xs);
    padding: var(--spacing-xs);
  }
}
</style>