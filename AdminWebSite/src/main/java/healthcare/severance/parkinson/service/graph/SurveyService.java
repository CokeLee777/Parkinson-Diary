package healthcare.severance.parkinson.service.graph;

import healthcare.severance.parkinson.dto.patient.PatientSurveyTableForm;

import java.time.LocalDate;
import java.util.List;

public interface SurveyService {
    List<PatientSurveyTableForm> getSurveyTable(Long patientNum, LocalDate time);
}
