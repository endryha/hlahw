# ALTER TABLE user DROP INDEX index_birthdate;
CREATE INDEX index_birthdate ON user (birthdate) USING BTREE;