package presentation.ui.splash_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import domain.repository.AuthRepository
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import util.StartActivity
import util.TextUtils

class SplashViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _nextScreen: MutableStateFlow<StartActivity?> = MutableStateFlow(null)
    val nextScreen: StateFlow<StartActivity?> = _nextScreen

//    private val _publicInfo = MutableStateFlow<PublicInfo?>()
//    val publicInfo: StateFlow<PublicInfo?> = _publicInfo

    init {
        getPublicInfoFromServer()
    }

    private fun getPublicInfoFromServer() {
//        viewModelScope.launch(Dispatchers.IO) {
//            authRepository.getPublicInfo().also { data ->
//                data?.let {
//                    _publicInfo.postValue(PublicInfo(data))
//                }
//            }
//        }
    }

    fun checkAndUpdateToken() {
        viewModelScope.launch(Dispatchers.IO) {
            val accessToken = authRepository.getAccessTokenFromPrefs()
            TextUtils.isAccessTokenValid(accessToken = accessToken)  // выводим в консоль сколько ещё годен
            val isItTimeToUpdateToken = TextUtils.isItTimeToUpdateToken(accessToken)
            if (isItTimeToUpdateToken) {
                Logger.d("4444 REFRESH Пора обновить токены, начинаем - SplashScreen/checkTokenAndRefresh()")
                val response = authRepository.refreshTokenSync()
                if (response?.status?.isSuccess() == true) {
                    registerFirebase()
                    _nextScreen.value = StartActivity.MAIN_ACTIVITY
                    Logger.d("4444 REFRESH HOME_ACTIVITY")
                } else if (response?.status?.isSuccess() == false || response == null) {
                    _nextScreen.value = StartActivity.AUTH_ACTIVITY
                    Logger.d("4444 REFRESH AUTH_ACTIVITY")
                }
            } else { // если токен актуален, то регистрируем FireBase и переходим на главную страницу
                Logger.d("REFRESH AccessToken актуален, регистрируем Firebase - SplashScreen/checkTokenAndRefresh()")
                registerFirebase()
                _nextScreen.value = StartActivity.MAIN_ACTIVITY
            }
        }
    }

    private fun registerFirebase() {

    }
}