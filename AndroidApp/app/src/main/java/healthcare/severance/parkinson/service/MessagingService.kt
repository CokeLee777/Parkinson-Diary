package healthcare.severance.parkinson.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.survey.SurveyActivity01
import java.util.*
import kotlin.random.Random

class MessagingService: FirebaseMessagingService() {

    private val TAG: String = "FirebaseMessagingService"
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var notification: Notification

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.i(TAG, "설문조사 알림")

        if(message.data != null){
            showNotification(message.data["title"], message.data["body"])
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(TAG, String.format("Received new token=%s", token))
    }



    private fun showNotification(title: String?, body: String?){
        val requestId: Int = Random.nextInt(10000000)

        val notificationManager: NotificationManager
        = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent: PendingIntent =
            Intent(this, SurveyActivity01::class.java).let { intent ->
                //보류중인 intent 지정, 특정 이벤트(지정된 알림 시간) 발생 시 실행
                PendingIntent.getActivity(
                    this,
                    requestId,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT)
            }
        val channelId = "survey_channel_id"
        val channelName = "survey_channel_name"
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                notificationManager.createNotificationChannel(this)
            }
        }

        //알림 객체 생성
        notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.active_alarm_button)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setVibrate(longArrayOf(500,500,500,500,500,500,500,500,500))
            .setAutoCancel(true)
            .build()

        //알림 시작
        NotificationManagerCompat.from(this).apply {
            notify(requestId, notification)
        }
    }
}