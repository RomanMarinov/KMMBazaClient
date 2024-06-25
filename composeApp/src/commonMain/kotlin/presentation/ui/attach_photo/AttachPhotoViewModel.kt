package presentation.ui.attach_photo

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import domain.add_address.Data
import domain.model.user_info.Domofon
import domain.model.user_info.Sputnik
import domain.repository.AddAddressRepository
import domain.repository.UserInfoRepository
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import presentation.ui.add_adress_result.model.ResultSendPhoto

class AttachPhotoViewModel(
    private val userInfoRepository: UserInfoRepository,
    private val addAddressRepository: AddAddressRepository
) : ViewModel() {

    private var _resultSendPhoto: MutableStateFlow<ResultSendPhoto> = MutableStateFlow(ResultSendPhoto.DEFAULT)
    val resultSendPhoto: StateFlow<ResultSendPhoto> = _resultSendPhoto

    private var _sputnikCamerasCount: MutableStateFlow<String> = MutableStateFlow("")
    val sputnikCamerasCount: StateFlow<String> = _sputnikCamerasCount

    private val sputnikCameras: MutableStateFlow<List<Sputnik>?> = MutableStateFlow(emptyList())

    private var isNeedAssociatePhoneToServices: MutableState<Boolean?> = mutableStateOf(null)

    init {
        checkPhoneLinkedToAccount()
    }

    private fun checkPhoneLinkedToAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userInfoRepository.getUserInfo().data?.profile?.needAssociatePhoneToServices
            isNeedAssociatePhoneToServices.value = result
        }
    }

    fun sendPhoto(imageByteArray: ByteArray?, addrId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            imageByteArray?.let { image ->
               val response = addAddressRepository.uploadImage(imageByteArray = image, id = addrId)
                response?.let {
                    if (it.status.isSuccess()) {
                        _resultSendPhoto.value = ResultSendPhoto.SUCCESS
                    } else {
                        _resultSendPhoto.value = ResultSendPhoto.FAILURE
                    }
                }
            }
        }
    }



    fun getCountCameras(dataAddress: Data?) {
        viewModelScope.launch {
            val domofons: Domofon? = userInfoRepository.getUserInfo().data?.domofon
            domofons?.let { domof ->

                Logger.d("4444 domof=" + domof.sputnik)
                Logger.d("4444 dataAddress.addrId=" + dataAddress?.addrId)
                val countCameras = domof.sputnik?.filter { it.addrId == dataAddress?.addrId }?.size
                Logger.d("4444 countCameras=" + countCameras)
                when (countCameras) {
                    1 -> {
                        _sputnikCamerasCount.value = "$countCameras камера"
                    }
                    in 2..4 -> {
                        _sputnikCamerasCount.value = "$countCameras камеры"
                    }
                    in 5..15 -> {
                        _sputnikCamerasCount.value = "$countCameras камер"
                    }
                    else -> { "" }
                }

            }
        }
    }
}
