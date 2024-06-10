package presentation.ui.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import data.auth.local.AppPreferencesRepository
import domain.model.auth.FingerprintBody
import domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileScreenViewModel(
    private val authRepository: AuthRepository,
    private val appPreferencesRepository: AppPreferencesRepository
) : ViewModel() {

    private var _logout: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val logout: StateFlow<Boolean?> = _logout

    fun logout(fingerprint: String) {
        Logger.d("4444 ProfileScreenViewModel logout fingerprint=" + fingerprint)
        viewModelScope.launch(Dispatchers.IO) {
            val result = authRepository.logOut(
                FingerprintBody(
                    fingerprint = fingerprint
                )
            )
            _logout.value = result
        }
    }
}