# 基于SpringBoot和Vue的网上宠物店系统的设计与实现

## 摘要

随着互联网技术的快速发展和人们生活水平的不断提高，宠物行业迎来了蓬勃发展的黄金时期。越来越多的宠物主人希望能够通过互联网平台方便快捷地购买宠物用品、预约宠物服务以及与其他宠物爱好者进行交流。本文设计并实现了一个基于SpringBoot框架和Vue前端技术的网上宠物店系统。该系统采用前后端分离的架构设计，后端使用SpringBoot2.2.2作为核心框架，整合MyBatis-Plus实现数据持久化操作，前端采用Vue3配合Vite构建工具进行开发。系统实现了用户管理、商品浏览、购物车、订单处理、宠物寄养预约、论坛交流、公告资讯以及客服聊天等核心功能模块。系统前台为普通用户提供了便捷的购物和社交平台，后台管理系统则为运营人员提供了高效的数据管理手段。本文详细阐述了系统的需求分析、架构设计、数据库设计以及关键技术的实现方案，并通过功能测试验证了系统的可行性和稳定性。

**关键词**：网上宠物店；SpringBoot；Vue；前后端分离；MyBatis-Plus

---

## 1 引言

### 1.1 研究背景

近年来，随着城市化进程的加快和居民收入水平的提升，宠物已经成为越来越多家庭的重要成员。根据相关统计数据显示，中国宠物市场规模持续扩大，宠物消费需求日益多元化。在这一背景下，传统的宠物店经营模式已经难以满足宠物主人随时随地购买宠物用品、获取宠物护理知识以及与其他宠物爱好者交流的需求。

互联网技术的飞速发展，特别是Web应用技术的成熟，为宠物行业与互联网的深度融合提供了技术可能。网上宠物店系统能够打破时间和空间的限制，让宠物主人可以便捷地浏览商品、了解宠物护理知识、与其他宠物爱好者互动交流。同时，对于宠物店经营者而言，通过线上平台能够拓展销售渠道、提升运营效率、降低经营成本。因此，设计和开发一个功能完善、用户体验良好的网上宠物店系统具有重要的实际应用价值。

### 1.2 研究意义

本系统的设计与实现具有以下几个方面的意义。首先，通过构建网上宠物店系统，可以为宠物主人提供一个便捷、高效的购物平台，使其能够随时随地浏览和购买宠物用品，享受更加优质的购物体验。其次，系统整合了宠物寄养预约、论坛交流等多元化功能，不仅满足了宠物主人的购物需求，还为其提供了获取宠物护理知识、交流养宠经验的社交平台，增强了用户粘性。再次，后台管理系统的设计与实现，为店铺运营人员提供了便捷的商品管理、订单处理、用户管理等工具，提升了店铺的运营效率和管理水平。最后，本系统的设计与实现过程，也为其他类似电商系统的开发提供了技术参考和实践经验。

### 1.3 技术路线

本系统采用当前主流的前后端分离架构进行设计与实现。在技术选型上，后端采用SpringBoot2.2.2作为核心框架，该框架具有简化配置、内嵌服务器、快速开发等优点，能够有效提高开发效率。数据持久化层使用MyBatis-Plus框架，该框架提供了强大的CRUD操作功能和条件构造器，能够显著减少数据库操作代码的编写。前端采用Vue3配合Vite构建工具进行开发，Vue3采用了Proxy作为响应式系统的实现，提供了更好的性能和更简洁的API；Vite则利用浏览器原生ES模块实现按需编译，大大提升了开发时的热更新速度。在数据传输方面，使用Axios进行HTTP请求的封装和处理，提供了统一的请求拦截和响应处理机制。数据库采用MySQL，该数据库具有性能稳定、开源免费、社区活跃等优点，是Web应用开发中常用的关系型数据库。

---

## 2 系统需求分析

### 2.1 系统角色分析

通过对网上宠物店业务的深入调研和分析，本系统涉及的主要角色包括普通用户和管理员两类。

普通用户是系统的主要服务对象，包括宠物主人、宠物爱好者等。该角色需要的功能包括：用户注册和登录、个人信息管理、商品浏览和搜索、购物车管理、商品订单处理、商品评价、宠物寄养预约、论坛发帖和回帖、公告资讯浏览、客服咨询等。

管理员是系统的后台管理人员，负责系统的日常运营和维护。该角色需要的功能包括：用户管理、商品管理、订单管理、宠物寄养订单管理、论坛帖子管理、公告资讯管理、客服聊天管理、轮播图管理、系统基础数据管理等。

### 2.2 功能需求分析

基于对系统角色的分析，本系统的功能需求可以从用户端功能和管理端功能两个维度进行阐述。

用户端功能主要包括以下几个模块：用户模块提供用户注册、登录、修改密码、个人信息修改等功能；商品模块实现商品的分类浏览、热销推荐、商品搜索、商品详情查看、商品收藏等功能；购物车模块支持购物车商品的添加、删除、数量修改、选中结算等功能；订单模块实现订单确认、订单提交、订单支付、订单查看、订单评价、订单退款等功能；宠物寄养模块提供寄养服务浏览、寄养预约、预约记录查看、寄存日志查看等功能；论坛模块实现论坛帖子浏览、帖子发布、帖子回复、我的帖子等功能；公告模块提供系统公告列表查看、公告详情查看等功能；客服模块支持用户与客服人员进行在线聊天沟通。

