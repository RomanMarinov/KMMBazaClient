package net.baza.bazanetclientapp

import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

actual fun onApplicationStartPlatformSpecific() {
    val configuration = NotificationPlatformConfiguration.Android(
        notificationIconResId = R.drawable.ic_launcher_foreground,
        notificationIconColorResId = R.color.colorBazaMainRed,
        notificationChannelData = NotificationPlatformConfiguration.Android.NotificationChannelData(
            id = "1",
            name = "General"
        ),
        showPushNotification = true // показывать на переднем плане
    )
    NotifierManager.initialize(configuration)

}