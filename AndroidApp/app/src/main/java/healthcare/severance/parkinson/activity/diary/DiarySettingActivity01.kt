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
import java.text.SimpleDateFormat
import java.util.*

class DiarySettingActivity01 : AppCompatActivity() {

    private lateinit var sleepStartSelectButton: Button
    private lateinit var sleepEndSelectButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_setting01)

        sleepStartSelectButton = findViewById(R.id.dSleepStartSelectButton)
        sleepEndSelectButton = findViewById(R.id.dSleepEndSelectButton)
    }

    fun sleepStartSelectButtonPressed(view: View){

        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            //사용자가 선택한 시간 세팅
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            //선택한 시간 Display
            sleepStartSelectButton.text = SimpleDateFormat("HH:mm").format(cal.time)
            sleepStartSelectButton.setTextColor(Color.WHITE)
            sleepStartSelectButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)

        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    fun sleepEndSelectButtonPressed(view: View){

        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            //사용자가 선택한 시간 세팅
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            //선택한 시간 Display
            sleepEndSelectButton.text = SimpleDateFormat("HH:mm").format(cal.time)
            sleepEndSelectButton.setTextColor(Color.WHITE)
            sleepEndSelectButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)

        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    fun backButtonPressed(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun nextButtonPressed(view: View){
        Log.i(view.resources.getResourceName(view.id),"clicked")

        val sleepStartTime = sleepStartSelectButton.text
        val sleepEndTime = sleepEndSelectButton.text
        //넘어갈때 값이 있는지 확인
        if(sleepStartTime.isBlank()){
            Toast.makeText(this@DiarySettingActivity01, "취침시간을 선택해주세요", Toast.LENGTH_SHORT).show()
        } else if(sleepEndTime.isBlank()) {
            Toast.makeText(this@DiarySettingActivity01, "기상시간을 선택해주세요", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, DiarySettingActivity02::class.java)
            intent.putExtra("sleep_start_time", sleepStartTime)
            intent.putExtra("sleep_end_time", sleepEndTime)

            startActivity(intent)
        }
    }
}