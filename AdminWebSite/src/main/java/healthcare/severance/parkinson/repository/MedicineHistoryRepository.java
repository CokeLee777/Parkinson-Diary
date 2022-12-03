package healthcare.severance.parkinson.repository;

import healthcare.severance.parkinson.domain.MedicineHistory;
import healthcare.severance.parkinson.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicineHistoryRepository extends JpaRepository<MedicineHistory, Long> {
    List<MedicineHistory> findByPatientAndReservedTakeTimeBetween(Patient patient, LocalDateTime start, LocalDateTime end);
}
