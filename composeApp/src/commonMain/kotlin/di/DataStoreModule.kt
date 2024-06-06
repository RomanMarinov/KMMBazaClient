package di

import org.koin.core.module.Module

internal expect fun getDatastoreModulePlatform(): Module


//val dataStoreModule = module {
//    single {
//        getDataStore {
//            androidContext().filesDir?.resolve(dataStoreFileName)?.absolutePath
//                ?: throw Exception("Couldn't get Android Datastore context.")
//        }
//    }
//}