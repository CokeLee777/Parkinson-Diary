package healthcare.severance.parkinson.api

import healthcare.severance.parkinson.dto.DiaryRequestDto
import healthcare.severance.parkinson.util.WebProperties
import healthcare.severance.parkinson.vo.DiaryResponseVo
import retrofit2.Call
import retrofit2.http.*

interface DiaryApi {

    /**
     * Request URL: http://[host]:[port]/api/diary
     */
    @GET(WebProperties.DIARY_PATH)
    fun getDiary(@Header("ACCESS_TOKEN") accessToken: String) : Call<DiaryResponseVo>

    @POST(WebProperties.DIARY_PATH)
    fun createDiary(@Header("ACCESS_TOKEN") accessToken: String,
                    @Body diaryRequestDto: DiaryRequestDto) : Call<Void>

    @PUT(WebProperties.DIARY_PATH)
    fun updateDiary(@Header("ACCESS_TOKEN") accessToken: String,
                    @Body diaryRequestDto: DiaryRequestDto) : Call<Void>
}