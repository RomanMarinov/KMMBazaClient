package presentation.ui.outdoor_screen

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

actual class OpenUrlFromOutdoorPlatform actual constructor() {
    @Composable
    actual fun execute(url: String) {
        val context = LocalContext.current
        try {

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(browserIntent)
            // findNavController().popBackStack()
//            dialog?.dismiss()

        } catch (e: Exception) {
            Log.d("4444", "Ошибка открытия URL: " + e.message)
        }
    }
}