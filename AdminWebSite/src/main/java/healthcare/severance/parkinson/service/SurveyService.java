package healthcare.severance.parkinson.service;

import healthcare.severance.parkinson.domain.Survey;
import healthcare.severance.parkinson.dto.patient.PatientSurveyTableForm;
import healthcare.severance.parkinson.repository.PatientRepository;
import healthcare.severance.parkinson.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final PatientRepository patientRepository;
    public List<PatientSurveyTableForm> getSurveyTable(Long id, LocalDate time) {
        List<Survey> surveyByIdAndSurveyTime = surveyRepository.findByPatientAndSurveyTimeBetween(
                patientRepository.findById(id).get(), time.atStartOfDay(), time.plusDays(1).atStartOfDay());
        return surveyByIdAndSurveyTime.stream().map(PatientSurveyTableForm::new).collect(Collectors.toList());
    }
}
