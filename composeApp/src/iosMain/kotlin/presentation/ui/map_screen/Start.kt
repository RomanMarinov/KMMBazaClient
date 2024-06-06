//package presentation.ui.map_screen
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import platform.CoreLocation.CLLocationCoordinate2DMake
//import platform.MapKit.MKCoordinateRegionMake
//import platform.MapKit.MKCoordinateSpanMake
//import platform.MapKit.MKMapView
//import platform.MapKit.MKMapViewDelegateProtocol
//import platform.darwin.NSObject
//
//
//var showSomething by remember { mutableStateOf(false) }
//var mapView: MKMapView? by remember { mutableStateOf(null) }
//
//Column(modifier = Modifier.fillMaxSize()) {
//    Box(modifier = Modifier.fillMaxSize()) {
//
//
//    }
//}
//
//UIKitView(factory = {
//    val mkMapView = MKMapView()
//
//    val center = CLLocationCoordinate2DMake(
//        latitude = 59.222340,
//        longitude = 39.882431
//    )
//
//    val span = MKCoordinateSpanMake(
//        latitudeDelta = 0.5, // Примерное значение для широты
//        longitudeDelta = 0.5 // Примерное значение для долготы
//    )
//
//    val region = MKCoordinateRegionMake(
//        center,
//        span = span
//    )
//
//    mkMapView.setRegion(region, animated = true)
//    //mkMapView.setCameraZoomRange(MKMapCameraZoomRange(13.0))
//
//    mkMapView.delegate = object : NSObject(), MKMapViewDelegateProtocol {
//        override fun mapViewWillStartLoadingMap(mapView: MKMapView) {
//            showSomething = true
//        }
//    }
//
//    mapView = mkMapView
//
//    mkMapView
//}, Modifier.fillMaxSize())
//
//if (showSomething) {
//    // Ваш код, который нужно выполнить, когда карта загружена
//}
//
//
////        var showSomething by remember { mutableStateOf(false) }
////
////        UIKitView(factory = {
////            val mkMapView = MKMapView()
////
////            mkMapView.delegate = object : NSObject(), MKMapViewDelegateProtocol {
////
////                override fun mapViewWillStartLoadingMap(mapView: MKMapView) {
////                   // super.mapViewWillStartLoadingMap(mapView)
////                    showSomething = true
////                }
////            }
////
////            mkMapView
////        }, Modifier.fillMaxSize())
////
////        if (showSomething) {
////
////        }
////////////////////////////////////////
//
////        var showSomething by remember { mutableStateOf(false) }
////
////        UIKitView(factory = {
////
////
////            val mkMapView = MKMapView()
////
////            mkMapView.delegate = object : MKMapViewDelegateProtocol {
//////            override fun mapView(mapView: MKMapView, didDeselectAnnotation: MKAnnotationProtocol) {
//////                showSomething = true
//////            }
////                override fun mapViewWillStartLoadingMap(mapView: MKMapView) {
////                    super.mapViewWillStartLoadingMap(mapView)
////                    showSomething = true
////                }
////
////                override fun description(): String? {
////                    TODO("Not yet implemented")
////                }
////
////                override fun hash(): NSUInteger {
////                    TODO("Not yet implemented")
////                }
////
////                override fun superclass(): ObjCClass? {
////                    TODO("Not yet implemented")
////                }
////
////                override fun `class`(): ObjCClass? {
////                    TODO("Not yet implemented")
////                }
////
////                override fun conformsToProtocol(aProtocol: Protocol?): Boolean {
////                    TODO("Not yet implemented")
////                }
////
////                override fun isEqual(`object`: Any?): Boolean {
////                    TODO("Not yet implemented")
////                }
////
////                override fun isKindOfClass(aClass: ObjCClass?): Boolean {
////                    TODO("Not yet implemented")
////                }
////
////                override fun isMemberOfClass(aClass: ObjCClass?): Boolean {
////                    TODO("Not yet implemented")
////                }
////
////                override fun isProxy(): Boolean {
////                    TODO("Not yet implemented")
////                }
////
////                override fun performSelector(aSelector: COpaquePointer?): Any? {
////                    TODO("Not yet implemented")
////                }
////
////                override fun performSelector(aSelector: COpaquePointer?, withObject: Any?): Any? {
////                    TODO("Not yet implemented")
////                }
////
////                override fun performSelector(
////                    aSelector: COpaquePointer?,
////                    withObject: Any?,
////                    _withObject: Any?,
////                ): Any? {
////                    TODO("Not yet implemented")
////                }
////
////                override fun respondsToSelector(aSelector: COpaquePointer?): Boolean {
////                    TODO("Not yet implemented")
////                }
////            }
////
////            mkMapView
////        }, Modifier.fillMaxSize())
////
////        if (showSomething) {
////
////        }