package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student create(Student student) {
        log.info("Вызван метод create");
        return studentRepository.save(student);
    }

    public Student get(Long id) {
        log.info("Вызван метод get");
        return studentRepository.findById(id).orElse(null);
    }

    public Student update(Student student) {
        log.info("Вызван метод update");
        if (!studentRepository.existsById(student.getId())) {
            return null;
        }
        return studentRepository.save(student);
    }

    public boolean delete(Long id) {
        log.info("Вызван метод delete");
        if (!studentRepository.existsById(id)) {
            return false;
        }
        studentRepository.deleteById(id);
        return true;
    }

    public List<Student> getAll() {
        log.info("Вызван метод getAll");
        return studentRepository.findAll();
    }

    public List<Student> findByAge(int age) {
        log.info("Вызван метод findByAge");
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        log.info("Вызван метод findByAgeBetween");
        return studentRepository.findByAgeBetween(min, max);
    }

    public Optional<Faculty> getFacultyOfStudent(Long studentId) {
        log.info("Вызван метод getFacultyOfStudent");
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(student.getFaculty());
    }

    public long countAll() {
        log.info("Вызван метод countAll");
        return studentRepository.countAll();
    }

    public Double getAverageAge() {
        log.info("Вызван метод getAverageAge");
        return studentRepository.findAverageAge();
    }

    public List<Student> getLastFive() {
        log.info("Вызван метод getLastFive");
        return studentRepository.findLastFive().stream()
                .limit(5)
                .toList();
    }

    public List<Student> findByNameStartingWithA() {
        log.info("Вызван метод findByNameStartingWithA");
        return studentRepository.findByNameStartingWithA();
    }
}
