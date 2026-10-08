package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @GetMapping("/{id}")
    public Student get(@PathVariable Long id) {
        return studentService.get(id);
    }

    @PutMapping
    public Student update(@RequestBody Student student) {
        return studentService.update(student);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return studentService.delete(id);
    }

    @GetMapping
    public List<Student> getAll() {
        return studentService.getAll();
    }

    @GetMapping("/findByAge")
    public List<Student> findByAge(@RequestParam int age) {
        return studentService.findByAge(age);
    }

    @GetMapping("/findByAgeBetween")
    public List<Student> findByAgeBetween(@RequestParam int min, @RequestParam int max) {
        return studentService.findByAgeBetween(min, max);
    }

    @GetMapping("/{id}/faculty")
    public Optional<Faculty> getFacultyOfStudent(@PathVariable Long id) {
        return studentService.getFacultyOfStudent(id);
    }

    @GetMapping("/count")
    public long count() {
        return studentService.countAll();
    }

    @GetMapping("/averageAge")
    public Double getAverageAge() {
        return studentService.getAverageAge();
    }

    @GetMapping("/lastFive")
    public List<Student> getLastFive() {
        return studentService.getLastFive();
    }

    @GetMapping("/findByNameStartingWithA")
    public List<String> findByNameStartingWithA() {
        return studentService.findByNameStartingWithA().stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .sorted()
                .toList();
    }

    @GetMapping("/sum")
    public int sum() {
        long n = 1_000_000;
        return (int) (n * (n + 1) / 2);
    }

    @GetMapping("/print-parallel")
    public String printParallel() {
        List<Student> students = studentService.getAll();

        // Основные потоки для первых двух студентов
        System.out.println(students.get(0).getName());
        System.out.println(students.get(1).getName());

        // Третий и четвертый студент в параллельном потоке 1
        ForkJoinPool pool = new ForkJoinPool();
        CompletableFuture.runAsync(() -> {
            System.out.println(students.get(2).getName());
            System.out.println(students.get(3).getName());
        }, pool);

        // Пятый и шестой студент в параллельном потоке 2
        CompletableFuture.runAsync(() -> {
            System.out.println(students.get(4).getName());
            System.out.println(students.get(5).getName());
        }, pool);

        // Ждем завершения параллельных задач
        pool.shutdown();
        try {
            pool.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "Names printed to console";
    }

    private synchronized void printStudent(String name) {
        System.out.println(name);
    }

    @GetMapping("/print-synchronized")
    public String printSynchronized() {
        List<Student> students = studentService.getAll();

        // Основные потоки для первых двух студентов
        printStudent(students.get(0).getName());
        printStudent(students.get(1).getName());

        // Третий и четвертый студент в параллельном потоке 1
        ForkJoinPool pool = new ForkJoinPool();
        CompletableFuture.runAsync(() -> {
            printStudent(students.get(2).getName());
            printStudent(students.get(3).getName());
        }, pool);

        // Пятый и шестой студент в параллельном потоке 2
        CompletableFuture.runAsync(() -> {
            printStudent(students.get(4).getName());
            printStudent(students.get(5).getName());
        }, pool);

        // Ждем завершения параллельных задач
        pool.shutdown();
        try {
            pool.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "Names printed to console";
    }
}
