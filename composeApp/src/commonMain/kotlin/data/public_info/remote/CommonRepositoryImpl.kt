package data.public_info.remote


import data.public_info.remote.dto.Data
import data.public_info.remote.dto.ResponsePublicInfo
import domain.repository.CommonRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class CommonRepositoryImpl(
    private val httpClient: HttpClient
) : CommonRepository {

    override suspend fun getPublicInfo(): Data {
        val response: HttpResponse = httpClient.get("public/info")
        val res = response.body<ResponsePublicInfo>().data
        return res
    }
}