package healthcare.severance.parkinson.service;

import healthcare.severance.parkinson.domain.Survey;
import healthcare.severance.parkinson.dto.patient.PatientGraphForm;
import healthcare.severance.parkinson.repository.PatientRepository;
import healthcare.severance.parkinson.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final PatientRepository patientRepository;
    public ArrayList<PatientGraphForm> getSurvey(Long id, LocalDate time) {
        List<Survey> surveyByIdAndSurveyTime = surveyRepository.findByPatientAndSurveyTimeBetween(
                patientRepository.findById(id).get(), time.atStartOfDay(), time.plusDays(1).atStartOfDay());
        ArrayList<PatientGraphForm> patientGraphForms = new ArrayList<>();
        for (Survey survey : surveyByIdAndSurveyTime) {
            patientGraphForms.add(new PatientGraphForm(survey));
        }
        return patientGraphForms;
    }
}
