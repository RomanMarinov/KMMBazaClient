package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MarkerOutdoor(
    val additional: AdditionalOutdoor,
    val angle: Int,
    val latitude: Double,
    val longitude: Double,
    val title: String
)