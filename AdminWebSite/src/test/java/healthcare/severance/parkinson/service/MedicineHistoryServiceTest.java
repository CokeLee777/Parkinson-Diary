package healthcare.severance.parkinson.service;

import healthcare.severance.parkinson.domain.*;
import healthcare.severance.parkinson.dto.patient.PatientMedicineTableForm;
import healthcare.severance.parkinson.repository.MedicineHistoryRepository;
import healthcare.severance.parkinson.repository.PatientRepository;
import healthcare.severance.parkinson.repository.UserRepository;
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
class MedicineHistoryServiceTest {
    @Autowired
    MedicineHistoryService medicineHistoryService;
    @Autowired
    MedicineHistoryRepository medicineHistoryRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    UserRepository userRepository;

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
                .user(userRepository.findByIdentifier(testUserIdentifier).get())
                .build();
        patientRepository.save(patient);
    }

    @Test
    void getSurveyList() {
        //given
        Patient testPatient = patientRepository.findById(testPatientNum).get();
        MedicineHistory givenMedicine = MedicineHistory.builder()
                .id("1")
                .isTake(true)
                .reservedTakeTime(testDate.atTime(11, 0))
                .actualTakeTime(testDate.atTime(12, 0))
                .patient(testPatient)
                .build();
        MedicineHistory givenMedicine2 = MedicineHistory.builder()
                .id("2")
                .isTake(false)
                .reservedTakeTime(testDate.atTime(12, 0))
                .actualTakeTime(testDate.atTime(13, 0))
                .patient(testPatient)
                .build();

        medicineHistoryRepository.save(givenMedicine);
        medicineHistoryRepository.save(givenMedicine2);
        //when
        List<PatientMedicineTableForm> survey = medicineHistoryService.getMedicineTable(testPatientNum, testDate);
        //then
        Assertions.assertThat(survey.size()).isEqualTo(2);
    }

    @Test
    void getSurvey() {
        //given
        Patient testPatient = patientRepository.findById(testPatientNum).get();
        MedicineHistory givenMedicine = MedicineHistory.builder()
                .id("1")
                .isTake(true)
                .reservedTakeTime(testDate.atTime(11, 0))
                .actualTakeTime(testDate.atTime(12, 0))
                .patient(testPatient)
                .build();

        medicineHistoryRepository.save(givenMedicine);
        //when
        List<PatientMedicineTableForm> survey = medicineHistoryService.getMedicineTable(testPatientNum, testDate);
        //then
        Assertions.assertThat(survey.size()).isEqualTo(1);
        Assertions.assertThat(survey.get(0).getIsTake()).isEqualTo("O");
    }
}