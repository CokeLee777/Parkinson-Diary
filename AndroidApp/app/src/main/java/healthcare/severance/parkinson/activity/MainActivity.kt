package healthcare.severance.parkinson.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.auth.LoginActivity
import healthcare.severance.parkinson.activity.diary.DiarySettingActivity01
import healthcare.severance.parkinson.activity.diary.SettingPageActivity
import healthcare.severance.parkinson.controller.AlarmController
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
    private lateinit var alarmController: AlarmController
    private lateinit var alarmButton: ImageButton
    private lateinit var diaryInfo: DiaryResponse
    private var isUpdate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 각 객체들 초기화
        init()

        //로그인한 사용자만 접근 가능
        if(!sessionManager.isAuthenticated()){
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun init(){
        sessionManager = SessionManager(applicationContext)
        alarmController = AlarmController(applicationContext)

        //알람 버튼 UI 세팅
        alarmButton = findViewById(R.id.mAlarmButton)
        if(sessionManager.isAlarmActive()){
            alarmButton.setImageResource(R.drawable.active_alarm_button)
        } else {
            alarmButton.setImageResource(R.drawable.inactive_alarm_button)
        }
        //메인 페이지에 접속할 때, 사용자 정보를 가져오도록 함
        this.getDiaryInfo(sessionManager.getAccessToken()!!)
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
                        //복용 정보가 있는경우 재설정으로 전달
                        if(diaryResponse!!.takeTimes.isNotEmpty()){
                            isUpdate = true
                        }
                        //다이어리 정보 저장
                        diaryInfo = diaryResponse

                    } else if(response.code() == 419){
                        Toast.makeText(this@MainActivity, "세션이 만료되었습니다",
                            Toast.LENGTH_SHORT).show()
                        sessionManager.unAuthenticate()
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
        if(sessionManager.isAlarmActive()){
            //세션에 있는 알림정보 inactive로 변경
            sessionManager.updateAlarmIsActive(false)
            //등록된 알람 서비스 취소하기
            alarmController.cancelMedicineAlarm(diaryInfo.takeTimes)
            //UI 변경
            alarmButton.setImageResource(R.drawable.inactive_alarm_button)
        } else {
            //세션에 있는 알림정보 inactive로 변경
            sessionManager.updateAlarmIsActive(true)
            //사용자가 설정한 알람 서비스 등록하기
            alarmController.setMedicineAlarm(diaryInfo.takeTimes)
            //UI 변경
            alarmButton.setImageResource(R.drawable.active_alarm_button)
        }
    }

}