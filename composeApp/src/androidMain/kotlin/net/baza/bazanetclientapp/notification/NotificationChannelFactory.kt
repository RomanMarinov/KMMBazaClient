package net.baza.bazanetclientapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import net.baza.bazanetclientapp.extensions.notificationManager

// не трогать
internal class NotificationChannelFactory(
    private val context: Context,
    private val channelData: NotificationPlatformConfiguration.Android.NotificationChannelData,
) {
    fun createChannels() {
        Logger.d("4444 NotificationChannelFactory createChannels 1")
        val notificationManager = context.notificationManager ?: return
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        Logger.d("4444 NotificationChannelFactory createChannels 2 ")
        val channel = NotificationChannel(
            channelData.id,
            channelData.name,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            this.description = channelData.description
            enableLights(true)
        }
        notificationManager.createNotificationChannel(channel)
    }
}