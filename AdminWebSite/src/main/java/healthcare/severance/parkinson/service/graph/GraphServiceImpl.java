package healthcare.severance.parkinson.service.graph;

import healthcare.severance.parkinson.dto.patient.PatientMedicineTableForm;
import healthcare.severance.parkinson.dto.patient.PatientSurveyTableForm;
import healthcare.severance.parkinson.repository.medicinehistory.MedicineHistoryRepository;
import healthcare.severance.parkinson.repository.patient.PatientRepository;
import healthcare.severance.parkinson.repository.survey.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GraphServiceImpl implements MedicineHistoryService, SurveyService {

    private final SurveyRepository surveyRepository;
    private final PatientRepository patientRepository;
    private final MedicineHistoryRepository medicineHistoryRepository;

    public List<PatientSurveyTableForm> getSurveyTable(Long patientNum, LocalDate time) {
        return surveyRepository.findByPatientAndSurveyTimeBetween(
                        patientRepository.findByPatientNum(patientNum),
                        time.atStartOfDay(),
                        time.plusDays(1).atStartOfDay()
                )
                .stream()
                .map(PatientSurveyTableForm::new)
                .collect(Collectors.toList());
    }

    public List<PatientMedicineTableForm> getMedicineTable(Long patientNum, LocalDate time) {
        return medicineHistoryRepository.findByPatientAndReservedTakeTimeBetween(
                        patientRepository.findByPatientNum(patientNum),
                        time.atStartOfDay(),
                        time.plusDays(1).atStartOfDay()
                )
                .stream()
                .map(PatientMedicineTableForm::new)
                .collect(Collectors.toList());
    }
}
