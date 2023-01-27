package healthcare.severance.parkinson.activity.diary

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.MainActivity
import healthcare.severance.parkinson.activity.auth.LoginActivity
import healthcare.severance.parkinson.dto.FcmRegistrationRequest
import healthcare.severance.parkinson.dto.TakeTime
import healthcare.severance.parkinson.service.RetrofitClient
import healthcare.severance.parkinson.service.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingPageActivity : AppCompatActivity() {

    private lateinit var sleepStartInfo: TextView
    private lateinit var sleepEndInfo: TextView
    private lateinit var medicineTimeInfo: TextView

    //토큰 정보를 불러오기 위한 세션 매니저 선언
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_page)

        //로그인한 사용자만 접근 가능
        sessionManager = SessionManager(applicationContext)
        if(!sessionManager.isAuthenticated()){
            val intent = Intent(this@SettingPageActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        init(intent)
    }

    fun init(intent: Intent) {
        sleepStartInfo = findViewById(R.id.sSleepStartInfo)
        sleepEndInfo = findViewById(R.id.sSleepEndInfo)
        medicineTimeInfo = findViewById(R.id.sMedicineTimeInfo)
        medicineTimeInfo.movementMethod = ScrollingMovementMethod()

        sleepStartInfo.text = String.format("취침시간: %s", intent.getStringExtra("sleep_start_time")!!.substring(0, 5))
        sleepEndInfo.text = String.format("기상시간: %s", intent.getStringExtra("sleep_end_time")!!.substring(0, 5))
        val takeTimes: ArrayList<TakeTime> = intent.getSerializableExtra("take_times") as ArrayList<TakeTime>
        if(takeTimes.isNotEmpty()){
            val tmp: StringBuilder = StringBuilder("약 복용시간: ")
            for (takeTime in takeTimes) {
                tmp.append("${takeTime.takeTime.substring(0, 5)}, ")
            }
            medicineTimeInfo.text = tmp.substring(0, tmp.length - 2)
        }
    }

    fun logout(view: View) {
        //설문조사 푸시알림 취소
        cancelSurveyNotification(sessionManager.getAccessToken()!!)

        if(sessionManager.isAuthenticated()){
            sessionManager.unAuthenticate()
        }
    }

    private fun cancelSurveyNotification(accessToken: String) {
        RetrofitClient.surveyService.cancelSurveyNotification(
            accessToken
        ).enqueue(object: Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if(response.isSuccessful || response.code() == 400 || response.code() == 419) {
                    cancelMedicineNotification(accessToken)
                } else {
                    Toast.makeText(this@SettingPageActivity, "알수없는 이유로 요청이 불가합니다",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Server error", t.message.toString())
                Toast.makeText(this@SettingPageActivity, "서버 내부 오류",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cancelMedicineNotification(accessToken: String) {
        RetrofitClient.medicineService.cancelMedicineNotification(
            accessToken
        ).enqueue(object: Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                //요청 성공시 메인 페이지로 이동
                if(response.isSuccessful) {
                    val intent = Intent(this@SettingPageActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@SettingPageActivity, "알수없는 이유로 요청이 불가합니다",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Server error", t.message.toString())
                Toast.makeText(this@SettingPageActivity, "서버 내부 오류",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun backButtonPressed(view: View){

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}