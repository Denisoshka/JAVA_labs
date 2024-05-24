CREATE TABLE accounts
(
    user_id    SERIAL PRIMARY KEY,
    username   TEXT UNIQUE NOT NULL,
    password   TEXT        NOT NULL,
    email      TEXT UNIQUE NOT NULL,
    created_at TIMESTAMP   NOT NULL,
    last_login TIMESTAMP
);