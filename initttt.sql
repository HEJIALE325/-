-- MySQL dump 10.13  Distrib 8.0.39, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: wangshangchongwudian
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 ',
  `yonghu_id` int NOT NULL COMMENT '创建用户',
  `address_name` varchar(200) NOT NULL COMMENT '收货人 ',
  `address_phone` varchar(200) NOT NULL COMMENT '电话 ',
  `address_dizhi` varchar(200) NOT NULL COMMENT '地址 ',
  `isdefault_types` int NOT NULL COMMENT '是否默认地址 ',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间 show3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3 COMMENT='收货地址';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` (`id`, `yonghu_id`, `address_name`, `address_phone`, `address_dizhi`, `isdefault_types`, `insert_time`, `update_time`, `create_time`) VALUES (2,3,'收货人2','17703786902','地址2',1,'2023-03-11 08:32:48','2023-03-11 08:32:48','2023-03-11 08:32:48'),(7,3,'收货人7','17703786907','地址7',1,'2023-03-11 08:32:48','2023-03-11 08:32:48','2023-03-11 08:32:48'),(9,3,'收货人9','17703786909','地址9',1,'2023-03-11 08:32:48','2023-03-11 08:32:48','2023-03-11 08:32:48'),(11,3,'收货人11','17703786911','地址11',1,'2023-03-11 08:32:48','2023-03-11 08:32:48','2023-03-11 08:32:48'),(15,2,'收货人','13265464988','地址2',1,'2026-04-08 01:05:28','2026-04-08 01:06:45','2026-04-08 01:05:28'),(17,1,'何家乐','19189079999','123',0,'2026-04-08 01:21:33','2026-04-08 01:21:32','2026-04-08 01:21:33');
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `yonghu_id` int DEFAULT NULL COMMENT '所属用户',
  `chongwuyongpin_id` int DEFAULT NULL COMMENT '商品',
  `buy_number` int DEFAULT NULL COMMENT '购买数量',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '添加时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb3 COMMENT='购物车';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` (`id`, `yonghu_id`, `chongwuyongpin_id`, `buy_number`, `create_time`, `update_time`, `insert_time`) VALUES (8,NULL,14,2,'2026-04-09 02:49:14','2026-04-09 03:12:51','2026-04-09 02:49:14'),(9,NULL,13,1,'2026-04-09 02:49:15','2026-04-09 03:05:35','2026-04-09 02:49:15'),(14,NULL,14,NULL,'2026-04-09 03:28:44',NULL,'2026-04-09 03:28:44'),(16,NULL,14,NULL,'2026-04-09 03:35:33',NULL,'2026-04-09 03:35:33'),(17,NULL,13,NULL,'2026-04-09 03:40:14',NULL,'2026-04-09 03:40:14'),(23,2,12,1,'2026-04-09 06:23:10',NULL,'2026-04-09 06:23:10'),(24,2,13,1,'2026-04-09 06:23:11',NULL,'2026-04-09 06:23:11');
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat`
--

DROP TABLE IF EXISTS `chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `yonghu_id` int DEFAULT NULL COMMENT '提问用户',
  `chat_issue` varchar(200) DEFAULT NULL COMMENT '问题',
  `issue_time` timestamp NULL DEFAULT NULL COMMENT '问题时间 Search111',
  `chat_reply` varchar(200) DEFAULT NULL COMMENT '回复',
  `reply_time` timestamp NULL DEFAULT NULL COMMENT '回复时间 Search111',
  `zhuangtai_types` int DEFAULT NULL COMMENT '状态',
  `chat_types` int DEFAULT NULL COMMENT '数据类型',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COMMENT='客服聊天';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat`
--

LOCK TABLES `chat` WRITE;
/*!40000 ALTER TABLE `chat` DISABLE KEYS */;
INSERT INTO `chat` (`id`, `yonghu_id`, `chat_issue`, `issue_time`, `chat_reply`, `reply_time`, `zhuangtai_types`, `chat_types`, `insert_time`) VALUES (1,1,'123','2023-03-11 09:42:06',NULL,NULL,2,1,'2023-03-11 09:42:07'),(2,1,NULL,NULL,'123123','2023-03-11 09:42:41',NULL,2,'2023-03-11 09:42:41');
/*!40000 ALTER TABLE `chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chongwujiyang`
--

DROP TABLE IF EXISTS `chongwujiyang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chongwujiyang` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 ',
  `chongwujiyang_name` varchar(200) DEFAULT NULL COMMENT '寄养服务名称  Search111 ',
  `chongwujiyang_photo` varchar(200) DEFAULT NULL COMMENT '寄养服务封面',
  `chongwujiyang_types` int DEFAULT NULL COMMENT '寄养服务类型 Search111',
  `chongwujiyang_new_money` decimal(10,2) DEFAULT NULL COMMENT '价格/天',
  `chongwujiyang_clicknum` int DEFAULT NULL COMMENT '寄养服务热度',
  `chongwujiyang_content` text COMMENT '寄养服务介绍 ',
  `chongwujiyang_delete` int DEFAULT NULL COMMENT '逻辑删除',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '录入时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间  show1 show2 photoShow',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3 COMMENT='宠物寄养';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chongwujiyang`
--

