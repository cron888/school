package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final Map<Long, Faculty> faculties = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    public Faculty create(Faculty faculty) {
        Long id = idCounter.incrementAndGet();
        faculty.setId(id);
        faculties.put(id, faculty);
        return faculty;
    }

    public Faculty get(Long id) {
        return faculties.get(id);
    }

    public Faculty update(Faculty faculty) {
        if (!faculties.containsKey(faculty.getId())) {
            return null;
        }
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public boolean delete(Long id) {
        return faculties.remove(id) != null;
    }

    public List<Faculty> getAll() {
        return new ArrayList<>(faculties.values());
    }

    public List<Faculty> findByColor(String color) {
        return faculties.values().stream()
                .filter(f -> f.getColor().equals(color))
                .collect(Collectors.toList());
    }
}
