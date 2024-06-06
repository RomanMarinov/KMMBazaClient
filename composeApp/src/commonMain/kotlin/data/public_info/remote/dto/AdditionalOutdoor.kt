package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AdditionalOutdoor(
    val cameraName: String,
    val previewUrl: String
)