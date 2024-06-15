package net.baza.bazanetclientapp.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.Notifier
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.mmk.kmpnotifier.permission.PermissionUtil
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.baza.bazanetclientapp.Constants.ACTION_NOTIFICATION_CLICK
import net.baza.bazanetclientapp.R
import net.baza.bazanetclientapp.extensions.notificationManager
import kotlin.random.Random


// класс для управления отправкой уведомления на android устройстве
internal class AndroidNotifier(
    private val context: Context,
    private val androidNotificationConfiguration: NotificationPlatformConfiguration.Android,
    private val notificationChannelFactory: NotificationChannelFactory,
    private val permissionUtil: PermissionUtil,
) : Notifier {


    override fun notify(title: String, body: String, payloadData: Map<String, String>): Int {
        val notificationID = Random.nextInt(0, Int.MAX_VALUE)
        notify(notificationID, title, body, payloadData)
        return notificationID
    }

    override fun notify(id: Int, title: String, body: String, payloadData: Map<String, String>) {
        permissionUtil.hasNotificationPermission {
            if (it.not())
                Logger.d(
                    "4444 AndroidNotifier Нужно спрашивать время выполнения " +
                            "разрешение на уведомление (Manifest.permission.POST_NOTIFICATIONS) в вашей деятельности"
                )
        }

        val notificationManager = context.notificationManager ?: return
        val pendingIntent = getPendingIntent(payloadData)
        notificationChannelFactory.createChannels()
        val notification = NotificationCompat.Builder(
            context,
            androidNotificationConfiguration.notificationChannelData.id
        ).apply {
            setSmallIcon(R.drawable.ic_logo_push)
//            setBadgeIconType(R.drawable.ic_home) // не
            // setLargeIcon(largeIconBitmap) не

            setChannelId(androidNotificationConfiguration.notificationChannelData.id)
            setContentTitle(title)
            setContentText(body)
            // setSmallIcon(androidNotificationConfiguration.notificationIconResId)
            setAutoCancel(true)
            setContentIntent(pendingIntent)
            /////////
            //  .setLargeIcon(res)
            setPriority(NotificationCompat.PRIORITY_HIGH) // Приоритет уведомления
            setVibrate(longArrayOf(100, 1000, 200, 340))
            setAutoCancel(true) // удаляется после клика
            setTicker("Notific")
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setSound(Settings.System.DEFAULT_NOTIFICATION_URI) // звуковой файл по умолчанию для уведомления.
            setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            setDefaults(Notification.DEFAULT_LIGHTS) // включает мигание светодиода уведомления
            setCategory(NotificationCompat.CATEGORY_ALARM)
            //.setContentIntent(getCallPendingIntent(context = context, address = address, imageUrl = imageUrl, videoUrl = videoUrl, uuid = uuid))
            setOngoing(false) // false - уведомление не является постоянным, то есть может быть удалено пользователем.
            //////////
            androidNotificationConfiguration.notificationIconColorResId?.let {
                color = ContextCompat.getColor(context, it)
            }
        }.build()

        notificationManager.notify(id, notification)
    }

    override fun remove(id: Int) {
        val notificationManager = context.notificationManager ?: return
        notificationManager.cancel(id)
    }

    override fun removeAll() {
        val notificationManager = context.notificationManager ?: return
        notificationManager.cancelAll()
    }

    private fun getPendingIntent(payloadData: Map<String, String>): PendingIntent? {
        val intent = getLauncherActivityIntent()?.apply {
            putExtra(ACTION_NOTIFICATION_CLICK, ACTION_NOTIFICATION_CLICK)
            payloadData.forEach { putExtra(it.key, it.value) }
        }
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        return PendingIntent.getActivity(context, 0, intent, flags)
    }

    private fun getLauncherActivityIntent(): Intent? {
        val packageManager = context.applicationContext.packageManager
        return packageManager.getLaunchIntentForPackage(context.applicationContext.packageName)
    }
}