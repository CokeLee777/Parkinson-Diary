package healthcare.severance.parkinson.repository;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.domain.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Page<Patient> findByName(String name, Pageable pageable);
    boolean existsByName(String name);
}
