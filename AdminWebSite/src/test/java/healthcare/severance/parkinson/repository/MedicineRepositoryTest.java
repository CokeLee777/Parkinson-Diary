package healthcare.severance.parkinson.repository;

import healthcare.severance.parkinson.domain.*;
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
class MedicineRepositoryTest {

    @Autowired
    MedicineRepository medicineRepository;
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
    final Patient testPatient = patientRepository.findById(testPatientNum).get();

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
    void findByPatientAndTakeTimeBetween() {
        //given
        Medicine givenMedicine = Medicine.builder()
                .id(1L)
                .isTake(true)
                .takeTime(testDate.atTime(11, 0))
                .patient(testPatient)
                .build();
        Medicine givenMedicine2 = Medicine.builder()
                .id(2L)
                .isTake(false)
                .takeTime(testDate.atTime(12, 0))
                .patient(testPatient)
                .build();

        medicineRepository.save(givenMedicine);
        medicineRepository.save(givenMedicine2);
        //when
        List<Medicine> medicineList = medicineRepository.findByPatientAndTakeTimeBetween(
                patientRepository.findById(this.testPatientNum).get(),
                testDate.atStartOfDay(),
                testDate.plusDays(1).atStartOfDay());
        //then
        Assertions.assertThat(medicineList.size()).isEqualTo(2);
    }
}