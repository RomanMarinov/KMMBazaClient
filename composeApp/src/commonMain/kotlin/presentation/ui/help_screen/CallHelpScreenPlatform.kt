package presentation.ui.help_screen

import androidx.compose.runtime.Composable

expect class CallHelpScreenPlatform() {
    @Composable
    fun makeCall(phoneNumber: String)
}