LOCK TABLES `chongwujiyang` WRITE;
/*!40000 ALTER TABLE `chongwujiyang` DISABLE KEYS */;
INSERT INTO `chongwujiyang` (`id`, `chongwujiyang_name`, `chongwujiyang_photo`, `chongwujiyang_types`, `chongwujiyang_new_money`, `chongwujiyang_clicknum`, `chongwujiyang_content`, `chongwujiyang_delete`, `insert_time`, `create_time`) VALUES (1,'寄养服务名称1','upload/chongwujiyang1.jpg',2,224.12,11,'寄养服务介绍1',1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(2,'寄养服务名称2','upload/chongwujiyang2.jpg',2,361.60,436,'寄养服务介绍2',1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(3,'寄养服务名称3','upload/chongwujiyang3.jpg',1,389.80,212,'寄养服务介绍3',1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(4,'寄养服务名称4','upload/chongwujiyang4.jpg',3,450.48,159,'寄养服务介绍4',1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(5,'寄养服务名称5','upload/chongwujiyang5.jpg',3,52.37,315,'寄养服务介绍5',1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(6,'寄养服务名称6','upload/chongwujiyang6.jpg',3,101.53,387,'寄养服务介绍6',1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(7,'寄养服务名称7','upload/chongwujiyang7.jpg',3,353.63,180,'寄养服务介绍7',1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(8,'寄养服务名称8','upload/chongwujiyang1.jpg',3,278.85,251,'寄养服务介绍8',1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(9,'寄养服务名称9','upload/chongwujiyang2.jpg',3,482.27,214,'寄养服务介绍9',1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(10,'寄养服务名称10','upload/chongwujiyang3.jpg',2,410.38,479,'寄养服务介绍10',1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(11,'寄养服务名称11','upload/chongwujiyang4.jpg',2,69.58,205,'寄养服务介绍11',1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(12,'寄养服务名称12','upload/chongwujiyang5.jpg',1,280.04,49,'寄养服务介绍12',1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(13,'寄养服务名称13','upload/chongwujiyang6.jpg',2,148.79,413,'寄养服务介绍13',1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(14,'寄养服务名称14','upload/chongwujiyang7.jpg',2,93.48,110,'寄养服务介绍14',1,'2023-03-11 08:32:48','2023-03-11 08:32:48');
/*!40000 ALTER TABLE `chongwujiyang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chongwujiyang_yuyue`
--

DROP TABLE IF EXISTS `chongwujiyang_yuyue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chongwujiyang_yuyue` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `chongwujiyang_id` int DEFAULT NULL COMMENT '宠物寄养 ',
  `yonghu_id` int DEFAULT NULL COMMENT '用户',
  `chongwujiyang_yuyue_name` varchar(200) DEFAULT NULL COMMENT '宠物名称  Search111 ',
  `chongwu_types` int DEFAULT NULL COMMENT '宠物类型  Search111 ',
  `chongwu_zhongliang` int DEFAULT NULL COMMENT '宠物重量',
  `chongwujiyang_yuyue_time` timestamp NULL DEFAULT NULL COMMENT '寄养开始时间',
  `chongwujiyang_yuyue_num` int DEFAULT NULL COMMENT '寄养天数 ',
  `shifou_types` int DEFAULT NULL COMMENT '是否接送  Search111 ',
  `chongwujiyang_yuyue_price` decimal(10,2) DEFAULT NULL COMMENT '总价格',
  `chongwujiyang_yuyue_yesno_types` int DEFAULT NULL COMMENT '寄养审核  Search111 ',
  `chongwujiyang_yuyue_yesno_text` text COMMENT '审核结果',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '添加时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间 show3 listShow',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3 COMMENT='宠物寄养订单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chongwujiyang_yuyue`
--

LOCK TABLES `chongwujiyang_yuyue` WRITE;
/*!40000 ALTER TABLE `chongwujiyang_yuyue` DISABLE KEYS */;
INSERT INTO `chongwujiyang_yuyue` (`id`, `chongwujiyang_id`, `yonghu_id`, `chongwujiyang_yuyue_name`, `chongwu_types`, `chongwu_zhongliang`, `chongwujiyang_yuyue_time`, `chongwujiyang_yuyue_num`, `shifou_types`, `chongwujiyang_yuyue_price`, `chongwujiyang_yuyue_yesno_types`, `chongwujiyang_yuyue_yesno_text`, `insert_time`, `create_time`) VALUES (1,1,3,'宠物名称1',2,376,'2023-03-11 08:32:48',254,1,197.44,1,NULL,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(2,2,1,'宠物名称2',2,291,'2023-03-11 08:32:48',247,2,654.35,1,NULL,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(3,3,1,'宠物名称3',2,35,'2023-03-11 08:32:48',50,2,561.75,1,NULL,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(4,4,3,'宠物名称4',3,130,'2023-03-11 08:32:48',219,1,737.60,1,NULL,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(5,5,1,'宠物名称5',3,349,'2023-03-11 08:32:48',369,1,185.48,1,NULL,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(6,6,1,'宠物名称6',1,354,'2023-03-11 08:32:48',478,2,618.21,1,NULL,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(7,7,3,'宠物名称7',1,414,'2023-03-11 08:32:48',473,2,200.20,1,NULL,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(8,8,2,'宠物名称8',2,93,'2023-03-11 08:32:48',26,1,959.70,1,NULL,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(9,9,3,'宠物名称9',1,31,'2023-03-11 08:32:48',332,2,946.05,1,NULL,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(10,10,2,'宠物名称10',3,39,'2023-03-11 08:32:48',277,2,784.89,1,NULL,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(11,11,2,'宠物名称11',2,476,'2023-03-11 08:32:48',287,2,666.22,1,NULL,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(12,12,1,'宠物名称12',1,164,'2023-03-11 08:32:48',111,2,231.58,1,NULL,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(13,13,3,'宠物名称13',2,306,'2023-03-11 08:32:48',176,1,189.21,2,NULL,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(14,14,2,'宠物名称14',3,265,'2023-03-11 08:32:48',453,2,556.11,3,'余额不足请充值1111','2023-03-11 08:32:48','2023-03-11 08:32:48'),(15,2,1,'123',2,123,'2023-03-18 09:39:44',8,1,2912.80,2,'123','2023-03-11 09:39:57','2023-03-11 09:39:57'),(16,13,1,'寄养服务名称13',NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,'2026-04-12 06:24:33','2026-04-12 06:24:33'),(17,14,1,'小白',2,14,'2026-04-13 12:00:00',7,1,654.36,2,'789','2026-04-12 06:59:09','2026-04-12 06:59:09'),(19,12,1,'777',3,77,'2026-04-12 08:22:00',1,1,280.04,2,NULL,'2026-04-12 08:22:15','2026-04-12 08:22:15'),(20,9,1,'88',3,12,'2026-04-25 08:41:00',1,2,482.27,2,NULL,'2026-04-12 08:42:06','2026-04-12 08:42:06');
/*!40000 ALTER TABLE `chongwujiyang_yuyue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chongwurizhi`
--

DROP TABLE IF EXISTS `chongwurizhi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chongwurizhi` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 ',
  `chongwujiyang_yuyue_id` int DEFAULT NULL COMMENT '宠物寄养订单',
  `chongwurizhi_name` varchar(200) DEFAULT NULL COMMENT '标题  Search111 ',
  `chongwurizhi_content` text COMMENT '寄养服务详情',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '录入时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间 show3 listShow',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3 COMMENT='寄存日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chongwurizhi`
--

LOCK TABLES `chongwurizhi` WRITE;
/*!40000 ALTER TABLE `chongwurizhi` DISABLE KEYS */;
INSERT INTO `chongwurizhi` (`id`, `chongwujiyang_yuyue_id`, `chongwurizhi_name`, `chongwurizhi_content`, `insert_time`, `create_time`) VALUES (1,1,'标题1','寄养服务详情1','2023-03-11 08:32:48','2023-03-11 08:32:48'),(2,2,'标题2','寄养服务详情2','2023-03-11 08:32:48','2023-03-11 08:32:48'),(3,3,'标题3','寄养服务详情3','2023-03-11 08:32:48','2023-03-11 08:32:48'),(4,4,'标题4','寄养服务详情4','2023-03-11 08:32:48','2023-03-11 08:32:48'),(5,5,'标题5','寄养服务详情5','2023-03-11 08:32:48','2023-03-11 08:32:48'),(6,6,'标题6','寄养服务详情6','2023-03-11 08:32:48','2023-03-11 08:32:48'),(7,7,'标题7','寄养服务详情7','2023-03-11 08:32:48','2023-03-11 08:32:48'),(8,8,'标题8','寄养服务详情8','2023-03-11 08:32:48','2023-03-11 08:32:48'),(9,9,'标题9','寄养服务详情9','2023-03-11 08:32:48','2023-03-11 08:32:48'),(10,10,'标题10','寄养服务详情10','2023-03-11 08:32:48','2023-03-11 08:32:48'),(11,11,'标题11','寄养服务详情11','2023-03-11 08:32:48','2023-03-11 08:32:48'),(12,12,'标题12','寄养服务详情12','2023-03-11 08:32:48','2023-03-11 08:32:48'),(13,13,'标题13','寄养服务详情13','2023-03-11 08:32:48','2023-03-11 08:32:48'),(14,14,'标题14','寄养服务详情14','2023-03-11 08:32:48','2023-03-11 08:32:48'),(15,15,'123','<p>123</p>','2023-03-11 09:42:29','2023-03-11 09:42:29');
/*!40000 ALTER TABLE `chongwurizhi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chongwuyongpin`
--

DROP TABLE IF EXISTS `chongwuyongpin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chongwuyongpin` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 ',
  `chongwuyongpin_name` varchar(200) DEFAULT NULL COMMENT '商品名称  Search111 ',
  `chongwuyongpin_uuid_number` varchar(200) DEFAULT NULL COMMENT '商品编号',
  `chongwuyongpin_photo` varchar(200) DEFAULT NULL COMMENT '商品照片',
  `chongwuyongpin_types` int DEFAULT NULL COMMENT '商品类型 Search111',
  `chongwuyongpin_kucun_number` int DEFAULT NULL COMMENT '商品库存',
  `chongwuyongpin_old_money` decimal(10,2) DEFAULT NULL COMMENT '商品原价 ',
  `chongwuyongpin_new_money` decimal(10,2) DEFAULT NULL COMMENT '现价',
  `chongwuyongpin_clicknum` int DEFAULT NULL COMMENT '商品热度',
  `chongwuyongpin_content` text COMMENT '商品介绍 ',
  `shangxia_types` int DEFAULT NULL COMMENT '是否上架 ',
  `chongwuyongpin_delete` int DEFAULT NULL COMMENT '逻辑删除',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '录入时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间  show1 show2 photoShow',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3 COMMENT='商品';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chongwuyongpin`
--

LOCK TABLES `chongwuyongpin` WRITE;
/*!40000 ALTER TABLE `chongwuyongpin` DISABLE KEYS */;
INSERT INTO `chongwuyongpin` (`id`, `chongwuyongpin_name`, `chongwuyongpin_uuid_number`, `chongwuyongpin_photo`, `chongwuyongpin_types`, `chongwuyongpin_kucun_number`, `chongwuyongpin_old_money`, `chongwuyongpin_new_money`, `chongwuyongpin_clicknum`, `chongwuyongpin_content`, `shangxia_types`, `chongwuyongpin_delete`, `insert_time`, `create_time`) VALUES (1,'商品名称1','1678523568634','upload/chongwuyongpin1.jpg',1,101,697.02,44.19,110,'商品介绍1',1,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(2,'商品名称2','1678523568630','upload/chongwuyongpin2.jpg',1,102,682.23,377.16,155,'商品介绍2',1,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(3,'商品名称3','1678523568648','upload/chongwuyongpin3.jpg',1,103,868.29,47.84,57,'商品介绍3',1,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(4,'商品名称4','1678523568584','upload/chongwuyongpin4.jpg',3,104,578.94,153.11,152,'商品介绍4',1,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(5,'商品名称5','1678523568591','upload/chongwuyongpin5.jpg',1,105,827.88,321.76,159,'商品介绍5',1,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(6,'商品名称6','1678523568655','upload/chongwuyongpin6.jpg',3,106,914.20,396.75,243,'商品介绍6',1,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(7,'商品名称7','1678523568581','upload/chongwuyongpin7.jpg',1,107,561.88,22.49,401,'商品介绍7',1,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(8,'商品名称8','1678523568584','upload/chongwuyongpin8.jpg',2,105,809.07,234.38,471,'商品介绍8',1,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(9,'商品名称9','1678523568616','upload/chongwuyongpin9.jpg',3,106,827.90,290.13,459,'商品介绍9',1,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(10,'商品名称10','1678523568622','upload/chongwuyongpin10.jpg',2,1008,579.20,219.81,331,'商品介绍10',1,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(11,'商品名称11','1678523568598','upload/chongwuyongpin11.jpg',3,1006,885.17,308.03,497,'商品介绍11',1,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(12,'商品名称12','1678523568606','upload/chongwuyongpin12.jpg',2,1009,928.57,383.83,149,'商品介绍12',1,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(13,'商品名称13','1678523568676','upload/chongwuyongpin13.jpg',2,1010,721.56,166.27,23,'商品介绍13',1,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(14,'商品名称14','1678523568662','upload/1775542413876.webp',3,1013,999.00,888.00,416,'商品介绍14',1,1,'2023-03-11 08:32:48','2023-03-11 08:32:48');
/*!40000 ALTER TABLE `chongwuyongpin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chongwuyongpin_collection`
--

DROP TABLE IF EXISTS `chongwuyongpin_collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chongwuyongpin_collection` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `chongwuyongpin_id` int DEFAULT NULL COMMENT '商品',
  `yonghu_id` int DEFAULT NULL COMMENT '用户',
  `chongwuyongpin_collection_types` int DEFAULT NULL COMMENT '类型',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '收藏时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间 show3 photoShow',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb3 COMMENT='商品收藏';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chongwuyongpin_collection`
--

LOCK TABLES `chongwuyongpin_collection` WRITE;
/*!40000 ALTER TABLE `chongwuyongpin_collection` DISABLE KEYS */;
INSERT INTO `chongwuyongpin_collection` (`id`, `chongwuyongpin_id`, `yonghu_id`, `chongwuyongpin_collection_types`, `insert_time`, `create_time`) VALUES (1,1,2,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(2,2,2,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(3,3,3,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(4,4,2,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(6,6,3,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(9,9,1,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(10,10,3,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(11,11,3,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(14,14,3,1,'2023-03-11 08:32:48','2023-03-11 08:32:48'),(15,12,1,1,'2023-03-11 09:36:50','2023-03-11 09:36:50'),(16,13,NULL,1,'2026-04-09 07:32:48','2026-04-09 07:32:48');
/*!40000 ALTER TABLE `chongwuyongpin_collection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chongwuyongpin_commentback`
--

DROP TABLE IF EXISTS `chongwuyongpin_commentback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chongwuyongpin_commentback` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `chongwuyongpin_id` int DEFAULT NULL COMMENT '商品',
  `yonghu_id` int DEFAULT NULL COMMENT '用户',
  `chongwuyongpin_commentback_text` text COMMENT '评价内容',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '评价时间',
  `reply_text` text COMMENT '回复内容',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '回复时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间 show3 listShow',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3 COMMENT='商品评价';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chongwuyongpin_commentback`
--

LOCK TABLES `chongwuyongpin_commentback` WRITE;
/*!40000 ALTER TABLE `chongwuyongpin_commentback` DISABLE KEYS */;
INSERT INTO `chongwuyongpin_commentback` (`id`, `chongwuyongpin_id`, `yonghu_id`, `chongwuyongpin_commentback_text`, `insert_time`, `reply_text`, `update_time`, `create_time`) VALUES (1,1,2,'评价内容1','2023-03-11 08:32:48','回复信息1','2023-03-11 08:32:48','2023-03-11 08:32:48'),(2,2,1,'评价内容2','2023-03-11 08:32:48','回复信息2','2023-03-11 08:32:48','2023-03-11 08:32:48'),(3,3,1,'评价内容3','2023-03-11 08:32:48','回复信息3','2023-03-11 08:32:48','2023-03-11 08:32:48'),(4,4,2,'评价内容4','2023-03-11 08:32:48','回复信息4','2023-03-11 08:32:48','2023-03-11 08:32:48'),(5,5,3,'评价内容5','2023-03-11 08:32:48','回复信息5','2023-03-11 08:32:48','2023-03-11 08:32:48'),(6,6,3,'评价内容6','2023-03-11 08:32:48','回复信息6','2023-03-11 08:32:48','2023-03-11 08:32:48'),(7,7,2,'评价内容7','2023-03-11 08:32:48','回复信息7','2023-03-11 08:32:48','2023-03-11 08:32:48'),(8,8,2,'评价内容8','2023-03-11 08:32:48','回复信息8','2023-03-11 08:32:48','2023-03-11 08:32:48'),(9,9,3,'评价内容9','2023-03-11 08:32:48','回复信息9','2023-03-11 08:32:48','2023-03-11 08:32:48'),(11,11,1,'评价内容11','2023-03-11 08:32:48','回复信息11','2023-03-11 08:32:48','2023-03-11 08:32:48'),(12,12,1,'评价内容12','2023-03-11 08:32:48','回复信息12','2023-03-11 08:32:48','2023-03-11 08:32:48'),(13,13,3,'评价内容13','2023-03-11 08:32:48','回复信息13','2023-03-11 08:32:48','2023-03-11 08:32:48'),(14,14,3,'评价内容14','2023-03-11 08:32:48','回复信息14','2023-03-11 08:32:48','2023-03-11 08:32:48'),(15,10,1,'123','2023-03-11 09:42:02','12312','2023-03-11 09:42:20','2023-03-11 09:42:02');
/*!40000 ALTER TABLE `chongwuyongpin_commentback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chongwuyongpin_order`
--

DROP TABLE IF EXISTS `chongwuyongpin_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chongwuyongpin_order` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `chongwuyongpin_order_uuid_number` varchar(200) DEFAULT NULL COMMENT '订单号 Search111 ',
  `address_id` int DEFAULT NULL COMMENT '收货地址 ',
  `chongwuyongpin_id` int DEFAULT NULL COMMENT '商品',
  `yonghu_id` int DEFAULT NULL COMMENT '用户',
  `buy_number` int DEFAULT NULL COMMENT '购买数量',
  `chongwuyongpin_order_true_price` decimal(10,2) DEFAULT NULL COMMENT '实付价格',
  `chongwuyongpin_order_types` int DEFAULT NULL COMMENT '订单类型 Search111 ',
  `chongwuyongpin_order_payment_types` int DEFAULT NULL COMMENT '支付类型',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '订单创建时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间 show3 listShow',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3 COMMENT='商品订单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chongwuyongpin_order`
--

LOCK TABLES `chongwuyongpin_order` WRITE;
/*!40000 ALTER TABLE `chongwuyongpin_order` DISABLE KEYS */;
INSERT INTO `chongwuyongpin_order` (`id`, `chongwuyongpin_order_uuid_number`, `address_id`, `chongwuyongpin_id`, `yonghu_id`, `buy_number`, `chongwuyongpin_order_true_price`, `chongwuyongpin_order_types`, `chongwuyongpin_order_payment_types`, `insert_time`, `create_time`) VALUES (1,'1678527396125',6,14,1,1,407.89,102,1,'2023-03-11 09:36:36','2023-03-11 09:36:36'),(2,'1678527396125',6,9,1,1,290.13,105,1,'2023-03-11 09:36:36','2023-03-11 09:36:36'),(3,'1678527396125',6,10,1,1,219.81,105,1,'2023-03-11 09:36:36','2023-03-11 09:36:36'),(4,'1775611309959',15,8,2,1,234.38,103,1,'2026-04-08 01:21:50','2026-04-08 01:21:50'),(5,'1775614270617',17,11,1,1,308.03,105,1,'2026-04-08 02:11:11','2026-04-08 02:11:11'),(6,'1775704027106',15,11,2,1,308.03,101,1,'2026-04-09 03:07:07','2026-04-09 03:07:07'),(7,'1775706826443',15,11,2,1,308.03,101,1,'2026-04-09 03:53:46','2026-04-09 03:53:46'),(8,'1775706826443',15,9,2,1,290.13,101,1,'2026-04-09 03:53:46','2026-04-09 03:53:46'),(9,'1775706826443',15,8,2,1,234.38,101,1,'2026-04-09 03:53:46','2026-04-09 03:53:46'),(10,'1775706964124',17,12,1,3,1151.49,101,1,'2026-04-09 03:56:04','2026-04-09 03:56:04'),(11,'1775715757814',15,13,2,2,332.54,101,1,'2026-04-09 06:22:38','2026-04-09 06:22:38'),(12,'1775715851175',17,9,1,1,290.13,101,1,'2026-04-09 06:24:11','2026-04-09 06:24:11'),(13,'1775715851175',17,8,1,1,234.38,101,1,'2026-04-09 06:24:11','2026-04-09 06:24:11'),(14,'1775716688557',17,10,1,1,219.81,101,1,'2026-04-09 06:38:09','2026-04-09 06:38:09'),(15,'1775716688557',17,13,1,1,166.27,101,1,'2026-04-09 06:38:09','2026-04-09 06:38:09'),(16,'1775782418079',17,11,1,2,NULL,101,2,'2026-04-10 00:53:38','2026-04-10 00:53:38'),(17,'1775782418079',17,14,1,1,NULL,101,2,'2026-04-10 00:53:38','2026-04-10 00:53:38');
/*!40000 ALTER TABLE `chongwuyongpin_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config`
--

DROP TABLE IF EXISTS `config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '配置参数名称',
  `value` varchar(100) DEFAULT NULL COMMENT '配置参数值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COMMENT='配置文件';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config`
--

LOCK TABLES `config` WRITE;
/*!40000 ALTER TABLE `config` DISABLE KEYS */;
INSERT INTO `config` (`id`, `name`, `value`) VALUES (1,'轮播图1','upload/config1.jpg'),(2,'轮播图2','upload/config2.jpg'),(3,'轮播图3','upload/config3.jpg'),(4,'faceFile','upload/1776338645687.jpg');
/*!40000 ALTER TABLE `config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dictionary`
--

DROP TABLE IF EXISTS `dictionary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dictionary` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dic_code` varchar(200) DEFAULT NULL COMMENT '字段',
  `dic_name` varchar(200) DEFAULT NULL COMMENT '字段名',
  `code_index` int DEFAULT NULL COMMENT '编码',
  `index_name` varchar(200) DEFAULT NULL COMMENT '编码名字  Search111 ',
  `super_id` int DEFAULT NULL COMMENT '父字段id',
  `beizhu` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb3 COMMENT='字典';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dictionary`
--

LOCK TABLES `dictionary` WRITE;
/*!40000 ALTER TABLE `dictionary` DISABLE KEYS */;
INSERT INTO `dictionary` (`id`, `dic_code`, `dic_name`, `code_index`, `index_name`, `super_id`, `beizhu`, `create_time`) VALUES (38,'shangxia_types','上下架',1,'上架',NULL,NULL,'2023-03-11 08:32:39'),(39,'shangxia_types','上下架',2,'下架',NULL,NULL,'2023-03-11 08:32:39'),(40,'chongwuyongpin_types','商品类型',1,'商品类型1',NULL,NULL,'2023-03-11 08:32:39'),(41,'chongwuyongpin_types','商品类型',2,'商品类型2',NULL,NULL,'2023-03-11 08:32:39'),(42,'chongwuyongpin_types','商品类型',3,'商品类型3',NULL,NULL,'2023-03-11 08:32:39'),(43,'chongwuyongpin_collection_types','收藏表类型',1,'收藏',NULL,NULL,'2023-03-11 08:32:39'),(44,'chongwuyongpin_order_types','订单类型',101,'已支付',NULL,NULL,'2023-03-11 08:32:39'),(45,'chongwuyongpin_order_types','订单类型',102,'已退款',NULL,NULL,'2023-03-11 08:32:39'),(46,'chongwuyongpin_order_types','订单类型',103,'已发货',NULL,NULL,'2023-03-11 08:32:39'),(47,'chongwuyongpin_order_types','订单类型',104,'已收货',NULL,NULL,'2023-03-11 08:32:39'),(48,'chongwuyongpin_order_types','订单类型',105,'已评价',NULL,NULL,'2023-03-11 08:32:39'),(49,'chongwuyongpin_order_payment_types','订单支付类型',1,'余额',NULL,NULL,'2023-03-11 08:32:39'),(50,'isdefault_types','是否默认地址',1,'否',NULL,NULL,'2023-03-11 08:32:39'),(51,'isdefault_types','是否默认地址',2,'是',NULL,NULL,'2023-03-11 08:32:39'),(52,'chongwujiyang_types','寄养服务类型',1,'寄养服务类型1',NULL,NULL,'2023-03-11 08:32:39'),(53,'chongwujiyang_types','寄养服务类型',2,'寄养服务类型2',NULL,NULL,'2023-03-11 08:32:39'),(54,'chongwujiyang_types','寄养服务类型',3,'寄养服务类型3',NULL,NULL,'2023-03-11 08:32:39'),(55,'chongwu_types','宠物类型',1,'宠物类型1',NULL,NULL,'2023-03-11 08:32:39'),(56,'chongwu_types','宠物类型',2,'宠物类型2',NULL,NULL,'2023-03-11 08:32:39'),(57,'chongwu_types','宠物类型',3,'宠物类型3',NULL,NULL,'2023-03-11 08:32:39'),(58,'shifou_types','是否接送',1,'是',NULL,NULL,'2023-03-11 08:32:39'),(59,'shifou_types','是否接送',2,'否',NULL,NULL,'2023-03-11 08:32:39'),(60,'chongwujiyang_yuyue_yesno_types','寄养审核',1,'未审核',NULL,NULL,'2023-03-11 08:32:39'),(61,'chongwujiyang_yuyue_yesno_types','寄养审核',2,'同意',NULL,NULL,'2023-03-11 08:32:39'),(62,'chongwujiyang_yuyue_yesno_types','寄养审核',3,'拒绝',NULL,NULL,'2023-03-11 08:32:39'),(63,'chat_types','数据类型',1,'问题',NULL,NULL,'2023-03-11 08:32:39'),(64,'chat_types','数据类型',2,'回复',NULL,NULL,'2023-03-11 08:32:39'),(65,'zhuangtai_types','状态',1,'未回复',NULL,NULL,'2023-03-11 08:32:39'),(66,'zhuangtai_types','状态',2,'已回复',NULL,NULL,'2023-03-11 08:32:39'),(67,'news_types','公告类型',1,'公告类型1',NULL,NULL,'2023-03-11 08:32:40'),(68,'news_types','公告类型',2,'公告类型2',NULL,NULL,'2023-03-11 08:32:40'),(69,'sex_types','性别类型',1,'男',NULL,NULL,'2023-03-11 08:32:40'),(70,'sex_types','性别类型',2,'女',NULL,NULL,'2023-03-11 08:32:40'),(71,'forum_state_types','帖子状态',1,'发帖',NULL,NULL,'2023-03-11 08:32:40'),(72,'forum_state_types','帖子状态',2,'回帖',NULL,NULL,'2023-03-11 08:32:40');
/*!40000 ALTER TABLE `dictionary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `forum`
--

DROP TABLE IF EXISTS `forum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `forum` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `forum_name` varchar(200) DEFAULT NULL COMMENT '帖子标题  Search111 ',
  `yonghu_id` int DEFAULT NULL COMMENT '用户',
  `users_id` int DEFAULT NULL COMMENT '管理员',
  `forum_content` text COMMENT '发布内容',
  `super_ids` int DEFAULT NULL COMMENT '父id',
  `forum_state_types` int DEFAULT NULL COMMENT '帖子状态',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '发帖时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间 show2',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb3 COMMENT='论坛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forum`
--

LOCK TABLES `forum` WRITE;
/*!40000 ALTER TABLE `forum` DISABLE KEYS */;
INSERT INTO `forum` (`id`, `forum_name`, `yonghu_id`, `users_id`, `forum_content`, `super_ids`, `forum_state_types`, `insert_time`, `update_time`, `create_time`) VALUES (2,'帖子标题2',2,NULL,'发布内容2',190,1,'2023-03-11 08:32:48','2023-03-11 08:32:48','2023-03-11 08:32:48'),(3,'帖子标题3',3,NULL,'发布内容3',178,1,'2023-03-11 08:32:48','2023-03-11 08:32:48','2023-03-11 08:32:48'),(4,'帖子标题4',2,NULL,'发布内容4',319,1,'2023-03-11 08:32:48','2023-03-11 08:32:48','2023-03-11 08:32:48'),(5,'帖子标题5',1,NULL,'发布内容5',490,1,'2023-03-11 08:32:48','2023-03-11 08:32:48','2023-03-11 08:32:48'),(6,'帖子标题6',1,NULL,'发布内容6',278,1,'2023-03-11 08:32:48','2023-03-11 08:32:48','2023-03-11 08:32:48'),(7,'帖子标题7',1,NULL,'发布内容7',83,1,'2023-03-11 08:32:48','2023-03-11 08:32:48','2023-03-11 08:32:48'),(8,'帖子标题8',3,NULL,'发布内容8',285,1,'2023-03-11 08:32:48','2023-03-11 08:32:48','2023-03-11 08:32:48'),(9,'帖子标题9',3,NULL,'发布内容9',182,1,'2023-03-11 08:32:48','2023-03-11 08:32:48','2023-03-11 08:32:48'),(10,'帖子标题10',3,NULL,'发布内容10',465,1,'2023-03-11 08:32:48','2023-03-11 08:32:48','2023-03-11 08:32:48'),(11,'帖子标题11',1,NULL,'发布内容11',424,1,'2023-03-11 08:32:48','2023-03-11 08:32:48','2023-03-11 08:32:48'),(12,'帖子标题12',1,NULL,'发布内容12',129,1,'2023-03-11 08:32:48','2023-03-11 08:32:48','2023-03-11 08:32:48'),(13,'帖子标题13',3,NULL,'发布内容13',412,1,'2023-03-11 08:32:48','2023-03-11 08:32:48','2023-03-11 08:32:48'),(14,'帖子标题14',1,NULL,'发布内容14',488,1,'2023-03-11 08:32:48','2023-03-11 08:32:48','2023-03-11 08:32:48'),(16,NULL,NULL,1,'123123',14,2,'2023-03-11 09:42:50',NULL,'2023-03-11 09:42:50'),(18,NULL,NULL,2,'123456789',14,2,'2026-04-07 06:49:41',NULL,'2026-04-07 06:49:41'),(20,NULL,2,NULL,'我的评论',14,2,'2026-04-09 08:17:14',NULL,'2026-04-09 08:17:14'),(21,NULL,1,NULL,'999',14,2,'2026-04-09 08:49:52',NULL,'2026-04-09 08:49:52'),(22,NULL,1,NULL,'12',14,2,'2026-04-09 08:51:43',NULL,'2026-04-09 08:51:43'),(23,NULL,1,NULL,'789',14,2,'2026-04-09 08:52:10',NULL,'2026-04-09 08:52:10'),(24,NULL,1,NULL,'666',14,2,'2026-04-09 08:57:24',NULL,'2026-04-09 08:57:24');
/*!40000 ALTER TABLE `forum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `news`
--

DROP TABLE IF EXISTS `news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `news` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `news_name` varchar(200) DEFAULT NULL COMMENT '公告标题  Search111 ',
  `news_types` int DEFAULT NULL COMMENT '公告类型  Search111 ',
  `news_photo` varchar(200) DEFAULT NULL COMMENT '公告图片',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '添加时间',
  `news_content` text COMMENT '公告详情',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间 show1 show2 nameShow',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3 COMMENT='公告信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `news`
--

LOCK TABLES `news` WRITE;
/*!40000 ALTER TABLE `news` DISABLE KEYS */;
INSERT INTO `news` (`id`, `news_name`, `news_types`, `news_photo`, `insert_time`, `news_content`, `create_time`) VALUES (1,'公告标题1',2,'upload/news1.jpg','2023-03-11 08:32:48','公告详情1','2023-03-11 08:32:48'),(2,'公告标题2',1,'upload/news2.jpg','2023-03-11 08:32:48','公告详情2','2023-03-11 08:32:48'),(3,'公告标题3',1,'upload/news3.jpg','2023-03-11 08:32:48','公告详情3','2023-03-11 08:32:48'),(4,'公告标题4',2,'upload/news4.jpg','2023-03-11 08:32:48','公告详情4','2023-03-11 08:32:48'),(5,'公告标题5',1,'upload/news5.jpg','2023-03-11 08:32:48','公告详情5','2023-03-11 08:32:48'),(6,'公告标题6',1,'upload/news6.jpg','2023-03-11 08:32:48','公告详情6','2023-03-11 08:32:48'),(7,'公告标题7',2,'upload/news7.jpg','2023-03-11 08:32:48','公告详情7','2023-03-11 08:32:48'),(8,'公告标题8',2,'upload/news8.jpg','2023-03-11 08:32:48','公告详情8','2023-03-11 08:32:48'),(9,'公告标题9',2,'upload/news9.jpg','2023-03-11 08:32:48','公告详情9','2023-03-11 08:32:48'),(10,'公告标题10',2,'upload/news10.jpg','2023-03-11 08:32:48','公告详情10','2023-03-11 08:32:48'),(11,'公告标题11',2,'upload/news11.jpg','2023-03-11 08:32:48','公告详情11','2023-03-11 08:32:48'),(12,'公告标题12',2,'upload/news12.jpg','2023-03-11 08:32:48','公告详情12','2023-03-11 08:32:48'),(13,'公告标题13',1,'upload/news13.jpg','2023-03-11 08:32:48','公告详情13','2023-03-11 08:32:48'),(14,'公告标题14',1,'upload/news14.jpg','2023-03-11 08:32:48','公告详情14','2023-03-11 08:32:48');
/*!40000 ALTER TABLE `news` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pet_category`
--

DROP TABLE IF EXISTS `pet_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pet_category` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '分类名称',
  `parent_id` int DEFAULT NULL COMMENT '父分类ID，0表示顶级分类',
  `level` int NOT NULL DEFAULT '1' COMMENT '分类层级',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='宠物分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pet_category`
--

LOCK TABLES `pet_category` WRITE;
/*!40000 ALTER TABLE `pet_category` DISABLE KEYS */;
INSERT INTO `pet_category` (`id`, `name`, `parent_id`, `level`, `sort`, `create_time`) VALUES (1,'狗',0,1,1,'2026-04-13 02:16:07'),(2,'猫',0,1,2,'2026-04-13 02:16:07'),(4,'拉布拉多',1,2,1,'2026-04-13 02:16:07'),(5,'金毛',1,2,2,'2026-04-13 02:16:07'),(6,'泰迪',1,2,3,'2026-04-13 02:16:07'),(7,'英短',2,2,1,'2026-04-13 02:16:07'),(8,'美短',2,2,2,'2026-04-13 02:16:07'),(9,'布偶',2,2,3,'2026-04-13 02:16:07'),(11,'爬宠',0,1,0,'2026-04-13 03:41:51');
/*!40000 ALTER TABLE `pet_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pet_order`
--

DROP TABLE IF EXISTS `pet_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pet_order` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_uuid` varchar(100) NOT NULL COMMENT '订单号',
  `user_id` int NOT NULL COMMENT '用户ID',
  `pet_id` int NOT NULL COMMENT '宠物ID',
  `address_id` int NOT NULL COMMENT '收货地址ID',
  `price` decimal(10,2) NOT NULL COMMENT '订单价格',
  `order_type` int NOT NULL DEFAULT '1' COMMENT '订单类型',
  `payment_type` int DEFAULT NULL COMMENT '支付类型',
  `order_status` int NOT NULL DEFAULT '1' COMMENT '订单状态：1=待付款，2=待发货，3=已发货，4=已完成，5=已取消',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `pet_name` varchar(100) DEFAULT NULL COMMENT '宠物名称快照',
  `pet_image_url` varchar(255) DEFAULT NULL COMMENT '宠物图片URL快照',
  `pet_gender` varchar(10) DEFAULT NULL COMMENT '宠物性别快照',
  `pet_age` varchar(20) DEFAULT NULL COMMENT '宠物年龄快照',
  `pet_description` text COMMENT '宠物描述快照',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `pet_id` (`pet_id`),
  KEY `order_status` (`order_status`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='宠物订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pet_order`
--

LOCK TABLES `pet_order` WRITE;
/*!40000 ALTER TABLE `pet_order` DISABLE KEYS */;
INSERT INTO `pet_order` (`id`, `order_uuid`, `user_id`, `pet_id`, `address_id`, `price`, `order_type`, `payment_type`, `order_status`, `create_time`, `update_time`, `pet_name`, `pet_image_url`, `pet_gender`, `pet_age`, `pet_description`) VALUES (1,'1776082632203',1,2,17,2000.00,1,1,2,'2026-04-13 12:17:12','2026-04-13 12:58:13',NULL,NULL,NULL,NULL,NULL),(2,'1776085034443',1,3,17,1800.00,1,2,2,'2026-04-13 12:57:14','2026-04-13 12:58:11','咪咪','upload/1776060712277.webp','母','1个月','粘人可爱，已驱虫'),(3,'1776088380952',1,1,17,1500.00,1,1,5,'2026-04-13 13:53:01',NULL,'小黑','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cute%20black%20labrador%20puppy&image_size=square_hd','公','2个月','活泼可爱，已接种疫苗'),(4,'1776088953676',1,1,17,1500.00,1,1,5,'2026-04-13 14:02:34',NULL,'小黑','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cute%20black%20labrador%20puppy&image_size=square_hd','公','2个月','活泼可爱，已接种疫苗'),(5,'1776089015468',1,1,17,1500.00,1,1,5,'2026-04-13 14:03:35',NULL,'小黑','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cute%20black%20labrador%20puppy&image_size=square_hd','公','2个月','活泼可爱，已接种疫苗'),(6,'1776089068765',1,1,17,1500.00,1,1,5,'2026-04-13 14:04:29',NULL,'小黑','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cute%20black%20labrador%20puppy&image_size=square_hd','公','2个月','活泼可爱，已接种疫苗'),(7,'1776089299772',1,1,17,1500.00,1,1,5,'2026-04-13 14:08:20',NULL,'小黑','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cute%20black%20labrador%20puppy&image_size=square_hd','公','2个月','活泼可爱，已接种疫苗');
/*!40000 ALTER TABLE `pet_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pets`
--

DROP TABLE IF EXISTS `pets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pets` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '宠物名称',
  `category_id` int NOT NULL COMMENT '分类ID',
  `gender` varchar(10) NOT NULL COMMENT '性别',
  `age` varchar(20) NOT NULL COMMENT '年龄',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `description` text COMMENT '描述',
  `image_url` varchar(255) DEFAULT NULL COMMENT '图片URL',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态：1=上架，0=下架',
  `click_num` int NOT NULL DEFAULT '0' COMMENT '点击数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  KEY `status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='宠物表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pets`
--

LOCK TABLES `pets` WRITE;
/*!40000 ALTER TABLE `pets` DISABLE KEYS */;
INSERT INTO `pets` (`id`, `name`, `category_id`, `gender`, `age`, `price`, `description`, `image_url`, `status`, `click_num`, `create_time`) VALUES (1,'小黑',4,'公','2个月',1500.00,'活泼可爱，已接种疫苗','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cute%20black%20labrador%20puppy&image_size=square_hd',1,11,'2026-04-13 02:16:07'),(2,'小白',5,'母','3个月',2000.00,'温顺乖巧，已接种疫苗','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cute%20golden%20retriever%20puppy&image_size=square_hd',0,4,'2026-04-13 02:16:07'),(3,'咪咪',6,'母','1个月',1800.00,'粘人可爱，已驱虫','upload/1776060712277.webp',0,4,'2026-04-13 02:16:07');
/*!40000 ALTER TABLE `pets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `token` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint NOT NULL COMMENT '用户id',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `tablename` varchar(100) DEFAULT NULL COMMENT '表名',
  `role` varchar(100) DEFAULT NULL COMMENT '角色',
  `token` varchar(200) NOT NULL COMMENT '密码',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `expiratedtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COMMENT='token表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `token`
--

LOCK TABLES `token` WRITE;
/*!40000 ALTER TABLE `token` DISABLE KEYS */;
INSERT INTO `token` (`id`, `userid`, `username`, `tablename`, `role`, `token`, `addtime`, `expiratedtime`) VALUES (1,1,'admin','users','管理员','ioe9qtyl59042a6ep5yy6bcxu2u82zb2','2023-03-11 08:52:04','2026-04-13 06:59:10'),(2,1,'a1','yonghu','用户','31e149w70a5bx8tg7vo0k0azkxo6rcq9','2023-03-11 08:53:53','2026-04-16 12:17:00'),(3,2,'admin1','users','管理员','78yi79j7skm497eypzzqtfq5qfa6hxsa','2026-03-28 09:19:32','2026-04-12 09:12:13'),(4,2,'a2','yonghu','用户','ttup27ki8i7su9lbc6egch0h4dcex4cf','2026-04-07 12:39:52','2026-04-12 06:47:47');
/*!40000 ALTER TABLE `token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `role` varchar(100) DEFAULT '管理员' COMMENT '角色',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COMMENT='管理员';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `username`, `password`, `role`, `addtime`) VALUES (1,'admin','0000000000000000000000000586034f','管理员','2022-04-30 16:00:00'),(2,'admin1','admin','管理员','2026-03-28 09:19:18');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `yonghu`
--

DROP TABLE IF EXISTS `yonghu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `yonghu` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(200) DEFAULT NULL COMMENT '账户',
  `password` varchar(200) DEFAULT NULL COMMENT '密码',
  `yonghu_name` varchar(200) DEFAULT NULL COMMENT '用户姓名 Search111 ',
  `yonghu_phone` varchar(200) DEFAULT NULL COMMENT '联系方式',
  `yonghu_id_number` varchar(200) DEFAULT NULL COMMENT '身份证号',
  `yonghu_photo` varchar(200) DEFAULT NULL COMMENT '用户头像',
  `sex_types` int DEFAULT NULL COMMENT '性别 Search111',
  `yonghu_email` varchar(200) DEFAULT NULL COMMENT '电子邮箱',
  `new_money` decimal(10,2) DEFAULT NULL COMMENT '余额 ',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COMMENT='用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `yonghu`
--

LOCK TABLES `yonghu` WRITE;
/*!40000 ALTER TABLE `yonghu` DISABLE KEYS */;
INSERT INTO `yonghu` (`id`, `username`, `password`, `yonghu_name`, `yonghu_phone`, `yonghu_id_number`, `yonghu_photo`, `sex_types`, `yonghu_email`, `new_money`, `create_time`) VALUES (1,'hejiale','0000000000000000000000002fe8791e','何家乐','19189079999','410224199010102001','upload/1775726222888.jpg',1,'hejiale325@qq.com',72423.30,'2023-03-11 08:32:48'),(2,'a2','00000000000000000000000056760663','用户姓名2','17703786902','410224199010102002','upload/yonghu2.jpg',2,'2@qq.com',18628.20,'2023-03-11 08:32:48'),(3,'a3','00000000000000000000000056760663','用户姓名3','17703786903','410224199010102003','upload/1775542316999.webp',2,'3@qq.com',36.21,'2023-03-11 08:32:48');
/*!40000 ALTER TABLE `yonghu` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-16 19:42:28
