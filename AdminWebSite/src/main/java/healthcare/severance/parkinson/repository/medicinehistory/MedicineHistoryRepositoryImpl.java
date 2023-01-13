package healthcare.severance.parkinson.repository.medicinehistory;

import healthcare.severance.parkinson.domain.MedicineHistory;
import healthcare.severance.parkinson.domain.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class MedicineHistoryRepositoryImpl implements MedicineHistoryRepository{

    private final MedicineHistoryJpaRepository medicineHistoryJpaRepository;

    @Override
    public List<MedicineHistory> findByPatientAndReservedTakeTimeBetween(Patient patient, LocalDateTime start, LocalDateTime end) {
        return medicineHistoryJpaRepository.findByPatientAndReservedTakeTimeBetween(patient, start, end);
    }
}
