package healthcare.severance.parkinson.controller;

import healthcare.severance.parkinson.dto.user.RegisterForm;
import healthcare.severance.parkinson.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(value = "test")
class LoginControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    UserService userService;

    // test values
    String testIdentifier = "testId";
    String testPassword = "123456!";

    @BeforeEach
    void beforeEach() {
        RegisterForm registerForm = new RegisterForm();
        registerForm.setIdentifier(testIdentifier);
        registerForm.setPassword(testPassword);
        registerForm.setRepeatPassword(testPassword);
        registerForm.setUsername("테스트");
        registerForm.setEmail("testtest@gmail.com");
        userService.createUser(registerForm);
    }

    @Test
    void login() throws Exception {
        //given
        //when
        //then
        mvc.perform(post("/login").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("identifier", testIdentifier)
                        .param("password", testPassword))
                .andExpect(authenticated());
    }

    @Test
    void loginFailByUncorrectedId() throws Exception {
        //given
        String uncorrectedId = "uncorrectedId";
        //when
        //then
        mvc.perform(post("/login").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("identifier", uncorrectedId)
                        .param("password", testPassword))
                .andExpect(unauthenticated());
    }

    @Test
    void loginFailByUncorrectedPassword() throws Exception {
        //given
        String uncorrectedPassword = "uncorrectedPassword!";
        //when
        //then
        mvc.perform(post("/login").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("identifier", testIdentifier)
                        .param("password", uncorrectedPassword))
                .andExpect(unauthenticated());
    }
}