-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: todo
-- ------------------------------------------------------
-- Server version	5.1.32-community

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
-- Current Database: `todo`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `todo` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `todo`;

--
-- Table structure for table `encuestas`
--

DROP TABLE IF EXISTS `encuestas`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `encuestas` (
  `nombre` varchar(255) DEFAULT NULL,
  `telefono` int(11) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `intra` varchar(255) DEFAULT NULL,
  `intranet` varchar(255) DEFAULT NULL,
  `acceso` varchar(255) DEFAULT NULL,
  `imagen` varchar(1000) DEFAULT NULL,
  `mapa` varchar(1000) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `encuestas`
--

LOCK TABLES `encuestas` WRITE;
/*!40000 ALTER TABLE `encuestas` DISABLE KEYS */;
/*!40000 ALTER TABLE `encuestas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `todo`
--

DROP TABLE IF EXISTS `todo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `todo` (
  `todoid` int(11) NOT NULL AUTO_INCREMENT,
  `todo` varchar(255) NOT NULL,
  PRIMARY KEY (`todoid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `todo`
--

LOCK TABLES `todo` WRITE;
/*!40000 ALTER TABLE `todo` DISABLE KEYS */;
/*!40000 ALTER TABLE `todo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2009-04-15 12:05:23
