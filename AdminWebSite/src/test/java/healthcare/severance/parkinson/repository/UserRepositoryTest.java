package healthcare.severance.parkinson.repository;

import healthcare.severance.parkinson.domain.RoleType;
import healthcare.severance.parkinson.domain.User;
import org.assertj.core.api.Assertions;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(value = "test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    // test values
    String testUserIdentifier = "testId";
    String testUserPassword = "12346567!";
    String testUserName = "테스트";
    String testUserEmail = "testtest@gmail.com";

    @BeforeEach
    void beforeEach() {
        User user = User.builder()
                .role(RoleType.USER)
                .identifier(testUserIdentifier)
                .password(testUserPassword)
                .username(testUserName)
                .email(testUserEmail)
                .build();
        userRepository.save(user);
    }

    @AfterEach
    void afterEach() {
        User testId = userRepository.findByIdentifier(testUserIdentifier).get();
        userRepository.delete(testId);
    }

    @Test
    @Transactional
    void findByIdentifier() {
        User findUser = userRepository.findByIdentifier(testUserIdentifier).get();

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
                .role(RoleType.USER)
                .identifier(testUserIdentifier)
                .password(testUserPassword)
                .username(testUserName)
                .email(testUserEmail)
                .build();

        Assertions.assertThatThrownBy(() -> userRepository.save(user))
                .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
    }
}