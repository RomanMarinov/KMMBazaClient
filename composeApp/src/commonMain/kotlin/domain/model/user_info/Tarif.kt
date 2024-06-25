package domain.model.user_info

import kotlinx.serialization.Serializable

@Serializable
data class Tarif(
    val bitrate: Int? = null,
    val canChange: Boolean,
    val group: String,
    val id: Int,
    val name: String,
    val next: Int,
   // @SerialName("tariff_change_options")
    val tariffChangeOptions: List<TariffChangeOption>? = null
)