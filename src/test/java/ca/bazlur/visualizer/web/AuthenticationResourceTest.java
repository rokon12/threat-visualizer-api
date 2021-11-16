package ca.bazlur.visualizer.web;

import ca.bazlur.visualizer.domain.dto.AuthRequest;
import ca.bazlur.visualizer.domain.dto.CreateUserRequest;
import ca.bazlur.visualizer.domain.dto.UserView;
import ca.bazlur.visualizer.repo.UserRepository;
import ca.bazlur.visualizer.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static ca.bazlur.visualizer.util.JsonHelper.fromJson;
import static ca.bazlur.visualizer.util.JsonHelper.toJson;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthenticationResourceTest {

    private final String password = "Test12345";
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final UserRepository userRepository;


    @Autowired
    public AuthenticationResourceTest(MockMvc mockMvc,
                                      ObjectMapper objectMapper,
                                      UserService userService,
                                      UserRepository userRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testLoginSuccess() throws Exception {
        var userView = createUser();

        var request = new AuthRequest();
        request.setUsername(userView.getUsername());
        request.setPassword(password);

        MvcResult createResult = this.mockMvc
            .perform(post("/api/v1/public/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(objectMapper, request)))
            .andExpect(status().isOk())
            .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
            .andReturn();

        UserView authUserView = fromJson(objectMapper, createResult.getResponse().getContentAsString(), UserView.class);
        assertEquals(userView.getId(), authUserView.getId(), "User ids must match!");
    }


    @Test
    void testLoginFail() throws Exception {
        var userView = createUser();

        var request = new AuthRequest();
        request.setUsername(userView.getUsername());
        request.setPassword("xyz");

        this.mockMvc
            .perform(post("/api/v1/public/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(objectMapper, request)))
            .andExpect(status().isUnauthorized())
            .andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION))
            .andReturn();
    }

    private UserView createUser() {
        var createRequest = new CreateUserRequest();
        createRequest.setUsername("admin@admin.ca");
        createRequest.setFullName("Test user");
        createRequest.setPassword(password);
        createRequest.setRePassword(password);

        return userService.create(createRequest);
    }

    @Test
    void testRegisterSuccess() throws Exception {
        var userRequest = new CreateUserRequest();
        userRequest.setUsername("admin@admin.ca");
        userRequest.setFullName("Test User A");
        userRequest.setPassword(password);
        userRequest.setRePassword(password);

        var createResult = this.mockMvc
            .perform(post("/api/v1/public/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(objectMapper, userRequest)))
            .andExpect(status().isOk())
            .andReturn();

        var userView = fromJson(objectMapper, createResult.getResponse().getContentAsString(), UserView.class);
        assertNotNull(userView.getId(), "User id must not be null!");
        assertEquals(userRequest.getFullName(), userView.getFullName(), "User fullname  update isn't applied!");
    }

    @Test
    void testRegisterFail() throws Exception {
        var userRequest = new CreateUserRequest();
        userRequest.setUsername("invalid.username");
        userRequest.setFullName("Test User A");
        userRequest.setPassword(password);
        userRequest.setRePassword(password);

        var createResult = this.mockMvc
            .perform(post("/api/v1/public/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(objectMapper, userRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("Method argument validation failed")))
            .andExpect(jsonPath("$.message").value("Method argument validation failed"))
            .andExpect(jsonPath("$.details[0].errorMessage").value("must be a well-formed email address"))
            .andReturn();
    }
}
