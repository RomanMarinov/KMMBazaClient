package presentation.ui.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import data.auth.local.AppPreferencesRepository
import domain.model.auth.FingerprintBody
import domain.model.user_info.UserInfo
import domain.repository.AuthRepository
import domain.repository.UserInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileScreenViewModel(
    private val authRepository: AuthRepository,
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {

    private val _userInfo: MutableStateFlow<UserInfo?> = MutableStateFlow(null)
    var userInfo: StateFlow<UserInfo?> = _userInfo

    private var _logout: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val logout: StateFlow<Boolean?> = _logout

    init {
        getUserInfo()
    }

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

    private fun getUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userInfoRepository.getUserInfo()
            _userInfo.value = result
        }
    }
}