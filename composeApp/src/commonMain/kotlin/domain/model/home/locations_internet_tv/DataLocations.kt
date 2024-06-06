package domain.model.home.locations_internet_tv

import kotlinx.serialization.Serializable

@Serializable
data class DataLocations(
    val id: Int,
    val name: String,
    val oper: String,
    val tarifGroup: String,
    val inet: Boolean,
    val ktv: Boolean
)