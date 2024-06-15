package di

import di.network_module.networkModule

fun commonModule() = listOf(
    networkModule,
    repositoryModule,
    viewModelModule,
    getDatastoreModulePlatform()
)