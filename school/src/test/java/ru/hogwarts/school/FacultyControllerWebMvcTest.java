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
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerWebMvcTest {

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate;

    @MockitoBean
    private FacultyService facultyService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
    }

    @Test
    void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Potions");
        faculty.setColor("Black");

        Faculty savedFaculty = new Faculty(1L, "Potions", "Black");
        when(facultyService.create(any(Faculty.class))).thenReturn(savedFaculty);

        String url = "http://localhost:" + port + "/faculty";
        String response = restTemplate.postForObject(url, faculty, String.class);
        assertThat(response).contains("\"name\":\"Potions\"");
        assertThat(response).contains("\"color\":\"Black\"");
        assertThat(response).contains("\"id\":1");
    }

    @Test
    void testGetFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Potions", "Black");
        when(facultyService.get(1L)).thenReturn(faculty);

        String url = "http://localhost:" + port + "/faculty/{id}";
        String response = restTemplate.getForObject(url, String.class, 1L);
        assertThat(response).contains("\"name\":\"Potions\"");
        assertThat(response).contains("\"color\":\"Black\"");
    }

    @Test
    void testUpdateFaculty() throws Exception {
        Faculty updatedFaculty = new Faculty(1L, "Defense Against Dark Arts", "White");
        when(facultyService.update(any(Faculty.class))).thenReturn(updatedFaculty);

        Faculty request = new Faculty(1L, "Defense Against Dark Arts", "White");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = "http://localhost:" + port + "/faculty";
        String response = restTemplate.exchange(url, HttpMethod.PUT,
                new HttpEntity<>(request, headers), String.class).getBody();

        assertThat(response).contains("\"name\":\"Defense Against Dark Arts\"");
        assertThat(response).contains("\"color\":\"White\"");
    }

    @Test
    void testDeleteFaculty() throws Exception {
        when(facultyService.delete(1L)).thenReturn(true);

        String url = "http://localhost:" + port + "/faculty/{id}";
        String response = restTemplate.exchange(url, HttpMethod.DELETE,
                null, String.class, 1L).getBody();

        assertThat(response).isEqualTo("true");
    }

    @Test
    void testGetAllFaculties() throws Exception {
        List<Faculty> faculties = List.of(
                new Faculty(1L, "Potions", "Black"),
                new Faculty(2L, "Transfiguration", "White")
        );
        when(facultyService.getAll()).thenReturn(faculties);

        String url = "http://localhost:" + port + "/faculty";
        String response = restTemplate.getForObject(url, String.class);
        assertThat(response).contains("\"name\":\"Potions\"");
        assertThat(response).contains("\"name\":\"Transfiguration\"");
    }

    @Test
    void testFindByColor() throws Exception {
        List<Faculty> faculties = List.of(new Faculty(1L, "Potions", "Black"));
        when(facultyService.findByColor("Black")).thenReturn(faculties);

        String url = "http://localhost:" + port + "/faculty/findByColor?color=Black";
        String response = restTemplate.getForObject(url, String.class);
        assertThat(response).contains("\"name\":\"Potions\"");
    }

    @Test
    void testSearchFaculty() throws Exception {
        List<Faculty> faculties = List.of(new Faculty(1L, "Potions", "Black"));
        when(facultyService.search("potions")).thenReturn(faculties);

        String url = "http://localhost:" + port + "/faculty/search?query=potions";
        String response = restTemplate.getForObject(url, String.class);
        assertThat(response).contains("\"name\":\"Potions\"");
    }

    @Test
    void testGetStudentsOfFaculty() throws Exception {
        List<Student> students = Collections.emptyList();
        when(facultyService.getStudentsOfFaculty(1L)).thenReturn(students);

        String url = "http://localhost:" + port + "/faculty/{id}/students";
        String response = restTemplate.getForObject(url, String.class, 1L);
        assertThat(response).isEqualTo("[]");
    }
}
