-- configuration database for hibernate_test project

-- creates database hibernete_test
CREATE DATABASE IF NOT EXISTS Gestion;

-- creates a database user user_test
CREATE USER IF NOT EXISTS 'administrador'@'localhost' IDENTIFIED BY 'pAss1234';

-- add privileges to the user hibernate_test
GRANT ALL PRIVILEGES ON Gestion.* TO 'administrador'@'localhost';
GRANT SELECT ON performance_schema.* TO 'administrador'@'localhost';
