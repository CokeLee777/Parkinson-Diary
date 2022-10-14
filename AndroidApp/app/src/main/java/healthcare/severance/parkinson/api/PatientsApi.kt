package healthcare.severance.parkinson.api

import healthcare.severance.parkinson.dto.LoginRequestDto
import healthcare.severance.parkinson.util.WebProperties.LOGIN_PATH
import healthcare.severance.parkinson.vo.LoginResponseVo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PatientsApi {
    /**
     * Request URL: http://[host]:[port]/api/patients/login
     * Request Body: LoginRequestDto
     * Response Body: LoginResponseVo
     */
    @POST(LOGIN_PATH)
    fun login(@Body loginRequest: LoginRequestDto) : Call<LoginResponseVo>
}