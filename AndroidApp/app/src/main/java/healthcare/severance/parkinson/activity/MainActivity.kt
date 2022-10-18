package healthcare.severance.parkinson.activity

import android.app.Activity
import android.content.Intent
import android.hardware.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import healthcare.severance.parkinson.activity.auth.LoginActivity
import healthcare.severance.parkinson.activity.diary.DiarySettingActivity01
import healthcare.severance.parkinson.activity.diary.SettingPageActivity
import healthcare.severance.parkinson.databinding.ActivityMainBinding
import healthcare.severance.parkinson.service.RetrofitClient
import healthcare.severance.parkinson.service.SessionManager
import healthcare.severance.parkinson.vo.DiaryResponse
import healthcare.severance.parkinson.vo.DiaryResponseVo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : Activity(), SensorEventListener {

    // lateinit : 초기화 지연
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var diaryInfo: DiaryResponse
    private var isUpdate: Boolean = false

    private val executorService: ExecutorService = Executors.newFixedThreadPool(10)

    private lateinit var sensorManager: SensorManager
    private var accelermeterSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        //로그인한 사용자만 접근 가능
        if(!sessionManager.isAuthenticated()){
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        getDiaryInfo()

//        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        accelermeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
//
//        //
//        executorService.execute {
//            sensorManager.registerListener(this, accelermeterSensor, SensorManager.SENSOR_DELAY_NORMAL)
//        }
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
                    if(diaryResponseVo!!.data.takeTimes.isNotEmpty()){
                        isUpdate = true
                    }
                    //다이어리 정보 저장
                    diaryInfo = DiaryResponse(
                        diaryResponseVo.data.sleepStartTime,
                        diaryResponseVo.data.sleepEndTime,
                        diaryResponseVo.data.takeTimes)
                } else if(response.code() == 419){
                    Toast.makeText(this@MainActivity, "세션이 만료되었습니다", Toast.LENGTH_SHORT).show()
                    sessionManager.unAuthenticate()
                }
            }

            override fun onFailure(call: Call<DiaryResponseVo>, t: Throwable) {
                Log.e("Server error", t.message.toString())
                Toast.makeText(this@MainActivity, "서버 내부 오류", Toast.LENGTH_SHORT).show()
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

    fun setAlarm(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")
    }
}