<template>
  <footer class="footer">
    <div class="container">
      <!-- 服务承诺 -->
      <div class="footer-services">
        <div class="service-item">
          <span class="service-icon">🛡️</span>
          <span class="service-text">正品保证</span>
        </div>
        <div class="service-item">
          <span class="service-icon">🚚</span>
          <span class="service-text">快速配送</span>
        </div>
        <div class="service-item">
          <span class="service-icon">💳</span>
          <span class="service-text">7天无理由退换</span>
        </div>
        <div class="service-item">
          <span class="service-icon">👨‍⚕️</span>
          <span class="service-text">专业咨询</span>
        </div>
      </div>
      
      <!-- 底部内容 -->
      <div class="footer-content">
        <div class="footer-section">
          <h3>宠物电商平台</h3>
          <p class="footer-desc">
            用心呵护每一位毛孩子，为宠物提供优质的商品和服务
          </p>
          <div class="footer-social">
            <a href="#" class="social-icon">🐾</a>
            <a href="#" class="social-icon">📱</a>
            <a href="#" class="social-icon">💬</a>
          </div>
        </div>
        
        <div class="footer-section">
          <h3>快速链接</h3>
          <ul>
            <li><router-link to="/">首页</router-link></li>
            <li><router-link to="/products">商品分类</router-link></li>
            <li><router-link to="/chongwujiyang">宠物寄养</router-link></li>
            <li><router-link to="/forum">宠物论坛</router-link></li>
            <li><router-link to="/news">宠物资讯</router-link></li>
            <li><router-link to="/about">关于我们</router-link></li>
          </ul>
        </div>
        
        <div class="footer-section">
          <h3>宠物专区</h3>
          <ul>
            <li><router-link to="/dog" class="category-link dog">🐶 狗狗专区</router-link></li>
            <li><router-link to="/cat" class="category-link cat">🐱 猫咪专区</router-link></li>
            <li><router-link to="/small-pet" class="category-link small-pet">🐹 小宠专区</router-link></li>
            <li><router-link to="/fish" class="category-link fish">🐠 水族专区</router-link></li>
          </ul>
        </div>
        
        <div class="footer-section">
          <h3>联系方式</h3>
          <div class="contact-item">
            <span class="contact-icon">📞</span>
            <span>400-123-4567</span>
          </div>
          <div class="contact-item">
            <span class="contact-icon">📧</span>
            <span>service@petshop.com</span>
          </div>
          <div class="contact-item">
            <span class="contact-icon">📍</span>
            <span>北京市朝阳区宠物大厦</span>
          </div>
        </div>
      </div>
      
      <!-- 版权信息 -->
      <div class="footer-bottom">
        <p>&copy; 2026 宠物电商平台. 用心呵护每一位毛孩子 ❤️</p>
      </div>
    </div>
  </footer>
</template>

<script setup>
// 公共底部组件
</script>

<style scoped>
.footer {
  background: var(--card);
  color: var(--text-1);
  padding: var(--spacing-2xl) 0 var(--spacing-base);
  margin-top: var(--spacing-2xl);
  border-top: 1px solid var(--border);
}

/* 服务承诺 */
.footer-services {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--spacing-xl);
  margin-bottom: var(--spacing-2xl);
  padding-bottom: var(--spacing-xl);
  border-bottom: 1px solid var(--border);
  text-align: center;
}

.service-item {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-sm);
  font-weight: 500;
  color: var(--text-1);
}

.service-icon {
  font-size: 24px;
}

/* 底部内容 */
.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: var(--spacing-2xl);
  margin-bottom: var(--spacing-2xl);
}

.footer-section h3 {
  color: var(--text-1);
  margin-bottom: var(--spacing-base);
  font-size: var(--fs-lg);
  font-weight: 600;
}

.footer-desc {
  color: var(--text-2);
  margin-bottom: var(--spacing-lg);
  line-height: 1.6;
}

/* 社交图标 */
.footer-social {
  display: flex;
  gap: var(--spacing-base);
}

.social-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  background: var(--bg);
  border-radius: var(--radius-full);
  color: var(--primary);
  text-decoration: none;
  font-size: 18px;
  transition: all 0.3s ease;
}

.social-icon:hover {
  background: var(--primary);
  color: white;
  transform: translateY(-2px);
}

/* 列表样式 */
.footer-section ul {
  list-style: none;
  padding: 0;
}

.footer-section ul li {
  margin-bottom: var(--spacing-sm);
}

.footer-section ul li a {
  color: var(--text-2);
  text-decoration: none;
  transition: color 0.3s ease;
  font-size: var(--fs-sm);
  display: inline-block;
}

.footer-section ul li a:hover {
  color: var(--primary);
  transform: translateX(4px);
}

/* 分类链接样式 */
.category-link {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
}

.category-link.dog {
  color: var(--dog);
}

.category-link.cat {
  color: var(--cat);
}

.category-link.small-pet {
  color: var(--small-pet);
}

.category-link.fish {
  color: var(--fish);
}

/* 联系方式 */
.contact-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  margin-bottom: var(--spacing-sm);
  color: var(--text-2);
  font-size: var(--fs-sm);
}

.contact-icon {
  color: var(--primary);
  font-size: 16px;
}

/* 版权信息 */
.footer-bottom {
  text-align: center;
  padding-top: var(--spacing-xl);
  border-top: 1px solid var(--border);
  color: var(--text-3);
  font-size: var(--fs-xs);
}

/* 移动端适配 */
@media (max-width: 768px) {
  .footer-services {
    grid-template-columns: repeat(2, 1fr);
    gap: var(--spacing-base);
  }
  
  .footer-content {
    grid-template-columns: 1fr;
    gap: var(--spacing-xl);
  }
}
</style>