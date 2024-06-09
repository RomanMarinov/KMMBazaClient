package di

import notification.Notifier
import com.mmk.kmpnotifier.notification.PushNotifier
import com.mmk.kmpnotifier.permission.PermissionUtil
import net.baza.bazanetclientapp.di.Platform
import org.koin.dsl.bind
import org.koin.dsl.module
import permission.IosPermissionUtil
import platform.UserNotifications.UNUserNotificationCenter

internal actual val platformModule = module {
    factory { Platform.Ios } bind Platform::class
    factory { IosPermissionUtil(notificationCenter = UNUserNotificationCenter.currentNotificationCenter()) } bind PermissionUtil::class
    factory {
        notification.IosNotifier(
            permissionUtil = get(),
            notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
        )
    } bind Notifier::class

    factory {
        firebase.FirebasePushNotifierImpl()
    } bind PushNotifier::class
}