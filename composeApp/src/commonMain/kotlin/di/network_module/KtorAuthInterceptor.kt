package di.network_module

import co.touchlab.kermit.Logger
import data.data_store.AppPreferencesRepository
import domain.model.auth.AuthLoginResponse
import domain.model.auth.AuthRefreshBody
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.content.OutgoingContent
import io.ktor.http.content.TextContent
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.withCharset
import io.ktor.util.AttributeKey
import io.ktor.utils.io.charsets.Charsets
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import util.TextUtils

private const val LOGOUT_PREFIX = "auth/logout"
private const val USER_PREFIX = "user/"
private const val VERIFICATION_PREFIX = "verification/"
private const val DOMOFON_PREFIX = "domofon/"
private const val CONNECTION_REQUEST_PREFIX = "/connection_request"

class KtorAuthInterceptor(
    private val appPreferencesRepository: AppPreferencesRepository
) : HttpClientPlugin<ResponseObserver.Config, KtorAuthInterceptor> {

    override val key: AttributeKey<KtorAuthInterceptor> = AttributeKey("NewAuthInterceptor")

    override fun install(plugin: KtorAuthInterceptor, scope: HttpClient) {
        scope.requestPipeline.intercept(HttpRequestPipeline.Render) { response ->
            Logger.d("4444 KtorAuthInterceptor  install context.url=" + context.url.build())
            if (context.url.toString().contains(USER_PREFIX, true)
                || context.url.toString().contains(LOGOUT_PREFIX, true)
                || context.url.toString().contains(VERIFICATION_PREFIX, true)
                || context.url.toString().contains(DOMOFON_PREFIX, true)
                || context.url.toString().contains(CONNECTION_REQUEST_PREFIX, true)) {
                // обновляем токены, если accessToken протух
                val isItTimeToRefreshToken = TextUtils.isItTimeToUpdateToken(
                    appPreferencesRepository.fetchInitialPreferences().accessToken
                )
                val accessToken = if (isItTimeToRefreshToken) {
                    val resp = refreshNow(appPreferencesRepository, scope)
                    val res = resp?.body<AuthLoginResponse>()
                    res?.accessToken ?: ""
                } else {
                    appPreferencesRepository.fetchInitialPreferences().accessToken
                }

                context.headers.append(HttpHeaders.Authorization, "Bearer $accessToken")
//                val responseWithHeader = scope.request {
//                    header(HttpHeaders.Authorization, "Bearer $accessToken")
//                }


                // продолжение обработки запроса с новым ответом.
                proceedWith(response)
//                if (accessToken.isNotEmpty()) {
//                    context.headers.append(HttpHeaders.Authorization, "Bearer $accessToken")
//                    proceed()
//                }
            } else {
                // продолжение обработки запроса с текущим ответом.
                proceedWith(response)
            }
        }
//
//        scope.responsePipeline.intercept(HttpResponsePipeline.After) { response ->
//            if (context.response.status.value == HttpStatusCode.Unauthorized.value) {
//                Logger.d("4444  qwe NewAuthInterceptor HttpResponsePipeline.After context.request.url=" + context.request.url + " code=" + context.response.status.value)
//                // preferencesDataStoreRepositoryProvider.get().saveStateUnauthorized(isUnauthorized = true)
//                finish()
//            } else {
//                Logger.d("4444  qwe NewAuthInterceptor some 1 response=" + context.request.url + " code=" + context.response.status.value)
//                proceed()
//            }
//            Logger.d("4444 qwe NewAuthInterceptor some 2 response=" + context.request.url + " code=" + context.response.status.value)
//        }

    }

    override fun prepare(block: ResponseObserver.Config.() -> Unit): KtorAuthInterceptor {
        return this
    }

    private suspend fun refreshNow(
        appPreferencesRepository: AppPreferencesRepository,
        scope: HttpClient
    ): HttpResponse? {
        Logger.d("REFRESH Начинаем обновление токенов - NetworkModule/refreshNow()")
        val refreshToken = appPreferencesRepository.fetchInitialPreferences().refreshToken
        val fingerprint = appPreferencesRepository.fetchInitialPreferences().fingerPrint
        val body = AuthRefreshBody(refreshToken, fingerprint)
        val requestBodyJson = Json.encodeToString(body)
        return try {
             val requestBody: OutgoingContent = TextContent(
                 text = requestBodyJson,
                 contentType = ContentType.Application.Json.withCharset(Charsets.UTF_8)
             )
           // val requestBody: RequestBody = requestBodyJson.toRequestBody("application/json".toMediaTypeOrNull())

            val response = scope.post("auth/refresh_token") {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
//            val response = scope.post("auth/refresh_token") {
//                //contentType(ContentType.Application.Json)
//                setBody(requestBody)
//            }
            if (response.status.isSuccess()) {
                val result = response.body<AuthLoginResponse>()
                //Logger.d("4444 refreshNow isSuccess result= ${result}")
                appPreferencesRepository.setAuthToPrefsAndAuthState(
                    phone = result.payload?.phone ?: 0L,
                    accessToken = result.accessToken ?: "",
                    refreshToken = result.refreshToken ?: "",
                    fingerprint = fingerprint
                )
                response
            } else {
                // Logger.d("REFRESH Ошибка обновления токенов. response body = ${response.peekBody(Long.MAX_VALUE).string()}")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /////////////////////////////////////////////////

//    private suspend fun refreshNow(
//        appPreferencesRepository: AppPreferencesRepository,
//        scope: HttpClient
//    ): HttpResponse? {
//        //val gson = Gson()
//
//        Logger.d("REFRESH Начинаем обновление токенов - NetworkModule/refreshNow()")
//        val refreshToken = appPreferencesRepository.fetchInitialPreferences().refreshToken
//        val fingerprint = appPreferencesRepository.fetchInitialPreferences().fingerPrint
//        val body = AuthRefreshBody(refreshToken, fingerprint)
////        val requestBodyJson = gson.toJson(body)
//        val requestBodyJson = Json.encodeToString(body)
////        val requestBody: OutgoingContent = TextContent(
////            text = requestBodyJson,
////            contentType = ContentType.Application.Json.withCharset(Charsets.UTF_8)
////        )
//        //  val requestBody: RequestBody = requestBodyJson.toRequestBody("application/json".toMediaTypeOrNull())
//
////        val request: HttpRequestData = HttpRequestBuilder().apply {
////            url {
////                takeFrom("https://api.baza.net/auth/refresh_token")
////            }
////            method = HttpMethod.Post
////            contentType(ContentType.Application.Json)
////            setBody(requestBody)
////        }.build()
//
//        return try {
//           // val requestBodyJson = Json.encodeToString(body)
//            val response = scope.post("auth/refresh_token") {
//                contentType(ContentType.Application.Json)
//                setBody(requestBodyJson)
//            }
//            if (response.status.isSuccess()) {
//                val result = response.body<AuthLoginResponse>()
//                Logger.d("4444 refreshNow isSuccess result= ${result}")
//                appPreferencesRepository.setAuthToPrefsAndAuthState(
//                    phone = result.payload?.phone ?: 0L,
//                    accessToken = result.accessToken ?: "",
//                    refreshToken = result.refreshToken ?: "",
//                    fingerprint = fingerprint
//                )
//                response
//            } else {
//                // Logger.d("REFRESH Ошибка обновления токенов. response body = ${response.peekBody(Long.MAX_VALUE).string()}")
//                null
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
/////////////////////////////////////////////////////////////////////////////

//    private fun refreshNow2(appPreferencesRepository: AppPreferencesRepository): AuthLoginResponse? {
//        val gson = Gson()
//        Logger.d("REFRESH Начинаем обновление токенов - NetworkModule/refreshNow()")
//        val refreshToken = appAuth.getRefreshTokenFromPrefs() ?: ""
//        val fingerprint = appAuth.getFingerprintFromPrefs() ?: ""
//        val body = AuthRefreshBody(refreshToken, fingerprint)
//        val requestBodyJson = gson.toJson(body)
//        Log.i("REFRESH","requestBody = $requestBodyJson - NetworkModule/refreshNow()")
//        val requestBody: RequestBody = requestBodyJson.toRequestBody("application/json".toMediaTypeOrNull())
//
//        val request: Request = Request.Builder()
//            .url(BuildConfig.BASE_URL + AUTH_REFRESH_TOKEN)
//            .post(requestBody)
//            .build()
//        Log.i("REFRESH","request url = ${request.url} - NetworkModule/refreshNow()")
//        Log.i("REFRESH","request body = ${TextUtils.bodyToString(request)} - NetworkModule/refreshNow()")
//        return try {
//            val call: Call = OkHttpClient().newCall(request)
//            val response: Response = call.execute()
//            if (response.isSuccessful) {
//                val responseBody = response.body?.string()
//                Log.i("REFRESH","Успешно обновили токены. response body = $responseBody")
//                response.body?.close()
//                val resp = gson.fromJson(responseBody, AuthLoginResponse::class.java)
//                Log.i("REFRESH","AuthLoginResponse = $resp")
//                appAuth.setAuthToPrefsAndAuthState(
//                    phone = resp.payload.phone,
//                    accessToken = resp.accessToken,
//                    refreshToken = resp.refreshToken,
//                    fingerprint = fingerprint
//                )
//                resp
//            } else {
//                Log.e("REFRESH","Ошибка обновления токенов. response body = ${response.peekBody(Long.MAX_VALUE).string()}")
//                null
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
}


//class KtorAuthInterceptor2(
//    private val authRepositoryProvider: Provider<AuthRepository>,
//    private val preferencesDataStoreRepositoryProvider: Provider<PreferencesDataStoreRepository>
//) : HttpClientPlugin<ResponseObserver.Config, KtorAuthInterceptor> {
//
//    override val key: AttributeKey<KtorAuthInterceptor> = AttributeKey("NewAuthInterceptor")
//
//    override fun install(plugin: KtorAuthInterceptor, scope: HttpClient) {
//        scope.requestPipeline.intercept(HttpRequestPipeline.Render) { response ->
//
//            Log.d("4444", " qwe NewAuthInterceptor res=" + context.url.build())
//            if (context.url.toString().contains(net.baza.bazanetclientapp.Constants.PART_URL_MESSAGES)) {
//                val refreshToken = authRepositoryProvider.get().getRefreshTokensFromDataStore.first()
//                val accessToken = authRepositoryProvider.get().getAccessTokensFromDataStore.first()
//                if (accessToken.isNotEmpty()) {
//                    // val accessToken = "wefwefwefwef"
//                    context.headers.append(HttpHeaders.Authorization, "Bearer $accessToken")
//                }
//            }
//        }
//
//        scope.responsePipeline.intercept(HttpResponsePipeline.After) { response ->
//            if (context.response.status.value == HttpStatusCode.Unauthorized.value) {
//                Log.d("4444", " qwe NewAuthInterceptor Unauthorized response=" + context.request.url + " code=" + context.response.status.value)
//                preferencesDataStoreRepositoryProvider.get().saveStateUnauthorized(isUnauthorized = true)
//                finish()
//            } else {
//                Log.d("4444", " qwe NewAuthInterceptor some 1 response=" + context.request.url + " code=" + context.response.status.value)
//                proceed()
//            }
//            Log.d("4444", " qwe NewAuthInterceptor some 2 response=" + context.request.url + " code=" + context.response.status.value)
//        }
//    }
//
//    override fun prepare(block: ResponseObserver.Config.() -> Unit): KtorAuthInterceptor {
//        return this
//    }
//}