package presentation.ui.help_screen

import androidx.compose.runtime.Composable
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class CallHelpScreenPlatform actual constructor() {
    @Composable
    actual fun makeCall(phoneNumber: String) {
        val nsUrl = NSURL(string = "tel://$phoneNumber")
        val application = UIApplication.sharedApplication()
        if (application.canOpenURL(nsUrl)) {
            application.openURL(nsUrl)
        }
    }
}