package presentation.ui.map_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import data.public_info.remote.dto.Data
import data.public_info.remote.dto.Location
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_favorite
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import observer.ObserverProtocol
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorized
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.MapKit.MKCoordinateRegionMake
import platform.MapKit.MKCoordinateSpanMake
import platform.MapKit.MKMapSizeMake
import platform.MapKit.MKMapView
import platform.MapKit.MKMapViewDelegateProtocol
import platform.MapKit.MKPointAnnotation
import platform.UIKit.UIGestureRecognizer
import platform.darwin.NSObject
import presentation.ui.map_screen.model.MarkerDetail
import util.ColorCustomResources
import kotlin.math.abs
import kotlin.math.log2
import kotlin.math.min

var mapViewGlobal: MKMapView? = null

private var clLocationManager: CLLocationManager? = null

fun calculateZoomLevel(centerCoordinateDistance: Double): Double {
    val equatorLength = 40075016.686 // Длина экватора в метрах
    val zoomMax = 19.0 // Максимальное значение zoom level

    // Формула для вычисления zoom level
    val zoomLevel = log2(equatorLength / (centerCoordinateDistance * 360.0))

    return if (zoomLevel < 0) 0.0 else min(zoomLevel, zoomMax)
}

//class MVD() : NSObject(), MKMapViewDelegateProtocol {
//
//    @OptIn(ExperimentalForeignApi::class, ExperimentalResourceApi::class)
//    override fun mapView(mapView: platform.MapKit.MKMapView, viewForAnnotation: platform.MapKit.MKAnnotationProtocol) {
//        val location = CLLocationCoordinate2DMake(51.0, 0.0)
//        val annotation =
//            MKPointAnnotation(
//                location,
//                title = "Marker",
//                subtitle = "marker"
//            )
//
//        val annotationView = MKAnnotationView(annotation = annotation, reuseIdentifier = "customAnnotation")
//
//        val image = UIImage("star.fill")
//        annotationView.image = image
//       // return annotationView
//    }
//}

@OptIn(ExperimentalForeignApi::class)
fun settingMapKit(mkMapView: MKMapView, publicInfo: Data?) {

    val center = CLLocationCoordinate2DMake(
        latitude = 59.222340,
        longitude = 39.882431
    )

    val span = MKCoordinateSpanMake(
        latitudeDelta = 0.5, // Примерное значение для широты
        longitudeDelta = 0.5// Примерное значение для долготы
    )

    val region = MKCoordinateRegionMake(
        centerCoordinate = center,
        span = span
    )
//
//
//    //  val cgRectMake = CGRectMake(10.0, 50.0, 40.0, 40.0) // фигня какая-то
//    // mkMapView.setBounds(cgRectMake)
//
    mkMapView.setRegion(region, animated = true)


//    // "https://osm.baza.net/hot/"
//    val urlMap = publicInfo?.mapServers?.get(0)
//    val tileSource = XYTileSource(
//        "Custom Tiles",
//        0,
//        22,
//        256,
//        ".png",
//        arrayOf(urlMap),
//        "© OpenStreetMap contributors"
//    )

    // хуйня не работает
//    val mkTileOverlay = MKTileOverlay("https://osm.baza.net/hot/")
//    // Установка атрибутов тайлов
//    mkTileOverlay.tileSize = CGSizeMake(256.0, 256.0)
//    mkTileOverlay.isGeometryFlipped()
//    mkTileOverlay.minimumZ = 10
//    mkTileOverlay.maximumZ = 19
//    mkTileOverlay.canReplaceMapContent()
//    mkMapView.addOverlay(mkTileOverlay)
/////////////////////////////////

    // val oahuCenter = CLLocation(21.4765,-157.9647)
    //val region = MKCoordinateRegion(oahuCenter, 50000, 60000)
    // mkMapView.setCameraBoundary(mkMapView.cameraBoundary(region, true))


//    //////////////////
//    val northLatitude = 60.5
//    val eastLongitude = 43.0
//    val southLatitude = 58.74
//    val westLongitude = 37.0
//
////    val clLocationCoordinate2D = CLLocationCoordinate2DMake(latitude = 59.222340, longitude = 39.882431)
////    val mkCoordinateSpan = MKCoordinateSpanMake(10.0, 10.0)
////    val mkCoordinateRegion = MKCoordinateRegionMake(clLocationCoordinate2D, mkCoordinateSpan)
////    val mkMapCameraBoundary = MKMapCameraBoundary(mkCoordinateRegion)
////    mkMapView.setCameraBoundary(mkMapCameraBoundary)
///////////////////////////
//    // Создание координат для углов границы карты
//    val northEastCoordinate = CLLocationCoordinate2DMake(northLatitude, eastLongitude)
//    val southWestCoordinate = CLLocationCoordinate2DMake(southLatitude, westLongitude)
//
//
//// Создание координаты центра карты
//    val centerLatitude = (northLatitude + southLatitude) / 2
//    val centerLongitude = (eastLongitude + westLongitude) / 2
//    val centerCoordinate = CLLocationCoordinate2DMake(centerLatitude, centerLongitude)
//
//// Вычисление ширины и высоты области карты
//    val spanLatitude = northLatitude - southLatitude
//    val spanLongitude = eastLongitude - westLongitude
//    val span1 = MKCoordinateSpanMake(spanLatitude, spanLongitude)
//
//// Создание области карты на основе координат центра и размера области
//    val region1 = MKCoordinateRegionMake(centerCoordinate, span1)
//
//// Создание ограничения карты для камеры
//    val cameraBoundary = MKMapCameraBoundary(region1)
//
//// Установка ограничений камеры на карте
//    mkMapView.setCameraBoundary(cameraBoundary)


    /////////////////////
//// Создание координат для углов границы карты
//    val northEastCoordinate = CLLocationCoordinate2DMake(northLatitude, eastLongitude)
//    val southWestCoordinate = CLLocationCoordinate2DMake(southLatitude, westLongitude)
//
//// Создание области карты на основе углов границы
//    val region = MKCoordinateRegionMake(northEastCoordinate, southWestCoordinate)
//
//// Создание ограничения карты для камеры
//    val cameraBoundary = MKMapCameraBoundary(region)
//
//// Установка ограничений камеры на карте
//    mkMapView.setCameraBoundary(cameraBoundary)
//
//


    /////////////////////////////
//    mkMapView.setCameraBoundary(MKMapCameraBoundary(MKCoordinateRegionMake(center, span)))

//    val zoomRange = mkMapView.cameraZoomRange(200000.0)
    //  mkMapView.setCameraZoomRange(MKMapCameraZoomRange(20000.0), true)


//////////////////////////////////
//    mkMapView.setCameraZoomRange(MKMapCameraZoomRange(3500.0), true)

    //mapRect: MKMapRect, zoomScale: MKZoomScale)


    // mkMapView.setCameraZoomRange(MKMapCameraZoomRange(20.0, 9.09))
    //mkMapView.setCenterCoordinate(center)

    //  MKMapPointMake(59.222340, 39.882431) // не понятно зачем это
    MKMapSizeMake(100.0, 200.0)
    //MKMapRect()


    mkMapView.setScrollEnabled(true)
    mkMapView.setRotateEnabled(true)
    mkMapView.setZoomEnabled(true)
    //  mkMapView.addOverlay()


    // Получение всех gesture recognizers у MKMapView
    val gestureRecognizers = mkMapView.gestureRecognizers?.toList()

    // Установка cancelsTouchesInView = NO для всех gesture recognizers
    gestureRecognizers?.forEach { recognizer ->
        if (recognizer is UIGestureRecognizer) {
            recognizer.cancelsTouchesInView = false
        }
    }


}

