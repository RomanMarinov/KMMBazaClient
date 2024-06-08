package presentation.ui.help_screen

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

actual class CallHelpScreenPlatform actual constructor() {
    @Composable
    actual fun makeCall(phoneNumber: String) {
        val context = LocalContext.current
        val uri = Uri.parse("tel:$phoneNumber")
        val intent = Intent(Intent.ACTION_DIAL, uri)
        context.startActivity(intent)
    }
}