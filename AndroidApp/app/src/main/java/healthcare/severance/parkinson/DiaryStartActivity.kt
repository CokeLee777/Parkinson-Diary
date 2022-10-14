package healthcare.severance.parkinson

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import healthcare.severance.parkinson.service.SessionManager

class DiaryStartActivity : AppCompatActivity() {
    //토큰 정보를 불러오기 위한 세션 매니저 선언
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_diary)

        sessionManager = SessionManager(applicationContext)

        // 일정시간 지연 후에 실행
        Handler(Looper.getMainLooper()).postDelayed({
            val intent: Intent
            if(sessionManager.isAuthenticated()){
                Log.i("Authentication","not required")
                intent = Intent(this, MainActivity::class.java)
            } else {
                Log.i("Authentication","required")
                intent = Intent(this, LoginActivity::class.java)
            }

            startActivity(intent)
            /**
             * 이전 키를 눌렀을  스플래시 스크린 화면으로 이동을 방지하기 위해
             * 이동한 다음 사용안함으로 finish 처리
             */
            finish()

        }, 500)
    }
}
