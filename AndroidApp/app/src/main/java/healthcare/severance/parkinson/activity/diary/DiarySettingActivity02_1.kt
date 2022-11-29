package healthcare.severance.parkinson.activity.diary

import android.content.Intent
import android.os.Bundle
import android.text.Editable
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
        val medicineCount = intent.getIntExtra("medicine_take_count", -1)
        if(medicineCount != -1){
            medicineCountTextView.setText(medicineCount.toString())
        }
    }


    fun backButtonPressed(view: View){
        val intent = Intent(this, DiarySettingActivity01::class.java)
        maintainIntentExtra(intent)
        startActivity(intent)
    }

    fun nextButtonPressed(view: View){
        val medicineCount = medicineCountTextView.text
        if(medicineCount.isBlank() || medicineCount.isEmpty()){
            Toast.makeText(this@DiarySettingActivity02_1, "복용횟수를 입력해주세요",
                Toast.LENGTH_SHORT).show()
        } else if(medicineCount.toString().toInt() <= 0) {
            Toast.makeText(this@DiarySettingActivity02_1, "복용횟수는 1회 이상이어야 합니다",
                Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this@DiarySettingActivity02_1, DiarySettingActivity02_2::class.java)
            intent.putExtra("medicine_take_count", medicineCount.toString().toInt())
            //이전에 입력한 값을 유지
            maintainIntentExtra(intent)
            startActivity(intent)
        }
    }

    private fun maintainIntentExtra(nextIntent: Intent){
        nextIntent.putExtra("sleep_start_time", intent.getStringExtra("sleep_start_time"))
        nextIntent.putExtra("sleep_end_time", intent.getStringExtra("sleep_end_time"))
        nextIntent.putExtra("is_update", intent.getBooleanExtra("is_update", true))
    }
}