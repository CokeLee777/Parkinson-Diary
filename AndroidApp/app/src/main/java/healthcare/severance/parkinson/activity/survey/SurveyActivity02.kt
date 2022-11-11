package healthcare.severance.parkinson.activity.survey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import healthcare.severance.parkinson.R

class SurveyActivity02 : AppCompatActivity() {

    private var hasMedicinalEffect: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey02)

        hasMedicinalEffect = intent.getBooleanExtra("hasMedicinalEffect", true)
    }

    fun yesButtonPressed(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        val intent = Intent(this, SurveyActivity03::class.java)
        intent.putExtra("hasMedicinalEffect", hasMedicinalEffect)
        intent.putExtra("hasAbnormalMovement", true)
        startActivity(intent)
    }

    fun noButtonPressed(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        val intent = Intent(this, SurveyActivity03::class.java)
        intent.putExtra("hasMedicinalEffect", hasMedicinalEffect)
        intent.putExtra("hasAbnormalMovement", false)
        startActivity(intent)
    }
}