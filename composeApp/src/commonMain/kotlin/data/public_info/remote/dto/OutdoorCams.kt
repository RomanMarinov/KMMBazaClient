package data.public_info.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OutdoorCams(
    val count: Int,
    @SerialName("markers")
    val markersOutdoors: List<MarkerOutdoor>,
    val title: String
)