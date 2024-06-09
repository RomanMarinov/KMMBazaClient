package di

import di.network_module.networkModule
import net.baza.bazanetclientapp.di.platformModule

fun commonModule() = listOf(
    networkModule,
    repositoryModule,
    viewModelModule,
    getDatastoreModulePlatform(),
    platformModule
)