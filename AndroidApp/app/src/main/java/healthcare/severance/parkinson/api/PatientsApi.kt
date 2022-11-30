package healthcare.severance.parkinson.api

import healthcare.severance.parkinson.dto.FcmRegistrationRequest
import healthcare.severance.parkinson.dto.LoginRequest
import healthcare.severance.parkinson.dto.LoginResponse
import healthcare.severance.parkinson.util.WebProperties.PATIENTS_PATH
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PatientsApi {
    /**
     * Request URL: http://[host]:[port]/api/patients/login
     */
    @POST("$PATIENTS_PATH/login")
    fun login(@Body loginRequest: LoginRequest) : Call<LoginResponse>

    @POST("$PATIENTS_PATH/fcm-token/re-issue")
    fun reissueFcmRegistrationToken(@Header("ACCESS_TOKEN") accessToken: String,
                                    @Body fcmRegistrationRequest: FcmRegistrationRequest) : Call<Void>
}