package presentation.ui.outdoor_screen

import androidx.compose.runtime.Composable

expect class OpenUrlFromOutdoorPlatform() {
    @Composable
    fun execute(url: String)
}