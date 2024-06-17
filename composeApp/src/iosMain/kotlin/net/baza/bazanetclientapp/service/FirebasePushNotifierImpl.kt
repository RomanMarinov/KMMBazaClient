//package net.baza.bazanetclientapp.service
//
//import cocoapods.FirebaseMessaging.FIRMessaging
//import cocoapods.FirebaseMessaging.FIRMessagingDelegateProtocol
//import com.mmk.kmpnotifier.notification.NotifierManager
//import com.mmk.kmpnotifier.notification.NotifierManagerImpl
//import com.mmk.kmpnotifier.notification.PushNotifier
//import kotlinx.cinterop.ExperimentalForeignApi
//import platform.UIKit.UIApplication
//import platform.UIKit.registerForRemoteNotifications
//import platform.darwin.NSObject
//import kotlin.coroutines.resume
//import kotlin.coroutines.suspendCoroutine
//
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
//        override fun messaging(messaging: FIRMessaging, didReceiveRegistrationToken: String?) {
//
//
//
//            println("FirebaseMessaging: didReceive remoteMessage")
//            val userInfo = remoteMessage.appData
//            notifierManager.onPushNotification(userInfo["title"] as? String, userInfo["body"] as? String)
//
//            didReceiveRegistrationToken?.let { token ->
//                println("FirebaseMessaging: onNewToken is called")
//
//                notifierManager.addListener(object : NotifierManager.Listener{
//                    override fun onNewToken(token: String) {
//                        super.onNewToken(token)
//                    }
//                })
//               // notifierManager.onNewToken(didReceiveRegistrationToken)
//            }
//        }
//
//    }
//}
