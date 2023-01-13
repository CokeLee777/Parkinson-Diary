package healthcare.severance.parkinson.service;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.domain.RoleType;
import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.dto.patient.PatientEditForm;
import healthcare.severance.parkinson.dto.patient.PatientForm;
import healthcare.severance.parkinson.exception.CustomException;
import healthcare.severance.parkinson.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles(value = "test")
class PatientServiceTest {

    @Autowired
    PatientService patientService;
    @Autowired
    UserRepository userRepository;

    // test values
    static String testPatientName = "ㅌㅅㅌ";
    static Long testPatientNum = 1111L;
    String testUserIdentifier = "testId";
    String testUserPassword = "12346567!";
    String testUserName = "테스트";
    String testUserEmail = "testtest@gmail.com";
    String sleepStartTime = "20:00";
    String sleepEndTime = "08:00";

    @Test
    void createPatientAndFindByName() {
        //given
        User user = makeUser();
        userRepository.save(user);
        PatientForm patientForm = setPatient(user);
        //when
        patientService.createPatient(patientForm);
        //then
        List<Patient> patients = patientService.findPatientByName(testPatientName, null).getContent();
        Assertions.assertThat(patients.size()).isGreaterThan(0);
        for (Patient patient : patients) {
            Assertions.assertThat(patient.getName()).isEqualTo(testPatientName);
        }
    }

    @Test
    void findPatientByPatientNum() {
        //given
        User user = makeUser();
        userRepository.save(user);
        PatientForm patientForm = setPatient(user);
        patientService.createPatient(patientForm);
        //when
        Patient patientByPatientNum = patientService.findPatientByPatientNum(patientForm.getPatientNum());
        //then
        Assertions.assertThat(patientByPatientNum.getName()).isEqualTo(testPatientName);
    }

    @Test
    void editPatient() {
        //given
        User user = makeUser();
        userRepository.save(user);
        PatientForm patientForm = setPatient(user);
        patientService.createPatient(patientForm);
        //when
        PatientEditForm patientEditForm = new PatientEditForm();
        patientEditForm.setName(testPatientName);
        patientEditForm.setInChargeUser(user);
        patientEditForm.setSleepStartTime(sleepStartTime);
        patientEditForm.setSleepEndTime(sleepEndTime);
        patientService.editPatient(patientForm.getPatientNum(), patientEditForm);
        //then
        Assertions.assertThat(
                patientService.findPatientByPatientNum(testPatientNum).getName())
                .isEqualTo(patientEditForm.getName());
    }

    @Test
    void deletePatient() {
        //given
        User user = makeUser();
        userRepository.save(user);
        PatientForm patientForm = setPatient(user);
        patientService.createPatient(patientForm);
        //when
        patientService.deletePatient(patientForm.getPatientNum());
        //then
        Assertions.assertThatThrownBy(() -> patientService.findPatientByPatientNum(patientForm.getPatientNum())).isInstanceOf(CustomException.class);
    }

    private User makeUser() {
    return User.builder()
            .role(RoleType.DOCTOR)
            .identifier(testUserIdentifier)
            .password(testUserPassword)
            .username(testUserName)
            .email(testUserEmail)
            .build();
}
    private static PatientForm setPatient(User user) {
        PatientForm patientForm = new PatientForm();
        patientForm.setPatientNum(testPatientNum);
        patientForm.setName(testPatientName);
        patientForm.setInChargeUser(user);
        patientForm.setRepeatName(testPatientName);
        return patientForm;
    }
}