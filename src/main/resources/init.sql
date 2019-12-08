CREATE DATABASE If NOT EXISTS payment_system;
USE payment_system;
CREATE TABLE IF NOT EXISTS users (
    name VARCHAR(255) PRIMARY KEY NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    authrority_level INT(4) NOT NULL
);

CREATE TABLE IF NOT EXISTS cards (
    id INT(4) PRIMARY KEY NOT NULL,
    number VARCHAR(19) NOT NULL,
    validity VARCHAR(5) NOT NULL,
    secret_number INT(4) NOT NULL,
    account_id INT(4) NOT NULL
);

CREATE TABLE IF NOT EXISTS accounts (
    id INT(4) PRIMARY KEY NOT NULL,
    number VARCHAR(20) NOT NULL,
    sum INT(4) NOT NULL,
    blocked BOOLEAN(4) NOT NULL
);

CREATE TABLE IF NOT EXISTS payments (
    id INT(4) PRIMARY KEY NOT NULL,
    number INT(4) NOT NULL
);

CREATE TABLE IF NOT EXISTS admins (
    name VARCHAR(255) PRIMARY KEY NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    authrority_level INT(4) NOT NULL
);

CREATE TABLE IF NOT EXISTS card_ids (
    id INT(4) NOT NULL,
    FOREIGN KEY (id) REFERENCES users (card_id) ON DELETE CASCADE
);