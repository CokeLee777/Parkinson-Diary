package healthcare.severance.parkinson.dto.patient;

import healthcare.severance.parkinson.domain.Survey;
import lombok.Data;

@Data
public class PatientSurveyTableForm {
    private final String surveyTime;

    private final String medicinalEffect;

    private final String abnormalMovement;

    private final String patientCondition;

    public PatientSurveyTableForm(Survey survey) {
        // 설문 폼 생성
        this.surveyTime = survey.getSurveyTime().getYear() + "년 "
                + survey.getSurveyTime().getMonthValue() + "월 "
                + survey.getSurveyTime().getDayOfMonth()  + "일 "
                + survey.getSurveyTime().getHour()  + "시 "
                + survey.getSurveyTime().getMinute()  + "분";
        this.medicinalEffect = survey.getMedicinalEffect() == true ? "있음" : "없음";
        this.abnormalMovement = survey.getAbnormalMovement() == true ? "있음" : "없음";
        if (0 <= survey.getPatientCondition() && survey.getPatientCondition() < 33) {
            this.patientCondition = "안좋음(" + survey.getPatientCondition() + ")";
        } else if (33 <= survey.getPatientCondition() && survey.getPatientCondition() < 66) {
            this.patientCondition = "보통(" + survey.getPatientCondition() + ")";
        } else if (66 <= survey.getPatientCondition() && survey.getPatientCondition() <= 100) {
            this.patientCondition = "좋음(" + survey.getPatientCondition() + ")";
        } else {
            this.patientCondition = survey.getPatientCondition().toString();
        }
    }
}
