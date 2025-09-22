package hexlet.code.controller.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import jakarta.transaction.Transactional;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.type.TypeReference;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskStatusControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    private User admin;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor adminToken;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
            .apply(springSecurity())
            .build();

        admin = userRepository.findByEmail("hexlet@example.com")
            .orElseThrow(() -> new ResourceNotFoundException("The administrator was not found"));
        adminToken = jwt().jwt(builder -> builder.subject(admin.getEmail()));
    }

    TaskStatusDTO createTaskStatus() throws Exception {
        var testTaskStatusCreateDTO = Instancio.of(modelGenerator.getTaskStatusCreateDTOModel())
            .create();

        var request = post("/api/task_statuses")
            .with(adminToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testTaskStatusCreateDTO));

        var response = mockMvc.perform(request)
            .andReturn()
            .getResponse();

        var body = response.getContentAsString();
        var taskStatusDTO = objectMapper.readValue(body, TaskStatusDTO.class);

        return taskStatusDTO;
    }

    @Test
    void testIndex() throws Exception {
        var firstResponse = mockMvc.perform(get("/api/task_statuses")
                .with(adminToken))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        var firstBody = firstResponse.getContentAsString();
        var defaultTaskStatuses = objectMapper
            .readValue(firstBody, new TypeReference<List<TaskStatusDTO>>() {
            });

        var expected = new ArrayList<>(defaultTaskStatuses);

        var testTaskStatusDTO1 = createTaskStatus();
        var testTaskStatusDTO2 = createTaskStatus();
        var testTaskStatusDTO3 = createTaskStatus();

        expected.add(testTaskStatusDTO1);
        expected.add(testTaskStatusDTO2);
        expected.add(testTaskStatusDTO3);

        var secondResponse = mockMvc.perform(get("/api/task_statuses")
                .with(adminToken))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        var secondBody = secondResponse.getContentAsString();
        var actual = objectMapper
            .readValue(secondBody, new TypeReference<List<TaskStatusDTO>>() {
            });

        assertNotNull(defaultTaskStatuses);
        assertNotNull(actual);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void testShow() throws Exception {
        var testTaskStatusDTO = createTaskStatus();
        var testTaskStatusId = testTaskStatusDTO.getId();

        var response = mockMvc.perform(get("/api/task_statuses/" + testTaskStatusId)
                .with(adminToken))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        var body = response.getContentAsString();
        var actual = objectMapper.readValue(body, TaskStatusDTO.class);

        assertNotNull(actual);
        assertThat(actual).isEqualTo(testTaskStatusDTO);
    }

    @Test
    void testCreate() throws Exception {
        var testTaskStatusDTO = createTaskStatus();
        var taskStatus = taskStatusRepository.findById(testTaskStatusDTO.getId()).orElse(null);

        assertNotNull(taskStatus);
        assertThat(taskStatus.getName()).isEqualTo(testTaskStatusDTO.getName());
        assertThat(taskStatus.getSlug()).isEqualTo(testTaskStatusDTO.getSlug());
    }

    @Test
    void update() throws Exception {
        var testTaskStatusDTO = createTaskStatus();
        var testTaskStatusId = testTaskStatusDTO.getId();

        var dataToUpdate = new HashMap<>();
        dataToUpdate.put("name", "Completed");
        dataToUpdate.put("slug", "completed");

        var response = mockMvc.perform(put("/api/task_statuses/" + testTaskStatusId)
                .with(adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dataToUpdate)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        var body = response.getContentAsString();
        var actual = objectMapper.readValue(body, TaskStatusDTO.class);

        assertNotNull(actual);
        assertThat(actual.getName()).isEqualTo("Completed");
        assertThat(actual.getSlug()).isEqualTo("completed");
    }

    @Test
    void testDestroy() throws Exception {
        var testTaskStatusDTO = createTaskStatus();
        var testTaskStatusId = testTaskStatusDTO.getId();

        var response = mockMvc.perform(delete("/api/task_statuses/" + testTaskStatusId)
                .with(adminToken))
            .andExpect(status().isNoContent())
            .andReturn()
            .getResponse();

        var body = response.getContentAsString();

        assertThat(body).isEqualTo("");
    }
}
