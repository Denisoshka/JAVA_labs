CREATE TABLE ACCOUNTS
(
    user_id          SERIAL PRIMARY KEY,
    user_name        TEXT UNIQUE NOT NULL,
    password_hash    INTEGER     NOT NULL,
    avatar_mime_type TEXT,
    avatar           BYTEA
);

CREATE TABLE FILES
(
    file_id      SERIAL PRIMARY KEY,
    user_name    TEXT  NOT NULL,
    file_name    TEXT  NOT NULL,
    file_size    INT   NOT NULL,
    mime_type    TEXT  NULL,
    file_content BYTEA NOT NULL
)