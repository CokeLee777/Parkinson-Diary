package healthcare.severance.parkinson.service

import healthcare.severance.parkinson.api.DiaryApi
import healthcare.severance.parkinson.api.MedicineApi
import healthcare.severance.parkinson.api.PatientsApi
import healthcare.severance.parkinson.api.SurveyApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

/**
 * object 클래스는 일반 클래스와 달리 싱글톤 클래스 정의를 위한 클래스이다.
 */
object RetrofitClient {

    //const val BASE_URL = "http://parkinson-diary-lb-507925214.ap-northeast-2.elb.amazonaws.com:3000"
    private const val BASE_URL = "http://10.0.2.2:8080"

    val patientsService: PatientsApi
    val diaryService: DiaryApi
    val medicineService: MedicineApi
    val surveyService: SurveyApi

    init {
        // Retrofit 객체를 빌더패턴으로 생성, 컨버터는 GSON 컨버터 사용
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        patientsService = retrofit.create(PatientsApi::class.java)
        diaryService = retrofit.create(DiaryApi::class.java)
        medicineService = retrofit.create(MedicineApi::class.java)
        surveyService = retrofit.create(SurveyApi::class.java)
    }

}