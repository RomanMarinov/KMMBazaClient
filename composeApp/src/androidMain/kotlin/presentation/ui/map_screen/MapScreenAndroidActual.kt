package presentation.ui.map_screen

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import net.baza.bazanetclientapp.R
import data.public_info.remote.dto.Data
import data.public_info.remote.dto.Location
import data.public_info.remote.dto.MarkerCityCam
import data.public_info.remote.dto.MarkerDomofon
import data.public_info.remote.dto.MarkerOffice
import data.public_info.remote.dto.MarkerOutdoor
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_favorite
import kmm.composeapp.generated.resources.ic_map_zoom_minus
import kmm.composeapp.generated.resources.ic_map_zoom_plus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import org.osmdroid.api.IMapController
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import presentation.ui.map_screen.model.MarkerDetail
import util.ColorCustomResources
import util.Strings
import java.lang.Math.abs


lateinit var markerCityTriangle: MarkerCityTriangle
private var listMarkersCityTriangle: MutableList<Marker> = mutableListOf()
private var listMarkersCityCam: MutableList<Marker> = mutableListOf()
private var listCityCam: MutableList<MarkerCityCam> = mutableListOf()


private var listMarkersDomofon: MutableList<Marker> = mutableListOf()
private var listDomofonCam: MutableList<MarkerDomofon> = mutableListOf()

private var listMarkersOutDoor: MutableList<Marker> = mutableListOf()
private var listOutDoorCam: MutableList<MarkerOutdoor> =
    mutableListOf()

private var listMarkersOffice: MutableList<Marker> = mutableListOf()
private var listOffice: MutableList<MarkerOffice> =
    mutableListOf()


private val _mapViewZoom = MutableStateFlow(0.0)
val mapViewZoom: StateFlow<Double> = _mapViewZoom

private val _mapViewCenter = MutableStateFlow("")
val mapViewCenter: StateFlow<String> = _mapViewCenter

var zoom = 0.0
var loc = ""

var mapViewGlobal: MapView? = null

var leftTopLatitude = ""
var leftTopLongitude = ""
var rightBottomLatitude = ""
var rightBottomLongitude = ""
var centerLatitude = ""
var centerLongitude = ""

@Composable
fun MapScreenAndroidActual(
    viewModel: MapScreenViewModel = koinInject(),
    paddingValue: PaddingValues,
    moveToBottomSheetMapFragment: (MarkerDetail) -> Unit,
) {
   // SetLocalLifecycleOwner()
    val context = LocalContext.current
    //val scope = rememberCoroutineScope()

    val publicInfo by viewModel.publicInfo.collectAsState()
    val setLocation by viewModel.setLocation.collectAsState()
//    val setLocationState = remember { mutableStateOf(setLocation) }
//    val geoPointZoom by viewModel.geoPointZoom.collectAsStateWithLifecycle()
//    val zoomNew by viewModel.zoomNew.collectAsState(null)

 //   val mapCityCams by viewModel.mapCityCams.collectAsStateWithLifecycle()
    val locations by viewModel.locationsTitle.collectAsStateWithLifecycle()
//    val mapCategories by viewModel.mapCategories.collectAsStateWithLifecycle()


    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    MapView(
                        context
                    ).apply {
                        maxZoomLevel = 20.0
                        minZoomLevel = 9.09
                    }
                },
                update = { mapView ->
                    mapViewGlobal = mapView

                    settingMapView(mapView = mapView, publicInfo = publicInfo)

                    mapView.addMapListener(mapViewScrollListener)
                }
            )

            TopControl(
                locations = locations,
                viewModel = viewModel,
                setLocation = setLocation,
                paddingValue = paddingValue
            )

            ZoomControl()

            BottomControl(
                paddingValue = paddingValue,
                viewModel = viewModel,
                context = context,
                moveToBottomSheetMapFragment = { markerDetail ->
                    moveToBottomSheetMapFragment(markerDetail)
                }
            )
        }
    }


}

