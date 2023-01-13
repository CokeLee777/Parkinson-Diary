package healthcare.severance.parkinson.repository.user;

import healthcare.severance.parkinson.domain.User;

import java.util.HashMap;

public interface UserRepository {

    User findByIdentifier(String identifier);

    Boolean existsByIdentifier(String identifier);

    Boolean existsByEmail(String email);

    HashMap<Long, String> findHashMapAllIdAndIdentifier();

    void save(User user);
}