管理端功能主要包括：用户管理实现用户列表查看、用户信息编辑、用户禁用启用等功能；商品管理实现商品列表管理、商品新增、商品编辑、商品上下架、商品删除等功能；订单管理实现订单列表查看、订单状态管理、发货处理、评价管理等功能；宠物寄养订单管理实现预约订单查看、预约审核、审核结果填写等功能；论坛管理实现帖子列表查看、帖子删除、敏感词过滤等功能；公告管理实现公告发布、公告编辑、公告删除等功能；客服管理实现聊天记录查看、客服分配等功能；轮播图管理实现首页轮播图的设置和管理；基础数据管理实现字典数据的维护，包括商品类型、订单状态等枚举值的配置。

### 2.3 非功能需求分析

除了功能需求外，本系统还需要满足以下非功能需求。在性能方面，系统需要能够支持多用户同时访问，页面响应时间控制在合理范围内，保证用户操作的流畅性。在安全性方面，系统需要实现用户密码的加密存储，防止SQL注入和XSS攻击，对敏感操作进行权限验证。在可用性方面，系统需要保证7×24小时的稳定运行，具有良好的容错能力。在可维护性方面，系统代码需要结构清晰、注释完善，便于后续的维护和升级。在可扩展性方面，系统架构需要具备良好的扩展能力，便于后续功能的添加和修改。

---

## 3 系统设计

### 3.1 系统架构设计

本系统采用前后端分离的架构设计，整体架构分为表现层、业务逻辑层、数据访问层三个层次。前端负责页面展示和用户交互，后端负责业务逻辑处理和数据管理，前后端通过HTTP协议进行数据交互。这种架构模式使得前后端可以独立开发和部署，提高了开发效率和系统的可维护性。

后端采用SpringBoot框架作为核心框架，整合MyBatis-Plus实现数据持久化操作。SpringBoot遵循约定大于配置的原则，提供了自动配置功能，大大简化了项目的搭建和配置过程。MyBatis-Plus在MyBatis的基础上进行了功能增强，提供了通用的Mapper接口和条件构造器，能够快速实现CRUD操作，减少了大量的样板代码。

前端采用Vue3作为核心框架，配合Vite构建工具进行开发。Vue3采用了Composition API的组织方式，使得代码逻辑更加清晰，逻辑复用更加方便。Vite利用浏览器原生ES模块实现开发时的按需编译，相比Webpack具有更快的热更新速度。路由管理采用Vue Router，数据状态管理采用Pinia，HTTP请求使用Axios封装。

系统的整体架构如图1所示。

```
┌─────────────────────────────────────────────────────────────┐
│                        用户端浏览器                          │
├─────────────────────────────────────────────────────────────┤
│                     Vue3 + Vite前端应用                      │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐       │
│  │ 页面组件 │  │ 路由管理 │  │ 状态管理 │  │ API封装 │       │
│  └─────────┘  └─────────┘  └─────────┘  └─────────┘       │
├─────────────────────────────────────────────────────────────┤
│                      HTTP / RESTful API                      │
├─────────────────────────────────────────────────────────────┤
│                   SpringBoot 后端服务                         │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐       │
│  │Controller│  │ Service │  │  Mapper │  │  工具类  │       │
│  └─────────┘  └─────────┘  └─────────┘  └─────────┘       │
├─────────────────────────────────────────────────────────────┤
│                    MyBatis-Plus + MySQL                      │
└─────────────────────────────────────────────────────────────┘
```

**图1 系统整体架构图**

### 3.2 功能模块设计

根据需求分析的结果，本系统的功能模块设计如图2所示。

```
┌─────────────────────────────────────────────────────────────────┐
│                        网上宠物店系统                             │
├─────────────────────────────┬───────────────────────────────────┤
│        用户端功能            │           管理端功能               │
├─────────────────────────────┼───────────────────────────────────┤
│  ┌─────────┐  ┌─────────┐  │  ┌─────────┐  ┌─────────┐       │
│  │用户模块 │  │商品模块 │  │  │用户管理 │  │商品管理 │       │
│  └─────────┘  └─────────┘  │  └─────────┘  └─────────┘       │
│  ┌─────────┐  ┌─────────┐  │  ┌─────────┐  ┌─────────┐       │
│  │购物车模块│  │订单模块 │  │  │订单管理 │  │寄养订单 │       │
│  └─────────┘  └─────────┘  │  └─────────┘  └─────────┘       │
│  ┌─────────┐  ┌─────────┐  │  ┌─────────┐  ┌─────────┐       │
│  │寄养模块 │  │论坛模块 │  │  │论坛管理 │  │公告管理 │       │
│  └─────────┘  └─────────┘  │  └─────────┘  └─────────┘       │
│  ┌─────────┐  ┌─────────┐  │  ┌─────────┐  ┌─────────┐       │
│  │公告模块 │  │客服模块 │  │  │客服管理 │  │轮播图管理│       │
│  └─────────┘  └─────────┘  │  └─────────┘  └─────────┘       │
└─────────────────────────────┴───────────────────────────────────┘
```

