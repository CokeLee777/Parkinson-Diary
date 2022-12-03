package healthcare.severance.parkinson.service

import android.app.PendingIntent
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import healthcare.severance.parkinson.activity.alarm.AlarmReceiver
import healthcare.severance.parkinson.activity.auth.LoginActivity
import healthcare.severance.parkinson.activity.survey.SurveyActivity01
import healthcare.severance.parkinson.controller.MedicineNotificationController
import healthcare.severance.parkinson.controller.SurveyNotificationController
import healthcare.severance.parkinson.dto.FcmRegistrationRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random


class MessagingService: FirebaseMessagingService() {

    private val TAG: String = "FirebaseMessagingService"
    private lateinit var medicineNotificationController: MedicineNotificationController
    private lateinit var surveyNotificationController: SurveyNotificationController

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // 알림(알람)과 동시에 화면 켜짐 이벤트 실행
        val powerManager: PowerManager = getSystemService(POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, "parkinson-diary::WakeLock"
        )

        // wakeLock 60초 뒤에 해제되지 않는다면 자동으로 해제
        wakeLock.acquire(1 * 10 * 1000L)

        medicineNotificationController = MedicineNotificationController(this)
        surveyNotificationController = SurveyNotificationController(this)

        val type = message.data["type"]
        val title = message.data["title"]
        val body = message.data["body"]

        Log.i(TAG, title!!)
        if(type.equals("survey")){
            showSurveyNotification(title, body)
        } else if(type.equals("medicine")){
            showMedicineAlarmNotification(title, body)
        }
        // 모든 알림이 끝나고 wakeLock 을 아직 가지고있다면 반환
        if(wakeLock.isHeld){
            wakeLock.release()
        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val sessionManager = SessionManager(applicationContext)

        RetrofitClient.patientsService.reissueFcmRegistrationToken(
            sessionManager.getAccessToken()!!, FcmRegistrationRequest(
                token
            )
        ).enqueue(object: Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if(!response.isSuccessful) {
                    val intent = Intent(this@MessagingService, LoginActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Server error", t.message.toString())
                Toast.makeText(this@MessagingService, "서버 내부 오류",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showMedicineAlarmNotification(title: String?, body: String?){
        val sessionManager = SessionManager(applicationContext)

        //약 복용 히스토리 추가
        val medicineHistoryId = medicineNotificationController
            .createMedicineNotificationHistory(sessionManager.getAccessToken()!!)

        val requestId: Int = Random.nextInt(10000000)
        val pendingIntent: PendingIntent =
        Intent(this, AlarmReceiver::class.java).let { intent ->
            intent.putExtra("medicine_history_id", medicineHistoryId)
            //보류중인 intent 지정, 특정 이벤트(지정된 알림 시간) 발생 시 실행
            PendingIntent.getBroadcast(
                this,
                requestId,
                intent,
                PendingIntent.FLAG_ONE_SHOT)
        }

        medicineNotificationController.startNotify(title!!, requestId, pendingIntent)
    }

    private fun showSurveyNotification(title: String?, body: String?){
        val requestId: Int = Random.nextInt(10000000)
        val pendingIntent: PendingIntent =
            Intent(this, SurveyActivity01::class.java).let { intent ->
                //보류중인 intent 지정, 특정 이벤트(지정된 알림 시간) 발생 시 실행
                PendingIntent.getActivity(
                    this,
                    requestId,
                    intent,
                    PendingIntent.FLAG_ONE_SHOT)
            }

        surveyNotificationController.startNotify(title!!, requestId, pendingIntent)
    }
}