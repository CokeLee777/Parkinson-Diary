package healthcare.severance.parkinson.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import healthcare.severance.parkinson.domain.RoleType;
import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.dto.patient.PatientForm;
import healthcare.severance.parkinson.exception.CustomException;
import healthcare.severance.parkinson.repository.UserRepository;
import healthcare.severance.parkinson.service.PatientService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(value = "test")
public class PatientControllerFailureTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    PatientService patientService;
    @Autowired
    UserRepository userRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    // test values
    static String testPatientName = "ㅌㅅㅌ";
    static Long testPatientNum = 1111L;
    String testUserIdentifier = "testId";
    String sleepStartTime = "20:00";
    String sleepEndTime = "08:00";

    @Test
    @WithMockUser
    void addPatientRepeatNameError() throws Exception {
        //given
        String wrongRepeatName = "불일치";

        RequestBuilder request = post("/patient/add")
                .param("patientNum", String.valueOf(testPatientNum))
                .param("name", testPatientName)
                .param("repeatName", wrongRepeatName)
                .param("inChargeUser", objectMapper.writeValueAsString(getUser().getId()))
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        mvc.perform(request)
                .andExpect(model().hasErrors());
        //then
        Assertions.assertThatThrownBy(() -> patientService.findPatientByPatientNum(testPatientNum))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @WithMockUser
    void addPatientPatientNumTypeError() throws Exception {
        //given
        String wrongPatientNum = "문자값";

        RequestBuilder request = post("/patient/add")
                .param("patientNum", wrongPatientNum)
                .param("name", testPatientName)
                .param("repeatName", testPatientName)
                .param("inChargeUser", objectMapper.writeValueAsString(getUser().getId()))
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        mvc.perform(request)
                .andExpect(model().hasErrors());
        //then
        Assertions.assertThatThrownBy(() -> patientService.findPatientByPatientNum(testPatientNum))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @WithMockUser
    void patientDetailNoPatientNumError() throws Exception {
        //given
        PatientForm patientForm = setPatient(getUser());
        patientService.createPatient(patientForm);
        //when
        //then
        mvc.perform(get("/patient/9999"))
                .andExpect(view().name("/error/customErrorPage"))
                .andExpect(model().attribute("errorCode", "환자 번호를 찾을 수 없습니다."));
    }

    @Test
    @WithMockUser
    void patientEditNoPatientNumError() throws Exception {
        //given
        PatientForm patientForm = setPatient(getUser());
        patientService.createPatient(patientForm);

        String editName = "ㅅㅈ";

        RequestBuilder request = post("/patient/9999/edit")
                .param("buttonValue", "update")
                .param("name", editName)
                .param("sleepStartTime", sleepStartTime)
                .param("sleepEndTime", sleepEndTime)
                .param("inChargeUser",
                        objectMapper.writeValueAsString(userRepository.findByIdentifier(testUserIdentifier).get().getId()))
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        //then
        mvc.perform(request)
                .andExpect(view().name("/error/customErrorPage"))
                .andExpect(model().attribute("errorCode", "환자 번호를 찾을 수 없습니다."));
    }

    @Test
    @WithMockUser
    void patientDeleteNoPatientNumError() throws Exception {
        //given
        PatientForm patientForm = setPatient(getUser());
        patientService.createPatient(patientForm);

        RequestBuilder request = post("/patient/9999/edit")
                .param("buttonValue", "delete")
                .param("name", testPatientName)
                .param("sleepStartTime", sleepStartTime)
                .param("sleepEndTime", sleepEndTime)
                .param("inChargeUser",
                        objectMapper.writeValueAsString(userRepository.findByIdentifier(testUserIdentifier).get().getId()))
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        //then
        mvc.perform(request)
                .andExpect(view().name("/error/customErrorPage"))
                .andExpect(model().attribute("errorCode", "환자 번호를 찾을 수 없습니다."));
    }

    @Test
    @WithMockUser
    void patientEditNoButtonValueError() throws Exception {
        //given
        PatientForm patientForm = setPatient(getUser());
        patientService.createPatient(patientForm);

        RequestBuilder request = post("/patient/1111/edit")
                .param("buttonValue", "")
                .param("name", testPatientName)
                .param("sleepStartTime", sleepStartTime)
                .param("sleepEndTime", sleepEndTime)
                .param("inChargeUser",
                        objectMapper.writeValueAsString(userRepository.findByIdentifier(testUserIdentifier).get().getId()))
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        //then
        mvc.perform(request)
                .andExpect(view().name("patient/patientEditForm"))
                .andExpect(model().attribute("patient", patientService.findPatientByPatientNum(testPatientNum)));
    }

    private static PatientForm setPatient(User user) {
        PatientForm patientForm = new PatientForm();
        patientForm.setPatientNum(testPatientNum);
        patientForm.setName(testPatientName);
        patientForm.setInChargeUser(user);
        patientForm.setRepeatName(testPatientName);
        return patientForm;
    }
    private User getUser() {
        User user = User.builder()
                .role(RoleType.USER)
                .identifier(testUserIdentifier)
                .password("testPass!")
                .username("정세영")
                .email("test@gmail.com")
                .build();
        return userRepository.save(user);
    }
}
