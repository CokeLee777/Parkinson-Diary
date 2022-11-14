package healthcare.severance.parkinson.service;

import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.dto.user.RegisterForm;
import healthcare.severance.parkinson.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    // test values
    String testUserIdentifier = "testId";
    String testUserPassword = "12346567!";
    String testUserName = "테스트";
    String testUserEmail = "testtest@gmail.com";

    @Test
    void createUser() {
        //given
        RegisterForm registerForm = getRegisterForm(testUserIdentifier,testUserPassword,testUserName,testUserEmail);
        //when
        userService.createUser(registerForm);
        User testId = userRepository.findByIdentifier(testUserIdentifier).get();
        //then
        assertThat(testId.getUsername()).isEqualTo(registerForm.getUsername());
    }

    @Test
    void createUserByDuplicatedIdentifier() {
        //given
        String newEmail = "test@gmail.com";

        RegisterForm registerForm = getRegisterForm(testUserIdentifier,testUserPassword,testUserName,testUserEmail);
        RegisterForm duplicatedForm = getRegisterForm(testUserIdentifier,testUserPassword,testUserName,newEmail);

        //when
        userService.createUser(registerForm);
        //then
        Assertions.assertThatThrownBy(
                () -> userService.createUser(duplicatedForm))
                .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void findAllUsernamesAndIds() {
        //given
        String testUserIdentifier2 = "testId2";
        String testUserIdentifier3 = "testId3";
        String testUserEmail2 = "testtest2@gmail.com";
        String testUserEmail3 = "testtest3@gmail.com";

        RegisterForm registerForm1 = getRegisterForm(testUserIdentifier,testUserPassword,testUserName,testUserEmail);
        RegisterForm registerForm2 = getRegisterForm(testUserIdentifier2,testUserPassword,testUserName,testUserEmail2);
        RegisterForm registerForm3 = getRegisterForm(testUserIdentifier3,testUserPassword,testUserName,testUserEmail3);

        userService.createUser(registerForm1);
        userService.createUser(registerForm2);
        userService.createUser(registerForm3);

        //when
        HashMap<Long, String> allUsernamesAndIds = userService.findAllUsernamesAndIds();

        //then
        assertThat(allUsernamesAndIds.size()).isEqualTo(3);
    }

    @Test
    void createUserByDuplicatedEmail() {
        //given
        String newIdentifier = "testId1";

        RegisterForm registerForm = getRegisterForm(testUserIdentifier,testUserPassword,testUserName,testUserEmail);
        RegisterForm duplicatedForm = getRegisterForm(newIdentifier,testUserPassword,testUserName,testUserEmail);

        //when
        userService.createUser(registerForm);
        //then
        Assertions.assertThatThrownBy(
                        () -> userService.createUser(duplicatedForm))
                .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void isDuplicateUser() {
        //given
        RegisterForm registerForm1 = getRegisterForm(testUserIdentifier,testUserPassword,testUserName,testUserEmail);
        userService.createUser(registerForm1);
        //when
        Boolean duplicateUser = userService.isDuplicateUser(testUserIdentifier);
        //then
        Assertions.assertThat(duplicateUser).isTrue();
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