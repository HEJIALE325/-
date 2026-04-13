import { createRouter, createWebHistory } from 'vue-router'
import { usersApi } from '../utils/api'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // 前台用户路由
    {
      path: '/',
      name: 'user-home',
      component: () => import('../views/user/HomeView.vue')
    },
    {
      path: '/products',
      name: 'user-products',
      component: () => import('../views/user/ProductsView.vue')
    },
    {
      path: '/product/:id',
      name: 'user-product-detail',
      component: () => import('../views/user/ProductDetailView.vue')
    },
    {
      path: '/cart',
      name: 'user-cart',
      component: () => import('../views/user/CartView.vue')
    },
    {
      path: '/order-confirm',
      name: 'user-order-confirm',
      component: () => import('../views/user/OrderConfirmView.vue')
    },
    {
      path: '/user/login',
      name: 'user-login',
      component: () => import('../views/user/LoginView.vue')
    },
    {
      path: '/user/register',
      name: 'user-register',
      component: () => import('../views/user/RegisterView.vue')
    },
    {
      path: '/user/profile',
      name: 'user-profile',
      component: () => import('../views/user/ProfileView.vue')
    },
    {
      path: '/user/orders',
      name: 'user-orders',
      component: () => import('../views/user/OrdersView.vue')
    },
    {
      path: '/user/favorites',
      name: 'user-favorites',
      component: () => import('../views/user/FavoritesView.vue')
    },
    {
      path: '/user/address',
      name: 'user-address',
      component: () => import('../views/user/AddressView.vue')
    },
    {
      path: '/user/comments',
      name: 'user-comments',
      component: () => import('../views/user/CommentsView.vue')
    },
    {
      path: '/user/pet-orders',
      name: 'user-pet-orders',
      component: () => import('../views/user/PetOrdersView.vue')
    },
    // 宠物购买相关路由
    {
      path: '/pets',
      name: 'user-pets',
      component: () => import('../views/user/PetsView.vue')
    },
    {
      path: '/pet/:id',
      name: 'user-pet-detail',
      component: () => import('../views/user/PetDetailView.vue')
    },
    {
      path: '/user/pet-purchase-orders',
      name: 'user-pet-purchase-orders',
      component: () => import('../views/user/PetPurchaseOrdersView.vue')
    },
    {
      path: '/news',
      name: 'user-news',
      component: () => import('../views/user/NewsView.vue')
    },
    {
      path: '/news/detail/:id',
      name: 'user-news-detail',
      component: () => import('../views/user/NewsDetailView.vue')
    },
    // 论坛相关路由
    {
      path: '/forum',
      name: 'user-forum',
      component: () => import('../views/user/ForumView.vue')
    },
    {
      path: '/forum/detail/:id',
      name: 'user-forum-detail',
      component: () => import('../views/user/ForumDetailView.vue')
    },
    // 宠物寄养相关路由
    {
      path: '/chongwujiyang',
      name: 'user-chongwujiyang',
      component: () => import('../views/user/ChongwujiyangListView.vue')
    },
    {
      path: '/chongwujiyang/detail/:id',
      name: 'user-chongwujiyang-detail',
      component: () => import('../views/user/ChongwujiyangDetailView.vue')
    },
    {
      path: '/chongwujiyang/booking/:id',
      name: 'user-chongwujiyang-booking',
      component: () => import('../views/user/ChongwujiyangBookingView.vue')
    },
    // 后台管理路由 - 登录页面（独立路由）
    {
      path: '/admin/login',
      name: 'admin-login',
      component: () => import('../views/admin/LoginView.vue')
    },
    // 后台管理路由 - 带布局的嵌套路由
    {
      path: '/admin',
      name: 'admin-layout',
      component: () => import('../components/admin/AdminLayout.vue'),
      children: [
        {
          path: '',
          name: 'admin-home',
          component: () => import('../views/admin/HomeView.vue')
        },
        {
          path: 'users',
          name: 'admin-users',
          component: () => import('../views/admin/UsersView.vue')
        },
        {
          path: 'products',
          name: 'admin-products',
          component: () => import('../views/admin/ProductsView.vue')
        },
        {
          path: 'profile',
          name: 'admin-profile',
          component: () => import('../views/admin/ProfileView.vue')
        },
        {
          path: 'admin-users',
          name: 'admin-admin-users',
          component: () => import('../views/admin/AdminUsersView.vue')
        },
        {
          path: 'chongwujiyang',
          name: 'admin-chongwujiyang',
          component: () => import('../views/admin/ChongwujiyangView.vue')
        },
        {
          path: 'chongwujiyang-yuyue',
          name: 'admin-chongwujiyang-yuyue',
          component: () => import('../views/admin/ChongwujiyangYuyueView.vue')
        },
        {
          path: 'chongwurizhi',
          name: 'admin-chongwurizhi',
          component: () => import('../views/admin/ChongwurizhiView.vue')
        },
        // 客服聊天管理
        {
          path: 'customer-service',
          name: 'admin-customer-service',
          component: () => import('../views/admin/CustomerServiceView.vue')
        },
        // 基础数据管理
        {
          path: 'jichushuju',
          name: 'admin-jichushuju',
          component: () => import('../views/admin/JichushujuView.vue')
        },
        // 论坛管理
        {
          path: 'luntan',
          name: 'admin-luntan',
          component: () => import('../views/admin/LuntanView.vue')
        },
        // 公告信息管理
        {
          path: 'gonggao',
          name: 'admin-gonggao',
          component: () => import('../views/admin/GonggaoView.vue')
        },
        // 轮播图信息
        {
          path: 'lunbotu',
          name: 'admin-lunbotu',
          component: () => import('../views/admin/LunbotuView.vue')
        },
        // 商品评价管理
        {
          path: 'chongwuyongpin-commentback',
          name: 'admin-chongwuyongpin-commentback',
          component: () => import('../views/admin/ChongwuyongpinCommentbackView.vue')
        },
        // 商品订单管理
        {
          path: 'chongwuyongpin-order',
          name: 'admin-chongwuyongpin-order',
          component: () => import('../views/admin/ChongwuyongpinOrderView.vue')
        }
      ]
    }
  ]
})

// 全局前置守卫，检查用户登录状态
router.beforeEach(async (to, from, next) => {
  // 判断是否是后台路由（/admin/*）
  const isAdminRoute = to.path.startsWith('/admin')
  // 后台登录页面不需要检查
  const isAdminLogin = to.path === '/admin/login'
  
  // 如果是后台路由且不是登录页面，需要检查登录状态
  if (isAdminRoute && !isAdminLogin) {
    try {
      // 调用session接口检查用户是否登录
      await usersApi.session()
      // 登录成功，继续访问
      next()
    } catch (error) {
      // 登录失败，跳转到登录页面
      next('/admin/login')
    }
  } else {
    // 非后台路由或登录页面，直接访问
    next()
  }
})

export default router