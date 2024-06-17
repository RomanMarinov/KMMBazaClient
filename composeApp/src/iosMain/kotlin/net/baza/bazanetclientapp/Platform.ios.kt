package net.baza.bazanetclientapp

import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.Notifier
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

actual fun onApplicationStartPlatformSpecific() {
    NotifierManager.initialize(
        NotificationPlatformConfiguration.Ios(
            showPushNotification = true,
            askNotificationPermissionOnStart = true
        )
    )

    NotifierManager.addListener(object : NotifierManager.Listener {
        override fun onNewToken(token: String) {
            Logger.d("4444 Push Notification ios onNewToken: $token")
        }

        override fun onPushNotification(title: String?, body: String?) {
            super.onPushNotification(title, body)
            Logger.d("4444 Push Notification ios notification type message is received: Title: $title and Body: $body")
        }

        override fun onPayloadData(data: PayloadData) {
            super.onPayloadData(data)
            Logger.d("4444 Push Notification ios payloadData: $data")
        }

        override fun onNotificationClicked(data: PayloadData) {
            super.onNotificationClicked(data)
            Logger.d("4444 Notification clicked, ios Notification payloadData: $data")
        }
    })
    NotifTest()
}

class NotifTest() : Notifier {
    override fun notify(title: String, body: String, payloadData: Map<String, String>): Int {
        Logger.d("4444 NotifTest payloadData: $payloadData")
return 0
    }

    override fun notify(id: Int, title: String, body: String, payloadData: Map<String, String>) {
        TODO("Not yet implemented")
    }
    
    override fun remove(id: Int) {
        TODO("Not yet implemented")
    }

    override fun removeAll() {
        TODO("Not yet implemented")
    }

}
