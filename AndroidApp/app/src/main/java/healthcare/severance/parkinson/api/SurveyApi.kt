package healthcare.severance.parkinson.api

import healthcare.severance.parkinson.dto.SurveyRequest
import healthcare.severance.parkinson.util.WebProperties.SURVEY_PATH
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface SurveyApi {

    /**
     * Request URL: http://[host]:[port]/api/survey
     */
    @POST(SURVEY_PATH)
    fun createSurvey(@Header("ACCESS_TOKEN") accessToken: String,
                     @Body surveyRequest: SurveyRequest) : Call<Void>

    @POST("$SURVEY_PATH/notification")
    fun registerSurveyNotification(@Header("ACCESS_TOKEN") accessToken: String) : Call<Void>

    @DELETE("$SURVEY_PATH/notification")
    fun cancelSurveyNotification(@Header("ACCESS_TOKEN") accessToken: String) : Call<Void>
}