**图2 系统功能模块图**

用户端各个模块的功能说明如下。用户模块负责处理用户的注册、登录、个人信息维护等操作，是用户使用系统的基础。商品模块提供商品的分类浏览、搜索查询、详情展示、收藏等功能，帮助用户快速找到心仪的商品。购物车模块实现购物车的增删改查操作，支持用户对已选商品进行数量调整和结算处理。订单模块处理订单的全生命周期，包括订单确认、提交、支付、查看、评价等环节。宠物寄养模块提供寄养服务的浏览和预约功能，用户可以查看各类寄养服务并提交预约申请。论坛模块为用户提供交流分享的平台，用户可以发布帖子、回复他人帖子、浏览热门话题等。公告模块发布系统的重要通知和宠物相关资讯，让用户及时了解平台动态。客服模块提供用户与客服人员的在线沟通渠道，解答用户疑问和处理用户问题。

管理端各个模块的功能说明如下。用户管理模块实现对系统用户的查询、编辑、禁用启用等操作。商品管理模块实现商品的增删改查、商品上下架、库存管理等功能。订单管理模块实现订单的查询、状态变更、发货处理等操作。宠物寄养订单管理模块实现寄养预约订单的审核、拒绝、处理等功能。论坛管理模块实现对论坛帖子的审核、删除、敏感词过滤等操作。公告管理模块实现公告资讯的发布、编辑、删除等操作。客服管理模块实现客服聊天记录查看、问题跟踪等功能。轮播图管理模块实现首页轮播图的配置和管理。

### 3.3 数据库设计

本系统使用MySQL作为数据库管理系统，根据功能需求分析，设计了以下主要数据表。

**（1）用户表（yonghu）**

用户表用于存储系统普通用户的基本信息，主要字段包括：用户ID、账户名、密码、用户姓名、联系方式、身份证号、用户头像、性别、电子邮箱、余额、创建时间等。该表的设计考虑了用户信息的完整性和安全性，密码字段采用明文存储（实际生产环境中应加密存储），余额字段用于支持平台的虚拟货币支付功能。

**（2）管理员表（users）**

管理员表用于存储系统管理员的信息，主要字段包括：管理员ID、用户名、密码、角色、添加时间等。该表结构相对简单，主要用于后台管理员的身份验证和权限控制。

**（3）商品表（chongwuyongpin）**

商品表用于存储宠物用品商品的信息，主要字段包括：商品ID、商品名称、商品编号、商品照片、商品类型、商品库存、商品原价、商品现价、商品热度、商品介绍、上下架状态、逻辑删除标记、录入时间、创建时间等。商品类型字段通过字典表进行关联，支持动态配置。

**（4）购物车表（cart）**

购物车表用于存储用户的购物车商品信息，主要字段包括：购物车ID、所属用户ID、商品ID、购买数量、添加时间、更新时间、创建时间等。该表记录了用户添加到购物车的商品及其数量信息。

**（5）商品订单表（chongwuyongpin_order）**

商品订单表用于存储商品订单的信息，主要字段包括：订单ID、订单号、收货地址ID、商品ID、用户ID、购买数量、实付价格、订单类型、支付类型、订单创建时间、创建时间等。订单类型包括已支付、已退款、已发货、已收货、已评价等状态。

**（6）宠物寄养表（chongwujiyang）**

宠物寄养表用于存储宠物寄养服务的信息，主要字段包括：寄养服务ID、寄养服务名称、寄养服务封面、寄养服务类型、价格每天、寄养服务热度、寄养服务介绍、逻辑删除标记、录入时间、创建时间等。

**（7）宠物寄养预约表（chongwujiyang_yuyue）**

宠物寄养预约表用于存储用户的寄养预约信息，主要字段包括：预约ID、宠物寄养ID、用户ID、宠物名称、宠物类型、宠物重量、寄养开始时间、寄养天数、是否接送、总价格、审核状态、审核结果、添加时间、创建时间等。

**（8）寄存日志表（chongwurizhi）**

寄存日志表用于存储宠物寄存过程的日志信息，主要字段包括：日志ID、宠物寄养订单ID、标题、日志内容、录入时间、创建时间等。

**（9）论坛表（forum）**

论坛表用于存储论坛帖子的信息，主要字段包括：帖子ID、帖子标题、用户ID、管理员ID、发布内容、父ID、帖子状态、发帖时间、修改时间、创建时间等。父ID字段用于支持帖子的回复功能，实现帖子的树形结构。

**（10）公告表（news）**

公告表用于存储系统公告资讯的信息，主要字段包括：公告ID、公告标题、公告类型、公告图片、添加时间、公告详情、创建时间等。

**（11）客服聊天表（chat）**

客服聊天表用于存储用户与客服的聊天记录，主要字段包括：聊天ID、提问用户ID、问题内容、问题时间、回复内容、回复时间、状态、数据类型、创建时间等。

**（12）商品收藏表（chongwuyongpin_collection）**

商品收藏表用于存储用户收藏的商品信息，主要字段包括：收藏ID、商品ID、用户ID、收藏类型、收藏时间、创建时间等。

