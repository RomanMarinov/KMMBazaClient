package presentation.ui.splash_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.NotifierManager
import data.data_store.AppPreferencesRepository
import domain.model.auth.firebase.FirebaseRequestBody
import domain.repository.AuthRepository
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
//import net.baza.bazanetclientapp.notification.NotifierManagerImpl
import util.StartActivity
import util.TextUtils

class SplashViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _nextScreen: MutableStateFlow<StartActivity?> = MutableStateFlow(null)
    val nextScreen: StateFlow<StartActivity?> = _nextScreen

//    private val _publicInfo = MutableStateFlow<PublicInfo?>()
//    val publicInfo: StateFlow<PublicInfo?> = _publicInfo

    private var _fireBaseToken = MutableStateFlow("")

    val scope = CoroutineScope(Dispatchers.Main)

    init {
        getPublicInfoFromServer()
        getFireBaseToken()
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

    private fun getFireBaseToken() {
        scope.launch {
          //  _fireBaseToken.value = NotifierManager.getPushNotifier().getToken().toString()
        }
    }

    fun checkAndUpdateToken(fireBaseToken: String?, fingerPrint: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val accessToken = authRepository.getAccessTokenFromPrefs()
            TextUtils.isAccessTokenValid(accessToken = accessToken)  // выводим в консоль сколько ещё годен
            val isItTimeToUpdateToken = TextUtils.isItTimeToUpdateToken(accessToken)
            if (isItTimeToUpdateToken) {
                Logger.d("4444 REFRESH Пора обновить токены, начинаем - SplashScreen/checkTokenAndRefresh()")
                val response = authRepository.refreshTokenSync()
                if (response?.status?.isSuccess() == true) {
                   // registerFirebase()
                    sendRegisterFireBaseData(fireBaseToken, fingerPrint)
                    _nextScreen.value = StartActivity.MAIN_ACTIVITY
                    Logger.d("4444 REFRESH HOME_ACTIVITY")
                } else if (response?.status?.isSuccess() == false || response == null) {
                    _nextScreen.value = StartActivity.AUTH_ACTIVITY
                    Logger.d("4444 REFRESH AUTH_ACTIVITY")
                }
            } else { // если токен актуален, то регистрируем FireBase и переходим на главную страницу
                Logger.d("REFRESH AccessToken актуален, регистрируем Firebase - SplashScreen/checkTokenAndRefresh()")
               // registerFirebase()
                sendRegisterFireBaseData(fireBaseToken, fingerPrint)
                _nextScreen.value = StartActivity.MAIN_ACTIVITY
            }
        }
    }

    private suspend fun sendRegisterFireBaseData(fireBaseToken: String?, fingerPrint: String) {
        Logger.d("4444 MainActivityViewModel sendRegisterFireBaseData")
        // переместить ан ui
        //val fireBaseToken1 = NotifierManager.getPushNotifier().getToken()
        val fire = getFirebaseTokenPlatform()
       // Logger.d("4444 LOGGER HUYOGER fireBaseToken=" + fireBaseToken)

        scope.launch {
//            val fireBaseToken1 = NotifierManager.getPushNotifier().getToken()
        }

        authRepository.setFingerPrint(fingerPrint = fingerPrint)
//        val fingerPrint = appPreferencesRepository.fetchInitialPreferences().fingerPrint
        val platformName = GetPlatformName().getName()

        Logger.d("4444 platformName="+ platformName)
        val firebaseRequestBody = FirebaseRequestBody(
            firebaseToken = fire,
            fingerprint = fingerPrint,
            version = 250,
            device = platformName
        )
        // пока не решу проблему на ios
        authRepository.sendRegisterFireBaseData(firebaseRequestBody = firebaseRequestBody)


//        var typeToken = ""
//        try {
//            val tokenData: TokenData = appAuth.getFirebaseTokenFromPrefs()
////            val versionCode: Int = BuildConfig.VERSION_CODE
//            val versionCode: Int = 1
//
//            var response: Response<OurServer>? = null
//
//            when(tokenData.typeToken) {
//                net.baza.bazanetclientapp.Constants.Auth.HUAWEI_TOKEN -> {
//                    typeToken = tokenData.typeToken
//                    response = repository.sendRegisterFireBaseData(
//                        firebaseToken = tokenData.token,
//                        fingerprint = fingerprint,
//                        versionCode = versionCode,
//                        typeToken = tokenData.typeToken
//                    )
//                }
//                net.baza.bazanetclientapp.Constants.Auth.FIREBASE_TOKEN -> {
//                    typeToken = tokenData.typeToken
//                    response = repository.sendRegisterFireBaseData(
//                        firebaseToken = tokenData.token,
//                        fingerprint = fingerprint,
//                        versionCode = versionCode,
//                        typeToken = tokenData.typeToken
//                    )
//                }
//            }
//
//            return if (response?.isSuccessful == false) { // если ошибка FireBase, то удаляем запись из Prefs
//                appAuth.removeFirebaseTokenFromPrefs()
//                when(typeToken) {
//                    net.baza.bazanetclientapp.Constants.Auth.HUAWEI_TOKEN -> {
//                        logManager.writeLogToDB("Не удалось зарегистрировать HuaweiT")
//                        writeAndSendFirebaseErrorLog("Ошибка получения токена Huawei")
//                    }
//                    net.baza.bazanetclientapp.Constants.Auth.FIREBASE_TOKEN -> {
//                        logManager.writeLogToDB("Не удалось зарегистрировать Firebase")
//                        writeAndSendFirebaseErrorLog("Ошибка получения токена Firebase")
//                    }
//                }
//                false
//            } else {
//                when(typeToken) {
//                    net.baza.bazanetclientapp.Constants.Auth.HUAWEI_TOKEN -> {
//                        logManager.writeLogToDB("Успешно зарегистрировали Huawei")
//                    }
//                    net.baza.bazanetclientapp.Constants.Auth.FIREBASE_TOKEN -> {
//                        logManager.writeLogToDB("Успешно зарегистрировали Firebase")
//                    }
//                }
//                true
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            when(typeToken) {
//                net.baza.bazanetclientapp.Constants.Auth.HUAWEI_TOKEN -> {
//                    logManager.writeLogToDB("Ошибка регистрации Huawei")
//                }
//                net.baza.bazanetclientapp.Constants.Auth.FIREBASE_TOKEN -> {
//                    logManager.writeLogToDB("Ошибка регистрации Firebase")
//                }
//            }


          //  return false
      //  }

       // return true
    }

    fun initHuinit() {

    }
}