val mapViewScrollListener = object : MapListener {
    override fun onScroll(event: ScrollEvent?): Boolean {
        val boundingBox = mapViewGlobal?.boundingBox
// ДЛЯ ПАУЗЫ ЗАЧЕМ ТО
        leftTopLatitude = boundingBox?.latNorth.toString()
        leftTopLongitude = boundingBox?.lonWest.toString()
        rightBottomLatitude = boundingBox?.latSouth.toString()
        rightBottomLongitude = boundingBox?.lonEast.toString()
        centerLatitude = boundingBox?.centerLatitude.toString()
        centerLongitude = boundingBox?.centerLongitude.toString()
        return false
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        event?.let {
            val currentZoom = it.zoomLevel // от 9 до 21 где 9-высоко
            Log.d("4444", " current =" + currentZoom)
            setIconMarkerTriangle(currentZoom)
        }
        return false
    }
}


fun settingMapView(
    mapView: MapView,
    publicInfo: Data?,
) {

    // Обновление MapView
    // "https://osm.baza.net/hot/"
    val urlMap = publicInfo?.mapServers?.get(0)
    val tileSource = XYTileSource(
        "Custom Tiles",
        0,
        22,
        256,
        ".png",
        arrayOf(urlMap),
        "© OpenStreetMap contributors"
    )
//
    val geoPoint by mutableStateOf(GeoPoint(59.222340, 39.882431))

    val northLatitude = 60.5
    val eastLongitude = 43.0
    val southLatitude = 58.74
    val westLongitude = 37.0

    val mapLimits = BoundingBox(
        northLatitude,
        eastLongitude,
        southLatitude,
        westLongitude
    )

    mapView.setTileSource(tileSource)
    mapView.setMultiTouchControls(true)
    mapView.setScrollableAreaLimitDouble(mapLimits)

//                mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER) // не использовать +-
    mapView.isTilesScaledToDpi = false

//                mapView.setTileSource(tileSource)
    mapView.controller.setCenter(geoPoint)
    // mapView.controller.setZoom(zoom)
    mapView.controller.setZoom(13.0)
}

private fun getLocationScreen(mapView: MapView): String {
    val valueLat = mapView.mapCenter.latitude
    val valueLong = mapView.mapCenter.longitude
    return "$valueLat, $valueLong"
}

private fun <T> removeMarkersIsNotEmpty(
    markerCams: List<T>,
    listMarkersCam: MutableList<Marker>,
    // mapView: MapView,
) {
    if (markerCams.isNotEmpty()) {
        for (marker in listMarkersCam) {
            mapViewGlobal?.overlays?.remove(marker)
        }
        mapViewGlobal?.invalidate() // разобраться с методом
        listMarkersCam.clear()
    }
}

private fun <T> removeMarkersIsEmpty(
    markerCams: List<T>,
    listMarkersCam: MutableList<Marker>,
    // mapView: MapView,
) {
    if (markerCams.isEmpty()) {
        for (marker in listMarkersCam) {
            mapViewGlobal?.overlays?.remove(marker)
        }
        mapViewGlobal?.invalidate() // разобраться с методом
        listMarkersCam.clear()
    }
}

private fun closeInfoMarkerWindow(
    markerCam: Marker?,
//    mapView: MapView,
) {    // закрыть окно описания маркета
    mapViewGlobal?.setOnTouchListener(object : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> markerCam?.closeInfoWindow()
            }
            return v?.onTouchEvent(event) ?: true
        }
    })
}

private fun setIconMarkerTriangle(currentZoom: Double) {
    when (currentZoom) {
        in 12.0..13.9 -> {
            setVisibleMarkerTriangle(false)
        }

        in 14.0..15.9 -> {
            setVisibleMarkerTriangle(true)
        }

        in 16.0..21.9 -> {
            setVisibleMarkerTriangle(true)
        }

        else -> {
            setVisibleMarkerTriangle(false)
        }
    }
}

private fun setVisibleMarkerTriangle(bool: Boolean) {
    mapViewGlobal?.let {
        val overlays: List<Overlay> = it.overlays
        for (overlay in overlays) {
            if (overlay is MarkerCityTriangle) {
                val marker = overlay as MarkerCityTriangle
                marker.isEnabled = bool
                it.invalidate()
            }
        }
    }
}


