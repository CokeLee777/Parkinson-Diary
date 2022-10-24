package healthcare.severance.parkinson.activity.diary

import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.MainActivity
import healthcare.severance.parkinson.activity.auth.LoginActivity
import healthcare.severance.parkinson.service.SessionManager
import java.text.SimpleDateFormat
import java.util.*

class DiarySettingActivity01 : AppCompatActivity() {

    private lateinit var sleepStartSelectButton: Button
    private lateinit var sleepEndSelectButton: Button

    private lateinit var sessionManager: SessionManager

    companion object {
        const val DEFAULT_SLEEP_START_TIME = "22:00"
        const val DEFAULT_SLEEP_END_TIME = "08:00"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_setting01)
        //로그인한 사용자만 접근 가능
        sessionManager = SessionManager(this)
        if(!sessionManager.isAuthenticated()){
            val intent = Intent(this@DiarySettingActivity01, LoginActivity::class.java)
            startActivity(intent)
        }

        initDefaultSleepTime()
    }

    fun initDefaultSleepTime(){
        //버튼 초기화
        sleepStartSelectButton = findViewById(R.id.dSleepStartSelectButton)
        sleepEndSelectButton = findViewById(R.id.dSleepEndSelectButton)
        //취침시간, 기상시간 기본값 설정
        sleepStartSelectButton.text = DEFAULT_SLEEP_START_TIME
        sleepEndSelectButton.text = DEFAULT_SLEEP_END_TIME
    }

    fun sleepStartSelectButtonPressed(view: View){
        //캘린더 객체 생성
        val cal = Calendar.getInstance()
        //TimePicker의 내부에서 실행할 기능들 정의
        val timeSetListener = setTimePickerDialog(cal, sleepStartSelectButton)
        //TimePickerDialog UI를 띄운다
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    fun sleepEndSelectButtonPressed(view: View){
        //캘린더 객체 생성
        val cal = Calendar.getInstance()
        //TimePicker의 내부에서 실행할 기능들 정의
        val timeSetListener = setTimePickerDialog(cal, sleepEndSelectButton)
        //TimePickerDialog UI를 띄운다
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    private fun setTimePickerDialog(cal: Calendar, button: Button): TimePickerDialog.OnTimeSetListener {
        return TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            //사용자가 선택한 시간 세팅
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            //선택한 시간 Display
            button.text = SimpleDateFormat("HH:mm").format(cal.time)
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
        }
    }

    fun backButtonPressed(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun nextButtonPressed(view: View){
        val sleepStartTime = sleepStartSelectButton.text
        val sleepEndTime = sleepEndSelectButton.text
        //넘어갈때 값이 있는지 확인
        if(sleepStartTime.isBlank()){
            Toast.makeText(this@DiarySettingActivity01, "취침시간을 선택해주세요", Toast.LENGTH_SHORT).show()
        } else if(sleepEndTime.isBlank()) {
            Toast.makeText(this@DiarySettingActivity01, "기상시간을 선택해주세요", Toast.LENGTH_SHORT).show()
        } else {
            val beforeIntent = intent
            val intent = Intent(this, DiarySettingActivity02_1::class.java)
            intent.putExtra("sleep_start_time", sleepStartTime)
            intent.putExtra("sleep_end_time", sleepEndTime)
            intent.putExtra("is_update", beforeIntent.getBooleanExtra("is_update", true))

            startActivity(intent)
        }
    }
}