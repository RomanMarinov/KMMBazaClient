package di

import org.koin.core.module.Module
import AndroidPlatform
import Platform
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import presentation.ui.home_screen.HomeScreenViewModel

actual val viewModelModule2 = module {
//    single<Platform> { AndroidPlatform() }

    viewModelOf(::HomeScreenViewModel)
}
