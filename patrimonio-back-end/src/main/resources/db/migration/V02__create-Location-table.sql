CREATE TABLE `dbPatrimonySystem`.`Location`(
	`location_id` bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
	`description` varchar(100) NOT NULL UNIQUE
);