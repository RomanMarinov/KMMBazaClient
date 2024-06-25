package di

import org.koin.core.module.Module
import IOSPlatform
import Platform
import org.koin.dsl.module
import presentation.ui.splash_activity.SplashViewModel
import org.koin.core.module.dsl.singleOf
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

actual val viewModelModule2 = module {
//    factory { MainActivityViewModel(get()) }
//    factory { IncomingCallViewModel(get()) }

//    single<Platform> { IOSPlatform() }

//    singleOf(::SplashViewModel)

//    factory {
//
//        HomeScreenViewModel(get(),get())
//    }
//    factory { OutdoorScreenViewModel(get(),get()) }
//    factory { DomofonScreenViewModel(get(), get()) }
//    factory { MapScreenViewModel(get()) }
//    factory { HelpScreenViewModel(get()) }
//
//    factory { InternetTvScreenViewModel(get()) }
//
//    factory { AuthActivityViewModel(get(), get()) }
//
//    factory { SplashViewModel(get(), get()) }
//
//    factory { ProfileScreenViewModel(get(), get()) }
//
//    factory { AddAddressViewModel(get(), get()) }
//
//    factory { AttachPhotoViewModel(get(), get()) }
//
//    factory { HistoryCallScreenViewModel(get(), get()) }
//
//    factory { AddressesScreenViewModel(get(), get()) }
//
//    factory { PaymentServiceViewModel() }
}