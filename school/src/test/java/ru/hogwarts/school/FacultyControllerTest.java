package ru.hogwarts.school;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class FacultyControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private FacultyRepository facultyRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void tearDown() {
        facultyRepository.deleteAll();
    }

    @Test
    void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Potions");
        faculty.setColor("Black");

        mockMvc.perform(post("/faculty")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Potions"))
                .andExpect(jsonPath("$.color").value("Black"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testGetFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Potions");
        faculty.setColor("Black");

        String createResponse = mockMvc.perform(post("/faculty")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Faculty created = objectMapper.readValue(createResponse, Faculty.class);
        Long id = created.getId();

        mockMvc.perform(get("/faculty/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Potions"));
    }

    @Test
    void testUpdateFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Potions");
        faculty.setColor("Black");

        String createResponse = mockMvc.perform(post("/faculty")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Faculty created = objectMapper.readValue(createResponse, Faculty.class);
        Long id = created.getId();

        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setId(id);
        updatedFaculty.setName("Defense Against Dark Arts");
        updatedFaculty.setColor("White");

        mockMvc.perform(put("/faculty")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Defense Against Dark Arts"));
    }

    @Test
    void testDeleteFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Potions");
        faculty.setColor("Black");

        String createResponse = mockMvc.perform(post("/faculty")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Faculty created = objectMapper.readValue(createResponse, Faculty.class);
        Long id = created.getId();

        mockMvc.perform(delete("/faculty/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testGetAllFaculties() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setName("Potions");
        faculty1.setColor("Black");
        mockMvc.perform(post("/faculty")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(faculty1)));

        Faculty faculty2 = new Faculty();
        faculty2.setName("Transfiguration");
        faculty2.setColor("White");
        mockMvc.perform(post("/faculty")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(faculty2)));

        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testFindByColor() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setName("Potions");
        faculty1.setColor("Black");
        mockMvc.perform(post("/faculty")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(faculty1)));

        Faculty faculty2 = new Faculty();
        faculty2.setName("Transfiguration");
        faculty2.setColor("White");
        mockMvc.perform(post("/faculty")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(faculty2)));

        mockMvc.perform(get("/faculty/findByColor").param("color", "Black"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testSearchFaculty() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setName("Potions");
        faculty1.setColor("Black");
        mockMvc.perform(post("/faculty")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(faculty1)));

        Faculty faculty2 = new Faculty();
        faculty2.setName("Transfiguration");
        faculty2.setColor("White");
        mockMvc.perform(post("/faculty")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(faculty2)));

        mockMvc.perform(get("/faculty/search").param("query", "potions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetStudentsOfFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Potions");
        faculty.setColor("Black");

        String createResponse = mockMvc.perform(post("/faculty")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Faculty created = objectMapper.readValue(createResponse, Faculty.class);
        Long id = created.getId();

        mockMvc.perform(get("/faculty/{id}/students", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
