package healthcare.severance.parkinson.service;

import healthcare.severance.parkinson.domain.RoleType;
import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Transactional
    @Test
    void findAllUsername() {
        User user = User.builder()
                .role(RoleType.USER)
                .identifier("testId")
                .password("testPass!")
                .username("정세영")
                .email("test@gmail.com")
                .build();
    }
}