package healthcare.severance.parkinson.activity.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import healthcare.severance.parkinson.controller.MediaPlayerController
import healthcare.severance.parkinson.controller.MedicineNotificationController
import java.time.LocalDateTime

class AlarmReceiver: BroadcastReceiver() {

    private lateinit var medicineNotificationController: MedicineNotificationController

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("약 복용시간 알림종료", String.format("%s:%s",
            LocalDateTime.now().hour.toString(), LocalDateTime.now().minute.toString()))
        //알림 읽기처리
        medicineNotificationController = MedicineNotificationController(context!!)
        medicineNotificationController.stopNotify()
    }
}