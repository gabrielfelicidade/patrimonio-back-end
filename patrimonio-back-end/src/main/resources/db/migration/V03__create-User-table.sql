CREATE TABLE `dbPatrimonySystem`.`User`(
	`user_id` bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
	`name` varchar(50) NOT NULL,
	`username` varchar(30) NOT NULL UNIQUE,
	`password` varchar(60) NOT NULL,
	`userlevel` int NOT NULL
);

INSERT INTO `dbPatrimonySystem`.`User`(`name`, `username`, `password`, `userlevel`) VALUES('Administrador', 'admin', '$2a$10$yEm6uUAVQM5Z2zQxBH/Nb.oN5lwS7IhSUO4zR6lPJ6L52dZC26bVu', 2);
/* Remover ap�s desenvolvimento */
INSERT INTO `dbPatrimonySystem`.`User`(`name`, `username`, `password`, `userlevel`) VALUES('Developer', 'dev', '$2a$10$oi1EvPZe.9WezPQwnUKVou8xwcUPy7YUqBbAHQIa2tac0OFa5JH9u', 2);