**（13）商品评价表（chongwuyongpin_commentback）**

商品评价表用于存储用户对商品的评价信息，主要字段包括：评价ID、商品ID、用户ID、评价内容、评价时间、回复内容、回复时间、创建时间等。

**（14）收货地址表（address）**

收货地址表用于存储用户的收货地址信息，主要字段包括：地址ID、创建用户、收货人、电话、地址、是否默认地址、添加时间、修改时间、创建时间等。

**（15）字典表（dictionary）**

字典表用于存储系统的字典数据，主要字段包括：字典ID、字段编码、字段名、编码、编码名字、父字段ID、备注、创建时间等。该表采用键值对的形式存储系统中的枚举类型数据，便于统一管理和动态配置。

**（16）轮播图表（config）**

轮播图表用于存储系统的轮播图配置，主要字段包括：配置ID、配置参数名称、配置参数值等。

**（17）token表（token）**

token表用于存储用户的登录令牌信息，主要字段包括：令牌ID、用户ID、用户名、表名、角色、令牌、过期时间、新增时间等。该表用于实现用户的会话管理和token验证功能。

各数据表之间的关系如图3所示。

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│    yonghu    │────▶│     cart     │◀────│chongwuyongpin │
│     用户      │     │    购物车     │     │     商品      │
└──────────────┘     └──────────────┘     └──────────────┘
       │                    │                    │
       │                    ▼                    ▼
       │              ┌──────────────┐     ┌──────────────┐
       │              │chongwuyongpin│────▶│chongwuyongpin │
       │              │   _order    │     │_commentback   │
       │              │   商品订单   │     │   商品评价   │
       │              └──────────────┘     └──────────────┘
       │                    │
       │                    ▼
       │              ┌──────────────┐
       └─────────────▶│   address    │
                      │   收货地址   │
                      └──────────────┘

┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│chongwujiyang │────▶│chongwujiyang │
│    宠物寄养   │     │   _yuyue     │
└──────────────┘     │   寄养预约   │
                      └──────────────┘
                            │
                            ▼
                      ┌──────────────┐
                      │ chongwurizhi │
                      │   寄存日志   │
                      └──────────────┘

┌──────────────┐     ┌──────────────┐
│    forum     │     │     news      │
│    论坛      │     │    公告      │
└──────────────┘     └──────────────┘
       │
       ▼
┌──────────────┐
│     chat     │
│    客服聊天   │
└──────────────┘
```

**图3 数据库ER图**

---

## 4 系统实现

### 4.1 开发环境

本系统的开发环境配置如下。后端开发使用IntelliJ IDEA作为集成开发环境，JDK版本为1.8，Apache Maven作为项目构建工具，Apache Tomcat作为应用服务器。前端开发使用Visual Studio Code作为代码编辑器，Node.js版本为16及以上，npm作为包管理工具。数据库使用MySQL5.7版本，数据库管理工具使用Navicat。版本控制使用Git，系统代码托管于本地仓库。

### 4.2 后端实现

#### 4.2.1 项目创建与配置

后端项目基于SpringBoot2.2.2版本创建，项目名称为wangshangchongwudian，包名为com.jlwl。在pom.xml文件中配置了项目所需的各项依赖，包括spring-boot-starter-web（Web开发核心包）、mybatis-spring-boot-starter（MyBatis集成包）、mybatis-plus（MyBatis-Plus增强包）、mysql-connector-java（MySQL驱动包）、shiro-spring（Shiro安全框架包）、fastjson（JSON处理包）、hutool-all（工具类集合包）、poi-ooxml（Excel处理包）等。

在application.yml配置文件中，配置了服务器端口为8080，上下文路径为/wangshangchongwudian。数据库连接配置了MySQL的连接地址、端口号、数据库名称、用户名和密码。MyBatis-Plus配置指定了Mapper映射文件的路径、实体类的包路径、主键生成策略、字段填充策略等参数。

#### 4.2.2 实体类与Mapper层

本系统使用MyBatis-Plus的代码生成器功能，根据数据库表结构自动生成了实体类、Mapper接口、Service接口和Controller控制器。实体类继承BaseEntity基类，通过@TableName注解指定对应的数据库表名，通过@TableField注解配置字段映射关系。

以用户实体类YonghuEntity为例，核心代码结构如下：

```java
@TableName("yonghu")
public class YonghuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    @TableField("yonghu_name")
    private String yonghuName;

    @TableField("yonghu_phone")
    private String yonghuPhone;

    @TableField("yonghu_id_number")
    private String yonghuIdNumber;

    @TableField("yonghu_photo")
    private String yonghuPhoto;

    @TableField("sex_types")
    private Integer sexTypes;

    @TableField("yonghu_email")
    private String yonghuEmail;

    @TableField("new_money")
    private BigDecimal newMoney;

    @TableField("create_time")
    private Date createTime;
}
```

Mapper层继承BaseMapper<T>接口，MyBatis-Plus会自动提供基本的CRUD方法，无需编写SQL语句即可实现数据访问操作。对于复杂的查询需求，可以在Mapper.xml文件中编写自定义的SQL语句。

#### 4.2.3 Service层实现

Service层负责处理业务逻辑，在本系统中，Service接口继承IService<T>接口，Service实现类继承ServiceImpl<Mapper, Entity>类。这种设计方式使得Service实现类同时具备 IService 提供的高级功能和 ServiceImpl 封装的 BaseMapper 基本操作，大大简化了代码的编写。

以用户服务YonghuService为例，核心代码结构如下：

```java
public interface YonghuService extends IService<YonghuEntity> {
}

