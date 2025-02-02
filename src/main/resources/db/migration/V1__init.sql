-- 文件路径: src/main/resources/db/migration/V1__init.sql

CREATE TABLE IF NOT EXISTS `comments` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `text` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
                            `parent_comment_id` bigint DEFAULT NULL,
                            `news_id` bigint NOT NULL,
                            `author_id` bigint NOT NULL,
                            `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `news` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                        `url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                        `text` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
                        `author_id` bigint NOT NULL,
                        `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                        `news_type` varchar(24) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NORMAL' COMMENT '新闻类型',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=473 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `users` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
                         `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                         `email` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
                         `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         `is_deleted` tinyint DEFAULT '0' COMMENT '是否被删除',
                         `about` text COLLATE utf8mb4_unicode_ci COMMENT '个人说明',
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `votes` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `user_id` bigint NOT NULL,
                         `item_id` bigint NOT NULL,
                         `item_type` varchar(24) NOT NULL,
                         `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;