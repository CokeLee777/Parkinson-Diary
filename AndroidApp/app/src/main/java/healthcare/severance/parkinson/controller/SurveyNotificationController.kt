package healthcare.severance.parkinson.controller

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.auth.LoginActivity
import healthcare.severance.parkinson.service.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class SurveyNotificationController(private val context: Context?) {

    private val TAG: String = "SURVEY_NOTIFICATION_CONTROLLER"
    private val notificationManager: NotificationManager
        = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun startNotify(title: String, requestId: Int, pendingIntent: PendingIntent) {
        // 알림 채널 생성
        val notificationChannelId = createNotificationChannel()
        //알림 객체 생성
        val notification = createNotification(notificationChannelId, title, pendingIntent)
        //알림 시작
        startNotification(requestId, notification)
    }

    private fun startNotification(requestId: Int, notification: Notification) {
        NotificationManagerCompat.from(context!!).apply {
            notify(requestId, notification)
        }
    }

    private fun createNotification(
        channelId: String,
        title: String,
        pendingIntent: PendingIntent
    ) = NotificationCompat.Builder(context!!, channelId)
        .setSmallIcon(R.drawable.survey_notification_icon)
        .setContentTitle(title)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        .setVibrate(longArrayOf(500,500,500,500,500,500,500,500,500))
        .setAutoCancel(true)
        .build()

    private fun createNotificationChannel(): String {
        val channelId = "survey_channel_id"
        val channelName = "survey_channel_name"

        NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            notificationManager.createNotificationChannel(this)
        }

        return channelId
    }

    fun registrantSurveyNotification(accessToken: String) {
        RetrofitClient.surveyService.registerSurveyNotification(
            accessToken
        ).enqueue(object: Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if(response.isSuccessful) {
                    Log.d(TAG, "설문조사 알림 등록")
                } else {
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Server error", t.message.toString())
            }
        })
    }

    fun cancelSurveyNotification(accessToken: String) {
        RetrofitClient.surveyService.cancelSurveyNotification(
            accessToken
        ).enqueue(object: Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if(response.isSuccessful) {
                    Log.d(TAG, "설문조사 알림 등록 취소")
                } else {
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Server error", t.message.toString())
            }
        })
    }
}