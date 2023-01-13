package healthcare.severance.parkinson.service;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.domain.RoleType;
import healthcare.severance.parkinson.domain.Survey;
import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.dto.patient.PatientSurveyTableForm;
import healthcare.severance.parkinson.repository.patient.PatientRepository;
import healthcare.severance.parkinson.repository.survey.SurveyJpaRepository;
import healthcare.severance.parkinson.repository.survey.SurveyRepository;
import healthcare.severance.parkinson.repository.user.UserRepository;
import healthcare.severance.parkinson.service.graph.SurveyService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@ActiveProfiles(value = "test")
@Transactional
class SurveyServiceTest {

    @Autowired
    SurveyService surveyService;
    @Autowired
    SurveyRepository surveyRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SurveyJpaRepository surveyJpaRepository;

    // test values
    String testPatientName = "ㅌㅅㅌ";
    Long testPatientNum = 1111L;
    String testUserIdentifier = "testId";
    String testUserPassword = "12346567!";
    String testUserName = "테스트";
    String testUserEmail = "testtest@gmail.com";
    LocalDate testDate = LocalDate.of(1950, 11, 15);

    @BeforeEach
    void beforeEach() {
        User user = User.builder()
                .role(RoleType.USER)
                .identifier(testUserIdentifier)
                .password(testUserPassword)
                .username(testUserName)
                .email(testUserEmail)
                .build();
        userRepository.save(user);

        Patient patient = Patient.builder()
                .patientNum(testPatientNum)
                .name(testPatientName)
                .user(userRepository.findByIdentifier(testUserIdentifier))
                .build();
        patientRepository.save(patient);
    }

    @Test
    void getSurveyList() {
        //given
        Survey givenSurvey = Survey.builder()
                .id(1L)
                .patient(patientRepository.findByPatientNum(testPatientNum))
                .patientCondition(2.0)
                .medicinalEffect(true)
                .abnormalMovement(true)
                .surveyTime(testDate.atTime(11,0))
                .build();

        Survey givenSurvey2 = Survey.builder()
                .id(2L)
                .patient(patientRepository.findByPatientNum(testPatientNum))
                .patientCondition(2.0)
                .medicinalEffect(true)
                .abnormalMovement(true)
                .surveyTime(testDate.atTime(12,0))
                .build();

        surveyJpaRepository.save(givenSurvey);
        surveyJpaRepository.save(givenSurvey2);
        //when
        List<PatientSurveyTableForm> survey = surveyService.getSurveyTable(testPatientNum, testDate);
        //then
        Assertions.assertThat(survey.size()).isEqualTo(2);
    }

    @Test
    void getSurvey() {
        //given
        Survey givenSurvey = Survey.builder()
                .id(1L)
                .patient(patientRepository.findByPatientNum(testPatientNum))
                .patientCondition(2.0)
                .medicinalEffect(true)
                .abnormalMovement(true)
                .surveyTime(testDate.atTime(11,0))
                .build();

        surveyJpaRepository.save(givenSurvey);
        //when
        List<PatientSurveyTableForm> survey = surveyService.getSurveyTable(testPatientNum, testDate);
        //then
        Assertions.assertThat(survey.size()).isEqualTo(1);
        Assertions.assertThat(survey.get(0).getAbnormalMovement()).isEqualTo("O");
    }
}