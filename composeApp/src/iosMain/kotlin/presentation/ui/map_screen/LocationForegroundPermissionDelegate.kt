//package presentation.ui.map_screen
//
//
//
//import com.adrianwitaszak.kmmpermissions.permissions.model.PermissionState
//import com.adrianwitaszak.kmmpermissions.permissions.util.openAppSettingsPage
//import platform.CoreLocation.CLLocationManager
//import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
//import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
//import platform.CoreLocation.kCLAuthorizationStatusDenied
//import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
//import platform.CoreLocation.kCLAuthorizationStatusRestricted
//import platform.darwin.NSObject
//
//internal class LocationForegroundPermissionDelegate = object NSObject(),  PermissionDelegate {
//    private var locationManager = CLLocationManager()
//
//    override fun getPermissionState(): PermissionState {
//        return when (locationManager.authorizationStatus()) {
//            kCLAuthorizationStatusAuthorizedAlways,
//            kCLAuthorizationStatusAuthorizedWhenInUse,
//            kCLAuthorizationStatusRestricted -> PermissionState.GRANTED
//
//            kCLAuthorizationStatusNotDetermined -> PermissionState.NOT_DETERMINED
//            kCLAuthorizationStatusDenied -> PermissionState.DENIED
//            else -> PermissionState.NOT_DETERMINED
//        }
//    }
//
//    override suspend fun providePermission() {
//        locationManager.requestWhenInUseAuthorization()
//    }
//
//    override fun openSettingPage() {
//        openAppSettingsPage()
//    }
//}