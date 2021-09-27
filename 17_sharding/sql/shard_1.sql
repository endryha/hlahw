CREATE TABLE books
(
    id          bigint            not null,
    category_id int               not null,
    author      character varying not null,
    title       character varying not null,
    year        int               not null,
    CONSTRAINT category_id_check CHECK ( category_id = 2)
);

CREATE INDEX idx_author ON books USING btree (author);
CREATE INDEX idx_year ON books USING btree (year DESC NULLS LAST);