package healthcare.severance.parkinson.service

import healthcare.severance.parkinson.api.DiaryApi
import healthcare.severance.parkinson.api.PatientsApi
import healthcare.severance.parkinson.api.SurveyApi
import healthcare.severance.parkinson.util.WebProperties.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * object 클래스는 일반 클래스와 달리 싱글톤 클래스 정의를 위한 클래스이다.
 */
object RetrofitClient {

    val loginService: PatientsApi
    val diaryService: DiaryApi
    val surveyService: SurveyApi

    init {
        // Retrofit 객체를 빌더패턴으로 생성, 컨버터는 GSON 컨버터 사용
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        loginService = retrofit.create(PatientsApi::class.java)
        diaryService = retrofit.create(DiaryApi::class.java)
        surveyService = retrofit.create(SurveyApi::class.java)
    }

}