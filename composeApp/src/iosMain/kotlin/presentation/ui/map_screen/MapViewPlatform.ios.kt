package presentation.ui.map_screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import presentation.ui.map_screen.model.MarkerDetail

actual class MapViewPlatform actual constructor() {
    actual fun getMapViewManipulation(): String {

        return "ios"
    }

    @Composable
    actual fun SetMapView(
        paddingValue: PaddingValues,
        viewModel: MapScreenViewModel,
        moveToBottomSheetMapFragment: (MarkerDetail) -> Unit
    ) {


        MapScreenIosActual(
            paddingValue = paddingValue,
            moveToBottomSheetMapFragment = { markerDetail ->
                moveToBottomSheetMapFragment(markerDetail)
            }
        )
    }
}

