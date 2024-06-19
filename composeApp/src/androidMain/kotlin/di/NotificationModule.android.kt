package di

import data.notification.NotificationPushCustomizerImpl
import domain.notification.NotificationPushCustomizer
import org.koin.core.module.Module
import org.koin.dsl.module

actual val notificationModule: Module = module {
        single<NotificationPushCustomizer> { NotificationPushCustomizerImpl(get(), get()) }
}