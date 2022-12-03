package healthcare.severance.parkinson.service;

import healthcare.severance.parkinson.domain.MedicineHistory;
import healthcare.severance.parkinson.dto.patient.PatientMedicineTableForm;
import healthcare.severance.parkinson.repository.MedicineHistoryRepository;
import healthcare.severance.parkinson.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MedicineHistoryService {

    private final MedicineHistoryRepository medicineHistoryRepository;
    private final PatientRepository patientRepository;

    public List<PatientMedicineTableForm> getMedicineTable(Long id, LocalDate time) {
        List<MedicineHistory> medicineByIdAndSurveyTime = medicineHistoryRepository.findByPatientAndReservedTakeTimeBetween(
                patientRepository.findById(id).get(), time.atStartOfDay(), time.plusDays(1).atStartOfDay());
        return medicineByIdAndSurveyTime.stream().map(PatientMedicineTableForm::new).collect(Collectors.toList());
    }
}
