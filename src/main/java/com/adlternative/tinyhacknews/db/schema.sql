CREATE TABLE IF NOT EXISTS  Users (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      username VARCHAR(50) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      email VARCHAR(50) NOT NULL,
                      createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updatedAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS  News (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    url VARCHAR(255) NOT NULL,
    text LONGTEXT NOT NULL,
    authorId BIGINT NOT NULL,
    createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS Comments (
                                     id INT PRIMARY KEY AUTO_INCREMENT,
                                     text LONGTEXT NOT NULL,
                                     parentCommentId BIGINT NULL,
                                     newsId BIGINT NOT NULL,
                                     authorId BIGINT NOT NULL,
                                     createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     updatedAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;