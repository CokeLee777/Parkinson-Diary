package healthcare.severance.parkinson.controller

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import healthcare.severance.parkinson.R
import healthcare.severance.parkinson.activity.MainActivity
import healthcare.severance.parkinson.activity.alarm.AlarmReceiver
import healthcare.severance.parkinson.activity.auth.LoginActivity
import healthcare.severance.parkinson.activity.survey.SurveyActivity01
import healthcare.severance.parkinson.dto.TakeTime
import healthcare.severance.parkinson.service.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.random.Random

class MedicineNotificationController(private val context: Context){

    private val TAG: String = "MEDICINE_NOTIFICATION_CONTROLLER"
    private lateinit var  notificationManager: NotificationManager

    fun startNotify(title: String, requestId: Int, pendingIntent: PendingIntent) {
        notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // 알림 채널 생성
        val notificationChannelId = createNotificationChannel()
        //알림 객체 생성
        val notification = createNotification(notificationChannelId, title, pendingIntent)
        //알림 시작
        startNotification(requestId, notification)
    }

    fun stopNotify(){
        MediaPlayerController.getInstance(context)?.stopMusic()
    }

    private fun startNotification(requestId: Int, notification: Notification) {
        NotificationManagerCompat.from(context).apply {
            //새로운 알람 시작
            notify(requestId, notification)
            //알람 소리 시작
            MediaPlayerController.getInstance(context)?.playMusic()
        }
    }

    private fun createNotification(
        channelId: String,
        title: String,
        pendingIntent: PendingIntent
    ) = NotificationCompat.Builder(context!!, channelId)
        .setSmallIcon(R.drawable.active_alarm_button)
        .setContentTitle(title)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)
        .setDeleteIntent(pendingIntent)
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        .setVibrate(longArrayOf(500, 500, 500, 500, 500, 500, 500, 500, 500))
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

    fun registrantMedicineNotification(accessToken: String) {
        RetrofitClient.medicineService.registerMedicineNotification(
            accessToken
        ).enqueue(object: Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if(response.isSuccessful) {
                    Log.d(TAG, "복용시간 알람 등록")
                } else {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Server error", t.message.toString())
            }
        })
    }

    fun cancelMedicineNotification(accessToken: String) {
        RetrofitClient.medicineService.cancelMedicineNotification(
            accessToken
        ).enqueue(object: Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if(response.isSuccessful) {
                    Log.d(TAG, "복용시간 알람 취소")
                } else {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Server error", t.message.toString())
            }
        })
    }

}