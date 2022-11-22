package healthcare.severance.parkinson.controller;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.domain.RoleType;
import healthcare.severance.parkinson.domain.Survey;
import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.repository.PatientRepository;
import healthcare.severance.parkinson.repository.SurveyRepository;
import healthcare.severance.parkinson.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(value = "test")
class SurveyControllerTest {

    @Autowired
    MockMvc mvc;

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
                .patient(patientRepository.findById(testPatientNum).get())
                .patientCondition(2.0)
                .medicinalEffect(true)
                .abnormalMovement(true)
                .surveyTime(testDate.atTime(11,0))
                .build();

        Survey givenSurvey2 = Survey.builder()
                .id(2L)
                .patient(patientRepository.findById(testPatientNum).get())
                .patientCondition(2.0)
                .medicinalEffect(true)
                .abnormalMovement(true)
                .surveyTime(testDate.atTime(12,0))
                .build();
        surveyRepository.save(givenSurvey);
        surveyRepository.save(givenSurvey2);

        RequestBuilder request = post("/patient/1111/survey")
                .param("selectedDate", String.valueOf(testDate))
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        //then
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("patient/patientGraph"))
                .andExpect(model().attributeExists("survey"));
    }

    @Test
    @WithMockUser
    void surveyFormNoSurvey() throws Exception {
        //given
        RequestBuilder request = post("/patient/1111/survey")
                .param("selectedDate", String.valueOf(testDate))
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        //then
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("patient/patientGraph"))
                .andExpect(model().attributeExists("noSurveyError"));
    }
}