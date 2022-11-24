package healthcare.severance.parkinson.api

import healthcare.severance.parkinson.dto.FcmRegistrationRequest
import healthcare.severance.parkinson.dto.SurveyRequest
import healthcare.severance.parkinson.util.WebProperties.SURVEY_PATH
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SurveyApi {

    /**
     * Request URL: http://[host]:[port]/api/survey
     */
    @POST(SURVEY_PATH)
    fun createSurvey(@Header("ACCESS_TOKEN") accessToken: String,
                     @Body surveyRequest: SurveyRequest) : Call<Void>

    @POST("$SURVEY_PATH/notification")
    fun notifySurvey(@Header("ACCESS_TOKEN") accessToken: String,
                     @Body fcmRegistrationRequest: FcmRegistrationRequest) : Call<Void>
}