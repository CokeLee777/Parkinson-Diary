package healthcare.severance.parkinson.dto

import com.google.gson.annotations.SerializedName

data class MedicineHistoryRequest(
    //JSON 형식으로 요청, 응답시 Snake 명명 표기법
    @SerializedName(value = "medicine_history_id")
    val medicineHistoryId: String
)