fun createMarkerTriangle(iconTriangle: Drawable?) {
    // Log.d("4444", " testCreateTriangle сработал")
    if (listMarkersCityTriangle.size != 0 && listCityCam.size != 0) {
        for (index in 0 until listMarkersCityTriangle.size) {
            //  Log.d("4444", " testCreateTriangle сработал зашел в список")
            val geoPointTriangle =
                GeoPoint(listCityCam[index].latitude, listCityCam[index].longitude)
            listMarkersCityTriangle[index].position = geoPointTriangle

            val angle = listCityCam[index].angle.toFloat()

            listMarkersCityTriangle[index].rotation = angle * -1
            listMarkersCityTriangle[index].setAnchor(
                Marker.ANCHOR_CENTER,
                Marker.ANCHOR_CENTER
            )
            listMarkersCityTriangle[index].setInfoWindow(null)

            // listMarkersCityTriangle[index].icon = iconTriangle
            listMarkersCityTriangle[index].icon = iconTriangle

        }
    }
}

private fun setZoomLocation(zoom: Double, center: String) {
    val latitude = center.substring(0, center.indexOf(",")).toDouble()
    val longitude = center.substring(center.indexOf(" ")).trim().toDouble()
    mapViewGlobal?.let {
        val mapController: IMapController = it.controller
        val startPoint = GeoPoint(latitude, longitude)
        mapController.setZoom(zoom)
        mapController.setCenter(startPoint)
    }
}

