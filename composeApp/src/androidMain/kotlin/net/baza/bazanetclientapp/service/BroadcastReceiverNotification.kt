package net.baza.bazanetclientapp.service

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import net.baza.bazanetclientapp.IncomingCallActivity


class BroadcastReceiverNotification : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        try {

//            val channelId = intent?.getStringExtra("channelID")?.toInt()

            when(intent?.action) {
                "full_screen" -> {
                    Log.d("4444", " intent?.action == full_screen")


                    val channelId = intent?.getStringExtra("channelID")?.toInt()


                    // есть ли у приложения разрешение на отображение наложений поверх других приложений
                    val hasOverlayPermission = Settings.canDrawOverlays(context)

                    val keyguardManager = context?.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                    val isScreenLocked = keyguardManager.isDeviceLocked // позволяет получить информацию о состоянии блокировки экрана
                    Log.d("4444", " BroadcastReceiver Notification hasOverlayPermission=" + hasOverlayPermission  + " isScreenLocked=" + isScreenLocked)

                    val incomingCallIntent = Intent(context, IncomingCallActivity::class.java)
                    incomingCallIntent.putExtra("address", intent.getStringExtra("address"))
                    incomingCallIntent.putExtra("imageUrl", intent.getStringExtra("imageUrl"))
                    incomingCallIntent.putExtra("videoUrl", intent.getStringExtra("videoUrl"))
                    incomingCallIntent.putExtra("channelID", intent.getStringExtra("channelID")?.toInt())
                    incomingCallIntent.putExtra("uuid", intent.getStringExtra("uuid"))

                    incomingCallIntent.flags =
                            // что активность должна быть запущена в новой задаче (также известной как стек активности). Если задача уже существует, новая задача будет создана для активности.
                        Intent.FLAG_ACTIVITY_NEW_TASK
//                            // все задания в задаче (стеке активности) должны быть удалены перед запуском новой активности. Таким образом, все другие активности в стеке будут закрыты.
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
//                            //  если активность уже находится в стеке активности, то все активности поверх нее будут удалены, и она будет приведена в верхнюю часть стека.
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
//                            // если задача (стек активности) будет сброшена системой, все активности в задаче будут удалены.
                    //       Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
//                            // что если активность уже запущена в задаче (стеке активности), она будет перезапущена с ее изначальным состоянием. В противном случае будет создана новая задача для активности.
                    //                  Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                    context.startActivity(incomingCallIntent)
                }
                "click_notification_missed_call" -> {
                    Log.d("4444", " сработал click_notification_missed_call")
                }

                "missed_call_notification_swipe" -> {
                    ringtoneStop(context = context)

                    val channelId = intent?.getStringExtra("channelID")?.toInt()

                    Log.d("4444", " missed_call_notification_swipe channelId=" + channelId)
                    // missed_call_notification_swipe channelId=null
                    context?.let { ctx ->
                        deleteNotification(context = ctx, channelId = channelId)
                    }
                }
                "call_notification_swipe" -> {
                    ringtoneStop(context = context)
                    context?.let { ctx ->
                        val channelId = intent?.getStringExtra("channelID")?.toInt()

                        Log.d("4444", " call_notification_swipe context=" + context)
                        deleteNotification(context = ctx, channelId = channelId)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("4444", " try catch BroadcastReceiverNotification e=" + e)
        }

        if (intent?.action == "kill_screen") {
            Log.d("4444", " intent?.action == kill_screen")
            if (context == null) {
                Log.d("4444", " kill_screen context NULL")
            } else {
                Log.d("4444", " kill_screen context NOT NULL")
            }

            val killIntent = Intent("close_call_activity")
            killIntent.putExtra("close_activity", true)
            context?.let { ctx ->
                LocalBroadcastManager.getInstance(ctx).sendBroadcast(killIntent)
            }
        }
    }

    private fun ringtoneStop(context: Context?) {
        try {
            val intent = Intent(context, RingtoneService::class.java)
            intent.action = RingtoneService.ACTION_STOP_RINGTONE
            context?.startService(intent)
        } catch (e: Exception) {
            Log.d("4444", " try catch Ошибка остановки звука звонка: ", e)
        }
    }

    private fun deleteNotification(context: Context, channelId: Int?) {
        Log.d("4444", " deleteNotification channelId=" + channelId)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        channelId?.let {
            notificationManager.cancel(it)
        }
    }
}