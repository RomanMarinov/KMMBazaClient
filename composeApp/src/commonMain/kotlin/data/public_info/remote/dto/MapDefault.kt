package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MapDefault(
    val cityName: String,
    val gpsLat: Double,
    val gpsLon: Double
)