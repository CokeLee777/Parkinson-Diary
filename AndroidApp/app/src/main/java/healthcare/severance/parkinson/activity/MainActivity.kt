package healthcare.severance.parkinson.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.auth.LoginActivity
import healthcare.severance.parkinson.activity.diary.DiarySettingActivity01
import healthcare.severance.parkinson.activity.diary.SettingPageActivity
import healthcare.severance.parkinson.controller.MedicineNotificationController
import healthcare.severance.parkinson.controller.SurveyNotificationController
import healthcare.severance.parkinson.databinding.ActivityMainBinding
import healthcare.severance.parkinson.dto.DiaryResponse
import healthcare.severance.parkinson.service.RetrofitClient
import healthcare.severance.parkinson.service.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : Activity() {

    // lateinit : 초기화 지연
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var medicineNotificationController: MedicineNotificationController
    private lateinit var surveyNotificationController: SurveyNotificationController
    private lateinit var alarmButton: ImageButton
    private lateinit var startButton: ImageButton
    private lateinit var diaryInfo: DiaryResponse
    private var isUpdate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //로그인한 사용자만 접근 가능
        sessionManager = SessionManager(applicationContext)
        if(!sessionManager.isAuthenticated()){
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        //각 객체들 초기화
        init()
    }

    fun init(){
        medicineNotificationController = MedicineNotificationController(applicationContext)
        surveyNotificationController = SurveyNotificationController(applicationContext)

        //메인 페이지에 접속할 때, 사용자 정보를 가져오도록 함
        alarmButton = findViewById(R.id.mAlarmButton)
        this.getDiaryInfo(sessionManager.getAccessToken()!!)

        //알람 버튼 UI 세팅
        if(sessionManager.isAlarmActive()){
            alarmButton.setImageResource(R.drawable.active_alarm_button)
        } else {
            alarmButton.setImageResource(R.drawable.inactive_alarm_button)
        }
    }

    private fun getDiaryInfo(accessToken: String){
        //서버로 요청
        RetrofitClient.diaryService
            .getDiary(accessToken)
            .enqueue(object: Callback<DiaryResponse> {
                override fun onResponse(call: Call<DiaryResponse>,
                                        response: Response<DiaryResponse>) {

                    val diaryResponse: DiaryResponse? = response.body()
                    if(response.isSuccessful) {
                        //복용 정보가 있는경우 재설정으로 전달 -> UI 세팅
                        startButton = findViewById(R.id.mStartButton)
                        if(diaryResponse!!.takeTimes.isNotEmpty()){
                            isUpdate = true
                            startButton.setImageResource(R.drawable.restart_button)
                        } else {
                            startButton.setImageResource(R.drawable.start_button)
                        }
                        //다이어리 정보 저장
                        diaryInfo = diaryResponse

                    } else if(response.code() == 401 || response.code() == 419){
                        Toast.makeText(this@MainActivity, "로그인이 필요합니다",
                            Toast.LENGTH_SHORT).show()
                        if(sessionManager.isAuthenticated()){
                            sessionManager.unAuthenticate()
                        }
                        val intent = Intent(this@MainActivity,
                            LoginActivity::class.java)
                        startActivity(intent)
                    }
            }

            override fun onFailure(call: Call<DiaryResponse>, t: Throwable) {
                Log.e("Server error", t.message.toString())
                Toast.makeText(this@MainActivity, "서버 내부 오류",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun startDiary(view: View){
        // intent: 개별 구성요소간의 런타임 바인딩을 제공해주는 객체
        val intent = Intent(this@MainActivity, DiarySettingActivity01::class.java)
        intent.putExtra("is_update", isUpdate)

        startActivity(intent)
    }

    fun setDiary(view: View){

        val intent = Intent(this@MainActivity, SettingPageActivity::class.java)
        intent.putExtra("sleep_start_time", diaryInfo.sleepStartTime)
        intent.putExtra("sleep_end_time", diaryInfo.sleepEndTime)
        intent.putExtra("take_times", diaryInfo.takeTimes)

        startActivity(intent)
    }

    fun alarmButtonPressed(view: View){
        val alarmButton = findViewById<ImageButton>(view.id)

        val accessToken = sessionManager.getAccessToken()!!

        if(sessionManager.isAlarmActive()){
            //등록된 알림 서비스 취소하기
            medicineNotificationController
                .cancelMedicineNotification(accessToken)
            surveyNotificationController
                .cancelSurveyNotification(accessToken)
            //세션에 있는 알림정보 inactive로 변경
            sessionManager.updateAlarmIsActive(false)
            //UI 변경
            alarmButton.setImageResource(R.drawable.inactive_alarm_button)
        } else {
            //사용자가 설정한 알람 서비스 등록하기
            medicineNotificationController
                .registrantMedicineNotification(accessToken)
            surveyNotificationController
                .registrantSurveyNotification(accessToken)
            //세션에 있는 알림정보 active로 변경
            sessionManager.updateAlarmIsActive(true)
            //UI 변경
            alarmButton.setImageResource(R.drawable.active_alarm_button)
        }


    }

}