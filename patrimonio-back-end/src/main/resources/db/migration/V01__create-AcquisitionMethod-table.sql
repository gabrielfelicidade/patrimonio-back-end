CREATE TABLE `dbPatrimonySystem`.`AcquisitionMethod`(
	`acquisition_method_id` bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
	`description` varchar(50) NOT NULL UNIQUE
);