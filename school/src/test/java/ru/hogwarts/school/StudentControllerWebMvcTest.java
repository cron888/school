package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerWebMvcTest {

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate;

    @MockitoBean
    private StudentService studentService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
    }

    @Test
    void testCreateStudent() throws Exception {
        Student student = new Student();
        student.setName("Ivan Petrov");
        student.setAge(20);

        Student savedStudent = new Student(1L, "Ivan Petrov", 20);
        when(studentService.create(any(Student.class))).thenReturn(savedStudent);

        String url = "http://localhost:" + port + "/student";
        String response = restTemplate.postForObject(url, student, String.class);
        assertThat(response).contains("\"name\":\"Ivan Petrov\"");
        assertThat(response).contains("\"age\":20");
        assertThat(response).contains("\"id\":1");
    }

    @Test
    void testGetStudent() throws Exception {
        Student student = new Student(1L, "Ivan Petrov", 20);
        when(studentService.get(1L)).thenReturn(student);

        String url = "http://localhost:" + port + "/student/{id}";
        String response = restTemplate.getForObject(url, String.class, 1L);
        assertThat(response).contains("\"name\":\"Ivan Petrov\"");
        assertThat(response).contains("\"age\":20");
    }

    @Test
    void testUpdateStudent() throws Exception {
        Student updatedStudent = new Student(1L, "Ivan Ivanov", 25);
        when(studentService.update(any(Student.class))).thenReturn(updatedStudent);

        Student request = new Student(1L, "Ivan Ivanov", 25);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = "http://localhost:" + port + "/student";
        String response = restTemplate.exchange(url, HttpMethod.PUT,
                new HttpEntity<>(request, headers), String.class).getBody();

        assertThat(response).contains("\"name\":\"Ivan Ivanov\"");
        assertThat(response).contains("\"age\":25");
    }

    @Test
    void testDeleteStudent() throws Exception {
        when(studentService.delete(1L)).thenReturn(true);

        String url = "http://localhost:" + port + "/student/{id}";
        String response = restTemplate.exchange(url, HttpMethod.DELETE,
                null, String.class, 1L).getBody();

        assertThat(response).isEqualTo("true");
    }

    @Test
    void testGetAllStudents() throws Exception {
        List<Student> students = List.of(
                new Student(1L, "Ivan Petrov", 20),
                new Student(2L, "Maria Ivanova", 22)
        );
        when(studentService.getAll()).thenReturn(students);

        String url = "http://localhost:" + port + "/student";
        String response = restTemplate.getForObject(url, String.class);
        assertThat(response).contains("\"name\":\"Ivan Petrov\"");
        assertThat(response).contains("\"name\":\"Maria Ivanova\"");
    }

    @Test
    void testFindByAge() throws Exception {
        List<Student> students = List.of(new Student(1L, "Ivan Petrov", 20));
        when(studentService.findByAge(20)).thenReturn(students);

        String url = "http://localhost:" + port + "/student/findByAge?age=20";
        String response = restTemplate.getForObject(url, String.class);
        assertThat(response).contains("\"name\":\"Ivan Petrov\"");
    }

    @Test
    void testFindByAgeBetween() throws Exception {
        List<Student> students = List.of(new Student(1L, "Maria Ivanova", 22));
        when(studentService.findByAgeBetween(20, 24)).thenReturn(students);

        String url = "http://localhost:" + port + "/student/findByAgeBetween?min=20&max=24";
        String response = restTemplate.getForObject(url, String.class);
        assertThat(response).contains("\"name\":\"Maria Ivanova\"");
    }

    @Test
    void testGetFacultyOfStudent() throws Exception {
        when(studentService.getFacultyOfStudent(1L)).thenReturn(java.util.Optional.empty());

        String url = "http://localhost:" + port + "/student/{id}/faculty";
        String response = restTemplate.getForObject(url, String.class, 1L);
        assertThat(response).isEqualTo("null");
    }
}
