package util

import co.touchlab.kermit.Logger
import io.ktor.utils.io.core.String
import io.ktor.utils.io.core.toByteArray
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.abs


const val TIMESTAMP_REGEX = "\"exp\":(\\d{10}),"
const val TIME_GAP_BEFORE_ACCESS_TOKEN_EXPIRE = 300

private val PHONE_NUMBER_PLUS_79 = "^\\+79[0-9]{9}\$".toRegex() // формат +79ххххххххх
private val PHONE_NUMBER_79 = "^79[0-9]{9}\$".toRegex() // формат +79ххххххххх
private val PHONE_NUMBER_89 = "^89[0-9]{9}\$".toRegex() // формат 89ххххххххх
private val PHONE_NUMBER_9 = "^9[0-9]{9}\$".toRegex() // формат 9ххххххххх

object TextUtils {
    fun isAccessTokenValid(accessToken: String?): Boolean {
        if (accessToken.isNullOrEmpty()) return true
        Logger.d("4444 isAccessTokenValid accessToken=" + accessToken)
        val expTimeSeconds = getExpTimeInSeconds(accessToken)
        Logger.d("4444 isAccessTokenValid expTimeSeconds=" + expTimeSeconds)
//        val currentTimeSeconds = System.currentTimeMillis() / 1000L
        val currentTimeSeconds = CurrentTimeMillisHelperPlatform().getTime() / 1000L
        val difference = expTimeSeconds - currentTimeSeconds
        val whole = difference / 60
        val fractional = abs(difference - (whole * 60))
        if (expTimeSeconds != 0L) {
            Logger.d(
                "4444 AUTH Токен ' .${getTokenSnippet(accessToken)} ' годен ещё = $whole:$fractional минут : TextUtils/isAccessTokenExpired()"
            )
        } else {
            Logger.d(
                "4444 AUTH Токен ' .${getTokenSnippet(accessToken)} ' годен ещё = ранее не был установлен : TextUtils/isAccessTokenExpired()"
            )
        }
        return expTimeSeconds > currentTimeSeconds // true if the token is still valid
    }

//    @OptIn(ExperimentalEncodingApi::class)
//    fun getExpTimeInSeconds(accessToken: String?): Long {
//
//        if (accessToken.isNullOrEmpty()) return 0L
//
//        val decoded = String(Base64.decode(accessToken.toByteArray()))
//        val regex = Regex(TIMESTAMP_REGEX)
//        val matchResult = regex.find(decoded)
//        if (matchResult != null) {
//            return matchResult.groupValues[1].toLongOrNull() ?: 0L
//        }
//        return 0L
//    }

    @OptIn(ExperimentalEncodingApi::class)
    fun getExpTimeInSeconds(accessToken: String?): Long {
        if (accessToken.isNullOrEmpty()) return 0L

        // Разделяем токен на части
        val parts = accessToken.split(".")
        if (parts.size != 3) return 0L // JWT должен состоять из трех частей

        // Декодируем вторую часть (payload)
        val decoded = String(Base64.decode(parts[1].toByteArray()))
        val regex = Regex(TIMESTAMP_REGEX)
        val matchResult = regex.find(decoded)
        if (matchResult != null) {
            return matchResult.groupValues[1].toLongOrNull() ?: 0L
        }
        return 0L
    }

    private fun getTokenSnippet(token: String?): String {
        if (token.isNullOrEmpty()) return ""
        val regex = (".+\\..+\\.").toRegex()
        val shorterString = token.replace(regex, "")
        shorterString.trim()
        return shorterString.substring(0, 4)
    }

    fun isItTimeToUpdateToken(accessToken: String?): Boolean {
        val expTimeSeconds = getExpTimeInSeconds(accessToken)
//        val currentTimeSeconds = System.currentTimeMillis() / 1000L
        val currentTimeSeconds = CurrentTimeMillisHelperPlatform().getTime() / 1000L
        val remainingTimeSeconds = expTimeSeconds - currentTimeSeconds
        return remainingTimeSeconds <= TIME_GAP_BEFORE_ACCESS_TOKEN_EXPIRE
    }

    fun getPhoneAsFormattedString(phone: Long): String {
        val phoneString = phone.toString()
        return if (phoneString.matches(PHONE_NUMBER_9)) {
            "+7 (${phoneString.substring(0, 3)}) ${phoneString.substring(3, 6)} ${phoneString.substring(6, 8)} ${phoneString.substring(8)}"
        } else phoneString
    }

    fun getCorrectSupportCallNumber(phone: String?): String {
        Logger.d { "4444 phone=" + phone }
        var supportCallNumber = ""
        phone?.let {
            supportCallNumber = if (it.isNotEmpty()) {
                "${it.substring(0, 1)}-" +
                        "${it.substring(1, 4)}-" +
                        "${it.substring(4, 8)}-" +
                        "${it.substring(8)}"
            } else {
                "8-800-1000-249"
            }
        } ?: run {
            supportCallNumber = "8-800-1000-249"
        }
        return supportCallNumber
    }

//    suspend fun bodyToString(request: HttpRequestData): String? {
//        return try {
//            val copy: HttpRequestBuilder = request.
//            val content: OutgoingContent = copy.body as OutgoingContent
//            val buffer = BytePacketBuilder()
//            content.writeTo(buffer)
//            buffer.build().readText()
//        } catch (e: IOException) {
//            "bodyToString() did not work"
//        }
//    }
}