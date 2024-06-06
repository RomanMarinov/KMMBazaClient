package presentation.ui.auth_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import domain.model.auth.AuthLoginBody
import domain.model.auth.FingerprintBody
import domain.repository.AuthRepository
import domain.repository.CommonRepository
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthActivityViewModel(
    private val authRepository: AuthRepository,
    private val commonRepository: CommonRepository
) : ViewModel() {

    private var _supportCallNumber: MutableStateFlow<String> = MutableStateFlow("")
    val supportCallNumber: StateFlow<String> = _supportCallNumber

    private var _logInStatusWiFi: MutableStateFlow<String> = MutableStateFlow("")
    val logInStatusWiFi: StateFlow<String> = _logInStatusWiFi

    private var _logInStatusCode: MutableStateFlow<Int> = MutableStateFlow(-1)
    val logInStatusCode: StateFlow<Int> = _logInStatusCode

    init {
        getSupportCallNumber()
    }

    private fun getSupportCallNumber() {
        viewModelScope.launch(Dispatchers.IO) {
            val publicInfo = commonRepository.getPublicInfo()
            _supportCallNumber.value = publicInfo.validateCallNumber
        }
    }

    fun login(authLoginBody: AuthLoginBody) {
        viewModelScope.launch(Dispatchers.IO) {
            _logInStatusCode.value = -1 // установить по умолчанию для повторных запросов
            Logger.d { "4444 ViewPagerAuth authLoginBody=" + authLoginBody }

            val httpResponse: HttpResponse? = authRepository.logIn(authLoginBody = authLoginBody)

            Logger.d { "4444 ViewPagerAuth login=" + httpResponse?.status?.value }
            when (httpResponse?.status?.value) {

                200 -> { // переход на экран home
                    _logInStatusCode.value = 200
                }

                404 -> { // С указанного номера не было звонка"
                    _logInStatusCode.value = 404
                }

                else -> { // ошибка авторизации
                    _logInStatusCode.value = 0
                }
            }
        }
    }

    fun resetIntervalCounters() {
        authRepository.resetIntervalCounters(true)
    }

    fun ipAuthorization(fingerprintBody: FingerprintBody, isInetCellular: Boolean) {
        Logger.d { "4444 ipAuthorization fingerprintBody =" + fingerprintBody + " isInetCellular=" +isInetCellular }

        if (isInetCellular) {
            _logInStatusWiFi.value = "Пожалуйста, проверьте, что устройство подключено по Wi-Fi"
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val httpResponse: HttpResponse? = authRepository.ipAuthorization(fingerprintBody)

                Logger.d { "4444 ipAuthorization httpResponse =" + httpResponse }
                httpResponse?.let {
                    if (it.status.isSuccess()) {
                        when (it.status.value) {
                            200 -> {
                                _logInStatusCode.value = 200
                            }
                        }
                    } else {
                        _logInStatusWiFi.value = "Не удалось авторизоваться по Wi-Fi"
                    }
                }
            }
        }
    }
}