private fun setMarkerParams(
    marker: Marker?,
    geoPoint: GeoPoint,
    iconId: Drawable?,
    title: String,
) {
    marker?.let {
        it.position = geoPoint
        it.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        it.icon = iconId
        //it.image = iconId4
        it.title = title
//                marker.snippet = "The Pentagon."
//                marker.subDescription = "The Pentagon is ..."
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TopControl(
    locations: List<String>,
    setLocation: Location?,
    viewModel: MapScreenViewModel,
    paddingValue: PaddingValues,
) {
    var expanded by remember { mutableStateOf(false) }
    var labelClick by remember { mutableStateOf("г. Вологда") }
    var indexClick by remember { mutableStateOf(-1) }
    var dropdownMenuWidth by remember { mutableStateOf(0) }
    var dropdownMenuHeight by remember { mutableStateOf(0) }
    val localDensity = LocalDensity.current

    LaunchedEffect(indexClick) {
        viewModel.setMapLocation(position = indexClick)
    }

    LaunchedEffect(setLocation) {
        setLocation?.let {
            if (it.leftTopLat.isNotEmpty() && it.leftTopLon.isNotEmpty() && it.rightBottomLat.isNotEmpty() && it.rightBottomLon.isNotEmpty()) {
                val leftTopLat: Double = it.leftTopLat.toDouble()
                val leftTopLon: Double = it.leftTopLon.toDouble()
                val rightBottomLat: Double = it.rightBottomLat.toDouble()
                val rightBottomLon: Double = it.rightBottomLon.toDouble()

                val squareABS =
                    (abs(leftTopLat - rightBottomLat) * abs(leftTopLon - rightBottomLon)) * 1000
                if (squareABS >= 8) {
                    setZoomLocation(13.0, it.center)
                } else {
                    setZoomLocation(16.0, it.center)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            OutlinedButton(
                shape = RoundedCornerShape(10),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 4.dp, end = 2.dp)
                    .weight(1f)
                    .height(40.dp),
                //.clip(RoundedCornerShape(15)),
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = ColorCustomResources.colorTransparentItem
                ),

                ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_favorite),
                        contentDescription = null,
                        //  modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Избранное",
                        //  modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "2",
                        //    modifier = Modifier.weight(1f)
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().weight(1f)
//                    .onGloballyPositioned { coordinates ->
//                        dropdownMenuWidth = with(localDensity) {
//                            (coordinates.size.width / density).toInt()
//                        }
//                    }
                //  horizontalAlignment = Alignment.End
            ) {

                OutlinedButton(
                    shape = RoundedCornerShape(10),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 2.dp, end = 4.dp)
                        .onGloballyPositioned { coordinates ->
                            dropdownMenuWidth = with(localDensity) {
                                (coordinates.size.width / density).toInt()
                            }
                            dropdownMenuHeight = with(localDensity) {
                                (coordinates.size.height).toInt()
                            }

                        },
                    //   .weight(1f),
                    //.clip(RoundedCornerShape(15)),
                    onClick = {
                        expanded = !expanded
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = ColorCustomResources.colorBazaMainBlue
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = labelClick,
                            modifier = Modifier
                                //.padding(start = 16.dp)
                                .weight(1f) // Занимает все доступное пространство влево
                        )

                        Icon(
                            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            //  modifier = Modifier.padding(end = 16.dp) // Отступ справа
                        )
                    }
                }

                DropdownMenu(
                    modifier = Modifier
                        //.padding(0.dp)
                        .height(400.dp)
                        .background(color = Color.White)
                        .shadow(0.dp, shape = RoundedCornerShape(10.dp)),
                    //  .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 4.dp),
                    //  .offset(x = 2.dp),
//                    modifier = Modifier.background(color = Color.Transparent),
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    offset = DpOffset(2.dp, 0.dp),
                    //scrollState = ,
                    content = {
                        locations.forEachIndexed { index, label ->
                            DropdownMenuItem(
                                modifier = Modifier
                                    .background(
                                        if (indexClick == index) ColorCustomResources.colorBazaMainBlue
                                        else Color.Transparent
                                    )
                                    .width(dropdownMenuWidth.dp)
                                    .height(40.dp),
                                text = {
                                    Text(
                                        text = label,
                                        color = if (indexClick == index) Color.White else Color.Black
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    labelClick = label
                                    indexClick = index
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ZoomControl() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        Icon(
            modifier = Modifier
                .padding(bottom = 8.dp)
                //.systemBarsPadding() // Добавить отступ от скрытого статус-бара
                .size(40.dp)
                .clip(RoundedCornerShape(50))
                .clickable {
                    mapViewGlobal?.let { mapView ->
                        val zoomLevel = mapView.zoomLevelDouble
                        val getLocal = getLocationScreen(mapView = mapView)

                        setZoomLocation(zoomLevel.plus(1.0), getLocal)
                    }
                },
            // .clip(RoundedCornerShape(50)),
            imageVector = vectorResource(Res.drawable.ic_map_zoom_plus),
            contentDescription = "plus",

            )
        Icon(
            modifier = Modifier
                //.padding(),
                //.systemBarsPadding() // Добавить отступ от скрытого статус-бара
                .size(40.dp)
                .clip(RoundedCornerShape(50))
                .clickable {
                    mapViewGlobal?.let { mapView ->
                        val zoomLevel = mapView.zoomLevelDouble
                        val getLocal = getLocationScreen(mapView = mapView)

                        setZoomLocation(zoomLevel.plus(-1.0), getLocal)
                    }
//                    zoomLevel?.let {
//                        viewModel.setZoomAfterClick(it.plus(-1.0), zoomLocation)
//                    }
//                    viewModel.clickCountZoom(getCurrentZoom(-1.0), getLocationScreen())
                    // viewModel.clickCountZoom(-1.0)
                },
            // .clip(RoundedCornerShape(50)),
            imageVector = vectorResource(Res.drawable.ic_map_zoom_minus),
            contentDescription = "minus",

            )
    }
}

@Composable
fun BottomControl(
    paddingValue: PaddingValues,
    viewModel: MapScreenViewModel,
    context: Context,
    moveToBottomSheetMapFragment: (MarkerDetail) -> Unit,
) {
    val mapCategories = viewModel.mapCategories.collectAsStateWithLifecycle()

    val mapCityCams by viewModel.mapCityCams.collectAsStateWithLifecycle()
    val mapOutDoorCams by viewModel.mapOutDoorCams.collectAsStateWithLifecycle()
    val mapDomofonCams by viewModel.mapDomofonCams.collectAsStateWithLifecycle()
    val mapOffice by viewModel.mapOffice.collectAsStateWithLifecycle()

    val lazyListState = rememberLazyListState()
    var indexClickCityCam by remember { mutableStateOf(true) }
    var indexClickOutdoor by remember { mutableStateOf(false) }
    var indexClickDomofon by remember { mutableStateOf(false) }
    var indexClickOffice by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 84.dp)
            .padding(paddingValue),
        //  .background(Color.Red),
        verticalArrangement = Arrangement.Bottom,
    ) {
        LazyRow(
            state = lazyListState,
            // contentPadding = paddingValue
        ) {
            itemsIndexed(mapCategories.value) { index, item ->
                Spacer(modifier = Modifier.width(4.dp))
                Row(
                    modifier = Modifier
                        .clickable {
                            // трабл с этого метода
                            viewModel.onClickCategory(position = index)
                            if (index == 0) {
                                indexClickCityCam = !indexClickCityCam
                            }
                            if (index == 1) {
                                indexClickOutdoor = !indexClickOutdoor
                            }
                            if (index == 2) {
                                indexClickDomofon = !indexClickDomofon
                            }
                            if (index == 3) {
                                indexClickOffice = !indexClickOffice
                            }
                        }
                        .clip(RoundedCornerShape(10))
                        .height(40.dp)
                        .background(
                            method(
                                index = index,
                                indexClickCityCam = indexClickCityCam,
                                indexClickOutdoor = indexClickOutdoor,
                                indexClickDomofon = indexClickDomofon,
                                indexClickOffice = indexClickOffice
                            )
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        // modifier = Modifier.fillMaxWidth(),
                        text = item.title,
                        color = Color.White
                    )
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = item.count.toString(),
                        color = Color.Green
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }


    outdoorMarkers(
        mapOutDoorCams = mapOutDoorCams,
        context = context,
        moveToBottomSheetMapFragment = { markerDetail ->
            moveToBottomSheetMapFragment(markerDetail)
        }
    )
    domofonMarkers(
        mapDomofonCams = mapDomofonCams,
        context = context,
        moveToBottomSheetMapFragment = { markerDetail ->
            moveToBottomSheetMapFragment(markerDetail)
        }
    )
    cityMarkers(
        mapCityCams = mapCityCams,
        context = context,
        moveToBottomSheetMapFragment = { markerDetail ->
            moveToBottomSheetMapFragment(markerDetail)
        }
    )
    officeMarkers(
        mapOffice = mapOffice,
        context = context,
        moveToBottomSheetMapFragment = { markerDetail ->
            moveToBottomSheetMapFragment(markerDetail)
        })
}

fun cityMarkers(
    mapCityCams: List<MarkerCityCam>?,
    context: Context,
    moveToBottomSheetMapFragment: (MarkerDetail) -> Unit,
) {
    mapCityCams?.let {

        removeMarkersIsNotEmpty(mapCityCams, listMarkersCityCam)
        removeMarkersIsNotEmpty(mapCityCams, listMarkersCityTriangle)

        var markerCityCam: Marker? = null
        for (cityCam in mapCityCams ?: emptyList()) {
            co.touchlab.kermit.Logger.d { " 4444 SetMapView android cityCam 1" }

            markerCityTriangle =
                MarkerCityTriangle(mapViewGlobal) // маркер угла должен быть первый добавлен
            mapViewGlobal?.overlays?.add(markerCityTriangle)
            mapViewGlobal?.invalidate() // перерисовка из главного потока // postInvalidate из фонового
            listMarkersCityTriangle.add(markerCityTriangle)
        }

        for (cityCam in mapCityCams ?: emptyList()) {
            co.touchlab.kermit.Logger.d { " 4444 SetMapView android cityCam 2" }

            markerCityCam = MarkerCityCamera(mapViewGlobal) // маркер камеры

            val geoPointCityCam = GeoPoint(cityCam.latitude, cityCam.longitude)
            val title = cityCam.title

            val iconCityCam = ContextCompat.getDrawable(
                context,
                R.drawable.ic_map_city_cam
            )

            setMarkerParams(
                marker = markerCityCam,
                geoPoint = geoPointCityCam,
                iconId = iconCityCam,
                title = title
            )

            //////// //markerCityTriangle = Marker(binding.mapView) // маркер угла должен быть первый добавлен

            // binding.mapView.overlays.add(markerCityTriangle)
            // маркер камер должен быть вторым установлен (т.е. поверх первого)
            mapViewGlobal?.overlays?.add(markerCityCam)
            mapViewGlobal?.invalidate() // перерисовка из главного потока // postInvalidate из фонового

            showInfoMarkerWindow(
                markerCityCam, cityCam,
                moveToBottomSheetMapFragment = { markerDetail ->
                    moveToBottomSheetMapFragment(markerDetail)
                }
            )

            listMarkersCityCam.add(markerCityCam) // список для только для удаления маркеров markerCams
            //  listMarkersCityTriangle.add(markerCityTriangle)
            listCityCam.add(cityCam)
        }

        val iconTriangle = ContextCompat.getDrawable(
            context,
            R.drawable.ic_map_triangle
        )
        createMarkerTriangle(iconTriangle = iconTriangle)

//        val overlays: List<Overlay> = mapViewGlobal.overlays
        // showFavoriteCityCam(overlays = overlays)

        closeInfoMarkerWindow(markerCityCam)

        mapViewGlobal?.let {
            setIconMarkerTriangle(it.zoomLevelDouble)
        }

        removeMarkersIsEmpty(mapCityCams, listMarkersCityCam)
        removeMarkersIsEmpty(mapCityCams, listMarkersCityTriangle)

//        removeMarkers(mapCityCams, listMarkersCityCam)
//        removeMarkers(mapCityCams, listMarkersCityTriangle)
    }
}

fun outdoorMarkers(
    mapOutDoorCams: List<MarkerOutdoor>?,
    context: Context,
    moveToBottomSheetMapFragment: (MarkerDetail) -> Unit,
) {
    mapOutDoorCams?.let {

        removeMarkersIsNotEmpty(it, listMarkersOutDoor)

        var markerOutDoorCam: Marker? = null
        for (outDoorCam in it) {

            markerOutDoorCam = Marker(mapViewGlobal) // маркер камеры

            val geoPoint = GeoPoint(outDoorCam.latitude, outDoorCam.longitude)
            val iconId = ContextCompat.getDrawable(context, R.drawable.ic_map_outdoor)
            val title = outDoorCam.title

            setMarkerParams(
                marker = markerOutDoorCam,
                geoPoint = geoPoint,
                iconId = iconId,
                title = title
            )

            mapViewGlobal?.overlays?.add(markerOutDoorCam)
            mapViewGlobal?.invalidate() // перерисовка из главного потока // postInvalidate из фонового

            showInfoMarkerWindow(
                markerOutDoorCam,
                outDoorCam,
                moveToBottomSheetMapFragment = { markerDetail ->
                    moveToBottomSheetMapFragment(markerDetail)
                }
            )

            listMarkersOutDoor.add(markerOutDoorCam) // список для только для удаления маркеров markerCams
            listOutDoorCam.add(outDoorCam)
        }

        closeInfoMarkerWindow(markerOutDoorCam)
        //setIconMarkerMap(binding.mapView.zoomLevel.toDouble())


        removeMarkersIsEmpty(it, listMarkersOutDoor)
//        removeMarkers(it, listMarkersOutDoor)
    }
}

fun domofonMarkers(
    mapDomofonCams: List<MarkerDomofon>?,
    context: Context,
    moveToBottomSheetMapFragment: (MarkerDetail) -> Unit,
) {
    mapDomofonCams?.let {

        removeMarkersIsNotEmpty(it, listMarkersDomofon)

        var markerDomofonCam: Marker? = null
        for (domofonCam in it) {
            markerDomofonCam = Marker(mapViewGlobal) // маркер камеры

            val geoPoint = GeoPoint(domofonCam.latitude, domofonCam.longitude)
            val iconId = ContextCompat.getDrawable(context, R.drawable.ic_map_domofon)
            val title = domofonCam.title

            setMarkerParams(
                marker = markerDomofonCam,
                geoPoint = geoPoint,
                iconId = iconId,
                title = title
            )

            mapViewGlobal?.overlays?.add(markerDomofonCam)
            mapViewGlobal?.invalidate() // перерисовка из главного потока // postInvalidate из фонового

            showInfoMarkerWindow(
                markerDomofonCam,
                domofonCam,
                moveToBottomSheetMapFragment = { markerDetail ->
                    moveToBottomSheetMapFragment(markerDetail)
                }
            )

            listMarkersDomofon.add(markerDomofonCam) // список для только для удаления маркеров markerCams
            listDomofonCam.add(domofonCam)
        }

        closeInfoMarkerWindow(markerDomofonCam)
        //  setIconMarkerMap(binding.mapView.zoomLevel.toDouble())

        removeMarkersIsEmpty(it, listMarkersDomofon)
//        removeMarkers(it, listMarkersDomofon)
    }
}

fun officeMarkers(
    mapOffice: List<MarkerOffice>?,
    context: Context,
    moveToBottomSheetMapFragment: (MarkerDetail) -> Unit,
) {
    mapOffice?.let {

        removeMarkersIsNotEmpty(it, listMarkersOffice)

        var markerOffice: Marker? = null
        for (office in it) {

            markerOffice = Marker(mapViewGlobal) // маркер камеры

            val geoPoint = GeoPoint(office.latitude, office.longitude)
            val iconId = ContextCompat.getDrawable(context, R.drawable.ic_map_office)
            val title = office.title

            setMarkerParams(
                marker = markerOffice,
                geoPoint = geoPoint,
                iconId = iconId,
                title = title
            )

            mapViewGlobal?.overlays?.add(markerOffice)
            mapViewGlobal?.invalidate() // перерисовка из главного потока // postInvalidate из фонового

            showInfoMarkerWindow(
                markerOffice,
                office,
                moveToBottomSheetMapFragment = { markerDetail ->
                    moveToBottomSheetMapFragment(markerDetail)
                }
            )

            listMarkersOffice.add(markerOffice) // список для только для удаления маркеров markerCams
            listOffice.add(office)
        }

        closeInfoMarkerWindow(markerOffice)
        //  setIconMarkerMap(binding.mapView.zoomLevel.toDouble())

        removeMarkersIsEmpty(it, listMarkersOffice)
//        removeMarkers(it, listMarkersOffice)
    }
}

private fun <T> showInfoMarkerWindow(
    markerCam: Marker,
    objectCam: T,
    moveToBottomSheetMapFragment: (MarkerDetail) -> Unit,
) {
    markerCam.setOnMarkerClickListener { marker, mapView ->
        Log.d("4444", " showInfoMarkerWindow")
        if (objectCam is MarkerCityCam) {
            moveToBottomSheetMapFragment(
                MarkerDetail(
                    cameraName = objectCam.additionalMap.cameraName,
                    server = "https://".plus(objectCam.additionalMap.server.plus("/")),
                    token = objectCam.additionalMap.token,
                    titleType = Strings.typeCity,
                    titleAddress = objectCam.title,
                    previewUrl = objectCam.additionalMap.previewUrl,
                    videoUrl = objectCam.additionalMap.videoUrl,
                    worktime = "",
                    visible = "",
                    dtpCounts = objectCam.additionalMap.dtpCounts,
                    albumId = objectCam.additionalMap.albumId,
                    isFavorite = false,
                    longitude = objectCam.longitude.toString(),
                    latitude = objectCam.latitude.toString()
                )
            )
        }

        if (objectCam is MarkerOutdoor) {
            Log.d("4444", " showInfoMarkerWindow MarkerOutdoor")
            moveToBottomSheetMapFragment(
                MarkerDetail(
                    cameraName = "",
                    token = "",
                    server = "",
                    titleType = Strings.typeOutDoor,
                    titleAddress = objectCam.title,
                    previewUrl = objectCam.additional.previewUrl,
                    videoUrl = "",
                    worktime = "",
                    visible = "",
                    dtpCounts = 0,
                    albumId = 0,
                    isFavorite = false
                )
            )
        }
        if (objectCam is MarkerOffice) {
            moveToBottomSheetMapFragment(
                MarkerDetail(
                    cameraName = "",
                    token = "",
                    server = "",
                    titleType = Strings.typeOffice,
                    titleAddress = objectCam.additional.address,
                    previewUrl = "",
                    videoUrl = "",
                    worktime = objectCam.additional.worktime,
                    visible = objectCam.additional.phone.visible,
                    dtpCounts = 0,
                    albumId = 0,
                    isFavorite = false
                )
            )
        }
        if (objectCam is MarkerDomofon) {
            moveToBottomSheetMapFragment(
                MarkerDetail(
                    cameraName = "",
                    token = "",
                    server = "",
                    titleType = Strings.typeDomofon,
                    titleAddress = objectCam.title,
                    previewUrl = "",
                    videoUrl = "",
                    worktime = "",
                    visible = "",
                    dtpCounts = 0,
                    albumId = 0,
                    isFavorite = false
                )
            )
        }
        //   marker.showInfoWindow()
        true
    }
}


fun method(
    index: Int,
    indexClickCityCam: Boolean,
    indexClickOutdoor: Boolean,
    indexClickDomofon: Boolean,
    indexClickOffice: Boolean,
): Color {
    val color = when (index) {
        0 -> if (indexClickCityCam) ColorCustomResources.colorBazaMainBlue else ColorCustomResources.colorTransparentItem
        1 -> if (indexClickOutdoor) ColorCustomResources.colorMapOutdoor else ColorCustomResources.colorTransparentItem
        2 -> if (indexClickDomofon) ColorCustomResources.colorMapDomofon else ColorCustomResources.colorTransparentItem
        3 -> if (indexClickOffice) ColorCustomResources.colorMapOffice else ColorCustomResources.colorTransparentItem
        else -> ColorCustomResources.colorTransparentItem

    }
    return color
}