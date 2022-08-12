package healthcare.severance.parkinson.repository;

import healthcare.severance.parkinson.domain.RoleType;
import healthcare.severance.parkinson.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    void findByIdentifier() {
        User user = User.builder()
                .role(RoleType.USER)
                .identifier("test123")
                .password("test123!")
                .username("test")
                .email("test@gmail.com")
                .build();
        userRepository.save(user);

        User findUser = userRepository.findByIdentifier(user.getIdentifier()).get();

        assertThat(findUser).isSameAs(user);
    }

    @Test
    void existsByIdentifier() {
    }
}