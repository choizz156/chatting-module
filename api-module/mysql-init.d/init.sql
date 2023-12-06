CREATE USER 'choizz'@'36.38.67.6' IDENTIFIED BY '1234';
CREATE USER 'choizz'@'choizz-api1.kro.kr' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON *.* TO 'choizz'@'36.38.67.6';
GRANT ALL PRIVILEGES ON *.* TO 'choizz'@'choizz-api1.kro.kr';
CREATE DATABASE choizzdb DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci