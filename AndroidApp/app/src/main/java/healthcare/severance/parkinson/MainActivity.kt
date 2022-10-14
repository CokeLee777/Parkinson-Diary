package healthcare.severance.parkinson

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import healthcare.severance.parkinson.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun startDiary(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        // intent: 개별 구성요소간의 런타임 바인딩을 제공해주는 객체
        val intent = Intent(this, DiarySettingActivity01::class.java);
        // 다이어리 세팅 시작
        startActivity(intent);
    }

    fun setDiary(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")
    }

    fun setAlarm(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")
    }
}