package healthcare.severance.parkinson.activity.diary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import healthcare.severance.parkinson.R

class DiarySettingActivity02 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_setting02)

        val intent = intent
        Log.i("sleepStartTime=", intent.getStringExtra("sleep_start_time").toString())
        Log.i("sleepEndTime=", intent.getStringExtra("sleep_end_time").toString())
    }

    fun backButtonPressed(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        val intent = Intent(this, DiarySettingActivity01::class.java)
        startActivity(intent)
    }

    fun nextButtonPressed(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        Log.i(intent.extras?.get("patientName").toString(), "intent info")
        val intent = Intent(this, DiarySettingActivity03::class.java)
        startActivity(intent)
    }
}