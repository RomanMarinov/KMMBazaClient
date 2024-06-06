package data.home.remote

import data.home.remote.model.dto.LocationTariffsDTO
import data.home.remote.model.dto.LocationsTariffsBodyDTO
import data.home.remote.model.dto.ResponseLocationsInternetTvDTO
import domain.model.home.locations_internet_tv.DataLocations
import domain.model.home.tariffs_by_location.LocationTariffs
import domain.model.home.tariffs_by_location.LocationsTariffsBody
import domain.repository.HomeRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRepositoryImpl(
    private val httpClient: HttpClient,
) : HomeRepository {
    override suspend fun getLocationsInternetTv(): Flow<List<DataLocations>> = flow {
        val response = httpClient.get("public/locations")
        val res = response.body<ResponseLocationsInternetTvDTO>()
        val locations = res.data.map {
            it.mapToDomain()
        }
        emit(locations)
    }

    override suspend fun getTariffsByLocation(body: LocationsTariffsBody): LocationTariffs {
        try {
            val bodyDTO: LocationsTariffsBodyDTO = body.mapToData()
            val response = httpClient.post("public/location_tariffs") {
                contentType(ContentType.Application.Json)
                setBody(body = bodyDTO)
            }
            val res = response.body<LocationTariffsDTO>()

            return res.mapToDomain()
        } catch (e: Exception) {
            // Обработка исключения, например, логирование или выброс другого исключения
            throw YourCustomException("Ошибка при запросе тарифов по местоположению: ${e.message}")
        }
    }
}

class YourCustomException(message: String) : Exception(message) {

}