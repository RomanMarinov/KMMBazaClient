package presentation.ui.help_screen

import androidx.compose.runtime.Composable

expect class OpenUrlFromHelpPlatform() {
    @Composable
    fun execute(url: String)
}