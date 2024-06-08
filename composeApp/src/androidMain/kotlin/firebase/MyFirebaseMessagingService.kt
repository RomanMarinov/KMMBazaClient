//package firebase
//
//import com.google.firebase.messaging.FirebaseMessagingService
//
//internal class MyFirebaseMessagingService : FirebaseMessagingService() {
//
//    private val notifierManager by lazy { NotifierManagerImpl }
//    private val notifier: Notifier by lazy { notifierManager.getLocalNotifier() }
//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//        println("FirebaseMessaging: onNewToken is called")
//        notifierManager.onNewToken(token)
//    }
//
//    override fun onMessageReceived(message: RemoteMessage) {
//        super.onMessageReceived(message)
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
//    }
//}