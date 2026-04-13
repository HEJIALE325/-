# 修复宠物API接口500错误问题计划

## 问题分析
经过分析，发现宠物相关API接口报500错误的主要原因有：
1. 宠物相关的DAO接口定义了自定义查询方法，但缺少对应的XML映射文件
2. 后端控制器和服务层的实现方式与项目中其他模块不一致
3. 项目中其他模块都有对应的XML映射文件，宠物相关模块缺少

## 解决方案
采用两种方案结合：
1. 对于简单的CRUD操作，使用MyBatis Plus的内置方法
2. 对于需要自定义查询的操作，创建对应的XML映射文件

## 需要修改/创建的文件

### 后端文件
1. `/workspace/src/main/java/com/controller/PetCategoryController.java` - 简化控制器实现
2. `/workspace/src/main/java/com/controller/PetController.java` - 简化控制器实现
3. `/workspace/src/main/java/com/controller/PetOrderController.java` - 简化控制器实现
4. `/workspace/src/main/java/com/service/PetCategoryService.java` - 简化服务接口
5. `/workspace/src/main/java/com/service/PetService.java` - 简化服务接口
6. `/workspace/src/main/java/com/service/PetOrderService.java` - 简化服务接口
7. `/workspace/src/main/java/com/service/impl/PetCategoryServiceImpl.java` - 简化服务实现
8. `/workspace/src/main/java/com/service/impl/PetServiceImpl.java` - 简化服务实现
9. `/workspace/src/main/java/com/service/impl/PetOrderServiceImpl.java` - 简化服务实现
10. `/workspace/src/main/java/com/dao/PetCategoryDao.java` - 调整DAO接口
11. `/workspace/src/main/java/com/dao/PetDao.java` - 调整DAO接口
12. `/workspace/src/main/java/com/dao/PetOrderDao.java` - 调整DAO接口

### XML映射文件（需要创建）
13. `/workspace/src/main/resources/mapper/PetCategoryDao.xml` - 宠物分类XML映射
14. `/workspace/src/main/resources/mapper/PetDao.xml` - 宠物XML映射
15. `/workspace/src/main/resources/mapper/PetOrderDao.xml` - 宠物订单XML映射

## 修复步骤
1. 简化后端控制器，使用标准的CRUD方法
2. 简化服务层，使用MyBatis Plus内置方法
3. 创建对应的XML映射文件
4. 测试所有API接口是否正常工作

## 风险处理
- 确保修改后的代码与项目现有结构和风格保持一致
- 逐步修改，每修改一个模块就进行测试
- 保持原有的业务逻辑不变