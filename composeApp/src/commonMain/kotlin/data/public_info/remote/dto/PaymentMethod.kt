package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethod(
    val gateway: String,
    val subtitle: String,
    val svg_icon: String,
    val title: String
)