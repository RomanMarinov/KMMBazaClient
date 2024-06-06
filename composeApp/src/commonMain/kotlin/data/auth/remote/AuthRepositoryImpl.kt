package data.auth.remote

import co.touchlab.kermit.Logger
import data.auth.local.AppPreferencesRepository
import data.auth.remote.dto.AuthLoginBodyDTO
import data.auth.remote.dto.AuthLoginResponseDTO
import data.auth.remote.dto.FingerprintBodyDTO
import domain.model.auth.AuthLoginBody
import domain.model.auth.AuthLoginResponse
import domain.model.auth.AuthRefreshBody
import domain.model.auth.FingerprintBody
import domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Задаём прогрессивный интервал для отправки запросов авторизации в секундах
// (каждый последующий интервал увеличивается на 1 секунду)
private var START_REQUEST_INTERVAL = 0L
private var CURRENT_REQUEST_INTERVAL = 0L
private const val INCREASE_REQUEST_INTERVAL = 1000L
private var isNeededToResetIntervalCounters = false

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val appPreferencesRepository: AppPreferencesRepository
) : AuthRepository {

    override fun resetIntervalCounters(resetCounters: Boolean) {
        if (resetCounters) {
            isNeededToResetIntervalCounters = true
        }
    }

    override suspend fun logIn(authLoginBody: AuthLoginBody): HttpResponse? {
//        logManager.writeLogToDB("Отправляем запрос на авторизацию с номером телефона '${body.phone}'")

        val httpResponse: HttpResponse? = null
        try {
            while (true) {

                if (isNeededToResetIntervalCounters) {
                    START_REQUEST_INTERVAL = 0L
                    CURRENT_REQUEST_INTERVAL = 0L
                    isNeededToResetIntervalCounters = false
                }

                START_REQUEST_INTERVAL += CURRENT_REQUEST_INTERVAL

                delay(CURRENT_REQUEST_INTERVAL)

                val authLoginBodyDTO = AuthLoginBodyDTO(
                    phone = authLoginBody.phone,
                    fingerprint = authLoginBody.fingerprint
                )
                val response: HttpResponse = httpClient.post("auth/login") {
                    contentType(ContentType.Application.Json)
                    setBody(body = authLoginBodyDTO)
                }
//
                Logger.d { "4444 ViewPagerAuth response=" + httpResponse?.status?.value }

                CURRENT_REQUEST_INTERVAL += INCREASE_REQUEST_INTERVAL

                when (response.status.value) {
                    200 -> {
                        if (!response.status.isSuccess()) throw Exception("200 Response is not successful : AuthRepositoryImpl/login()")
                        //logManager.writeLogToDB("Успешная авторизация с номером телефона '${body.phone}'")
                        Logger.d { "4444 AUTH Успешная авторизация: AuthRepositoryImpl/login()" }
                        val responseBody = response.body<AuthLoginResponseDTO>()
                        val accessToken = responseBody.accessToken
                        val refreshToken = responseBody.refreshToken

                        appPreferencesRepository.setAuthToPrefsAndAuthState(
                            phone = authLoginBody.phone,
                            accessToken = accessToken ?: "",
                            refreshToken = refreshToken ?: "",
                            fingerprint = authLoginBody.fingerprint
                        )
                        return response
                    }

                    404 -> {
                        //logManager.writeLogToDB("Ошибка авторизации с номером телефона '${body.phone}'")
                        Logger.d { "4444 AUTH Не удалось авторизоваться: RepositoryImpl/login()" }
                        // Если вернулись на этот экран после набора номера
                        // и время ожидания 200-го ответа превысило 30 секунд,
                        // значит, ошибка связана с несоответствием номеров
                        // (или слишком долго думали)
                        if (CURRENT_REQUEST_INTERVAL > 7000L) {
                            appPreferencesRepository.removePhoneAndAccessTokenFromPrefs()
                            return response
                        }
                        continue
                    }
                    else -> {
//                logManager.writeLogToDB("Ошибка авторизации с номером телефона '${body.phone}'")
                        appPreferencesRepository.removePhoneAndAccessTokenFromPrefs()
                        return response
//                        continue
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            //logManager.writeLogToDB("Ошибка авторизации с номером телефона '${body.phone}'")
            //logManager.writeLogToDB(e.stackTraceToString())
            Logger.d { "4444 try catch AuthRepositoryImpl e=" + e }
            return httpResponse
        }
    }

    override suspend fun logOut(fingerprintBody: FingerprintBody): Boolean {
        try {
            val fingerprintBodyDTO = FingerprintBodyDTO(
                fingerprint = fingerprintBody.fingerprint
            )

//            logManager.writeLogToDB("Отправляем запрос на выход из аккаунта")
            val response = httpClient.post("auth/logout") {
                contentType(ContentType.Application.Json)
                setBody(body = fingerprintBodyDTO)
            }
            if (response.status.isSuccess()) {
                // если надо для возврата во viewmodel
              //  val res = response.body<AuthLogoutResponse>()
                appPreferencesRepository.removePhoneAndAccessTokenFromPrefs()
                appPreferencesRepository.removeRefreshTokenFromPrefsAndAuthState()
                Logger.d("4444 Успешно вышли из аккаунта")
                //logManager.writeLogToDB("Успешно вышли из аккаунта")
                return true
            } else {
                Logger.d("4444 AuthRepositoryImpl logOut not isSuccess")
               // logManager.writeLogToDB("Ошибка выхода из аккаунта")
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            //logManager.writeLogToDB("Ошибка выхода из аккаунта: ${e.javaClass.simpleName}")
            //logManager.writeLogToDB(e.stackTraceToString())
            return false
        }
    }
//
//    override fun refreshTokenSync(body: AuthRefreshBody): Response<AuthLoginResponse>? {
//        TODO("Not yet implemented")
//    }
//
    override suspend fun ipAuthorization(fingerprintBody: FingerprintBody): HttpResponse? {
        try {
            val fingerprintBodyDTO = FingerprintBodyDTO(
                fingerprint = fingerprintBody.fingerprint
            )

            val response = httpClient.post("auth/ip_authorization") {
                contentType(ContentType.Application.Json)
                setBody(body = fingerprintBodyDTO)
            }

            if (response.status.isSuccess()) {
                Logger.d{"4444 AuthRepositoryImpl ipAuthorization isSuccess"}
                //logManager.writeLogToDB("Успешно авторизовались по wifi")
                val resultDTO = response.body<AuthLoginResponseDTO>()
                val result = resultDTO.mapToDomain()

                val phone = result.payload?.phone ?: 0L
                val accessToken = result.accessToken ?: ""
                val refreshToken = result.refreshToken ?: ""

                appPreferencesRepository.setAuthToPrefsAndAuthState(
                    phone = phone,
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                    fingerprint = fingerprintBody.fingerprint
                )
                return response
            } else {
                Logger.d{"4444 AuthRepositoryImpl ipAuthorization not isSuccess"}
                //logManager.writeLogToDB("Ошибка wi-fi авторизации")
                appPreferencesRepository.removePhoneAndAccessTokenFromPrefs()
                return response
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Logger.d{"4444 try catch AuthRepositoryImpl ipAuthorization e=" + e}
            // logManager.writeLogToDB("Ошибка wi-fi авторизации")
            // logManager.writeLogToDB(e.stackTraceToString())
            return null
        }
    }

    override suspend fun getAccessTokenFromPrefs() : String {
        return appPreferencesRepository.fetchInitialPreferences().accessToken
    }


    override suspend fun refreshTokenSync(): HttpResponse? {
        Logger.d("4444 REFRESH Начинаем обновление токенов - RefreshManagerImpl/refreshTokenSync()")
        val refreshToken = appPreferencesRepository.fetchInitialPreferences().refreshToken
        val fingerPrint =  appPreferencesRepository.fetchInitialPreferences().fingerPrint
        val body = AuthRefreshBody(refreshToken, fingerPrint)

        val coroutineScope = CoroutineScope(Dispatchers.IO)
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
           // logManager.writeLogToDB("Отправляем запрос на обновление токенов: RepositoryImpl/refreshTokenSync()")
        }
        if (body.refreshToken.isEmpty() || body.fingerprint.isEmpty()) {
            Logger.d{ "4444 AUTH Не отправляем запрос на обновление токенов, т.к. данные в body пустые: RepositoryImpl/refreshTokenSync()" }
            scope.launch {
//                logManager.writeLogToDB("Не отправляем запрос на обновление токенов, т.к. данные в body пустые: RepositoryImpl/refreshTokenSync()")
            }
            return null
        }
        Logger.d{ "4444 AUTH Отправляем запрос на обновление токенов, body = $body - RepositoryImpl/refreshTokenSync()" }

        var finalResponse: HttpResponse? = null
        try {

            val job = coroutineScope.launch {

                val response = httpClient.post("auth/refresh_token") {
                    contentType(ContentType.Application.Json)
                    setBody(body = body)
                }

                val result = response.body<AuthLoginResponse>()

                finalResponse = response
                if (response.status.isSuccess()) {
                    scope.launch {
                        //logManager.writeLogToDB("Успешно обновили токены - RepositoryImpl/refreshTokenSync()")
                    }

                    val responsePhone = result.payload?.phone ?: 0L
                    val responseAccessToken = result.accessToken ?: ""
                    val responseRefreshToken = result.refreshToken ?: ""

                    appPreferencesRepository.setAuthToPrefsAndAuthState(
                        responsePhone,
                        responseAccessToken,
                        responseRefreshToken,
                        body.fingerprint
                    )

                    finalResponse = response
                    Logger.d{ "4444 AUTH Записали новые данные appAuth - RepositoryImpl/refreshTokenSync()" }
                    Logger.d{"4444 AUTH Успешно обновили токены - RepositoryImpl/refreshTokenSync()"}
                } else {
                    appPreferencesRepository.removePhoneAndAccessTokenFromPrefs()
                    scope.launch {
//                        logManager.writeLogToDB("Не удалось обновить токены - RepositoryImpl/refreshTokenSync()")
  //                      dao.clearTableSync()
    //                    userPaymentsDao.clearTableSync()
                    }
                    Logger.d{"4444 AUTH Не удалось обновить токены - RepositoryImpl/refreshTokenSync()"}
                }
            }
            job.start()
            delay(1000L)
            job.join()

            //if (thread.isInterrupted) return null
            val responseNull = if (finalResponse == null) "is null" else "is not null"
            Logger.d{"4444 AUTH Ответ сервера при обновлении токенов $responseNull - RepositoryImpl/refreshTokenSync()"}
            scope.launch {
               // logManager.writeLogToDB("Ответ сервера при обновлении токенов $responseNull - RepositoryImpl/refreshTokenSync()")
            }
            return finalResponse
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.d{"4444 AUTH Ошибка обновления токенов: ${e} - RepositoryImpl/refreshTokenSync()"}
            scope.launch {
               // logManager.writeLogToDB("Ошибка обновления токенов: ${e.javaClass.simpleName} - RepositoryImpl/refreshTokenSync()")
               // logManager.writeLogToDB(e.stackTraceToString())
            }
            return null
        }
    }
}


