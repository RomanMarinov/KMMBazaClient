package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Messengers(
    val telegram: Telegram,
    val vk: Vk,
    val whatsapp: Whatsapp
)