package healthcare.severance.parkinson.activity.diary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.MainActivity
import healthcare.severance.parkinson.activity.auth.LoginActivity
import healthcare.severance.parkinson.controller.MedicineNotificationController
import healthcare.severance.parkinson.dto.DiaryRequest
import healthcare.severance.parkinson.dto.TakeTime
import healthcare.severance.parkinson.service.RetrofitClient
import healthcare.severance.parkinson.service.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiarySettingActivity03 : AppCompatActivity() {

    private lateinit var sleepStartInfo: TextView
    private lateinit var sleepEndInfo: TextView
    private lateinit var medicineTimeInfo: TextView
    //토큰 정보를 불러오기 위한 세션 매니저 선언
    private lateinit var sessionManager: SessionManager
    private lateinit var medicineNotificationController: MedicineNotificationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_setting03)

        sessionManager = SessionManager(applicationContext)
        if(!sessionManager.isAuthenticated()){
            val intent = Intent(this@DiarySettingActivity03, LoginActivity::class.java)
            startActivity(intent)
        }

        init()
    }

    fun init(){

        medicineNotificationController = MedicineNotificationController(applicationContext)

        sleepStartInfo = findViewById(R.id.dSleepStartInfo)
        sleepEndInfo = findViewById(R.id.dSleepEndInfo)
        medicineTimeInfo = findViewById(R.id.dMedicineTimeInfo)

        sleepStartInfo.text = String.format("취침시간: %s", intent.getStringExtra("sleep_start_time"))
        sleepEndInfo.text = String.format("기상시간: %s", intent.getStringExtra("sleep_end_time"))
        val takeTimes = intent.getStringArrayListExtra("take_times")!!.sorted()
        val tmp: StringBuilder = StringBuilder("약 복용시간: ")
        for(takeTime in takeTimes){
            tmp.append("$takeTime, ")
        }
        medicineTimeInfo.text = tmp.substring(0, tmp.length - 2)
    }

    fun backButtonPressed(view: View){
        val intent = Intent(this, DiarySettingActivity02_2::class.java)
        maintainIntentExtra(intent)
        startActivity(intent)
    }

    fun finishSettingButtonPressed(view: View){
        val requestTakeTimes = convertRequestData()
        if(sessionManager.isAlarmActive()){
            //약 복용시간 알람 세팅

            if (!intent.getBooleanExtra("is_update", true)) {
                createDiary(requestTakeTimes)
            } else {
                updateDiary(requestTakeTimes)
            }
        }

    }

    private fun convertRequestData(): ArrayList<TakeTime>{
        val userInputTakeTimes = intent.getIntegerArrayListExtra("take_times")
        val requestTakeTimes = arrayListOf<TakeTime>()
        for(i in 0 until userInputTakeTimes!!.size){
            requestTakeTimes.add(TakeTime(userInputTakeTimes[i].toString()))
        }

        return requestTakeTimes
    }

    fun createDiary(requestTakeTimes: ArrayList<TakeTime>){
        //서버로 요청
        RetrofitClient.diaryService.createDiary(
            sessionManager.getAccessToken()!!,
            DiaryRequest(
                intent.getStringExtra("sleep_start_time")!!,
                intent.getStringExtra("sleep_end_time")!!,
                requestTakeTimes
            )
        ).enqueue(object: Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if(response.isSuccessful) {
                    val intent = Intent(this@DiarySettingActivity03,
                        MainActivity::class.java)
                    startActivity(intent)
                } else if(response.code() == 401 || response.code() == 419){
                    Toast.makeText(this@DiarySettingActivity03, "로그인이 필요합니다",
                        Toast.LENGTH_SHORT).show()
                    if(sessionManager.isAuthenticated()){
                        sessionManager.unAuthenticate()
                    }
                    val intent = Intent(this@DiarySettingActivity03,
                        LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@DiarySettingActivity03, "알수없는 이유로 요청이 불가합니다",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Server error", t.message.toString())
                Toast.makeText(this@DiarySettingActivity03, "서버 내부 오류",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateDiary(requestTakeTimes: ArrayList<TakeTime>){
        //서버로 요청
        RetrofitClient.diaryService.updateDiary(
            sessionManager.getAccessToken()!!,
            DiaryRequest(
                intent.getStringExtra("sleep_start_time")!!,
                intent.getStringExtra("sleep_end_time")!!,
                requestTakeTimes
            )
        ).enqueue(object: Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if(response.isSuccessful) {
                    val intent = Intent(this@DiarySettingActivity03,
                        MainActivity::class.java)
                    startActivity(intent)
                } else if(response.code() == 401 || response.code() == 419){
                    Toast.makeText(this@DiarySettingActivity03, "세션이 만료되었습니다",
                        Toast.LENGTH_SHORT).show()
                    if(sessionManager.isAuthenticated()){
                        sessionManager.unAuthenticate()
                    }
                    val intent = Intent(this@DiarySettingActivity03,
                        LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@DiarySettingActivity03, "알수없는 이유로 요청이 불가합니다",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Server error", t.message.toString())
                Toast.makeText(this@DiarySettingActivity03, "서버 내부 오류",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun maintainIntentExtra(nextIntent: Intent){
        nextIntent.putExtra("sleep_start_time", intent.getStringExtra("sleep_start_time"))
        nextIntent.putExtra("sleep_end_time", intent.getStringExtra("sleep_end_time"))
        nextIntent.putExtra("medicine_take_count", intent.getIntExtra("medicine_take_count", 1))
        nextIntent.putExtra("is_update", intent.getBooleanExtra("is_update", true))
    }

}