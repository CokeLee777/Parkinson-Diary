package healthcare.severance.parkinson.repository;

import healthcare.severance.parkinson.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
