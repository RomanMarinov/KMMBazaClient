package net.baza.bazanetclientapp

import co.touchlab.kermit.Logger
import cocoapods.FirebaseMessaging.FIRMessaging
import cocoapods.FirebaseMessaging.FIRMessagingDelegateProtocol
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import com.mmk.kmpnotifier.notification.PushNotifier
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIApplication
import platform.UIKit.registerForRemoteNotifications
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class ServiceNotificationPlatform : NotifierManager.Listener{

    override fun onPayloadData(data: PayloadData) {
        super.onPayloadData(data)
        Logger.d("4444 ServiceNotificationPlatform onPayloadData")
    }

    override fun onPushNotification(title: String?, body: String?) {
        super.onPushNotification(title, body)
        Logger.d("4444 ServiceNotificationPlatform onPushNotification")



    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Logger.d("4444 ServiceNotificationPlatform onNewToken")
    }

//    NotifierManager.addListener(object : NotifierManager.Listener {
//        override fun onPayloadData(data: PayloadData) {
//            println("Push Notification payloadData: $data") //PayloadData is just typeAlias for Map<String,*>.
//        }
//    })

//@OptIn(ExperimentalForeignApi::class)
//class FirebasePushNotifierImpl : PushNotifier {
//
//    init {
//        println("FirebasePushNotifier is initialized")
//        UIApplication.sharedApplication.registerForRemoteNotifications()
//        FIRMessaging.messaging().delegate = FirebaseMessageDelegate()
//    }
//
//
//    override suspend fun getToken(): String? = suspendCoroutine { cont ->
//        FIRMessaging.messaging().tokenWithCompletion { token, error ->
//            cont.resume(token)
//            error?.let { println("Error while getting token: $error") }
//        }
//
//    }
//
//    override suspend fun deleteMyToken() = suspendCoroutine { cont ->
//        FIRMessaging.messaging().deleteTokenWithCompletion {
//            cont.resume(Unit)
//        }
//    }
//
//    override suspend fun subscribeToTopic(topic: String) {
//        FIRMessaging.messaging().subscribeToTopic(topic)
//    }
//
//    override suspend fun unSubscribeFromTopic(topic: String) {
//        FIRMessaging.messaging().unsubscribeFromTopic(topic)
//    }
//
//
//    private class FirebaseMessageDelegate : FIRMessagingDelegateProtocol, NSObject() {
//        private val notifierManager by lazy { NotifierManager }
//
//        override fun messaging(messaging: FIRMessaging, didReceiveRegistrationToken: String?) {
//            // Обработка получения регистрационного токена
//            didReceiveRegistrationToken?.let { token ->
//                println("FirebaseMessaging: onNewToken is called")
//
//                notifierManager.addListener(object : NotifierManager.Listener{
//                    override fun onNewToken(token: String) {
//                        super.onNewToken(token)
//                    }
//                })
//            }
//        }
//
//        override fun messaging(messaging: FIRMessaging, didReceive remoteMessage: FIRMessagingRemoteMessage) {
//            // Обработка получения сообщения
//            Logger.d("FirebaseMessaging: ios didReceive remoteMessage=")
//            val userInfo = remoteMessage.appData
//
//            // Получаем данные из сообщения и передаем их в NotifierManager
//            notifierManager.onPushNotification(userInfo["title"] as? String, userInfo["body"] as? String)
//        }
//    }
//}


}