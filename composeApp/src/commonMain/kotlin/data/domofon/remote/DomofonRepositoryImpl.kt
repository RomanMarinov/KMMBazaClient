package data.domofon.remote

import co.touchlab.kermit.Logger
import data.domofon.remote.model.dto.unlock.DomofonUnlockResponseDTO
import domain.model.domofon.unlock.DomofonUnlockResponse
import domain.repository.DomofonRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class DomofonRepositoryImpl(
    private val httpClient: HttpClient,
) : DomofonRepository {


    override suspend fun sendOpenDomofon(deviceId: String): DomofonUnlockResponse? {
        Logger.d("4444  sendOpenDomofon deviceID = $deviceId")
       // logManager.writeLogToDB("Отправляем запрос на открытие двери домофона")
        try {
            val response = httpClient.post("domofon/open_door/$deviceId") {
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
//                logManager.writeLogToDB("Успешно открыли дверь домофона с deviceId = '$deviceID'")
               val result = response.body<DomofonUnlockResponseDTO>()
                return result.mapToDomain()
            } else {
//                logManager.writeLogToDB("Ошибка открытия двери домофона с deviceId = '$deviceID'")
                return null
            }
        } catch (e: Exception) {
            e.printStackTrace()
           // logManager.writeLogToDB("Ошибка открытия двери домофона с deviceId = '$deviceID': ${e.javaClass.simpleName}")
           // logManager.writeLogToDB(e.stackTraceToString())
            return null
        }
    }
}



