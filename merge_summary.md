此次合并主要添加了在线客服聊天功能，包括前端聊天组件和后台管理界面的完善，同时将模拟数据替换为实际API调用。
| 文件 | 变更 |
|------|---------|
| frontend/src/App.vue | - 引入并添加了ChatComponent组件到应用根组件中 |
| frontend/src/components/ChatComponent.vue | - 新增在线客服聊天组件，支持消息发送、接收、状态管理和未读消息提醒<br>- 实现了聊天窗口的显示/隐藏功能和自动轮询新消息机制<br>- 包含完整的样式设计，支持响应式布局和消息状态显示 |
| frontend/src/views/admin/CustomerServiceView.vue | - 从使用模拟数据改为调用实际的chatApi API获取客服聊天列表<br>- 字段名称变更：content→chatIssue, status→zhuangtaiTypes, createTime→issueTime, response→chatReply, updateTime→replyTime<br>- 状态值从字符串改为数字（0:待处理, 1:处理中, 2:已完成）<br>- 移除了用户名显示，优化了时间显示逻辑<br>- 实现了完整的客服咨询处理流程，包括查看详情和回复功能 |