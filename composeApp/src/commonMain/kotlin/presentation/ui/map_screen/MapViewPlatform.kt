package presentation.ui.map_screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import presentation.ui.map_screen.model.MarkerDetail


expect class MapViewPlatform() {
    fun getMapViewManipulation(): String

    @Composable
    fun SetMapView(
        paddingValue: PaddingValues,
        viewModel: MapScreenViewModel,
        moveToBottomSheetMapFragment: (MarkerDetail) -> Unit,
    )
}