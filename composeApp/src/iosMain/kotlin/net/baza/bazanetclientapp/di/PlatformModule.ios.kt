package net.baza.bazanetclientapp.di

import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.Notifier
import com.mmk.kmpnotifier.notification.PushNotifier
import com.mmk.kmpnotifier.permission.PermissionUtil
import net.baza.bazanetclientapp.firebase.FirebasePushNotifierImpl
import net.baza.bazanetclientapp.notification.IosNotifier
import org.koin.dsl.bind
import org.koin.dsl.module
import net.baza.bazanetclientapp.permission.IosPermissionUtil
import platform.UserNotifications.UNUserNotificationCenter


internal actual val platformModule = module {
    Logger.d("4444 platformModule = module")
    factory { Platform.Ios } bind Platform::class
    factory { IosPermissionUtil(notificationCenter = UNUserNotificationCenter.currentNotificationCenter()) } bind PermissionUtil::class
    factory {
        IosNotifier(
            permissionUtil = get(),
            notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
        )
    } bind Notifier::class

    factory { FirebasePushNotifierImpl() } bind PushNotifier::class
}