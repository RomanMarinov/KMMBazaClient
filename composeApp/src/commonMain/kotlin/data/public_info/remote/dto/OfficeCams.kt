package data.public_info.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OfficeCams(
    val count: Int,
    @SerialName("markers")
    val markersOffice: List<MarkerOffice>,
    val title: String
)