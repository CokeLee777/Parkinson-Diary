package healthcare.severance.parkinson.activity.alarm

import android.content.Context

import android.media.MediaPlayer
import android.media.RingtoneManager

class AlarmController private constructor(private var context: Context?) {

    private var mediaPlayer: MediaPlayer? = null

    companion object {
        var instance: AlarmController? = null

        fun getInstance(context: Context?): AlarmController? {
            if (instance == null) {
                instance = AlarmController(context)
            }
            return instance
        }
    }

    fun playMusic() {
        mediaPlayer = MediaPlayer.create(context,
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
        mediaPlayer!!.apply {
            isLooping = true
        }.start()
    }

    fun stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
        }
    }
}