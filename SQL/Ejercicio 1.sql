CREATE DATABASE IF NOT EXISTS pizzeria CHARSET = utf8;
USE pizzeria;

START TRANSACTION;
CREATE TABLE IF NOT EXISTS ingredient (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    UNIQUE(name)
);

CREATE TABLE IF NOT EXISTS user (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    lastName VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS pizza (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    url VARCHAR(255) NOT NULL,
    UNIQUE(name)
);

COMMIT;

START TRANSACTION;
CREATE TABLE IF NOT EXISTS comment (
    id BINARY(16) PRIMARY KEY,
    text TEXT NOT NULL,
    rating FLOAT NOT NULL,
    date CHAR(10) NOT NULL,
    id_user BINARY(16) NOT NULL,
    id_pizza BINARY(16) NOT NULL,
    CONSTRAINT FK_user_comment FOREIGN KEY (id_user) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT FK_pizza_comment FOREIGN KEY (id_pizza) REFERENCES pizza(id) ON DELETE CASCADE
);

CREATE INDEX IX_user_comment ON comment(id_user);

CREATE INDEX IX_pizza_comment ON comment(id_pizza);

COMMIT;

START TRANSACTION;

CREATE TABLE IF NOT EXISTS pizza_ingredient (
    id BINARY(16) PRIMARY KEY,
    id_pizza BINARY(16) NOT NULL,
    id_ingredient BINARY(16) NOT NULL,
    quantity TINYINT DEFAULT 1 NOT NULL,
    FOREIGN KEY (id_pizza) REFERENCES pizza(id) ON DELETE CASCADE,
    FOREIGN KEY (id_ingredient) REFERENCES ingredient(id) ON DELETE CASCADE,
    UNIQUE(id_pizza, id_ingredient)
);

CREATE INDEX IX_pizza_pizza_ingredient ON pizza_ingredient(id_pizza);

CREATE INDEX IX_ingredient_pizza_ingredient ON pizza_ingredient(id_ingredient);

COMMIT;