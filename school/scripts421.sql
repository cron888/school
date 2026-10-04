-- Шаг 1. Ограничения для таблиц Student и Faculty

-- Возраст студента не может быть меньше 16 лет
ALTER TABLE student ADD CONSTRAINT chk_student_age_min CHECK (age >= 16);

-- Имена студентов должны быть уникальными и не равны нулю
ALTER TABLE student ADD CONSTRAINT uk_student_name UNIQUE (name);
ALTER TABLE student ADD CONSTRAINT chk_student_name_not_null CHECK (name IS NOT NULL AND name <> '');

-- Пара "название" - "цвет факультета" должна быть уникальной
ALTER TABLE faculty ADD CONSTRAINT uk_faculty_name_color UNIQUE (name, color);

-- При создании студента без возраста ему автоматически присваивается 20 лет
ALTER TABLE student ALTER COLUMN age SET DEFAULT 20;
