package healthcare.severance.parkinson.repository;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

    List<Survey> findByPatientAndSurveyTimeBetween(Patient patient, LocalDateTime start, LocalDateTime end);
}
