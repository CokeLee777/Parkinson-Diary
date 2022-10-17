package healthcare.severance.parkinson.activity.survey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.diary.DiarySettingActivity01
import healthcare.severance.parkinson.activity.diary.DiarySettingActivity03

class SurveyActivity03 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey03)
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