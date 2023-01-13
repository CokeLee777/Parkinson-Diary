package healthcare.severance.parkinson.controller;

import healthcare.severance.parkinson.domain.*;
import healthcare.severance.parkinson.repository.medicinehistory.MedicineHistoryJpaRepository;
import healthcare.severance.parkinson.repository.medicinehistory.MedicineHistoryRepository;
import healthcare.severance.parkinson.repository.patient.PatientRepository;
import healthcare.severance.parkinson.repository.survey.SurveyJpaRepository;
import healthcare.severance.parkinson.repository.survey.SurveyRepository;
import healthcare.severance.parkinson.repository.user.UserRepository;
import healthcare.severance.parkinson.service.graph.MedicineHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(value = "test")
class GraphControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    MedicineHistoryRepository medicineHistoryRepository;
    @Autowired
    MedicineHistoryService medicineHistoryService;
    @Autowired
    SurveyRepository surveyRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SurveyJpaRepository surveyJpaRepository;
    @Autowired
    MedicineHistoryJpaRepository medicineHistoryJpaRepository;

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
                .role(RoleType.DOCTOR)
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
    @WithMockUser
    void surveyForm() throws Exception {
        //given
        Patient testPatient = patientRepository.findByPatientNum(testPatientNum);
        Survey givenSurvey = Survey.builder()
                .id(1L)
                .patient(testPatient)
                .patientCondition(2.0)
                .medicinalEffect(true)
                .abnormalMovement(true)
                .surveyTime(testDate.atTime(11,0))
                .build();

        Survey givenSurvey2 = Survey.builder()
                .id(2L)
                .patient(testPatient)
                .patientCondition(2.0)
                .medicinalEffect(true)
                .abnormalMovement(true)
                .surveyTime(testDate.atTime(12,0))
                .build();
        surveyJpaRepository.save(givenSurvey);
        surveyJpaRepository.save(givenSurvey2);

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

        medicineHistoryJpaRepository.save(givenMedicine);
        medicineHistoryJpaRepository.save(givenMedicine2);

        RequestBuilder request = post("/patient/1111/graph")
                .param("selectedDate", String.valueOf(testDate))
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        //then
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("patient/patientGraph"))
                .andExpect(model().attributeExists("surveys"))
                .andExpect(model().attributeExists("medicines"));
    }

    @Test
    @WithMockUser
    void surveyFormNoSurvey() throws Exception {
        //given
        RequestBuilder request = post("/patient/1111/graph")
                .param("selectedDate", String.valueOf(testDate))
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        //then
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("patient/patientGraph"))
                .andExpect(model().attributeExists("noSurveyError"))
                .andExpect(model().attributeExists("noMedicineError"));
    }
}