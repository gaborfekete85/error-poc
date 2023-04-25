CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users(
    user_id uuid NOT NULL,
    name varchar(50),
    email varchar(50) NOT NULL
);