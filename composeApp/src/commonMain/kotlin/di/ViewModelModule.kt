package di

//import presentation.ui.map_screen.MapScreenViewModel
import co.touchlab.kermit.Logger
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

val viewModelModule = module {
    Logger.d("4444 viewModelModule")
    // factory определение, чтобы не хранить какие-либо экземпляры в памяти (избегайте утечек в жизненном цикле Android):
    // Функция get()позволяет попросить Koin разрешить необходимую зависимость.

    factory { HomeScreenViewModel(get(),get()) }
    factory { OutdoorScreenViewModel(get(),get()) }
    factory { DomofonScreenViewModel(get(), get()) }
    factory { MapScreenViewModel(get()) }
    factory { HelpScreenViewModel(get()) }

    factory { InternetTvScreenViewModel(get()) }

    factory { AuthActivityViewModel(get(), get()) }

    factory { SplashViewModel(get()) }

    factory { ProfileScreenViewModel(get(), get(), get()) }

    factory { AddAddressViewModel(get(), get()) }

    factory { AttachPhotoViewModel(get(), get()) }

    factory { HistoryCallScreenViewModel(get(), get()) }

    factory { AddressesScreenViewModel(get(), get()) }

    factory { PaymentServiceViewModel() }

}

