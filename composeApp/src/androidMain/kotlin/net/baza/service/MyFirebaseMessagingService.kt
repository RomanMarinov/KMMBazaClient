package net.baza.service

import android.util.Log
import co.touchlab.kermit.Logger
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
//import com.mmk.kmpnotifier.Constants
//import com.mmk.kmpnotifier.extensions.shouldShowNotification
import com.mmk.kmpnotifier.notification.Notifier
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import org.koin.dsl.module

//import com.mmk.kmpnotifier.notification.NotifierManagerImpl
//internal actual val platformModule = module {
//
//}
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val notifierManager by lazy { NotifierManager }
    private val notifier: Notifier by lazy { notifierManager.getLocalNotifier() }


//    NotifierManager.addListener(object : NotifierManager.Listener {----------
//        override fun onNewToken(token: String) {
//            Logger.d("4444 Push Notification ios onNewToken: $token")
//        }
//
//        override fun onPushNotification(title: String?, body: String?) {
//            super.onPushNotification(title, body)
//            Logger.d("4444 Push Notification  ios type message is received: Title: $title and Body: $body")
//        }
//
//        override fun onPayloadData(data: PayloadData) {
//            super.onPayloadData(data)
//            Logger.d("4444 Push Notification ios payloadData: $data")
//        }
//
//        override fun onNotificationClicked(data: PayloadData) {
//            super.onNotificationClicked(data)
//            Logger.d("4444 Notification clicked, ios  Notification payloadData: $data")
//        }
//    })

//
//    private val notifierManager by lazy { NotifierManagerImpl }
//    private val notifier: Notifier by lazy { notifierManager.getLocalNotifier() }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("MyFirebaseMessagingService: onNewToken is called")

        notifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {
                Logger.d("4444 Push Notification ios onNewToken: $token")
            }
        })
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Logger.d("4444 MyFirebaseMessagingService android message: $message")
        val address = message.data["address"]
        val imageUrl = message.data["image_url"]
        val uuid = message.data["uuid"]
        val title = message.data["title"]
        val videoUrl = message.data["video_url"]

        Log.d("4444", " address=" + address + "\nimageUrl=" + imageUrl + "\nuuid=" + uuid + "\ntitle=" + title + "\nvideoUrl=" + videoUrl)

//        notifierManager.addListener(object : NotifierManager.Listener {
//            override fun onPushNotification(title: String?, body: String?) {
//                super.onPushNotification(title, body)
//
//
//            }
//        })



        notifierManager.addListener(object : NotifierManager.Listener {
            override fun onPayloadData(data: PayloadData) {
                super.onPayloadData(data)
                Logger.d("4444 MyFirebaseMessagingService android payloadData: $data")

                val payloadData = message.data
                message.notification?.let {
                   // if (notifierManager.shouldShowNotification())
                        notifier.notify(
                            title = it.title ?: "",
                            body = it.body ?: "",
                            payloadData = payloadData
                        )
                    onPushNotification(title = it.title, body = it.body)
                }


            }
        })


//
//        val payloadData = message.data
//        message.notification?.let {
//            if (notifierManager.shouldShowNotification())
//                notifier.notify(
//                    title = it.title ?: "",
//                    body = it.body ?: "",
//                    payloadData = payloadData
//                )
//
//            notifierManager.onPushNotification(title = it.title, body = it.body)
//        }
//        if (payloadData.isNotEmpty()) {
//            val data =
//                payloadData + mapOf(Constants.ACTION_NOTIFICATION_CLICK to Constants.ACTION_NOTIFICATION_CLICK)
//            notifierManager.onPushPayloadData(data)
//        }
    }
}