CREATE TABLE users (
    id      SERIAL PRIMARY KEY,
    name    varchar(50) NOT NULL,
    age     int NOT NULL
);

CREATE INDEX name_idx ON users (name);
CREATE INDEX age_idx ON users (age);