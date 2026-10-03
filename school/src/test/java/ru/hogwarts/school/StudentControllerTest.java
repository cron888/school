package ru.hogwarts.school;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StudentControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private StudentRepository studentRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void tearDown() {
        studentRepository.deleteAll();
    }

    @Test
    void testCreateStudent() throws Exception {
        Student student = new Student();
        student.setName("Ivan Petrov");
        student.setAge(20);

        mockMvc.perform(post("/student")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivan Petrov"))
                .andExpect(jsonPath("$.age").value(20))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testGetStudent() throws Exception {
        Student student = new Student();
        student.setName("Ivan Petrov");
        student.setAge(20);

        String createResponse = mockMvc.perform(post("/student")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Student created = objectMapper.readValue(createResponse, Student.class);
        Long id = created.getId();

        mockMvc.perform(get("/student/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivan Petrov"));
    }

    @Test
    void testUpdateStudent() throws Exception {
        Student student = new Student();
        student.setName("Ivan Petrov");
        student.setAge(20);

        String createResponse = mockMvc.perform(post("/student")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Student created = objectMapper.readValue(createResponse, Student.class);
        Long id = created.getId();

        Student updatedStudent = new Student();
        updatedStudent.setId(id);
        updatedStudent.setName("Ivan Ivanov");
        updatedStudent.setAge(25);

        mockMvc.perform(put("/student")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivan Ivanov"));
    }

    @Test
    void testDeleteStudent() throws Exception {
        Student student = new Student();
        student.setName("Ivan Petrov");
        student.setAge(20);

        String createResponse = mockMvc.perform(post("/student")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Student created = objectMapper.readValue(createResponse, Student.class);
        Long id = created.getId();

        mockMvc.perform(delete("/student/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testGetAllStudents() throws Exception {
        Student student1 = new Student();
        student1.setName("Ivan Petrov");
        student1.setAge(20);
        mockMvc.perform(post("/student")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(student1)));

        Student student2 = new Student();
        student2.setName("Maria Ivanova");
        student2.setAge(22);
        mockMvc.perform(post("/student")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(student2)));

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testFindByAge() throws Exception {
        Student student1 = new Student();
        student1.setName("Ivan Petrov");
        student1.setAge(20);
        mockMvc.perform(post("/student")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(student1)));

        Student student2 = new Student();
        student2.setName("Maria Ivanova");
        student2.setAge(22);
        mockMvc.perform(post("/student")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(student2)));

        mockMvc.perform(get("/student/findByAge").param("age", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testFindByAgeBetween() throws Exception {
        Student student1 = new Student();
        student1.setName("Ivan Petrov");
        student1.setAge(18);
        mockMvc.perform(post("/student")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(student1)));

        Student student2 = new Student();
        student2.setName("Maria Ivanova");
        student2.setAge(22);
        mockMvc.perform(post("/student")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(student2)));

        Student student3 = new Student();
        student3.setName("Petr Sidorov");
        student3.setAge(25);
        mockMvc.perform(post("/student")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(student3)));

        mockMvc.perform(get("/student/findByAgeBetween").param("min", "20").param("max", "24"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetFacultyOfStudent() throws Exception {
        Student student = new Student();
        student.setName("Ivan Petrov");
        student.setAge(20);

        String createResponse = mockMvc.perform(post("/student")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Student created = objectMapper.readValue(createResponse, Student.class);
        Long id = created.getId();

        mockMvc.perform(get("/student/{id}/faculty", id))
                .andExpect(status().isOk());
    }
}
