package healthcare.severance.parkinson.activity.survey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.MainActivity
import healthcare.severance.parkinson.activity.auth.LoginActivity
import healthcare.severance.parkinson.dto.SurveyRequest
import healthcare.severance.parkinson.service.RetrofitClient
import healthcare.severance.parkinson.service.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SurveyActivity03 : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private var hasMedicinalEffect: Boolean = true
    private var hasAbnormalMovement: Boolean = true
    private lateinit var patientCondition: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey03)

        sessionManager = SessionManager(this)
        if(!sessionManager.isAuthenticated()){
            val intent = Intent(this@SurveyActivity03, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    fun finishButtonPressed(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        val beforeIntent: Intent = intent
        hasMedicinalEffect = beforeIntent
            .getBooleanExtra("hasMedicinalEffect", true)
        hasAbnormalMovement = beforeIntent
            .getBooleanExtra("hasAbnormalMovement", true)
        patientCondition = findViewById(R.id.sPatientCondition)

        createSurvey(hasMedicinalEffect, hasAbnormalMovement, patientCondition.progress.toDouble())
    }

    fun createSurvey(hasMedicinalEffect: Boolean,
                     hasAbnormalMovement: Boolean,
                     patientCondition: Double){
        //서버로 요청
        RetrofitClient.surveyService.createSurvey(
            sessionManager.getAccessToken()!!,
            SurveyRequest(
                hasMedicinalEffect,
                hasAbnormalMovement,
                patientCondition
            )
        ).enqueue(object: Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if(response.isSuccessful) {
                    val intent = Intent(this@SurveyActivity03,
                        MainActivity::class.java)
                    startActivity(intent)
                } else if(response.code() == 419){
                    Toast.makeText(this@SurveyActivity03, "세션이 만료되었습니다",
                        Toast.LENGTH_SHORT).show()
                    sessionManager.unAuthenticate()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Server error", t.message.toString())
                Toast.makeText(this@SurveyActivity03, "서버 내부 오류",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }
}