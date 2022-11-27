package healthcare.severance.parkinson.service;

import healthcare.severance.parkinson.domain.Medicine;
import healthcare.severance.parkinson.dto.patient.PatientMedicineTableForm;
import healthcare.severance.parkinson.repository.MedicineRepository;
import healthcare.severance.parkinson.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicineService {

    private final MedicineRepository medicineRepository;

    private final PatientRepository patientRepository;

    public List<PatientMedicineTableForm> getMedicineTable(Long id, LocalDate time) {
        List<Medicine> medicineByIdAndSurveyTime = medicineRepository.findByPatientAndTakeTimeBetween(
                patientRepository.findById(id).get(), time.atStartOfDay(), time.plusDays(1).atStartOfDay());
        return medicineByIdAndSurveyTime.stream().map(PatientMedicineTableForm::new).collect(Collectors.toList());
    }
}
