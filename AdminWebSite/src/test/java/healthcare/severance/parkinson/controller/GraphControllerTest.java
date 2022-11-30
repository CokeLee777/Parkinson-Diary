package healthcare.severance.parkinson.controller;

import healthcare.severance.parkinson.domain.*;
import healthcare.severance.parkinson.repository.MedicineRepository;
import healthcare.severance.parkinson.repository.PatientRepository;
import healthcare.severance.parkinson.repository.SurveyRepository;
import healthcare.severance.parkinson.repository.UserRepository;
import healthcare.severance.parkinson.service.MedicineService;
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
    MedicineRepository medicineRepository;
    @Autowired
    MedicineService medicineService;
    @Autowired
    SurveyRepository surveyRepository;
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
    @WithMockUser
    void surveyForm() throws Exception {
        //given
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
        surveyRepository.save(givenSurvey);
        surveyRepository.save(givenSurvey2);

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