-- ==============================================================================
DROP TABLE IF EXISTS app CASCADE;
DROP TABLE IF EXISTS stat CASCADE;
-- ==============================================================================
CREATE TABLE IF NOT EXISTS app (id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, app varchar(128) NOT NULL UNIQUE CHECK(LENGTH(app) >= 3));
CREATE TABLE IF NOT EXISTS stat ( id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, app_id bigint REFERENCES app(id) ON DELETE CASCADE NOT NULL, uri varchar(256) NOT NULL CHECK(LENGTH(uri) >= 1), ip varchar(45) NOT NULL CHECK(LENGTH(ip) >= 7), requested timestamp WITHOUT TIME ZONE NOT NULL);
-- ==============================================================================
DELETE FROM app;
DELETE FROM stat;
ALTER TABLE stat ALTER id RESTART;
ALTER TABLE app ALTER id RESTART;
-- ==============================================================================