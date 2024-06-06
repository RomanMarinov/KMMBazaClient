package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Whatsapp(
    val iosId: Int,
    val iosUrl: String,
    val webUrl: String
)