package data.home.remote.model.dto

import domain.model.home.tariffs_by_location.LocationTariffs
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationTariffsDTO(
    @SerialName("data")
    val data: List<DataTariffsDTO>
) {
    fun mapToDomain() : LocationTariffs {
        return LocationTariffs(
            data = data.map { it.mapToDomain() }
        )
    }
}
