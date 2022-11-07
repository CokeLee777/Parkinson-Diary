package healthcare.severance.parkinson.activity.diary

import android.content.Intent
import android.os.Bundle
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

    private var items = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_setting02_2)

        init()
        //로그인한 사용자만 접근 가능
        if(!sessionManager.isAuthenticated()){
            val intent = Intent(this@DiarySettingActivity02_2,
                LoginActivity::class.java)
            startActivity(intent)
        }

        initRecyclerView(intent)
    }

    fun init(){
        sessionManager = SessionManager(applicationContext)
    }

    private fun initRecyclerView(intent: Intent){
        //리사이클러뷰 초기화
        recyclerView = findViewById(R.id.dRecyclerView)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        //아이템 바인딩
        val itemLen: Int = intent.getIntExtra("medicine_take_count", 1)
        for(i in 1 .. itemLen) {
            items.add("")
        }
        val recyclerViewAdapter = RecyclerViewAdapter(items)
        recyclerView.adapter = recyclerViewAdapter
    }

    fun backButtonPressed(view: View){
        val intent = Intent(this, DiarySettingActivity02_1::class.java)
        startActivity(intent)
    }

    fun nextButtonPressed(view: View){
        if(items.contains("")){
            Toast.makeText(this@DiarySettingActivity02_2, "복용시간을 입력해주세요",
                Toast.LENGTH_SHORT).show()
        } else {
            val beforeIntent = intent
            val intent = Intent(this, DiarySettingActivity03::class.java)
            intent.putExtra("sleep_start_time", beforeIntent.getStringExtra("sleep_start_time"))
            intent.putExtra("sleep_end_time", beforeIntent.getStringExtra("sleep_end_time"))
            intent.putExtra("take_times", items)
            intent.putExtra("is_update", beforeIntent.getBooleanExtra("is_update", true))

            startActivity(intent)
        }
    }
}