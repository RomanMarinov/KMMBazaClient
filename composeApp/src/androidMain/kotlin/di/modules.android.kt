package di

import org.koin.core.module.Module
import AndroidPlatform
import Platform
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import presentation.ui.add_address.AddAddressViewModel
import presentation.ui.attach_photo.AttachPhotoViewModel
import presentation.ui.auth_activity.AuthActivityViewModel
import presentation.ui.domofon_screen.DomofonScreenViewModel
import presentation.ui.help_screen.HelpScreenViewModel
import presentation.ui.history_call.HistoryCallScreenViewModel
import presentation.ui.home_screen.HomeScreenViewModel
import presentation.ui.internet_tv_screen.InternetTvScreenViewModel
import presentation.ui.map_screen.MapScreenViewModel
import presentation.ui.outdoor_screen.OutdoorScreenViewModel
import presentation.ui.payment_service_screen.PaymentServiceViewModel
import presentation.ui.profile_screen.ProfileScreenViewModel
import presentation.ui.profile_screen.address_screen.AddressesScreenViewModel
import presentation.ui.splash_activity.SplashViewModel


actual val viewModelModule2: Module = module {
   // viewModel { SplashViewModel(get(), get()) }
// viewModel {}:
//
//viewModel {} обеспечивает создание и управление экземпляром ViewModel. При использовании viewModel {}, Koin автоматически обрабатывает жизненный цикл ViewModel, гарантируя, что ViewModel будет создан и уничтожен соответствующим образом.
//Экземпляры, созданные с помощью viewModel {}, обычно предназначены для использования в архитектуре MVVM (Model-View-ViewModel).
//factory {}:
//
//factory {} используется для создания нового экземпляра каждый раз, когда он запрашивается. Это означает, что каждый раз, когда компонент запрашивает factory-компонент, ему будет предоставлен новый экземпляр этого компонента.
//Экземпляры, созданные с помощью factory {}, могут быть полезными, когда вам нужно получать новый объект при каждом запросе, а не использовать один и тот же экземпляр.
    viewModel {

        HomeScreenViewModel(get(),get())
    }
    viewModel { OutdoorScreenViewModel(get(),get()) }
    viewModel { DomofonScreenViewModel(get(), get()) }
    viewModel { MapScreenViewModel(get()) }
    viewModel { HelpScreenViewModel(get()) }

    viewModel { InternetTvScreenViewModel(get()) }

    viewModel { AuthActivityViewModel(get(), get()) }

    viewModel { SplashViewModel(get(), get()) }

    viewModel { ProfileScreenViewModel(get(), get()) }

    viewModel { AddAddressViewModel(get(), get()) }

    viewModel { AttachPhotoViewModel(get(), get()) }

    viewModel { HistoryCallScreenViewModel(get(), get()) }

    viewModel { AddressesScreenViewModel(get(), get()) }

    viewModel { PaymentServiceViewModel() }
}

//actual val viewModelModule2 = module {
////    single<Platform> { AndroidPlatform() }
//
//    viewModelOf(::SplashViewModel)
//}
