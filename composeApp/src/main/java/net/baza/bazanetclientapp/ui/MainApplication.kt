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
import com.mmk.kmpnotifier.notification.PayloadData
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import net.baza.bazanetclientapp.onApplicationStartPlatformSpecific
import di.commonModule
import net.baza.bazanetclientapp.R
import net.baza.bazanetclientapp.ui.AppInitializer.onApplicationStart
//import net.baza.bazanetclientapp.di.ContextInitializer
//import net.baza.bazanetclientapp.di.onLibraryInitialized
//import net.baza.bazanetclientapp.notification.NotifierManagerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.KoinApplication
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.koinApplication

// koin documentation
//https://insert-koin.io/docs/reference/koin-android/start

class MainApplication : Application() {
    @AddTrace(name = "onCreateTrace", enabled = true /* optional */)
    override fun onCreate() {
        super.onCreate()
        Logger.d("4444 MainApplication loaded")
//        koinApplication {
//            onApplicationStart()
//        }
       //ContextInitializer().create(this)
       // AppInitializer.onApplicationStart()
        AppInitializer.initialize(this)



//
        //onApplicationStartPlatformSpecific()
    }
}



object AppInitializer {
    @Throws(Exception::class)
    fun initialize(
       // isDebug: Boolean = false,
        //onKoinStart: KoinApplication.() -> Unit,
        context: MainApplication
    ) {


//        koinApplication {
////            // помогает отслеживать и понимать, что происходит во время инициализации и работы Koin в вашем Android-приложении
////            androidLogger()
////            // используется для доступа к ресурсам приложения, управления жизненным циклом и других операций, которые требуют доступа к контексту Android
////            androidContext(context)
////            //  onApplicationStart()
////            modules(commonModule())
//////                .also {
//////                it.koin.onLibraryInitialized()
//////            }
////
////            AppInitializer.onApplicationStart()
////            //onApplicationStartPlatformSpecific()
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

           // AppInitializer.onApplicationStart()
            //onApplicationStartPlatformSpecific()
        }


        koinApplication {
            onApplicationStart()
        }
    }



    fun onApplicationStart() {

//        val configuration = NotificationPlatformConfiguration.Android(
//            notificationIconResId = R.drawable.ic_launcher_foreground,
//            notificationIconColorResId = R.color.colorBazaMainRed,
////            notificationChannelData = NotificationPlatformConfiguration.Android.NotificationChannelData(
////                id = "CHANNEL_ID",
////                name = "General"
////            ),
////            showPushNotification = true // показывать на переднем плане
//        )
//
//        NotifierManager.initialize(configuration)

        onApplicationStartPlatformSpecific()
        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {
                Logger.d("4444 Push Notification android onNewToken: $token")
            }

            override fun onPushNotification(title: String?, body: String?) {
                super.onPushNotification(title, body)
                Logger.d("4444 Push Notification  android type message is received: Title: $title and Body: $body")
            }

            override fun onPayloadData(data: PayloadData) {
                super.onPayloadData(data)
                Logger.d("4444 Push Notification android payloadData: $data")
            }

            override fun onNotificationClicked(data: PayloadData) {
                super.onNotificationClicked(data)
                Logger.d("4444 Notification clicked, android  Notification payloadData: $data")
            }
        })
    }
}