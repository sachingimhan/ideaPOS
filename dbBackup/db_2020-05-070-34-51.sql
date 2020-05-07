-- MySQL dump 10.17  Distrib 10.3.22-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: ideaPOS
-- ------------------------------------------------------
-- Server version	10.3.22-MariaDB-1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `ideaPOS`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `ideaPOS` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `ideaPOS`;

--
-- Table structure for table `Customer`
--

DROP TABLE IF EXISTS `Customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Customer` (
  `custID` varchar(20) NOT NULL,
  `custName` varchar(200) NOT NULL,
  `custAddress` varchar(200) NOT NULL,
  `custContact` varchar(12) DEFAULT NULL,
  `isLocked` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`custID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Customer`
--

LOCK TABLES `Customer` WRITE;
/*!40000 ALTER TABLE `Customer` DISABLE KEYS */;
INSERT INTO `Customer` VALUES ('CUST-0001','Cash Customer','Idea POS System, Delgoda','0715131363',1);
/*!40000 ALTER TABLE `Customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CustomerReturn`
--

DROP TABLE IF EXISTS `CustomerReturn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CustomerReturn` (
  `retID` varchar(200) NOT NULL,
  `orderID` varchar(20) NOT NULL,
  `userID` varchar(5) NOT NULL,
  `retDate` date NOT NULL,
  `billDate` date NOT NULL,
  `totalAmount` decimal(10,2) NOT NULL,
  PRIMARY KEY (`retID`),
  KEY `orderID` (`orderID`),
  CONSTRAINT `CustomerReturn_ibfk_1` FOREIGN KEY (`orderID`) REFERENCES `Order` (`orderID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CustomerReturn`
--

LOCK TABLES `CustomerReturn` WRITE;
/*!40000 ALTER TABLE `CustomerReturn` DISABLE KEYS */;
INSERT INTO `CustomerReturn` VALUES ('RET-0001','INV-0001','U-001','2020-05-02','2020-05-02',200.00),('RET-0002','INV-0006','U-001','2020-05-03','2020-05-03',300.00),('RET-0003','INV-0008','U-001','2020-04-30','2020-04-30',150.00),('RET-0004','inv-0010','U-001','2020-05-02','2020-04-30',300.00);
/*!40000 ALTER TABLE `CustomerReturn` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DiscountItem`
--

DROP TABLE IF EXISTS `DiscountItem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DiscountItem` (
  `disCode` int(11) NOT NULL AUTO_INCREMENT,
  `itemCode` varchar(20) NOT NULL,
  `costPrice` decimal(10,2) NOT NULL,
  `discount` varchar(10) DEFAULT NULL,
  `discountAmount` decimal(10,2) NOT NULL,
  `disDate` date NOT NULL,
  PRIMARY KEY (`disCode`),
  KEY `itemCode` (`itemCode`),
  CONSTRAINT `DiscountItem_ibfk_1` FOREIGN KEY (`itemCode`) REFERENCES `Item` (`itemCode`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DiscountItem`
--

LOCK TABLES `DiscountItem` WRITE;
/*!40000 ALTER TABLE `DiscountItem` DISABLE KEYS */;
/*!40000 ALTER TABLE `DiscountItem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GRN`
--

DROP TABLE IF EXISTS `GRN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GRN` (
  `grnID` varchar(50) NOT NULL,
  `itemCode` varchar(20) NOT NULL,
  `supplierID` varchar(20) NOT NULL,
  `userID` varchar(5) NOT NULL,
  `batchNo` varchar(50) NOT NULL,
  `grnDate` date NOT NULL,
  `qty` int(11) NOT NULL,
  `unitPrice` decimal(10,2) NOT NULL,
  `totalCost` decimal(10,2) NOT NULL,
  `mdf` date DEFAULT NULL,
  `expiryDate` date DEFAULT NULL,
  `isConformed` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`grnID`,`batchNo`),
  KEY `itemCode` (`itemCode`),
  KEY `supplierID` (`supplierID`),
  KEY `userID` (`userID`),
  CONSTRAINT `GRN_ibfk_1` FOREIGN KEY (`itemCode`) REFERENCES `Item` (`itemCode`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `GRN_ibfk_2` FOREIGN KEY (`supplierID`) REFERENCES `Supplier` (`supplierID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `GRN_ibfk_3` FOREIGN KEY (`userID`) REFERENCES `User` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GRN`
--

LOCK TABLES `GRN` WRITE;
/*!40000 ALTER TABLE `GRN` DISABLE KEYS */;
INSERT INTO `GRN` VALUES ('GRN-1683707808','1691708186','SUP-0001','U-001','B-1202116641','2020-05-02',50,90.00,4500.00,'2020-05-02','2021-05-02',1);
/*!40000 ALTER TABLE `GRN` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Item`
--

DROP TABLE IF EXISTS `Item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Item` (
  `itemCode` varchar(20) NOT NULL,
  `itemName` varchar(200) NOT NULL,
  `costPrice` decimal(10,2) NOT NULL,
  `retailPrice` decimal(10,2) NOT NULL,
  `wholeSalePrice` decimal(10,2) DEFAULT 0.00,
  `itemQty` decimal(10,2) DEFAULT 0.00,
  `discount` decimal(10,2) DEFAULT 0.00,
  `reorderLevel` int(11) DEFAULT 0,
  PRIMARY KEY (`itemCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Item`
--

LOCK TABLES `Item` WRITE;
/*!40000 ALTER TABLE `Item` DISABLE KEYS */;
INSERT INTO `Item` VALUES ('1691708186','Kiri Samba (1KG)',90.00,100.00,0.00,32.50,0.00,10),('1745213998','Parippu (1Kg)',120.00,150.00,0.00,23.00,0.00,10),('1803849895','Rayigam Soya Meate (250g)',50.00,55.00,0.00,4.00,0.00,10);
/*!40000 ALTER TABLE `Item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LoginDetail`
--

DROP TABLE IF EXISTS `LoginDetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LoginDetail` (
  `userID` varchar(5) NOT NULL,
  `userName` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(20) NOT NULL,
  `activeState` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `userName` (`userName`),
  CONSTRAINT `LoginDetail_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `User` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LoginDetail`
--

LOCK TABLES `LoginDetail` WRITE;
/*!40000 ALTER TABLE `LoginDetail` DISABLE KEYS */;
INSERT INTO `LoginDetail` VALUES ('U-001','admin','21232f297a57a5a743894a0e4a801fc3','Administrator',1),('U-002','kamal','aa63b0d5d950361c05012235ab520512','Cashier',1);
/*!40000 ALTER TABLE `LoginDetail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Order`
--

DROP TABLE IF EXISTS `Order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Order` (
  `orderID` varchar(20) NOT NULL,
  `custID` varchar(20) NOT NULL,
  `userID` varchar(5) NOT NULL,
  `orderDate` date NOT NULL,
  `paymentMethod` varchar(100) NOT NULL,
  `grossAmount` decimal(10,2) NOT NULL,
  `netAmount` decimal(10,2) NOT NULL,
  `cash` decimal(10,2) NOT NULL,
  `balance` decimal(10,2) NOT NULL,
  PRIMARY KEY (`orderID`),
  KEY `custID` (`custID`),
  KEY `userID` (`userID`),
  CONSTRAINT `Order_ibfk_1` FOREIGN KEY (`custID`) REFERENCES `Customer` (`custID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Order_ibfk_2` FOREIGN KEY (`userID`) REFERENCES `User` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Order`
--

LOCK TABLES `Order` WRITE;
/*!40000 ALTER TABLE `Order` DISABLE KEYS */;
INSERT INTO `Order` VALUES ('INV-0001','CUST-0001','U-001','2020-05-02','Cash Pay',705.00,705.00,750.00,45.00),('INV-0002','CUST-0001','U-001','2020-05-02','Cash Pay',305.00,305.00,320.00,15.00),('INV-0003','CUST-0001','U-001','2020-05-02','Cash Pay',100.00,100.00,100.00,0.00),('INV-0004','CUST-0001','U-001','2020-05-02','Cash Pay',330.00,330.00,330.00,0.00),('INV-0005','CUST-0001','U-001','2020-05-03','Cash Pay',360.00,360.00,360.00,0.00),('INV-0006','CUST-0001','U-001','2020-05-03','Cash Pay',1000.00,1000.00,1000.00,0.00),('INV-0007','CUST-0001','U-001','2020-05-03','Cash Pay',400.00,400.00,500.00,100.00),('INV-0008','CUST-0001','U-001','2020-04-30','Cash Pay',525.00,525.00,550.00,25.00),('INV-0009','CUST-0001','U-001','2020-04-30','Cash Pay',410.00,410.00,500.00,90.00),('INV-0010','CUST-0001','U-001','2020-04-30','Cash Pay',355.00,355.00,355.00,0.00),('INV-0011','CUST-0001','U-001','2021-05-02','Cash Pay',200.00,200.00,200.00,0.00),('INV-0012','CUST-0001','U-001','2020-05-02','Cash Pay',155.00,155.00,200.00,45.00),('INV-0013','CUST-0001','U-001','2020-05-03','Cash Pay',100.00,100.00,100.00,0.00),('INV-0014','CUST-0001','U-001','2020-05-03','Cash Pay',2000.00,2000.00,2000.00,0.00),('INV-0015','CUST-0001','U-001','2020-05-04','Cash Pay',1525.00,1525.00,1600.00,75.00),('INV-0016','CUST-0001','U-002','2019-12-15','Cash Pay',275.00,275.00,300.00,25.00),('INV-0017','CUST-0001','U-001','2020-05-05','Cash Pay',450.00,405.00,500.00,95.00),('INV-0018','CUST-0001','U-001','2019-12-15','Cash Pay',305.00,305.00,320.00,15.00),('INV-0019','CUST-0001','U-001','2019-12-15','Cash Pay',705.00,705.00,800.00,95.00),('INV-0020','CUST-0001','U-001','2019-12-15','Cash Pay',150.00,150.00,200.00,50.00),('INV-0021','CUST-0001','U-001','2020-05-05','Cash Pay',250.00,250.00,300.00,50.00),('INV-0022','CUST-0001','U-001','2020-05-05','Cash Pay',250.00,250.00,250.00,0.00),('INV-0023','CUST-0001','U-001','2020-05-05','Cash Pay',150.00,150.00,150.00,0.00),('INV-0024','CUST-0001','U-001','2020-05-05','Cash Pay',55.00,55.00,100.00,45.00),('INV-0025','CUST-0001','U-001','2020-05-07','Cash Pay',360.00,360.00,400.00,40.00),('INV-0026','CUST-0001','U-001','2020-05-07','Cash Pay',750.00,750.00,800.00,50.00),('INV-0027','CUST-0001','U-001','2020-05-07','Cash Pay',100.00,100.00,100.00,0.00),('INV-0028','CUST-0001','U-001','2020-06-07','Cash Pay',910.00,910.00,100.00,-810.00);
/*!40000 ALTER TABLE `Order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OrderItem`
--

DROP TABLE IF EXISTS `OrderItem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OrderItem` (
  `orderID` varchar(20) NOT NULL,
  `itemCode` varchar(20) NOT NULL,
  `description` varchar(200) NOT NULL,
  `unitPrice` decimal(10,2) NOT NULL,
  `qty` decimal(10,2) NOT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  `subTotal` decimal(10,2) NOT NULL,
  PRIMARY KEY (`orderID`,`itemCode`),
  KEY `itemCode` (`itemCode`),
  CONSTRAINT `OrderItem_ibfk_1` FOREIGN KEY (`orderID`) REFERENCES `Order` (`orderID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `OrderItem_ibfk_2` FOREIGN KEY (`itemCode`) REFERENCES `Item` (`itemCode`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OrderItem`
--

LOCK TABLES `OrderItem` WRITE;
/*!40000 ALTER TABLE `OrderItem` DISABLE KEYS */;
INSERT INTO `OrderItem` VALUES ('INV-0001','1691708186','Kiri Samba (1KG)',100.00,5.00,0.00,500.00),('INV-0001','1745213998','Parippu (1Kg)',150.00,1.00,0.00,150.00),('INV-0001','1803849895','Rayigam Soya Meate (250g)',55.00,1.00,0.00,55.00),('INV-0002','1691708186','Kiri Samba (1KG)',100.00,1.00,0.00,100.00),('INV-0002','1745213998','Parippu (1Kg)',150.00,1.00,0.00,150.00),('INV-0002','1803849895','Rayigam Soya Meate (250g)',55.00,1.00,0.00,55.00),('INV-0003','1691708186','Kiri Samba (1KG)',100.00,1.00,0.00,100.00),('INV-0004','1691708186','Kiri Samba (1KG)',100.00,2.00,0.00,200.00),('INV-0004','1745213998','Parippu (1Kg)',150.00,0.50,0.00,75.00),('INV-0004','1803849895','Rayigam Soya Meate (250g)',55.00,1.00,0.00,55.00),('INV-0005','1691708186','Kiri Samba (1KG)',100.00,1.00,0.00,100.00),('INV-0005','1745213998','Parippu (1Kg)',150.00,1.00,0.00,150.00),('INV-0005','1803849895','Rayigam Soya Meate (250g)',55.00,2.00,0.00,110.00),('INV-0006','1691708186','Kiri Samba (1KG)',100.00,10.00,0.00,1000.00),('INV-0007','1691708186','Kiri Samba (1KG)',100.00,1.00,0.00,100.00),('INV-0007','1745213998','Parippu (1Kg)',150.00,2.00,0.00,300.00),('INV-0008','1691708186','Kiri Samba (1KG)',100.00,1.00,0.00,100.00),('INV-0008','1745213998','Parippu (1Kg)',150.00,1.00,0.00,150.00),('INV-0008','1803849895','Rayigam Soya Meate (250g)',55.00,5.00,0.00,275.00),('INV-0009','1691708186','Kiri Samba (1KG)',100.00,3.00,0.00,300.00),('INV-0009','1803849895','Rayigam Soya Meate (250g)',55.00,2.00,0.00,110.00),('INV-0010','1745213998','Parippu (1Kg)',150.00,2.00,0.00,300.00),('INV-0010','1803849895','Rayigam Soya Meate (250g)',55.00,1.00,0.00,55.00),('INV-0011','1691708186','Kiri Samba (1KG)',100.00,2.00,0.00,200.00),('INV-0012','1691708186','Kiri Samba (1KG)',100.00,1.00,0.00,100.00),('INV-0012','1803849895','Rayigam Soya Meate (250g)',55.00,1.00,0.00,55.00),('INV-0013','1691708186','Kiri Samba (1KG)',100.00,1.00,0.00,100.00),('INV-0014','1691708186','Kiri Samba (1KG)',100.00,20.00,0.00,2000.00),('INV-0015','1691708186','Kiri Samba (1KG)',100.00,5.00,0.00,500.00),('INV-0015','1745213998','Parippu (1Kg)',150.00,5.00,0.00,750.00),('INV-0015','1803849895','Rayigam Soya Meate (250g)',55.00,5.00,0.00,275.00),('INV-0016','1691708186','Kiri Samba (1KG)',100.00,0.50,0.00,50.00),('INV-0016','1745213998','Parippu (1Kg)',150.00,1.50,0.00,225.00),('INV-0017','1691708186','Kiri Samba (1KG)',100.00,3.00,0.00,300.00),('INV-0017','1745213998','Parippu (1Kg)',150.00,1.00,0.00,150.00),('INV-0018','1691708186','Kiri Samba (1KG)',100.00,1.00,0.00,100.00),('INV-0018','1745213998','Parippu (1Kg)',150.00,1.00,0.00,150.00),('INV-0018','1803849895','Rayigam Soya Meate (250g)',55.00,1.00,0.00,55.00),('INV-0019','1691708186','Kiri Samba (1KG)',100.00,5.00,0.00,500.00),('INV-0019','1745213998','Parippu (1Kg)',150.00,1.00,0.00,150.00),('INV-0019','1803849895','Rayigam Soya Meate (250g)',55.00,1.00,0.00,55.00),('INV-0020','1745213998','Parippu (1Kg)',150.00,1.00,0.00,150.00),('INV-0021','1691708186','Kiri Samba (1KG)',100.00,1.00,0.00,100.00),('INV-0021','1745213998','Parippu (1Kg)',150.00,1.00,0.00,150.00),('INV-0022','1691708186','Kiri Samba (1KG)',100.00,1.00,0.00,100.00),('INV-0022','1745213998','Parippu (1Kg)',150.00,1.00,0.00,150.00),('INV-0023','1745213998','Parippu (1Kg)',150.00,1.00,0.00,150.00),('INV-0024','1803849895','Rayigam Soya Meate (250g)',55.00,1.00,0.00,55.00),('INV-0025','1691708186','Kiri Samba (1KG)',100.00,1.00,0.00,100.00),('INV-0025','1745213998','Parippu (1Kg)',150.00,1.00,0.00,150.00),('INV-0025','1803849895','Rayigam Soya Meate (250g)',55.00,2.00,0.00,110.00),('INV-0026','1745213998','Parippu (1Kg)',150.00,5.00,0.00,750.00),('INV-0027','1691708186','Kiri Samba (1KG)',100.00,1.00,0.00,100.00),('INV-0028','1691708186','Kiri Samba (1KG)',100.00,5.00,0.00,500.00),('INV-0028','1745213998','Parippu (1Kg)',150.00,2.00,0.00,300.00),('INV-0028','1803849895','Rayigam Soya Meate (250g)',55.00,2.00,0.00,110.00);
/*!40000 ALTER TABLE `OrderItem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ReturnItem`
--

DROP TABLE IF EXISTS `ReturnItem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ReturnItem` (
  `retID` varchar(200) NOT NULL,
  `itemCode` varchar(20) NOT NULL,
  `unitPrice` decimal(10,2) NOT NULL,
  `returnQty` decimal(10,2) NOT NULL,
  `subTotal` decimal(10,2) NOT NULL,
  KEY `retID` (`retID`),
  CONSTRAINT `ReturnItem_ibfk_1` FOREIGN KEY (`retID`) REFERENCES `CustomerReturn` (`retID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ReturnItem`
--

LOCK TABLES `ReturnItem` WRITE;
/*!40000 ALTER TABLE `ReturnItem` DISABLE KEYS */;
INSERT INTO `ReturnItem` VALUES ('RET-0001','1691708186',100.00,2.00,200.00),('RET-0002','1691708186',100.00,3.00,300.00),('RET-0003','1745213998',150.00,1.00,150.00),('RET-0004','1745213998',150.00,2.00,300.00);
/*!40000 ALTER TABLE `ReturnItem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Supplier`
--

DROP TABLE IF EXISTS `Supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Supplier` (
  `supplierID` varchar(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `contact` varchar(15) NOT NULL,
  PRIMARY KEY (`supplierID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Supplier`
--

LOCK TABLES `Supplier` WRITE;
/*!40000 ALTER TABLE `Supplier` DISABLE KEYS */;
INSERT INTO `Supplier` VALUES ('SUP-0001','abc','asdj','2616');
/*!40000 ALTER TABLE `Supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `userID` varchar(5) NOT NULL,
  `userName` varchar(200) NOT NULL,
  `userAddress` varchar(200) DEFAULT NULL,
  `userContact` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES ('U-001','IdeaPOS Admin','Default','Default'),('U-002','kamal gunasekara','Delgoda','0715131363');
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-07  0:34:53
