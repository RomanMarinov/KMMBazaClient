package presentation.ui.map_screen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import data.public_info.remote.dto.Data
import data.public_info.remote.dto.Location
import data.public_info.remote.dto.MarkerCityCam
import data.public_info.remote.dto.MarkerDomofon
import data.public_info.remote.dto.MarkerOffice
import data.public_info.remote.dto.MarkerOutdoor
import domain.repository.CommonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import presentation.ui.map_screen.model.GeoPointScreen
import presentation.ui.map_screen.model.MapCategory

class MapScreenViewModel(
    private val commonRepository: CommonRepository,
) : ViewModel() {

    private var _locationsTitle: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val locationsTitle: StateFlow<List<String>> = _locationsTitle

    private var _publicInfo: MutableStateFlow<Data?> = MutableStateFlow(null)
    val publicInfo: StateFlow<Data?> = _publicInfo

    private var _mapCityCams: MutableStateFlow<List<MarkerCityCam>?> =
        MutableStateFlow(emptyList())
    val mapCityCams: StateFlow<List<MarkerCityCam>?> = _mapCityCams
    private var _mapDomofonCams: MutableStateFlow<List<MarkerDomofon>> =
        MutableStateFlow(emptyList())
    val mapDomofonCams: StateFlow<List<MarkerDomofon>> = _mapDomofonCams
    private var _mapOutDoorCams: MutableStateFlow<List<MarkerOutdoor>> =
        MutableStateFlow(emptyList())
    val mapOutDoorCams: StateFlow<List<MarkerOutdoor>> = _mapOutDoorCams
    private var _mapOffice: MutableStateFlow<List<MarkerOffice>> =
        MutableStateFlow(emptyList())
    val mapOffice: StateFlow<List<MarkerOffice>> = _mapOffice


    private var _mapCategories: MutableStateFlow<List<MapCategory>> = MutableStateFlow(emptyList())
    val mapCategories: StateFlow<List<MapCategory>> = _mapCategories

    private var _setLocation: MutableStateFlow<Location?> = MutableStateFlow(null)
    val setLocation: StateFlow<Location?> = _setLocation

    private var _spinnerCityPosition: MutableStateFlow<Int> = MutableStateFlow(0)
    val spinnerCityPosition: StateFlow<Int> = _spinnerCityPosition

    private val _zoomNew: MutableSharedFlow<Double?> = MutableSharedFlow(replay = 1)
    val zoomNew: SharedFlow<Double?> = _zoomNew.asSharedFlow()

    private var _geoPointZoom: MutableStateFlow<GeoPointScreen?> = MutableStateFlow(null)
    val geoPointZoom: StateFlow<GeoPointScreen?> = _geoPointZoom

    private var _isEmptyCityCams = true
    private var _isEmptyDomofonCams = true
    private var _isEmptyOutDoorCams = true
    private var _isEmptyOffice = true

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            val publicInfo = commonRepository.getPublicInfo()
            _publicInfo.value = publicInfo

            getMapCategories(publicInfo = publicInfo)
            setStartSettingForMapCategories()
            getLocationTitle()
        }
    }

    private fun getMapCategories(publicInfo: Data) {
        val mapCategoriesTemp: MutableList<MapCategory> = mutableListOf()
        publicInfo.mapMarkers?.let { mapMarkers ->
            mapCategoriesTemp.add(
                0, MapCategory(
                    title = mapMarkers.cityCams.title,
                    count = mapMarkers.cityCams.count
                )
            )
            mapCategoriesTemp.add(
                1, MapCategory(
                    title = mapMarkers.outdoorCams.title,
                    count = mapMarkers.outdoorCams.count
                )
            )
            mapCategoriesTemp.add(
                2, MapCategory(
                    title = mapMarkers.domofonCams.title,
                    count = mapMarkers.domofonCams.count
                )
            )
            mapCategoriesTemp.add(
                3, MapCategory(
                    title = mapMarkers.officeCams.title,
                    count = mapMarkers.officeCams.count,
                )
            )
            _mapCategories.value = mapCategoriesTemp
        }
    }

    fun setMapLocation(position: Int) {
        Logger.d {"4444  MapViewModel onClick position=" + position }
        _publicInfo.value?.locations?.forEach { item ->
            Logger.d {"4444  MapViewModel onClick item=" + item }

            val titleToMatch = _locationsTitle.value.getOrNull(position) // Получаем title по индексу position
            if (titleToMatch != null) {
                if (titleToMatch.contains(item.title)) {
                    _setLocation.value = Location(
                        camerasAvailable = item.camerasAvailable,
                        center = item.center,
                        leftTopLat = item.leftTopLat,
                        leftTopLon = item.leftTopLon,
                        location = item.location,
                        rightBottomLat = item.rightBottomLat,
                        rightBottomLon = item.rightBottomLon,
                        title = titleToMatch,
                        titlePrefix = item.titlePrefix
                    )
                }
            }
        }
    }

    private fun getLocationTitle() {
        val list: MutableList<String> = mutableListOf()
        val data = _publicInfo.value?.locations
        data?.let {
            it.map { location ->
                list.add(location.titlePrefix + " " + location.title)
            }
        }

        list.sortedBy { !it.startsWith("рп") }
            .sortedBy { !it.startsWith("г") }
            .also { listSort ->
                _locationsTitle.value = listSort
            }

        _locationsTitle.value.forEachIndexed { index, item ->
            if (item.contains("Вологда")) {
                _spinnerCityPosition.value = index
            }
            return@forEachIndexed
        }
    }

    private fun setStartSettingForMapCategories(){
        onClickCategory(0)
        onClickCategory(3)
    }

    fun onClickCategory(position: Int) {
        try {
            val categories = _mapCategories.value

            if (position >= 0 && position < categories.size) {
                val category = categories[position].title

                //Logger.d{" 4444 onClickCategory category=" + category}

                when (category) {
                    "Городские камеры" -> {
                        if (!_isEmptyCityCams) {
                            _mapCityCams.value = emptyList()
                            _isEmptyCityCams = true
                        } else {
                            _isEmptyCityCams = false
                            fillingMapCams()
                        }
                    }

                    "Дворовые камеры" -> {
                        if (!_isEmptyOutDoorCams) {
                            _mapOutDoorCams.value = emptyList()
                            _isEmptyOutDoorCams = true
                        } else {
                            _isEmptyOutDoorCams = false
                            fillingMapCams()
                        }
                    }

                    "Домофоны" -> {
                        if (!_isEmptyDomofonCams) {
                            _mapDomofonCams.value = emptyList()
                            _isEmptyDomofonCams = true
                        } else {
                            _isEmptyDomofonCams = false
                            fillingMapCams()
                        }
                    }

                    "Офисы" -> {
                        if (!_isEmptyOffice) {
                            _mapOffice.value = emptyList()
                            _isEmptyOffice = true
                        } else {
                            _isEmptyOffice = false
                            fillingMapCams()
                        }
                    }
                }
            } else {
                Logger.d{"4444 Invalid position: $position"}
            }
        } catch (e: Exception) {
            Logger.d{"4444  try catch e=" + e}
        }
    }


    //////////////////////////////////////

    private fun fillingMapCams() {
        if (!_isEmptyOutDoorCams) { // очередь первая - 1й слой наложения на карте
            _mapOutDoorCams.value = emptyList()
            _publicInfo.value?.let {
                _mapOutDoorCams.value = it.mapMarkers?.outdoorCams?.markersOutdoors ?: emptyList()
            }
        }
        if (!_isEmptyDomofonCams) { // очередь вторая - 2й слой наложения на карте
            _mapDomofonCams.value = emptyList()
            _publicInfo.value?.let {
                _mapDomofonCams.value = it.mapMarkers?.domofonCams?.markersDomofon ?: emptyList()
            }
        }
        if (!_isEmptyCityCams) { // очередь третья - 3й слой наложения на карте
            //Logger.d{" 4444 fillingMapCams _mapCityCams"}
            _mapCityCams.value = emptyList()
            _publicInfo.value?.let {
                _mapCityCams.value = it.mapMarkers?.cityCams?.markerCityCams ?: emptyList()
            }
        }
        if (!_isEmptyOffice) { // очередь четвертая - 4й слой наложения на карте
            _mapOffice.value = emptyList()
            _publicInfo.value?.let {
                _mapOffice.value = it.mapMarkers?.officeCams?.markersOffice ?: emptyList()
            }
        }
    }

    ////////////////////////

    // для хелпа
    suspend fun setIndexSpinner(selectedCity: String) {
        _locationsTitle.collect {
            it.forEachIndexed { index, item ->
                if (item.contains(selectedCity)) {
                    Logger.d {"4444 setIndexSpinner selectedCity=" + selectedCity + " index=" + index }
                    _spinnerCityPosition.value = index
                }
                return@forEachIndexed
            }
            return@collect
        }
    }
}