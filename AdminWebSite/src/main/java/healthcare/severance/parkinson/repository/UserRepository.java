package healthcare.severance.parkinson.repository;

import healthcare.severance.parkinson.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdentifier(String identifier);
    Boolean existsByIdentifier(String identifier);

}
