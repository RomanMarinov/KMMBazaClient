package data.history_call.remote

import co.touchlab.kermit.Logger
import data.history_call.remote.model.dto.HistoryCallResponseDTO
import domain.model.history_call.HistoryCallAddress
import domain.repository.HistoryCallRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class HistoryCallRepositoryImpl(
    private val httpClient: HttpClient
) : HistoryCallRepository {

    override suspend fun getHistoryCall(): List<HistoryCallAddress>? {
//        logManager.writeLogToDB("Отправляем запрос на получение списка пропущенных звонков")
        try {
            val response = httpClient.get("domofon/call_history") {
                contentType(ContentType.Application.Json)
            }
            return if (response.status.isSuccess()) {
//                logManager.writeLogToDB("Успешно получили список пропущенных звонков")
                val result = response.body<HistoryCallResponseDTO>()
                result.mapToDomain().data
            } else {
                //logManager.writeLogToDB("Ошибка получения списка пропущенных звонков")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.d("4444 try catch getHistoryCall e=" + e)
            // logManager.writeLogToDB("Ошибка получения списка пропущенных звонков: ${e.javaClass.simpleName}")
            //logManager.writeLogToDB(e.stackTraceToString())
            return null
        }
    }
}