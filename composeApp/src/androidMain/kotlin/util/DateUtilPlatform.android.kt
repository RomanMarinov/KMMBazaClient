package util

import java.text.SimpleDateFormat
import java.util.Locale

actual class DateUtilPlatform {
    actual fun getTimeStamp(dateString: String): Long {
        val formatter = SimpleDateFormat("dd.MM.yy HH:mm:ss", Locale.getDefault())
        val date = formatter.parse(dateString)

        return date.time / 1000

    }
}