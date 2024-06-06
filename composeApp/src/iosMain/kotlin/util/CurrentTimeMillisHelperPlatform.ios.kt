package util

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual class CurrentTimeMillisHelperPlatform actual constructor() {
    actual fun getTime(): Long {
        return (NSDate().timeIntervalSince1970 * 1000).toLong()
    }
}