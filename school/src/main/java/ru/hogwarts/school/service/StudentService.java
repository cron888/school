package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final Map<Long, Student> students = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    public Student create(Student student) {
        Long id = idCounter.incrementAndGet();
        student.setId(id);
        students.put(id, student);
        return student;
    }

    public Student get(Long id) {
        return students.get(id);
    }

    public Student update(Student student) {
        if (!students.containsKey(student.getId())) {
            return null;
        }
        students.put(student.getId(), student);
        return student;
    }

    public boolean delete(Long id) {
        return students.remove(id) != null;
    }

    public List<Student> getAll() {
        return new ArrayList<>(students.values());
    }

    public List<Student> findByAge(int age) {
        return students.values().stream()
                .filter(s -> s.getAge() == age)
                .collect(Collectors.toList());
    }
}
