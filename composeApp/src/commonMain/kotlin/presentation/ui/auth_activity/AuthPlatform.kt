package presentation.ui.auth_activity

import androidx.compose.runtime.Composable

expect class AuthPlatform() {
    @Composable
    fun MakeCall()

    @Composable
    fun getFingerprint() : String

    @Composable
    fun getInetCellular() : Boolean
}