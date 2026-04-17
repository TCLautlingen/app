CREATE TABLE "user" (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    password_salt VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    is_member BOOLEAN NOT NULL DEFAULT FALSE,
    is_admin BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE court (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE refresh_token (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id INT NOT NULL REFERENCES "user"(id),
    token VARCHAR(512) NOT NULL UNIQUE,
    expires_at BIGINT NOT NULL
);

CREATE TABLE booking (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES "user"(id),
    court_id INT NOT NULL REFERENCES court(id),
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    duration INT NOT NULL
);

INSERT INTO court (name) VALUES
    ('Court 1'),
    ('Court 2'),
    ('Court 3'),
    ('Court 4'),
    ('Court 5');