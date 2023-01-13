package healthcare.severance.parkinson.repository.medicinehistory;

import healthcare.severance.parkinson.domain.MedicineHistory;
import healthcare.severance.parkinson.domain.Patient;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicineHistoryRepository {

    List<MedicineHistory> findByPatientAndReservedTakeTimeBetween(Patient patient, LocalDateTime start, LocalDateTime end);

}
