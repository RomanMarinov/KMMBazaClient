//package net.baza.bazanetclientapp.ui
//
////import org.koin.core.context.startKoin
//
//import android.app.Application
//import com.mmk.kmpnotifier.notification.NotifierManager
//import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
//import di.commonModule
//import net.baza.bazanetclientapp.R
//import org.koin.android.ext.koin.androidContext
//import org.koin.android.ext.koin.androidLogger
//import org.koin.core.KoinApplication
//import org.koin.core.context.GlobalContext.startKoin
//
//// koin documentation
////https://insert-koin.io/docs/reference/koin-android/start
//
//class MainApplication : Application() {
//    override fun onCreate() {
//        super.onCreate()
//
//////        /**
//////         * По умолчанию значение showPushNotification истинно.
//////         * Если для параметра showPushNotification установлено значение false, push-уведомление на переднем плане не будет отображаться пользователю.
//////         * Вы по-прежнему можете получать содержимое уведомлений, используя метод прослушивателя #onPushNotification.
//////         */
////        NotifierManager.initialize(
////            configuration = NotificationPlatformConfiguration.Android(
////                notificationIconResId = R.drawable.ic_launcher_foreground,
////                showPushNotification = true,
////            )
////        )
//
//        NotifierManager.initialize(
//            NotificationPlatformConfiguration.Android(
//                notificationIconResId = R.drawable.ic_home,
//                notificationIconColorResId = R.color.color_outdoor_create_shortcut,
//                notificationChannelData = NotificationPlatformConfiguration.Android.NotificationChannelData(
//                    id = "CHANNEL_ID_GENERAL",
//                    name = "General"
//                )
//            )
//        )
//
//
//        startKoin {
//            // помогает отслеживать и понимать, что происходит во время инициализации и работы Koin в вашем Android-приложении
//            androidLogger()
//            // используется для доступа к ресурсам приложения, управления жизненным циклом и других операций, которые требуют доступа к контексту Android
//            androidContext(this@MainApplication)
//            modules(commonModule())
//        }
//    }
//}


package net.baza.bazanetclientapp.ui

//import org.koin.core.context.startKoin

import android.app.Application
import co.touchlab.kermit.Logger
import com.google.firebase.perf.metrics.AddTrace
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.mmk.kmpnotifier.permission.AndroidPermissionUtil
import di.commonModule
import net.baza.bazanetclientapp.R
import net.baza.bazanetclientapp.di.ContextInitializer
import net.baza.bazanetclientapp.di.onLibraryInitialized
import net.baza.bazanetclientapp.notification.NotifierManagerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

// koin documentation
//https://insert-koin.io/docs/reference/koin-android/start

class MainApplication : Application() {
    @AddTrace(name = "onCreateTrace", enabled = true /* optional */)
    override fun onCreate() {
        super.onCreate()
        Logger.d("4444 MainApplication loaded")




////        /**
////         * По умолчанию значение showPushNotification истинно.
////         * Если для параметра showPushNotification установлено значение false, push-уведомление на переднем плане не будет отображаться пользователю.
////         * Вы по-прежнему можете получать содержимое уведомлений, используя метод прослушивателя #onPushNotification.
////         */
//        NotifierManager.initialize(
//            configuration = NotificationPlatformConfiguration.Android(
//                notificationIconResId = R.drawable.ic_launcher_foreground,
//                showPushNotification = true,
//            )
//        )
//        FirebaseApp.initializeApp(this)



// ПЕРЕПИСАТЬ

        ContextInitializer().create(this)

        val configuration = NotificationPlatformConfiguration.Android(
            notificationIconResId = R.drawable.ic_home,
            notificationIconColorResId = R.color.colorBazaMainRed,
            notificationChannelData = NotificationPlatformConfiguration.Android.NotificationChannelData(
                id = "CHANNEL_ID_GENERAL",
                name = "General"
            )
        )


        NotifierManagerImpl.initialize(configuration = configuration)



//        NotifierManager.initialize(configuration = configuration)
        Logger.d("4444 MainApplication NotifierManager.initialize")

        AppInitializer.initialize(this)



//                // В этом методе вы можете отправить токен уведомления на сервер.
//        NotifierManager.addListener(object : NotifierManager.Listener {
//            override fun onNewToken(token: String) {
//                Logger.d("4444 FirebaseOnNewToken: $token")
//                //AppLogger.d("FirebaseOnNewToken: $token")
//            }
//        })

        // Локальное уведомление
//        val local_notifier = NotifierManager.getLocalNotifier()
//        val notificationId = local_notifier.notify("Title", "Body")
//// or you can use below to specify ID yourself
//        local_notifier.notify(notificationId, "Title", "Body")

//        Прослушивайте изменения токена push-уведомлений
//        В этом методе вы можете отправить токен уведомления на сервер.
//        NotifierManager.addListener(object : NotifierManager.Listener {
//            override fun onNewToken(token: String) {
//                Logger.d("4444 onNewToken: $token") //Update user token in the server if needed
//            }
//        })

////        //Получать сообщения типа уведомления
//        NotifierManager.addListener(object : NotifierManager.Listener {
//            override fun onPushNotification(title: String?, body: String?) {
//                Logger.d("4444 Push Notification notification title: $title")
//                Logger.d("4444 Push Notification notification body: $body")
//            }
//        })
//
//
////
//        // Получение полезных данных
//        NotifierManager.addListener(object : NotifierManager.Listener {
//            override fun onPayloadData(data: PayloadData) {
//                Logger.d("4444 Push Notification payloadData: $data") //PayloadData is just typeAlias for Map<String,*>.
//            }
//        })

//        startKoin {
//            // помогает отслеживать и понимать, что происходит во время инициализации и работы Koin в вашем Android-приложении
//            androidLogger()
//            // используется для доступа к ресурсам приложения, управления жизненным циклом и других операций, которые требуют доступа к контексту Android
//            androidContext(this@MainApplication)
//            modules(commonModule())
//        }



    }


}



