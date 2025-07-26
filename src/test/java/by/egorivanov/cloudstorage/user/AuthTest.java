package by.egorivanov.cloudstorage.user;

import by.egorivanov.cloudstorage.dto.request.UserCreateEditDto;
import by.egorivanov.cloudstorage.exception.UserAlreadyExistsException;
import by.egorivanov.cloudstorage.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestPropertySource("classpath:application-test.yml")
public class AuthTest {

    private static final String AUTH_JSON = "{\"username\": \"%s\", \"password\": \"%s\"}";

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17");

    @Test
    void signUp_UserAlreadyExistExceptionTest() throws Exception {
        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AUTH_JSON.formatted("testLogin", "testPassword")))
                .andExpect(status().isConflict())
                .andExpect(
                        result ->
                                assertInstanceOf(UserAlreadyExistsException.class, result.getResolvedException()));
    }

    @Test
    void signIn_WhenValidCredentials_ShouldReturnUser() throws Exception {

        userService.create(new UserCreateEditDto("loginUser", "loginPassword"));

        mockMvc.perform(post("/api/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AUTH_JSON.formatted("loginUser", "loginPassword")))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON))
                .andExpect((ResultMatcher) jsonPath("$.username").value("loginUser"));
    }
}
