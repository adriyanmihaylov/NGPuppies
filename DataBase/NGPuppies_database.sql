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


-- Dumping database structure for ngpuppies
CREATE DATABASE IF NOT EXISTS `ngpuppies` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `ngpuppies`;

-- Dumping structure for table ngpuppies.bills
CREATE TABLE IF NOT EXISTS `bills` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ServiceID` int(11) NOT NULL DEFAULT 0,
  `SubscriberID` varchar(50) NOT NULL DEFAULT '0',
  `StartDate` date NOT NULL COMMENT 'Bill start date',
  `EndDate` date NOT NULL COMMENT 'Due date',
  `Amount` double NOT NULL DEFAULT 0,
  `CurrencyID` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`Id`),
  KEY `FK_bills_services` (`ServiceID`),
  KEY `FK_bills_currencies` (`CurrencyID`),
  KEY `FK_bills_subscribers` (`SubscriberID`),
  CONSTRAINT `FK_bills_currencies` FOREIGN KEY (`CurrencyID`) REFERENCES `currencies` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_bills_services` FOREIGN KEY (`ServiceID`) REFERENCES `services` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_bills_subscribers` FOREIGN KEY (`SubscriberID`) REFERENCES `subscribers` (`PhoneNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

-- Dumping data for table ngpuppies.bills: ~4 rows (approximately)
/*!40000 ALTER TABLE `bills` DISABLE KEYS */;
INSERT INTO `bills` (`Id`, `ServiceID`, `SubscriberID`, `StartDate`, `EndDate`, `Amount`, `CurrencyID`) VALUES
	(2, 1, '0882965405', '2018-08-14', '2018-08-21', 100, 1),
	(3, 2, '0882965505', '2018-08-14', '2018-08-21', 150, 2),
	(4, 3, '0885629906', '2018-08-14', '2018-08-21', 60, 3),
	(5, 3, '0886510950', '2018-08-14', '2018-08-21', 10, 3);
/*!40000 ALTER TABLE `bills` ENABLE KEYS */;

-- Dumping structure for table ngpuppies.currencies
CREATE TABLE IF NOT EXISTS `currencies` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Type` varchar(50) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- Dumping data for table ngpuppies.currencies: ~4 rows (approximately)
/*!40000 ALTER TABLE `currencies` DISABLE KEYS */;
INSERT INTO `currencies` (`Id`, `Type`) VALUES
	(1, 'BGN'),
	(2, 'EUR'),
	(3, 'USD'),
	(4, 'GBP');
/*!40000 ALTER TABLE `currencies` ENABLE KEYS */;

-- Dumping structure for table ngpuppies.roles
CREATE TABLE IF NOT EXISTS `roles` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Type` varchar(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- Dumping data for table ngpuppies.roles: ~2 rows (approximately)
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` (`Id`, `Type`) VALUES
	(1, 'ADMIN'),
	(2, 'USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;

-- Dumping structure for table ngpuppies.services
CREATE TABLE IF NOT EXISTS `services` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Type` varchar(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- Dumping data for table ngpuppies.services: ~3 rows (approximately)
/*!40000 ALTER TABLE `services` DISABLE KEYS */;
INSERT INTO `services` (`Id`, `Type`) VALUES
	(1, 'Telephone'),
	(2, 'Television'),
	(3, 'Internet');
/*!40000 ALTER TABLE `services` ENABLE KEYS */;

-- Dumping structure for table ngpuppies.subscribers
CREATE TABLE IF NOT EXISTS `subscribers` (
  `PhoneNumber` varchar(50) NOT NULL,
  `FirstName` varchar(50) NOT NULL,
  `LastName` varchar(50) NOT NULL,
  `EGN` varchar(10) NOT NULL,
  `Address` varchar(50) DEFAULT NULL,
  `BankID` int(11) NOT NULL,
  PRIMARY KEY (`PhoneNumber`),
  UNIQUE KEY `subscribers_PhoneNumber_uindex` (`PhoneNumber`),
  KEY `FK_subscribers_users` (`BankID`),
  CONSTRAINT `FK_subscribers_users` FOREIGN KEY (`BankID`) REFERENCES `users` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table ngpuppies.subscribers: ~4 rows (approximately)
/*!40000 ALTER TABLE `subscribers` DISABLE KEYS */;
INSERT INTO `subscribers` (`PhoneNumber`, `FirstName`, `LastName`, `EGN`, `Address`, `BankID`) VALUES
	('0882965405', 'Georgi', 'Georgiev', '123456789', 'MLadost 4', 3),
	('0882965505', 'Yani', 'Drenchev', '123456789', 'Orlandovci', 1),
	('0885629906', 'Adrian', 'Mihailov', '123456789', 'Musagenica', 2),
	('0886510950', 'Ivan', 'Ivanov', '78945613', 'Mladost 3', 4);
/*!40000 ALTER TABLE `subscribers` ENABLE KEYS */;

-- Dumping structure for table ngpuppies.users
CREATE TABLE IF NOT EXISTS `users` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Username` varchar(255) NOT NULL DEFAULT '0',
  `Password` varchar(255) NOT NULL DEFAULT '0',
  `RoleID` int(11) NOT NULL,
  `EIK` varchar(50) NOT NULL,
  `details` text DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_users_roles` (`RoleID`),
  CONSTRAINT `FK_users_roles` FOREIGN KEY (`RoleID`) REFERENCES `roles` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COMMENT='details';

-- Dumping data for table ngpuppies.users: ~4 rows (approximately)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`Id`, `Username`, `Password`, `RoleID`, `EIK`, `details`) VALUES
	(1, 'DSK', '12345', 2, '000365498', NULL),
	(2, 'Pireus', '123', 2, '000365498', NULL),
	(3, 'FIBANK', '1234', 2, '000365498', NULL),
	(4, 'Uncreditr', '13245', 2, '000365498', NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
