CREATE USER 'choizz'@'localhost' IDENTIFIED BY '1234';
CREATE USER 'choizz'@'%' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON *.* TO 'choizz'@'localhost';
GRANT ALL PRIVILEGES ON *.* TO 'choizz'@'%';
CREATE DATABASE choizzdb DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci