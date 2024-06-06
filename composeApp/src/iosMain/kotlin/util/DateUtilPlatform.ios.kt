package util

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.timeIntervalSince1970

actual class DateUtilPlatform {
    actual fun getTimeStamp(dateString: String): Long {
        val formatter = NSDateFormatter()
        formatter.dateFormat = "dd.MM.yy HH:mm:ss"
        val date: NSDate = formatter.dateFromString(dateString) ?: NSDate()
        return (date.timeIntervalSince1970.toLong())
    }
}