@Service("yonghuService")
public class YonghuServiceImpl extends ServiceImpl<YonghuDao, YonghuEntity> implements YonghuService {
}
```

对于复杂的业务逻辑，在Service实现类中进行处理。以用户注册为例，需要检查用户名、联系方式、身份证号是否已被使用，如果都未使用则创建新用户：

```java
public R register(YonghuEntity yonghu, HttpServletRequest request) {
    Wrapper<YonghuEntity> queryWrapper = new EntityWrapper<YonghuEntity>()
        .eq("username", yonghu.getUsername())
        .or()
        .eq("yonghu_phone", yonghu.getYonghuPhone())
        .or()
        .eq("yonghu_id_number", yonghu.getYonghuIdNumber());
    
    YonghuEntity yonghuEntity = yonghuService.selectOne(queryWrapper);
    if(yonghuEntity != null) {
        return R.error("账户或者联系方式或者身份证号已经被使用");
    }
    yonghu.setNewMoney(0.0);
    yonghu.setCreateTime(new Date());
    yonghuService.insert(yonghu);
    return R.ok();
}
```

#### 4.2.4 Controller层实现

Controller层负责处理用户请求和响应，在本系统中采用@RestController注解标记 RESTful风格的控制器。Controller层通过依赖注入获取Service层的实例，调用相应的业务方法处理请求，并将结果封装为统一的响应格式返回给前端。

以用户控制器YonghuController为例，核心代码结构如下：

```java
@RestController
@RequestMapping("/yonghu")
public class YonghuController {
    
    @Autowired
    private YonghuService yonghuService;
    
    @Autowired
    private TokenService tokenService;
    
    @IgnoreAuth
    @RequestMapping(value = "/login")
    public R login(String username, String password, String captcha, HttpServletRequest request) {
        YonghuEntity yonghu = yonghuService.selectOne(
            new EntityWrapper<YonghuEntity>().eq("username", username));
        if(yonghu==null || !yonghu.getPassword().equals(password)) {
            return R.error("账号或密码不正确");
        }
        String token = tokenService.generateToken(yonghu.getId(), username, "yonghu", "用户");
        R r = R.ok();
        r.put("token", token);
        r.put("role", "用户");
        r.put("username", yonghu.getYonghuName());
        r.put("tableName", "yonghu");
        r.put("userId", yonghu.getId());
        return r;
    }
    
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if("用户".equals(role)) {
            params.put("yonghuId", request.getSession().getAttribute("userId"));
        }
        PageUtils page = yonghuService.queryPage(params);
        return R.ok().put("data", page);
    }
}
```

#### 4.2.5 统一响应封装

本系统定义了统一的响应格式类R，用于封装接口的返回结果。响应格式包含code（状态码）、msg（消息）、data（数据）三个字段。当code为0时表示请求成功，其他值表示请求失败。

```java
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    
    public R() {
        put("code", 0);
    }
    
    public static R ok() {
        return new R();
    }
    
    public static R error() {
        return error(500, "未知异常，请联系管理员");
    }
    
    public static R error(String msg) {
        return error(500, msg);
    }
    
    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }
    
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
```

#### 4.2.6 Token认证机制

本系统使用自定义的Token认证机制实现用户登录状态的维护。用户登录成功后，后端生成一个唯一的Token令牌返回给前端，前端将Token存储在本地，每次请求时将Token放在请求头中传递给后端。后端通过拦截器验证Token的有效性，验证通过后将用户信息存入Session中，供后续业务逻辑使用。

Token服务类TokenService的核心代码如下：

```java
public class TokenService {
    
    @Autowired
    private TokenDao tokenDao;
    
    public String generateToken(Long userId, String username, String tableName, String role) {
        String token = UUID.randomUUID().toString().replace("-", "");
        Date expireTime = new Date(System.currentTimeMillis() + 7200 * 1000);
        
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUserid(userId);
        tokenEntity.setUsername(username);
        tokenEntity.setTablename(tableName);
        tokenEntity.setRole(role);
        tokenEntity.setToken(token);
        tokenEntity.setExpiratedtime(expireTime);
        
        tokenDao.insert(tokenEntity);
        return token;
    }
}
```

认证拦截器AuthorizationInterceptor在请求处理前验证Token的有效性，如果Token有效则将用户信息存入Session，否则返回未授权错误。

#### 4.2.7 跨域处理

本系统通过实现WebMvcConfigurer接口并重写addCorsMappings方法来配置跨域资源共享（CORS）。配置允许所有的来源、所有的请求方法、所有的请求头，并允许credentials支持Cookie的跨域传递。

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
```

### 4.3 前端实现

#### 4.3.1 项目创建与配置

