package healthcare.severance.parkinson.activity.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.se.omapi.Session
import android.util.Log
import healthcare.severance.parkinson.controller.MediaPlayerController
import healthcare.severance.parkinson.controller.MedicineNotificationController
import healthcare.severance.parkinson.service.SessionManager
import java.time.LocalDateTime

class AlarmReceiver: BroadcastReceiver() {

    private lateinit var medicineNotificationController: MedicineNotificationController

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("약 복용시간 알림종료", String.format("%s:%s",
            LocalDateTime.now().hour.toString(), LocalDateTime.now().minute.toString()))

        medicineNotificationController = MedicineNotificationController(context!!)
        //알람 복용완료 업데이트
        val sessionManager = SessionManager(context.applicationContext)
        val medicineHistoryId = intent!!.getStringExtra("medicine_history_id")
        medicineNotificationController
            .updateMedicineNotificationHistory(
                sessionManager.getAccessToken()!!,
                medicineHistoryId!!
            )
        //알림 소리 끄기
        medicineNotificationController.stopNotify()
    }
}