package di

import co.touchlab.kermit.Logger
import data.data_store.AppPreferencesRepository
import data.data_store.dataStoreFileName
import data.data_store.getDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


internal actual fun getDatastoreModulePlatform() = module {
    Logger.d("4444 getDatastoreModulePlatform")
    single {
        getDataStore {
            androidContext().filesDir?.resolve(dataStoreFileName)?.absolutePath
                ?: throw Exception("Couldn't get Android Datastore context.")
        }
    }

    single { AppPreferencesRepository(get()) }

}