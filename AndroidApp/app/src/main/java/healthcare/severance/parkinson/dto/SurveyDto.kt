package healthcare.severance.parkinson.dto

import com.google.gson.annotations.SerializedName

data class SurveyRequest(
    //JSON 형식으로 요청, 응답시 Snake 명명 표기법
    @SerializedName(value = "has_abnormal_movement")
    val hasAbnormalMovement: Boolean,

    @SerializedName(value = "has_medicinal_effect")
    val hasMedicinalEffect: Boolean,

    @SerializedName(value = "patient_condition")
    val patientCondition: Double
)