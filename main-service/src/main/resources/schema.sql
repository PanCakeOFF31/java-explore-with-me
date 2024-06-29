-- ==============================================================================
--DROP TABLE IF EXISTS person CASCADE;
--DROP TABLE IF EXISTS category CASCADE;
--DROP TABLE IF EXISTS location CASCADE;
--DROP TABLE IF EXISTS event CASCADE;
--DROP TABLE IF EXISTS compilation CASCADE;
--DROP TABLE IF EXISTS event_compilation CASCADE;
--DROP TABLE IF EXISTS request CASCADE;
--DROP TABLE IF EXISTS event_like CASCADE;
--DROP TABLE IF EXISTS event_rating CASCADE;
-- ==============================================================================
-- Для h2 запросы, так как он '\n\ распознает как конец запроса
--create TABLE IF NOT EXISTS person (id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, name varchar(250) NOT NULL CHECK(LENGTH(name) >= 2), email varchar(254) NOT NULL UNIQUE CHECK(LENGTH(email) >= 6 AND email ~ ('^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z0-9.-]+$')));
--create TABLE IF NOT EXISTS category (id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, name varchar(50) NOT NULL UNIQUE CHECK(LENGTH(name) >= 1));
--create TABLE IF NOT EXISTS location (id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, latitude float4 NOT NULL, longitude float4 NOT NULL, UNIQUE (latitude, longitude));
--create TABLE IF NOT EXISTS event (id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, category_id bigint REFERENCES category(id) ON delete RESTRICT NOT NULL, initiator_id bigint REFERENCES person(id) ON delete CASCADE NOT NULL, location_id bigint REFERENCES location(id) ON delete RESTRICT NOT NULL, title varchar(120) NOT NULL CHECK(LENGTH(title) >= 3), annotation varchar(2000) NOT NULL CHECK(LENGTH(annotation) >= 20), description varchar(7000) NOT NULL CHECK(LENGTH(description) >= 20), created_on timestamp WITHOUT TIME ZONE NOT NULL, event_date timestamp WITHOUT TIME ZONE NOT NULL, published_on timestamp WITHOUT TIME ZONE NULL, paid boolean DEFAULT false, participant_limit int DEFAULT 0, confirmed_requests int DEFAULT 0, request_moderation boolean DEFAULT true, state varchar(9) NOT NULL DEFAULT 'PENDING' , views bigint DEFAULT 0, likes bigint DEFAULT 0, dislikes bigint DEFAULT 0, rating float(2) DEFAULT 0.0);
--create TABLE IF NOT EXISTS compilation (id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, title varchar(50) NOT NULL UNIQUE CHECK(LENGTH(title) >= 1), pinned boolean DEFAULT false);
--create TABLE IF NOT EXISTS event_compilation ( id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, event_id bigint REFERENCES event(id) ON delete CASCADE NOT NULL, compilation_id bigint REFERENCES compilation(id) ON delete CASCADE NOT NULL, UNIQUE (event_id, compilation_id));
--create TABLE IF NOT EXISTS request ( id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, event_id bigint REFERENCES event(id) ON delete CASCADE NOT NULL, requester_id bigint REFERENCES person(id) ON delete CASCADE NOT NULL, created_on timestamp WITHOUT TIME ZONE NOT NULL, status varchar(9) NOT NULL, UNIQUE (requester_id, created_on));
--create TABLE IF NOT EXISTS event_like ( user_id bigint REFERENCES person(id) ON delete CASCADE NOT NULL, event_id bigint REFERENCES event(id) ON delete CASCADE NOT NULL, is_like boolean NOT NULL, PRIMARY KEY (user_id, event_id));
--create TABLE IF NOT EXISTS event_rating ( user_id bigint REFERENCES person(id) ON delete CASCADE NOT NULL, event_id bigint REFERENCES event(id) ON delete CASCADE NOT NULL, rating int NOT NULL CHECK(rating BETWEEN 0 AND 10), PRIMARY KEY (user_id, event_id));
-- ==============================================================================
CREATE TABLE IF NOT EXISTS person (
	id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name varchar(250) NOT NULL CHECK(LENGTH(name) >= 2),
	email varchar(254) NOT NULL UNIQUE CHECK(LENGTH(email) >= 6 AND email ~ ('^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z0-9.-]+$'))
);

