package di

import data.auth.local.AppPreferencesRepository
import data.auth.local.dataStoreFileName
import data.auth.local.getDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


internal actual fun getDatastoreModulePlatform() = module {

    single {
        getDataStore {
            androidContext().filesDir?.resolve(dataStoreFileName)?.absolutePath
                ?: throw Exception("Couldn't get Android Datastore context.")
        }
    }

    single { AppPreferencesRepository(get()) }

}