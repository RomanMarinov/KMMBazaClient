package presentation.ui.history_call

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.model.history_call.HistoryCallAddress
import domain.model.user_info.Sputnik
import domain.repository.HistoryCallRepository
import domain.repository.UserInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import util.GetVideoUrl

class HistoryCallScreenViewModel(
    private val historyCallRepository: HistoryCallRepository,
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {

    private var _historyCalls: MutableStateFlow<List<HistoryCallAddress>> =
        MutableStateFlow(emptyList())
    val historyCalls: StateFlow<List<HistoryCallAddress>> = _historyCalls

    private var domofonList: MutableList<Sputnik> = mutableListOf()
    private var _videoUrl: MutableStateFlow<String> = MutableStateFlow("")
    val videoUrl: StateFlow<String> = _videoUrl

    init {
        getHistoryCalls()
        getDomofonSputnikList()
    }

    private fun getHistoryCalls() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = historyCallRepository.getHistoryCall()
            list?.let {
                _historyCalls.value = it
            } ?: run {
                historyCallRepository.getHistoryCall().also { list ->
                    list?.let {
                        _historyCalls.value = it
                    }
                }
            }
        }
    }

    private fun getDomofonSputnikList() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userInfoRepository.getUserInfo().data?.domofon?.sputnik
            result?.let {
                domofonList = it.toMutableList()
            }
        }
    }

    fun getVideoUrl(deviceID: String, timestamp: Long) {
        domofonList.forEach {
            if (it.deviceID == deviceID) {
                val videoUrl = GetVideoUrl.addFromParameterToUrl(url = it.videoUrl, timestamp = timestamp)
                _videoUrl.value = videoUrl
            }
            return@forEach
        }
    }
}