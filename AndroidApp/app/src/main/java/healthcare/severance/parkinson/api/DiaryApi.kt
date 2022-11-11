package healthcare.severance.parkinson.api

import healthcare.severance.parkinson.dto.DiaryRequest
import healthcare.severance.parkinson.dto.DiaryResponse
import healthcare.severance.parkinson.util.WebProperties
import retrofit2.Call
import retrofit2.http.*

interface DiaryApi {

    /**
     * Request URL: http://[host]:[port]/api/diary
     */
    @GET(WebProperties.DIARY_PATH)
    fun getDiary(@Header("ACCESS_TOKEN") accessToken: String) : Call<DiaryResponse>

    @POST(WebProperties.DIARY_PATH)
    fun createDiary(@Header("ACCESS_TOKEN") accessToken: String,
                    @Body diaryRequest: DiaryRequest) : Call<Void>

    @PUT(WebProperties.DIARY_PATH)
    fun updateDiary(@Header("ACCESS_TOKEN") accessToken: String,
                    @Body diaryRequest: DiaryRequest) : Call<Void>
}