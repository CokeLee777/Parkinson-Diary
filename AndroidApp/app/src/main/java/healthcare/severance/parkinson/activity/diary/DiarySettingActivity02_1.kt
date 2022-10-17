package healthcare.severance.parkinson.activity.diary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import healthcare.severance.parkinson.R

class DiarySettingActivity02_1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_setting02_1)
    }

    fun backButtonPressed(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        val intent = Intent(this, DiarySettingActivity01::class.java)
        startActivity(intent)
    }

    fun nextButtonPressed(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        val medicineCount = findViewById<TextView>(R.id.dMedicineCountForm).text
        if(medicineCount.isBlank() || medicineCount.isEmpty()){
            Toast.makeText(this@DiarySettingActivity02_1, "복용횟수를 입력해주세요", Toast.LENGTH_SHORT).show()
        } else {
            val beforeIntent = intent
            val intent = Intent(this, DiarySettingActivity02_2::class.java)
            intent.putExtra("medicine_take_count", medicineCount.toString().toInt())
            intent.putExtra("sleep_start_time", beforeIntent.getStringExtra("sleep_start_time"))
            intent.putExtra("sleep_end_time", beforeIntent.getStringExtra("sleep_end_time"))

            startActivity(intent)
        }
    }
}