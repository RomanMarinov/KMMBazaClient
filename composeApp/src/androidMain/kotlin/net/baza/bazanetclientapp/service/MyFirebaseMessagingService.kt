package net.baza.bazanetclientapp.service

import android.Manifest
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import co.touchlab.kermit.Logger
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
//import com.mmk.kmpnotifier.Constants
//import com.mmk.kmpnotifier.extensions.shouldShowNotification
import com.mmk.kmpnotifier.notification.Notifier
import com.mmk.kmpnotifier.notification.NotifierManager
import data.data_store.AppPreferencesRepository
import domain.notification.NotificationPushCustomizer
import domain.repository.AuthRepository
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import util.TextUtils

//import com.mmk.kmpnotifier.notification.NotifierManagerImpl
//internal actual val platformModule = module {
//
//}
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val notifierManager by lazy { NotifierManager }
    private val notifier: Notifier by lazy { notifierManager.getLocalNotifier() }

    private val notificationPushCustomizer: NotificationPushCustomizer by inject()
    private val appPreferencesRepository: AppPreferencesRepository by inject()
    private val authRepository: AuthRepository by inject()

    private lateinit var accessToken: String
    private lateinit var refreshToken: String
    private lateinit var fingerPrint: String

    private val context: Context by lazy { applicationContext }


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
        Log.d("4444", " onMessageReceived message type=" + message.data["type"])
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            // logManager.writeLogToDB("Входящее событие FirebaseMessagingService message=" + message)
        }

        getProfileData(message = message)

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
//            }
//        })
//      //  notify(id: Int, title: String, body: String, payloadData: Map<String, String>) {
//            permissionUtil.hasNotificationPermission {
//                if (it.not())
//                    Logger.d(
//                        "4444 AndroidNotifier Нужно спрашивать время выполнения " +
//                                "разрешение на уведомление (Manifest.permission.POST_NOTIFICATIONS) в вашей деятельности"
//                    )
//            }
//       val notificationManager = notificationManager ?: return
//
//
//
//
//            val pendingIntent = getPendingIntent(payloadData)
//            notificationChannelFactory.createChannels()
//            val notification = NotificationCompat.Builder(
//                context,
//                androidNotificationConfiguration.notificationChannelData.id
//            ).apply {
//                setSmallIcon(R.drawable.ic_logo_push)
////            setBadgeIconType(R.drawable.ic_home) // не
//                // setLargeIcon(largeIconBitmap) не
//
//                setChannelId(androidNotificationConfiguration.notificationChannelData.id)
//                setContentTitle(title)
//                setContentText(body)
//                // setSmallIcon(androidNotificationConfiguration.notificationIconResId)
//                setAutoCancel(true)
//                setContentIntent(pendingIntent)
//                /////////
//                //  .setLargeIcon(res)
//                setPriority(NotificationCompat.PRIORITY_HIGH) // Приоритет уведомления
//                setVibrate(longArrayOf(100, 1000, 200, 340))
//                setAutoCancel(true) // удаляется после клика
//                setTicker("Notific")
//                setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                setSound(Settings.System.DEFAULT_NOTIFICATION_URI) // звуковой файл по умолчанию для уведомления.
//                setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
//                setDefaults(Notification.DEFAULT_LIGHTS) // включает мигание светодиода уведомления
//                setCategory(NotificationCompat.CATEGORY_ALARM)
//                //.setContentIntent(getCallPendingIntent(context = context, address = address, imageUrl = imageUrl, videoUrl = videoUrl, uuid = uuid))
//                setOngoing(false) // false - уведомление не является постоянным, то есть может быть удалено пользователем.
//                //////////
//                androidNotificationConfiguration.notificationIconColorResId?.let {
//                    color = ContextCompat.getColor(context, it)
//                }
//            }.build()
//
//            notificationManager.notify(id, notification)
//      //  }
////        notifier.notify(
////            id =
////        )
//
//        notifierManager.addListener(object : NotifierManager.Listener {
//            override fun onPayloadData(data: PayloadData) {
//                super.onPayloadData(data)
//                Logger.d("4444 MyFirebaseMessagingService android payloadData: $data")
//
//                val payloadData = message.data
//                message.notification?.let {
//                   // if (notifierManager.shouldShowNotification())
//                        notifier.notify(
//                            title = it.title ?: "",
//                            body = it.body ?: "",
//                            payloadData = payloadData
//                        )
//                    onPushNotification(title = it.title, body = it.body)
//                }
//
//
//            }
//        })
//
//
////
////        val payloadData = message.data
////        message.notification?.let {
////            if (notifierManager.shouldShowNotification())
////                notifier.notify(
////                    title = it.title ?: "",
////                    body = it.body ?: "",
////                    payloadData = payloadData
////                )
////
////            notifierManager.onPushNotification(title = it.title, body = it.body)
////        }
////        if (payloadData.isNotEmpty()) {
////            val data =
////                payloadData + mapOf(Constants.ACTION_NOTIFICATION_CLICK to Constants.ACTION_NOTIFICATION_CLICK)
////            notifierManager.onPushPayloadData(data)
////        }
    }

    private fun getProfileData(message: RemoteMessage) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val jobAccessToken: Deferred<String> = async {
                appPreferencesRepository.fetchInitialPreferences().accessToken
            }

            val jobRefreshToken: Deferred<String> = async {
                appPreferencesRepository.fetchInitialPreferences().refreshToken
            }

            val jobFingerPrint: Deferred<String> = async {
                appPreferencesRepository.fetchInitialPreferences().fingerPrint
            }

            accessToken = jobAccessToken.await()
            refreshToken = jobRefreshToken.await()
            fingerPrint = jobFingerPrint.await()

            checkAndUpdateToken(message = message)
        }
    }

    private suspend fun checkAndUpdateToken(message: RemoteMessage) {
        val scope = CoroutineScope(Dispatchers.IO)

        if (accessToken.isNotEmpty()) { // юзер авторизован
            val needUpdate = TextUtils.isItTimeToUpdateToken(accessToken = accessToken)
            if (needUpdate) { // обновить токен

                Log.d("4444", " BazaNetFirebaseMessagingService обновить токен")
                try {
                    val response = authRepository.refreshTokenSync()
                    if (response?.status?.isSuccess() == true) {
                        checkVersionCode(message = message)
                    } else {
                        scope.launch {
                            //logManager.writeLogToDB("ошибка обновления токена для пуша")
                        }
                        Log.d("4444", " BazaNetFirebaseMessagingService ошибка обновления токена ")
                    }
                } catch (e: Exception) {
                    Log.d("4444", " try catch checkAndUpdateToken e=" + e)
                }
            } else {
                checkVersionCode(message = message)
            }
        } else {
            scope.launch {
                //logManager.writeLogToDB("Нет авторизации для отображения пуша")
            }
            Log.d(
                "4444",
                " BazaNetFirebaseMessagingService токен пустой значит юзер выполнил логаут"
            )
            println("::::::::::::::::::::::::: BazaNetFirebaseMessagingService токен пустой значит юзер выполнил логаут")
        }
    }

    private fun checkVersionCode(message: RemoteMessage) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
           // logManager.writeLogToDB("Попытка отобразить пуш или экран")
        }

        // отобразить пуш или экран
        Log.d("4444", " BazaNetFirebaseMessagingService новое отобразить пуш или экран")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // от 13 и выше
            Log.d("4444", " NotificationManagerImpl requestMultiplePermission 13")
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PermissionChecker.PERMISSION_GRANTED
            Log.d(
                "4444",
                " NotificationManagerImpl requestMultiplePermission 13 granted=" + granted
            )
            if (granted) {
                checkTypeMessageReceived(message = message)
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) { // от 11 до 12
            Log.d("4444", " requestMultiplePermission 11 и 12")
            val notificationsAppEnabled =
                NotificationManagerCompat.from(context).areNotificationsEnabled()
            Log.d(
                "4444",
                " DomofonFragment requestMultiplePermission 11 и 12 notificationsAppEnabled=" + notificationsAppEnabled
            )
            if (notificationsAppEnabled) {
                checkTypeMessageReceived(message = message)
            }
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) { // от 8 до 10
            Log.d("4444", " requestMultiplePermission 10")
            val notificationsAppEnabled =
                NotificationManagerCompat.from(context).areNotificationsEnabled()
            Log.d(
                "4444",
                " DomofonFragment requestMultiplePermission 10 notificationsAppEnabled=" + notificationsAppEnabled
            )
            if (notificationsAppEnabled) {
                checkTypeMessageReceived(message = message)
            }
        }
    }

    private fun checkTypeMessageReceived(message: RemoteMessage) {
        val scope = CoroutineScope(Dispatchers.IO)
        when (message.data["type"]) {
            TypeEventNotification.CALL.value -> {
                scope.launch {
                    //logManager.writeLogToDB("BazaNetFirebaseMessagingService type CALL")
                }
                Log.d("4444", " BazaNetFirebaseMessagingService showNotificationCall ")
                showNotificationCall(message = message)
            }

            TypeEventNotification.MISSED_CALL.value -> {
                scope.launch {
                    //logManager.writeLogToDB("BazaNetFirebaseMessagingService type MISSED_CALL")
                }
                Log.d("4444", " BazaNetFirebaseMessagingService showNotificationMissedCall")
                showNotificationMissedCall(message = message, accessToken = accessToken)
            }

            TypeEventNotification.NOTIFY_INFO.value -> {
                scope.launch {
                    //logManager.writeLogToDB("BazaNetFirebaseMessagingService type NOTIFY_INFO")
                }
                showNotificationInfo(message = message)
            }
        }
    }

    private fun showNotificationCall(message: RemoteMessage) {

        val address = message.data["address"]
        val imageUrl = message.data["image_url"]
        val uuid = message.data["uuid"]
//        val title = message.data["title"]
        val videoUrl = message.data["video_url"]

        notificationPushCustomizer.showNotificationCall(
            address = address ?: "",
            imageUrl = imageUrl ?: "",
            uuid = uuid ?: "",
  //          title = title ?: "",
            videoUrl = videoUrl ?: ""
        )
    }

    private fun showNotificationMissedCall(message: RemoteMessage, accessToken: String) {

        val address = message.data["address"]
        val imageUrl = message.data["image_url"]
        val uuid = message.data["uuid"]
        //val title = message.data["title"]

        notificationPushCustomizer.showNotificationMissedCall(
            address = address ?: "",
            imageUrl = imageUrl ?: "",
            uuid = uuid ?: "",
           // title = title ?: "",
            accessToken = accessToken
        )
    }

    private fun showNotificationInfo(message: RemoteMessage) {

    }

}



//private fun createNotificationChannel() {
//    val importance = android.app.NotificationManager.IMPORTANCE_HIGH
//    val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
//    channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
//    channel.lightColor = Color.BLUE
//    channel.enableLights(true)
//    channel.vibrationPattern =
//        longArrayOf(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000) // 10сек
//
//    NotificationManagerCompat.from(context).createNotificationChannel(channel)
//}