前端项目使用Vite构建工具创建，项目名称为frontend。基于Vue3版本进行开发，使用Vue Router进行路由管理，使用Pinia进行状态管理。项目的主要依赖包括vue（核心框架）、vue-router（路由管理）、pinia（状态管理）、axios（HTTP请求）、sass（CSS预处理器）等。

在vite.config.js配置文件中，配置了开发服务器的代理，将API请求代理到后端服务器，解决了开发环境下的跨域问题。

```javascript
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 8081,
    proxy: {
      '/wangshangchongwudian': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

#### 4.3.2 路由配置

前端路由配置文件位于src/router/index.js，使用Vue Router4的语法进行配置。路由分为用户端路由和管理端路由两部分，用户端路由使用userLayout布局，访问前缀为/user，配置了登录、注册、首页、商品、商品详情、购物车、订单、宠物寄养、论坛、公告资讯、个人中心等路由。管理端路由使用adminLayout布局，访问前缀为/admin，配置了管理员登录、首页、个人中心、用户管理、商品管理、订单管理、宠物寄养订单管理、论坛管理、公告管理、轮播图管理、基础数据管理等路由。

路由导航守卫在用户访问需要登录的页面时进行权限验证，检查本地存储中是否存在有效的Token，如果不存在则跳转到登录页面。

```javascript
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  
  if (requiresAuth && !token) {
    next('/user/login')
  } else {
    next()
  }
})
```

#### 4.3.3 API封装

前端API封装文件位于src/utils/api.js，将所有的API请求按照业务模块进行组织。每个模块提供getPage（分页查询）、getInfo（详情查询）、save（保存）、update（更新）、delete（删除）、getList（列表查询）、add（新增）、getDetail（详情）等方法。这种封装方式使得API调用更加直观和便捷。

以用户API为例，核心代码结构如下：

```javascript
export const yonghuApi = {
  getPage: params => http.get('/yonghu/page', params),
  getInfo: id => http.get(`/yonghu/info/${id}`),
  save: data => http.post('/yonghu/save', data),
  update: data => http.post('/yonghu/update', data),
  delete: ids => http.post('/yonghu/delete', ids),
  getList: params => http.get('/yonghu/list', params),
  getDetail: id => http.get(`/yonghu/detail/${id}`),
  add: data => http.post('/yonghu/add', data),
  login: (data, config) => http.post('/yonghu/login', data, config),
  register: data => http.post('/yonghu/register', data),
  session: () => http.get('/yonghu/session'),
  logout: () => http.get('/yonghu/logout'),
}
```

HTTP请求封装文件位于src/utils/request.js，使用Axios创建请求实例，配置了请求拦截器和响应拦截器。请求拦截器在请求发送前从本地存储获取Token并添加到请求头中。响应拦截器对返回结果进行统一处理，根据code值判断请求是否成功，对401未授权错误自动跳转到登录页面。

```javascript
const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '//localhost:8080/wangshangchongwudian',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json;charset=utf-8' }
})

request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.token = token
  }
  return config
})

request.interceptors.response.use(response => {
  const res = response.data
  if (res.code === 0) {
    return res
  } else if (res.code === 401) {
    router.push('/user/login')
    return Promise.reject(error)
  } else {
    return Promise.reject(error)
  }
})
```

#### 4.3.4 用户登录功能实现

用户登录页面使用Vue3的Composition API进行开发，页面布局采用左右分栏设计，左侧展示品牌信息和平台特色，右侧展示登录表单。用户输入用户名和密码后，点击登录按钮触发登录处理函数。

登录处理函数的核心代码如下：

```javascript
const handleLogin = async (e) => {
  e.preventDefault()
  try {
    isLoading.value = true
    const formData = new URLSearchParams()
    formData.append('username', form.value.username)
    formData.append('password', form.value.password)
    
    const response = await yonghuApi.login(formData, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    })
    
    if (response.code === 0) {
      if (response.token) {
        localStorage.setItem('token', response.token)
      }
      const userResponse = await yonghuApi.session()
      if (userResponse.code === 0) {
        router.push('/')
        message.success('登录成功')
      }
    }
  } catch (error) {
    message.error('登录失败，请稍后重试')
  } finally {
    isLoading.value = false
  }
}
```

登录成功后，系统保存Token到本地存储，并获取用户会话信息后跳转到首页。登录失败时显示错误提示信息，引导用户重新输入凭证。

#### 4.3.5 商品展示功能实现

商品列表页面使用Vue3的Composition API开发，页面初始化时调用API获取商品列表数据，并使用v-for指令渲染商品卡片。商品卡片展示商品图片、商品名称、商品价格等信息，点击商品卡片跳转到商品详情页面。

核心代码结构如下：

```javascript
const fetchProducts = async () => {
  try {
    loading.value = true
    const response = await chongwuyongpinApi.getList({ page: 1, limit: 6 })
    if (response.code === 0) {
      products.value = response.data.list || []
    }
  } catch (err) {
    console.error('获取商品列表失败:', err)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchProducts()
})
```

商品详情页面展示商品的完整信息，包括商品图片、商品名称、商品价格、商品库存、商品介绍等。用户可以调整购买数量，将商品加入购物车或直接购买。

#### 4.3.6 购物车功能实现

购物车页面展示用户已添加的商品列表，支持修改商品数量、删除商品、清空购物车等操作。用户选中商品后点击结算按钮，跳转到订单确认页面。

核心代码结构如下：

```javascript
const loadCartItems = async () => {
  try {
    loading.value = true
    const sessionResponse = await usersApi.session()
    if (sessionResponse.code === 0 && sessionResponse.data) {
      const yonghuId = sessionResponse.data.yonghuId || sessionResponse.data.id
      const response = await cartApi.getList({ page: 1, limit: 8, yonghuId })
      if (response.code === 0) {
        cartItems.value = response.data.list || []
      }
    }
  } catch (error) {
    console.error('获取购物车商品失败:', error)
  } finally {
    loading.value = false
  }
}

