SET timezone = 'Europe/Bucharest';

CREATE TYPE station_category AS ENUM (
    'GARA_PRINCIPALA',
    'STATION',
    'HALTA',
    'POPAS_FEROVIAR'
);

CREATE TYPE train_category AS ENUM (
    'R',
    'IR'
);

CREATE TYPE user_category AS ENUM (
    'CUSTOMER',
    'ADMIN'
);

CREATE TABLE stations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    city VARCHAR(255) NOT NULL,
    type station_category NOT NULL DEFAULT 'STATION'
);

CREATE TABLE routes (
    id SERIAL PRIMARY KEY,
    route_name VARCHAR(255) NOT NULL
);

CREATE TABLE trains (
    id SERIAL PRIMARY KEY,
    train_number VARCHAR(50) NOT NULL UNIQUE,
    train_type train_category NOT NULL,
    route_id INT REFERENCES routes(id) ON DELETE SET NULL,
    total_capacity INT NOT NULL,
    delay_minutes INT DEFAULT 0
);

CREATE TABLE route_stations (
    id SERIAL PRIMARY KEY,
    route_id INT REFERENCES routes(id) ON DELETE CASCADE,
    station_id INT REFERENCES stations(id) ON DELETE CASCADE,
    stop_order INT NOT NULL,
    arrival_time TIME,
    departure_time TIME,
    UNIQUE (route_id, station_id),
    UNIQUE (route_id, stop_order)
);

CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    train_id INT NOT NULL REFERENCES trains(id),
    customer_email VARCHAR(255) NOT NULL,
    seats_booked INT NOT NULL,
    start_station_id INT NOT NULL REFERENCES stations(id),
    end_station_id INT NOT NULL REFERENCES stations(id),
    travel_date DATE NOT NULL,
    booking_date TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role user_category DEFAULT 'CUSTOMER'
);