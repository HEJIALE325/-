此次合并主要实现了宠物销售管理系统的完整功能，包括前端展示、后端API和管理后台。新增了宠物分类、宠物详情、宠物购买订单等核心模块，完善了用户购买宠物的全流程。
| 文件 | 变更 |
|------|---------|
| frontend/src/components/Header.vue | - 在导航栏新增"宠物"链接<br>- 在用户菜单新增"宠物购买订单"菜单项 |
| frontend/src/router/index.js | - 新增宠物相关路由：/pets（宠物列表）、/pet/:id（宠物详情）、/user/pet-purchase-orders（宠物购买订单） |
| frontend/src/utils/api.js | - 新增宠物分类API服务，支持分类管理和查询<br>- 新增宠物API服务，支持宠物列表、详情、搜索等操作<br>- 新增宠物订单API服务，支持订单创建、状态更新等功能 |
| frontend/src/views/user/PetDetailView.vue | - 新增宠物详情页面，展示宠物详细信息和购买功能 |
| frontend/src/views/user/PetPurchaseOrdersView.vue | - 新增宠物购买订单页面，管理用户的宠物订单 |
| frontend/src/views/user/PetsView.vue | - 新增宠物列表页面，展示可购买的宠物 |
| src/main/java/com/controller/PetCategoryController.java | - 新增宠物分类控制器，提供分类管理和查询API |
| src/main/java/com/controller/PetController.java | - 新增宠物控制器，提供宠物管理、查询、上架/下架等API |
| src/main/java/com/controller/PetOrderController.java | - 新增宠物订单控制器，提供订单管理、状态更新等API |
| src/main/java/com/dao/PetCategoryDao.java | - 新增宠物分类DAO接口，定义数据访问方法 |
| src/main/java/com/dao/PetDao.java | - 新增宠物DAO接口，定义数据访问方法 |
| src/main/java/com/dao/PetOrderDao.java | - 新增宠物订单DAO接口，定义数据访问方法 |
| src/main/java/com/entity/PetCategoryEntity.java | - 新增宠物分类实体类，定义分类数据结构 |
| src/main/java/com/entity/PetEntity.java | - 新增宠物实体类，定义宠物数据结构 |
| src/main/java/com/entity/PetOrderEntity.java | - 新增宠物订单实体类，定义订单数据结构 |
| src/main/java/com/entity/vo/PetCategoryVO.java | - 新增宠物分类VO类，用于前端展示 |
| src/main/java/com/entity/vo/PetOrderVO.java | - 新增宠物订单VO类，用于前端展示 |
| src/main/java/com/entity/vo/PetVO.java | - 新增宠物VO类，用于前端展示 |
| src/main/java/com/service/PetCategoryService.java | - 新增宠物分类服务接口，定义业务逻辑方法 |
| src/main/java/com/service/PetOrderService.java | - 新增宠物订单服务接口，定义业务逻辑方法 |
| src/main/java/com/service/PetService.java | - 新增宠物服务接口，定义业务逻辑方法 |
| src/main/java/com/service/impl/PetCategoryServiceImpl.java | - 实现宠物分类服务接口，提供具体业务逻辑 |
| src/main/java/com/service/impl/PetOrderServiceImpl.java | - 实现宠物订单服务接口，提供具体业务逻辑 |
| src/main/java/com/service/impl/PetServiceImpl.java | - 实现宠物服务接口，提供具体业务逻辑 |
| src/main/resources/admin/admin/src/views/modules/pet/add-or-update.vue | - 新增管理员后台宠物添加/编辑页面 |
| src/main/resources/admin/admin/src/views/modules/pet/list.vue | - 新增管理员后台宠物列表页面 |
| src/main/resources/admin/admin/src/views/modules/petCategory/add-or-update.vue | - 新增管理员后台宠物分类添加/编辑页面 |
| src/main/resources/admin/admin/src/views/modules/petCategory/list.vue | - 新增管理员后台宠物分类列表页面 |
| src/main/resources/admin/admin/src/views/modules/petOrder/list.vue | - 新增管理员后台宠物订单列表页面 |
| .trae/specs/pet-selling-management/checklist.md | - 新增宠物销售管理功能检查清单 |
| .trae/specs/pet-selling-management/spec.md | - 新增宠物销售管理功能规格说明 |
| .trae/specs/pet-selling-management/tasks.md | - 新增宠物销售管理功能任务列表 |