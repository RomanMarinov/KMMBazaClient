package presentation.ui.internet_tv_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import domain.model.home.locations_internet_tv.DataLocations
import domain.model.home.tariffs_by_location.DataTariffs
import domain.model.home.tariffs_by_location.LocationsTariffsBody
import domain.model.home.tariffs_by_location.PackageTariffCheckable
import domain.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InternetTvScreenViewModel(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private var _locationsTitle: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val locationsTitle: StateFlow<List<String>> = _locationsTitle

    private var _locations: MutableStateFlow<List<DataLocations>> = MutableStateFlow(emptyList())
    private val locations: StateFlow<List<DataLocations>> = _locations


    private var _locationTariffsByLocation: MutableStateFlow<List<DataTariffs>> = MutableStateFlow(emptyList())
    val locationTariffsByLocation: StateFlow<List<DataTariffs>> = _locationTariffsByLocation

    private var _selectedLocation: MutableStateFlow<DataLocations?> = MutableStateFlow(null)
    val selectedLocation: StateFlow<DataLocations?> = _selectedLocation

    init {
        getLocationsInternetTv()
        // getTariffsByLocation()
    }

    private fun getLocationsInternetTv() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                homeRepository.getLocationsInternetTv().collect { locList ->
                    val sortList = locList.sortedWith(
                        compareBy {
                            when {
                                it.name.startsWith("г") -> 0
                                it.name.startsWith("рп") -> 1
                                it.name.startsWith("п") -> 2
                                it.name.startsWith("д") -> 3
                                it.name.startsWith("с") -> 4
                                else -> 5
                            }
                        }
                    )

                    _locations.value = sortList

                    val titleSortList = sortList.map { it.name }
                    _locationsTitle.value = titleSortList
                }
            } catch (e: Exception) {
                Logger.d { "4444 try catch e=" + e }
            }

        }
    }

    fun getTariffsByLocation(body: LocationsTariffsBody) {
        Logger.d{"4444 getTariffsByLocation body=" + body}
        viewModelScope.launch(Dispatchers.IO) {
            val response = homeRepository.getTariffsByLocation(body = body)
            _locationTariffsByLocation.value = response.data

            Logger.d{"4444 getTariffsByLocation ОТВЕТ =" + response.data}
        }
    }

    fun setSelectedLocation(location: String) {
        Logger.d{"4444 setSelectedLocation=" + location}
        viewModelScope.launch(Dispatchers.IO) {
            locations.collect { listTitle ->
                listTitle
                    .find {
                        it.name.contains(location, ignoreCase = true)
                    }
                    .also {
                        _selectedLocation.value = it
                    }
            }
        }
    }

    fun setListTariffsPackageCheckable(startListCheckable: MutableList<PackageTariffCheckable>) {
        Logger.d{"4444 startListCheckable=" + startListCheckable}
    }

}