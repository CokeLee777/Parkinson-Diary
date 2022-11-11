package healthcare.severance.parkinson.activity.alarm

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import healthcare.severance.parkinson.controller.NotificationController
import healthcare.severance.parkinson.util.NotificationProperties.MEDICINE_CHANNEL_ID
import healthcare.severance.parkinson.util.NotificationProperties.MEDICINE_ID
import java.time.LocalDateTime


class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent != null){
            Log.d("약 복용시간 알림", String.format("%s:%s",
                LocalDateTime.now().hour.toString(), LocalDateTime.now().minute.toString()))

            //지연 intent 선언 -> let을 사용하여 마지막 줄의 반환값인 PendingIntent를 반환
            val pendingIntent: PendingIntent =
                Intent(context, AlarmTerminateReceiver::class.java).let { i ->
                PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_IMMUTABLE)
            }

            val notificationController = NotificationController(context)
            //알림 채널 세팅
            notificationController.setChannel(
                MEDICINE_CHANNEL_ID,
                "알람 채널",
                "약 복용 알림 채널"
            )
            //알림 세팅
            notificationController.setNotification(
                MEDICINE_CHANNEL_ID,
                "약 복용시간 알림",
                pendingIntent
            )
            //알림 시작
            notificationController.startAlarmNotify(MEDICINE_ID)
        }
    }


}