package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MobileTariff(
    val gbytes: Int,
    val minutes: Int,
    val name: String,
    val price: Int,
    val sms: Int
)