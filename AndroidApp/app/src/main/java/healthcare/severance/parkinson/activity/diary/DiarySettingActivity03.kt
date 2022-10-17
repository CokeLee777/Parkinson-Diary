package healthcare.severance.parkinson.activity.diary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import healthcare.severance.parkinson.activity.MainActivity
import healthcare.severance.parkinson.R

class DiarySettingActivity03 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_setting03)
    }

    fun backButtonPressed(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        val intent = Intent(this, DiarySettingActivity02::class.java)
        startActivity(intent)
    }

    fun finishSettingButtonPressed(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}