@OptIn(ExperimentalForeignApi::class, ExperimentalResourceApi::class)
@Composable
fun MapScreenIosActual(
    viewModel: MapScreenViewModel = koinInject(),
    paddingValue: PaddingValues,
    moveToBottomSheetMapFragment: (MarkerDetail) -> Unit,
) {
   // SetLocalLifecycleOwner()


//    // Получение CPointer для объекта mapViewGlobal
//    val mapViewPointer: CPointer<MKMapView> = mapViewGlobal.
//
//    val scope = rememberCoroutineScope()
//    mapViewGlobal?.region?.getPointer(scope)?.pointed?.span

    val setLocation by viewModel.setLocation.collectAsState()
    val publicInfo by viewModel.publicInfo.collectAsState()
    val locations by viewModel.locationsTitle.collectAsStateWithLifecycle()
    val mapCityCams by viewModel.mapCityCams.collectAsStateWithLifecycle()

    var showSomething by remember { mutableStateOf(false) }
    //var mapViewGlobal: MKMapView? by remember { mutableStateOf(null) }


    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            UIKitView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    val mkMapView = MKMapView()



                    mapViewGlobal = mkMapView
                    mkMapView
                },
                update = { mkMapView ->
////////////////////////////////////////////
                    // леха

                    settingMapKit(mkMapView = mkMapView, publicInfo = publicInfo)

//
//
//                    mkMapView.delegate = object : NSObject(), MKMapViewDelegateProtocol {
//
//
//                        override fun mapView(mapView: MKMapView, regionDidChangeAnimated: Boolean) {
//
//
////                            let center = mapView.centerCoordinate
////
////                                    mapLatitude = CGFloat(center.latitude)
////                            mapLonitude = CGFloat(center.longitude)
////                            let latAndLong = "Lat: \(mapLatitude) Long: \(mapLonitude)"
//
//                            val c = mapView.centerCoordinate
//                           // val mapLat = CGFloat(c.)
//
//
//
//
//                            Logger.d("4444 mkMapView 2 .mapView: $mapView" + " regionDidChangeAnimated=" + regionDidChangeAnimated)
//                            //super.mapView(mapView,regionDidChangeAnimated = true)
//                        }

//                        @Suppress("CONFLICTING_OVERLOADS")
//                        override fun mapView(
//                            mapView: MKMapView,
//                            regionWillChangeAnimated: Boolean,
//                        ) {
//                            Logger.d("4444 mkMapView 1 .mapView: $mapView")
//                            //  super.mapView(mapView, regionWillChangeAnimated)
//                        }
//
//                        @Suppress("CONFLICTING_OVERLOADS")
//                        override fun mapView(mapView: MKMapView, regionDidChangeAnimated: Boolean) {
//                            Logger.d("4444 mkMapView 2 .mapView: $mapView")
//                            //super.mapView(mapView,regionDidChangeAnimated = true)
//
//                            val bounds = mapView.layer().bounds
//                            val bounds2 = mapView.layer().bounds()
//                            val actions = mapView.layer().actions
//                            val zPosition = mapView.layer().zPosition
//                            val zPosition2 = mapView.layer().zPosition()
//                            val borderWidth = mapView.layer().borderWidth
//                            val borderWidth2 = mapView.layer().borderWidth()
//                            val contentsRect = mapView.layer().contentsRect
//                            //  val frame = mapView.layer().frame()
//
//
//                            val region1 = mapView.cameraBoundary?.region
//                            val maxCenterCoordinateDistance =
//                                mapView.cameraZoomRange.maxCenterCoordinateDistance
//                            val minCenterCoordinateDistance =
//                                mapView.cameraZoomRange.minCenterCoordinateDistance
//
//                            val heading = mapView.camera.heading
//
//
//                            Logger.d("4444 mkMapView 2 .bounds: $bounds")
//                            Logger.d("4444 mkMapView 2 .bounds2: $bounds2")
//                            Logger.d("4444 mkMapView 2 .actions: $actions")
//                            Logger.d("4444 mkMapView 2 .zPosition: $zPosition")
//                            Logger.d("4444 mkMapView 2 .zPosition2: $zPosition2")
//                            Logger.d("4444 mkMapView 2 .borderWidth: $borderWidth")
//                            Logger.d("4444 mkMapView 2 .borderWidth2: $borderWidth2")
//                            Logger.d("4444 mkMapView 2 .contentsRect: $contentsRect")
//                            // Logger.d("4444 mkMapView 2 .frame: $frame" )
//
//
//                            Logger.d("4444 mkMapView 2 .region1: $region1")
//                            Logger.d("4444 mkMapView 2 .maxCenterCoordinateDistance: $maxCenterCoordinateDistance")
//                            Logger.d("4444 mkMapView 2 .minCenterCoordinateDistance: $minCenterCoordinateDistance")
//
//
//                            Logger.d("4444 mkMapView 2 .heading: $heading")
////                            Logger.d("4444 mkMapView 2 .bounds: $zPosition2" )
////                            Logger.d("4444 mkMapView 2 .bounds: $borderWidth" )
////                            Logger.d("4444 mkMapView 2 .bounds: $borderWidth2" )
////                            Logger.d("4444 mkMapView 2 .bounds: $contentsRect" )
////                            Logger.d("4444 mkMapView 2 .bounds: $frame" )
//
//
//                        }
//
//                        override fun mapViewDidFinishLoadingMap(mapView: MKMapView) {
//                            Logger.d("4444 mkMapView mapViewDidFinishLoadingMap")
////                            super.mapViewDidFinishLoadingMap(mapView)
//                        }
//
//                        override fun mapViewDidChangeVisibleRegion(mapView: MKMapView) {
//                            Logger.d("4444 mkMapView mapViewDidChangeVisibleRegion")
//  //                          super.mapViewDidChangeVisibleRegion(mapView)
//                        }
//
//                        override fun mapViewDidFinishRenderingMap(
//                            mapView: MKMapView,
//                            fullyRendered: Boolean,
//                        ) {
//                            Logger.d("4444 mkMapView mapViewDidFinishRenderingMap")
//                            //super.mapViewDidFinishRenderingMap(mapView, fullyRendered)
//                        }

//                        override fun mapViewDidStopLocatingUser(mapView: MKMapView) {
//                            Logger.d("4444 mkMapView mapViewDidStopLocatingUser")
//                           // super.mapViewDidStopLocatingUser(mapView)
//                        }
//
//                        override fun mapViewWillStartLoadingMap(mapView: MKMapView) {
//                            Logger.d("4444 mkMapView mapViewWillStartLoadingMap")
//                            //super.mapViewWillStartLoadingMap(mapView)
//                        }
//
//                        override fun mapViewWillStartLocatingUser(mapView: MKMapView) {
//                            Logger.d("4444 mkMapView mapViewWillStartLocatingUser")
//                           // super.mapViewWillStartLocatingUser(mapView)
//                        }
//
//                        override fun mapViewWillStartRenderingMap(mapView: MKMapView) {
//                            Logger.d("4444 mkMapView mapViewWillStartRenderingMap")
//                           // super.mapViewWillStartRenderingMap(mapView)
//                        }
                    //////////////////////////////////////////////////////////////////
//                        override fun mapView(mapView: MKMapView, didAddAnnotationViews: List<*>) {
//                            Logger.d("4444 mkMapView didAddAnnotationViews")
//                           // super.mapView(mapView, didAddAnnotationViews)
//                        }
//
//                        override fun mapView(mapView: MKMapView, didDeselectAnnotation: MKAnnotationProtocol) {
//                            Logger.d("4444 mkMapView didDeselectAnnotation")
//                           // super.mapView(mapView, didDeselectAnnotation)
//                        }
//
//                        override fun mapView(mapView: MKMapView, rendererForOverlay: MKOverlayProtocol): MKOverlayRenderer {
//                            Logger.d("4444 mkMapView rendererForOverlay")
//                            return super.mapView(mapView, rendererForOverlay)
//                        }

//                        override fun mapView(mapView: MKMapView, didDeselectAnnotationView: MKAnnotationView) {
//                            Logger.d("4444 mkMapView didDeselectAnnotationView")
//                           // super.mapView(mapView, didDeselectAnnotationView)
//                        }
//
//                        override fun mapView(mapView: MKMapView, didUpdateUserLocation: MKUserLocation) {
//                            Logger.d("4444 mkMapView didUpdateUserLocation")
//                         //   super.mapView(mapView, didUpdateUserLocation)
//                        }
//
//                        override fun mapView(mapView: MKMapView, annotationView: MKAnnotationView, calloutAccessoryControlTapped: UIControl) {
//                            Logger.d("4444 mkMapView calloutAccessoryControlTapped")
//                            //super.mapView(mapView, annotationView, calloutAccessoryControlTapped)
//                        }
//
//                        override fun mapView(
//                            mapView: MKMapView,
//                            didChangeUserTrackingMode: MKUserTrackingMode,
//                            animated: Boolean
//                        ) {
//                            Logger.d("4444 mkMapView didChangeUserTrackingMode")
//                          //  super.mapView(mapView, didChangeUserTrackingMode, animated)
//                        }
//
//                        override fun mapView(
//                            mapView: MKMapView,
//                            annotationView: MKAnnotationView,
//                            didChangeDragState: MKAnnotationViewDragState,
//                            fromOldState: MKAnnotationViewDragState
//                        ) {
//                            Logger.d("4444 mkMapView didChangeDragState")
//                            //super.mapView(mapView, annotationView, didChangeDragState, fromOldState)
//                        }
//
//                        override fun finalize() {
//                            Logger.d("4444 mkMapView finalize")
//                            //super.finalize()
//                        }
                }


