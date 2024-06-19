package net.baza.bazanetclientapp.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.annotation.RequiresApi
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class RingtoneService : Service() {

    companion object {
        const val ACTION_PLAY_RINGTONE = "PLAY_RINGTONE"
        const val ACTION_STOP_RINGTONE = "STOP_RINGTONE"
    }

    private var context: Context? = null
    private var audioManager: AudioManager? = null
    private var currentVolume: Int = 0

    private var ringtone: Ringtone? = null
    private val executor = ScheduledThreadPoolExecutor(1)
    private val handler = Handler(Looper.getMainLooper())

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = try {
            RingtoneManager.getRingtone(applicationContext, notification)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY_RINGTONE -> {
                if (ringtone != null) {
                    ringtone?.play()
                    ringtone?.isLooping = true

                    executor.schedule({
                        ringtone?.isLooping = false
                        ringtone?.stop()
                        stopSelf()
                    }, 30, TimeUnit.SECONDS) // 30 seconds

                    executor.scheduleWithFixedDelay({
                        handler.post {
                            audioManager =
                                context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                            currentVolume =
                                audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: 0
                            ringtone?.volume = (1 * currentVolume.toFloat()) / 15
                        }
                    }, 0, 500, TimeUnit.MILLISECONDS) // каждую секунду
                }
            }

            ACTION_STOP_RINGTONE -> {
                ringtone?.isLooping = false
                ringtone?.stop()
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        ringtone?.stop()
        executor.shutdownNow()

    }
}