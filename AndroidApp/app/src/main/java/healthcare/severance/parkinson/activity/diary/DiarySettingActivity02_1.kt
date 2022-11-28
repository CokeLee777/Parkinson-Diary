package healthcare.severance.parkinson.activity.diary

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.auth.LoginActivity
import healthcare.severance.parkinson.service.SessionManager

class DiarySettingActivity02_1 : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var medicineCountTextView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_setting02_1)

        //로그인한 사용자만 접근 가능
        sessionManager = SessionManager(applicationContext)
        if(!sessionManager.isAuthenticated()){
            val intent = Intent(this@DiarySettingActivity02_1,
                LoginActivity::class.java)
            startActivity(intent)
        }

        init()
    }

    fun init(){
        medicineCountTextView = findViewById(R.id.dMedicineCountForm)
    }


    fun backButtonPressed(view: View){
        val intent = Intent(this, DiarySettingActivity01::class.java)
        startActivity(intent)
    }

    fun nextButtonPressed(view: View){
        val medicineCount = medicineCountTextView.text
        if(medicineCount.isBlank() || medicineCount.isEmpty()){
            Toast.makeText(this@DiarySettingActivity02_1, "복용횟수를 입력해주세요",
                Toast.LENGTH_SHORT).show()
        } else {
            val beforeIntent = intent
            val intent = Intent(this, DiarySettingActivity02_2::class.java)
            intent.putExtra("medicine_take_count", medicineCount.toString().toInt())
            intent.putExtra("sleep_start_time", beforeIntent.getStringExtra("sleep_start_time"))
            intent.putExtra("sleep_end_time", beforeIntent.getStringExtra("sleep_end_time"))
            intent.putExtra("is_update", beforeIntent.getBooleanExtra("is_update", true))

            startActivity(intent)
        }
    }
}