package net.baza.bazanetclientapp.notification

//import com.mmk.kmpnotifier.notification.PayloadData
//import com.mmk.kmpnotifier.notification.PushNotifier
//import di.KMPKoinComponent
//import di.LibDependencyInitializer
//import notification.configuration.NotificationPlatformConfiguration
//import permission.PermissionUtil
//import org.koin.core.component.get

// не трогать
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

//    // закоментил из-за проблем
    fun initialize(configuration: NotificationPlatformConfiguration) {
        LibDependencyInitializer.initialize(configuration)
    }

    fun getConfiguration(): NotificationPlatformConfiguration = get()

    fun getLocalNotifier(): Notifier {
        requireInitialization()
        return get()
    }

    fun getPushNotifier(): PushNotifier {
        requireInitialization()
        return get()
    }

    fun getPermissionUtil(): PermissionUtil {
        requireInitialization()
        return get()
    }

    fun addListener(listener: NotifierManager.Listener) {
        listeners.add(listener)
    }

    fun onNewToken(token: String) {
        listeners.forEach { it.onNewToken(token) }
    }

    fun onPushPayloadData(data: PayloadData) {
        println("Received Push Notification payload data")
        if (listeners.size == 0) println("There is no listener to notify onPushPayloadData")
        listeners.forEach { it.onPayloadData(data) }
    }

    fun onPushNotification(title: String?, body: String?) {
        println("Received Push Notification notification type message")
        if (listeners.size == 0) println("There is no listener to notify onPushNotification")
        listeners.forEach { it.onPushNotification(title = title, body = body) }
    }

    fun onNotificationClicked(data: PayloadData) {
        println("Notification is clicked")
        if (listeners.size == 0) println("There is no listener to notify onPushPayloadData")
        listeners.forEach { it.onNotificationClicked(data) }
    }

    private fun requireInitialization() {
        if (LibDependencyInitializer.isInitialized().not()) throw IllegalStateException(
            "NotifierFactory не инициализирован». " +
                    "Пожалуйста, инициализируйте NotifierFactory, вызвав метод #initialize"
        )
    }

}