package healthcare.severance.parkinson.dto.patient;

import healthcare.severance.parkinson.domain.MedicineHistory;
import lombok.Data;

@Data
public class PatientMedicineTableForm {

    private final String reservedTakeTime;
    private final String actualTakeTime;
    private final String isTake;

    public PatientMedicineTableForm(MedicineHistory medicineHistory) {
        // 설문 폼 생성
        this.reservedTakeTime = medicineHistory.getReservedTakeTime().getYear() + "년 "
                + medicineHistory.getReservedTakeTime().getMonthValue() + "월 "
                + medicineHistory.getReservedTakeTime().getDayOfMonth()  + "일 "
                + medicineHistory.getReservedTakeTime().getHour()  + "시 "
                + medicineHistory.getReservedTakeTime().getMinute()  + "분";
        this.actualTakeTime = medicineHistory.getActualTakeTime() == null ?
                "기록 없음" : medicineHistory.getActualTakeTime().getYear() + "년 "
                + medicineHistory.getActualTakeTime().getMonthValue() + "월 "
                + medicineHistory.getActualTakeTime().getDayOfMonth()  + "일 "
                + medicineHistory.getActualTakeTime().getHour()  + "시 "
                + medicineHistory.getActualTakeTime().getMinute()  + "분";
        this.isTake = medicineHistory.getIsTake() == true ? "O" : "X";
    }
}
