package hexlet.code.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.LabelDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import jakarta.transaction.Transactional;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

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

import com.fasterxml.jackson.core.type.TypeReference;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LabelControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

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

    LabelDTO createLabel() throws Exception {
        var testLabelCreateDTO = Instancio.of(modelGenerator.getLabelCreateDTOModel())
            .create();

        var request = post("/api/labels")
            .with(adminToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testLabelCreateDTO));

        var response = mockMvc.perform(request)
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse();

        var body = response.getContentAsString();
        var labelDTO = objectMapper.readValue(body, LabelDTO.class);

        return labelDTO;
    }

    @Test
    void testIndex() throws Exception {
        var firstResponse = mockMvc.perform(get("/api/labels")
                .with(adminToken))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        var firstBody = firstResponse.getContentAsString();
        var defaultLabels = objectMapper.readValue(firstBody, new TypeReference<List<LabelDTO>>() {
        });

        var expected = new ArrayList<>(defaultLabels);

        var testLabel1 = createLabel();
        var testLabel2 = createLabel();
        var testLabel3 = createLabel();

        expected.add(testLabel1);
        expected.add(testLabel2);
        expected.add(testLabel3);

        var secondResponse = mockMvc.perform(get("/api/labels")
                .with(adminToken))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        var secondBody = secondResponse.getContentAsString();
        var actual = objectMapper.readValue(secondBody, new TypeReference<List<LabelDTO>>() {
        });

        assertNotNull(defaultLabels);
        assertNotNull(actual);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void testShow() throws Exception {
        var testLabelDTO = createLabel();
        var testLabelId = testLabelDTO.getId();

        var response = mockMvc.perform(get("/api/labels/" + testLabelId)
                .with(adminToken))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        var body = response.getContentAsString();
        var actual = objectMapper.readValue(body, LabelDTO.class);

        assertNotNull(actual);
        assertThat(actual).isEqualTo(testLabelDTO);
    }

    @Test
    void testCreate() throws Exception {
        var testLabelDTO = createLabel();
        var label = labelRepository.findById(testLabelDTO.getId()).orElse(null);

        assertNotNull(label);
        assertThat(label.getName()).isEqualTo(testLabelDTO.getName());
    }

    @Test
    void testUpdate() throws Exception {
        var testLabelDTO = createLabel();
        var testLabelId = testLabelDTO.getId();

        var dataToUpdate = new HashMap<>();
        dataToUpdate.put("name", "name");

        var response = mockMvc.perform(put("/api/labels/" + testLabelId)
                .with(adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dataToUpdate)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        var body = response.getContentAsString();
        var actual = objectMapper.readValue(body, LabelDTO.class);

        assertNotNull(actual);
        assertThat(actual.getName()).isEqualTo("name");
    }

    @Test
    void testDestroy() throws Exception {
        var testLabelDTO = createLabel();
        var testLabelId = testLabelDTO.getId();

        var response = mockMvc.perform(delete("/api/labels/" + testLabelId)
                .with(adminToken))
            .andExpect(status().isNoContent())
            .andReturn()
            .getResponse();

        var body = response.getContentAsString();

        assertThat(body).isEmpty();
    }
}
