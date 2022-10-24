package healthcare.severance.parkinson.activity.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import healthcare.severance.parkinson.activity.alarm.NotificationProperties.NOTIFICATION_ID
import java.time.LocalDateTime

class AlarmTerminateReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("약 복용시간 알림종료", String.format("%s:%s",
            LocalDateTime.now().hour.toString(), LocalDateTime.now().minute.toString()))
        //알림 읽기처리
        NotificationController(context).stopAlarmNotify()
    }
}