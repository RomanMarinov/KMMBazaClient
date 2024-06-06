package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val camerasAvailable: Boolean,
    val center: String,
    val leftTopLat: String,
    val leftTopLon: String,
    val location: String,
    val rightBottomLat: String,
    val rightBottomLon: String,
    val title: String,
    val titlePrefix: String
)