CREATE TABLE users
(
    id   int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(50) NOT NULL,
    age  int(3) NOT NULL,
    INDEX USING BTREE (age)
);

-- CREATE INDEX name_idx ON users (name) USING BTREE;
-- CREATE INDEX age_idx ON users (age) USING BTREE;