package healthcare.severance.parkinson.controller;

import healthcare.severance.parkinson.domain.RoleType;
import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.repository.user.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(value = "test")
class AdminControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    UserJpaRepository userRepository;

    String testUserIdentifier = "testId";
    String testUserPassword = "12346567!";
    String testUserName = "테스트";
    String testUserEmail = "testtest@gmail.com";

    @Test
    @WithMockUser(authorities = "DOCTOR")
    void getMembershipFormDOCTOR() throws Exception {
        //given
        RequestBuilder request = get("/admin/setting/membership")
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        //then
        mvc.perform(request)
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(authorities = "UNSIGNED")
    void getMembershipFormUNSIGNED() throws Exception {
        //given
        RequestBuilder request = get("/admin/setting/membership")
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        //then
        mvc.perform(request)
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getMembershipForm() throws Exception {
        //given
        RequestBuilder request = get("/admin/setting/membership")
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        //then
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("membershipForm"))
                .andExpect(model().attributeExists("unsignedUsers"))
                .andExpect(model().attributeExists("signedUsers"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void signUserNoParam() throws Exception {
        //given
        List<User> userList = new ArrayList<>();
        RequestBuilder request = post("/admin/signUser")
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        //then
        mvc.perform(request)
                .andExpect(status().is(200))
                .andExpect(model().attributeExists("NullCheckedUsers"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void unsignUserNoParam() throws Exception {
        //given
        RequestBuilder request = post("/admin/unsignUser")
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        //then
        mvc.perform(request)
                .andExpect(status().is(200))
                .andExpect(model().attributeExists("NullCheckedUsers"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void signUser() throws Exception {
        //given
        User user = User.builder()
                .role(RoleType.UNSIGNED)
                .identifier(testUserIdentifier)
                .password(testUserPassword)
                .username(testUserName)
                .email(testUserEmail)
                .build();
        userRepository.save(user);
        List<User> users = userRepository.findUsersByRole(RoleType.UNSIGNED);


        RequestBuilder request = post("/admin/signUser")
                .param("unsignedUser", users.get(0).getId().toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        //then
        mvc.perform(request)
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/admin/setting/membership"));
        userRepository.delete(user);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void unsignUser() throws Exception {
        //given
        User user = User.builder()
                .role(RoleType.DOCTOR)
                .identifier(testUserIdentifier)
                .password(testUserPassword)
                .username(testUserName)
                .email(testUserEmail)
                .build();
        userRepository.save(user);
        List<User> users = userRepository.findUsersByRole(RoleType.DOCTOR);

        RequestBuilder request = post("/admin/unsignUser")
                .param("signedUser", users.get(0).getId().toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        //then
        mvc.perform(request)
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/admin/setting/membership"));
    }
}