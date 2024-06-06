package data.public_info.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DomofonCams(
    val count: Int,
    @SerialName("markers")
    val markersDomofon: List<MarkerDomofon>,
    val title: String
)