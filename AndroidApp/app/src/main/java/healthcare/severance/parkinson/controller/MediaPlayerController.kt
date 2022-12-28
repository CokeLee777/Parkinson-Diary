package healthcare.severance.parkinson.controller

import android.content.Context

import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import kotlin.concurrent.thread

class MediaPlayerController private constructor(private var context: Context?) {

    private var mediaPlayer: MediaPlayer? = null

    companion object {
        var instance: MediaPlayerController? = null

        fun getInstance(context: Context?): MediaPlayerController? {
            if (instance == null) {
                instance = MediaPlayerController(context)
            }
            return instance
        }
    }

    fun playMusic() {
        mediaPlayer = MediaPlayer.create(context,
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))

        thread {
            mediaPlayer!!.apply {
                isLooping = true
            }.start()
            //10초 뒤에 자동으로 종료
            Thread.sleep(10000)
            mediaPlayer!!.stop()
        }
    }

    fun stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
        }
    }
}