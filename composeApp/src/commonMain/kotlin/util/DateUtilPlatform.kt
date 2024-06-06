package util

expect class DateUtilPlatform() {
    fun getTimeStamp(dateString: String): Long
}