package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty create(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty get(Long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty update(Faculty faculty) {
        if (!facultyRepository.existsById(faculty.getId())) {
            return null;
        }
        return facultyRepository.save(faculty);
    }

    public boolean delete(Long id) {
        if (!facultyRepository.existsById(id)) {
            return false;
        }
        facultyRepository.deleteById(id);
        return true;
    }

    public List<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    public List<Faculty> findByColor(String color) {
        return facultyRepository.findByColor(color);
    }

    public List<Faculty> search(String query) {
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(query, query);
    }

    public List<Student> getStudentsOfFaculty(Long facultyId) {
        Faculty faculty = facultyRepository.findById(facultyId).orElse(null);
        if (faculty == null) {
            return List.of();
        }
        return faculty.getStudents() != null ? faculty.getStudents() : List.of();
    }
}
