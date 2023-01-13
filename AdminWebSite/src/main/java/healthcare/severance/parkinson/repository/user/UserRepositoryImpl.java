package healthcare.severance.parkinson.repository.user;

import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

import static healthcare.severance.parkinson.exception.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository{

    private final UserJpaRepository userJpaRepository;

    @Override
    public User findByIdentifier(String identifier) {
        return userJpaRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디 입니다."));
    }

    @Override
    public Boolean existsByIdentifier(String identifier) {
        return userJpaRepository.existsByIdentifier(identifier);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public HashMap<Long, String> findHashMapAllIdAndIdentifier() {
        return getUserHashMap(userJpaRepository.findAll());
    }

    private static HashMap<Long, String> getUserHashMap(List<User> userList) {
        HashMap<Long, String> userHashMap = new HashMap<>();
        for (User user : userList) {
            userHashMap.put(user.getId(), user.getIdentifier());
        }
        return userHashMap;
    }

    @Override
    public void save(User user) {
        userJpaRepository.save(user);
    }
}
