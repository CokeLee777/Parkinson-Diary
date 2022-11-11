package healthcare.severance.parkinson.service;

import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.dto.user.RegisterForm;
import healthcare.severance.parkinson.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

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

    public HashMap<Long, String> findAllUsernamesAndIds() {
        List<User> users = userRepository.findAll();
        HashMap<Long, String> userMap = new HashMap<>();
        for (User user : users) {
            userMap.put(user.getId(), user.getUsername());
        }
        return userMap;
    }
}