const checkout = async () => {
  if (selectedItems.value.length === 0) {
    message.error('请选择要结算的商品')
    return
  }
  router.push({
    path: '/order-confirm',
    query: { selectedItems: JSON.stringify(selectedItems.value) }
  })
}
```

#### 4.3.7 论坛功能实现

论坛页面为用户提供交流分享的平台，支持发布新帖、查看帖子列表、回复帖子等功能。帖子的发布和回复采用富文本编辑器，支持图文混排。

论坛列表页面核心代码如下：

```javascript
const fetchForumList = async () => {
  try {
    loading.value = true
    const response = await forumApi.getList({ page: 1, limit: 10 })
    if (response.code === 0) {
      forumList.value = response.data.list || []
    }
  } catch (error) {
    console.error('获取论坛列表失败:', error)
  } finally {
    loading.value = false
  }
}
```

#### 4.3.8 宠物寄养功能实现

宠物寄养模块提供寄养服务的浏览和预约功能。用户选择寄养服务后，填写宠物信息、寄养开始时间、寄养天数等预约信息，提交预约申请。预约申请提交后，系统生成寄养预约订单，等待管理员审核。

核心代码如下：

```javascript
const submitReservation = async () => {
  try {
    const response = await chongwujiyangYuyueApi.add({
      chongwujiyangId: serviceId.value,
      yonghuId: userId.value,
      chongwujiyangYuyueName: petName.value,
      chongwuTypes: petType.value,
      chongwuZhongliang: petWeight.value,
      chongwujiyangYuyueTime: startDate.value,
      chongwujiyangYuyueNum: days.value,
      shifouTypes: needPickup.value ? 1 : 2,
      chongwujiyangYuyuePrice: totalPrice.value
    })
    if (response.code === 0) {
      message.success('预约提交成功')
      router.push('/user/orders')
    }
  } catch (error) {
    message.error('预约提交失败')
  }
}
```

### 4.5 关键技术实现

#### 4.5.1 分页查询实现

本系统使用MyBatis-Plus提供的分页插件实现数据分页查询功能。首先在配置类中注册分页插件，然后使用Page对象指定当前页码和每页记录数进行查询。

后端分页查询的核心代码如下：

```java
@Configuration
public class MybatisPlusConfig {
    
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setLimit(-1);
        return page;
    }
}

