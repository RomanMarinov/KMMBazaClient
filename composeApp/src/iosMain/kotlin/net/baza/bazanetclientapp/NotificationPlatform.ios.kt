package net.baza.bazanetclientapp

import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData

actual fun notificationPlatform() {
    NotifierManager.addListener(object : NotifierManager.Listener {
        override fun onPushNotification(title: String?, body: String?) {
            super.onPushNotification(title, body)
            Logger.d("4444 onPushNotification ios bosy=" + body)
        }
        override fun onPayloadData(data: PayloadData) {
            super.onPayloadData(data)
            Logger.d("4444 onPayloadData ios data=" + data)
        }

        override fun onNewToken(token: String) {
            super.onNewToken(token)
            Logger.d("4444 onPayloadData ios token=" + token)
        }
    })

}