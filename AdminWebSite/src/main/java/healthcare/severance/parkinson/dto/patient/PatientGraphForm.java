package healthcare.severance.parkinson.dto.patient;

import healthcare.severance.parkinson.domain.Survey;
import lombok.Data;

@Data
public class PatientGraphForm {
    private final String surveyTime;

    private final String medicinalEffect;

    private final String abnormalMovement;

    private final String patientCondition;

    public PatientGraphForm(Survey survey) {
        this.surveyTime = survey.getSurveyTime().getYear() + "년 "
                + survey.getSurveyTime().getMonthValue() + "월 "
                + survey.getSurveyTime().getDayOfMonth()  + "일 "
                + survey.getSurveyTime().getHour()  + "시 "
                + survey.getSurveyTime().getMinute()  + "분";
        this.medicinalEffect = survey.getMedicinalEffect() == true ? "있음" : "없음";
        this.abnormalMovement = survey.getAbnormalMovement() == true ? "있음" : "없음";
        if (1.0 <= survey.getPatientCondition() && survey.getPatientCondition() < 3.3) {
            this.patientCondition = "안좋음(" + survey.getPatientCondition() + ")";
        } else if (3.3 <= survey.getPatientCondition() && survey.getPatientCondition() < 6.6) {
            this.patientCondition = "보통(" + survey.getPatientCondition() + ")";
        } else if (6.6 <= survey.getPatientCondition() && survey.getPatientCondition() <= 10.0) {
            this.patientCondition = "좋음(" + survey.getPatientCondition() + ")";
        } else {
            this.patientCondition = survey.getPatientCondition().toString();
        }
    }
}
