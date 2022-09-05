package healthcare.severance.parkinson

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class DiaryStartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_diary)

        // 일정시간 지연 후에 실행
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            /**
             * 이전 키를 눌렀을  스플래시 스크린 화면으로 이동을 방지하기 위해
             * 이동한 다음 사용안함으로 finish 처리
             */
            finish()

        }, 500)
    }
}
