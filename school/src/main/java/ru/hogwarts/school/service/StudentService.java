package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }

    public Student get(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student update(Student student) {
        if (!studentRepository.existsById(student.getId())) {
            return null;
        }
        return studentRepository.save(student);
    }

    public boolean delete(Long id) {
        if (!studentRepository.existsById(id)) {
            return false;
        }
        studentRepository.deleteById(id);
        return true;
    }

    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public List<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public Optional<Faculty> getFacultyOfStudent(Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(student.getFaculty());
    }

    public long countAll() {
        return studentRepository.countAll();
    }

    public Double getAverageAge() {
        return studentRepository.findAverageAge();
    }

    public List<Student> getLastFive() {
        return studentRepository.findLastFive().stream()
                .limit(5)
                .toList();
    }
}
