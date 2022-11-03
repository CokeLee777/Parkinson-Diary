package healthcare.severance.parkinson.activity.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import healthcare.severance.parkinson.activity.alarm.NotificationProperties.ALARM_ID
import java.time.LocalDateTime

class AlarmTerminateReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("약 복용시간 알림종료", String.format("%s:%s",
            LocalDateTime.now().hour.toString(), LocalDateTime.now().minute.toString()))
        //알림 읽기처리
        NotificationController(context).stopAlarmNotify(ALARM_ID)
    }
}