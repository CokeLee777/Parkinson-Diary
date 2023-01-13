package healthcare.severance.parkinson.repository;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.domain.RoleType;
import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.repository.patient.PatientRepository;
import healthcare.severance.parkinson.repository.user.UserJpaRepository;
import healthcare.severance.parkinson.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(value = "test")
class PatientRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    PatientRepository patientRepository;

    // test values
    static String testPatientName = "ㅌㅅㅌ";
    static Long testPatientNum = 1111L;
    String testUserIdentifier = "testId";
    String testUserPassword = "12346567!";
    String testUserName = "테스트";
    String testUserEmail = "testtest@gmail.com";

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
    }

    @AfterEach
    void AfterEach() {
        User testId = userRepository.findByIdentifier(testUserIdentifier);
        userJpaRepository.delete(testId);
    }

    @Test
    void save() {
        // given
        Patient patient = Patient.builder()
                .patientNum(testPatientNum)
                .name(testPatientName)
                .user(userRepository.findByIdentifier(testUserIdentifier))
                .build();

        //when
        patientRepository.save(patient);

        //then
        Assertions.assertThat(patient.getPatientNum()).isEqualTo(patientRepository.findByPatientNum(testPatientNum).getPatientNum());
        patientRepository.deleteByPatientNum(testPatientNum);
    }

    @Test
    void findByName() {
        //given
        Patient patient = Patient.builder()
                .patientNum(testPatientNum)
                .name(testPatientName)
                .user(userRepository.findByIdentifier(testUserIdentifier))
                .build();
        patientRepository.save(patient);

        //when
        Page<Patient> patientByName = patientRepository.findByName(testUserIdentifier, null);

        //then
        for (Patient patient1 : patientByName) {
            if (patient1.getPatientNum() == testPatientNum) {
                Assertions.assertThat(patient1.getName()).isEqualTo(patientRepository.findByPatientNum(testPatientNum).getName());
            }
        }
        patientRepository.deleteByPatientNum(testPatientNum);
    }

    @Test
    void existsByPatientNum() {
        //given
        Patient patient = Patient.builder()
                .patientNum(testPatientNum)
                .name(testPatientName)
                .user(userRepository.findByIdentifier(testUserIdentifier))
                .build();
        patientRepository.save(patient);

        //when
        boolean result = patientRepository.existsByPatientNum(testPatientNum);

        //then
        Assertions.assertThat(result).isTrue();
        patientRepository.deleteByPatientNum(testPatientNum);
    }
}