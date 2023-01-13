package healthcare.severance.parkinson.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import healthcare.severance.parkinson.domain.RoleType;
import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.dto.patient.PatientForm;
import healthcare.severance.parkinson.exception.CustomException;
import healthcare.severance.parkinson.repository.user.UserRepository;
import healthcare.severance.parkinson.service.PatientService;
import org.assertj.core.api.Assertions;
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
class PatientControllerTest {

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
    void patientList() throws Exception {
        mvc.perform(get("/patient/patientList"))
                .andExpect(view().name("patient/patientList"));
    }

    @Test
    @WithMockUser
    void patientSearchList() throws Exception {
        //given
        PatientForm patientForm = setPatient(setUser());
        patientService.createPatient(patientForm);
        //when
        //then
        mvc.perform(get("/patient/patientList")
                        .param("keyword", testPatientName))
                .andExpect(view().name("patient/patientList"))
                .andExpect(model().attributeExists("keyword"));

    }

    @Test
    @WithMockUser
    void addPatientForm() throws Exception {
        mvc.perform(get("/patient/add"))
                .andExpect(view().name("patient/patientForm"));
    }

    @Test
    @WithMockUser
    void patientEditForm() throws Exception {
        //given
        PatientForm patientForm = setPatient(setUser());
        patientService.createPatient(patientForm);
        //when
        //then
        mvc.perform(get("/patient/1111/edit"))
                .andExpect(view().name("patient/patientEditForm"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void addPatient() throws Exception {
        //given
        RequestBuilder request = post("/patient/add")
                .param("patientNum", String.valueOf(testPatientNum))
                .param("name", testPatientName)
                .param("repeatName", testPatientName)
                .param("inChargeUser", objectMapper.writeValueAsString(setUser().getId()))
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        mvc.perform(request)
                .andExpect(redirectedUrl("/patient/patientList"));
        //then
        Assertions.assertThat(patientService.findPatientByPatientNum(testPatientNum).getName()).isEqualTo(testPatientName);
    }

    @Test
    @WithMockUser
    void patientDetail() throws Exception {
        //given
        PatientForm patientForm = setPatient(setUser());
        patientService.createPatient(patientForm);
        //when
        //then
        mvc.perform(get("/patient/1111"))
                .andExpect(view().name("patient/patientDetail"))
                .andExpect(model().attribute("patient",patientService.findPatientByPatientNum(testPatientNum)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void patientEdit() throws Exception {
        //given
        PatientForm patientForm = setPatient(setUser());
        patientService.createPatient(patientForm);

        String editName = "ㅅㅈ";

        RequestBuilder request = post("/patient/1111/edit")
                .param("buttonValue", "update")
                .param("name", editName)
                .param("sleepStartTime", sleepStartTime)
                .param("sleepEndTime", sleepEndTime)
                .param("inChargeUser",
                        objectMapper.writeValueAsString(userRepository.findByIdentifier(testUserIdentifier).getId()))
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        mvc.perform(request)
                .andExpect(redirectedUrl("/patient/1111"));
        //then
        Assertions.assertThat(patientService.findPatientByPatientNum(testPatientNum).getName()).isEqualTo(editName);
    }

    @Test
    @WithMockUser
    void patientDelete() throws Exception {
        //given
        PatientForm patientForm = setPatient(setUser());
        patientService.createPatient(patientForm);

        RequestBuilder request = post("/patient/1111/edit")
                .param("buttonValue", "delete")
                .param("name", testPatientName)
                .param("sleepStartTime", sleepStartTime)
                .param("sleepEndTime", sleepEndTime)
                .param("inChargeUser",
                        objectMapper.writeValueAsString(userRepository.findByIdentifier(testUserIdentifier).getId()))
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        //when
        mvc.perform(request)
                .andExpect(redirectedUrl("/patient/patientList"));
        //then
        Assertions.assertThatThrownBy(() ->
                patientService.findPatientByPatientNum(testPatientNum)).isInstanceOf(CustomException.class);
    }

    private static PatientForm setPatient(User user) {
        PatientForm patientForm = new PatientForm();
        patientForm.setPatientNum(testPatientNum);
        patientForm.setName(testPatientName);
        patientForm.setInChargeUser(user);
        patientForm.setRepeatName(testPatientName);
        return patientForm;
    }
    private User setUser() {
        User user = User.builder()
                .role(RoleType.DOCTOR)
                .identifier(testUserIdentifier)
                .password("testPass!")
                .username("정세영")
                .email("test@gmail.com")
                .build();
        userRepository.save(user);
        return user;
    }
}