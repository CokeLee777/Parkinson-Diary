package healthcare.severance.parkinson.repository.survey;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.domain.Survey;

import java.time.LocalDateTime;
import java.util.List;

public interface SurveyRepository {

    List<Survey> findByPatientAndSurveyTimeBetween(Patient patient, LocalDateTime start, LocalDateTime end);
}
