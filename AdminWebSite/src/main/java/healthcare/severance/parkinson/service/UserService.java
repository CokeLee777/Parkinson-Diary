package healthcare.severance.parkinson.service;

import healthcare.severance.parkinson.dto.user.RegisterForm;
import healthcare.severance.parkinson.exception.CustomException;
import healthcare.severance.parkinson.exception.ErrorCode;
import healthcare.severance.parkinson.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void createUser(RegisterForm form){
        userRepository.save(form.toUser(passwordEncoder));
    }

    public Boolean isDuplicateUserIdentifier(String identifier){
        return userRepository.existsByIdentifier(identifier);
    }

    public Boolean isDuplicateUserEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public HashMap<Long, String> findAllUsernamesAndIds() {
        return userRepository.findHashMapAllIdAndIdentifier();
    }
}
