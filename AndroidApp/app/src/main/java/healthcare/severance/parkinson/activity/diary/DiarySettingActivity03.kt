package healthcare.severance.parkinson.activity.diary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import healthcare.severance.parkinson.activity.MainActivity
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.auth.LoginActivity
import healthcare.severance.parkinson.dto.DiaryRequestDto
import healthcare.severance.parkinson.dto.TakeTime
import healthcare.severance.parkinson.service.RetrofitClient
import healthcare.severance.parkinson.service.SessionManager
import healthcare.severance.parkinson.vo.DiaryResponseVo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiarySettingActivity03 : AppCompatActivity() {

    private lateinit var sleepStartInfo: TextView
    private lateinit var sleepEndInfo: TextView
    private lateinit var medicineTimeInfo: TextView

    //토큰 정보를 불러오기 위한 세션 매니저 선언
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_setting03)

        sessionManager = SessionManager(this)
        if(!sessionManager.isAuthenticated()){
            val intent = Intent(this@DiarySettingActivity03, LoginActivity::class.java)
            startActivity(intent)
        }

        init(intent)
    }

    fun init(intent: Intent){
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
        if(!beforeIntent.getBooleanExtra("is_update", true)){
            createDiary(beforeIntent)
        } else {
            updateDiary(beforeIntent)
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

    fun createDiary(beforeIntent: Intent){
        //서버로 요청
        val requestTakeTimes = convertRequestData(beforeIntent)
        RetrofitClient.diaryService.createDiary(
            sessionManager.getAccessToken()!!,
            DiaryRequestDto(
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
                    val intent = Intent(this@DiarySettingActivity03, MainActivity::class.java)
                    startActivity(intent)
                } else if(response.code() == 419){
                    Toast.makeText(this@DiarySettingActivity03, "세션이 만료되었습니다", Toast.LENGTH_SHORT).show()
                    sessionManager.unAuthenticate()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Server error", t.message.toString())
                Toast.makeText(this@DiarySettingActivity03, "서버 내부 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateDiary(beforeIntent: Intent){
        //서버로 요청
        val requestTakeTimes = convertRequestData(beforeIntent)
        RetrofitClient.diaryService.updateDiary(
            sessionManager.getAccessToken()!!,
            DiaryRequestDto(
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
                    val intent = Intent(this@DiarySettingActivity03, MainActivity::class.java)
                    startActivity(intent)
                } else if(response.code() == 419){
                    Toast.makeText(this@DiarySettingActivity03, "세션이 만료되었습니다", Toast.LENGTH_SHORT).show()
                    sessionManager.unAuthenticate()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Server error", t.message.toString())
                Toast.makeText(this@DiarySettingActivity03, "서버 내부 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }
}