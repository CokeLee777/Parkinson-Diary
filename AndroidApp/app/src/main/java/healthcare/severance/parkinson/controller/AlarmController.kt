package healthcare.severance.parkinson.controller

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import healthcare.severance.parkinson.activity.alarm.AlarmReceiver
import healthcare.severance.parkinson.activity.survey.SurveyActivity01
import healthcare.severance.parkinson.dto.TakeTime
import java.util.*

class AlarmController(private val context: Context){

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    //알림 세팅
    fun setMedicineAlarm(takeTimes: ArrayList<TakeTime>){

        for(i in 0 until takeTimes.size){
            Log.d("약 복용시간 세팅",
                String.format("%s:%s",
                    takeTimes[i].takeTime.substring(0, 2), takeTimes[i].takeTime.substring(3, 5)))

            val pendingIntent: PendingIntent =
                Intent(context, AlarmReceiver::class.java).let { intent ->
                    //보류중인 intent 지정, 특정 이벤트(지정된 알림 시간) 발생 시 실행
                    PendingIntent.getBroadcast(context, i, intent, PendingIntent.FLAG_IMMUTABLE)
                }

            val hour: Int = takeTimes[i].takeTime.substring(0, 2).toInt()
            val minute: Int = takeTimes[i].takeTime.substring(3, 5).toInt()
            // 시간 세팅
            val calendar = setTime(hour, minute)

            // 정확한 시간에 반복알림(백그라운드 포함)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,    //실제시간을 기준으로 알람 울리기, 절전모드에서도 동작한다
                    calendar.timeInMillis,      //알람이 울릴 시간 지정
                    pendingIntent
                )
            }
        }
    }

    fun cancelMedicineAlarm(takeTimes: ArrayList<TakeTime>){
        Log.d(System.currentTimeMillis().toString(), "약 복용 알람 취소")
        for(i in 0 until takeTimes.size){
            val pendingIntent: PendingIntent =
                Intent(context, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(context, i, intent, PendingIntent.FLAG_IMMUTABLE)
            }
            alarmManager.cancel(pendingIntent)
        }
    }

    fun setSurveyAlarm(hour: Int, minute: Int){
        Log.d("설문조사 알림 세팅", String.format("%s:%s", hour, minute))

        val pendingIntent: PendingIntent =
            Intent(context, SurveyActivity01::class.java).let { intent ->
                //보류중인 intent 지정, 특정 이벤트(지정된 알림 시간) 발생 시 실행
            PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_IMMUTABLE)
        }
        // 시간 세팅
        val calendar = setTime(hour, minute)
        // 시작시간부터 1시간마다 반복 알림
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,      //알람이 울릴 시간 지정
                1000 * 60 * 60, //1시간 마다 반복
                pendingIntent
            )
        }
    }

    fun cancelSurveyAlarm(){
        Log.d(System.currentTimeMillis().toString(), "설문조사 알림 취소")

        val pendingIntent: PendingIntent =
            Intent(context, SurveyActivity01::class.java).let { intent ->
                //보류중인 intent 지정, 특정 이벤트(지정된 알림 시간) 발생 시 실행
            PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_IMMUTABLE)
        }
        alarmManager.cancel(pendingIntent)
    }

    private fun setTime(hour: Int, minute: Int): Calendar {
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        //이미 지난 시간이라면 내일 알림
        if(calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE, 1)
        }

        return calendar
    }


}