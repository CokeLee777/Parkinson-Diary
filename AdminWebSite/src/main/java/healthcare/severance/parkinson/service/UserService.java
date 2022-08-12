package healthcare.severance.parkinson.service;

import healthcare.severance.parkinson.dto.user.RegisterForm;
import healthcare.severance.parkinson.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(RegisterForm form){
        userRepository.save(form.toUser(passwordEncoder));
    }

    public Boolean isDuplicateUser(String identifier){
        return userRepository.existsByIdentifier(identifier);
    }
}
