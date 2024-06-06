package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MarkerDomofon(
    val additional: AdditionalDomofon,
    val angle: Int,
    val latitude: Double,
    val longitude: Double,
    val title: String
)