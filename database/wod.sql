DROP DATABASE IF EXISTS wod;
CREATE DATABASE `wod`;
USE `wod`;

CREATE TABLE user_auth (
  id INT(11) NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  PRIMARY KEY(id),
  UNIQUE KEY (email)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO user_auth (email, password) VALUES ('test@mail.ru','1111');
INSERT INTO user_auth (email, password) VALUES ('aaaa@mail.ru','1111');
INSERT INTO user_auth (email, password) VALUES ('qwer@mail.ru','1111');
INSERT INTO user_auth (email, password) VALUES ('bbbb@mail.ru','1111');

CREATE TABLE user_info (
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  lvl INT(255) NOT NULL,
  PRIMARY KEY(id),
  UNIQUE KEY (email)
) ENGINE=INNODB DEFAULT CHARSET=utf8;