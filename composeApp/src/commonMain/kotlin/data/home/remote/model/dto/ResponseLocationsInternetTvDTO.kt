package data.home.remote.model.dto

import domain.model.home.locations_internet_tv.ResponseLocationsInternetTv
import kotlinx.serialization.Serializable

@Serializable
data class ResponseLocationsInternetTvDTO(
    val data: List<DataLocationsDTO>
) {
    fun mapToDomain() : ResponseLocationsInternetTv {
        return ResponseLocationsInternetTv(
            data = data.map { it.mapToDomain() }
        )
    }
}