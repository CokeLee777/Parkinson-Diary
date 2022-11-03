package healthcare.severance.parkinson.activity

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.alarm.AlarmReceiver
import healthcare.severance.parkinson.activity.auth.LoginActivity
import healthcare.severance.parkinson.activity.diary.DiarySettingActivity01
import healthcare.severance.parkinson.activity.diary.SettingPageActivity
import healthcare.severance.parkinson.activity.survey.SurveyActivity01
import healthcare.severance.parkinson.databinding.ActivityMainBinding
import healthcare.severance.parkinson.service.RetrofitClient
import healthcare.severance.parkinson.service.SessionManager
import healthcare.severance.parkinson.vo.DiaryResponseVo
import healthcare.severance.parkinson.vo.TakeTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : Activity(), SensorEventListener {

    // lateinit : 초기화 지연
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var diaryInfo: DiaryResponseVo
    private var isUpdate: Boolean = false

    private var alarmManager: AlarmManager? = null

    private lateinit var alarmButton: ImageButton

    private val executorService: ExecutorService = Executors.newFixedThreadPool(10)

    private lateinit var sensorManager: SensorManager
    private var accelermeterSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
//        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        accelermeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
//
//        //
//        executorService.execute {
//            sensorManager.registerListener(this, accelermeterSensor, SensorManager.SENSOR_DELAY_NORMAL)
//        }
    }

    fun init(){
        sessionManager = SessionManager(this)
        //로그인한 사용자만 접근 가능
        if(!sessionManager.isAuthenticated()){
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //알람 버튼 UI 세팅
        alarmButton = findViewById(R.id.mAlarmButton)
        if(sessionManager.isAlarmActive()){
            alarmButton.setImageResource(R.drawable.active_alarm_button)
        } else {
            alarmButton.setImageResource(R.drawable.inactive_alarm_button)
        }

        //메인 페이지에 접속할 때, 사용자 정보를 가져오도록 함
        getDiaryInfo()
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {

        val sensor = sensorEvent?.sensor
        if(sensor?.type == Sensor.TYPE_LINEAR_ACCELERATION){

            val curTime: Long = System.currentTimeMillis()
            if((curTime - 0) > 1000){
                val x = sensorEvent!!.values[0]
                val y = sensorEvent!!.values[1]
                val z = sensorEvent!!.values[2]

                Log.i(String.format("Time=%s, Acceleration values", (curTime - 0)), String.format("x=%s, y=%s, z=%s", x, y, z))
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.i("onAccuracyChanged", "onAccuracyChanged")
    }

    private fun getDiaryInfo(){
        //서버로 요청
        RetrofitClient.diaryService.getDiary(
            sessionManager.getAccessToken()!!
        ).enqueue(object: Callback<DiaryResponseVo> {
            override fun onResponse(
                call: Call<DiaryResponseVo>,
                response: Response<DiaryResponseVo>
            ) {
                val diaryResponseVo: DiaryResponseVo? = response.body()
                if(response.isSuccessful) {
                    //복용 정보가 있는경우 재설정으로 전달
                    if(diaryResponseVo!!.takeTimes.isNotEmpty()){
                        isUpdate = true
                    }
                    //다이어리 정보 저장
                    diaryInfo = diaryResponseVo

                } else if(response.code() == 419){
                    Toast.makeText(this@MainActivity, "세션이 만료되었습니다",
                        Toast.LENGTH_SHORT).show()
                    sessionManager.unAuthenticate()
                }
            }

            override fun onFailure(call: Call<DiaryResponseVo>, t: Throwable) {
                Log.e("Server error", t.message.toString())
                Toast.makeText(this@MainActivity, "서버 내부 오류",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun startDiary(view: View){
        // intent: 개별 구성요소간의 런타임 바인딩을 제공해주는 객체
        val intent = Intent(this@MainActivity, DiarySettingActivity01::class.java)
        intent.putExtra("is_update", isUpdate)

        // 다이어리 세팅 시작
        startActivity(intent)
    }

    fun setDiary(view: View){
        // intent: 개별 구성요소간의 런타임 바인딩을 제공해주는 객체
        val intent = Intent(this@MainActivity, SettingPageActivity::class.java)
        intent.putExtra("sleep_start_time", diaryInfo.sleepStartTime)
        intent.putExtra("sleep_end_time", diaryInfo.sleepEndTime)
        intent.putExtra("take_times", diaryInfo.takeTimes)

        // 다이어리 세팅 시작
        startActivity(intent)
    }

    /**
     * 버튼 눌림 다시 설정 필요
     */
    fun alarmButtonPressed(view: View){
        val alarmButton = findViewById<ImageButton>(view.id)
        if(sessionManager.isAlarmActive()){
            //세션에 있는 알림정보 inactive로 변경
            sessionManager.updateAlarmIsActive(false)
            //등록된 알람 서비스 취소하기
            cancelAlarm(diaryInfo.takeTimes)
            //설문조사 알림 서비스 취소하기
            cancelNotification()
            //UI 변경
            alarmButton.setImageResource(R.drawable.inactive_alarm_button)
        } else {
            //세션에 있는 알림정보 inactive로 변경
            sessionManager.updateAlarmIsActive(true)
            //사용자가 설정한 알람 서비스 등록하기
            setAlarm(diaryInfo.takeTimes)
            //설문조사 알림 서비스 등록하기
            setSurveyNotification()
            //UI 변경
            alarmButton.setImageResource(R.drawable.active_alarm_button)
        }
    }

    fun cancelAlarm(takeTimes: ArrayList<TakeTime>){
        Log.i(System.currentTimeMillis().toString(), "알림 기능 취소")
        for(i in 0 until takeTimes.size){
            //PendingIntent.FLAG_NO_CREATE : 기존의 생성된 PendingIntent 반환
            val intent: PendingIntent =
                Intent(this, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(this, i, intent, PendingIntent.FLAG_IMMUTABLE)
            }
            //알람 삭제
            alarmManager?.cancel(intent)
        }
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
            alarmManager?.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,    //실제시간을 기준으로 알람 울리기, 절전모드에서도 동작한다
                calendar.timeInMillis,      //알람이 울릴 시간 지정
                intent
            )
        }
    }

    fun setSurveyNotification(){
        Log.i("설문조사 알림 세팅", String.format("%s:%s",
            LocalDateTime.now().hour.toString(), LocalDateTime.now().minute.toString()))

        val intent: PendingIntent =
            Intent(this, SurveyActivity01::class.java).let { intent ->
                //보류중인 intent 지정, 특정 이벤트(지정된 알림 시간) 발생 시 실행
                PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_IMMUTABLE)
            }

        //알림 시간 세팅
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 18)
            set(Calendar.MINUTE, 5)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        //이미 지난 시간이라면 내일 알림
        if(calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE, 1)
        }
        //정확한 시간에 반복알림(백그라운드 포함)
        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,    //실제시간을 기준으로 알람 울리기, 절전모드에서도 동작한다
            calendar.timeInMillis,
            1000*60*60,
            intent
        )
    }

    fun cancelNotification(){
        Log.i(System.currentTimeMillis().toString(), "설문조사 알림 기능 취소")
        //PendingIntent.FLAG_NO_CREATE : 기존의 생성된 PendingIntent 반환
        val intent: PendingIntent =
            Intent(this, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_IMMUTABLE)
            }
        //알람 삭제
        alarmManager?.cancel(intent)
    }
}