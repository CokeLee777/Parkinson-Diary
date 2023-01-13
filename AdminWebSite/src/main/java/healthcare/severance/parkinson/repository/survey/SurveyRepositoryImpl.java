package healthcare.severance.parkinson.repository.survey;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.domain.Survey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class SurveyRepositoryImpl implements SurveyRepository {

    private final SurveyJpaRepository surveyJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Survey> findByPatientAndSurveyTimeBetween(Patient patient, LocalDateTime start, LocalDateTime end) {
        return surveyJpaRepository.findByPatientAndSurveyTimeBetween(patient, start, end);
    }
}
