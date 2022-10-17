package healthcare.severance.parkinson.activity

import android.app.Activity
import android.content.Intent
import android.hardware.*
import android.os.Bundle
import android.util.Log
import android.view.View
import healthcare.severance.parkinson.activity.diary.DiarySettingActivity01
import healthcare.severance.parkinson.activity.diary.SettingPageActivity
import healthcare.severance.parkinson.databinding.ActivityMainBinding
import healthcare.severance.parkinson.service.SessionManager
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : Activity(), SensorEventListener {

    // lateinit : 초기화 지연
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SensorManager

    private val executorService: ExecutorService = Executors.newFixedThreadPool(10)

    private lateinit var sensorManager: SensorManager
    private var accelermeterSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    fun startDiary(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        // intent: 개별 구성요소간의 런타임 바인딩을 제공해주는 객체
        val intent = Intent(this, DiarySettingActivity01::class.java)
        // 다이어리 세팅 시작
        startActivity(intent)
    }

    fun setDiary(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        // intent: 개별 구성요소간의 런타임 바인딩을 제공해주는 객체
        val intent = Intent(this, SettingPageActivity::class.java)
        // 다이어리 세팅 시작
        startActivity(intent)
    }

    fun setAlarm(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")
    }
}