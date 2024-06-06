package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AdditionalMap(
    val albumId: Int,
    val cameraName: String,
    val dtpCounts: Int,
    val location: String,
    val locationTitle: String,
    val previewUrl: String,
    val server: String,
    val token: String,
    val videoUrl: String
)