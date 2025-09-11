package hexlet.code.controller.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.UserDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.User;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class UsersControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

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

    UserDTO createTestUser() throws Exception {

        var testUserCreateDTO = Instancio.of(modelGenerator.getUserCreateDTOModel()).create();

        var request = post("/api/users")
            .with(adminToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testUserCreateDTO));

        var response = mockMvc.perform(request)
            .andReturn()
            .getResponse();

        var body = response.getContentAsString();
        var userDTO = objectMapper.readValue(body, UserDTO.class);

        return userDTO;

    }

    @Test
    void testIndex() throws Exception {

        var testUserDTO1 = createTestUser();
        var testUserDTO2 = createTestUser();
        var testUserDTO3 = createTestUser();

        var expected = new ArrayList<UserDTO>(List.of(testUserDTO1, testUserDTO2, testUserDTO3));

        var response = mockMvc.perform(get("/api/users")
            .with(adminToken))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        var body = response.getContentAsString();
        var actual = objectMapper.readValue(body, new TypeReference<List<UserDTO>>() { });
        actual.removeFirst();

        assertNotNull(actual);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void testShow() throws Exception {

        var testUserDTO = createTestUser();
        var testUserId = testUserDTO.getId();

        var response = mockMvc.perform(get("/api/users/" + testUserId)
            .with(adminToken))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        var body = response.getContentAsString();
        var actual = objectMapper.readValue(body, UserDTO.class);

        assertNotNull(actual);
        assertThat(actual).isEqualTo(testUserDTO);

    }

    @Test
    void testCreate() throws Exception {

        var testUserDTO = createTestUser();

        var user = userRepository.findByEmail(testUserDTO.getEmail()).orElse(null);

        assertNotNull(user);
        assertThat(user.getFirstName()).isEqualTo(testUserDTO.getFirstName());
        assertThat(user.getLastName()).isEqualTo(testUserDTO.getLastName());

    }

    @Test
    void testUpdate() throws Exception {

        var testUserDTO = createTestUser();
        var testUserId = testUserDTO.getId();

        var dataToUpdate = new HashMap<>();
        dataToUpdate.put("firstName", "Mike");
        dataToUpdate.put("lastName", "Wheeler");

        var response = mockMvc.perform(put("/api/users/" + testUserId)
            .with(adminToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dataToUpdate)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        var body = response.getContentAsString();
        var actual = objectMapper.readValue(body, UserDTO.class);

        assertNotNull(actual);
        assertThat(actual.getFirstName()).isEqualTo("Mike");
        assertThat(actual.getLastName()).isEqualTo("Wheeler");
    }

    @Test
    void testDestroy() throws Exception {

        var testUserDTO = createTestUser();
        var testUserId = testUserDTO.getId();

        var response = mockMvc.perform(delete("/api/users/" + testUserId)
            .with(adminToken))
            .andExpect(status().isNoContent())
            .andReturn()
            .getResponse();

        var body = response.getContentAsString();

        assertThat(body).isEqualTo("");

    }

}
