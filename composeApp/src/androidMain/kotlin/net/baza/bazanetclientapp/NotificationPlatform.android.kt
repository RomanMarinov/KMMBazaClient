package net.baza.bazanetclientapp

import android.util.Log
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import java.util.logging.Logger

actual fun notificationPlatform() {
    NotifierManager.addListener(object : NotifierManager.Listener {
        override fun onNewToken(token: String) {
            Log.d("4444"," Push Notification android onNewToken: $token")
        }

        override fun onPushNotification(title: String?, body: String?) {
            super.onPushNotification(title, body)
            Log.d("4444", " Push Notification  android type message is received: Title: $title and Body: $body")
        }

        override fun onPayloadData(data: PayloadData) {
            super.onPayloadData(data)
            Log.d("4444", " Push Notification android payloadData: $data")
        }

        override fun onNotificationClicked(data: PayloadData) {
            super.onNotificationClicked(data)
            Log.d("4444", " Notification clicked, android  Notification payloadData: $data")
        }
    })

}