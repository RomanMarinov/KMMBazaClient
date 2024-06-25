package data.user_info.remote

import co.touchlab.kermit.Logger
import data.auth.remote.dto.firebase.OurServerDTO
import domain.model.auth.firebase.FirebaseRequestBody
import domain.model.auth.firebase.FirebaseRequestBodyTEST
import domain.model.user_info.UserInfo
import domain.repository.UserInfoRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class UserInfoRepositoryImpl(private val httpClient: HttpClient) : UserInfoRepository {

    override suspend fun getUserInfo(): UserInfo {
        val response = httpClient.get("user/info")
        //Logger.d("4444 response=" + response)
        val result = response.body<UserInfo>()
        //   Logger.d("4444 result=" + result.data.additionalAddresses)
        return result
    }

    override suspend fun sendSelfPush(body: FirebaseRequestBodyTEST) {
        val firebaseRequestBodyDTO = body.mapToData()
        try {

           // Logger.d("4444 sendSelfPush body=" + firebaseRequestBodyDTO)
            val response = httpClient.post("user/firebase_v1") {
                contentType(ContentType.Application.Json)
                setBody(body = firebaseRequestBodyDTO)
            }

            if (response.status.isSuccess()) {
                Logger.d { "4444 sendSelfPush isSuccess" }
                //            val result = response.body<OurServerDTO>()
//            if (result.data.result) {
//                Logger.d{"4444 sendRegisterFireBaseData isSuccess result=" + result.data.result}
//            } else {
//                Logger.d{"4444 sendRegisterFireBaseData isSuccess result=" + result.data.result}
//            }
            } else {
                Logger.d { "4444 sendSelfPush Failure" }
            }
        } catch (e: Exception) {
            Logger.d { "4444 try catch sendSelfPush e=" + e }
        }
    }
}