//                    mapViewGlobal?.delegate = object : NSObject(), MKMapViewDelegateProtocol {
//
//                    @Suppress("CONFLICTING_OVERLOADS")
//                    override fun mapView(mapView: MKMapView, regionWillChangeAnimated: Boolean) {
//                        Logger.d("4444 mkMapView 1 .mapView: $mapView")
//                        //  super.mapView(mapView, regionWillChangeAnimated)
//                    }
//
//                    @Suppress("CONFLICTING_OVERLOADS")
//                    override fun mapView(mapView: MKMapView, regionDidChangeAnimated: Boolean) {
//                        Logger.d("4444 mkMapView 2 .mapView: $mapView")
//                        //super.mapView(mapView,regionDidChangeAnimated = true)
//                    }
//
//                }
//                        override fun mapView(mapView: MKMapView, regionWillChangeAnimated: Boolean) {
//                            Logger.d("4444 mkMapView 1 .mapView: $mapView")
//
//                          //  super.mapView(mapView, regionWillChangeAnimated)
//                        }

//                        override fun mapView(mapView: MKMapView, regionDidChangeAnimated: Boolean) {
//                            Logger.d("4444 mkMapView 2 .mapView: $mapView")
//                            super.mapView(mapView,regionDidChangeAnimated = true)
//                        }

//////////////////////////////////////
//                    // установка маркеров

//                    val res = vectorResource(resource = Res.drawable.ic_favorite)
//
//                    mapCityCams?.let { listMarker ->
//                        val pins = listMarker.map { item ->
//                            val pin = MKPointAnnotation()
//                            pin.setCoordinate(CLLocationCoordinate2DMake(item.latitude, item.longitude))
//                            pin.setTitle(item.title)
//                            pin
//                        }
//                        mkMapView.addAnnotations(pins)
//                    }
                ///////////////////////////////////

