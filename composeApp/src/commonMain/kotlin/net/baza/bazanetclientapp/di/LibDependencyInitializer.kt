package net.baza.bazanetclientapp.di

import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.PushNotifier
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.mmk.kmpnotifier.permission.PermissionUtil
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication
import org.koin.dsl.module


internal object LibDependencyInitializer {
    var koinApp: KoinApplication? = null
        private set
    @Throws(Exception::class)
    fun initialize(configuration: NotificationPlatformConfiguration) {
        Logger.d("4444 LibDependencyInitializer initialize")
        if (isInitialized()) return
        val configModule = module {
            single { configuration }
        }

        koinApp = koinApplication {
            Logger.d("4444 LibDependencyInitializer initialize")
            modules(configModule + platformModule)
        }.also {

            it.koin.onLibraryInitialized()
        }
    }
    @Throws(Exception::class)
    fun isInitialized() = koinApp != null
}

@Throws(Exception::class)
fun Koin.onLibraryInitialized() {
    Logger.d("4444 LibDependencyInitializer Library is initialized")
    val permissionUtil by inject<PermissionUtil>()
    val platform by inject<Platform>()
    val configuration by inject<NotificationPlatformConfiguration>()


    get<PushNotifier>() //This will make sure that that when lib is initialized, init method is called

    when (platform) {

        Platform.Android -> Unit //In Android platform permission should be asked in activity

        Platform.Ios -> {
            val askNotificationPermissionOnStart =
                (configuration as? NotificationPlatformConfiguration.Ios)?.askNotificationPermissionOnStart
                    ?: true
            if (askNotificationPermissionOnStart) permissionUtil.askNotificationPermission()
        }
    }
}

