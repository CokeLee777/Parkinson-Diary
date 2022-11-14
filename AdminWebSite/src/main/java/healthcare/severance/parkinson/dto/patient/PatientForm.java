package healthcare.severance.parkinson.dto.patient;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.domain.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
public class PatientForm {
    @NotNull(message = "환자 번호를 작성해주세요.")
    private Long patientNum;
    @NotBlank(message = "환자의 이름을 작성해주세요.")
    private String name;

    @NotBlank(message = "환자의 이름을 재작성해주세요")
    private String repeatName;

    private User inChargeUser;

    public Patient toPatient(){
        return Patient.builder()
                .patientNum(patientNum)
                .user(inChargeUser)
                .name(name)
                .sleepStartTime(LocalTime.of(22,0,0))
                .sleepEndTime(LocalTime.of(8,0,0))
                .build();
    }
}