//                    mapViewGlobal?.delegate = object : NSObject(), MKMapViewDelegateProtocol {
//
//                        override fun mapViewWillStartLocatingUser(mapView: MKMapView) { // не работает
//                            val res1 = mapView.camera.centerCoordinateDistance()
//                            Logger.d("4444 mkMapView.delegate: $res1")
//                        }
//
//                        override fun mapView(
//                            mapView: MKMapView,
//                            didUpdateUserLocation: MKUserLocation,
//                        ) { // не работает
//                            val res1 = mapView.camera.centerCoordinateDistance()
//                            val res2 = didUpdateUserLocation.coordinate
//                            Logger.d("4444 mkMapView.delegate: $res1, Longitude:$res2")
//                        }
//
//                        override fun mapViewWillStartLoadingMap(mapView: MKMapView) { // работет после загрузки
//                            showSomething = true
//                            Logger.d("4444 mkMapView.delegate: $showSomething")
//                        }
//
//                        override fun mapView(
//                            mapView: MKMapView,
//                            annotationView: MKAnnotationView,
//                            didChangeDragState: MKAnnotationViewDragState,
//                            fromOldState: MKAnnotationViewDragState,
//                        ) {
//                            Logger.d(
//                                "4444 mapViewGlobal?.annotationView: $annotationView" +
//                                        " didChangeDragState=" + didChangeDragState + " fromOldState=" + fromOldState
//                            )
//                        }
//
//                        override fun mapView(
//                            mapView: MKMapView,
//                            didChangeUserTrackingMode: MKUserTrackingMode,
//                            animated: Boolean,
//                        ) {
//                            Logger.d(
//                                "4444 mapViewGlobal?.didChangeUserTrackingMode: $didChangeUserTrackingMode" +
//                                        " animated=" + animated
//                            )
//                        }
//
//                    }
// /////////////////////////////////////////////////////////////////
//
//                    val locationManager = CLLocationManager()
//                    locationManager.delegate = object : NSObject(),
//                        CLLocationManagerDelegateProtocol {
//
//                        override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
//                            val locations1 = didUpdateLocations.filterIsInstance<CLLocation>()
//                            Logger.d { "4444  locationManager?.locations1=" + locations1 }
//                            if (locations1.isNotEmpty()) {
//                                val currentLocation = locations1.first()
//                                val latitude = currentLocation.coordinate.objcPtr()
//                                //val longitude = currentLocation.coordinate
////                            Logger.d("4444 Latitude: $latitude, Longitude:")
//                            }
////                        val res = MKCoordinateSpan(didUpdateLocations.objcPtr()).latitudeDelta
////                        Logger.d{"4444  locationManager?.res=" + res}
//                        }
//
//                        override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
//                            Logger.d("4444 locationManagerDidChangeAuthorization: ${manager.location}:")
//                        }
//
//                        override fun locationManagerDidResumeLocationUpdates(manager: CLLocationManager) {
//                            Logger.d("4444 locationManagerDidResumeLocationUpdates: ${manager.location}:")
//                        }
//                    }
//
//                    locationManager.requestWhenInUseAuthorization()
//                    locationManager.startUpdatingLocation()
// /////////////////////////////////////////////////////////////////
//                        it.delegate = object : CADisplayLink(), MKMapViewDelegateProtocol {
//
//                            override fun mapView(mapView: MKMapView, regionDidChangeAnimated: Boolean) {
//
//                                val res = mapView.camera.centerCoordinateDistance()
//                                val zoom = calculateZoomLevel(res)
//                                Logger.d { "4444  mapViewGlobal?.zoomzoom=" + zoom }
//
//
////                        super.mapView(mapView, regionDidChangeAnimated)
//                            }
//
//                            override fun mapViewDidChangeVisibleRegion(mapView: MKMapView) {
//
//                                val res = mapView.camera.centerCoordinateDistance()
//                                val zoom = calculateZoomLevel(res)
//                                Logger.d { "4444  mapViewGlobal?.zoomzoom=" + zoom }
//                            }
//                        }
//
//                        it.delegate = object : NSObject(), MKMapViewDelegateProtocol {
//                            override fun mapViewDidChangeVisibleRegion(mapView: MKMapView) {
//
//                                // mapView.
//                            }
//                        }
                ////////////////////////////
//                    val heading = it.camera().heading
//                    val centerCoordinateDistance = it.camera().centerCoordinateDistance
//                    val pitch = it.camera().pitch
//                    Logger.d(
//                        "4444 headiheadingng: ${heading}" + " centerCoordinateDistance="
//                                + centerCoordinateDistance + " pitch=" + pitch
//                    )
//
////       mapViewGlobal?.region?.getPointer()?.pointed?.span
/////////////////////////////////
//                    if (showSomething) {
//                        // Ваш код, который нужно выполнить, когда карта загружена
//                        Logger.d { "4444  mapViewGlobal?.camera=" + mapViewGlobal?.camera()?.centerCoordinate?.size }
//                    }
///////////////////////////////////////////
                //         }


            )


            //    mapViewGlobal?.addObserver(cLLocationManagerDelegate)

            mapViewGlobal?.delegate =
                object : NSObject(), ObserverProtocol, MKMapViewDelegateProtocol {

                    override fun mapView(mapView: MKMapView, regionDidChangeAnimated: Boolean) {


//                            let center = mapView.centerCoordinate
//
//                                    mapLatitude = CGFloat(center.latitude)
//                            mapLonitude = CGFloat(center.longitude)
//                            let latAndLong = "Lat: \(mapLatitude) Long: \(mapLonitude)"

                        val c = mapView.centerCoordinate
                        // val mapLat = CGFloat(c.)


                        Logger.d("4444 mkMapView 2 .mapView: $mapView" + " regionDidChangeAnimated=" + regionDidChangeAnimated)
                        //super.mapView(mapView,regionDidChangeAnimated = true)
                    }

                    override fun mapViewDidFinishRenderingMap(
                        mapView: MKMapView,
                        fullyRendered: Boolean,
                    ) {
                        Logger.d("4444 mkMapView mapViewDidFinishRenderingMap")
                        //super.mapViewDidFinishRenderingMap(mapView, fullyRendered)
                    }

                    override fun observeValueForKeyPath(
                        keyPath: String?,
                        ofObject: Any?,
                        change: Map<Any?, *>?,
                        context: COpaquePointer?,
                    ) {
                        TODO("Not yet implemented")


                    }


//                override fun mapView(
//                    mapView: MKMapView,
//                    rendererForOverlay: MKOverlayProtocol
//                ): MKOverlayView {
//                    // реализация вашей функции
//
//
//                    val mkMapCameraZoomRange = MKMapCameraZoomRange(3500.0)
//                    mapView.setCameraZoomRange(mkMapCameraZoomRange, true)
//                    //mapView.setCameraZoomRange(zoomRange, animated: true)
//
//
//
//                    return tileRenderer
//                }

//                override fun mapView(
//                    mapView: MKMapView,
//                    rendererForOverlay: MKOverlayProtocol
//                ): MKOverlayRenderer {
//                    return super.mapView(mapView, rendererForOverlay)
//                }


//                func mapView(_ mapView: MKMapView, rendererFor overlay: MKOverlay) -> MKOverlayRenderer {
//                    let zoomRange = MKMapView.CameraZoomRange(minCenterCoordinateDistance: 3500)
//                    mapView.setCameraZoomRange(zoomRange, animated: true)
//                    return tileRenderer
//                }
                }

