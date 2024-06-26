package di

import org.koin.core.module.Module
import net.baza.bazanetclientapp.MainActivityViewModel
import net.baza.bazanetclientapp.ui.IncomingCallViewModel
import org.koin.dsl.module


actual val viewModelModule2: Module = module {
    factory { MainActivityViewModel(get()) }
    factory { IncomingCallViewModel(get()) }
// viewModel {}:
//
//viewModel {} обеспечивает создание и управление экземпляром ViewModel. При использовании viewModel {}, Koin автоматически обрабатывает жизненный цикл ViewModel, гарантируя, что ViewModel будет создан и уничтожен соответствующим образом.
//Экземпляры, созданные с помощью viewModel {}, обычно предназначены для использования в архитектуре MVVM (Model-View-ViewModel).
//factory {}:
//
//factory {} используется для создания нового экземпляра каждый раз, когда он запрашивается. Это означает, что каждый раз, когда компонент запрашивает factory-компонент, ему будет предоставлен новый экземпляр этого компонента.
//Экземпляры, созданные с помощью factory {}, могут быть полезными, когда вам нужно получать новый объект при каждом запросе, а не использовать один и тот же экземпляр.
//    viewModel {
//
//        HomeScreenViewModel(get(),get())
//    }
//    viewModel { OutdoorScreenViewModel(get(),get()) }
//    viewModel { DomofonScreenViewModel(get(), get()) }
//    viewModel { MapScreenViewModel(get()) }
//    viewModel { HelpScreenViewModel(get()) }
//
//    viewModel { InternetTvScreenViewModel(get()) }
//
//    viewModel { AuthActivityViewModel(get(), get()) }
//
//    viewModel { SplashViewModel(get(), get()) }
//
//    viewModel { ProfileScreenViewModel(get(), get()) }
//
//    viewModel { AddAddressViewModel(get(), get()) }
//
//    viewModel { AttachPhotoViewModel(get(), get()) }
//
//    viewModel { HistoryCallScreenViewModel(get(), get()) }
//
//    viewModel { AddressesScreenViewModel(get(), get()) }
//
//    viewModel { PaymentServiceViewModel() }
}

//actual val viewModelModule2 = module {
////    single<Platform> { AndroidPlatform() }
//
//    viewModelOf(::SplashViewModel)
//}
