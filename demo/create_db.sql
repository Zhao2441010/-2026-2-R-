-- create_db.sql
-- 创建数据库并建立与实体 Task 对应的表（兼容 Hibernate 的命名策略）

-- 1) 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `cat_system` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2) 使用数据库
USE `cat_system`;

-- 3) 创建表 task（字段名与实体属性一致）
CREATE TABLE IF NOT EXISTS `task` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(255) DEFAULT NULL,
  `eventdate` DATE DEFAULT NULL,
  `need` BIGINT DEFAULT 0,
  `have` BIGINT DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4) 简单检查（可手动运行以下语句来确认）
-- SHOW DATABASES;
-- USE cat_system; SHOW TABLES;
-- DESCRIBE task;

-- 注意：如果你的应用仍然无法访问或 Hibernate 没有自动建表，请检查：
--  - application.yml 中的 datasource 配置（用户名/密码/URL）是否正确
--  - 数据库用户是否有 CREATE/ALTER 权限
--  - 应用启动日志中是否有连接错误或权限错误

-- 如需授予权限（以 root@localhost 为例，若使用其它用户请替换）：
-- GRANT ALL PRIVILEGES ON `cat_system`.* TO 'root'@'localhost';
-- FLUSH PRIVILEGES;

