package presentation.ui.outdoor_screen

import androidx.compose.runtime.Composable
import co.touchlab.kermit.Logger
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class OpenUrlFromOutdoorPlatform actual constructor() {
    @Composable
    actual fun execute(url: String) {
        try {
            val nsUrl = NSURL.URLWithString(url)
            if (nsUrl != null) {
                UIApplication.sharedApplication.openURL(nsUrl)
            } else {
                Logger.d("4444 Invalid URL: $url")
            }
        } catch (e: Exception) {
            Logger.d("4444 Error opening URL: ${e.message}")
        }
    }
}