-- Создание таблиц
CREATE TABLE IF NOT EXISTS faculty (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    color VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS student (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INTEGER NOT NULL,
    faculty_id BIGINT REFERENCES faculty(id)
);

CREATE TABLE IF NOT EXISTS avatar (
    id BIGSERIAL PRIMARY KEY,
    file_path VARCHAR(255),
    file_size BIGINT,
    media_type VARCHAR(255),
    data BYTEA,
    student_id BIGINT REFERENCES student(id)
);

-- Вставка факультетов
INSERT INTO faculty (name, color) VALUES
('Гриффиндор', 'Красный и золотой'),
('Слизерин', 'Зелёный и серебряный'),
('Когтевран', 'Коричневый и бронзовый'),
('Пуффендуй', 'Жёлтый и чёрный');

-- Вставка студентов Гриффиндора
INSERT INTO student (name, age, faculty_id) VALUES
('Гарри Поттер', 17, 1),
('Гермиона Грейнджер', 17, 1),
('Рон Уизли', 17, 1),
('Невилл Долори', 17, 1),
('Фред Уизли', 17, 1),
('Джордж Уизли', 17, 1),
('Билл Уизли', 23, 1),
('Чарли Уизли', 21, 1),
('Перси Уизли', 19, 1),
('Молли Уизли', 40, 1);

-- Вставка студентов Слизерина
INSERT INTO student (name, age, faculty_id) VALUES
('Драко Малфой', 17, 2),
('Винсент Крабб', 17, 2),
('Грегори Гойл', 17, 2),
('Парвато Патил', 17, 2),
('Блэйз Забини', 17, 2),
('Милли Cent Хипгуд', 17, 2),
('Теодор Нотт', 17, 2),
('Астрид Понс', 17, 2);

-- Вставка студентов Когтеврана
INSERT INTO student (name, age, faculty_id) VALUES
('Лаванда Браун', 17, 3),
('Дин Томас', 17, 3),
('Сьюзен Бунс', 17, 3),
('Эрин Хилл', 17, 3),
('Коллин Криви', 17, 3),
('Рори Маклейн', 17, 3),
('Майя Кэролл', 17, 3),
('Финли О''Брайен', 17, 3);

-- Вставка студентов Пуффендуя
INSERT INTO student (name, age, faculty_id) VALUES
('Седрик Диггори', 17, 4),
('Эмма Кэролл', 17, 4),
('Оливви Хэмблинг', 17, 4),
('Том Фавершам', 17, 4),
('Марта Пепль', 17, 4),
('Хьюберт Бамфил', 17, 4),
('Джастин Финч-Флетчи', 17, 4),
('Альфред Снелл', 17, 4);
