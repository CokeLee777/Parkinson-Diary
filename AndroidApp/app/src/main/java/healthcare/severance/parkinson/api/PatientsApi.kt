package healthcare.severance.parkinson.api

import healthcare.severance.parkinson.dto.LoginRequest
import healthcare.severance.parkinson.dto.LoginResponse
import healthcare.severance.parkinson.util.WebProperties.LOGIN_PATH
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PatientsApi {
    /**
     * Request URL: http://[host]:[port]/api/patients/login
     */
    @POST(LOGIN_PATH)
    fun login(@Body loginRequest: LoginRequest) : Call<LoginResponse>
}