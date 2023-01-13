package healthcare.severance.parkinson.repository.user;

import healthcare.severance.parkinson.domain.RoleType;
import healthcare.severance.parkinson.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdentifier(String identifier);
    Boolean existsByIdentifier(String identifier);
    Boolean existsByEmail(String email);
}
