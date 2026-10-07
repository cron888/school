package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

@Service
public class FacultyService {

    private static final Logger log = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty create(Faculty faculty) {
        log.info("Вызван метод create");
        return facultyRepository.save(faculty);
    }

    public Faculty get(Long id) {
        log.info("Вызван метод get");
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty update(Faculty faculty) {
        log.info("Вызван метод update");
        if (!facultyRepository.existsById(faculty.getId())) {
            return null;
        }
        return facultyRepository.save(faculty);
    }

    public boolean delete(Long id) {
        log.info("Вызван метод delete");
        if (!facultyRepository.existsById(id)) {
            return false;
        }
        facultyRepository.deleteById(id);
        return true;
    }

    public List<Faculty> getAll() {
        log.info("Вызван метод getAll");
        return facultyRepository.findAll();
    }

    public List<Faculty> findByColor(String color) {
        log.info("Вызван метод findByColor");
        return facultyRepository.findByColor(color);
    }

    public List<Faculty> search(String query) {
        log.info("Вызван метод search");
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(query, query);
    }

    public List<Student> getStudentsOfFaculty(Long facultyId) {
        log.info("Вызван метод getStudentsOfFaculty");
        Faculty faculty = facultyRepository.findById(facultyId).orElse(null);
        if (faculty == null) {
            return List.of();
        }
        return faculty.getStudents() != null ? faculty.getStudents() : List.of();
    }

    public Faculty findLongestName() {
        log.info("Вызван метод findLongestName");
        return facultyRepository.findLongestName();
    }
}
