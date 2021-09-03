-- configuration database for hibernate_test project

-- creates database hibernete_test
CREATE DATABASE IF NOT EXISTS Gestion;

-- creates a database user user_test
CREATE USER IF NOT EXISTS 'administrador'@'localhost' IDENTIFIED BY 'pAss1234';

-- add privileges to the user hibernate_test
GRANT ALL PRIVILEGES ON Gestion.* TO 'administrador'@'localhost';
GRANT SELECT ON performance_schema.* TO 'administrador'@'localhost';


-- create the Users table and add the SU user
CREATE TABLE `usuario` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`Usuario` VARCHAR(50) NOT NULL,
	`Cedula` VARCHAR(20) NOT NULL,
	`Nombre` VARCHAR(64) NULL DEFAULT NULL,
	`Pass` VARCHAR(64) NOT NULL,
	`OldPass` VARCHAR(64) NULL DEFAULT '',
	`Activo` TINYINT NOT NULL DEFAULT 0,
	`Admin` TINYINT NOT NULL DEFAULT 0,
	`CreadorId` INT NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `Índice 2` (`Usuario`)
)
COMMENT='Usuarios de la aplicacion'
COLLATE='latin1_swedish_ci'
;