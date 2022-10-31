package healthcare.severance.parkinson.activity.diary

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import healthcare.severance.parkinson.activity.MainActivity
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.alarm.AlarmReceiver
import healthcare.severance.parkinson.activity.auth.LoginActivity
import healthcare.severance.parkinson.dto.DiaryRequestDto
import healthcare.severance.parkinson.dto.TakeTime
import healthcare.severance.parkinson.service.RetrofitClient
import healthcare.severance.parkinson.service.SessionManager
import healthcare.severance.parkinson.vo.DiaryResponseVo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class DiarySettingActivity03 : AppCompatActivity() {

    private lateinit var sleepStartInfo: TextView
    private lateinit var sleepEndInfo: TextView
    private lateinit var medicineTimeInfo: TextView

    //토큰 정보를 불러오기 위한 세션 매니저 선언
    private lateinit var sessionManager: SessionManager
    private lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_setting03)

        sessionManager = SessionManager(this)
        if(!sessionManager.isAuthenticated()){
            val intent = Intent(this@DiarySettingActivity03, LoginActivity::class.java)
            startActivity(intent)
        }

        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        init(intent)
    }

    fun init(intent: Intent){
        sleepStartInfo = findViewById(R.id.dSleepStartInfo)
        sleepEndInfo = findViewById(R.id.dSleepEndInfo)
        medicineTimeInfo = findViewById(R.id.dMedicineTimeInfo)

        sleepStartInfo.text = String.format("취침시간: %s", intent.getStringExtra("sleep_start_time"))
        sleepEndInfo.text = String.format("기상시간: %s", intent.getStringExtra("sleep_end_time"))
        val takeTimes = intent.getStringArrayListExtra("take_times")!!.sorted()
        val tmp: StringBuilder = StringBuilder("약 복용시간: ")
        for(takeTime in takeTimes){
            tmp.append("$takeTime, ")
        }
        medicineTimeInfo.text = tmp.substring(0, tmp.length - 2)
    }

    fun backButtonPressed(view: View){
        val intent = Intent(this, DiarySettingActivity02_2::class.java)
        startActivity(intent)
    }

    fun finishSettingButtonPressed(view: View){
        val beforeIntent = intent
        val requestTakeTimes = convertRequestData(beforeIntent)
        //알림 세팅
        if(sessionManager.isAlarmActive()){
            setAlarm(requestTakeTimes)
        }
        if(!beforeIntent.getBooleanExtra("is_update", true)){
            createDiary(beforeIntent, requestTakeTimes)
        } else {
            updateDiary(beforeIntent, requestTakeTimes)
        }
    }

    private fun convertRequestData(beforeIntent: Intent): ArrayList<TakeTime>{
        val userInputTakeTimes = beforeIntent.getIntegerArrayListExtra("take_times")
        val requestTakeTimes = arrayListOf<TakeTime>()
        for(i in 0 until userInputTakeTimes!!.size){
            requestTakeTimes.add(TakeTime(userInputTakeTimes[i].toString()))
        }

        return requestTakeTimes
    }

    fun createDiary(beforeIntent: Intent, requestTakeTimes: ArrayList<TakeTime>){
        //서버로 요청
        RetrofitClient.diaryService.createDiary(
            sessionManager.getAccessToken()!!,
            DiaryRequestDto(
                beforeIntent.getStringExtra("sleep_start_time")!!,
                beforeIntent.getStringExtra("sleep_end_time")!!,
                requestTakeTimes
            )
        ).enqueue(object: Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if(response.isSuccessful) {
                    val intent = Intent(this@DiarySettingActivity03, MainActivity::class.java)
                    startActivity(intent)
                } else if(response.code() == 419){
                    Toast.makeText(this@DiarySettingActivity03, "세션이 만료되었습니다", Toast.LENGTH_SHORT).show()
                    sessionManager.unAuthenticate()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Server error", t.message.toString())
                Toast.makeText(this@DiarySettingActivity03, "서버 내부 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateDiary(beforeIntent: Intent, requestTakeTimes: ArrayList<TakeTime>){
        //서버로 요청
        RetrofitClient.diaryService.updateDiary(
            sessionManager.getAccessToken()!!,
            DiaryRequestDto(
                beforeIntent.getStringExtra("sleep_start_time")!!,
                beforeIntent.getStringExtra("sleep_end_time")!!,
                requestTakeTimes
            )
        ).enqueue(object: Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if(response.isSuccessful) {
                    val intent = Intent(this@DiarySettingActivity03, MainActivity::class.java)
                    startActivity(intent)
                } else if(response.code() == 419){
                    Toast.makeText(this@DiarySettingActivity03, "세션이 만료되었습니다", Toast.LENGTH_SHORT).show()
                    sessionManager.unAuthenticate()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Server error", t.message.toString())
                Toast.makeText(this@DiarySettingActivity03, "서버 내부 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun setAlarm(takeTimes: ArrayList<TakeTime>){
        //사용자가 설정한 약 복용시간 세팅
        for(i in 0 until takeTimes.size){
            val intent: PendingIntent =
                Intent(this, AlarmReceiver::class.java).let { intent ->
                    //보류중인 intent 지정, 특정 이벤트(지정된 알림 시간) 발생 시 실행
                    PendingIntent.getBroadcast(this, i, intent, PendingIntent.FLAG_IMMUTABLE)
                }

            Log.i("약 복용시간 세팅",
                String.format("%s:%s", takeTimes[i].takeTime.substring(0, 2), takeTimes[i].takeTime.substring(3, 5)))
            //알림 시간 세팅
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, takeTimes[i].takeTime.substring(0, 2).toInt())
                set(Calendar.MINUTE, takeTimes[i].takeTime.substring(3, 5).toInt())
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            //이미 지난 시간이라면 내일 알림
            if(calendar.before(Calendar.getInstance())){
                calendar.add(Calendar.DATE, 1)
            }
            //정확한 시간에 반복알림(백그라운드 포함)
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,    //실제시간을 기준으로 알람 울리기, 절전모드에서도 동작한다
                calendar.timeInMillis,      //알람이 울릴 시간 지정
                intent
            )
        }
    }
}