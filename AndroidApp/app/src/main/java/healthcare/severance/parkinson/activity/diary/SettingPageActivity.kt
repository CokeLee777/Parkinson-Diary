package healthcare.severance.parkinson.activity.diary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.auth.LoginActivity
import healthcare.severance.parkinson.service.SessionManager

class SettingPageActivity : AppCompatActivity() {

    //토큰 정보를 불러오기 위한 세션 매니저 선언
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_page)

        sessionManager = SessionManager(applicationContext)
    }

    fun logout(view: View) {
        Log.i(view.resources.getResourceName(view.id),"clicked")

        if(sessionManager.isAuthenticated()){
            sessionManager.unAuthenticate()
        }

        // intent: 개별 구성요소간의 런타임 바인딩을 제공해주는 객체
        val intent = Intent(this, LoginActivity::class.java)
        // 다이어리 세팅 시작
        startActivity(intent)
    }
}