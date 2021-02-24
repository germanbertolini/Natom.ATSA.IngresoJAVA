-- MySQL dump 10.13  Distrib 5.5.40, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: afiliados
-- ------------------------------------------------------
-- Server version	5.5.40-0ubuntu0.14.04.1

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
-- Table structure for table `DOCUMENTO_ARCHIVADO`
--

DROP TABLE IF EXISTS `DOCUMENTO_ARCHIVADO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DOCUMENTO_ARCHIVADO` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACTUAL` bit(1) DEFAULT NULL,
  `ARCHIVO` varchar(255) DEFAULT NULL,
  `NOTA` varchar(255) DEFAULT NULL,
  `PERSONA_ID` bigint(20) DEFAULT NULL,
  `TIPO_DOCUMENTO_ID` bigint(20) DEFAULT NULL,
  `TRAMITE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_1suaxw8jwqi737xnbgbatoup` (`PERSONA_ID`),
  KEY `FK_ris6tn1i2whbrqu86fux1pvrf` (`TIPO_DOCUMENTO_ID`),
  KEY `FK_aeap19d4kqwhi6fc909bgh2um` (`TRAMITE_ID`),
  CONSTRAINT `FK_1suaxw8jwqi737xnbgbatoup` FOREIGN KEY (`PERSONA_ID`) REFERENCES `PERSONA` (`ID`),
  CONSTRAINT `FK_aeap19d4kqwhi6fc909bgh2um` FOREIGN KEY (`TRAMITE_ID`) REFERENCES `TRAMITE` (`ID`),
  CONSTRAINT `FK_ris6tn1i2whbrqu86fux1pvrf` FOREIGN KEY (`TIPO_DOCUMENTO_ID`) REFERENCES `TIPO_DOCUMENTO_ARCHIVADO` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DOCUMENTO_ARCHIVADO`
--

