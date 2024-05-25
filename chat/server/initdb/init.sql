CREATE TABLE ACCOUNTS
(
    user_id       SERIAL PRIMARY KEY,
    user_name     TEXT UNIQUE NOT NULL,
    password_hash INTEGER     NOT NULL
);

CREATE TABLE FILES
(
    file_id      SERIAL PRIMARY KEY,
    user_name    TEXT  NOT NULL,
    file_name    TEXT  NOT NULL,
    file_size    INT   NOT NULL,
    mime_type    TEXT  NOT NULL,
    file_content BYTEA NOT NULL
)