////
//            val res: ImageVector = vectorResource(resource = Res.drawable.ic_favorite)
//
//
//           // val coder = NSCoder(res)
//            val data = NSData()
//            UIImage(data)
//            UIImageAsset()
//            UIImageView()
//
//            MKMarkerAnnotationView().setImage(UIImage("ic_lock.xml"))
//
//
//
//
//            mapCityCams?.let { listMarker ->
//                val pins: List<MKMarkerAnnotationView> = listMarker.map { item ->
//                    val pin = MKMarkerAnnotationView()
//
//
////                    pin.setCenter(CLLocationCoordinate2DMake(item.latitude, item.longitude))
////                    //pin.displayPriority(1.0F)
////                    pin.layer
////                    pin.setCoordinate(CLLocationCoordinate2DMake(item.latitude, item.longitude))
////                    pin.setTitle(item.title)
//                    pin
//                }
//                mapViewGlobal?.addAnnotations(pins)
//            }

//
//            mapCityCams?.let { listMarker ->
//                val pins: List<MKAnnotationView> = listMarker.map { item ->
//                    val pin = MKAnnotationView()
//                    pin.setCenter(CLLocationCoordinate2DMake(item.latitude, item.longitude))
//                    //pin.displayPriority(1.0F)
//                    pin.layer
//                    pin.setCoordinate(CLLocationCoordinate2DMake(item.latitude, item.longitude))
//                    pin.setTitle(item.title)
//                    pin
//                }
//                mapViewGlobal?.addAnnotations(pins)
//            }


//            mapCityCams?.let { listMarker ->
//                val pins: List<MKPinAnnotationView> = listMarker.map { item ->
//                    val pin = MKPinAnnotationView()
//                    pin.coordinateSpace()
//                    pin.setCoordinate(CLLocationCoordinate2DMake(item.latitude, item.longitude))
//                    pin.setTitle(item.title)
//                    pin
//                }
//                mapViewGlobal?.addAnnotations(pins)
//            }

//
////
//
            mapCityCams?.let { listMarker ->
                val pins: List<MKPointAnnotation> = listMarker.map { item ->
                    val pin = MKPointAnnotation()
                    pin.setCoordinate(CLLocationCoordinate2DMake(item.latitude, item.longitude))
                    pin.setTitle(item.title)
                    pin
                }
                mapViewGlobal?.addAnnotations(pins)
            }

            TopControl(
                locations = locations,
                viewModel = viewModel,
                setLocation = setLocation,
                paddingValue = paddingValue
            )

//            ZoomControl()

           // FindMeControl(paddingValue = paddingValue)
//
//            BottomControl(
//                paddingValue = paddingValue,
//                viewModel = viewModel,
//                context = context,
//                moveToBottomSheetMapFragment = { markerDetail ->
//                    moveToBottomSheetMapFragment(markerDetail)
//                }
//            )
        }
    }
}


//// Функция для преобразования NSString в CFStringRef
//@OptIn(ExperimentalForeignApi::class)
//fun nsStringToCFStringRef(nsString: NSString): CFStringRef {
//    val utf8String = nsString.cStringUsingEncoding(NSUTF8StringEncoding)?.toKString()
//    return CFStringCreateWithCString(null, utf8String, kCFStringEncodingUTF8) ?: throw IllegalStateException("Failed to convert NSString to CFStringRef")
//}

//@OptIn(ExperimentalForeignApi::class)
//fun createCGImageRef(): CGImageRef? {
//    // Создаем NSString из Kotlin String
//    val kotlinString = "some_text"
//    val nsString = NSString.
//
//    // Преобразуем NSString в CFStringRef
//    val cfStringRef = nsStringToCFStringRef(nsString)
//
//    // Здесь продолжайте ваш код для создания CGImageRef
//    val url = CFURLCreateWithFileSystemPath(null, "ic_lock.xml", 0.0)
//    val dataProvider = CGDataProviderCreateWithURL(url)
//    return CGImageCreateWithPNGDataProvider(dataProvider, null, true, null)
//}

//
//@OptIn(ExperimentalForeignApi::class)
//fun createCGImageRef(): CGImageRef? {
//    // Пример создания CGImageRef из файла PNG
//
//    val kotlinString = "ic_lock.xml"
//    val nsString = NSString.stringWithString(kotlinString) // Преобразование Kotlin String в NSString
//    val cfStringRef = nsString as CFStringRef   // Преобразование NSString в CFStringRef (с использованием __bridge)
//
////    val cfStringRef: CFStringRef = CFStringRef("some_text")
//    CFURLCreateWithFileSystemPath()
//    val url = CFURLCreateWithFileSystemPath(null, cfStringRef, null, null)
//    val dataProvider = CGDataProviderCreateWithURL(url)
//    return CGImageCreateWithPNGDataProvider(dataProvider, null, true, null)
//}

//@OptIn(ExperimentalForeignApi::class)
//fun drawPin(point: CGPoint, annotation: MKAnnotationView) {
//    val annotationView = MKMarkerAnnotationView()
//
//
//    val cgRectMake = CGRectMake(0.0, 0.0, 40.0, 40.0)
//    annotationView.setBounds(cgRectMake)
//
//
//   // val cgImage = CGImageRef()
//    val uiImage = UIImage()
//    annotationView.setImage(uiImage)
//
////    annotationView.contentMode = .scaleAspectFit
////            annotationView.bounds = CGRect(x: 0, y: 0, width: 40, height: 40)
////    annotationView.drawHierarchy(CGRect(
////    x: point.x - annotationView.bounds.size.width / 2.0,
////    y: point.y - annotationView.bounds.size.height,
////    width: annotationView.bounds.width,
////    height: annotationView.bounds.height),
////    afterScreenUpdates: true)
//}

// Создание пользовательского класса для кастомного маркера
//class CustomAnnotationView(reuseIdentifier: String) : MKPinAnnotationView(reuseIdentifier) {
//    init {
//        UIImageView
//        UIImage()
//
//        // Устанавливаем собственную картинку для маркера
//        val imageView = UIImageView(UIImage(named = "your_custom_image_name"))
//        imageView.frame = CGRect(x = 0.0, y = 0.0, width = 40.0, height = 40.0)
//        imageView.contentMode = UIViewContentMode.ScaleAspectFit
//        leftCalloutAccessoryView = imageView
//    }
//}


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
                        .background(color = Color.Transparent)
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

