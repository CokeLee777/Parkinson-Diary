package healthcare.severance.parkinson.service;

import healthcare.severance.parkinson.domain.RoleType;
import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.dto.user.RegisterForm;
import healthcare.severance.parkinson.repository.user.UserJpaRepository;
import healthcare.severance.parkinson.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(value = "test")
@Transactional
class UserServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserJpaRepository userJpaRepository;

    // test values
    String testUserIdentifier = "testId";
    String testUserPassword = "12346567!";
    String testUserName = "테스트";
    String testUserEmail = "testtest@gmail.com";
    String signTestUser = "unsignedUser";
    String signTestUserEmail = "unsigned@gmail.com";

    @BeforeEach
    void BeforeEach() {
        RegisterForm registerForm = getRegisterForm(testUserIdentifier,testUserPassword,testUserName,testUserEmail);
        userService.createUser(registerForm);
    }

    @AfterEach
    void AfterEach() {
        userJpaRepository.delete(userJpaRepository.findByIdentifier(testUserIdentifier).get());
    }

    @Test
    void createUser() {
        //given
        //when
        User testId = userRepository.findByIdentifier(testUserIdentifier);
        //then
        assertThat(testId.getUsername()).isEqualTo(testUserName);
    }

    @Test
    void findAllUsernamesAndIds() {
        //given
        String testUserIdentifier2 = "testId2";
        String testUserIdentifier3 = "testId3";
        String testUserEmail2 = "testtest2@gmail.com";
        String testUserEmail3 = "testtest3@gmail.com";

        RegisterForm registerForm2 = getRegisterForm(testUserIdentifier2,testUserPassword,testUserName,testUserEmail2);
        RegisterForm registerForm3 = getRegisterForm(testUserIdentifier3,testUserPassword,testUserName,testUserEmail3);

        userService.createUser(registerForm2);
        userService.createUser(registerForm3);

        //when
        HashMap<Long, String> allUsernamesAndIds = userService.findAllUsernamesAndIds();

        //then
        assertThat(allUsernamesAndIds.size()).isEqualTo(0);
    }

    @Test
    void findAllUsernamesAndIdsSigned() {
        //given
        User user = User.builder()
                .role(RoleType.DOCTOR)
                .identifier(signTestUser)
                .password(testUserPassword)
                .username(testUserName)
                .email(signTestUserEmail)
                .build();
        userRepository.save(user);
        //when
        HashMap<Long, String> allUsernamesAndIds = userService.findAllUsernamesAndIds();

        //then
        assertThat(allUsernamesAndIds.size()).isEqualTo(1);
        userJpaRepository.delete(userJpaRepository.findByIdentifier(signTestUser).get());
    }

    @Test
    void isDuplicateUser() {
        //given
        //when
        Boolean duplicateUser = userService.isDuplicateUserIdentifier(testUserIdentifier);
        Boolean duplicateEmail = userService.isDuplicateUserEmail(testUserEmail);
        //then
        Assertions.assertThat(duplicateUser).isTrue();
        Assertions.assertThat(duplicateEmail).isTrue();
    }

    @Test
    void signUser() {
        //given
        List<User> user = userService.findUnsignedUser();
        //when
        userService.signUser(user);
        //then
        Assertions.assertThat(userService.findSignedUser().get(0).getRole())
                .isEqualTo(RoleType.DOCTOR);
    }
    @Test
    void unsignUser() {
        //given
        User user = User.builder()
                .role(RoleType.DOCTOR)
                .identifier(signTestUser)
                .password(testUserPassword)
                .username(testUserName)
                .email(signTestUserEmail)
                .build();
        userRepository.save(user);
        List<User> users = userService.findSignedUser();
        //when
        userService.unsignUser(users);
        //then
        Assertions.assertThat(userService.findUnsignedUser().get(0).getRole())
                .isEqualTo(RoleType.UNSIGNED);
        userJpaRepository.delete(userJpaRepository.findByIdentifier(signTestUser).get());
    }

    @Test
    void findUnsignedUser() {
        //given
        //when
        List<User> user = userService.findUnsignedUser();
        //then
        Assertions.assertThat(user.get(0).getRole()).isEqualTo(RoleType.UNSIGNED);
    }

    @Test
    void findSignedUser() {
        //given
        User user = User.builder()
                .role(RoleType.DOCTOR)
                .identifier(signTestUser)
                .password(testUserPassword)
                .username(testUserName)
                .email(signTestUserEmail)
                .build();
        userRepository.save(user);
        //when
        List<User> users = userService.findSignedUser();
        //then
        Assertions.assertThat(users.get(0).getRole()).isEqualTo(RoleType.DOCTOR);
        userJpaRepository.delete(userJpaRepository.findByIdentifier(signTestUser).get());
    }


    private RegisterForm getRegisterForm(String UserIdentifier, String UserPassword, String UserName, String Email) {
        RegisterForm registerForm = new RegisterForm();
        registerForm.setIdentifier(UserIdentifier);
        registerForm.setPassword(UserPassword);
        registerForm.setRepeatPassword(UserPassword);
        registerForm.setUsername(UserName);
        registerForm.setEmail(Email);
        return registerForm;
    }

}