drop table if exists transaction;
drop table if exists user_account;
drop table if exists user_account_friends;

create table transaction (
                             transaction_id bigint not null auto_increment,
                             amount decimal(38,2) not null,
                             creditAmount decimal(38,2),
                             description varchar(255) not null,
                             creditor_id bigint not null,
                             debtor_id bigint not null,
                             primary key (transaction_id));

create table user_account (
                              id bigint not null auto_increment,
                              balance decimal(38,2),
                              email varchar(30) not null,
                              password varchar(255) not null,
                              username varchar(255) not null,
                              primary key (id));

create table user_account_friends (
                                      UserAccount_id bigint not null,
                                      friends_id bigint not null);