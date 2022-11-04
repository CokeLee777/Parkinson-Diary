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
import healthcare.severance.parkinson.controller.AlarmController
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
    private lateinit var alarmController: AlarmController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_setting03)

        init(intent)

        if(!sessionManager.isAuthenticated()){
            val intent = Intent(this@DiarySettingActivity03, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun init(intent: Intent){
        sessionManager = SessionManager(applicationContext)
        alarmController = AlarmController(applicationContext)

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
        startActivity(intent)
    }

    fun finishSettingButtonPressed(view: View){
        val beforeIntent = intent
        val requestTakeTimes = convertRequestData(beforeIntent)
        //알림 세팅
        if(sessionManager.isAlarmActive()){
            alarmController.setMedicineAlarm(requestTakeTimes)
            alarmController.setSurveyAlarm(
                beforeIntent.getStringExtra("sleep_end_time")!!.substring(0, 2).toInt(),
                beforeIntent.getStringExtra("sleep_end_time")!!.substring(3, 5).toInt()
            )
        }
        if(!beforeIntent.getBooleanExtra("is_update", true)){
            createDiary(beforeIntent, requestTakeTimes)
        } else {
            updateDiary(beforeIntent, requestTakeTimes)
        }
    }

    private fun convertRequestData(beforeIntent: Intent): ArrayList<TakeTime>{
        val userInputTakeTimes = beforeIntent.getIntegerArrayListExtra("take_times")
        val requestTakeTimes = arrayListOf<TakeTime>()
        for(i in 0 until userInputTakeTimes!!.size){
            requestTakeTimes.add(TakeTime(userInputTakeTimes[i].toString()))
        }

        return requestTakeTimes
    }

    fun createDiary(beforeIntent: Intent, requestTakeTimes: ArrayList<TakeTime>){
        //서버로 요청
        RetrofitClient.diaryService.createDiary(
            sessionManager.getAccessToken()!!,
            DiaryRequest(
                beforeIntent.getStringExtra("sleep_start_time")!!,
                beforeIntent.getStringExtra("sleep_end_time")!!,
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
                } else if(response.code() == 419){
                    Toast.makeText(this@DiarySettingActivity03, "세션이 만료되었습니다",
                        Toast.LENGTH_SHORT).show()
                    sessionManager.unAuthenticate()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Server error", t.message.toString())
                Toast.makeText(this@DiarySettingActivity03, "서버 내부 오류",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateDiary(beforeIntent: Intent, requestTakeTimes: ArrayList<TakeTime>){
        //서버로 요청
        RetrofitClient.diaryService.updateDiary(
            sessionManager.getAccessToken()!!,
            DiaryRequest(
                beforeIntent.getStringExtra("sleep_start_time")!!,
                beforeIntent.getStringExtra("sleep_end_time")!!,
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
                } else if(response.code() == 419){
                    Toast.makeText(this@DiarySettingActivity03, "세션이 만료되었습니다",
                        Toast.LENGTH_SHORT).show()
                    sessionManager.unAuthenticate()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Server error", t.message.toString())
                Toast.makeText(this@DiarySettingActivity03, "서버 내부 오류",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

}