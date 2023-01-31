package healthcare.severance.parkinson.repository;

import healthcare.severance.parkinson.domain.RoleType;
import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.repository.user.UserJpaRepository;
import healthcare.severance.parkinson.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(value = "test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserJpaRepository userJpaRepository;

    // test values
    String testUserIdentifier = "testId";
    String testUserPassword = "12346567!";
    String testUserName = "테스트";
    String testUserEmail = "testtest@gmail.com";
    String unsignedUser = "unsignedUser";
    String unsignedUserEmail = "unsigned@gmail.com";

    @BeforeEach
    void beforeEach() {
        User user = User.builder()
                .role(RoleType.DOCTOR)
                .identifier(testUserIdentifier)
                .password(testUserPassword)
                .username(testUserName)
                .email(testUserEmail)
                .build();
        userRepository.save(user);
    }

    @AfterEach
    void afterEach() {
        userJpaRepository.delete(userRepository.findByIdentifier(testUserIdentifier));
    }

    @Test
    @Transactional
    void findByIdentifier() {
        User findUser = userRepository.findByIdentifier(testUserIdentifier);

        assertThat(findUser.getIdentifier()).isEqualTo(testUserIdentifier);
    }


    @Test
    void existsByIdentifier() {
        Boolean exists = userRepository.existsByIdentifier(testUserIdentifier);

        assertThat(exists).isTrue();
    }

    @Test
    void duplicateUser() {
        User user = User.builder()
                .role(RoleType.DOCTOR)
                .identifier(testUserIdentifier)
                .password(testUserPassword)
                .username(testUserName)
                .email(testUserEmail)
                .build();

        Assertions.assertThatThrownBy(() -> userRepository.save(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void findUnsignedUser() {
        //given
        User user = User.builder()
                .role(RoleType.UNSIGNED)
                .identifier(unsignedUser)
                .password(testUserPassword)
                .username(testUserName)
                .email(unsignedUserEmail)
                .build();
        userRepository.save(user);
        //when
        List<User> users = userRepository.findUnsignedUser();
        //then
        Assertions.assertThat(users.get(0).getIdentifier()).isEqualTo(unsignedUser);
        userJpaRepository.delete(userJpaRepository.findByIdentifier(unsignedUser).get());
    }

    @Test
    void findSignedUser() {
        //given

        //when
        List<User> unsignedUser = userRepository.findSignedUser();
        //then
        Assertions.assertThat(unsignedUser.get(0).getIdentifier()).isEqualTo(testUserIdentifier);
    }
}