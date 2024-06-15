package net.baza.bazanetclientapp.di

import android.content.Context
import androidx.startup.Initializer
import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.Notifier

import net.baza.bazanetclientapp.firebase.FirebasePushNotifierImpl
import net.baza.bazanetclientapp.notification.NotificationChannelFactory
import com.mmk.kmpnotifier.notification.PushNotifier
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.mmk.kmpnotifier.permission.AndroidPermissionUtil
import com.mmk.kmpnotifier.permission.PermissionUtil
import net.baza.bazanetclientapp.notification.AndroidNotifier
import net.baza.bazanetclientapp.permission.AndroidMockPermissionUtil
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal lateinit var applicationContext: Context
    private set

internal class ContextInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Logger.d("4444 platformModule ContextInitializer")
        applicationContext = context.applicationContext
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        Logger.d("4444 platformModule dependencies")
        return emptyList()
    }
}

internal actual val platformModule = module {
        Logger.d("4444 platformModule")
//    AndroidMockPermissionUtil
    factory { Platform.Android } bind Platform::class
    single { applicationContext }
    factoryOf(::AndroidMockPermissionUtil) bind PermissionUtil::class
    factory {
        val configuration = get<NotificationPlatformConfiguration>() as NotificationPlatformConfiguration.Android
        AndroidNotifier(
            context = get(),
            androidNotificationConfiguration = configuration,
            notificationChannelFactory = NotificationChannelFactory(
                context = get(),
                channelData = configuration.notificationChannelData
            ),
            permissionUtil = get()
        )
    } bind Notifier::class

    factoryOf(::FirebasePushNotifierImpl) bind PushNotifier::class

}


//internal actual val platformModule = module {
//    factory { Platform.Android } bind Platform::class
//    single { applicationContext }
//    factoryOf(::AndroidPermissionUtil)
//    factory {
//        val configuration =
//            get<NotificationPlatformConfiguration>() as NotificationPlatformConfiguration.Android
//        AndroidNotifier(
//            context = get(),
//            androidNotificationConfiguration = configuration,
//            notificationChannelFactory = NotificationChannelFactory(
//                context = get(),
//                channelData = configuration.notificationChannelData
//            ),
//            permissionUtil = get()
//        )
//    } bind Notifier::class
//
//    factoryOf(::FirebasePushNotifierImpl) bind PushNotifier::class
//
//}

