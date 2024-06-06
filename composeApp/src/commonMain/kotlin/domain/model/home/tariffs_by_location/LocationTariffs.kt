package domain.model.home.tariffs_by_location

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationTariffs(
    @SerialName("data")
    val data: List<DataTariffs>
)
