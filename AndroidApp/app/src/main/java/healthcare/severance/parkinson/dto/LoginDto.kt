package healthcare.severance.parkinson.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    //JSON 형식으로 요청, 응답시 Snake 명명 표기법
    @SerializedName(value = "patient_num")
    val patientNum: Long,

    @SerializedName(value = "fcm_registration_token")
    val fcmRegistrationToken: String
)

data class LoginResponse(
    //JSON 형식으로 요청, 응답시 Snake 명명 표기법
    @SerializedName(value = "access_token")
    val accessToken: String
)