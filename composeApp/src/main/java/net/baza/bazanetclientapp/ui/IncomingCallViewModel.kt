package net.baza.bazanetclientapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import domain.repository.DomofonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import presentation.ui.domofon_screen.model.UnLockState
import util.DomofonUnLockHandler

class IncomingCallViewModel(
    private val domofonRepository: DomofonRepository
) : ViewModel() {

    private var _statusDomofonOpenDoor: MutableStateFlow<UnLockState> = MutableStateFlow(UnLockState.DEFAULT)
    val statusDomofonOpenDoor: StateFlow<UnLockState> = _statusDomofonOpenDoor

    fun onClickUnLock(deviceId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = DomofonUnLockHandler.onClickLock(
                deviceId = deviceId,
                domofonRepository = domofonRepository
            )
            result?.let {
                if (it) {
                    _statusDomofonOpenDoor.value = UnLockState.OPENED_DOOR
                } else {
                    _statusDomofonOpenDoor.value = UnLockState.ERROR_OPEN
                }
            }
        }
    }
}