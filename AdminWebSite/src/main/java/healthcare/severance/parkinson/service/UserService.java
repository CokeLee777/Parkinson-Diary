package healthcare.severance.parkinson.service;

import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.dto.user.RegisterForm;
import healthcare.severance.parkinson.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

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
    public List<User> findSignedUser() {
        return userRepository.findSignedUser();
    }
    public List<User> findUnsignedUser() {
        return userRepository.findUnsignedUser();
    }

    @Transactional
    public void signUser(List<User> users) {
        for (User user : users) {
            user.signUser();
        }
    }
    @Transactional
    public void unsignUser(List<User> users) {
        for (User user : users) {
            user.unsignUser();
        }
    }
}
