drop table if exists transaction;
drop table if exists user_account;
drop table if exists user_account_friends;

CREATE TABLE user_account (
id BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
email VARCHAR(30) NOT NULL,
username VARCHAR(255) NOT NULL,
password VARCHAR(255) NOT NULL,
balance DECIMAL(19,2) DEFAULT '0.00');
CREATE TABLE transaction (
transaction_id BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
amount DECIMAL(19,2),
credit_amount DECIMAL(19,2),
description VARCHAR(255),
creditor_id BIGINT(20),
debtor_id BIGINT(20));
CREATE TABLE user_account_friends (
user_account_id BIGINT(20) NOT NULL,
friends_id BIGINT(20) NOT NULL,
PRIMARY KEY (user_account_id, friends_id),
CONSTRAINT fk_user_account
FOREIGN KEY (user_account_id)
REFERENCES user_account (id)
ON DELETE CASCADE,
CONSTRAINT fk_friends
FOREIGN KEY (friends_id)
REFERENCES user_account (id)
ON DELETE CASCADE
);