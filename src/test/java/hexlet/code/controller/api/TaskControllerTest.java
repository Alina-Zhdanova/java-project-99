package hexlet.code.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TaskControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

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

    TaskDTO createTask() throws Exception {
        var testUserCreateDTO = Instancio.of(modelGenerator.getUserCreateDTOModel())
            .create();
        var testTaskStatusCreateDTO = Instancio
            .of(modelGenerator.getTaskStatusCreateDTOModel())
            .create();

        var testTaskCreateDTO = Instancio.of(modelGenerator.getTaskCreateDTOModel())
            .create();

        var userResponse = mockMvc.perform(post("/api/users")
                .with(adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserCreateDTO)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse();
        var userBody = userResponse.getContentAsString();
        var userDTO = objectMapper.readValue(userBody, UserDTO.class);

        testTaskCreateDTO.setAssigneeId(userDTO.getId());

        var taskStatusResponse = mockMvc.perform(post("/api/task_statuses")
                .with(adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTaskStatusCreateDTO)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse();
        var taskStatusBody = taskStatusResponse.getContentAsString();
        var taskStatusDTO = objectMapper.readValue(taskStatusBody, TaskStatusDTO.class);

        testTaskCreateDTO.setStatus(taskStatusDTO.getSlug());

        var labelId = labelRepository.findByName("bug").get().getId();
        testTaskCreateDTO.setLabels(List.of(labelId));

        var taskResponse = mockMvc.perform(post("/api/tasks")
                .with(adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTaskCreateDTO)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse();
        var taskBody = taskResponse.getContentAsString();
        var taskDTO = objectMapper.readValue(taskBody, TaskDTO.class);

        return taskDTO;
    }

    @Test
    void testIndex() throws Exception {
        var testTaskDTO1 = createTask();
        var testTaskDTO2 = createTask();
        var testTaskDTO3 = createTask();

        var expected = List.of(testTaskDTO1, testTaskDTO2, testTaskDTO3);

        var response = mockMvc.perform(get("/api/tasks")
                .with(adminToken))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        var body = response.getContentAsString();
        var actual = objectMapper.readValue(body, new TypeReference<List<TaskDTO>>() {
        });

        assertNotNull(actual);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void testShow() throws Exception {
        var testTaskDTO = createTask();
        var testTaskId = testTaskDTO.getId();

        var response = mockMvc.perform(get("/api/tasks/" + testTaskId)
                .with(adminToken))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        var body = response.getContentAsString();
        var actual = objectMapper.readValue(body, TaskDTO.class);

        assertNotNull(actual);
        assertThat(actual).isEqualTo(testTaskDTO);
    }

    @Test
    void testCreate() throws Exception {
        var testTaskDTO = createTask();
        var task = taskRepository.findById(testTaskDTO.getId()).orElse(null);

        assertNotNull(task);
        assertThat(task.getName()).isEqualTo(testTaskDTO.getTitle());
        assertThat(task.getDescription()).isEqualTo(testTaskDTO.getContent());
        assertThat(task.getAssignee().getId()).isEqualTo(testTaskDTO.getAssigneeId());
        assertThat(task.getTaskStatus().getSlug()).isEqualTo(testTaskDTO.getStatus());
    }

    @Test
    void testUpdate() throws Exception {
        var testTaskDTO = createTask();
        var testTaskId = testTaskDTO.getId();

        var dataToUpdate = new HashMap<>();
        dataToUpdate.put("title", "title");
        dataToUpdate.put("content", "content");

        var response = mockMvc.perform(put("/api/tasks/" + testTaskId)
                .with(adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dataToUpdate)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        var body = response.getContentAsString();
        var actual = objectMapper.readValue(body, TaskDTO.class);

        assertNotNull(actual);
        assertThat(actual.getTitle()).isEqualTo("title");
        assertThat(actual.getContent()).isEqualTo("content");
    }

    @Test
    void testDestroy() throws Exception {
        var testTaskDTO = createTask();
        var testTaskId = testTaskDTO.getId();

        var response = mockMvc.perform(delete("/api/tasks/" + testTaskId)
                .with(adminToken))
            .andExpect(status().isNoContent())
            .andReturn()
            .getResponse();

        var body = response.getContentAsString();

        assertThat(body).isEmpty();
    }
}
