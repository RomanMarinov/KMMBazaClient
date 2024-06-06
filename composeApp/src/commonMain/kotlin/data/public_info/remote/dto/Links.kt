package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Links(
    val businessDVR: String,
    val outdoorDVR: String,
    val personalDataTerms: String
)