CREATE TABLE IF NOT EXISTS category (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name varchar(50) NOT NULL UNIQUE CHECK(LENGTH(name) >= 1)
);

CREATE TABLE IF NOT EXISTS location (
	id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    latitude float4 NOT NULL,
    longitude float4 NOT NULL,
    UNIQUE (latitude, longitude)
);

CREATE TABLE IF NOT EXISTS event (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category_id bigint REFERENCES category(id) ON DELETE RESTRICT NOT NULL,
    initiator_id bigint REFERENCES person(id) ON DELETE RESTRICT NOT NULL,
    location_id bigint REFERENCES location(id) ON DELETE RESTRICT NOT NULL,
	title varchar(120) NOT NULL CHECK(LENGTH(title) >= 3),
	annotation varchar(2000) NOT NULL CHECK(LENGTH(annotation) >= 20),
	description varchar(7000) NOT NULL CHECK(LENGTH(description) >= 20),
    created_on timestamp WITHOUT TIME ZONE NOT NULL,
    event_date timestamp WITHOUT TIME ZONE NOT NULL,
    published_on timestamp WITHOUT TIME ZONE NULL,
    paid boolean DEFAULT false,
    participant_limit int DEFAULT 0,
    confirmed_requests int DEFAULT 0,
    request_moderation boolean DEFAULT true,
    state varchar(9) NOT NULL DEFAULT 'PENDING',
    views bigint DEFAULT 0,
    likes bigint DEFAULT 0,
    dislikes bigint DEFAULT 0,
    rating float(2) DEFAULT 0.0
);

CREATE TABLE IF NOT EXISTS compilation (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	title varchar(50) NOT NULL UNIQUE CHECK(LENGTH(title) >= 1),
    pinned boolean DEFAULT false
);

CREATE TABLE IF NOT EXISTS event_compilation (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id bigint REFERENCES event(id) ON DELETE RESTRICT NOT NULL,
    compilation_id bigint REFERENCES compilation(id) ON DELETE RESTRICT NOT NULL,
    UNIQUE (event_id, compilation_id)
);

CREATE TABLE IF NOT EXISTS request (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id bigint REFERENCES event(id) ON DELETE RESTRICT NOT NULL,
    requester_id bigint REFERENCES person(id) ON DELETE RESTRICT NOT NULL,
    created_on timestamp WITHOUT TIME ZONE NOT NULL,
    status varchar(9) NOT NULL DEFAULT 'PENDING',
    UNIQUE (requester_id, created_on)
);

CREATE TABLE IF NOT EXISTS event_like (
    user_id bigint REFERENCES person(id) ON DELETE RESTRICT NOT NULL,
    event_id bigint REFERENCES event(id) ON DELETE RESTRICT NOT NULL,
    is_like boolean NOT NULL,
    PRIMARY KEY (user_id, event_id)
);

CREATE TABLE IF NOT EXISTS event_rating (
    user_id bigint REFERENCES person(id) ON DELETE RESTRICT NOT NULL,
    event_id bigint REFERENCES event(id) ON DELETE RESTRICT NOT NULL,
    rating int NOT NULL CHECK(rating BETWEEN 0 AND 10),
    PRIMARY KEY (user_id, event_id)
);
-- ==============================================================================
--DELETE FROM person;
--DELETE FROM category;
--DELETE FROM location;
--DELETE FROM event;
--DELETE FROM compilation;
--DELETE FROM event_compilation;
--DELETE FROM request;
--DELETE FROM event_like;
--DELETE FROM event_rating;
-- ==============================================================================
--ALTER TABLE person ALTER id RESTART;
--ALTER TABLE category ALTER id RESTART;
--ALTER TABLE location ALTER id RESTART;
--ALTER TABLE event ALTER id RESTART;
--ALTER TABLE compilation ALTER id RESTART;
--ALTER TABLE event_compilation ALTER id RESTART;
--ALTER TABLE request ALTER id RESTART;