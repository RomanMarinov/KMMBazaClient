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
import com.google.firebase.BuildConfig
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import di.commonModule
import net.baza.bazanetclientapp.R
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin

// koin documentation
//https://insert-koin.io/docs/reference/koin-android/start

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

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

        NotifierManager.initialize(
            NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.ic_home,
                notificationIconColorResId = R.color.color_outdoor_create_shortcut,
                notificationChannelData = NotificationPlatformConfiguration.Android.NotificationChannelData(
                    id = "CHANNEL_ID_GENERAL",
                    name = "General"
                )
            )
        )

//        AppInitializer.initialize(isDebug = BuildConfig.DEBUG) {
//            androidContext(this@MainApplication)
//        }


        startKoin {
            // помогает отслеживать и понимать, что происходит во время инициализации и работы Koin в вашем Android-приложении
            androidLogger()
            // используется для доступа к ресурсам приложения, управления жизненным циклом и других операций, которые требуют доступа к контексту Android
            androidContext(this@MainApplication)
            modules(commonModule())
        }
    }
}





//object AppInitializer {
//
//    fun initialize(isDebug: Boolean = false, onKoinStart: KoinApplication.() -> Unit) {
//        org.koin.core.context.startKoin {
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