package presentation.ui.outdoor_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import domain.repository.CommonRepository
import domain.repository.UserInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.ui.outdoor_screen.model.OutdoorUiState

class OutdoorScreenViewModel @Throws(Exception::class) constructor(
//    private val outdoorRepository: OutdoorRepository
    private val userInfoRepository: UserInfoRepository,
    private val commonRepository: CommonRepository
) : ViewModel() {

    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var _outDoorsUiState: MutableStateFlow<OutdoorUiState> = MutableStateFlow(OutdoorUiState(emptyList()))
    val outDoorsUiState: StateFlow<OutdoorUiState?> = _outDoorsUiState

    private var _link: MutableStateFlow<String?> = MutableStateFlow("")
    val link: StateFlow<String?> = _link

    init {
        Logger.d("4444 OutdoorScreenViewModel")
        getOutdoors(false)
        getLinkOutdoorDescription()
    }

    fun getOutdoors(isLoading: Boolean) {
        Logger.d("4444 getOutdoors")
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = isLoading
            val outDoors = userInfoRepository.getUserInfo().data.dvr
            outDoors?.let { listDvr ->
                _outDoorsUiState.update {
                    it.copy(outdoors = listDvr)
                }
            }
            _isLoading.value = false
        }
    }

    private fun getLinkOutdoorDescription() {
        viewModelScope.launch(Dispatchers.IO) {
            val link = commonRepository.getPublicInfo().links?.outdoorDVR
            _link.value = link
        }
    }
}