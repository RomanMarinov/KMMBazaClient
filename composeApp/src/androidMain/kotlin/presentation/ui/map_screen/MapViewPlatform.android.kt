package presentation.ui.map_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import presentation.ui.map_screen.model.MarkerDetail

class MarkerCityCamera(mapView: MapView?) : Marker(mapView)
class MarkerCityTriangle(mapView: MapView?) : Marker(mapView)


actual class MapViewPlatform actual constructor() {

    actual fun getMapViewManipulation(): String {
        return "android"
    }

    @SuppressLint("UnrememberedMutableState", "SuspiciousIndentation")
    @Composable
    actual fun SetMapView(
        paddingValue: PaddingValues,
        viewModel: MapScreenViewModel,
        moveToBottomSheetMapFragment: (MarkerDetail) -> Unit
    ) {

        MapScreenAndroidActual(
            paddingValue = paddingValue,
            moveToBottomSheetMapFragment = { markerDetail ->
                 moveToBottomSheetMapFragment(markerDetail)
            }
        )
    }
}