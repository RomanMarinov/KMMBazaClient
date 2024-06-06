package data.public_info.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityCams(
    val count: Int,
    @SerialName("markers")
    val markerCityCams: List<MarkerCityCam>,
    val title: String
)