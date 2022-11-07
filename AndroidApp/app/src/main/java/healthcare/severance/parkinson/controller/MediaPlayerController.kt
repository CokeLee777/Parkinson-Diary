package healthcare.severance.parkinson.controller

import android.content.Context

import android.media.MediaPlayer
import android.media.RingtoneManager

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