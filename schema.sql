DROP TABLE IF EXISTS booking_players, bookings, profiles, notification_tokens, notifications, refresh_tokens, courts, users CASCADE;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(256) NOT NULL UNIQUE,
    password_hash VARCHAR(256) NOT NULL,
    password_salt VARCHAR(256) NOT NULL,
    is_member BOOLEAN NOT NULL DEFAULT FALSE,
    is_admin BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE profiles (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(256) NOT NULL,
    last_name VARCHAR(256) NOT NULL,
    phone_number VARCHAR(20),
    address VARCHAR(256),
    "user" INT NOT NULL REFERENCES users(id)
);

CREATE TABLE courts (
    id SERIAL PRIMARY KEY,
    name VARCHAR(256) NOT NULL
);

CREATE TABLE refresh_tokens (
    id SERIAL PRIMARY KEY,
    token VARCHAR(512) NOT NULL UNIQUE,
    expires_at BIGINT NOT NULL,
    "user" INT NOT NULL REFERENCES users(id)
);

CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    duration INT NOT NULL,
    "user" INT NOT NULL REFERENCES users(id),
    court INT NOT NULL REFERENCES courts(id)
);

CREATE TABLE booking_players (
    booking INT NOT NULL REFERENCES bookings(id),
    "user" INT NOT NULL REFERENCES users(id),
    PRIMARY KEY (booking, "user")
);

CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    title VARCHAR(256) NOT NULL,
    body TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INT NOT NULL REFERENCES users(id)
);

CREATE TABLE notification_tokens (
    id SERIAL PRIMARY KEY,
    token VARCHAR(256) NOT NULL,
    "user" INT NOT NULL REFERENCES users(id)
);

INSERT INTO courts (name) VALUES
    ('Platz 1'),
    ('Platz 2'),
    ('Platz 3'),
    ('Platz 4'),
    ('Platz 5');
