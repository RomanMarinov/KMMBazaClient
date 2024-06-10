package presentation.ui.profile_screen.address_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.multiplatform.webview.web.LoadingState
import domain.model.user_info.UserInfo
import domain.repository.AddAddressRepository
import domain.repository.UserInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddressesScreenViewModel(
    private val userInfoRepository: UserInfoRepository,
    private val addressRepository: AddAddressRepository
) : ViewModel() {

    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _addressDeleted: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    var addressDeleted: StateFlow<Boolean?> = _addressDeleted

    private val _userInfo: MutableStateFlow<UserInfo?> = MutableStateFlow(null)
    var userInfo: StateFlow<UserInfo?> = _userInfo

    init {
        getUserInfo(isLoading = false)
    }

    fun getUserInfo(isLoading: Boolean) {
        _isLoading.value = isLoading
        Logger.d("4444 AddressesScreenViewModel getUserInfo")
        viewModelScope.launch(Dispatchers.IO) {
            val result = userInfoRepository.getUserInfo()
          //  Logger.d("4444 getUserInfo result=" + result.data.domofon?.sputnik)
            _userInfo.value = result
            _isLoading.value = false
        }
    }

    fun deleteAddress(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = addressRepository.deleteAddress(id = id)
            _addressDeleted.value = result
            delay(100L)
            _addressDeleted.value = null
//            delay(500L)
//            result?.let {
//                if (it) {
//                  //  getUserInfo() // обновить список
//                }
//            }
        }
    }
}