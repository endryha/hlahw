CREATE TABLE users
(
    id   int(11) NOT NULL AUTO_INCREMENT,
    name varchar(50) NOT NULL,
    age  int(3) NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX name_idx ON users (name) USING BTREE;
CREATE INDEX age_idx ON users (age) USING BTREE;