@OptIn(ExperimentalForeignApi::class)
private fun setZoomLocation(zoom: Double, center: String) {
    val latitude = center.substring(0, center.indexOf(",")).toDouble()
    val longitude = center.substring(center.indexOf(" ")).trim().toDouble()
    mapViewGlobal?.let {


        val coordinate = CLLocationCoordinate2DMake(
            latitude = latitude,
            longitude = longitude
        )

        val span = MKCoordinateSpanMake(
            latitudeDelta = 0.5, // Примерное значение для широты
            longitudeDelta = 0.5 // Примерное значение для долготы
        )

        val region = MKCoordinateRegionMake(
            coordinate,
            span = span
        )

// Установка региона на карте
        mapViewGlobal?.setRegion(region, animated = true)

// Установка географической точки на карту
//        mapViewGlobal?.setCenterCoordinate(CLLocationCoordinate2D(latitude, longitude), animated = true)
//
//        val res =
//        mapViewGlobal?.moveC
//        )
    }
}

//@OptIn(ExperimentalResourceApi::class)
//@Composable
//fun ZoomControl() {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(end = 16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.End
//    ) {
//        Icon(
//            modifier = Modifier
//                .padding(bottom = 8.dp)
//                //.systemBarsPadding() // Добавить отступ от скрытого статус-бара
//                .size(40.dp)
//                .clip(RoundedCornerShape(50))
//                .clickable {
//                    mapViewGlobal?.let { mapView ->
//
//                        val zoomLevel = mapViewGlobal?.cameraZoomRange()
//                        val getLocal = getLocationScreen(mapView = mapView)
////                        val zoomLevel = mapView.zoomLevelDouble
////                        val getLocal = getLocationScreen(mapView = mapView)
//
//
//
//                        setZoomLocation(zoomLevel.plus(1.0), getLocal)
//                    }
//                },
//            // .clip(RoundedCornerShape(50)),
//            imageVector = vectorResource(Res.drawable.ic_map_zoom_plus),
//            contentDescription = "plus",
//
//            )
//        Icon(
//            modifier = Modifier
//                //.padding(),
//                //.systemBarsPadding() // Добавить отступ от скрытого статус-бара
//                .size(40.dp)
//                .clip(RoundedCornerShape(50))
//                .clickable {
//                    mapViewGlobal?.let { mapView ->
//                        val zoomLevel = mapView.zoomLevelDouble
//                        val getLocal = getLocationScreen(mapView = mapView)
//
//                        setZoomLocation(zoomLevel.plus(-1.0), getLocal)
//                    }
////                    zoomLevel?.let {
////                        viewModel.setZoomAfterClick(it.plus(-1.0), zoomLocation)
////                    }
////                    viewModel.clickCountZoom(getCurrentZoom(-1.0), getLocationScreen())
//                    // viewModel.clickCountZoom(-1.0)
//                },
//            // .clip(RoundedCornerShape(50)),
//            imageVector = vectorResource(Res.drawable.ic_map_zoom_minus),
//            contentDescription = "minus",
//
//            )
//    }
//}


class CheckLocationDelegate : NSObject(), CLLocationManagerDelegateProtocol {



    fun checkIfLocationServiceIsEnabled() {
        if (CLLocationManager.locationServicesEnabled()) {
            clLocationManager = CLLocationManager()
            clLocationManager?.desiredAccuracy = kCLLocationAccuracyBest
            clLocationManager?.delegate = this
           // checkLocationAuthorization()
        } else {
            Logger.d { "отключено! включите" }
        }
    }

    private fun checkLocationAuthorization() {
        Logger.d { "4444 checkLocationAuthorization" }
        when (CLLocationManager().authorizationStatus()) {
            //  доступ к данным о местоположении только в том случае, если ваше приложение активно
            // и в старых версиях iOS и означает разрешение на использование местоположения без конкретизации,
            // когда оно дается на всегда или только при использовании приложения.
            kCLAuthorizationStatusAuthorizedWhenInUse, kCLAuthorizationStatusAuthorized -> {
                Logger.d{"4444 kCLAuthorizationStatusAuthorizedWhenInUse"}
            }

            // пользователь отказал вашему приложению в доступе к данным о местоположении
            kCLAuthorizationStatusDenied -> {
                Logger.d{"4444 kCLAuthorizationStatusDenied"}
               // SnackbarHostHelper.Snackbar("StatusDenied")
            }

            // еще не принял решение относительно разрешений на местоположение
            kCLAuthorizationStatusNotDetermined -> {
                Logger.d{"4444 kCLAuthorizationStatusNotDetermined"}
                clLocationManager?.requestWhenInUseAuthorization()
            }

            // родительский контроль или другие ограничения предотвращают ваше приложение от получения доступа
            kCLAuthorizationStatusRestricted -> {
                Logger.d{"4444 kCLAuthorizationStatusRestricted"}
               // SnackbarHostHelper.Snackbar("StatusRestricted")
            }
        }
    }

    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
        checkLocationAuthorization()
    }

}


@Composable
fun FindMeControl(paddingValue: PaddingValues) {

//    val hui: NSObject = object : NSObject(), CLLocationManagerDelegateProtocol {
//        override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
//            checkIfLocationServiceIsEnabled()
//        }
//    }
    val checkLocationServiceState = remember { mutableStateOf(false) }
    val scope = CoroutineScope(Dispatchers.IO)



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValue),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        IconButton(
            onClick = {
               // checkLocationServiceState.value = true



            }) {
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    // .weight(1f)
                    .width(IntrinsicSize.Max),
                imageVector = vectorResource(Res.drawable.ic_favorite),
                contentDescription = null
            )
        }
    }

   // if (checkLocationServiceState.value) {
        scope.launch {
            CheckLocationDelegate().checkIfLocationServiceIsEnabled()

        }





    //}
}






