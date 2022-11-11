package healthcare.severance.parkinson.controller

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.util.NotificationProperties.MEDICINE_ID

class NotificationController(private val context: Context?) {

    private val notificationManager: NotificationManager
        = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var notification: Notification

    fun setChannel(channelId: String, channelName: String, channelDescription: String){
        notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = channelDescription
            notificationManager.createNotificationChannel(this)
        }
    }

    fun setNotification(channelId: String, title: String, pendingIntent: PendingIntent){
        //알림 객체 생성
        notification = NotificationCompat.Builder(context!!, channelId)
            .setSmallIcon(R.drawable.active_alarm_button)
            .setContentTitle(title)
            .setDefaults(Notification.DEFAULT_LIGHTS)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDeleteIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    fun startAlarmNotify(notificationId: Int){
        //알림 시작
        NotificationManagerCompat.from(context!!).apply {
            // 이전 알람을 취소
            cancel(notificationId)
            //새로운 알람 시작
            notify(notificationId, notification)
            if(notificationId == MEDICINE_ID){
                //알람 소리 시작
                MediaPlayerController.getInstance(context)?.playMusic()
            }
        }
    }

    fun stopAlarmNotify(notificationId: Int){
        notificationManager.cancel(notificationId)
        if(notificationId == MEDICINE_ID) {
            MediaPlayerController.getInstance(context)?.stopMusic()
        }
    }
}