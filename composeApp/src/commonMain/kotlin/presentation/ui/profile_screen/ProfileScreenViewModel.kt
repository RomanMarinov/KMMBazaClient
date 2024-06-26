package presentation.ui.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import data.data_store.AppPreferencesRepository
import domain.model.auth.FingerprintBody
import domain.model.user_info.UserInfo
import domain.repository.AuthRepository
import domain.repository.UserInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import presentation.ui.profile_screen.email_item.SendEmailState

class ProfileScreenViewModel(
    private val authRepository: AuthRepository,
    private val userInfoRepository: UserInfoRepository,
    private val appPreferencesRepository: AppPreferencesRepository
) : ViewModel() {

    private val _userInfo: MutableStateFlow<UserInfo?> = MutableStateFlow(null)
    var userInfo: StateFlow<UserInfo?> = _userInfo

    private var _logout: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val logout: StateFlow<Boolean?> = _logout

    private var _responseSendEmail: MutableStateFlow<SendEmailState> =
        MutableStateFlow(SendEmailState.DEFAULT)
    val responseSendEmail: StateFlow<SendEmailState> = _responseSendEmail

    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        getUserInfo(false)
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

    fun getUserInfo(isLoading: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = isLoading
            val result = userInfoRepository.getUserInfo()
            _userInfo.value = result
            _isLoading.value = false
        }
    }

    fun clearDataStore() {
        viewModelScope.launch(Dispatchers.IO) {
            appPreferencesRepository.clear()
        }
    }

    fun sendActualEmailToServer(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = userInfoRepository.sendActualEmailToServer(email = email)
            if (response) {
                _responseSendEmail.value = SendEmailState.SUCCESS
            } else {
                _responseSendEmail.value = SendEmailState.FAILURE
            }

        }
    }

    fun resetSendEmailState() {
        _responseSendEmail.value = SendEmailState.DEFAULT
    }
}