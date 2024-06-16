package presentation.ui.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.NotifierManager
import data.auth.local.AppPreferencesRepository
import domain.model.auth.firebase.FirebaseRequestBody
import domain.model.auth.firebase.FirebaseRequestBodyTEST
import domain.model.user_info.UserInfo
import domain.repository.UserInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
//import net.baza.bazanetclientapp.notification.NotifierManagerImpl
import presentation.ui.splash_activity.GetPlatformName

class HomeScreenViewModel(
    private val userInfoRepository: UserInfoRepository,
    private val appPreferencesRepository: AppPreferencesRepository,

) : ViewModel() {



    private val _userInfo: MutableStateFlow<UserInfo?> = MutableStateFlow(null)
    var userInfo: StateFlow<UserInfo?> = _userInfo

    init {
        Logger.d("4444 HomeScreenViewModel")
        getUserInfo()
    }
    @Throws(Exception::class)
    private fun getUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userInfoRepository.getUserInfo()
            _userInfo.value = result
        }
    }
    @Throws(Exception::class)
    suspend fun sendSelfPush() {
        Logger.d("4444 MainActivityViewModel sendRegisterFireBaseData")


        //val fireBaseToken = NotifierManagerImpl.getPushNotifier().getToken()
            val fireBaseToken = NotifierManager.getPushNotifier().getToken()


        val fingerPrint = appPreferencesRepository.fetchInitialPreferences().fingerPrint
        val platformName = GetPlatformName().getName()
        Logger.d("4444 platformName="+ platformName)
        fireBaseToken?.let {
            val firebaseRequestBody = FirebaseRequestBodyTEST(
                firebaseToken = it,
                fingerprint = fingerPrint,
                version = 1,
                device = platformName,
                title = "Входящий звонок",
                message = "Тестовая ул 1"
            )


//            val firebaseRequestBody = FirebaseRequestBody(
//                firebaseToken = it,
//                fingerprint = fingerPrint,
//                version = 1,
//                device = "android"
//            )
            userInfoRepository.sendSelfPush(body = firebaseRequestBody)
        }

    }
}