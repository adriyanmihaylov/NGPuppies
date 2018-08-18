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

-- Dumping structure for table ngpuppies_database.billing_records
CREATE TABLE IF NOT EXISTS `billing_records` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `StartDate` datetime NOT NULL DEFAULT current_timestamp(),
  `EndDate` datetime DEFAULT NULL ON UPDATE current_timestamp(),
  `Amount` double NOT NULL,
  `OfferedServiceID` int(11) NOT NULL DEFAULT 0,
  `CurrencyID` int(11) NOT NULL,
  `SubscriberID` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_billing_record_offered_sercvices` (`OfferedServiceID`),
  KEY `FK_billing_record_currencies` (`CurrencyID`),
  KEY `FK_billing_records_subscribers` (`SubscriberID`),
  CONSTRAINT `FK_billing_record_currencies` FOREIGN KEY (`CurrencyID`) REFERENCES `currencies` (`Id`),
  CONSTRAINT `FK_billing_record_offered_sercvices` FOREIGN KEY (`OfferedServiceID`) REFERENCES `offered_services` (`Id`),
  CONSTRAINT `FK_billing_records_subscribers` FOREIGN KEY (`SubscriberID`) REFERENCES `subscribers` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table ngpuppies_database.billing_records: ~0 rows (approximately)
/*!40000 ALTER TABLE `billing_records` DISABLE KEYS */;
/*!40000 ALTER TABLE `billing_records` ENABLE KEYS */;

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

-- Dumping structure for table ngpuppies_database.offered_services
CREATE TABLE IF NOT EXISTS `offered_services` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table ngpuppies_database.offered_services: ~0 rows (approximately)
/*!40000 ALTER TABLE `offered_services` DISABLE KEYS */;
/*!40000 ALTER TABLE `offered_services` ENABLE KEYS */;

-- Dumping structure for table ngpuppies_database.roles
CREATE TABLE IF NOT EXISTS `roles` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- Dumping data for table ngpuppies_database.roles: ~2 rows (approximately)
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` (`Id`, `Name`) VALUES
	(1, 'ADMIN'),
	(2, 'USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;

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
  KEY `FK_subscribers_users_credentials` (`ClientID`),
  CONSTRAINT `FK_subscribers_addresses` FOREIGN KEY (`AddressID`) REFERENCES `addresses` (`Id`),
  CONSTRAINT `FK_subscribers_users_credentials` FOREIGN KEY (`ClientID`) REFERENCES `users_credentials` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table ngpuppies_database.subscribers: ~0 rows (approximately)
/*!40000 ALTER TABLE `subscribers` DISABLE KEYS */;
/*!40000 ALTER TABLE `subscribers` ENABLE KEYS */;

-- Dumping structure for table ngpuppies_database.users_credentials
CREATE TABLE IF NOT EXISTS `users_credentials` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Username` varchar(50) NOT NULL DEFAULT '0',
  `Password` varchar(255) NOT NULL DEFAULT '0',
  `Role` varchar(50) NOT NULL DEFAULT '0',
  `Email` varchar(50) DEFAULT '0',
  `Eik` varchar(50) DEFAULT '0',
  `DetailsID` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_users_credentials_clients_details` (`DetailsID`),
  CONSTRAINT `FK_users_credentials_clients_details` FOREIGN KEY (`DetailsID`) REFERENCES `clients_details` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- Dumping data for table ngpuppies_database.users_credentials: ~1 rows (approximately)
/*!40000 ALTER TABLE `users_credentials` DISABLE KEYS */;
INSERT INTO `users_credentials` (`Id`, `Username`, `Password`, `Role`, `Email`, `Eik`, `DetailsID`) VALUES
	(3, 'admin', '123456', 'ADMIN', 'admin@abv.bg', '', NULL);
/*!40000 ALTER TABLE `users_credentials` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
