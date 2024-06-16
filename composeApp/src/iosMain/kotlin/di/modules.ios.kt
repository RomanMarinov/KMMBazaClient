package di

import org.koin.core.module.Module
import IOSPlatform
import Platform
import org.koin.dsl.module
import presentation.ui.home_screen.HomeScreenViewModel
import org.koin.core.module.dsl.singleOf

actual val viewModelModule2 = module {
//    single<Platform> { IOSPlatform() }

    singleOf(::HomeScreenViewModel)
}