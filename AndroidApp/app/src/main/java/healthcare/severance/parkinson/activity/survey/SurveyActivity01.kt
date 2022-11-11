package healthcare.severance.parkinson.activity.survey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.diary.DiarySettingActivity01
import healthcare.severance.parkinson.activity.diary.DiarySettingActivity03
import java.time.LocalDateTime

class SurveyActivity01 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey01)

        Log.d("설문조사 푸쉬알림", String.format("%s:%s",
            LocalDateTime.now().hour.toString(), LocalDateTime.now().minute.toString()))
    }

    fun yesButtonPressed(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        val intent = Intent(this, SurveyActivity02::class.java)
        intent.putExtra("hasMedicinalEffect", true)
        startActivity(intent)
    }

    fun noButtonPressed(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        val intent = Intent(this, SurveyActivity02::class.java)
        intent.putExtra("hasMedicinalEffect", false)
        startActivity(intent)
    }
}