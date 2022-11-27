package healthcare.severance.parkinson.repository;

import healthcare.severance.parkinson.domain.Medicine;
import healthcare.severance.parkinson.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByPatientAndTakeTimeBetween(Patient patient, LocalDateTime start, LocalDateTime end);
}
