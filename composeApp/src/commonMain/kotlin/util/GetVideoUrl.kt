package util

object GetVideoUrl {
    fun addFromParameterToUrl(url: String?, timestamp: Long?): String {
        val tokenIndex = url?.indexOf("&token")
        if (tokenIndex != -1) {
            val prefix = tokenIndex?.let { url.substring(0, it) }
            val suffix = tokenIndex?.let { url.substring(it) }
            return "$prefix&from=$timestamp$suffix"
        }
        return url
    }

    fun getTimeStamp(dateString: String): Long {
        return DateUtilPlatform().getTimeStamp(dateString = dateString)
    }
}