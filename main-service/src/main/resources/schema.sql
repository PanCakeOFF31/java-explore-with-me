-- ==============================================================================
drop table IF EXISTS person CASCADE;
drop table IF EXISTS category CASCADE;
drop table IF EXISTS location CASCADE;
drop table IF EXISTS event CASCADE;
drop table IF EXISTS compilation CASCADE;
drop table IF EXISTS event_compilation CASCADE;
drop table IF EXISTS request CASCADE;
-- ==============================================================================
-- Для h2 запросы, так как он '\n\ распознает как конец запроса
--create TABLE IF NOT EXISTS person (id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, name varchar(250) NOT NULL CHECK(LENGTH(name) >= 2), email varchar(254) NOT NULL UNIQUE CHECK(LENGTH(email) >= 6 AND email ~ ('^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z0-9.-]+$')));
--create TABLE IF NOT EXISTS category (id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, name varchar(50) NOT NULL UNIQUE CHECK(LENGTH(name) >= 1));
--create TABLE IF NOT EXISTS location (id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, latitude float4 NOT NULL, longitude float4 NOT NULL, UNIQUE (latitude, longitude));
--create TABLE IF NOT EXISTS event (id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, category_id bigint REFERENCES category(id) ON delete RESTRICT NOT NULL, initiator_id bigint REFERENCES person(id) ON delete CASCADE NOT NULL, location_id bigint REFERENCES location(id) ON delete RESTRICT NOT NULL, title varchar(120) NOT NULL CHECK(LENGTH(title) >= 3), annotation varchar(2000) NOT NULL CHECK(LENGTH(annotation) >= 20), description varchar(7000) NOT NULL CHECK(LENGTH(description) >= 20), created_on timestamp WITHOUT TIME ZONE NOT NULL, event_date timestamp WITHOUT TIME ZONE NOT NULL, published_on timestamp WITHOUT TIME ZONE NULL, paid boolean DEFAULT false, participant_limit int DEFAULT 0, confirmed_requests int DEFAULT 0, request_moderation boolean DEFAULT true, state varchar(9) NOT NULL DEFAULT 'PENDING' , views bigint DEFAULT 0);
--create TABLE IF NOT EXISTS compilation (id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, title varchar(50) NOT NULL UNIQUE CHECK(LENGTH(title) >= 1), pinned boolean DEFAULT false);
--create TABLE IF NOT EXISTS event_compilation ( id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, event_id bigint REFERENCES event(id) ON delete CASCADE NOT NULL, compilation_id bigint REFERENCES compilation(id) ON delete CASCADE NOT NULL, UNIQUE (event_id, compilation_id));
--create TABLE IF NOT EXISTS request ( id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, event_id bigint REFERENCES event(id) ON delete CASCADE NOT NULL, requester_id bigint REFERENCES person(id) ON delete CASCADE NOT NULL, created_on timestamp WITHOUT TIME ZONE NOT NULL, status varchar(9) NOT NULL, UNIQUE (requester_id, created_on));

create TABLE IF NOT EXISTS person (
	id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name varchar(250) NOT NULL CHECK(LENGTH(name) >= 2),
	email varchar(254) NOT NULL UNIQUE CHECK(LENGTH(email) >= 6 AND email ~ ('^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z0-9.-]+$'))
);

create TABLE IF NOT EXISTS category (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name varchar(50) NOT NULL UNIQUE CHECK(LENGTH(name) >= 1)
);

create TABLE IF NOT EXISTS location (
	id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    latitude float4 NOT NULL,
    longitude float4 NOT NULL,
    UNIQUE (latitude, longitude)
);

create TABLE IF NOT EXISTS event (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category_id bigint REFERENCES category(id) ON delete RESTRICT NOT NULL,
    initiator_id bigint REFERENCES person(id) ON delete CASCADE NOT NULL,
    location_id bigint REFERENCES location(id) ON delete RESTRICT NOT NULL,
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
    views bigint DEFAULT 0
);

create TABLE IF NOT EXISTS compilation (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	title varchar(50) NOT NULL UNIQUE CHECK(LENGTH(title) >= 1),
    pinned boolean DEFAULT false
);

create TABLE IF NOT EXISTS event_compilation (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id bigint REFERENCES event(id) ON delete CASCADE NOT NULL,
    compilation_id bigint REFERENCES compilation(id) ON delete CASCADE NOT NULL,
    UNIQUE (event_id, compilation_id)
);

create TABLE IF NOT EXISTS request (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id bigint REFERENCES event(id) ON delete CASCADE NOT NULL,
    requester_id bigint REFERENCES person(id) ON delete CASCADE NOT NULL,
    created_on timestamp WITHOUT TIME ZONE NOT NULL,
    status varchar(9) NOT NULL,
    UNIQUE (requester_id, created_on)
);
-- ==============================================================================
delete from person;
delete from category;
delete from location;
delete from event;
delete from compilation;
delete from event_compilation;
delete from request;
-- ==============================================================================
alter table person alter id RESTART;
alter table category alter id RESTART;
alter table location alter id RESTART;
alter table event alter id RESTART;
alter table compilation alter id RESTART;
alter table event_compilation alter id RESTART;
alter table request alter id RESTART;
-- ==============================================================================