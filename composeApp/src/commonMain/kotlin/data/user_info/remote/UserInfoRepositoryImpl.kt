package data.user_info.remote

import co.touchlab.kermit.Logger
import domain.model.user_info.UserInfo
import domain.repository.UserInfoRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class UserInfoRepositoryImpl(private val httpClient: HttpClient) : UserInfoRepository {

    override suspend fun getUserInfo(): UserInfo {
        val response = httpClient.get("user/info")
        //Logger.d("4444 response=" + response)
        val result = response.body<UserInfo>()
     //   Logger.d("4444 result=" + result.data.additionalAddresses)
        return result
    }
}