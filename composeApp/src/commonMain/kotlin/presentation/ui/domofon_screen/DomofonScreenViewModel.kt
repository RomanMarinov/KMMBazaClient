package presentation.ui.domofon_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import domain.model.user_info.Domofon
import domain.repository.DomofonRepository
import domain.repository.UserInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.ui.domofon_screen.model.SputnikUiState
import presentation.ui.domofon_screen.model.UnLockState
import util.DomofonUnLockHandler

class DomofonScreenViewModel(
    private val domofonRepository: DomofonRepository,
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {

    private var _domofonUiState: MutableStateFlow<SputnikUiState?> = MutableStateFlow(
        SputnikUiState(Domofon(emptyList(), emptyList())))
    val domofonUiState: StateFlow<SputnikUiState?> = _domofonUiState

    private var _statusDomofonUnlockDoor: MutableStateFlow<UnLockState> = MutableStateFlow(UnLockState.DEFAULT)
    val statusDomofonUnlockDoor: StateFlow<UnLockState> = _statusDomofonUnlockDoor

    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    init {
        getSputnik(isLoading = false)
    }

    fun getSputnik(isLoading: Boolean) {
        viewModelScope.launch {
            _isLoading.value = isLoading
            val domofons: Domofon? = userInfoRepository.getUserInfo().data?.domofon
            domofons?.let { domof ->
                _domofonUiState.update {
                    it?.copy(domofon = domof)
                }
            }
            _isLoading.value = false
        }
    }

    fun onClickUnLock(deviceId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = DomofonUnLockHandler.onClickLock(
                deviceId = deviceId,
                //statusDomofonUnlockDoor = _statusDomofonUnlockDoor,
                domofonRepository = domofonRepository
            )
            result?.let {
                if (it) {
                    _statusDomofonUnlockDoor.value = UnLockState.OPENED_DOOR
                    Logger.d("4444 result true=" + _statusDomofonUnlockDoor.value)
                } else {
                    _statusDomofonUnlockDoor.value = UnLockState.ERROR_OPEN
                    Logger.d("4444 result false=" + _statusDomofonUnlockDoor.value)
                }
            }
        }
    }

    fun resetSnackBarUnLockState() {
        _statusDomofonUnlockDoor.value = UnLockState.DEFAULT
        Logger.d("4444 result default=" + _statusDomofonUnlockDoor.value)
    }
}