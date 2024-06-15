package net.baza.bazanetclientapp.notification

import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.Notifier
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import com.mmk.kmpnotifier.notification.PushNotifier
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.mmk.kmpnotifier.permission.PermissionUtil
import net.baza.bazanetclientapp.di.KMPKoinComponent
import net.baza.bazanetclientapp.di.LibDependencyInitializer

import org.koin.core.component.get

internal object NotifierManagerImpl : KMPKoinComponent() {
    private val listeners = mutableListOf<NotifierManager.Listener>()

//    // закоментил из-за проблем
//    fun initialize(configuration: NotificationPlatformConfiguration) {
//        di.LibDependencyInitializer.initialize(configuration)
//    }
@Throws(Exception::class)
    fun initialize(configuration: NotificationPlatformConfiguration) {
        Logger.d("4444 NotifierManagerImpl initialize")
        LibDependencyInitializer.initialize(configuration)
    }
    @Throws(Exception::class)
    fun getConfiguration(): NotificationPlatformConfiguration = get()
    @Throws(Exception::class)
    fun getLocalNotifier(): Notifier {
        Logger.d("4444 NotifierManagerImpl getLocalNotifier")
        requireInitialization()
        return get()
    }
    @Throws(Exception::class)
    fun getPushNotifier(): PushNotifier {
        Logger.d("4444 NotifierManagerImpl getPushNotifier")
        requireInitialization()
        return get()
    }
    @Throws(Exception::class)
    fun getPermissionUtil(): PermissionUtil {
        requireInitialization()
        return get()
    }
    @Throws(Exception::class)
    fun addListener(listener: NotifierManager.Listener) {
        listeners.add(listener)
    }
    @Throws(Exception::class)
    fun onNewToken(token: String) {
        Logger.d("4444 NotifierManagerImpl onNewToken")
        listeners.forEach { it.onNewToken(token) }
    }
    @Throws(Exception::class)
    fun onPushPayloadData(data: PayloadData) {
        Logger.d("4444 NotifierManagerImpl Received Push Notification payload data")
        if (listeners.size == 0) Logger.d("4444 Нет прослушивателя для уведомления onPushPayloadData.")
        listeners.forEach { it.onPayloadData(data) }
    }
    @Throws(Exception::class)
    fun onPushNotification(title: String?, body: String?) {
        Logger.d("4444 NotifierManagerImpl Received Push Notification notification type message")
        if (listeners.size == 0) Logger.d("4444 Нет прослушивателя для уведомления onPushNotification.")
        listeners.forEach {
            it.onPushNotification(title = title, body = body)
        }
    }
    @Throws(Exception::class)
    fun onNotificationClicked(data: PayloadData) {
        Logger.d("4444 NotifierManagerImpl Notification is clicked")
        if (listeners.size == 0) Logger.d("4444 Нет прослушивателя для уведомления onPushPayloadData.")
        listeners.forEach { it.onNotificationClicked(data) }
    }
    @Throws(Exception::class)
    private fun requireInitialization() {
        Logger.d("4444 NotifierManagerImpl requireInitialization")
        if (LibDependencyInitializer.isInitialized().not()) throw IllegalStateException(
            "NotifierFactory не инициализирован Пожалуйста, инициализируйте NotifierFactory, вызвав метод #initialize"
        )
    }
}