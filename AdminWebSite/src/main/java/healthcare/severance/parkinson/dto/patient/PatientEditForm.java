package healthcare.severance.parkinson.dto.patient;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Slf4j
public class PatientEditForm {
    @NotBlank(message = "환자의 이름을 작성해주세요.")
    private String name;

    private User inChargeUser;

    public Patient toPatient(){
        return Patient.builder()
                .user(inChargeUser)
                .name(name)
                .build();
    }
}
