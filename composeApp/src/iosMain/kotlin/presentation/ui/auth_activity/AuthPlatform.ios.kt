package presentation.ui.auth_activity

import androidx.compose.runtime.Composable
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.value
import platform.Foundation.NSURL
import platform.SystemConfiguration.SCNetworkReachabilityCreateWithAddress
import platform.SystemConfiguration.SCNetworkReachabilityFlagsVar
import platform.SystemConfiguration.SCNetworkReachabilityGetFlags
import platform.SystemConfiguration.kSCNetworkFlagsConnectionRequired
import platform.SystemConfiguration.kSCNetworkFlagsReachable
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.darwin.UInt8
import platform.posix.AF_INET
import platform.posix.sockaddr_in

actual class AuthPlatform actual constructor() {
    @Composable
    actual fun MakeCall() {
        val nsUrl = NSURL(string = "tel://88001000249")
        val application = UIApplication.sharedApplication()
        if (application.canOpenURL(nsUrl)) {
            application.openURL(nsUrl)
        }
    }

    @Composable
    actual fun getFingerprint(): String {
        val identifierForVendor = UIDevice.currentDevice.identifierForVendor
        return identifierForVendor?.UUIDString ?: ""
    }

    @OptIn(ExperimentalForeignApi::class)
    @Composable
    actual fun getInetCellular(): Boolean {
        memScoped {
            val zeroAddress = alloc<sockaddr_in>()
            zeroAddress.sin_len = UInt8.SIZE_BYTES.toUByte()
            zeroAddress.sin_family = AF_INET.toUByte()

            val defaultRouteReachability = SCNetworkReachabilityCreateWithAddress(null, zeroAddress.ptr.reinterpret())

            val flags = alloc<SCNetworkReachabilityFlagsVar>()
            if (SCNetworkReachabilityGetFlags(defaultRouteReachability, flags.ptr).not()) {
                return false
            }

            val isReachable = flags.value and kSCNetworkFlagsReachable != 0u
            val needsConnection = flags.value and kSCNetworkFlagsConnectionRequired != 0u

            return isReachable && !needsConnection
        }
    }
}
