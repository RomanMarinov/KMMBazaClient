package net.baza.bazanetclientapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.data_store.AppPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import util.LifeCycleState

class MainActivityViewModel(
    private val appPreferencesRepository: AppPreferencesRepository
) : ViewModel() {

    fun saveLifeCycleState(state: String) {
        viewModelScope.launch(Dispatchers.IO) {
            appPreferencesRepository.saveLifeCycleState(state = state)
        }
    }
}