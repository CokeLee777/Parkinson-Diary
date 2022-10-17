package healthcare.severance.parkinson.activity.diary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import healthcare.severance.parkinson.activity.MainActivity
import healthcare.severance.parkinson.R

class DiarySettingActivity03 : AppCompatActivity() {

    private lateinit var sleepStartInfo: TextView
    private lateinit var sleepEndInfo: TextView
    private lateinit var medicineTimeInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_setting03)

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
        Log.i(view.resources.getResourceName(view.id),"clicked")

        val intent = Intent(this, DiarySettingActivity02_2::class.java)
        startActivity(intent)
    }

    fun finishSettingButtonPressed(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}