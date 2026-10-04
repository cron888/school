-- Шаг 2. Таблицы: Человек и Машина

CREATE TABLE person (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INTEGER,
    has_license BOOLEAN DEFAULT FALSE
);

CREATE TABLE car (
    id BIGSERIAL PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    price NUMERIC(12, 2) NOT NULL
);

-- Несколько человек могут пользоваться одной машиной
ALTER TABLE person ADD COLUMN car_id BIGINT REFERENCES car(id);
