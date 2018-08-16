-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.3.8-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for ngpuppies_database
CREATE DATABASE IF NOT EXISTS `ngpuppies_database` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `ngpuppies_database`;

-- Dumping structure for table ngpuppies_database.addresses
CREATE TABLE IF NOT EXISTS `addresses` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `City` varchar(50) DEFAULT NULL,
  `Street` varchar(100) DEFAULT NULL,
  `State` varchar(50) DEFAULT NULL,
  `PostCode` varchar(15) DEFAULT NULL,
  `Country` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table ngpuppies_database.addresses: ~0 rows (approximately)
/*!40000 ALTER TABLE `addresses` DISABLE KEYS */;
/*!40000 ALTER TABLE `addresses` ENABLE KEYS */;

-- Dumping structure for table ngpuppies_database.admins
CREATE TABLE IF NOT EXISTS `admins` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Username` varchar(255) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `Email` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Email` (`Email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table ngpuppies_database.admins: ~0 rows (approximately)
/*!40000 ALTER TABLE `admins` DISABLE KEYS */;
/*!40000 ALTER TABLE `admins` ENABLE KEYS */;

-- Dumping structure for table ngpuppies_database.billing_records
CREATE TABLE IF NOT EXISTS `billing_records` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `StartDate` datetime NOT NULL,
  `EndDate` datetime NOT NULL,
  `Amount` double NOT NULL,
  `OfferedServiceID` int(11) NOT NULL DEFAULT 0,
  `CurrencyID` int(11) NOT NULL,
  `SubscriberID` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_billing_record_offered_sercvices` (`OfferedServiceID`),
  KEY `FK_billing_record_currencies` (`CurrencyID`),
  KEY `FK_billing_records_subscribers` (`SubscriberID`),
  CONSTRAINT `FK_billing_record_currencies` FOREIGN KEY (`CurrencyID`) REFERENCES `currencies` (`Id`),
  CONSTRAINT `FK_billing_record_offered_sercvices` FOREIGN KEY (`OfferedServiceID`) REFERENCES `offered_sercvices` (`Id`),
  CONSTRAINT `FK_billing_records_subscribers` FOREIGN KEY (`SubscriberID`) REFERENCES `subscribers` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table ngpuppies_database.billing_records: ~0 rows (approximately)
/*!40000 ALTER TABLE `billing_records` DISABLE KEYS */;
/*!40000 ALTER TABLE `billing_records` ENABLE KEYS */;

-- Dumping structure for table ngpuppies_database.clients
CREATE TABLE IF NOT EXISTS `clients` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Username` varchar(255) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `EIK` varchar(35) NOT NULL,
  `DetailsID` int(10) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `EIK` (`EIK`),
  KEY `FK_clients_clients_details` (`DetailsID`),
  CONSTRAINT `FK_clients_clients_details` FOREIGN KEY (`DetailsID`) REFERENCES `clients_details` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table ngpuppies_database.clients: ~0 rows (approximately)
/*!40000 ALTER TABLE `clients` DISABLE KEYS */;
/*!40000 ALTER TABLE `clients` ENABLE KEYS */;

-- Dumping structure for table ngpuppies_database.clients_details
CREATE TABLE IF NOT EXISTS `clients_details` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Description` text DEFAULT '0',
  `FriendlyName` varchar(50) DEFAULT '0',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table ngpuppies_database.clients_details: ~0 rows (approximately)
/*!40000 ALTER TABLE `clients_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `clients_details` ENABLE KEYS */;

-- Dumping structure for table ngpuppies_database.currencies
CREATE TABLE IF NOT EXISTS `currencies` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(30) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Name` (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table ngpuppies_database.currencies: ~0 rows (approximately)
/*!40000 ALTER TABLE `currencies` DISABLE KEYS */;
/*!40000 ALTER TABLE `currencies` ENABLE KEYS */;

-- Dumping structure for table ngpuppies_database.offered_sercvices
CREATE TABLE IF NOT EXISTS `offered_sercvices` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table ngpuppies_database.offered_sercvices: ~0 rows (approximately)
/*!40000 ALTER TABLE `offered_sercvices` DISABLE KEYS */;
/*!40000 ALTER TABLE `offered_sercvices` ENABLE KEYS */;

-- Dumping structure for table ngpuppies_database.subscribers
CREATE TABLE IF NOT EXISTS `subscribers` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `PhoneNumber` varchar(50) NOT NULL,
  `FirstName` varchar(50) NOT NULL,
  `LastName` varchar(50) NOT NULL,
  `EGN` varchar(25) NOT NULL,
  `AddressID` int(11) DEFAULT NULL,
  `ClientID` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `EGN` (`EGN`),
  UNIQUE KEY `PhoneNumber` (`PhoneNumber`),
  KEY `FK_subscribers_addresses` (`AddressID`),
  KEY `FK_subscribers_clients` (`ClientID`),
  CONSTRAINT `FK_subscribers_addresses` FOREIGN KEY (`AddressID`) REFERENCES `addresses` (`Id`),
  CONSTRAINT `FK_subscribers_clients` FOREIGN KEY (`ClientID`) REFERENCES `clients` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table ngpuppies_database.subscribers: ~0 rows (approximately)
/*!40000 ALTER TABLE `subscribers` DISABLE KEYS */;
/*!40000 ALTER TABLE `subscribers` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
