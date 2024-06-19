package di

import di.network_module.networkModule

fun commonModule() = listOf(
    networkModule,
    viewModelModule2,
    repositoryModule,
    viewModelModule,
    notificationModule,
    getDatastoreModulePlatform()
)