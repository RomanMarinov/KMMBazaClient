package di.network_module

import data.auth.local.AppPreferencesRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    co.touchlab.kermit.Logger.d("4444 networkModule")
    single<HttpClient> {
        HttpClient() {

            val appPreferencesRepository: AppPreferencesRepository = get()
            install(KtorAuthInterceptor(appPreferencesRepository = appPreferencesRepository))

            install(ResponseObserver) {
                onResponse { response ->
                    //co.touchlab.kermit.Logger.d {" 4444 networkModule ResponseObserver HTTP onResponse= =$response.status.value" }
                }
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        //co.touchlab.kermit.Logger.d {" 4444 networkModule httpClient =$message" }
                    }
                }
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(Json {
                    //explicitNulls = false
                    // при десериализации JSON данные не будут потеряны из-за наличия дополнительных полей
                    ignoreUnknownKeys = true
                    // Результирующий JSON будет содержать отступы, переносы строк и пробелы для улучшения его визуального восприятия.
                    prettyPrint = true
                    // игнорировать некоторые ошибки формата JSON и пытаться продолжить разбор, вместо выбрасывания исключения.
                    // isLenient = true
                })
            }

            install(HttpTimeout) {
                socketTimeoutMillis = 100000
                requestTimeoutMillis = 100000
                connectTimeoutMillis = 100000
            }

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.baza.net"
                }
            }
        }
    }
}


//Метод proceedWith(response) в контексте использования в Ktor HttpClient означает,
// что после выполнения операций, указанных в блоке перехвата,
// происходит продолжение обработки запроса с переданным ответом.

//proceed(): Этот метод не принимает аргументов и просто позволяет продолжить выполнение запроса без изменений.
// Используется, когда необходимо выполнить обычный запрос без изменений.
//
//finish(): Этот метод завершает обработку запроса раньше времени и не передает управление дальше.
// Может использоваться, например, для предотвращения выполнения остальных обработчиков в цепочке.
//
//finishWith(response): Этот метод завершает обработку, возвращая заданный ответ.
// Может использоваться для возврата собственного ответа вместо оригинального.
//
//abort(): Этот метод предотвращает выполнение дальнейших обработчиков в цепочке и прерывает текущее выполнение запроса.