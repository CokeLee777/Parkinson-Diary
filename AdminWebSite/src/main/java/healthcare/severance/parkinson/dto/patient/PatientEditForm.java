package healthcare.severance.parkinson.dto.patient;

import com.fasterxml.jackson.annotation.JsonProperty;
import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@Slf4j
public class PatientEditForm {
    @NotNull(message = "환자 번호를 작성해주세요.")
    private Long patientNum;
    @NotBlank(message = "환자의 이름을 작성해주세요.")
    private String name;

    private User inChargeUser;

    private String sleepStartTime;
    private String sleepEndTime;

    public Patient toPatient(){
        return Patient.builder()
                .patientNum(patientNum)
                .user(inChargeUser)
                .name(name)
                .sleepStartTime(LocalTime.parse(sleepStartTime))
                .sleepEndTime(LocalTime.parse(sleepEndTime))
                .build();
    }
}
