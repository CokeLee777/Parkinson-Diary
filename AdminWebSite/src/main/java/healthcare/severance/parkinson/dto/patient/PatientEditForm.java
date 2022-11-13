package healthcare.severance.parkinson.dto.patient;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.domain.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalTime;

@Data
public class PatientEditForm {
    @NotBlank(message = "환자의 이름을 작성해주세요.")
    private String name;

    private User inChargeUser;

    private String sleepStartTime;
    private String sleepEndTime;

    public Patient toPatient(){
        return Patient.builder()
                .user(inChargeUser)
                .name(name)
                .sleepStartTime(LocalTime.parse(sleepStartTime))
                .sleepEndTime(LocalTime.parse(sleepEndTime))
                .build();
    }
}
