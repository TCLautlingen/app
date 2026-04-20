DROP TABLE IF EXISTS booking, refresh_token, court, "user" CASCADE;

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
    id SERIAL PRIMARY KEY,
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

CREATE TABLE device (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES "user"(id),
    device_unique_id VARCHAR(256) NOT NULL UNIQUE,
    notification_token VARCHAR(256) NOT NULL
);

CREATE TABLE notification (
    id SERIAL PRIMARY KEY,
    title VARCHAR(256) NOT NULL,
    body TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INT NOT NULL REFERENCES "user"(id)
);

INSERT INTO court (name) VALUES
    ('Platz 1'),
    ('Platz 2'),
    ('Platz 3'),
    ('Platz 4'),
    ('Platz 5');