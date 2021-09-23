create table superhero
(
    id    bigint not null auto_increment,
    name  varchar(255) NOT NULL,
    power varchar(255) NOT NULL,
    birthdate DATE NOT NULL,
    primary key (id),
    index using BTREE (name),
    index using BTREE (birthdate)
);