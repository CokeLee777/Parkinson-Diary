package healthcare.severance.parkinson.repository.survey;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SurveyJpaRepository extends JpaRepository<Survey, Long> {

    List<Survey> findByPatientAndSurveyTimeBetween(Patient patient, LocalDateTime start, LocalDateTime end);
}
