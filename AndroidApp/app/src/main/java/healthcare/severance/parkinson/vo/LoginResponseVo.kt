package healthcare.severance.parkinson.vo

import com.google.gson.annotations.SerializedName

data class LoginResponseVo(
    //JSON 형식으로 요청, 응답시 Snake 명명 표기법
    @SerializedName(value = "access_token")
    val accessToken: String
)