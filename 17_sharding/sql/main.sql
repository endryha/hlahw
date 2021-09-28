-- Create tables on the main server
CREATE SEQUENCE books_seq START 1;

CREATE TABLE books_1
(
    id          bigint            not null,
    category_id int               not null,
    author      character varying not null,
    title       character varying not null,
    year        int               not null,
    CONSTRAINT category_id_check CHECK ( category_id = 1 )
);

CREATE INDEX idx_category ON books_1 USING btree (category_id);
CREATE INDEX idx_author ON books_1 USING btree (author);
CREATE INDEX idx_year ON books_1 USING btree (year);

CREATE TABLE books_others
(
    id          bigint            not null,
    category_id int               not null,
    author      character varying not null,
    title       character varying not null,
    year        int               not null
);

CREATE INDEX idx_category_others ON books_others USING btree (category_id);
CREATE INDEX idx_author_others ON books_others USING btree (author);
CREATE INDEX idx_year_others ON books_others USING btree (year);

-- enabled FDW extension
CREATE
EXTENSION postgres_fdw;

-- Create foreign servers
CREATE
SERVER shard_1
FOREIGN DATA WRAPPER postgres_fdw
OPTIONS( host 'shard_1', port '5432', dbname 'hla');

CREATE
SERVER shard_2
FOREIGN DATA WRAPPER postgres_fdw
OPTIONS( host 'shard_2', port '5432', dbname 'hla');

CREATE
USER MAPPING FOR postgres SERVER shard_1 OPTIONS (user 'postgres', password 'postgres');

CREATE
USER MAPPING FOR postgres SERVER shard_2 OPTIONS (user 'postgres', password 'postgres');

-- Create foreign tables
CREATE
FOREIGN TABLE books_2
(
id bigint not null,
category_id  int not null,
author character varying not null,
title character varying not null,
year int not null
)
SERVER shard_1
OPTIONS (schema_name 'public', table_name 'books');

CREATE
FOREIGN TABLE books_3
(
id bigint not null,
category_id  int not null,
author character varying not null,
title character varying not null,
year int not null
)
SERVER shard_2
OPTIONS (schema_name 'public', table_name 'books');

CREATE VIEW books AS
SELECT *
FROM books_1
UNION ALL
SELECT *
FROM books_2
UNION ALL
SELECT *
FROM books_3;

-- Empty rules by default
CREATE
RULE books_insert AS ON
INSERT TO books
DO INSTEAD NOTHING;

CREATE
RULE books_update AS ON
UPDATE TO books
    DO INSTEAD NOTHING;

CREATE
RULE books_delete AS ON
DELETE
TO books
DO INSTEAD NOTHING;

-- Routing rules
-- Insert rules
CREATE
RULE books_insert_to_1 AS ON INSERT TO books
WHERE ( category_id = 1 )
DO INSTEAD INSERT INTO books_1 VALUES (NEW.*);

CREATE
RULE books_insert_to_2 AS ON INSERT TO books
WHERE ( category_id = 2 )
DO INSTEAD INSERT INTO books_2 VALUES (NEW.*);

CREATE
RULE books_insert_to_3 AS ON INSERT TO books
WHERE ( category_id = 3 )
DO INSTEAD INSERT INTO books_3 VALUES (NEW.*);

-- Unexpected category rule
CREATE
RULE books_insert_to_others AS ON INSERT TO books
WHERE ( category_id < 1 or category_id > 3 )
DO INSTEAD INSERT INTO books_others VALUES (NEW.*);

-- Update rules
CREATE
RULE books_update_to_1 AS ON
UPDATE TO books
WHERE (OLD.category_id = 1) DO INSTEAD
UPDATE books_1
SET author = NEW.author,
    title  = NEW.title,
    year   = NEW.year
WHERE id = OLD.id;

CREATE
RULE books_update_to_2 AS ON
UPDATE TO books
WHERE (OLD.category_id = 2) DO INSTEAD
UPDATE books_2
SET author = NEW.author,
    title  = NEW.title,
    year   = NEW.year
WHERE id = OLD.id;

CREATE
RULE books_update_to_3 AS ON
UPDATE TO books
WHERE (OLD.category_id = 3) DO INSTEAD
UPDATE books_3
SET author = NEW.author,
    title  = NEW.title,
    year   = NEW.year
WHERE id = OLD.id;

-- Delete rules
CREATE
RULE books_delete_to_1 AS ON DELETE
TO books
WHERE ( category_id = 1 )
DO INSTEAD
DELETE
FROM books_1
WHERE id = OLD.id;

CREATE
RULE books_delete_to_2 AS ON DELETE
TO books
WHERE ( category_id = 2 )
DO INSTEAD
DELETE
FROM books_2
WHERE id = OLD.id;

CREATE
RULE books_delete_to_3 AS ON DELETE
TO books
WHERE ( category_id = 3 )
DO INSTEAD
DELETE
FROM books_3
WHERE id = OLD.id;