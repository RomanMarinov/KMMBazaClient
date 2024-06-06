package presentation.ui.map_screen

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import observer.ObserverProtocol
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.darwin.NSObject


@OptIn(ExperimentalForeignApi::class)
val cLLocationManagerDelegate: NSObject = object :  NSObject(), ObserverProtocol, CLLocationManagerDelegateProtocol {
    override fun observeValueForKeyPath(
        keyPath: String?,
        ofObject: Any?,
        change: Map<Any?, *>?,
        context: COpaquePointer?,
    ) {
        TODO("Not yet implemented")
    }


}