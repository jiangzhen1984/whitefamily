-- MySQL dump 10.13  Distrib 5.5.46, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: wf
-- ------------------------------------------------------
-- Server version	5.5.46-0ubuntu0.14.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `wf_goods`
--

DROP TABLE IF EXISTS `wf_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wf_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `WF_CTE_DESC` text,
  `WF_IMAGE_URI` varchar(500) DEFAULT NULL,
  `WF_GOODS_NAME` varchar(500) DEFAULT NULL,
  `WF_CTE_TYPE` tinyint(4) DEFAULT '0',
  `WF_GOODS_UNIT` varchar(100) DEFAULT NULL,
  `WF_GOOD_CATE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_8yw9mj04p0knu4vxgib2ax9b4` (`WF_GOOD_CATE_ID`),
  CONSTRAINT `FK_8yw9mj04p0knu4vxgib2ax9b4` FOREIGN KEY (`WF_GOOD_CATE_ID`) REFERENCES `wf_category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=170 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_goods`
--

LOCK TABLES `wf_goods` WRITE;
/*!40000 ALTER TABLE `wf_goods` DISABLE KEYS */;
INSERT INTO `wf_goods` VALUES (1,'<p><br></p>',NULL,'牛筋面',0,'',6),(2,'<p><br></p>',NULL,'玉米面',0,'',6),(3,'<p><br></p>',NULL,'宽面',0,'',6),(4,'<p><br></p>',NULL,'方便面',0,'',6),(5,'<p><br></p>',NULL,'冷面',0,'',7),(6,'<p><br></p>',NULL,'宽粉',0,'',7),(7,'<p><br></p>',NULL,'细粉',0,'',7),(8,'<p><br></p>',NULL,'土豆粉',0,'',7),(9,'<p><br></p>',NULL,'粉丝',0,'',7),(10,'<p><br></p>',NULL,'地瓜粉',0,'',7),(11,'<p><br></p>',NULL,'厥根粉',0,'',7),(12,'<p><br></p>',NULL,'肥羊粉',0,'',7),(13,'<p><br></p>',NULL,'丝结',0,'',8),(14,'<p><br></p>',NULL,'素肚',0,'',8),(15,'<p><br></p>',NULL,'海带',0,'',9),(16,'<p><br></p>',NULL,'木耳',0,'',9),(17,'<p><br></p>',NULL,'藕片',0,'',9),(18,'<p><br></p>',NULL,'草丝',0,'',9),(19,'<p><br></p>',NULL,'银耳',0,'',9),(20,'<p><br></p>',NULL,'腐竹',0,'',10),(21,'<p><br></p>',NULL,'油豆皮',0,'',10),(22,'<p><br></p>',NULL,'干豆皮',0,'',10),(23,'<p><br></p>',NULL,'烤麸',0,'',10),(24,'<p><br></p>',NULL,'油豆泡',0,'',10),(25,'<p><br></p>',NULL,'豆腐串',0,'',10),(26,'<p><br></p>',NULL,'五香豆卷',0,'',10),(27,'<p><br></p>',NULL,'肥牛片',0,'',11),(28,'<p><br></p>',NULL,'羊肉片',0,'',11),(29,'<p><br></p>',NULL,'黄喉',0,'',11),(30,'<p><br></p>',NULL,'牛百叶',0,'',11),(31,'<p><br></p>',NULL,'牛肚',0,'',11),(32,'<p><br></p>',NULL,'鸡肉',0,'',11),(33,'<p><br></p>',NULL,'素丸子',0,'',12),(34,'<p><br></p>',NULL,'午餐肉',0,'',12),(35,'<p><br></p>',NULL,'培根',0,'',12),(36,'<p><br></p>',NULL,'鸭胸',0,'',12),(37,'<p><br></p>',NULL,'脆皮肠',0,'',12),(38,'<p><br></p>',NULL,'蒜肠',0,'',12),(39,'<p><br></p>',NULL,'腊肠',0,'',12),(40,'<p><br></p>',NULL,'红肠',0,'',12),(41,'<p><br></p>',NULL,'猪脆骨',0,'',12),(42,'<p><br></p>',NULL,'鹌鹑蛋',0,'',13),(43,'<p><br></p>',NULL,'鸭血',0,'',13),(44,'<p><br></p>',NULL,'玉米',0,'',13),(45,'<p><br></p>',NULL,'年糕',0,'',13),(46,'<p><br></p>',NULL,'鸡蛋',0,'',13),(47,'<p><br></p>',NULL,'海参',0,'',14),(48,'<p><br></p>',NULL,'鲍鱼',0,'',14),(49,'<p><br></p>',NULL,'海虾',0,'',14),(50,'<p><br></p>',NULL,'鱿鱼',0,'',14),(51,'<p><br></p>',NULL,'叶生菜',0,'',15),(52,'<p><br></p>',NULL,'圆生菜',0,'',15),(53,'<p><br></p>',NULL,'娃娃菜',0,'',15),(54,'<p><br></p>',NULL,'洋白菜',0,'',15),(55,'<p><br></p>',NULL,'地瓜',0,'',15),(56,'<p><br></p>',NULL,'白菜',0,'',15),(57,'<p><br></p>',NULL,'香菜',0,'',15),(58,'<p><br></p>',NULL,'油麦菜',0,'',15),(59,'<p><br></p>',NULL,'西兰花',0,'',15),(60,'<p><br></p>',NULL,'土豆',0,'',15),(61,'<p><br></p>',NULL,'南瓜',0,'',15),(62,'<p><br></p>',NULL,'菠菜',0,'',15),(63,'<p><br></p>',NULL,'油菜',0,'',15),(64,'<p><br></p>',NULL,'豆芽',0,'',15),(65,'<p><br></p>',NULL,'蒿子杆',0,'',15),(66,'<p><br></p>',NULL,'菜心',0,'',15),(67,'<p><br></p>',NULL,'青笋',0,'',15),(68,'<p><br></p>',NULL,'紫甘蓝',0,'',15),(69,'<p><br></p>',NULL,'苦苣',0,'',15),(70,'<p><br></p>',NULL,'山药',0,'',15),(71,'<p><br></p>',NULL,'香菜',0,'',15),(72,'<p><br></p>',NULL,'香葱',0,'',15),(73,'<p><br></p>',NULL,'小米辣',0,'',15),(74,'<p><br></p>',NULL,'蒜',0,'',15),(75,'<p><br></p>',NULL,'冬瓜',0,'',15),(76,'<p><br></p>',NULL,'金针菇',0,'',16),(77,'<p><br></p>',NULL,'平菇',0,'',16),(78,'<p><br></p>',NULL,'杏鲍菇',0,'',16),(79,'<p><br></p>',NULL,'白玉菇',0,'',16),(80,'<p><br></p>',NULL,'蟹味菇',0,'',16),(81,'<p><br></p>',NULL,'章鱼丸',0,'',17),(82,'<p><br></p>',NULL,'墨鱼丸',0,'',17),(83,'<p><br></p>',NULL,'牛肉丸',0,'',17),(84,'<p><br></p>',NULL,'撒尿牛丸',0,'',17),(85,'<p><br></p>',NULL,'鸡肉丸',0,'',17),(86,'<p><br></p>',NULL,'香菜丸',0,'',17),(87,'<p><br></p>',NULL,'木耳丸',0,'',17),(88,'<p><br></p>',NULL,'鱼豆腐',0,'',17),(89,'<p><br></p>',NULL,'千叶豆腐',0,'',17),(90,'<p><br></p>',NULL,'鲍鱼片',0,'',17),(91,'<p><br></p>',NULL,'鱼丸',0,'',17),(92,'<p><br></p>',NULL,'包心鱼丸',0,'',17),(93,'<p><br></p>',NULL,'开花肠',0,'',17),(94,'<p><br></p>',NULL,'腰花肠',0,'',17),(95,'<p><br></p>',NULL,'双色腰花肠',0,'',17),(96,'<p><br></p>',NULL,'烧鱼板',0,'',17),(97,'<p><br></p>',NULL,'蟹排',0,'',17),(98,'<p><br></p>',NULL,'蟹棒',0,'',17),(99,'<p><br></p>',NULL,'蟹王棒',0,'',17),(100,'<p><br></p>',NULL,'龙虾丸',0,'',17),(101,'<p><br></p>',NULL,'虾米饺',0,'',17),(102,'<p><br></p>',NULL,'鱼皮脆',0,'',17),(103,'<p><br></p>',NULL,'甜不辣',0,'',17),(104,'<p><br></p>',NULL,'香菇贡丸',0,'',17),(105,'<p><br></p>',NULL,'亲亲肠',0,'',17),(106,'<p><br></p>',NULL,'西湖燕饺',0,'',17),(107,'<p><br></p>',NULL,'彩虹卷',0,'',17),(108,'<p><br></p>',NULL,'淡水丸',0,'',17),(109,'<p><br></p>',NULL,'松竹卷',0,'',17),(110,'<p><br></p>',NULL,'鱼排',0,'',17),(111,'<p><br></p>',NULL,'香辣鱼果',0,'',17),(112,'<p><br></p>',NULL,'金陵鱼脆',0,'',17),(113,'<p><br></p>',NULL,'烤肠',0,'',17),(114,'<p><br></p>',NULL,'韭菜花',1,'',18),(115,'<p><br></p>',NULL,'豆腐乳',1,'',18),(116,'<p><br></p>',NULL,'蚝油',0,'',18),(117,'<p><br></p>',NULL,'花椒油',0,'',18),(118,'<p><br></p>',NULL,'一品鲜',0,'',18),(119,'<p><br></p>',NULL,'盐',0,'',18),(120,'<p><br></p>',NULL,'鸡精',0,'',18),(121,'<p><br></p>',NULL,'陈醋',0,'',18),(122,'<p><br></p>',NULL,'干拌香粉',0,'',19),(123,'<p><br></p>',NULL,'麻椒粉',0,'',19),(124,'<p><br></p>',NULL,'奇子香',1,'',19),(125,'<p><br></p>',NULL,'咖喱粉',1,'',19),(126,'<p><br></p>',NULL,'白胡椒',1,'',19),(127,'<p><br></p>',NULL,'姜粉',1,'',19),(128,'<p><br></p>',NULL,'淀粉',1,'',19),(129,'<p><br></p>',NULL,'味素',0,'',19),(130,'<p><br></p>',NULL,'嫩肉粉',1,'',19),(131,'<p><br></p>',NULL,'五香粉',1,'',19),(132,'<p><br></p>',NULL,'花生酱',0,'',20),(133,'<p><br></p>',NULL,'豆豉',1,'',20),(134,'<p><br></p>',NULL,'豆瓣酱',1,'',20),(135,'<p><br></p>',NULL,'咖喱酱',1,'',20),(136,'<p><br></p>',NULL,'番茄酱',0,'',20),(137,'<p><br></p>',NULL,'糖醋酱',0,'',20),(138,'<p><br></p>',NULL,'椒麻酱',1,'',20),(139,'<p><br></p>',NULL,'番茄沙司',1,'',20),(140,'<p><br></p>',NULL,'豆油',0,'',21),(141,'<p><br></p>',NULL,'香油',0,'',21),(142,'<p><br></p>',NULL,'一滴香',1,'',21),(143,'<p><br></p>',NULL,'火锅红',1,'',21),(144,'<p><br></p>',NULL,'鸡油',1,'',21),(145,'<p><br></p>',NULL,'羊油',1,'',21),(146,'<p><br></p>',NULL,'腰窝油',0,'',21),(147,'<p><br></p>',NULL,'牛骨',0,'',21),(148,'<p><br></p>',NULL,'花生碎',0,'',22),(149,'<p><br></p>',NULL,'麻椒',1,'',22),(150,'<p><br></p>',NULL,'青花椒',1,'',22),(151,'<p><br></p>',NULL,'小米辣泡椒',1,'',22),(152,'<p><br></p>',NULL,'A级二细',1,'',22),(153,'<p><br></p>',NULL,'A级三细',1,'',22),(154,'<p><br></p>',NULL,'B级二细',1,'',22),(155,'<p><br></p>',NULL,'小红粒',1,'',22),(156,'<p><br></p>',NULL,'芝麻',1,'',22),(157,'<p><br></p>',NULL,'洋葱',1,'',22),(158,'<p><br></p>',NULL,'鲜姜',1,'',22),(159,'<p><br></p>',NULL,'冰糖',1,'',22),(160,'<p><br></p>',NULL,'药料',1,'',22),(161,'<p><br></p>',NULL,'白酒',1,'',22),(162,'<p><br></p>',NULL,'绵糖',0,'',22),(163,'<p><br></p>',NULL,'腌肉粉',1,'',22),(164,'<p><br></p>',NULL,'红油底料',0,'',23),(165,'<p><br></p>',NULL,'番茄底料',0,'',23),(166,'<p><br></p>',NULL,'番茄底料',0,'',23),(167,'<p><br></p>',NULL,'咖喱底料',0,'',23),(168,'<p><br></p>',NULL,'底料伴侣',0,'',23),(169,'<p><br></p>',NULL,'泡椒',0,'',23);
/*!40000 ALTER TABLE `wf_goods` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-09-17 13:58:46
