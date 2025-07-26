--changeset egorivanov:1

CREATE SCHEMA IF NOT EXISTS cloud_file_storage;
set search_path = "cloud_file_storage";


CREATE TABLE IF NOT EXISTS Users
(
    ID       SERIAL PRIMARY KEY,
    Username VARCHAR(128) UNIQUE NOT NULL,
    Password VARCHAR(512)        NOT NULL
);