LOCK TABLES `DOCUMENTO_ARCHIVADO` WRITE;
/*!40000 ALTER TABLE `DOCUMENTO_ARCHIVADO` DISABLE KEYS */;
/*!40000 ALTER TABLE `DOCUMENTO_ARCHIVADO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ESTABLECIMIENTO`
--

DROP TABLE IF EXISTS `ESTABLECIMIENTO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ESTABLECIMIENTO` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CALLE` varchar(255) DEFAULT NULL,
  `CODIGO_POSTAL` varchar(255) DEFAULT NULL,
  `CUIT` varchar(255) DEFAULT NULL,
  `LOCALIDAD` varchar(255) DEFAULT NULL,
  `Nombre` varchar(255) DEFAULT NULL,
  `NOTAS` varchar(255) DEFAULT NULL,
  `NUMERO` int(11) DEFAULT NULL,
  `NUMERO_SUCURSAL` int(11) DEFAULT NULL,
  `PARTIDO` varchar(255) DEFAULT NULL,
  `TELEFONO` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ESTABLECIMIENTO`
--

LOCK TABLES `ESTABLECIMIENTO` WRITE;
/*!40000 ALTER TABLE `ESTABLECIMIENTO` DISABLE KEYS */;
INSERT INTO `ESTABLECIMIENTO` VALUES (1,'Puan','1706','11111','Haedo','Establecimiento 1','Un buen lugar',1,1,'Moron','44444444'),(2,'4 de noviembre','1706','22222','Villa Bosch','Establecimiento 2','Otro buen lugar',2,2,'3 de febrero','55555555');
/*!40000 ALTER TABLE `ESTABLECIMIENTO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ESTADO`
--

DROP TABLE IF EXISTS `ESTADO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ESTADO` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(255) DEFAULT NULL,
  `CODIGO` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_el8nuei6f517ihhgdoy5xyt3m` (`NOMBRE`),
  UNIQUE KEY `UK_k07drfjsgvl4fdbgy7vi76mqc` (`CODIGO`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ESTADO`
--

LOCK TABLES `ESTADO` WRITE;
/*!40000 ALTER TABLE `ESTADO` DISABLE KEYS */;
INSERT INTO `ESTADO` VALUES (1,'Pendiente','00'),(2,'Afiliado','05');
/*!40000 ALTER TABLE `ESTADO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ESTADO_TIPO_TRAMITE`
--

DROP TABLE IF EXISTS `ESTADO_TIPO_TRAMITE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ESTADO_TIPO_TRAMITE` (
  `ESTADO_ID` bigint(20) NOT NULL,
  `TIPO_TRAMITE_ID` bigint(20) NOT NULL,
  UNIQUE KEY `UK_ESTADO_TIPO_TRAMITE` (`ESTADO_ID`,`TIPO_TRAMITE_ID`),
  KEY `FK_d3tfujwcro04we0thdfymtxwu` (`TIPO_TRAMITE_ID`),
  CONSTRAINT `FK_d3tfujwcro04we0thdfymtxwu` FOREIGN KEY (`TIPO_TRAMITE_ID`) REFERENCES `TIPO_TRAMITE` (`ID`),
  CONSTRAINT `FK_e5l2ickp7n3s5thpr9ql8s6yx` FOREIGN KEY (`ESTADO_ID`) REFERENCES `ESTADO` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ESTADO_TIPO_TRAMITE`
--

LOCK TABLES `ESTADO_TIPO_TRAMITE` WRITE;
/*!40000 ALTER TABLE `ESTADO_TIPO_TRAMITE` DISABLE KEYS */;
/*!40000 ALTER TABLE `ESTADO_TIPO_TRAMITE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ESTADO_TRAMITE`
--

DROP TABLE IF EXISTS `ESTADO_TRAMITE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ESTADO_TRAMITE` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(255) DEFAULT NULL,
  `CODIGO` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_n1m7f8m8etf4dlyi5rx0jokgm` (`NOMBRE`),
  UNIQUE KEY `UK_65sx5rwa4obr9bntnntj5vl4i` (`CODIGO`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ESTADO_TRAMITE`
--

LOCK TABLES `ESTADO_TRAMITE` WRITE;
/*!40000 ALTER TABLE `ESTADO_TRAMITE` DISABLE KEYS */;
INSERT INTO `ESTADO_TRAMITE` VALUES (1,'Pendiente','00'),(2,'Aprobado','05');
/*!40000 ALTER TABLE `ESTADO_TRAMITE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ESTUDIO`
--

DROP TABLE IF EXISTS `ESTUDIO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ESTUDIO` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ESTABLECIMIENTO` varchar(255) DEFAULT NULL,
  `NIVEL` varchar(1) NOT NULL,
  `TERMINADO` bit(1) DEFAULT NULL,
  `TITULO` varchar(255) DEFAULT NULL,
  `PERSONA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_8o6ncyywnnojm2roc0e6btfk` (`PERSONA_ID`),
  CONSTRAINT `FK_8o6ncyywnnojm2roc0e6btfk` FOREIGN KEY (`PERSONA_ID`) REFERENCES `PERSONA` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ESTUDIO`
--

LOCK TABLES `ESTUDIO` WRITE;
/*!40000 ALTER TABLE `ESTUDIO` DISABLE KEYS */;
INSERT INTO `ESTUDIO` VALUES (1,'ENET','S','','Técnico electrónico',1),(6,'Aca cerca','P','','Primario',11);
/*!40000 ALTER TABLE `ESTUDIO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FAMILIAR`
--

DROP TABLE IF EXISTS `FAMILIAR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FAMILIAR` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `AFILIADO_ID` bigint(20) DEFAULT NULL,
  `FAMILIAR_ID` bigint(20) DEFAULT NULL,
  `TIPO_RELACION_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_fs2m7m6guqvr221m1gq7tgp6m` (`AFILIADO_ID`),
  KEY `FK_7sy0ddbpkq7wmo1tdnjyp2w24` (`FAMILIAR_ID`),
  KEY `FK_te7mqj4mxleg3t6o3i9c5tnun` (`TIPO_RELACION_ID`),
  CONSTRAINT `FK_7sy0ddbpkq7wmo1tdnjyp2w24` FOREIGN KEY (`FAMILIAR_ID`) REFERENCES `PERSONA` (`ID`),
  CONSTRAINT `FK_fs2m7m6guqvr221m1gq7tgp6m` FOREIGN KEY (`AFILIADO_ID`) REFERENCES `PERSONA` (`ID`),
  CONSTRAINT `FK_te7mqj4mxleg3t6o3i9c5tnun` FOREIGN KEY (`TIPO_RELACION_ID`) REFERENCES `TIPO_RELACION` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FAMILIAR`
--

LOCK TABLES `FAMILIAR` WRITE;
/*!40000 ALTER TABLE `FAMILIAR` DISABLE KEYS */;
INSERT INTO `FAMILIAR` VALUES (1,1,2,1),(4,9,10,1),(5,11,12,1),(6,11,13,2);
/*!40000 ALTER TABLE `FAMILIAR` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PAIS`
--

DROP TABLE IF EXISTS `PAIS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PAIS` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCRIPCION` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PAIS`
--

LOCK TABLES `PAIS` WRITE;
/*!40000 ALTER TABLE `PAIS` DISABLE KEYS */;
INSERT INTO `PAIS` VALUES (1,'Argentina'),(2,'Brasil');
/*!40000 ALTER TABLE `PAIS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PERSONA`
--

DROP TABLE IF EXISTS `PERSONA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PERSONA` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACTIVO` bit(1) DEFAULT NULL,
  `APELLIDOS` varchar(50) NOT NULL,
  `CODIGO_POSTAL` varchar(255) DEFAULT NULL,
  `CUIL` varchar(255) DEFAULT NULL,
  `DOCUMENTO` varchar(50) NOT NULL,
  `DOCUMENTO_TIPO` varchar(1) NOT NULL,
  `FECHA_AFILIACION` datetime DEFAULT NULL,
  `FECHA_INGRESO` datetime DEFAULT NULL,
  `FECHA_NACIMIENTO` datetime DEFAULT NULL,
  `LOCALIDAD` varchar(255) DEFAULT NULL,
  `NOMBRES` varchar(50) NOT NULL,
  `PROFESION` varchar(100) DEFAULT NULL,
  `SEXO` varchar(1) NOT NULL,
  `TELEFONO` varchar(255) DEFAULT NULL,
  `TIPO_PERSONA` varchar(1) NOT NULL,
  `ESTABLECIMIENTO_ID` bigint(20) DEFAULT NULL,
  `ESTADO_ID` bigint(20) DEFAULT NULL,
  `NACIONALIDAD` bigint(20) DEFAULT NULL,
  `NUMERO_AFILIADO` varchar(255) DEFAULT NULL,
  `DOMICILIO` varchar(255) DEFAULT NULL,
  `ESTADO_CIVIL` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_2k5liirbvr30utwxf3l4pa2cw` (`ESTABLECIMIENTO_ID`),
  KEY `FK_qt1dikwyy2mnxgrhrdqnspx88` (`ESTADO_ID`),
  KEY `FK_acd4fywt5mqoxps35k3xtue66` (`NACIONALIDAD`),
  CONSTRAINT `FK_2k5liirbvr30utwxf3l4pa2cw` FOREIGN KEY (`ESTABLECIMIENTO_ID`) REFERENCES `ESTABLECIMIENTO` (`ID`),
  CONSTRAINT `FK_acd4fywt5mqoxps35k3xtue66` FOREIGN KEY (`NACIONALIDAD`) REFERENCES `PAIS` (`ID`),
  CONSTRAINT `FK_qt1dikwyy2mnxgrhrdqnspx88` FOREIGN KEY (`ESTADO_ID`) REFERENCES `ESTADO` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PERSONA`
--

LOCK TABLES `PERSONA` WRITE;
/*!40000 ALTER TABLE `PERSONA` DISABLE KEYS */;
INSERT INTO `PERSONA` VALUES (1,'','Sanchez','1706','20292791608','29279160','D','2000-01-01 00:00:00','2000-01-01 00:00:00','1982-01-01 00:00:00','Haedo','Matias','Coso','M','1533935791','A',1,1,1,'1','Puan 791','C'),(2,'','Sanchez','1706','','25556555','D',NULL,NULL,'1982-01-01 00:00:00','Haedo','Damian',NULL,'M',NULL,'F',NULL,NULL,1,NULL,NULL,'S'),(3,'','Mendoza','1111','25638724','25638724','D',NULL,NULL,'1981-05-07 00:00:00','Cap. Fed','Diego','Programador','M','111112222','A',1,NULL,1,NULL,'Belgrano 1370 8vo C','S'),(9,'','Moncho','1','1','1','D',NULL,NULL,'2014-11-05 00:00:00','1','Matias','1','M','1','A',1,NULL,1,NULL,'1','S'),(10,'','Moncho',NULL,NULL,'22223333','D',NULL,NULL,'2014-11-12 00:00:00',NULL,'Gaston',NULL,'M',NULL,'F',NULL,NULL,NULL,NULL,NULL,NULL),(11,'','Perez','1705','3','3','D',NULL,NULL,'2014-11-20 00:00:00','Villa Bosch','Paula','3','M','33224455','A',1,NULL,1,NULL,'4 de noviembre 5182','S'),(12,'','Perez',NULL,NULL,'22222333','D',NULL,NULL,'2014-11-04 00:00:00',NULL,'Marisol',NULL,'F',NULL,'F',NULL,NULL,NULL,NULL,NULL,NULL),(13,'','Pousada',NULL,NULL,'334455','D',NULL,NULL,'2014-11-15 00:00:00',NULL,'Teresa',NULL,'F',NULL,'F',NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `PERSONA` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ROL`
--

DROP TABLE IF EXISTS `ROL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ROL` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_n5s1rabnjxug45hgklyvsqro9` (`NOMBRE`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ROL`
--

LOCK TABLES `ROL` WRITE;
/*!40000 ALTER TABLE `ROL` DISABLE KEYS */;
INSERT INTO `ROL` VALUES (1,'Rol1');
/*!40000 ALTER TABLE `ROL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ROL_TIPO_TRAMITE`
--

DROP TABLE IF EXISTS `ROL_TIPO_TRAMITE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ROL_TIPO_TRAMITE` (
  `ROL_ID` bigint(20) NOT NULL,
  `TIPO_TRAMITE_ID` bigint(20) NOT NULL,
  UNIQUE KEY `UK_ROL_TIPO_TRAMITE` (`ROL_ID`,`TIPO_TRAMITE_ID`),
  KEY `FK_1ywcxnxca301pdojwsau4t7o6` (`TIPO_TRAMITE_ID`),
  CONSTRAINT `FK_1ywcxnxca301pdojwsau4t7o6` FOREIGN KEY (`TIPO_TRAMITE_ID`) REFERENCES `TIPO_TRAMITE` (`ID`),
  CONSTRAINT `FK_ntuapq7yuei2fne7muvjgq2jf` FOREIGN KEY (`ROL_ID`) REFERENCES `ROL` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ROL_TIPO_TRAMITE`
--

LOCK TABLES `ROL_TIPO_TRAMITE` WRITE;
/*!40000 ALTER TABLE `ROL_TIPO_TRAMITE` DISABLE KEYS */;
INSERT INTO `ROL_TIPO_TRAMITE` VALUES (1,1);
/*!40000 ALTER TABLE `ROL_TIPO_TRAMITE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TIPO_DOCUMENTO_ARCHIVADO`
--

DROP TABLE IF EXISTS `TIPO_DOCUMENTO_ARCHIVADO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TIPO_DOCUMENTO_ARCHIVADO` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_pjl72r0ef20am9d1bs1d3mqbm` (`NOMBRE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TIPO_DOCUMENTO_ARCHIVADO`
--

LOCK TABLES `TIPO_DOCUMENTO_ARCHIVADO` WRITE;
/*!40000 ALTER TABLE `TIPO_DOCUMENTO_ARCHIVADO` DISABLE KEYS */;
/*!40000 ALTER TABLE `TIPO_DOCUMENTO_ARCHIVADO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TIPO_RELACION`
--

DROP TABLE IF EXISTS `TIPO_RELACION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TIPO_RELACION` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_ar53geu0tlug8djkggsxb5h6y` (`NOMBRE`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TIPO_RELACION`
--

LOCK TABLES `TIPO_RELACION` WRITE;
/*!40000 ALTER TABLE `TIPO_RELACION` DISABLE KEYS */;
INSERT INTO `TIPO_RELACION` VALUES (1,'Hermano'),(2,'Hijo');
/*!40000 ALTER TABLE `TIPO_RELACION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TIPO_TRAMITE`
--

DROP TABLE IF EXISTS `TIPO_TRAMITE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TIPO_TRAMITE` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(255) DEFAULT NULL,
  `CODIGO` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_np73h839tk587g1p1alpq4btx` (`NOMBRE`),
  UNIQUE KEY `UK_s98hkjsdv2tbl9ipwneikobsd` (`CODIGO`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TIPO_TRAMITE`
--

LOCK TABLES `TIPO_TRAMITE` WRITE;
/*!40000 ALTER TABLE `TIPO_TRAMITE` DISABLE KEYS */;
INSERT INTO `TIPO_TRAMITE` VALUES (1,'Afiliacion','00'),(2,'Cambio de datos registrales','05'),(4,'Cambio de establecimiento','10'),(5,'Baja de afiliado','15'),(6,'Afiliado Pasivo','20'),(7,'Nota Multipropósito','25'),(8,'Agregar Familiar/Dependiente','30'),(9,'Eliminar Familiar/Dependiente','35'),(10,'Carga de documentos para archvado','40');
/*!40000 ALTER TABLE `TIPO_TRAMITE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TIPO_TRAMITE_ROL_APROBADOR`
--

DROP TABLE IF EXISTS `TIPO_TRAMITE_ROL_APROBADOR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TIPO_TRAMITE_ROL_APROBADOR` (
  `TIPO_TRAMITE_ID` bigint(20) NOT NULL,
  `APROBADOR_ID` bigint(20) NOT NULL,
  UNIQUE KEY `UK_TIPO_TRAMITE_ROL_APROBADOR` (`TIPO_TRAMITE_ID`,`APROBADOR_ID`),
  KEY `FK_86klw23jkyxewnmbb97763fa` (`APROBADOR_ID`),
  CONSTRAINT `FK_pjpxcwea5evm59dx51dj6m1u5` FOREIGN KEY (`TIPO_TRAMITE_ID`) REFERENCES `TIPO_TRAMITE` (`ID`),
  CONSTRAINT `FK_86klw23jkyxewnmbb97763fa` FOREIGN KEY (`APROBADOR_ID`) REFERENCES `ROL` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TIPO_TRAMITE_ROL_APROBADOR`
--

LOCK TABLES `TIPO_TRAMITE_ROL_APROBADOR` WRITE;
/*!40000 ALTER TABLE `TIPO_TRAMITE_ROL_APROBADOR` DISABLE KEYS */;
/*!40000 ALTER TABLE `TIPO_TRAMITE_ROL_APROBADOR` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TIPO_TRAMITE_TIPO_DOCUMENTO`
--

DROP TABLE IF EXISTS `TIPO_TRAMITE_TIPO_DOCUMENTO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TIPO_TRAMITE_TIPO_DOCUMENTO` (
  `TIPO_TRAMITE_ID` bigint(20) NOT NULL,
  `TIPO_DOCUMENTO_ID` bigint(20) NOT NULL,
  UNIQUE KEY `UK_TIPO_TRAMITE_TIPO_DOCUMENTO` (`TIPO_TRAMITE_ID`,`TIPO_DOCUMENTO_ID`),
  KEY `FK_tn6e80kph2owlit6etfw76aax` (`TIPO_DOCUMENTO_ID`),
  CONSTRAINT `FK_dsirx82ov6mh2tt1egtsa4g1x` FOREIGN KEY (`TIPO_TRAMITE_ID`) REFERENCES `TIPO_TRAMITE` (`ID`),
  CONSTRAINT `FK_tn6e80kph2owlit6etfw76aax` FOREIGN KEY (`TIPO_DOCUMENTO_ID`) REFERENCES `TIPO_DOCUMENTO_ARCHIVADO` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TIPO_TRAMITE_TIPO_DOCUMENTO`
--

LOCK TABLES `TIPO_TRAMITE_TIPO_DOCUMENTO` WRITE;
/*!40000 ALTER TABLE `TIPO_TRAMITE_TIPO_DOCUMENTO` DISABLE KEYS */;
/*!40000 ALTER TABLE `TIPO_TRAMITE_TIPO_DOCUMENTO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TRAMITE`
--

DROP TABLE IF EXISTS `TRAMITE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TRAMITE` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA` varchar(255) DEFAULT NULL,
  `FECHA_APROBACION` datetime DEFAULT NULL,
  `FECHA_SOLICITUD` datetime NOT NULL,
  `APROBADOR_ID` bigint(20) DEFAULT NULL,
  `ESTADO_ID` bigint(20) DEFAULT NULL,
  `PERSONA_ID` bigint(20) DEFAULT NULL,
  `TIPO_TRAMITE_ID` bigint(20) DEFAULT NULL,
  `USUARIO_ID` bigint(20) DEFAULT NULL,
  `NOTA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_29a5xysshcxkx00inyji54q5w` (`APROBADOR_ID`),
  KEY `FK_c774kac1u14947r8qwft711ar` (`ESTADO_ID`),
  KEY `FK_sao2j9e2k39ydptjmt57n7jfc` (`PERSONA_ID`),
  KEY `FK_7xtrh1s008np21l04fchjvb6b` (`TIPO_TRAMITE_ID`),
  KEY `FK_5cs3umf5j3ihh14o3yqta1s7y` (`USUARIO_ID`),
  KEY `FK_pwo9johhfjsdfxvuuwjlfnv0e` (`NOTA_ID`),
  CONSTRAINT `FK_pwo9johhfjsdfxvuuwjlfnv0e` FOREIGN KEY (`NOTA_ID`) REFERENCES `TRAMITE` (`ID`),
  CONSTRAINT `FK_29a5xysshcxkx00inyji54q5w` FOREIGN KEY (`APROBADOR_ID`) REFERENCES `USUARIO` (`ID`),
  CONSTRAINT `FK_5cs3umf5j3ihh14o3yqta1s7y` FOREIGN KEY (`USUARIO_ID`) REFERENCES `USUARIO` (`ID`),
  CONSTRAINT `FK_7xtrh1s008np21l04fchjvb6b` FOREIGN KEY (`TIPO_TRAMITE_ID`) REFERENCES `TIPO_TRAMITE` (`ID`),
  CONSTRAINT `FK_c774kac1u14947r8qwft711ar` FOREIGN KEY (`ESTADO_ID`) REFERENCES `ESTADO_TRAMITE` (`ID`),
  CONSTRAINT `FK_sao2j9e2k39ydptjmt57n7jfc` FOREIGN KEY (`PERSONA_ID`) REFERENCES `PERSONA` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TRAMITE`
--

LOCK TABLES `TRAMITE` WRITE;
/*!40000 ALTER TABLE `TRAMITE` DISABLE KEYS */;
INSERT INTO `TRAMITE` VALUES (1,'',NULL,'1982-01-01 00:00:00',NULL,1,1,1,1,NULL);
/*!40000 ALTER TABLE `TRAMITE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USUARIO`
--

DROP TABLE IF EXISTS `USUARIO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USUARIO` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACTIVO` bit(1) DEFAULT NULL,
  `APELLIDOS` varchar(255) DEFAULT NULL,
  `CLAVE` varchar(255) DEFAULT NULL,
  `EMAIL` varchar(255) DEFAULT NULL,
  `NOMBRES` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_5kj57sci0og84ngs04ndwa7ch` (`EMAIL`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USUARIO`
--

LOCK TABLES `USUARIO` WRITE;
/*!40000 ALTER TABLE `USUARIO` DISABLE KEYS */;
INSERT INTO `USUARIO` VALUES (1,'','Galleta Perez','password','pepe.galleta@mail.com','Pepe Rigoberto');
/*!40000 ALTER TABLE `USUARIO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USUARIO_ROL`
--

DROP TABLE IF EXISTS `USUARIO_ROL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USUARIO_ROL` (
  `USUARIO_ID` bigint(20) NOT NULL,
  `ROL_ID` bigint(20) NOT NULL,
  UNIQUE KEY `UK_USUARIO_ROL` (`USUARIO_ID`,`ROL_ID`),
  KEY `FK_3vpuqm4h1m32r9gvhgo83rijy` (`ROL_ID`),
  CONSTRAINT `FK_1gwlvcmesems249np11tn2p02` FOREIGN KEY (`USUARIO_ID`) REFERENCES `USUARIO` (`ID`),
  CONSTRAINT `FK_3vpuqm4h1m32r9gvhgo83rijy` FOREIGN KEY (`ROL_ID`) REFERENCES `ROL` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USUARIO_ROL`
--

LOCK TABLES `USUARIO_ROL` WRITE;
/*!40000 ALTER TABLE `USUARIO_ROL` DISABLE KEYS */;
INSERT INTO `USUARIO_ROL` VALUES (1,1);
/*!40000 ALTER TABLE `USUARIO_ROL` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-11-12 21:58:13
