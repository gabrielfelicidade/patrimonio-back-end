CREATE TABLE `dbPatrimonySystem`.`Log`(
	`log_id` bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
	`date` datetime NOT NULL,
	`interfacename` varchar(100) NOT NULL,
	`action` varchar(100) NOT NULL,
	`user_id` varchar(85) NOT NULL
);