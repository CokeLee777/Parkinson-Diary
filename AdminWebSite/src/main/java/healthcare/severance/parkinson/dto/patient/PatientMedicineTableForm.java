package healthcare.severance.parkinson.dto.patient;

import healthcare.severance.parkinson.domain.Medicine;
import lombok.Data;

@Data
public class PatientMedicineTableForm {

    private final String MedicineTakeTime;
    private final String isTake;

    public PatientMedicineTableForm(Medicine medicine) {
        // 설문 폼 생성
        this.MedicineTakeTime = medicine.getTakeTime().getYear() + "년 "
                + medicine.getTakeTime().getMonthValue() + "월 "
                + medicine.getTakeTime().getDayOfMonth()  + "일 "
                + medicine.getTakeTime().getHour()  + "시 "
                + medicine.getTakeTime().getMinute()  + "분";
        this.isTake = medicine.getIsTake() == true ? "O" : "X";
    }
}
