package healthcare.severance.parkinson.repository;

import healthcare.severance.parkinson.domain.RoleType;
import healthcare.severance.parkinson.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    void findByIdentifier() {
        User user = User.builder()
                .role(RoleType.USER)
                .identifier("testId")
                .password("testPass!")
                .username("정세영")
                .email("test@gmail.com")
                .build();
        userRepository.save(user);

        User findUser = userRepository.findByIdentifier(user.getIdentifier()).get();

        assertThat(findUser).isSameAs(user);
    }

//    @Test
//    @Transactional
//    void usernameKoreanTest() {
//        User user = User.builder()
//                .role(RoleType.USER)
//                .identifier("test123")
//                .password("test123!")
//                .username("정세영")
//                .email("test@gmail.com")
//                .build();
//        userRepository.save(user);
//
//        List<User> all = userRepository.findAll();
//        System.out.println("all = " + all.get(0).getUsername());
//    }

    @Test
    void existsByIdentifier() {
    }
}