package healthcare.severance.parkinson.dto.patient;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@Slf4j
public class PatientForm {
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
