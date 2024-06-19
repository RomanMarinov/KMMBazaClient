package di

import org.koin.core.module.Module
import org.koin.dsl.module

expect val notificationModule: Module

//val notificationModule: Module = module {
//    single<NotificationPushCustomizer> { NotificationPushCustomizerImpl() }
//}