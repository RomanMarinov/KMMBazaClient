package domain.model.user_info

import kotlinx.serialization.Serializable

@Serializable
data class TariffChangeOption(
    val bitrate: Int,
    val name: String,
    val price: Int,
    val tarifId: Int
)