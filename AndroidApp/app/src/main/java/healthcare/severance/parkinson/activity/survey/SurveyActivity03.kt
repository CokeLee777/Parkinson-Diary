package healthcare.severance.parkinson.activity.survey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.MainActivity
import healthcare.severance.parkinson.activity.diary.DiarySettingActivity01
import healthcare.severance.parkinson.activity.diary.DiarySettingActivity03

class SurveyActivity03 : AppCompatActivity() {

    private var medicinalEffect: Boolean = true
    private var abnormalMovement: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey03)

        medicinalEffect = intent.getBooleanExtra("medicinalEffect", true)
        abnormalMovement = intent.getBooleanExtra("abnormalMovement", true)
    }

    fun finishButtonPressed(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}