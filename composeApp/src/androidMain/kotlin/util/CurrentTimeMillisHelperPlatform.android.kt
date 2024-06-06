package util

actual class CurrentTimeMillisHelperPlatform actual constructor() {
    actual fun getTime(): Long {
        return System.currentTimeMillis()
    }
}