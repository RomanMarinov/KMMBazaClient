package data.public_info.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarkerCityCam(
    @SerialName("additional")
    val additionalMap: AdditionalMap,
    val angle: Int,
    val latitude: Double,
    val longitude: Double,
    val title: String
)
