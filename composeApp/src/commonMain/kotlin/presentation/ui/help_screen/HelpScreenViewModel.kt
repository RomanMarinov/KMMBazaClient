package presentation.ui.help_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.repository.CommonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.ui.help_screen.model.HelpFaqUiState
import presentation.ui.help_screen.model.HelpOfficesUiState

class HelpScreenViewModel(
    private val commonRepository: CommonRepository
) : ViewModel() {

    private var _faqUiState: MutableStateFlow<HelpFaqUiState> = MutableStateFlow(HelpFaqUiState(emptyList()))
    val faqUiState: StateFlow<HelpFaqUiState> = _faqUiState

    private var _officesUiState: MutableStateFlow<HelpOfficesUiState> = MutableStateFlow(
        HelpOfficesUiState(emptyList()))
    val officesUiState: StateFlow<HelpOfficesUiState> = _officesUiState

    init {
        getPublicInfo()
    }

    private fun getPublicInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = commonRepository.getPublicInfo()

            res.faq?.let { listFaq ->
                _faqUiState.update {
                    it.copy(faq = listFaq)
                }
            }

            res.mapMarkers?.officeCams?.markersOffice?.let { officesCam ->
                _officesUiState.update {
                    it.copy(offices = officesCam)
                }
            }
        }
    }
}