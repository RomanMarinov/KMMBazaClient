package net.baza.bazanetclientapp.extensions

import com.mmk.kmpnotifier.notification.NotifierManager
import net.baza.bazanetclientapp.Constants.KEY_IOS_FIREBASE_NOTIFICATION
import com.mmk.kmpnotifier.notification.PayloadData

import net.baza.bazanetclientapp.notification.NotifierManagerImpl
import net.baza.bazanetclientapp.notification.configuration.NotificationPlatformConfiguration
import platform.UserNotifications.UNNotificationContent
/***
 * Чтобы получить полезную нагрузку данных уведомления, необходимо вызвать эту функцию.
 * Приложение iOS Swift сделало функцию ReceiveRemoteNotification
 *
 * Пример:
 *
 * ```
 * func application (_ application: UIApplication, DidReceiveRemoteNotification userInfo: [AnyHashable: Any]) async -> UIBackgroundFetchResult {
 * NotifierManager.shared.onApplicationDidReceiveRemoteNotification (userInfo: userInfo)
 * вернуть UIBackgroundFetchResult.newData
 * }
 * ```
 */
/***
 * Чтобы получить полезную нагрузку данных уведомления, необходимо вызвать эту функцию.
 *  * Приложение iOS Swift сделало функцию ReceiveRemoteNotification
 *
 * Example:
 *
 * ```
 * func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any]) async -> UIBackgroundFetchResult {
 *   NotifierManager.shared.onApplicationDidReceiveRemoteNotification(userInfo: userInfo)
 *   return UIBackgroundFetchResult.newData
 * }
 * ```
 */
public fun NotifierManager.onApplicationDidReceiveRemoteNotification(userInfo: Map<Any?, *>) {
    val payloadData = userInfo.asPayloadData()
    if (payloadData.containsKey(KEY_IOS_FIREBASE_NOTIFICATION))
        NotifierManagerImpl.onPushPayloadData(payloadData)
}

internal fun NotifierManager.onUserNotification(notificationContent: UNNotificationContent) {
    val userInfo = notificationContent.userInfo
    if (notificationContent.isPushNotification()) NotifierManagerImpl.onPushNotification(
        title = notificationContent.title,
        body = notificationContent.body
    )
    NotifierManager.onApplicationDidReceiveRemoteNotification(userInfo)
}

internal fun NotifierManager.onNotificationClicked(notificationContent: UNNotificationContent) {
    NotifierManagerImpl.onNotificationClicked(notificationContent.userInfo.asPayloadData())
}

internal fun NotifierManager.shouldShowNotification(notificationContent: UNNotificationContent): Boolean {
    val configuration =
        NotifierManagerImpl.getConfiguration() as? NotificationPlatformConfiguration.Ios
    val configurationShowPushNotificationEnabled = configuration?.showPushNotification ?: true
    return when {
        notificationContent.isPushNotification() && !configurationShowPushNotificationEnabled -> false
        else -> true
    }
}


internal fun Map<Any?, *>.asPayloadData(): PayloadData {
    return this.keys
        .filterNotNull()
        .filterIsInstance<String>()
        .associateWith { key -> this[key] }
}

private fun UNNotificationContent.isPushNotification(): Boolean {
    return userInfo.containsKey(KEY_IOS_FIREBASE_NOTIFICATION)
}