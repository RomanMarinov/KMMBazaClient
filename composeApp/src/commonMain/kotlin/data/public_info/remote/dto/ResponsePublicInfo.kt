package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponsePublicInfo(
    val data: Data
)