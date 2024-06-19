package di

import net.baza.bazanetclientapp.MainActivityViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val mainActivityModule: Module = module {
    factory { MainActivityViewModel(get()) }
}