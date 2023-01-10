package healthcare.severance.parkinson.service;

import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.dto.user.RegisterForm;
import healthcare.severance.parkinson.exception.CustomException;
import healthcare.severance.parkinson.repository.user.UserJpaRepository;
import healthcare.severance.parkinson.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

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
        assertThat(allUsernamesAndIds.size()).isEqualTo(3);
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