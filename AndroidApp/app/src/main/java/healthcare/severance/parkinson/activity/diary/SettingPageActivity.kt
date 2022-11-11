package healthcare.severance.parkinson.activity.diary

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.MainActivity
import healthcare.severance.parkinson.activity.auth.LoginActivity
import healthcare.severance.parkinson.dto.TakeTime
import healthcare.severance.parkinson.service.SessionManager

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
        sessionManager = SessionManager(this)
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
        val tmp: StringBuilder = StringBuilder("약 복용시간: ")
        for (takeTime in takeTimes) {
            tmp.append("${takeTime.takeTime.substring(0, 5)}, ")
        }
        medicineTimeInfo.text = tmp.substring(0, tmp.length - 2)
    }

    fun logout(view: View) {
        if(sessionManager.isAuthenticated()){
            sessionManager.unAuthenticate()
        }

        // intent: 개별 구성요소간의 런타임 바인딩을 제공해주는 객체
        val intent = Intent(this, LoginActivity::class.java)
        // 다이어리 세팅 시작
        startActivity(intent)
    }

    fun backButtonPressed(view: View){

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}