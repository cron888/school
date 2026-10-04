-- Шаг 3. JOIN-запросы

-- Получить всех студентов вместе с названиями факультетов (имя и возраст)
SELECT s.name, s.age, f.name AS faculty_name
FROM student s
JOIN faculty f ON s.faculty_id = f.id;

-- Получить студентов, у которых есть аватарки
SELECT s.name, s.age, a.file_path, a.media_type
FROM student s
JOIN avatar a ON s.id = a.student_id;
