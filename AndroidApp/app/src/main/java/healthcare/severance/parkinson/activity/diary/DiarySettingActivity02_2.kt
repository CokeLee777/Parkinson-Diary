package healthcare.severance.parkinson.activity.diary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.auth.LoginActivity
import healthcare.severance.parkinson.adapter.RecyclerViewAdapter
import healthcare.severance.parkinson.service.SessionManager

class DiarySettingActivity02_2 : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sessionManager: SessionManager

    private val takeTimes: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_setting02_2)

        //로그인한 사용자만 접근 가능
        sessionManager = SessionManager(applicationContext)
        if(!sessionManager.isAuthenticated()){
            val intent = Intent(this@DiarySettingActivity02_2,
                LoginActivity::class.java)
            startActivity(intent)
        }

        init()
    }

    private fun init(){
        //리사이클러뷰 초기화
        recyclerView = findViewById(R.id.dRecyclerView)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        //아이템 바인딩
        val itemLen: Int = intent.getIntExtra("medicine_take_count", 1)
        for(i in 1 .. itemLen) {
            takeTimes.add("")
        }

        val recyclerViewAdapter = RecyclerViewAdapter(takeTimes)
        recyclerView.adapter = recyclerViewAdapter
    }

    fun backButtonPressed(view: View){
        val intent = Intent(this, DiarySettingActivity02_1::class.java)
        maintainIntentExtra(intent)
        startActivity(intent)
    }

    fun nextButtonPressed(view: View){
        if(takeTimes.contains("")){
            Toast.makeText(this@DiarySettingActivity02_2, "복용시간을 입력해주세요",
                Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this@DiarySettingActivity02_2, DiarySettingActivity03::class.java)
            intent.putExtra("take_times", takeTimes)
            maintainIntentExtra(intent)

            startActivity(intent)
        }
    }

    private fun maintainIntentExtra(nextIntent: Intent){
        nextIntent.putExtra("sleep_start_time", intent.getStringExtra("sleep_start_time"))
        nextIntent.putExtra("sleep_end_time", intent.getStringExtra("sleep_end_time"))
        nextIntent.putExtra("medicine_take_count", intent.getIntExtra("medicine_take_count", 1))
        nextIntent.putExtra("is_update", intent.getBooleanExtra("is_update", true))
    }
}