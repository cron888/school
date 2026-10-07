package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    List<Faculty> findByColor(String color);

    List<Faculty> findByColorIgnoreCaseOrNameIgnoreCase(String color, String name);

    default Faculty findLongestName() {
        List<Faculty> faculties = findAll();
        return faculties.stream()
                .max((f1, f2) -> Integer.compare(f1.getName().length(), f2.getName().length()))
                .orElse(null);
    }
}