object AppInitializer {

    fun initialize(
       // isDebug: Boolean = false,
        //onKoinStart: KoinApplication.() -> Unit,
        context: MainApplication
    ) {
//        startKoin {
//          //  onKoinStart()
//            modules(appModules)
//            onApplicationStart()
//        }

        startKoin {
            // помогает отслеживать и понимать, что происходит во время инициализации и работы Koin в вашем Android-приложении
            androidLogger()
            // используется для доступа к ресурсам приложения, управления жизненным циклом и других операций, которые требуют доступа к контексту Android
            androidContext(context)
          //  onApplicationStart()
            modules(commonModule())
//                .also {
//                it.koin.onLibraryInitialized()
//            }
        }
    }

//    private fun org.koin.core.KoinApplication.onApplicationStart() {
//        NotifierManager.addListener(object : NotifierManager.Listener {
//            override fun onNewToken(token: String) {
//                Logger.d("4444 FirebaseOnNewToken: $token")
//// FirebaseOnNewToken: cx2TV1hpSMi3C9JaqBB21k:APA91bFlxh7vHiqcPtt6-zWEQzPjlD1uTWF-F76AU_Rs7ywn6Yp5QgiVAEBxqZCVlVU4xberwNz1-ObBUed0fMCOcjkbZIY_IaLhYJ10enClnJcbr5iJdf_mR2SrnNnTT7Lqq5Rb7EBi
//            }
//        })
//       // GoogleAuthProvider.create(GoogleAuthCredentials(serverId = "400988245981-u6ajdq65cv1utc6b0j7mtnhc5ap54kbd.apps.googleusercontent.com"))
//    }
}




//object AppInitializer {
//
//    fun initialize(isDebug: Boolean = false, onKoinStart: KoinApplication.() -> Unit) {
//        startKoin {
//            onKoinStart()
//            modules(appModules)
//            onApplicationStart()
//        }
//
//        if (isDebug) AppLogger.initialize()
//
//        Purchases.logLevel = LogLevel.DEBUG
//        Purchases.configure(if (isAndroid()) BuildConfig.REVENUECAT_API_KEY_ANDROID else BuildConfig.REVENUECAT_API_KEY_IOS)
//    }
//
//    private fun KoinApplication.onApplicationStart() {
//        NotifierManager.addListener(object : NotifierManager.Listener {
//            override fun onNewToken(token: String) {
//                AppLogger.d("FirebaseOnNewToken: $token")
//            }
//        })
//        GoogleAuthProvider.create(GoogleAuthCredentials(serverId = "400988245981-u6ajdq65cv1utc6b0j7mtnhc5ap54kbd.apps.googleusercontent.com"))
//    }
//}










//        // В этом методе вы можете отправить токен уведомления на сервер.
//        NotifierManager.addListener(object : NotifierManager.Listener {
//            override fun onNewToken(token: String) {
//                Logger.d("4444 FirebaseOnNewToken: $token")
//                //AppLogger.d("FirebaseOnNewToken: $token")
//            }
//        })
//
//        // Локальное уведомление
////        val local_notifier = NotifierManager.getLocalNotifier()
////        val notificationId = local_notifier.notify("Title", "Body")
////// or you can use below to specify ID yourself
////        local_notifier.notify(notificationId, "Title", "Body")
//
////        Прослушивайте изменения токена push-уведомлений
////        В этом методе вы можете отправить токен уведомления на сервер.
//        NotifierManager.addListener(object : NotifierManager.Listener {
//            override fun onNewToken(token: String) {
//                println("onNewToken: $token") //Update user token in the server if needed
//            }
//        })
//
////        //Получать сообщения типа уведомления
//        NotifierManager.addListener(object : NotifierManager.Listener {
//            override fun onPushNotification(title: String?, body: String?) {
//                println("Push Notification notification title: $title")
//            }
//        })
//
//        // Получение полезных данных
//        NotifierManager.addListener(object : NotifierManager.Listener {
//            override fun onPayloadData(data: PayloadData) {
//                println("Push Notification payloadData: $data") //PayloadData is just typeAlias for Map<String,*>.
//            }
//        })