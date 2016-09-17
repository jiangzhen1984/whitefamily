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
-- Table structure for table `wf_category`
--

DROP TABLE IF EXISTS `wf_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wf_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `WF_CTE_LEVEL` smallint(6) DEFAULT NULL,
  `WF_CTE_NAME` varchar(500) DEFAULT NULL,
  `WF_CTE_PARENT_ID` bigint(20) DEFAULT NULL,
  `WF_CTE_TYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_category`
--

LOCK TABLES `wf_category` WRITE;
/*!40000 ALTER TABLE `wf_category` DISABLE KEYS */;
INSERT INTO `wf_category` VALUES (1,0,'水发类',0,0),(2,0,'副食类',0,0),(3,0,'青菜-菌类',0,0),(4,0,'丸子大类',0,0),(5,0,'调料大类',0,0),(6,1,'面类',1,0),(7,1,'粉类',1,0),(8,1,'合成类',1,0),(9,1,'根茎类',1,0),(10,1,'豆制品',1,0),(11,1,'肉类',2,0),(12,1,'熟食类',2,0),(13,1,'半成品',2,0),(14,1,'海鲜类',2,0),(15,1,'青菜类',3,0),(16,1,'菌类',3,0),(17,1,'丸子类',4,0),(18,1,'调味品',5,0),(19,1,'调味粉',5,0),(20,1,'调酱类',5,0),(21,1,'油品类',5,0),(22,1,'调料类',5,0),(23,1,'酱料类',5,0);
/*!40000 ALTER TABLE `wf_category` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-09-17 13:58:33
