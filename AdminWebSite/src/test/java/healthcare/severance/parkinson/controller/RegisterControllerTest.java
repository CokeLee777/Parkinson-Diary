package healthcare.severance.parkinson.controller;

import healthcare.severance.parkinson.domain.RoleType;
import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.repository.user.UserJpaRepository;
import healthcare.severance.parkinson.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(value = "test")
class RegisterControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserJpaRepository userJpaRepository;

    // test values
    String testUserName = "ㅌㅅㅌ";
    String testUserIdentifier = "testId";
    String testUserPassword = "1234567a!";
    String testUserEmail = "testtest@gmail.com";

    @Test
    void getRegister() throws Exception {
        mvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void postRegister() throws Exception {
        //given
        RequestBuilder request = post("/register")
                .param("identifier", testUserIdentifier)
                .param("password", testUserPassword)
                .param("repeatPassword", testUserPassword)
                .param("username", testUserName)
                .param("email", testUserEmail)
                .param("role", String.valueOf(RoleType.DOCTOR))
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        //then
        mvc.perform(request)
                .andExpect(redirectedUrl("/"));

    }

    @Test
    void registerFail() throws Exception {
        //given
        String patternErrorPassword = "1234";

        RequestBuilder request = post("/register")
                .param("identifier", testUserIdentifier)
                .param("password", patternErrorPassword)
                .param("repeatPassword", patternErrorPassword)
                .param("username", testUserName)
                .param("email", testUserEmail)
                .param("role", String.valueOf(RoleType.DOCTOR))
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        //then
        mvc.perform(request)
                .andExpect(view().name("register"))
                .andExpect(model().hasErrors());
    }

    @Test
    void registerRepeatPasswordUncorrectedError() throws Exception {
        //given
        String wrongRepeatPassword = "wrongPass";

        RequestBuilder request = post("/register")
                .param("identifier", testUserIdentifier)
                .param("password", testUserPassword)
                .param("repeatPassword", wrongRepeatPassword)
                .param("username", testUserName)
                .param("email", testUserEmail)
                .param("role", String.valueOf(RoleType.DOCTOR))
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        //then
        mvc.perform(request)
                .andExpect(view().name("register"))
                .andExpect(model().hasErrors());
    }

    @Test
    void registerIdentifierDuplicateError() throws Exception {
        //given
        String duplicateIdentifier = "dupId";

        User user = User.builder()
                .role(RoleType.DOCTOR)
                .identifier(duplicateIdentifier)
                .password(testUserPassword)
                .username(testUserName)
                .email(testUserEmail)
                .build();
        userRepository.save(user);

        RequestBuilder request = post("/register")
                .param("identifier", duplicateIdentifier)
                .param("password", testUserPassword)
                .param("repeatPassword", testUserPassword)
                .param("username", testUserName)
                .param("email", testUserEmail)
                .param("role", String.valueOf(RoleType.DOCTOR))
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        //then
        mvc.perform(request)
                .andExpect(view().name("register"))
                .andExpect(model().hasErrors());
    }

    @Test
    void registerEmailFormUncorrectedError() throws Exception {
        //given
        String wrongPatternEmail = "wrongEmail";

        RequestBuilder request = post("/register")
                .param("identifier", testUserIdentifier)
                .param("password", testUserPassword)
                .param("repeatPassword", testUserPassword)
                .param("username", testUserName)
                .param("email", wrongPatternEmail)
                .param("role", String.valueOf(RoleType.DOCTOR))
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        //then
        mvc.perform(request)
                .andExpect(view().name("register"))
                .andExpect(model().hasErrors());
    }
}