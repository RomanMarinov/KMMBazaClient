package domain.model.home.locations_internet_tv

import kotlinx.serialization.Serializable

@Serializable
data class ResponseLocationsInternetTv(
    val data: List<DataLocations>
)