@RequestMapping("/page")
public R page(@RequestParam Map<String, Object> params) {
    PageUtils page = yonghuService.queryPage(params);
    return R.ok().put("data", page);
}
```

前端使用组件封装分页查询逻辑，传入当前页码和每页记录数，调用API获取分页数据后渲染页面。

#### 4.5.2 数据字典实现

本系统使用字典表存储系统中的枚举类型数据，如商品类型、订单状态、性别等。前端通过API获取字典数据后，在页面中展示对应的文字描述，而不是存储的数值。

字典服务将数字编码转换为文字描述：

```java
public void dictionaryConvert(Object view, HttpServletRequest request) {
    YonghuView yonghuView = (YonghuView) view;
    Map<Integer, String> sexTypesMap = dictionaryService.getStrMap("sex_types");
    yonghuView.setSexTypesValue(sexTypesMap.get(yonghuView.getSexTypes()));
}
```

#### 4.5.3 文件上传实现

本系统使用SpringBoot的文件上传功能处理图片等文件的上传。文件上传接口接收前端传来的文件流，保存到服务器指定目录，并返回文件的访问路径。

```java
@PostMapping("/upload")
public R upload(@RequestParam("file") MultipartFile file) {
    if (file.isEmpty()) {
        return R.error("上传文件不能为空");
    }
    String fileName = file.getOriginalFilename();
    String suffix = fileName.substring(fileName.lastIndexOf("."));
    String newFileName = UUID.randomUUID().toString() + suffix;
    String filePath = ResourceUtils.getFile("classpath:static/upload/").getAbsolutePath() + File.separator + newFileName;
    
    File dest = new File(filePath);
    file.transferTo(dest);
    
    return R.ok().put("url", "/upload/" + newFileName);
}
```

---

## 5 系统测试

### 5.1 测试环境

本系统的测试环境配置如下：服务器操作系统为Windows10，JDK版本为1.8，Tomcat版本为9.0，MySQL版本为5.7。前端测试浏览器为Chrome浏览器，版本为100以上。网络环境为局域网100Mbps带宽。

### 5.2 功能测试

本节对系统的主要功能模块进行测试验证。

**（1）用户注册登录功能测试**

测试用户注册功能时，输入用户名、用户姓名、联系方式、身份证号等必填信息，提交注册申请。系统检查用户信息的合法性，如果用户名已存在则提示错误信息，如果信息合法则创建新用户并返回注册成功提示。

测试用户登录功能时，输入正确的用户名和密码，系统验证用户身份后生成Token并跳转至首页。输入错误的密码，系统提示账号或密码不正确的错误信息。登录成功后，系统在本地存储中保存Token，后续请求携带Token进行身份验证。

**（2）商品浏览功能测试**

测试商品列表展示功能，系统正确展示商品图片、商品名称、商品价格等信息。测试商品分类筛选功能，根据选择的商品类型过滤商品列表。测试商品搜索功能，输入关键词后系统返回匹配的商品结果。测试商品详情展示功能，点击商品后正确展示商品的完整信息。

**（3）购物车功能测试**

测试添加商品到购物车功能，在商品详情页面点击加入购物车按钮，系统将商品添加到购物车并提示添加成功。测试修改购物车商品数量功能，在购物车页面调整商品数量后，系统自动计算小计金额并更新购物车数据。测试删除购物车商品功能，点击删除按钮后，系统移除商品并更新购物车列表。

**（4）订单功能测试**

测试订单确认功能，选择购物车中的商品后跳转到订单确认页面，正确展示商品信息、收货地址等信息。测试订单提交功能，选择收货地址和支付方式后提交订单，系统生成订单并跳转至订单列表页面。测试订单支付功能，点击支付按钮后系统扣除用户余额并更新订单状态。

**（5）宠物寄养预约功能测试**

测试寄养服务浏览功能，系统正确展示各类寄养服务的信息。测试寄养预约功能，填写宠物信息和预约时间后提交预约申请，系统生成预约订单等待审核。

**（6）论坛功能测试**

测试帖子浏览功能，系统正确展示论坛帖子列表。测试发布帖子功能，输入标题和内容后提交，系统创建新帖子并展示在帖子列表中。测试回复帖子功能，在帖子详情页面输入回复内容后提交，系统添加回复并更新帖子页面。

### 5.3 测试结果

经过全面的功能测试，本系统的各项功能均能正常运行。用户注册登录、商品浏览、购物车管理、订单处理、宠物寄养预约、论坛交流、公告资讯浏览、客服聊天等核心功能模块均通过测试，满足设计要求。系统界面友好、操作流畅、响应及时，具备良好的用户体验。

---

## 6 总结

### 6.1 成果总结

本文设计并实现了一个基于SpringBoot和Vue的网上宠物店系统，主要完成了以下工作：

在需求分析阶段，通过对宠物行业和电商市场的调研，明确了系统的功能需求和非功能需求，确定了普通用户和管理员两类角色，梳理了商品管理、订单处理、宠物寄养、论坛交流等核心业务模块。

在系统设计阶段，采用前后端分离的架构设计，后端使用SpringBoot+MyBatis-Plus技术栈，前端使用Vue3+Vite技术栈。设计了合理的数据库结构，绘制了系统功能模块图和数据库ER图。

在系统实现阶段，按照模块化的开发思路，依次实现了用户模块、商品模块、购物车模块、订单模块、宠物寄养模块、论坛模块、公告模块、客服模块等功能。前端采用Vue3的Composition API进行开发，提供了良好的用户界面和交互体验。

在系统测试阶段，对系统的各项功能进行了全面的测试验证，确保系统的稳定性和可靠性。

### 6.2 存在的问题与展望

本系统虽然基本实现了网上宠物店的核心功能，但还存在一些可以改进的地方。首先，支付功能目前只实现了余额支付，可以接入第三方支付平台如支付宝、微信支付等，提供更多的支付选择。其次，可以引入消息队列实现订单异步处理，提高系统的并发处理能力。再次，可以增加数据分析和可视化功能，为店铺运营提供数据支持。最后，可以开发移动端APP或小程序，为用户提供更加便捷的移动端访问体验。

随着宠物行业的持续发展和技术的不断进步，网上宠物店系统将会在功能和服务方面不断丰富和完善，为宠物主人提供更加便捷、优质的购物和服务体验。

---

## 参考文献

[1] 宋馨来. 基于前后端分离和SpringSecurity的用户登录功能的设计与实现[D].

[2] 叶方超. 基于SpringBoot的旧物回收商城的设计与实现[D].

[3] 陈蓓蕾. 基于SpringBoot的数据库接口设计[D].

[4] 田松涛. 基于SpringBoot的线上商城平台设计[D].

[5] 刘敏. 基于SpringBoot框架社交网络平台的设计与实现[D].

[6] 陈小燕. 基于SpringBootVue的好农物商城的设计与实现[D].

[7] 金馨. 基于SSM的宠物店线上运营系统的设计与实现[D].

[8] 杨华. 基于SSM的宠物店信息系统的设计与实现[D].

[9] 丁宁, 王志辉. SpringBoot实战[M]. 北京: 人民邮电出版社, 2020.

[10] 阮一鸣. Vue3企业级应用开发实战[M]. 北京: 电子工业出版社, 2021.