//
//@Composable
//fun BottomControl(
//    paddingValue: PaddingValues,
//    viewModel: MapScreenViewModel,
//    context: Context,
//    moveToBottomSheetMapFragment: (MarkerDetail) -> Unit,
//) {
//    val mapCategories = viewModel.mapCategories.collectAsStateWithLifecycle()
//
//    val mapCityCams by viewModel.mapCityCams.collectAsStateWithLifecycle()
//    val mapOutDoorCams by viewModel.mapOutDoorCams.collectAsStateWithLifecycle()
//    val mapDomofonCams by viewModel.mapDomofonCams.collectAsStateWithLifecycle()
//    val mapOffice by viewModel.mapOffice.collectAsStateWithLifecycle()
//
//    val lazyListState = rememberLazyListState()
//    var indexClickCityCam by remember { mutableStateOf(true) }
//    var indexClickOutdoor by remember { mutableStateOf(false) }
//    var indexClickDomofon by remember { mutableStateOf(false) }
//    var indexClickOffice by remember { mutableStateOf(true) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(bottom = 84.dp)
//            .padding(paddingValue),
//        //  .background(Color.Red),
//        verticalArrangement = Arrangement.Bottom,
//    ) {
//        LazyRow(
//            state = lazyListState,
//            // contentPadding = paddingValue
//        ) {
//            itemsIndexed(mapCategories.value) { index, item ->
//                Spacer(modifier = Modifier.width(4.dp))
//                Row(
//                    modifier = Modifier
//                        .clickable {
//                            // трабл с этого метода
//                            viewModel.onClickCategory(position = index)
//                            if (index == 0) {
//                                indexClickCityCam = !indexClickCityCam
//                            }
//                            if (index == 1) {
//                                indexClickOutdoor = !indexClickOutdoor
//                            }
//                            if (index == 2) {
//                                indexClickDomofon = !indexClickDomofon
//                            }
//                            if (index == 3) {
//                                indexClickOffice = !indexClickOffice
//                            }
//                        }
//                        .clip(RoundedCornerShape(10))
//                        .height(40.dp)
//                        .background(
//                            method(
//                                index = index,
//                                indexClickCityCam = indexClickCityCam,
//                                indexClickOutdoor = indexClickOutdoor,
//                                indexClickDomofon = indexClickDomofon,
//                                indexClickOffice = indexClickOffice
//                            )
//                        )
//                        .padding(horizontal = 16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        // modifier = Modifier.fillMaxWidth(),
//                        text = item.title,
//                        color = Color.White
//                    )
//                    Text(
//                        modifier = Modifier.padding(start = 16.dp),
//                        text = item.count.toString(),
//                        color = Color.Green
//                    )
//                }
//            }
//            item {
//                Spacer(modifier = Modifier.width(4.dp))
//            }
//        }
//    }
//
//
//    outdoorMarkers(
//        mapOutDoorCams = mapOutDoorCams,
//        context = context,
//        moveToBottomSheetMapFragment = { markerDetail ->
//            moveToBottomSheetMapFragment(markerDetail)
//        }
//    )
//    domofonMarkers(
//        mapDomofonCams = mapDomofonCams,
//        context = context,
//        moveToBottomSheetMapFragment = { markerDetail ->
//            moveToBottomSheetMapFragment(markerDetail)
//        }
//    )
//    cityMarkers(
//        mapCityCams = mapCityCams,
//        context = context,
//        moveToBottomSheetMapFragment = { markerDetail ->
//            moveToBottomSheetMapFragment(markerDetail)
//        }
//    )
//    officeMarkers(
//        mapOffice = mapOffice,
//        context = context,
//        moveToBottomSheetMapFragment = { markerDetail ->
//            moveToBottomSheetMapFragment(markerDetail)
//        })
//}
//
//fun cityMarkers(
//    mapCityCams: List<MarkerCityCam>?,
//    context: Context,
//    moveToBottomSheetMapFragment: (MarkerDetail) -> Unit,
//) {
//    mapCityCams?.let {
//
//        removeMarkersIsNotEmpty(mapCityCams, listMarkersCityCam)
//        removeMarkersIsNotEmpty(mapCityCams, listMarkersCityTriangle)
//
//        var markerCityCam: Marker? = null
//        for (cityCam in mapCityCams ?: emptyList()) {
//            co.touchlab.kermit.Logger.d { " 4444 SetMapView android cityCam 1" }
//
//            markerCityTriangle =
//                MarkerCityTriangle(mapViewGlobal) // маркер угла должен быть первый добавлен
//            mapViewGlobal?.overlays?.add(markerCityTriangle)
//            mapViewGlobal?.invalidate() // перерисовка из главного потока // postInvalidate из фонового
//            listMarkersCityTriangle.add(markerCityTriangle)
//        }
//
//        for (cityCam in mapCityCams ?: emptyList()) {
//            co.touchlab.kermit.Logger.d { " 4444 SetMapView android cityCam 2" }
//
//            markerCityCam = MarkerCityCamera(mapViewGlobal) // маркер камеры
//
//            val geoPointCityCam = GeoPoint(cityCam.latitude, cityCam.longitude)
//            val title = cityCam.title
//
//            val iconCityCam = ContextCompat.getDrawable(
//                context,
//                R.drawable.ic_map_city_cam
//            )
//
//            setMarkerParams(
//                marker = markerCityCam,
//                geoPoint = geoPointCityCam,
//                iconId = iconCityCam,
//                title = title
//            )
//
//            //////// //markerCityTriangle = Marker(binding.mapView) // маркер угла должен быть первый добавлен
//
//            // binding.mapView.overlays.add(markerCityTriangle)
//            // маркер камер должен быть вторым установлен (т.е. поверх первого)
//            mapViewGlobal?.overlays?.add(markerCityCam)
//            mapViewGlobal?.invalidate() // перерисовка из главного потока // postInvalidate из фонового
//
//            showInfoMarkerWindow(
//                markerCityCam, cityCam,
//                moveToBottomSheetMapFragment = { markerDetail ->
//                    moveToBottomSheetMapFragment(markerDetail)
//                }
//            )
//
//            listMarkersCityCam.add(markerCityCam) // список для только для удаления маркеров markerCams
//            //  listMarkersCityTriangle.add(markerCityTriangle)
//            listCityCam.add(cityCam)
//        }
//
//        val iconTriangle = ContextCompat.getDrawable(
//            context,
//            R.drawable.ic_map_triangle
//        )
//        createMarkerTriangle(iconTriangle = iconTriangle)
//
////        val overlays: List<Overlay> = mapViewGlobal.overlays
//        // showFavoriteCityCam(overlays = overlays)
//
//        closeInfoMarkerWindow(markerCityCam)
//
//        mapViewGlobal?.let {
//            setIconMarkerTriangle(it.zoomLevelDouble)
//        }
//
//        removeMarkersIsEmpty(mapCityCams, listMarkersCityCam)
//        removeMarkersIsEmpty(mapCityCams, listMarkersCityTriangle)
//
////        removeMarkers(mapCityCams, listMarkersCityCam)
////        removeMarkers(mapCityCams, listMarkersCityTriangle)
//    }
//}
//
//@Composable
//@OptIn(ExperimentalResourceApi::class)
//fun outdoorMarkers(
//    mapOutDoorCams: List<MarkerOutdoor>?,
//    context: Context,
//    moveToBottomSheetMapFragment: (MarkerDetail) -> Unit,
//) {
//    mapOutDoorCams?.let {
//
//        removeMarkersIsNotEmpty(it, listMarkersOutDoor)
//
//        var markerOutDoorCam: Marker? = null
//        for (outDoorCam in it) {
//
//            markerOutDoorCam = Marker(mapViewGlobal) // маркер камеры
//
//            val res = vectorResource(Res.drawable.ic_map_zoom_plus)
//            val geoPoint = GeoPoint(outDoorCam.latitude, outDoorCam.longitude)
//            val iconId = ContextCompat.getDrawable(context, R.drawable.ic_map_outdoor)
//            val title = outDoorCam.title
//
//            setMarkerParams(
//                marker = markerOutDoorCam,
//                geoPoint = geoPoint,
//                iconId = iconId,
//                title = title
//            )
//
//            mapViewGlobal?.overlays?.add(markerOutDoorCam)
//            mapViewGlobal?.invalidate() // перерисовка из главного потока // postInvalidate из фонового
//
//            showInfoMarkerWindow(
//                markerOutDoorCam,
//                outDoorCam,
//                moveToBottomSheetMapFragment = { markerDetail ->
//                    moveToBottomSheetMapFragment(markerDetail)
//                }
//            )
//
//            listMarkersOutDoor.add(markerOutDoorCam) // список для только для удаления маркеров markerCams
//            listOutDoorCam.add(outDoorCam)
//        }
//
//        closeInfoMarkerWindow(markerOutDoorCam)
//        //setIconMarkerMap(binding.mapView.zoomLevel.toDouble())
//
//
//        removeMarkersIsEmpty(it, listMarkersOutDoor)
////        removeMarkers(it, listMarkersOutDoor)
//    }
//}
//
//fun domofonMarkers(
//    mapDomofonCams: List<MarkerDomofon>?,
//    context: Context,
//    moveToBottomSheetMapFragment: (MarkerDetail) -> Unit,
//) {
//    mapDomofonCams?.let {
//
//        removeMarkersIsNotEmpty(it, listMarkersDomofon)
//
//        var markerDomofonCam: Marker? = null
//        for (domofonCam in it) {
//            markerDomofonCam = Marker(mapViewGlobal) // маркер камеры
//
//            val geoPoint = GeoPoint(domofonCam.latitude, domofonCam.longitude)
//            val iconId = ContextCompat.getDrawable(context, R.drawable.ic_map_domofon)
//            val title = domofonCam.title
//
//            setMarkerParams(
//                marker = markerDomofonCam,
//                geoPoint = geoPoint,
//                iconId = iconId,
//                title = title
//            )
//
//            mapViewGlobal?.overlays?.add(markerDomofonCam)
//            mapViewGlobal?.invalidate() // перерисовка из главного потока // postInvalidate из фонового
//
//            showInfoMarkerWindow(
//                markerDomofonCam,
//                domofonCam,
//                moveToBottomSheetMapFragment = { markerDetail ->
//                    moveToBottomSheetMapFragment(markerDetail)
//                }
//            )
//
//            listMarkersDomofon.add(markerDomofonCam) // список для только для удаления маркеров markerCams
//            listDomofonCam.add(domofonCam)
//        }
//
//        closeInfoMarkerWindow(markerDomofonCam)
//        //  setIconMarkerMap(binding.mapView.zoomLevel.toDouble())
//
//        removeMarkersIsEmpty(it, listMarkersDomofon)
////        removeMarkers(it, listMarkersDomofon)
//    }
//}
//
//fun officeMarkers(
//    mapOffice: List<MarkerOffice>?,
//    context: Context,
//    moveToBottomSheetMapFragment: (MarkerDetail) -> Unit,
//) {
//    mapOffice?.let {
//
//        removeMarkersIsNotEmpty(it, listMarkersOffice)
//
//        var markerOffice: Marker? = null
//        for (office in it) {
//
//            markerOffice = Marker(mapViewGlobal) // маркер камеры
//
//            val geoPoint = GeoPoint(office.latitude, office.longitude)
//            val iconId = ContextCompat.getDrawable(context, R.drawable.ic_map_office)
//            val title = office.title
//
//            setMarkerParams(
//                marker = markerOffice,
//                geoPoint = geoPoint,
//                iconId = iconId,
//                title = title
//            )
//
//            mapViewGlobal?.overlays?.add(markerOffice)
//            mapViewGlobal?.invalidate() // перерисовка из главного потока // postInvalidate из фонового
//
//            showInfoMarkerWindow(
//                markerOffice,
//                office,
//                moveToBottomSheetMapFragment = { markerDetail ->
//                    moveToBottomSheetMapFragment(markerDetail)
//                }
//            )
//
//            listMarkersOffice.add(markerOffice) // список для только для удаления маркеров markerCams
//            listOffice.add(office)
//        }
//
//        closeInfoMarkerWindow(markerOffice)
//        //  setIconMarkerMap(binding.mapView.zoomLevel.toDouble())
//
//        removeMarkersIsEmpty(it, listMarkersOffice)
////        removeMarkers(it, listMarkersOffice)
//    }
//}

//@Composable
//fun SetLocalLifecycleOwner() {
//    val localLifecycleOwner = LocalLifecycleOwner.current
//    DisposableEffect(
//        key1 = localLifecycleOwner,
//        effect = {
//            val observer = LifecycleEventObserver { _, event ->
//                when (event) {
//                    Lifecycle.Event.ON_START -> {
//                        Logger.d { "4444 MapScreen Lifecycle.Event.ON_START" }
//                    }
//
//                    Lifecycle.Event.ON_STOP -> { // когда свернул
//                        Logger.d { "4444 MapScreen Lifecycle.Event.ON_STOP" }
//                    }
//
//                    Lifecycle.Event.ON_DESTROY -> { // когда удалил из стека
//                        Logger.d { "4444 MapScreen Lifecycle.Event.ON_DESTROY" }
//                    }
//
//                    else -> {}
//                }
//            }
//            localLifecycleOwner.lifecycle.addObserver(observer)
//            onDispose {
//                localLifecycleOwner.lifecycle.removeObserver(observer)
//            }
//        }
//    )
//}
