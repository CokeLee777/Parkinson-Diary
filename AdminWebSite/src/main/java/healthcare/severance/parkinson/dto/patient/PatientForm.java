package healthcare.severance.parkinson.dto.patient;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.repository.UserRepository;
import healthcare.severance.parkinson.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Slf4j
public class PatientForm {
    @NotBlank(message = "환자의 이름을 작성해주세요.")
    private String name;

    @NotBlank(message = "환자의 이름을 재작성해주세요")
    private String repeatName;

    private User inChargeUser;

    public Patient toPatient(){
        return Patient.builder()
                .user(inChargeUser)
                .name(name)
                .build();
    }
}
